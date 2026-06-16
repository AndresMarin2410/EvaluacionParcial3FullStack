package cl.EvaluacionParcial3.MicroService_Carrito.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.EvaluacionParcial3.MicroService_Carrito.client.ProductoClient;
import cl.EvaluacionParcial3.MicroService_Carrito.dto.CarritoRequest;
import cl.EvaluacionParcial3.MicroService_Carrito.dto.CarritoResponse;
import cl.EvaluacionParcial3.MicroService_Carrito.dto.ProductoResponse;
import cl.EvaluacionParcial3.MicroService_Carrito.mapper.CarritoMapper;
import cl.EvaluacionParcial3.MicroService_Carrito.model.Carrito;
import cl.EvaluacionParcial3.MicroService_Carrito.model.EstadoCarrito;
import cl.EvaluacionParcial3.MicroService_Carrito.model.ItemCarrito;
import cl.EvaluacionParcial3.MicroService_Carrito.repository.CarritoRepository;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoClient productoClient;

    @Transactional
    public CarritoResponse agregarProducto(CarritoRequest request) {
        // busca si el usuario ya tiene un carrito ACTIVO
        Carrito carrito = carritoRepository.findByIdUsuarioAndEstado(request.getIdUsuario(), EstadoCarrito.ACTIVO)
                .orElseGet(() -> {
                    // si no tiene, creamos uno nuevo
                    Carrito nuevo = new Carrito();
                    nuevo.setIdUsuario(request.getIdUsuario());
                    nuevo.setFechaCreacion(LocalDateTime.now());
                    nuevo.setEstado(EstadoCarrito.ACTIVO);
                    nuevo.setItems(new ArrayList<>());
                    return nuevo;
                });

        // logica de items
        Optional<ItemCarrito> itemExistente = carrito.getItems().stream()
                .filter(item -> item.getProductoId().equals(request.getProductoId()))
                .findFirst();

        if (itemExistente.isPresent()) {
            // si ya existe, sumamos la cantidad
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + request.getCantidad());
        } else {
            // consultamos el precio real al ms-productos
            ProductoResponse producto = productoClient.obtenerProductoPorId(request.getProductoId());

            // si es nuevo, creamos el ítem y lo enlazamos al carrito
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setProductoId(request.getProductoId());
            nuevoItem.setCantidad(request.getCantidad());
            nuevoItem.setPrecioUnitario(producto.getPrecio());
            nuevoItem.setCarrito(carrito);
            carrito.getItems().add(nuevoItem);
        }

        // guardar (gracias al CascadeType.ALL se guardan los items tambien)
        Carrito carritoGuardado = carritoRepository.save(carrito);

        // retornar el dto usando el mapper
        return CarritoMapper.toResponseDTO(carritoGuardado);
    }

    public CarritoResponse obtenerCarritoActivo(Long idUsuario) {
        Carrito carrito = carritoRepository.findByIdUsuarioAndEstado(idUsuario, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new RuntimeException("No se encontró un carrito activo para este usuario"));

        return CarritoMapper.toResponseDTO(carrito);
    }

    @Transactional
    public CarritoResponse confirmarCompra(Long carritoId) {
        // buscamos el carrito
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("No se puede confirmar un carrito que no existe"));

        // validamos que no esté ya comprado o abandonado
        if (!carrito.getEstado().equals(EstadoCarrito.ACTIVO)) {
            throw new RuntimeException("Solo se pueden confirmar carritos en estado ACTIVO");
        }

        // validamos que tenga productos (no puedes comprar un carrito vacío)
        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new RuntimeException("No puedes confirmar un carrito sin productos");
        }

        // cambiamos el estado
        carrito.setEstado(EstadoCarrito.COMPRADO);

        // guardamos y retornamos el DTO
        Carrito carritoGuardado = carritoRepository.save(carrito);
        return CarritoMapper.toResponseDTO(carritoGuardado);
    }

    @Transactional
    public void vaciarCarrito(Long carritoId) {
        // buscamos el carrito por ID
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // limpiamos la lista de items
        // al ser CascadeType.ALL, Hibernate eliminará los registros de item_carrito
        carrito.getItems().clear();

        // guardamos los cambios
        carritoRepository.save(carrito);
    }
}
