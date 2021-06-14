package com.example.demo.exceptions;

import com.example.demo.entity.Emprestimo;

public class EmprestimoValidadorException extends Exception{


    public static final String erroValorNegativo = "Valor negativo";
    public static final String erroEmprestimoRecursivo = "Id do devedor igual ao id do credor";


    public EmprestimoValidadorException(Emprestimo emprestimo) {
        super("Valor a ser pago é maior do que o storage do bolsao devedor(id:"+emprestimo.getIdBolsaoDevedor()+"), valor limite eh:"+emprestimo.getIdBolsaoCredor());
    }


    public EmprestimoValidadorException(String mensagem) {
        super(mensagem);
    }
}
