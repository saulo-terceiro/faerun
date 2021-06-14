package com.example.demo.exceptions;

public class BolsaoNotFoundException extends NotFoundException {

    public static final String erroNotFoundBolsao = "Bolsao não existe, Id:";

    public BolsaoNotFoundException(String mensagem) {
        super(mensagem);
    }

    public BolsaoNotFoundException(String mensagem,long idBolsao) {
        super(mensagem+idBolsao);
    }

    public BolsaoNotFoundException(long idBolsao) {
        super(erroNotFoundBolsao+idBolsao);
    }

}
