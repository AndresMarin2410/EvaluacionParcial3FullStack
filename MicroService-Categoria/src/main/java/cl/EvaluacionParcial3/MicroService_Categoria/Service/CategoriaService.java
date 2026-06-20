package cl.EvaluacionParcial3.MicroService_Categoria.Service;

import cl.EvaluacionParcial3.MicroService_Categoria.Dto.CategoriaRequest;
import cl.EvaluacionParcial3.MicroService_Categoria.Dto.CategoriaResponse;
import cl.EvaluacionParcial3.MicroService_Categoria.Dto.CategoriaUpdateRequest;
import cl.EvaluacionParcial3.MicroService_Categoria.Mapper.CategoriaMapper;
import cl.EvaluacionParcial3.MicroService_Categoria.Model.Categoria;
import cl.EvaluacionParcial3.MicroService_Categoria.Repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@Slf4j
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private CategoriaMapper categoriaMapper;
    //Listar
    public List<Categoria> listarCategorias() {
        log.info("Listando todas las categorias");
        return categoriaRepository.findAll();
    }
    //Buscar por ID
    public CategoriaResponse buscarPorId(Long id) {
        log.info("Buscando la categoria con el ID: {}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro la categoria con el ID: "+ id));
        return categoriaMapper.toResponse(categoria);
    }
    //CREAR CATEGORIA
    public CategoriaResponse crearCategoria(CategoriaRequest request) {
        log.info("Creando categoria");
        Categoria categoria = categoriaRepository.save(categoriaMapper.fromRequest(request));
        return categoriaMapper.toResponse(categoria);
    }
    //ACTUALIZAR CATEGORIA

    public CategoriaResponse actualizarCategoria(Long id,CategoriaUpdateRequest request) {
        log.info("Actualizando categoria con el ID: {}",id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro la categoria con el ID: "+ id));
        if(request.getNombre() != null) {
            categoria.setNombre(request.getNombre());
        }
        if(request.getDescripcion() != null) {
            categoria.setDescripcion(request.getDescripcion());
        }
        if(request.getEstado() != null) {
            categoria.setEstado(request.getEstado());
        }
        if(request.getFechaCreacion() != null) {
            categoria.setFechaCreacion(request.getFechaCreacion());
        }
        categoria = categoriaRepository.save(categoria);
        return categoriaMapper.toResponse(categoria);
    }

    //ELIMINAR CATEGORIA
    public void eliminarCategoria(Long id) {
        log.info("Eliminando categoria con el ID: {}", id);
        if(!categoriaRepository.existsById(id)) {
            throw new NoSuchElementException("No se encontro la categoria con el ID: "+ id);
        }
        categoriaRepository.deleteById(id);
    }
}
