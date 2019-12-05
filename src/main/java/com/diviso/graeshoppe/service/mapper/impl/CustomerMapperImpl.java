package com.diviso.graeshoppe.service.mapper.impl;

/*import com.diviso.graeshoppe.client.customer.model.Contact;
import com.diviso.graeshoppe.client.customer.model.Customer;
import com.diviso.graeshoppe.client.customer.model.CustomerDTO;
import com.diviso.graeshoppe.service.mapper.ContactMapper;
import com.diviso.graeshoppe.service.mapper.CustomerMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-12-04T07:50:13+0530",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_222 (Private Build)"
)
@Component*/
public class CustomerMapperImpl /*implements CustomerMapper*/ {

  /*  @Autowired
    private ContactMapper contactMapper;

   // @Override
    public List<Customer> toEntity(List<CustomerDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Customer> list = new ArrayList<Customer>( dtoList.size() );
        for ( CustomerDTO customerDTO : dtoList ) {
            list.add( toEntity( customerDTO ) );
        }

        return list;
    }

  //  @Override
    public List<CustomerDTO> toDto(List<Customer> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<CustomerDTO> list = new ArrayList<CustomerDTO>( entityList.size() );
        for ( Customer customer : entityList ) {
            list.add( toDto( customer ) );
        }

        return list;
    }

   // @Override
    public CustomerDTO toDto(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();

        Long id = customerContactId( customer );
        if ( id != null ) {
            customerDTO.setContactId( id );
        }
        customerDTO.setCustomerUniqueId( customer.getCustomerUniqueId() );
        customerDTO.setId( customer.getId() );
        customerDTO.setIdpCode( customer.getIdpCode() );
        customerDTO.setIdpSub( customer.getIdpSub() );
            (@javax.validation.constraints.Pattern(regexp="^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$") ::
        byte[] image = customer.getImage();
        if ( image != null ) {
            customerDTO.setImage( Arrays.copyOf( image, image.length ) );
        }
        customerDTO.setImageContentType( customer.getImageContentType() );
        customerDTO.setImageLink( customer.getImageLink() );
        customerDTO.setName( customer.getName() );

        return customerDTO;
    }

  //  @Override
    public Customer toEntity(CustomerDTO customerDTO) {
        if ( customerDTO == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setContact( contactMapper.fromId( customerDTO.getContactId() ) );
        customer.setCustomerUniqueId( customerDTO.getCustomerUniqueId() );
        customer.setId( customerDTO.getId() );
        customer.setIdpCode( customerDTO.getIdpCode() );
        customer.setIdpSub( customerDTO.getIdpSub() );
            //(@javax.validation.constraints.Pattern(regexp="^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$") :: byte)[] image = customerDTO.getImage();
            byte[] image = customerDTO.getImage();
            if ( image != null ) {
            customer.setImage( Arrays.copyOf( image, image.length ) );
        }
        customer.setImageContentType( customerDTO.getImageContentType() );
        customer.setImageLink( customerDTO.getImageLink() );
        customer.setName( customerDTO.getName() );

        return customer;
    }

    private Long customerContactId(Customer customer) {
        if ( customer == null ) {
            return null;
        }
        Contact contact = customer.getContact();
        if ( contact == null ) {
            return null;
        }
        Long id = contact.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }*/
}
