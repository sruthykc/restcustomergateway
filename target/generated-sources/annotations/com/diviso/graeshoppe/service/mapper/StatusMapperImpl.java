package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.client.order.model.StatusDTO;
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
public class StatusMapperImpl implements StatusMapper {

    @Override
    public Status toEntity(StatusDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Status status = new Status();

        status.setId( dto.getId() );
        status.setName( dto.getName() );

        return status;
    }

    @Override
    public StatusDTO toDto(Status entity) {
        if ( entity == null ) {
            return null;
        }

        StatusDTO statusDTO = new StatusDTO();

        statusDTO.setId( entity.getId() );
        statusDTO.setName( entity.getName() );

        return statusDTO;
    }

    @Override
    public List<Status> toEntity(List<StatusDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Status> list = new ArrayList<Status>( dtoList.size() );
        for ( StatusDTO statusDTO : dtoList ) {
            list.add( toEntity( statusDTO ) );
        }

        return list;
    }

    @Override
    public List<StatusDTO> toDto(List<Status> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<StatusDTO> list = new ArrayList<StatusDTO>( entityList.size() );
        for ( Status status : entityList ) {
            list.add( toDto( status ) );
        }

        return list;
    }
}
