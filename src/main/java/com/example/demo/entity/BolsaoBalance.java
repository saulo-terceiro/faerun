package com.example.demo.entity;

import java.util.List;


public class BolsaoBalance {

	String idBolsao;
	float storage;
	List<OperationBalance> operationBalance;
	
	public String getIdBolsao() {
		return idBolsao;
	}
	public void setIdBolsao(String idBolsao) {
		this.idBolsao = idBolsao;
	}
	public float getStorage() {
		return storage;
	}
	public void setStorage(float storage) {
		this.storage = storage;
	}
	public List<OperationBalance> getOperationBalance() {
		return operationBalance;
	}
	public void setOperationBalance(List<OperationBalance> operationBalance) {
		this.operationBalance = operationBalance;
	}
}
