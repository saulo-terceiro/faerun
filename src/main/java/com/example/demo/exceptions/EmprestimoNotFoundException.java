package com.example.demo.exceptions;

public class EmprestimoNotFoundException extends NotFoundException{

    public static final String emprestimoNotFoundError = "Emprestimo não encontrado para as especificações";


    public EmprestimoNotFoundException() {
        super(emprestimoNotFoundError);
    }
}
