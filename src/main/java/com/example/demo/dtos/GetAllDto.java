package com.example.demo.dtos;

import com.example.demo.entity.Bolsao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetAllDto extends HashMap {

    public GetAllDto(List<Bolsao> allBolsoes) {
        List<BolsaoDtoResponse> bolsaoDtoResponsesList = new ArrayList<>();
        for(Bolsao bolsao : allBolsoes){
            bolsaoDtoResponsesList.add(new BolsaoDtoResponse(bolsao));
        }
        this.put("water-pockets",bolsaoDtoResponsesList);

    }

}
