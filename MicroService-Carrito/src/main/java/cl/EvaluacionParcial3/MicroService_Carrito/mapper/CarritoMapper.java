package cl.EvaluacionParcial3.MicroService_Carrito.mapper;

import java.util.stream.Collectors;

import cl.EvaluacionParcial3.MicroService_Carrito.dto.CarritoResponse;
import cl.EvaluacionParcial3.MicroService_Carrito.dto.ItemResponse;
import cl.EvaluacionParcial3.MicroService_Carrito.model.Carrito;
import cl.EvaluacionParcial3.MicroService_Carrito.model.ItemCarrito;

public class CarritoMapper {

    //convertir de Entidad a dto para enviar al cliente
    public static CarritoResponse toResponseDTO(Carrito entidad) {
        if (entidad == null) return null;

        CarritoResponse dto = new CarritoResponse();
        dto.setCarritoId(entidad.getCarritoId());
        dto.setIdUsuario(entidad.getIdUsuario());
        dto.setFechaCreacion(entidad.getFechaCreacion());
        dto.setEstado(entidad.getEstado());
        
        //mapeo de lista de items
        if (entidad.getItems() != null) {
            dto.setItems(entidad.getItems().stream()
                .map(CarritoMapper::toItemResponseDTO)
                .collect(Collectors.toList()));
            
            //calcular el total del carrito
            dto.setTotal(dto.getItems().stream()
                .mapToDouble(ItemResponse::getSubTotal)
                .sum());
        }

        return dto;
    }



    //convertir el item individual
    public static ItemResponse toItemResponseDTO(ItemCarrito entidad) {
        if (entidad == null) return null;

        ItemResponse dto = new ItemResponse();
        dto.setItemId(entidad.getItemId());
        dto.setProductoId(entidad.getProductoId());
        dto.setCantidad(entidad.getCantidad());
        dto.setPrecioUnitario(entidad.getPrecioUnitario());
        
        //calculo del subtotal 
        dto.setSubTotal(entidad.getCantidad() * entidad.getPrecioUnitario());

        return dto;
    }
}
