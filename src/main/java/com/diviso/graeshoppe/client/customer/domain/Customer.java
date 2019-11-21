package com.diviso.graeshoppe.client.customer.domain;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * A Customer.
 */


public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    
   
    private Long id;

    
    private String reference;

   
    private String name;

   
    private String searchKey;

    
    private String card;

   
    private Double curDebt;

   
    private LocalDate debtDate;

    
    private Double maxDebt;

   
    private Double discount;

   
    private Boolean visible;

  
    private byte[] photo;

   
    private String photoContentType;

    
    private Contact contact;

   
    private Set<Address> addresses = new HashSet<>();
    
    private Set<Note> notes = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public Customer reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public Customer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public Customer searchKey(String searchKey) {
        this.searchKey = searchKey;
        return this;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getCard() {
        return card;
    }

    public Customer card(String card) {
        this.card = card;
        return this;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public Double getCurDebt() {
        return curDebt;
    }

    public Customer curDebt(Double curDebt) {
        this.curDebt = curDebt;
        return this;
    }

    public void setCurDebt(Double curDebt) {
        this.curDebt = curDebt;
    }

    public LocalDate getDebtDate() {
        return debtDate;
    }

    public Customer debtDate(LocalDate debtDate) {
        this.debtDate = debtDate;
        return this;
    }

    public void setDebtDate(LocalDate debtDate) {
        this.debtDate = debtDate;
    }

    public Double getMaxDebt() {
        return maxDebt;
    }

    public Customer maxDebt(Double maxDebt) {
        this.maxDebt = maxDebt;
        return this;
    }

    public void setMaxDebt(Double maxDebt) {
        this.maxDebt = maxDebt;
    }

    public Double getDiscount() {
        return discount;
    }

    public Customer discount(Double discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Boolean isVisible() {
        return visible;
    }

    public Customer visible(Boolean visible) {
        this.visible = visible;
        return this;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public Customer photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public Customer photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Contact getContact() {
        return contact;
    }

    public Customer contact(Contact contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public Customer addresses(Set<Address> addresses) {
        this.addresses = addresses;
        return this;
    }

    public Customer addAddress(Address address) {
        this.addresses.add(address);
        address.setCustomer(this);
        return this;
    }

    public Customer removeAddress(Address address) {
        this.addresses.remove(address);
        address.setCustomer(null);
        return this;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public Customer notes(Set<Note> notes) {
        this.notes = notes;
        return this;
    }

    public Customer addNote(Note note) {
        this.notes.add(note);
        note.setCustomer(this);
        return this;
    }

    public Customer removeNote(Note note) {
        this.notes.remove(note);
        note.setCustomer(null);
        return this;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
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
        Customer customer = (Customer) o;
        if (customer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", name='" + getName() + "'" +
            ", searchKey='" + getSearchKey() + "'" +
            ", card='" + getCard() + "'" +
            ", curDebt=" + getCurDebt() +
            ", debtDate='" + getDebtDate() + "'" +
            ", maxDebt=" + getMaxDebt() +
            ", discount=" + getDiscount() +
            ", visible='" + isVisible() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            "}";
    }
}
