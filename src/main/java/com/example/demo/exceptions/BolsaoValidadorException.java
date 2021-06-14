package com.example.demo.exceptions;

public class BolsaoValidadorException extends Exception {

    public static final String erroStorageNegativo = "Bolsao com storage negativo";
    public static final String erroNomeVazio = "Bolsao com nome vazio";

    public static final String erroStorageVazio = "Bolsao com storage vazio";


    public BolsaoValidadorException(String mensagem) {
        super(mensagem);
    }
}
