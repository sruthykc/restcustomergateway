package com.diviso.graeshoppe.client.customer.domain;


import java.io.Serializable;
import java.util.Objects;

/**
 * A FavouriteProduct.
 */

public class FavouriteProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    

    private Long id;

    private Long productId;

    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public FavouriteProduct productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public FavouriteProduct customer(Customer customer) {
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
        FavouriteProduct favouriteProduct = (FavouriteProduct) o;
        if (favouriteProduct.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), favouriteProduct.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FavouriteProduct{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            "}";
    }
}
