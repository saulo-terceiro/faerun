package com.example.demo.entity;

import javax.persistence.*;

import com.example.demo.exceptions.BolsaoNotFoundException;
import com.example.demo.exceptions.EmprestimoValidadorException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Emprestimo {

    public static String TIPO_PAGAMENTO_TODOS = "T";
    public static String TIPO_PAGAMENTO_EMPRESTIMO_INDIVIDUAL = "I";

    public static String TIPO_OPERATION_SEND = "send";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEmprestimo;

    @OneToOne
    private Bolsao bolsaoDevedor;

    @OneToOne
    private Bolsao bolsaoCredor;

    private float quantity;

    public Emprestimo(Bolsao bolsaoDevedor, Bolsao bolsaoCredor) {
        this.bolsaoDevedor = bolsaoDevedor;
        this.bolsaoCredor = bolsaoCredor;

    }
    
    public Emprestimo(Bolsao bolsaoDevedor, Bolsao bolsaoCredor,float quantity) {
        this.bolsaoDevedor = bolsaoDevedor;
        this.bolsaoCredor = bolsaoCredor;
        this.quantity = quantity;

    }

    public Emprestimo() {

    }

    public Emprestimo(Emprestimo emprestimo) {
        this.setBolsaoCredor(emprestimo.getBolsaoCredor());
        this.setBolsaoDevedor(emprestimo.getBolsaoDevedor());
        this.setIdEmprestimo(emprestimo.getIdEmprestimo());
        this.setQuantity(emprestimo.getQuantity());
    }

    public long getIdEmprestimo() {
        return idEmprestimo;
    }

    public void setIdEmprestimo(long idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
    }

    public Bolsao getBolsaoDevedor() {
        return this.bolsaoDevedor;
    }

    public void setBolsaoDevedor(Bolsao bolsaoDevedor) {
        this.bolsaoDevedor = bolsaoDevedor;
    }

    public Bolsao getBolsaoCredor() {
        return bolsaoCredor;
    }

    public void setBolsaoCredor(Bolsao bolsaoCredor) {
        this.bolsaoCredor = bolsaoCredor;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void validaEmprestimoASerPago(float valorASerPago) throws EmprestimoValidadorException {
        if(valorASerPago>this.getBolsaoDevedor().getStorage()){
            throw new EmprestimoValidadorException(this);
        }
        if(valorASerPago<0){
            throw new EmprestimoValidadorException(EmprestimoValidadorException.erroValorNegativo);
        }
        if(this.getBolsaoDevedor().equals(this.getBolsaoCredor())){
            throw new EmprestimoValidadorException(EmprestimoValidadorException.erroEmprestimoRecursivo);
        }
    }

    public long getIdBolsaoDevedor(){
        return this.bolsaoDevedor.getIdBolsao();
    }

    public long getIdBolsaoCredor(){
        return this.bolsaoCredor.getIdBolsao();
    }

    public boolean isCredor(long bolsao){
        return this.getIdBolsaoCredor() == bolsao;
    }

    private void validarEmprestimo(Emprestimo emprestimo) throws EmprestimoValidadorException, BolsaoNotFoundException {
        Bolsao bolsaoDevedor = emprestimo.getBolsaoDevedor();
        Bolsao bolsaoCredor = emprestimo.getBolsaoCredor();
        float quantity = emprestimo.getQuantity();
        if(quantity<0){
            throw new EmprestimoValidadorException(EmprestimoValidadorException.erroValorNegativo);
        }
        if(bolsaoDevedor==null){
            throw new BolsaoNotFoundException(bolsaoDevedor.getIdBolsao());
        }
        if(bolsaoCredor==null){
            throw new BolsaoNotFoundException(bolsaoCredor.getIdBolsao());
        }
        if(bolsaoCredor.getStorage()<quantity){
            throw new EmprestimoValidadorException(bolsaoCredor.getName()+" possui estoque atual de "+bolsaoCredor.getStorage());
        }
    }
}
