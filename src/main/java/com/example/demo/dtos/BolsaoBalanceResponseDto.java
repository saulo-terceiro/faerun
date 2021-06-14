package com.example.demo.dtos;

import java.util.List;

public class BolsaoBalanceResponseDto {

    String Id;
    float storage;
    List<OperationBalanceResponseDto> Operations;


    public BolsaoBalanceResponseDto(String id,List<OperationBalanceResponseDto> operationBalanceResponsesList, float storage) {
        this.setId(id);
        this.Operations = operationBalanceResponsesList;
        this.storage = storage;
    }

    public float getStorage() {
        return storage;
    }

    public void setStorage(float storage) {
        this.storage = storage;
    }

    public List<OperationBalanceResponseDto> getOperations() {
        return Operations;
    }

    public void setOperations(List<OperationBalanceResponseDto> operations) {
        Operations = operations;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
