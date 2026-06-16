package cl.EvaluacionParcial3.MicroService_Categoria.Repository;

import cl.EvaluacionParcial3.MicroService_Categoria.Model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
