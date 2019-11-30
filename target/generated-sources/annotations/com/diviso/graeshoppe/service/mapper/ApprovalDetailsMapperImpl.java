package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.client.order.model.ApprovalDetailsDTO;
import com.diviso.graeshoppe.client.order.model.aggregator.ApprovalDetails;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-30T10:42:07+0530",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_222 (Private Build)"
)
@Component
public class ApprovalDetailsMapperImpl implements ApprovalDetailsMapper {

    @Override
    public ApprovalDetails toEntity(ApprovalDetailsDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ApprovalDetails approvalDetails = new ApprovalDetails();

        approvalDetails.setAcceptedAt( dto.getAcceptedAt() );
        approvalDetails.setDecision( dto.getDecision() );
        approvalDetails.setExpectedDelivery( dto.getExpectedDelivery() );
        approvalDetails.setId( dto.getId() );

        return approvalDetails;
    }

    @Override
    public ApprovalDetailsDTO toDto(ApprovalDetails entity) {
        if ( entity == null ) {
            return null;
        }

        ApprovalDetailsDTO approvalDetailsDTO = new ApprovalDetailsDTO();

        approvalDetailsDTO.setAcceptedAt( entity.getAcceptedAt() );
        approvalDetailsDTO.setDecision( entity.getDecision() );
        approvalDetailsDTO.setExpectedDelivery( entity.getExpectedDelivery() );
        approvalDetailsDTO.setId( entity.getId() );

        return approvalDetailsDTO;
    }

    @Override
    public List<ApprovalDetails> toEntity(List<ApprovalDetailsDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ApprovalDetails> list = new ArrayList<ApprovalDetails>( dtoList.size() );
        for ( ApprovalDetailsDTO approvalDetailsDTO : dtoList ) {
            list.add( toEntity( approvalDetailsDTO ) );
        }

        return list;
    }

    @Override
    public List<ApprovalDetailsDTO> toDto(List<ApprovalDetails> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ApprovalDetailsDTO> list = new ArrayList<ApprovalDetailsDTO>( entityList.size() );
        for ( ApprovalDetails approvalDetails : entityList ) {
            list.add( toDto( approvalDetails ) );
        }

        return list;
    }
}
