package com.example.demo.service;

import com.example.demo.entity.Bolsao;
import com.example.demo.exceptions.BolsaoNotFoundException;
import com.example.demo.exceptions.BolsaoValidadorException;
import com.example.demo.exceptions.EmprestimoNotFoundException;
import com.example.demo.exceptions.EmprestimoValidadorException;
import com.example.demo.repository.BolsaoRepository;

import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BolsaoService {

    @Autowired
    BolsaoRepository bolsaoRepository;
    
    @Autowired
    EmprestimoService emprestimoService;

    public ResponseEntity<Bolsao> getBolsaoResponse(long id){
        Optional<Bolsao> bolsaoOptional = bolsaoRepository.findById(id);
        if(bolsaoOptional.isPresent()){
            return new ResponseEntity<Bolsao>(bolsaoOptional.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public Bolsao getBolsao(long id) throws BolsaoNotFoundException{
        Optional<Bolsao> bolsaoOptional = bolsaoRepository.findById(id);
        if(bolsaoOptional.isPresent()){
            return bolsaoOptional.get();
        }
        throw new BolsaoNotFoundException(BolsaoNotFoundException.erroNotFoundBolsao,id);
    }
    
    public List<Bolsao> getAllBolsoes() {
    	
    	List<Bolsao> bolsoes = bolsaoRepository.findAll();
    	return bolsoes;
    }
    
    public Bolsao settle(long idDevedor, long idCredor, float quantity) throws BolsaoNotFoundException, EmprestimoValidadorException, EmprestimoNotFoundException {
        return emprestimoService.pagarEmprestimo(idDevedor,idCredor,quantity);
    }
    
    public JSONObject getJsonBolsao(Bolsao bolsao) {
    	JSONObject bolsaoJson = new JSONObject();
    	bolsaoJson.put("Id",bolsao.getIdBolsao());
    	bolsaoJson.put("storage", bolsao.getStorage());
    	bolsaoJson.put("name", bolsao.getName());
    	return bolsaoJson;
    }

	public Bolsao saveBolsao(Bolsao bolsao) throws BolsaoValidadorException {
        bolsao.validaBolsao();
		return bolsaoRepository.save(bolsao);
	}


}
