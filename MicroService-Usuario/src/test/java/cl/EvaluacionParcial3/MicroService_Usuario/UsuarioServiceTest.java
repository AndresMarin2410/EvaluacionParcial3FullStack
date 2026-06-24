package cl.EvaluacionParcial3.MicroService_Usuario;

import cl.EvaluacionParcial3.MicroService_Usuario.Dto.UsuarioRequest;
import cl.EvaluacionParcial3.MicroService_Usuario.Dto.UsuarioResponse;
import cl.EvaluacionParcial3.MicroService_Usuario.Dto.UsuarioUpdateRequest;
import cl.EvaluacionParcial3.MicroService_Usuario.Mapper.UsuarioMapper;
import cl.EvaluacionParcial3.MicroService_Usuario.Model.Usuario;
import cl.EvaluacionParcial3.MicroService_Usuario.Repository.UsuarioRepository;
import cl.EvaluacionParcial3.MicroService_Usuario.Service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    //El @Mock crea un clon del repositorio para no tocar la base de datos
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioMapper usuarioMapper;

    //Crea una instacia real del servicio e inyecta los clones que estan arriba, Repositorio-Mapper
    @InjectMocks
    private UsuarioService usuarioService;



    //Test de listar Usuarios
    @Test
    public void listarUsuarios() {
        //Preparamos la informacion falsa que va al repositorio
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();

        //Le dice a mockito que llame a findAll() y le devuelva una lista con los 2 usuarios
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));

        //Ejecuta el metodo real del servicio que probaremos
        List<Usuario> usuarios = usuarioService.buscarUsuarios();

        //Con jUnit comprobamos que el resultado sea el correcto
        assertNotNull(usuarios); //Valida que la lista no sea nulla
        assertEquals(2, usuarios.size()); //Valida que esten los 2 usuarios creados
        verify(usuarioRepository, times(1)).findAll(); //Valida que se hizo un solo llamado a la base de datos
        verifyNoInteractions(usuarioMapper); //Valida que el Mapper no se uso
    }

    //Test de buscar usuario por ID
    @Test
    public void buscarUsuarioPorId() {
        //Usamos 1L para decirle a java que el ID sera tipo Long
        Long id = 1L;
        Usuario usuarioMock = new Usuario();
        UsuarioResponse responseMock = new UsuarioResponse();

        // cuando el servicio busque el ID 1, le de un usuario de mentira
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioMock));
        //Cuando el usuario le pase el usuario de mentira, lo transforma en el dto de mnentira
        when(usuarioMapper.toResponse(usuarioMock)).thenReturn(responseMock);

        //Ejecuta el metodo del servicio
        UsuarioResponse resultado = usuarioService.buscarPorId(id);

        //Valida que llego el dto y que se consulto en el repositorio usando ese ID
        assertNotNull(resultado);
        verify(usuarioRepository).findById(id);
    }

    //Test para verificar que la exception NoSuchElement funciona en caso de no encontrar un usuario con ese ID
    @Test
    public void buscarUsuarioPorIdNoSuchElementException() {
        //Configuramos un ID cualquiera y le dice al repositorio que devuelva un Optional nullo
        Long id = 99L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        //El assertThrows ejecuta el codigo y verifica que se detenga con la exception esperada
        assertThrows(NoSuchElementException.class, () -> {
            usuarioService.buscarPorId(id);
        });
        //Si fallo, confimamos que el mapper no trabajo con datos invalidos.
        verify(usuarioMapper, never()).toResponse(any());
    }

    //Test buscar usuario por RUN
    @Test
    public void buscarUsuarioPorRun() {
        //Configuramos un RUN para el test
        String run = "09876543-1";
        Usuario usuarioMock = new Usuario();
        UsuarioResponse responseMock = new UsuarioResponse();

        //cuando el servicio busque el RUN, le de un usuario de mentira
        when(usuarioRepository.findByRun(run)).thenReturn(Optional.of(usuarioMock));
        //Cuando el usuario le pase el usuario de mentira, lo transforma en el dto de mnentira
        when(usuarioMapper.toResponse(usuarioMock)).thenReturn(responseMock);

        //Ejecuta el metodo
        UsuarioResponse resultado = usuarioService.buscarPorRun(run);

        //Verifica que no fallo y que la query simulada se lanzo
        assertNotNull(resultado);
        verify(usuarioRepository).findByRun(run);
    }

    //Test para verificar que la exception NoSuchElement funciona en caso de no encontrar un usuario con ese RUN
    @Test
    public void buscarUsuarioPorRunNoSuchElementException() {
        //Simula que el RUN no esta en el sistema
        String run = "00000000-0";
        when(usuarioRepository.findByRun(run)).thenReturn(Optional.empty());

        //Valida que el servicio se detiene con el error
        assertThrows(NoSuchElementException.class, () -> {
            usuarioService.buscarPorRun(run);
        });
        verify(usuarioMapper, never()).toResponse(any());
    }

    //Test de crear usuario
    @Test
    public void crearUsuario() {
        //Creamos un objeto para la simulacion
        UsuarioRequest request = new UsuarioRequest();
        request.setRun("01010101-1");

        //Separamos usuario1 de usuario2 para simular el JPA
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        UsuarioResponse responseMock = new UsuarioResponse();

        //programamos los guardados
        when(usuarioRepository.existsByRun("01010101-1")).thenReturn(false); //Validamos que el RUN no este duplicado
        when(usuarioMapper.fromRequest(request)).thenReturn(usuario1); // El mapper lo convierte en entidad
        when(usuarioRepository.save(usuario1)).thenReturn(usuario2); //El repositorio lo guarda y lo devuelve con ID
        when(usuarioMapper.toResponse(usuario2)).thenReturn(responseMock); //El mapper lo transforma en el DTO final

        //Ejecuta el metodo de crear usuario
        UsuarioResponse resultado = usuarioService.crearUsuario(request);

        //Validamos que el resultado no sea nullo y que se llamo al metodo .save() en el repositorio.
        assertNotNull(resultado);
        verify(usuarioRepository).save(usuario1);
    }

    //Test de actualizar informacion del usuario
    @Test
    public void actualizarUsuario() {

        //El request tendra 2 campos modificados
        Long id = 1L;
        UsuarioUpdateRequest request = new UsuarioUpdateRequest();
        request.setNombre("Michael");
        request.setCorreo("Michael@correo.cl");

        //Creamos el usuario tal cual como ya estaba guardado en la base de datos original
        Usuario usuarioRegistrado = new Usuario();
        usuarioRegistrado.setNombre("Pedro");
        usuarioRegistrado.setApellido("Pérez");
        usuarioRegistrado.setCorreo("pedro@correo.cl");
        usuarioRegistrado.setDireccion("Av. Siempre Viva 742");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioRegistrado));
        when(usuarioRepository.save(usuarioRegistrado)).thenReturn(usuarioRegistrado);
        when(usuarioMapper.toResponse(usuarioRegistrado)).thenReturn(new UsuarioResponse());

        // Ejecuta la actualizacion en el servicio
        usuarioService.actualizarUsuario(id, request);


        assertEquals("Michael", usuarioRegistrado.getNombre()); //Verificar que si cambia el nombre
        assertEquals("Michael@correo.cl", usuarioRegistrado.getCorreo()); // verifica que si cambio el correo
        assertEquals("Pérez", usuarioRegistrado.getApellido()); // verifica que se mantuvo el apellido
        assertEquals("Av. Siempre Viva 742", usuarioRegistrado.getDireccion()); // verificamos que se mantuvo la direccion
        verify(usuarioRepository).save(usuarioRegistrado); // confirmamos que se guardaron los cambios
    }

    // Test para actualizar usuario cuando el ID NO existe
    @Test
    public void actualizarUsuarioNoSuchElementException() {
        //Intentamos actualizar un ID que no existe
        Long id = 99L;
        UsuarioUpdateRequest request = new UsuarioUpdateRequest();
        request.setNombre("Michael");

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        //Verifica que se lance la exception
        assertThrows(NoSuchElementException.class, () -> {
            usuarioService.actualizarUsuario(id, request);
        });
        //Verificamos que el repositorio no intento guardar datos falsos
        verify(usuarioRepository, never()).save(any());
    }

    //Test para eliminar usuario mediante su ID
    @Test
    public void eliminarUsuario() {
        //Simulamos que el ID 1 existe en el sistema
        Long id = 1L;
        when(usuarioRepository.existsById(id)).thenReturn(true);

        //Ejecuta el metodo de eliminar
        usuarioService.eliminarUsuario(id);
        //Verifica que el repositorio recibio la orden de borrar 1 a la vez
        verify(usuarioRepository, times(1)).deleteById(id);
    }

    //Test para eliminar usuario cuando el ID no existe
    @Test
    public void eliminarUsuarioNoSuchElementException() {
        //Valida que el validador de existencia devuelva 'false'
        Long id = 99L;
        when(usuarioRepository.existsById(id)).thenReturn(false);
        //Valida la exception
        assertThrows(NoSuchElementException.class, () -> {
            usuarioService.eliminarUsuario(id);
        });
        //Verifica que nunca se llamo al metodo findById() para evitar caidas.
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

}
