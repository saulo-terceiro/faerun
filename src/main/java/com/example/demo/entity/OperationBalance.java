package com.example.demo.entity;


public class OperationBalance {

	
	String tipoOperation;
	Bolsao destination;
	float quantity;


	public static String TIPO_PAY = "pay";
	public static String TIPO_RECEIVE = "receive";





	public String getTipoOperation() {
		return tipoOperation;
	}
	public void setTipoOperation(String tipoOperation) {
		this.tipoOperation = tipoOperation;
	}
	public Bolsao getDestination() {
		return destination;
	}
	public void setDestination(Bolsao bolsao) {
		this.destination = bolsao;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	
	
}
