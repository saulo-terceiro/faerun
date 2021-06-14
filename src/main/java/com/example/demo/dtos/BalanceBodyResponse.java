package com.example.demo.dtos;

import com.example.demo.entity.BolsaoBalance;
import com.example.demo.entity.OperationBalance;

import java.util.*;


public class BalanceBodyResponse {
	List<BolsaoBalanceResponseDto> balance = new ArrayList<>();

	public BalanceBodyResponse(List<BolsaoBalance> bolsoesBalance) {
		for(BolsaoBalance bolsaoBalance : bolsoesBalance) {
			List<OperationBalanceResponseDto> operationBalanceResponsesList = new ArrayList<>();
			for (OperationBalance operationBalance : bolsaoBalance.getOperationBalance()) {
				OperationBalanceResponseDto operationBalanceResponse = new OperationBalanceResponseDto(operationBalance);
				operationBalanceResponsesList.add(operationBalanceResponse);
			}
			BolsaoBalanceResponseDto bolsaoBalanceResponseDto = new BolsaoBalanceResponseDto(bolsaoBalance.getIdBolsao().toString(),operationBalanceResponsesList, bolsaoBalance.getStorage());
			balance.add( bolsaoBalanceResponseDto);

		}
	}

	public List<BolsaoBalanceResponseDto> getBalance() {
		return balance;
	}

	public void setBalance(List<BolsaoBalanceResponseDto> balance) {
		this.balance = balance;
	}
}
