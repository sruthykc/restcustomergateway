package com.diviso.graeshoppe.client.customer.domain;


import java.io.Serializable;
import java.util.Objects;

import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A FavouriteStore.
 */

@Document(indexName = "favouritestore")
public class FavouriteStore implements Serializable {

    private static final long serialVersionUID = 1L;
 
    private Long id;

    private Long storeId;

    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public FavouriteStore storeId(Long storeId) {
        this.storeId = storeId;
        return this;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public FavouriteStore customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FavouriteStore favouriteStore = (FavouriteStore) o;
        if (favouriteStore.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), favouriteStore.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FavouriteStore{" +
            "id=" + getId() +
            ", storeId=" + getStoreId() +
            "}";
    }
}
