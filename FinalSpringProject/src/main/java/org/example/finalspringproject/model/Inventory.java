package org.example.finalspringproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "inventories")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference("inventory-product")
    private Product product;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @JsonBackReference("inventory-store")
    private Store store;

    @NotNull
    @Min(0)
    private Integer stockLevel;

    public Inventory() {
    }

    public Inventory(Product product, Store store, Integer stockLevel) {
        this.product = product;
        this.store = store;
        this.stockLevel = stockLevel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Integer getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(Integer stockLevel) {
        this.stockLevel = stockLevel;
    }
}
