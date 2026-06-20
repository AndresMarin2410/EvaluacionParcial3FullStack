package cl.EvaluacionParcial3.MicroService_Pagos.Repository;

import cl.EvaluacionParcial3.MicroService_Pagos.Model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByIdPedido(Long idPedido);
}
