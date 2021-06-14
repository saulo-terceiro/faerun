package com.example.demo.dtos;

import com.example.demo.entity.Bolsao;

import java.util.HashMap;
import java.util.TreeMap;

public class BolsaoDtoResponse extends TreeMap {

    public BolsaoDtoResponse(Bolsao saveBolsao) {
        this.put("Id",saveBolsao.getIdBolsao());
        //Id é palavra reservada, acaba dando erro se for gerar pelo proprio objeto
        this.put("name",saveBolsao.getName());
        this.put("storage",saveBolsao.getStorage());
    }

    public Bolsao getBolsao(){
        return new Bolsao((long)(this.get("Id")),(String)this.get("name"),(float)this.get("storage"));
    }

    public boolean equals(Object object){
        try {
            BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) object;
            if(bolsaoDtoResponse.getBolsao().equals(this.getBolsao()) && bolsaoDtoResponse.getBolsao().equalsDadosSemId(this.getBolsao())){
                return true;
            }else{
                return false;
            }
        }catch (Throwable t){
            return false;
        }
    }

}
