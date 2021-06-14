package com.example.demo.dtos;

import com.example.demo.entity.Emprestimo;

import java.util.ArrayList;
import java.util.List;

public class DebtsDtoResponse {

    List<DebtDto> debits = new ArrayList<>();

    public DebtsDtoResponse(List<Emprestimo> dividasBolsao) {
        for(Emprestimo emprestimo : dividasBolsao){
            debits.add(new DebtDto(emprestimo.getIdBolsaoCredor(),emprestimo.getQuantity()));
        }
    }

    public List<DebtDto> getDebits() {
        return debits;
    }

    public void setDebits(List<DebtDto> debits) {
        this.debits = debits;
    }
}
