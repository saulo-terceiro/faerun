package com.example.demo.requestBodys;

public class BorrowBodyRequest {
	
	private long from;
	
	private float quantity;

    public BorrowBodyRequest(Long idBolsao, float i) {
    	this.from =idBolsao;
    	this.quantity=i;
    }

	public BorrowBodyRequest() {

	}

    public long getFrom() {
		return from;
	}

	public void setFrom(long from) {
		this.from = from;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

}
