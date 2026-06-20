package cl.EvaluacionParcial3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.EvaluacionParcial3.model.ReporteVenta;
@Repository
public interface  ReporteVentaRepository extends JpaRepository<ReporteVenta, Long>{}
