package com.example.demo.dtos;

public class DebtDto {


    public long id;
    public float quantity;

    public DebtDto(long idBolsaoDevedor, float quantity) {
        this.setId(idBolsaoDevedor);
        this.setQuantity(quantity);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}
