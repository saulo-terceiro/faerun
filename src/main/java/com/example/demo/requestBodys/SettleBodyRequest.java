package com.example.demo.requestBodys;

public class SettleBodyRequest {
	
	private long to;
	
	private float quantity;

	public SettleBodyRequest(Long idBolsao, float v) {
		this.to = idBolsao;
		this.quantity = v;
	}

	public long getTo() {
		return to;
	}

	public void setTo(long to) {
		this.to = to;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

}
