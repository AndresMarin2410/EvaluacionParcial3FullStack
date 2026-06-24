package cl.EvaluacionParcial3.MicroService_Producto;
 
import cl.EvaluacionParcial3.MicroService_Producto.dto.ProductoRequestDTO;
import cl.EvaluacionParcial3.MicroService_Producto.dto.ProductoResponseDTO;
import cl.EvaluacionParcial3.MicroService_Producto.exceptions.CategoriaNotFoundException;
import cl.EvaluacionParcial3.MicroService_Producto.exceptions.ProductoNotFoundException;
import cl.EvaluacionParcial3.MicroService_Producto.model.Producto;
import cl.EvaluacionParcial3.MicroService_Producto.repository.ProductoRepository;
import cl.EvaluacionParcial3.MicroService_Producto.service.ProductoService;
 
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
 
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
 
@ExtendWith({MockitoExtension.class})
public class ProductoServiceTest {
 
    @Mock
    private ProductoRepository productoRepository;
 
    @Mock
    private WebClient.Builder webClientBuilder;
 
    @Mock
    private WebClient webClient;
 
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
 
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
 
    @Mock
    private WebClient.ResponseSpec responseSpec;
 
    private ProductoService productoService;
 
    // No usamos @InjectMocks porque el constructor de ProductoService recibe
    // un WebClient.Builder y llama a .build() internamente; necesitamos
    // controlar nosotros ese mock antes de construir el service.
    @BeforeEach
    public void setUp() {
        Mockito.when(webClientBuilder.build()).thenReturn(webClient);
        productoService = new ProductoService(productoRepository, webClientBuilder);
        // El campo categoriasUrl viene de @Value y no se inyecta en un test
        // unitario puro, así que lo seteamos manualmente con ReflectionTestUtils.
        ReflectionTestUtils.setField(productoService, "categoriasUrl", "http://localhost:8082");
    }
 
    // Simula que la categoría SI existe en ms-categorias (camino feliz)
    private void mockCategoriaExiste() {
        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString(), Mockito.any(Object.class)))
                .thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(new Object()));
    }
 
    // Simula que la categoría NO existe en ms-categorias (lanza 404)
    private void mockCategoriaNoExiste() {
        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString(), Mockito.any(Object.class)))
                .thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        WebClientResponseException notFound = WebClientResponseException.create(
                404, "Not Found", null, null, null);
        Mockito.when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.error(notFound));
    }
 
    @Test
    public void listarTodos() {
        Producto producto1 = Producto.builder().id(1L).nombre("Mouse").build();
        Producto producto2 = Producto.builder().id(2L).nombre("Teclado").build();
        Mockito.when(productoRepository.findAll()).thenReturn(List.of(producto1, producto2));
 
        List<ProductoResponseDTO> productos = productoService.listarTodos();
 
        Assertions.assertNotNull(productos);
        Assertions.assertEquals(2, productos.size());
        Mockito.verify(productoRepository, Mockito.times(1)).findAll();
    }
 
    @Test
    public void obtenerPorId() {
        Long id = 1L;
        Producto productoMock = Producto.builder()
                .id(id)
                .nombre("Mouse")
                .precio(9990.0)
                .categoriaId(2L)
                .activo(true)
                .fechaCreacion(LocalDate.now())
                .build();
        Mockito.when(productoRepository.findById(id)).thenReturn(Optional.of(productoMock));
 
        ProductoResponseDTO resultado = productoService.obtenerPorId(id);
 
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Mouse", resultado.getNombre());
        Mockito.verify(productoRepository).findById(id);
    }
 
    @Test
    public void obtenerPorIdProductoNotFoundException() {
        Long id = 99L;
        Mockito.when(productoRepository.findById(id)).thenReturn(Optional.empty());
 
        Assertions.assertThrows(ProductoNotFoundException.class,
                () -> productoService.obtenerPorId(id));
        Mockito.verify(productoRepository).findById(id);
    }
 
    @Test
    public void listarActivos() {
        Producto producto1 = Producto.builder().id(1L).nombre("Mouse").activo(true).build();
        Mockito.when(productoRepository.findByActivoTrue()).thenReturn(List.of(producto1));
 
        List<ProductoResponseDTO> productos = productoService.listarActivos();
 
        Assertions.assertNotNull(productos);
        Assertions.assertEquals(1, productos.size());
        Mockito.verify(productoRepository, Mockito.times(1)).findByActivoTrue();
    }
 
    @Test
    public void listarPorCategoria() {
        Long categoriaId = 2L;
        mockCategoriaExiste();
        Producto producto1 = Producto.builder().id(1L).nombre("Mouse").categoriaId(categoriaId).build();
        Mockito.when(productoRepository.findByCategoriaIdAndActivoTrue(categoriaId))
                .thenReturn(List.of(producto1));
 
        List<ProductoResponseDTO> productos = productoService.listarPorCategoria(categoriaId);
 
        Assertions.assertNotNull(productos);
        Assertions.assertEquals(1, productos.size());
        Mockito.verify(productoRepository).findByCategoriaIdAndActivoTrue(categoriaId);
    }
 
    @Test
    public void listarPorCategoriaCategoriaNotFoundException() {
        Long categoriaId = 99L;
        mockCategoriaNoExiste();
 
        Assertions.assertThrows(CategoriaNotFoundException.class,
                () -> productoService.listarPorCategoria(categoriaId));
        Mockito.verify(productoRepository, Mockito.never())
                .findByCategoriaIdAndActivoTrue(Mockito.anyLong());
    }
 
    @Test
    public void buscarPorNombre() {
        String nombre = "mouse";
        Producto producto1 = Producto.builder().id(1L).nombre("Mouse Inalámbrico").build();
        Mockito.when(productoRepository.findByNombreContainingIgnoreCase(nombre))
                .thenReturn(List.of(producto1));
 
        List<ProductoResponseDTO> productos = productoService.buscarPorNombre(nombre);
 
        Assertions.assertNotNull(productos);
        Assertions.assertEquals(1, productos.size());
        Mockito.verify(productoRepository).findByNombreContainingIgnoreCase(nombre);
    }
 
    @Test
    public void crearProducto() {
        ProductoRequestDTO request = ProductoRequestDTO.builder()
                .nombre("Mouse")
                .descripcion("Mouse óptico")
                .precio(9990.0)
                .categoriaId(2L)
                .build();
        mockCategoriaExiste();
        Producto guardado = Producto.builder()
                .id(1L)
                .nombre("Mouse")
                .precio(9990.0)
                .categoriaId(2L)
                .activo(true)
                .fechaCreacion(LocalDate.now())
                .build();
        Mockito.when(productoRepository.save(Mockito.any(Producto.class))).thenReturn(guardado);
 
        ProductoResponseDTO resultado = productoService.crear(request);
 
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Mouse", resultado.getNombre());
        Assertions.assertTrue(resultado.getActivo());
        Mockito.verify(productoRepository).save(Mockito.any(Producto.class));
    }
 
    @Test
    public void crearProductoCategoriaNotFoundException() {
        ProductoRequestDTO request = ProductoRequestDTO.builder()
                .nombre("Mouse")
                .precio(9990.0)
                .categoriaId(99L)
                .build();
        mockCategoriaNoExiste();
 
        Assertions.assertThrows(CategoriaNotFoundException.class,
                () -> productoService.crear(request));
        Mockito.verify(productoRepository, Mockito.never()).save(Mockito.any(Producto.class));
    }
 
    @Test
    public void actualizarProductoMismaCategoria() {
        Long id = 1L;
        ProductoRequestDTO request = ProductoRequestDTO.builder()
                .nombre("Mouse Pro")
                .descripcion("Nueva descripción")
                .precio(12990.0)
                .categoriaId(2L)
                .build();
        Producto productoExistente = Producto.builder()
                .id(id)
                .nombre("Mouse")
                .precio(9990.0)
                .categoriaId(2L)
                .activo(true)
                .build();
        Mockito.when(productoRepository.findById(id)).thenReturn(Optional.of(productoExistente));
        Mockito.when(productoRepository.save(productoExistente)).thenReturn(productoExistente);
 
        ProductoResponseDTO resultado = productoService.actualizar(id, request);
 
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Mouse Pro", productoExistente.getNombre());
        Assertions.assertEquals(12990.0, productoExistente.getPrecio());
        // Como la categoría no cambió, jamás debería llamar al WebClient
        Mockito.verify(webClient, Mockito.never()).get();
        Mockito.verify(productoRepository).save(productoExistente);
    }
 
    @Test
    public void actualizarProductoCambiaCategoria() {
        Long id = 1L;
        ProductoRequestDTO request = ProductoRequestDTO.builder()
                .nombre("Mouse Pro")
                .precio(12990.0)
                .categoriaId(5L)
                .build();
        Producto productoExistente = Producto.builder()
                .id(id)
                .nombre("Mouse")
                .precio(9990.0)
                .categoriaId(2L)
                .activo(true)
                .build();
        mockCategoriaExiste();
        Mockito.when(productoRepository.findById(id)).thenReturn(Optional.of(productoExistente));
        Mockito.when(productoRepository.save(productoExistente)).thenReturn(productoExistente);
 
        productoService.actualizar(id, request);
 
        Assertions.assertEquals(5L, productoExistente.getCategoriaId());
        // Como la categoría SI cambió, debe validar contra ms-categorias
        Mockito.verify(webClient, Mockito.times(1)).get();
    }
 
    @Test
    public void actualizarProductoNotFoundException() {
        Long id = 99L;
        ProductoRequestDTO request = ProductoRequestDTO.builder()
                .nombre("Mouse")
                .precio(9990.0)
                .categoriaId(2L)
                .build();
        Mockito.when(productoRepository.findById(id)).thenReturn(Optional.empty());
 
        Assertions.assertThrows(ProductoNotFoundException.class,
                () -> productoService.actualizar(id, request));
        Mockito.verify(productoRepository, Mockito.never()).save(Mockito.any(Producto.class));
    }
 
    @Test
    public void eliminarProducto() {
        Long id = 1L;
        Mockito.when(productoRepository.existsById(id)).thenReturn(true);
 
        productoService.eliminar(id);
 
        Mockito.verify(productoRepository, Mockito.times(1)).deleteById(id);
    }
 
    @Test
    public void eliminarProductoNotFoundException() {
        Long id = 99L;
        Mockito.when(productoRepository.existsById(id)).thenReturn(false);
 
        Assertions.assertThrows(ProductoNotFoundException.class,
                () -> productoService.eliminar(id));
        Mockito.verify(productoRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }
 
    @Test
    public void desactivarProducto() {
        Long id = 1L;
        Producto productoMock = Producto.builder().id(id).nombre("Mouse").activo(true).build();
        Mockito.when(productoRepository.findById(id)).thenReturn(Optional.of(productoMock));
        Mockito.when(productoRepository.save(productoMock)).thenReturn(productoMock);
 
        ProductoResponseDTO resultado = productoService.desactivar(id);
 
        Assertions.assertFalse(productoMock.getActivo());
        Assertions.assertNotNull(resultado);
        Mockito.verify(productoRepository).save(productoMock);
    }
 
    @Test
    public void activarProducto() {
        Long id = 1L;
        Producto productoMock = Producto.builder().id(id).nombre("Mouse").activo(false).build();
        Mockito.when(productoRepository.findById(id)).thenReturn(Optional.of(productoMock));
        Mockito.when(productoRepository.save(productoMock)).thenReturn(productoMock);
 
        ProductoResponseDTO resultado = productoService.activar(id);
 
        Assertions.assertTrue(productoMock.getActivo());
        Assertions.assertNotNull(resultado);
        Mockito.verify(productoRepository).save(productoMock);
    }
}