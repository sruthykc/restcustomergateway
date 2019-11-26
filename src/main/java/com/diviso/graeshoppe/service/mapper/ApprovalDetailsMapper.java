package com.diviso.graeshoppe.service.mapper;


import org.mapstruct.*;

import com.diviso.graeshoppe.client.order.model.ApprovalDetails;
import com.diviso.graeshoppe.client.order.model.ApprovalDetailsDTO;

/**
 * Mapper for the entity ApprovalDetails and its DTO ApprovalDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApprovalDetailsMapper extends EntityMapper<ApprovalDetailsDTO, ApprovalDetails> {



    default ApprovalDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApprovalDetails approvalDetails = new ApprovalDetails();
        approvalDetails.setId(id);
        return approvalDetails;
    }
}
