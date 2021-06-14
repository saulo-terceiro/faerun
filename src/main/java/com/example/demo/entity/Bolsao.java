package com.example.demo.entity;



import javax.persistence.*;
import javax.xml.ws.Response;

import com.example.demo.dtos.BolsaoDtoResponse;
import com.example.demo.exceptions.BolsaoValidadorException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.ResponseEntity;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Bolsao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idBolsao;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private float storage;

    public Bolsao(String nome, float storage) {
        this.name = nome;
        this.storage = storage;

    }

    public Bolsao() {

    }

    public Bolsao(long id, String name, float storage) {
        this.setIdBolsao(id);
        this.setName(name);
        this.setStorage(storage);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getStorage() {
        return storage;
    }

    public void setStorage(float storage) {
        this.storage = storage;
    }



    public Long getIdBolsao() {
        return idBolsao;
    }

    public BolsaoDtoResponse getBolsaoDtoResponse(){
        return  new BolsaoDtoResponse(this);
    }

    public void setIdBolsao(Long idBolsao) {
        this.idBolsao = idBolsao;
    }

    public ResponseEntity<Bolsao> validaBolsao() throws BolsaoValidadorException {
        try {
            if(this.getStorage()<0){
                throw new BolsaoValidadorException(BolsaoValidadorException.erroStorageNegativo);
            }else if(this.name.equals("") || this.name.equals(null)){
                throw new BolsaoValidadorException(BolsaoValidadorException.erroNomeVazio);
            }
        }catch (NullPointerException n){
            throw new BolsaoValidadorException(BolsaoValidadorException.erroStorageVazio);
        }

        return null;
    }

    public boolean equals(Object bolsao){
        try{
            Bolsao bolsao1 = (Bolsao) bolsao;
            if(this.getIdBolsao().equals(bolsao1.getIdBolsao())){
                return true;
            }

        }catch (Throwable t){
            return false;
        }
        return false;
    }
    public boolean equalsDadosSemId(Bolsao bolsao){
        if(this.getName().equals(bolsao.getName()) && this.getStorage()==bolsao.getStorage()){
            return true;
        }else{
            return false;
        }
    }

    public void somarStorage(float i) {
        this.setStorage(this.getStorage()+i);
    }
}
