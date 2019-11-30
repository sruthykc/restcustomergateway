package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.client.order.model.OfferDTO;
import com.diviso.graeshoppe.client.order.model.aggregator.Offer;
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
public class OfferMapperImpl implements OfferMapper {

    @Override
    public List<Offer> toEntity(List<OfferDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Offer> list = new ArrayList<Offer>( dtoList.size() );
        for ( OfferDTO offerDTO : dtoList ) {
            list.add( toEntity( offerDTO ) );
        }

        return list;
    }

    @Override
    public List<OfferDTO> toDto(List<Offer> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<OfferDTO> list = new ArrayList<OfferDTO>( entityList.size() );
        for ( Offer offer : entityList ) {
            list.add( toDto( offer ) );
        }

        return list;
    }

    @Override
    public OfferDTO toDto(Offer offer) {
        if ( offer == null ) {
            return null;
        }

        OfferDTO offerDTO = new OfferDTO();

        offerDTO.setOrderDiscountAmount( offer.getOrderDiscountAmount() );
        offerDTO.setDescription( offer.getDescription() );
        offerDTO.setId( offer.getId() );
        offerDTO.setOfferRef( offer.getOfferRef() );

        return offerDTO;
    }

    @Override
    public Offer toEntity(OfferDTO offerDTO) {
        if ( offerDTO == null ) {
            return null;
        }

        Offer offer = new Offer();

        offer.setId( offerDTO.getId() );
        offer.setOfferRef( offerDTO.getOfferRef() );
        offer.setOrderDiscountAmount( offerDTO.getOrderDiscountAmount() );
        offer.setDescription( offerDTO.getDescription() );

        return offer;
    }
}
