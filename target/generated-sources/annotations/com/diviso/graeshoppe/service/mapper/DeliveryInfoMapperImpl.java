package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.client.order.model.DeliveryInfoDTO;
import com.diviso.graeshoppe.client.order.model.aggregator.DeliveryInfo;
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
public class DeliveryInfoMapperImpl implements DeliveryInfoMapper {

    @Override
    public List<DeliveryInfo> toEntity(List<DeliveryInfoDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<DeliveryInfo> list = new ArrayList<DeliveryInfo>( dtoList.size() );
        for ( DeliveryInfoDTO deliveryInfoDTO : dtoList ) {
            list.add( toEntity( deliveryInfoDTO ) );
        }

        return list;
    }

    @Override
    public List<DeliveryInfoDTO> toDto(List<DeliveryInfo> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<DeliveryInfoDTO> list = new ArrayList<DeliveryInfoDTO>( entityList.size() );
        for ( DeliveryInfo deliveryInfo : entityList ) {
            list.add( toDto( deliveryInfo ) );
        }

        return list;
    }

    @Override
    public DeliveryInfoDTO toDto(DeliveryInfo deliveryInfo) {
        if ( deliveryInfo == null ) {
            return null;
        }

        DeliveryInfoDTO deliveryInfoDTO = new DeliveryInfoDTO();

        deliveryInfoDTO.setDeliveryCharge( deliveryInfo.getDeliveryCharge() );
        deliveryInfoDTO.setDeliveryNotes( deliveryInfo.getDeliveryNotes() );
        deliveryInfoDTO.setDeliveryType( deliveryInfo.getDeliveryType() );
        deliveryInfoDTO.setId( deliveryInfo.getId() );

        return deliveryInfoDTO;
    }

    @Override
    public DeliveryInfo toEntity(DeliveryInfoDTO deliveryInfoDTO) {
        if ( deliveryInfoDTO == null ) {
            return null;
        }

        DeliveryInfo deliveryInfo = new DeliveryInfo();

        deliveryInfo.setDeliveryCharge( deliveryInfoDTO.getDeliveryCharge() );
        deliveryInfo.setDeliveryNotes( deliveryInfoDTO.getDeliveryNotes() );
        deliveryInfo.setDeliveryType( deliveryInfoDTO.getDeliveryType() );
        deliveryInfo.setId( deliveryInfoDTO.getId() );

        return deliveryInfo;
    }
}
