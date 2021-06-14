package com.example.demo.dtos;

import com.example.demo.entity.OperationBalance;

public class OperationBalanceResponseDto {
    String operation;
    long destinationId;
    float quantity;

    public OperationBalanceResponseDto(OperationBalance operationBalance) {
        this.operation = operationBalance.getTipoOperation();
        this.destinationId = operationBalance.getDestination().getIdBolsao();
        this.quantity = operationBalance.getQuantity();
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(long destinationId) {
        this.destinationId = destinationId;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}
