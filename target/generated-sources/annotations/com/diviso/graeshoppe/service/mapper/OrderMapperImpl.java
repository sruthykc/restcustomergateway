package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.client.order.model.OrderDTO;
import com.diviso.graeshoppe.client.order.model.aggregator.DeliveryInfo;
import com.diviso.graeshoppe.client.order.model.aggregator.Order;
import com.diviso.graeshoppe.client.order.model.aggregator.Status;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-28T14:33:19+0530",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_151 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public List<Order> toEntity(List<OrderDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Order> list = new ArrayList<Order>( dtoList.size() );
        for ( OrderDTO orderDTO : dtoList ) {
            list.add( toEntity( orderDTO ) );
        }

        return list;
    }

    @Override
    public List<OrderDTO> toDto(List<Order> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<OrderDTO> list = new ArrayList<OrderDTO>( entityList.size() );
        for ( Order order : entityList ) {
            list.add( toDto( order ) );
        }

        return list;
    }

    @Override
    public OrderDTO toDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();

        Long id = orderStatusId( order );
        if ( id != null ) {
            orderDTO.setStatusId( id );
        }
        Long id1 = orderDeliveryInfoId( order );
        if ( id1 != null ) {
            orderDTO.setDeliveryInfoId( id1 );
        }
        orderDTO.setAllergyNote( order.getAllergyNote() );
        orderDTO.setCustomerId( order.getCustomerId() );
        orderDTO.setDate( order.getDate() );
        orderDTO.setEmail( order.getEmail() );
        orderDTO.setGrandTotal( order.getGrandTotal() );
        orderDTO.setId( order.getId() );
        orderDTO.setOrderId( order.getOrderId() );
        orderDTO.setPaymentRef( order.getPaymentRef() );
        orderDTO.setPreOrderDate( order.getPreOrderDate() );
        orderDTO.setStoreId( order.getStoreId() );

        return orderDTO;
    }

    @Override
    public Order toEntity(OrderDTO orderDTO) {
        if ( orderDTO == null ) {
            return null;
        }

        Order order = new Order();

        order.setStatus( orderDTOToStatus( orderDTO ) );
        order.setDeliveryInfo( orderDTOToDeliveryInfo( orderDTO ) );
        order.setAllergyNote( orderDTO.getAllergyNote() );
        order.setPreOrderDate( orderDTO.getPreOrderDate() );
        order.setCustomerId( orderDTO.getCustomerId() );
        order.setDate( orderDTO.getDate() );
        order.setEmail( orderDTO.getEmail() );
        order.setGrandTotal( orderDTO.getGrandTotal() );
        order.setId( orderDTO.getId() );
        order.setOrderId( orderDTO.getOrderId() );
        order.setPaymentRef( orderDTO.getPaymentRef() );
        order.setStoreId( orderDTO.getStoreId() );

        return order;
    }

    private Long orderStatusId(Order order) {
        if ( order == null ) {
            return null;
        }
        Status status = order.getStatus();
        if ( status == null ) {
            return null;
        }
        Long id = status.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long orderDeliveryInfoId(Order order) {
        if ( order == null ) {
            return null;
        }
        DeliveryInfo deliveryInfo = order.getDeliveryInfo();
        if ( deliveryInfo == null ) {
            return null;
        }
        Long id = deliveryInfo.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Status orderDTOToStatus(OrderDTO orderDTO) {
        if ( orderDTO == null ) {
            return null;
        }

        Status status = new Status();

        status.setId( orderDTO.getStatusId() );

        return status;
    }

    protected DeliveryInfo orderDTOToDeliveryInfo(OrderDTO orderDTO) {
        if ( orderDTO == null ) {
            return null;
        }

        DeliveryInfo deliveryInfo = new DeliveryInfo();

        deliveryInfo.setId( orderDTO.getDeliveryInfoId() );

        return deliveryInfo;
    }
}
