package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.client.order.model.OrderLineDTO;
import com.diviso.graeshoppe.client.order.model.aggregator.OrderLine;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-30T10:42:08+0530",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_222 (Private Build)"
)
@Component
public class OrderLineMapperImpl implements OrderLineMapper {

    @Override
    public List<OrderLine> toEntity(List<OrderLineDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<OrderLine> list = new ArrayList<OrderLine>( dtoList.size() );
        for ( OrderLineDTO orderLineDTO : dtoList ) {
            list.add( toEntity( orderLineDTO ) );
        }

        return list;
    }

    @Override
    public List<OrderLineDTO> toDto(List<OrderLine> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<OrderLineDTO> list = new ArrayList<OrderLineDTO>( entityList.size() );
        for ( OrderLine orderLine : entityList ) {
            list.add( toDto( orderLine ) );
        }

        return list;
    }

    @Override
    public OrderLineDTO toDto(OrderLine orderLine) {
        if ( orderLine == null ) {
            return null;
        }

        OrderLineDTO orderLineDTO = new OrderLineDTO();

        orderLineDTO.setId( orderLine.getId() );
        orderLineDTO.setPricePerUnit( orderLine.getPricePerUnit() );
        orderLineDTO.setProductId( orderLine.getProductId() );
        orderLineDTO.setQuantity( orderLine.getQuantity() );
        orderLineDTO.setTotal( orderLine.getTotal() );

        return orderLineDTO;
    }

    @Override
    public OrderLine toEntity(OrderLineDTO orderLineDTO) {
        if ( orderLineDTO == null ) {
            return null;
        }

        OrderLine orderLine = new OrderLine();

        orderLine.setId( orderLineDTO.getId() );
        orderLine.setPricePerUnit( orderLineDTO.getPricePerUnit() );
        orderLine.setProductId( orderLineDTO.getProductId() );
        orderLine.setQuantity( orderLineDTO.getQuantity() );
        orderLine.setTotal( orderLineDTO.getTotal() );

        return orderLine;
    }
}
