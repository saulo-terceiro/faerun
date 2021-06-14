package com.example.demo.dtos;

public class OperationSettleDto {

        String operation;
        long id;
        float quantity;

    public OperationSettleDto(String tipoOperationSend, Long idBolsao, float quantity) {
        this.operation = tipoOperationSend;
        this.id = idBolsao;
        this.quantity = quantity;
    }


    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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
