package com.diviso.graeshoppe.service.mapper;


import org.mapstruct.*;

import com.diviso.graeshoppe.client.order.model.aggregator.AuxilaryOrderLine;
import com.diviso.graeshoppe.client.order.model.AuxilaryOrderLineDTO;

/**
 * Mapper for the entity AuxilaryOrderLine and its DTO AuxilaryOrderLineDTO.
 */
@Mapper(componentModel = "spring", uses = {OrderLineMapper.class})
public interface AuxilaryOrderLineMapper extends EntityMapper<AuxilaryOrderLineDTO, AuxilaryOrderLine> {

    @Override
    AuxilaryOrderLineDTO toDto(AuxilaryOrderLine auxilaryOrderLine);

    @Override
    AuxilaryOrderLine toEntity(AuxilaryOrderLineDTO auxilaryOrderLineDTO);

    
}
