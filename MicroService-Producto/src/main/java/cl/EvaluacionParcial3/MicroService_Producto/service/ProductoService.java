package cl.EvaluacionParcial3.MicroService_Producto.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.EvaluacionParcial3.MicroService_Producto.dto.ProductoRequestDTO;
import cl.EvaluacionParcial3.MicroService_Producto.dto.ProductoResponseDTO;
import cl.EvaluacionParcial3.MicroService_Producto.exceptions.CategoriaNotFoundException;
import cl.EvaluacionParcial3.MicroService_Producto.exceptions.ProductoNotFoundException;
import cl.EvaluacionParcial3.MicroService_Producto.model.Producto;
import cl.EvaluacionParcial3.MicroService_Producto.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final WebClient webClient;

    @Value("${microservicios.categorias.url}")
    private String categoriasUrl;

    public ProductoService(ProductoRepository productoRepository, WebClient.Builder webClientBuilder) {
        this.productoRepository = productoRepository;
        this.webClient = webClientBuilder.build();
    }

    // Obtiene todos los productos registrados

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarTodos() {
        log.info("Listando todos los productos");
        List<Producto> productos = productoRepository.findAll();
        log.debug("Se encontraron {} productos en total", productos.size());
        return productos.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtiene un producto por su ID

    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró producto con ID: {}", id);
                    return new ProductoNotFoundException(id);
                });
        log.debug("Producto encontrado: {}", producto.getNombre());
        return convertirAResponseDTO(producto);
    }

    // Lista todos los productos activos

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarActivos() {
        log.info("Listando productos activos");
        List<Producto> productos = productoRepository.findByActivoTrue();
        log.debug("Se encontraron {} productos activos", productos.size());
        return productos.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Lista productos por categoria

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarPorCategoria(Long categoriaId) {
        log.info("Listando productos de la categoría ID: {}", categoriaId);
        validarCategoriaExiste(categoriaId);
        List<Producto> productos = productoRepository.findByCategoriaIdAndActivoTrue(categoriaId);
        log.debug("Se encontraron {} productos en la categoría {}", productos.size(), categoriaId);
        return productos.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Busca prudctos por nombre

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarPorNombre(String nombre) {
        log.info("Buscando productos con nombre que contenga: {}", nombre);
        List<Producto> productos = productoRepository.findByNombreContainingIgnoreCase(nombre);
        log.debug("Se encontraron {} productos con nombre '{}'", productos.size(), nombre);
        return productos.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Crea nuevo producto

    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        log.info("Creando nuevo producto: {}", dto.getNombre());

        // Regla: validar que la categoría existe en ms-categorias
        validarCategoriaExiste(dto.getCategoriaId());

        Producto producto = Producto.builder()
                .nombre(dto.getNombre().trim())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .categoriaId(dto.getCategoriaId())
                .activo(true)
                .fechaCreacion(LocalDate.now())
                .build();

        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado exitosamente con ID: {}", guardado.getId());
        return convertirAResponseDTO(guardado);
    }

    // Actualiza datos de un producto existente

    @Transactional
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        log.info("Actualizando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede actualizar: producto con ID {} no encontrado", id);
                    return new ProductoNotFoundException(id);
                });

        if (!producto.getCategoriaId().equals(dto.getCategoriaId())) {
            log.info("Categoría cambió de {} a {}, validando...", producto.getCategoriaId(), dto.getCategoriaId());
            validarCategoriaExiste(dto.getCategoriaId());
        }

        producto.setNombre(dto.getNombre().trim());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setCategoriaId(dto.getCategoriaId());

        Producto actualizado = productoRepository.save(producto);
        log.info("Producto actualizado exitosamente: ID {}", actualizado.getId());
        return convertirAResponseDTO(actualizado);
    }

    // Eliminar producto

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando producto con ID: {}", id);

        if (!productoRepository.existsById(id)) {
            log.warn("No se puede eliminar: producto con ID {} no encontrado", id);
            throw new ProductoNotFoundException(id);
        }

        productoRepository.deleteById(id);
        log.info("Producto eliminado exitosamente: ID {}", id);
    }

    // Desactivar un producto

    @Transactional
    public ProductoResponseDTO desactivar(Long id) {
        log.info("Desactivando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede desactivar: producto con ID {} no encontrado", id);
                    return new ProductoNotFoundException(id);
                });

        producto.setActivo(false);
        Producto desactivado = productoRepository.save(producto);
        log.info("Producto desactivado exitosamente: ID {}", desactivado.getId());
        return convertirAResponseDTO(desactivado);
    }

    // Activa producto desactivado

    @Transactional
    public ProductoResponseDTO activar(Long id) {
        log.info("Activando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede activar: producto con ID {} no encontrado", id);
                    return new ProductoNotFoundException(id);
                });

        producto.setActivo(true);
        Producto activado = productoRepository.save(producto);
        log.info("Producto activado exitosamente: ID {}", activado.getId());
        return convertirAResponseDTO(activado);
    }

    // Valida que una categoria exista en categorias mediante webclient

    private void validarCategoriaExiste(Long categoriaId) {
        log.info("Validando existencia de categoría ID: {} en ms-categorias", categoriaId);
        try {
            webClient.get()
                    .uri(categoriasUrl + "/api/categorias/{id}", categoriaId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.debug("Categoría ID {} validada correctamente", categoriaId);
        } catch (WebClientResponseException.NotFound e) {
            log.warn("Categoría ID {} no encontrada en ms-categorias", categoriaId);
            throw new CategoriaNotFoundException(categoriaId);
        } catch (Exception e) {
            log.error("Error al conectar con ms-categorias: {}", e.getMessage());
            throw new RuntimeException("No se pudo conectar con el servicio de categorías. Intente más tarde.");
        }
    }
    // Convierte una entidad producto en su dto de respuesta

    private ProductoResponseDTO convertirAResponseDTO(Producto producto) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .categoriaId(producto.getCategoriaId())
                .activo(producto.getActivo())
                .fechaCreacion(producto.getFechaCreacion())
                .build();
    }
}

