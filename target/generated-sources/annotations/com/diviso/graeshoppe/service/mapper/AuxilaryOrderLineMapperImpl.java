package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.client.order.model.AuxilaryOrderLineDTO;
import com.diviso.graeshoppe.client.order.model.aggregator.AuxilaryOrderLine;
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
public class AuxilaryOrderLineMapperImpl implements AuxilaryOrderLineMapper {

    @Override
    public List<AuxilaryOrderLine> toEntity(List<AuxilaryOrderLineDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AuxilaryOrderLine> list = new ArrayList<AuxilaryOrderLine>( dtoList.size() );
        for ( AuxilaryOrderLineDTO auxilaryOrderLineDTO : dtoList ) {
            list.add( toEntity( auxilaryOrderLineDTO ) );
        }

        return list;
    }

    @Override
    public List<AuxilaryOrderLineDTO> toDto(List<AuxilaryOrderLine> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AuxilaryOrderLineDTO> list = new ArrayList<AuxilaryOrderLineDTO>( entityList.size() );
        for ( AuxilaryOrderLine auxilaryOrderLine : entityList ) {
            list.add( toDto( auxilaryOrderLine ) );
        }

        return list;
    }

    @Override
    public AuxilaryOrderLineDTO toDto(AuxilaryOrderLine auxilaryOrderLine) {
        if ( auxilaryOrderLine == null ) {
            return null;
        }

        AuxilaryOrderLineDTO auxilaryOrderLineDTO = new AuxilaryOrderLineDTO();

        auxilaryOrderLineDTO.setId( auxilaryOrderLine.getId() );
        auxilaryOrderLineDTO.setPricePerUnit( auxilaryOrderLine.getPricePerUnit() );
        auxilaryOrderLineDTO.setProductId( auxilaryOrderLine.getProductId() );
        auxilaryOrderLineDTO.setQuantity( auxilaryOrderLine.getQuantity() );
        auxilaryOrderLineDTO.setTotal( auxilaryOrderLine.getTotal() );

        return auxilaryOrderLineDTO;
    }

    @Override
    public AuxilaryOrderLine toEntity(AuxilaryOrderLineDTO auxilaryOrderLineDTO) {
        if ( auxilaryOrderLineDTO == null ) {
            return null;
        }

        AuxilaryOrderLine auxilaryOrderLine = new AuxilaryOrderLine();

        auxilaryOrderLine.setId( auxilaryOrderLineDTO.getId() );
        auxilaryOrderLine.setPricePerUnit( auxilaryOrderLineDTO.getPricePerUnit() );
        auxilaryOrderLine.setProductId( auxilaryOrderLineDTO.getProductId() );
        auxilaryOrderLine.setQuantity( auxilaryOrderLineDTO.getQuantity() );
        auxilaryOrderLine.setTotal( auxilaryOrderLineDTO.getTotal() );

        return auxilaryOrderLine;
    }
}
