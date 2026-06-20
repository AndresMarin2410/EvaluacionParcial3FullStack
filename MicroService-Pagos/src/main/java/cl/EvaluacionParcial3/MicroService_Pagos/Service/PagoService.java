package cl.EvaluacionParcial3.MicroService_Pagos.Service;

import cl.EvaluacionParcial3.MicroService_Pagos.Client.PedidoClient;
import cl.EvaluacionParcial3.MicroService_Pagos.Dto.PagoRequest;
import cl.EvaluacionParcial3.MicroService_Pagos.Dto.PagoResponse;
import cl.EvaluacionParcial3.MicroService_Pagos.Dto.PagoUpdateRequest;
import cl.EvaluacionParcial3.MicroService_Pagos.Mapper.PagoMapper;
import cl.EvaluacionParcial3.MicroService_Pagos.Model.Pago;
import cl.EvaluacionParcial3.MicroService_Pagos.Repository.PagoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Transactional
@Slf4j
@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private PagoMapper pagoMapper;
    @Autowired
    private PedidoClient pedidoClient;

    public List<Pago> listarPagos() {
        log.info("Buscando todos los pagos");
        return pagoRepository.findAll();
    }

    public PagoResponse buscarPagoPorId(Long id) {
        log.info("Buscando pago por id: {}",id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("pago no encontrado"));
        return pagoMapper.toResponse(pago);
    }

    public PagoResponse buscarPorPedidoId(Long id) {
        log.info("Buscando pago por id: {}",id);
        Pago pago = pagoRepository.findByIdPedido(id).
                orElseThrow(() -> new NoSuchElementException("pago no encontrado"));
        return pagoMapper.toResponse(pago);
    }

    public PagoResponse crearPago(PagoRequest request) {
        log.info("Creando pago");

        pedidoClient.obtenerPedidoPorId(request.getPedidoId());

        Pago pago = pagoRepository.save(pagoMapper.fromRequest(request));
        return pagoMapper.toResponse(pago);
    }

    public PagoResponse actualizarPago(Long id, PagoUpdateRequest request) {
        log.info("Actualizando pago por id: {}",id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("pago no encontrado"));
        if(request.getMonto() != null) {
            pago.setMonto(request.getMonto());
        }
        if(request.getMetodoPago() != null) {
            pago.setMetodoPago(request.getMetodoPago());
        }
        if(request.getEstado() != null) {
            pago.setEstado(request.getEstado());
        }
        if(request.getFechaPago() != null) {
            pago.setFechaPago(request.getFechaPago());
        }
        pago = pagoRepository.save(pago);
        return pagoMapper.toResponse(pago);
    }

    public void eliminarPago(Long id) {
        log.info("Eliminando pago por id: {}",id);
        if(!pagoRepository.existsById(id)){
            throw new NoSuchElementException("pago no encontrado");
        }
        pagoRepository.deleteById(id);
    }
}
