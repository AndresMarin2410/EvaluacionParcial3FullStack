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

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioMapper usuarioMapper;
    @InjectMocks
    private UsuarioService usuarioService;



    //Test de listar Usuarios
    @Test
    public void listarUsuarios() {

        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));


        List<Usuario> usuarios = usuarioService.buscarUsuarios();

        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());
        verify(usuarioRepository, times(1)).findAll();
        verifyNoInteractions(usuarioMapper);
    }

    //Test de buscar usuario por ID
    @Test
    public void buscarUsuarioPorId() {

        Long id = 1L;
        Usuario usuarioMock = new Usuario();
        UsuarioResponse responseMock = new UsuarioResponse();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioMock));
        when(usuarioMapper.toResponse(usuarioMock)).thenReturn(responseMock);


        UsuarioResponse resultado = usuarioService.buscarPorId(id);


        assertNotNull(resultado);
        verify(usuarioRepository).findById(id);
    }

    //Test para verificar que la exception NoSuchElement funciona en caso de no encontrar un usuario con ese ID
    @Test
    public void buscarUsuarioPorIdNoSuchElementException() {

        Long id = 99L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());


        assertThrows(NoSuchElementException.class, () -> {
            usuarioService.buscarPorId(id);
        });
        verify(usuarioMapper, never()).toResponse(any());
    }

    //Test buscar usuario por RUN
    @Test
    public void buscarUsuarioPorRun() {
        String run = "09876543-1";
        Usuario usuarioMock = new Usuario();
        UsuarioResponse responseMock = new UsuarioResponse();

        when(usuarioRepository.findByRun(run)).thenReturn(Optional.of(usuarioMock));
        when(usuarioMapper.toResponse(usuarioMock)).thenReturn(responseMock);


        UsuarioResponse resultado = usuarioService.buscarPorRun(run);


        assertNotNull(resultado);
        verify(usuarioRepository).findByRun(run);
    }

    //Test para verificar que la exception NoSuchElement funciona en caso de no encontrar un usuario con ese RUN
    @Test
    public void buscarUsuarioPorRunNoSuchElementException() {
        String run = "00000000-0";
        when(usuarioRepository.findByRun(run)).thenReturn(Optional.empty());


        assertThrows(NoSuchElementException.class, () -> {
            usuarioService.buscarPorRun(run);
        });
        verify(usuarioMapper, never()).toResponse(any());
    }

    //Test de crear usuario
    @Test
    public void crearUsuario() {
        UsuarioRequest request = new UsuarioRequest();
        request.setRun("01010101-1");
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        UsuarioResponse responseMock = new UsuarioResponse();

        when(usuarioRepository.existsByRun("01010101-1")).thenReturn(false);
        when(usuarioMapper.fromRequest(request)).thenReturn(usuario1);
        when(usuarioRepository.save(usuario1)).thenReturn(usuario2);
        when(usuarioMapper.toResponse(usuario2)).thenReturn(responseMock);


        UsuarioResponse resultado = usuarioService.crearUsuario(request);

        assertNotNull(resultado);
        verify(usuarioRepository).save(usuario1);
    }

    //Test de actualizar informacion del usuario
    @Test
    public void actualizarUsuario() {
        Long id = 1L;
        UsuarioUpdateRequest request = new UsuarioUpdateRequest();
        request.setNombre("Michael");
        request.setCorreo("Michael@correo.cl");

        Usuario usuarioRegistrado = new Usuario();
        usuarioRegistrado.setNombre("Pedro");
        usuarioRegistrado.setApellido("Pérez");
        usuarioRegistrado.setCorreo("pedro@correo.cl");
        usuarioRegistrado.setDireccion("Av. Siempre Viva 742");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioRegistrado));
        when(usuarioRepository.save(usuarioRegistrado)).thenReturn(usuarioRegistrado);
        when(usuarioMapper.toResponse(usuarioRegistrado)).thenReturn(new UsuarioResponse());


        usuarioService.actualizarUsuario(id, request);


        assertEquals("Michael", usuarioRegistrado.getNombre());
        assertEquals("Michael@correo.cl", usuarioRegistrado.getCorreo());
        assertEquals("Pérez", usuarioRegistrado.getApellido());
        assertEquals("Av. Siempre Viva 742", usuarioRegistrado.getDireccion());
        verify(usuarioRepository).save(usuarioRegistrado);
    }

    // Test para actualizar usuario cuando el ID NO existe
    @Test
    public void actualizarUsuarioNoSuchElementException() {
        Long id = 99L;
        UsuarioUpdateRequest request = new UsuarioUpdateRequest();
        request.setNombre("Michael");

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            usuarioService.actualizarUsuario(id, request);
        });
        verify(usuarioRepository, never()).save(any());
    }

    //Test para eliminar usuario mediante su ID
    @Test
    public void eliminarUsuario() {
        Long id = 1L;
        when(usuarioRepository.existsById(id)).thenReturn(true);


        usuarioService.eliminarUsuario(id);

        verify(usuarioRepository, times(1)).deleteById(id);
    }

    //Test para eliminar usuario cuando el ID no existe
    @Test
    public void eliminarUsuarioNoSuchElementException() {

        Long id = 99L;
        when(usuarioRepository.existsById(id)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> {
            usuarioService.eliminarUsuario(id);
        });
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

}
