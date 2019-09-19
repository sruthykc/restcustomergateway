package com.diviso.graeshoppe.client.customer.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Contact.
 */

@Document(indexName = "contact")
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;
    
   
    private Long id;

  
    private String mobileNumber;

   
    private String telephone;
    
	private String email;
	
	  public Contact email(String email) {
    this.email = email;
    return this;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public Contact mobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getTelephone() {
        return telephone;
    }

    public Contact telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Contact contact = (Contact) o;
    return Objects.equals(this.email, contact.email) &&
        Objects.equals(this.id, contact.id) &&
        Objects.equals(this.mobileNumber, contact.mobileNumber) &&
        Objects.equals(this.telephone, contact.telephone);
  }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Contact{" +
            "id=" + getId() +
            ", mobileNumber='" + getMobileNumber() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
