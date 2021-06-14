package com.example.demo.controller;

import com.example.demo.dtos.*;
import com.example.demo.entity.BolsaoBalance;
import com.example.demo.entity.Bolsao;
import com.example.demo.entity.Emprestimo;
import com.example.demo.exceptions.*;
import com.example.demo.requestBodys.BorrowBodyRequest;
import com.example.demo.requestBodys.SettleBodyRequest;
import com.example.demo.service.BolsaoService;
import com.example.demo.service.EmprestimoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BolsaoController {
    @Autowired
    private BolsaoService bolsaoService;

    @Autowired
    private EmprestimoService emprestimoService;

    @RequestMapping(value = "/water-pockets", method = RequestMethod.POST)
    public ResponseEntity<Object> post(@RequestBody Bolsao bolsao) {
        try {
            BolsaoDtoResponse bolsaoDtoResponse = new BolsaoDtoResponse(bolsaoService.saveBolsao((bolsao)));
            return ResponseEntity.ok(bolsaoDtoResponse);
        }catch (BolsaoValidadorException t){
            ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse(t.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDtoResponse);
        }
    }

    @RequestMapping(value = "/water-pockets", method = RequestMethod.GET)
    public ResponseEntity<Object> getAll() {
        GetAllDto getAllDto = new GetAllDto(bolsaoService.getAllBolsoes());

        return ResponseEntity.ok(getAllDto);
    }

    @RequestMapping(value = "/water-pockets/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getById(@PathVariable(value = "id") long id) {
        try {
            Bolsao bolsao = bolsaoService.getBolsao(id);
            return ResponseEntity.ok(new BolsaoDtoResponse(bolsao));
        }catch (BolsaoNotFoundException t){
            ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse(t.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDtoResponse);
        }
    }

    @RequestMapping(value = "/water-pockets/{id}/debt", method = RequestMethod.GET)
    public ResponseEntity<Object> getEmprestimoByDevedor(@PathVariable(value = "id") long id) {
        try {
            return ResponseEntity.ok(new DebtsDtoResponse(emprestimoService.getDividasBolsao(id)));
        }catch (BolsaoNotFoundException t){
            ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse(t.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDtoResponse);
        }
    }
    
    @RequestMapping(value = "/water-pockets/balance", method = RequestMethod.GET)
    public ResponseEntity<BalanceBodyResponse> balance() {

        List<BolsaoBalance> bolsoesDto = emprestimoService.balance();
        BalanceBodyResponse balanceBodyResponse = new BalanceBodyResponse(bolsoesDto);
        return ResponseEntity.ok(balanceBodyResponse);
    }

    @RequestMapping(value = "/water-pockets/{id}/borrow", method = RequestMethod.POST)
    public ResponseEntity<Object> borrow(@PathVariable (required = true) long id, @RequestBody BorrowBodyRequest borrowRequest){
        try {
            BolsaoDtoResponse bolsaoDtoResponse = new BolsaoDtoResponse(emprestimoService.borrow(id,borrowRequest.getFrom(),borrowRequest.getQuantity()));
            return ResponseEntity.ok(bolsaoDtoResponse);
        }catch (NotFoundException t){
            ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse(t.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDtoResponse);
        }catch (EmprestimoValidadorException t){
            ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse(t.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDtoResponse);
        }
    }

    @RequestMapping(value = "/water-pockets/{id}/settle", method = RequestMethod.POST)
    public ResponseEntity<Object> settle(@PathVariable (required = true) long id, @RequestBody SettleBodyRequest settleRequest) {
        try {
            BolsaoDtoResponse bolsaoDtoResponse = new BolsaoDtoResponse(bolsaoService.settle(id, settleRequest.getTo(), settleRequest.getQuantity()));
            return ResponseEntity.ok(bolsaoDtoResponse);
        }catch (NotFoundException t){
            ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse(t.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDtoResponse);
        }catch (EmprestimoValidadorException t){
            ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse(t.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDtoResponse);
        }
    }
    @RequestMapping(value = "/water-pockets/settle-all", method = RequestMethod.POST)
    public ResponseEntity<Object> settleAll() throws BolsaoNotFoundException {
        try {
            return ResponseEntity.ok(emprestimoService.pagarTodosEmprestimos());
        } catch (NotFoundException t) {
            ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse(t.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDtoResponse);        }
    }
    
    @RequestMapping(value = "/water-pockets/emprestimos", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Emprestimo>> emprestimos() {
        return ResponseEntity.ok(emprestimoService.emprestimos());
    }

    public Bolsao getBolsao(long id) throws BolsaoNotFoundException {
        return bolsaoService.getBolsao(id);
    }









}
