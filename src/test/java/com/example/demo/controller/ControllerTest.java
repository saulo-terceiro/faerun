package com.example.demo.controller;

import com.example.demo.dtos.BalanceBodyResponse;
import com.example.demo.dtos.BolsaoDtoResponse;
import com.example.demo.dtos.DebtsDtoResponse;
import com.example.demo.dtos.GetAllDto;
import com.example.demo.entity.Bolsao;
import com.example.demo.requestBodys.BorrowBodyRequest;
import com.example.demo.requestBodys.SettleBodyRequest;
import lombok.SneakyThrows;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.ArrayList;
import java.util.List;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ControllerTest {

    @Autowired
    BolsaoController bolsaoController ;

    @Test
    public void testPostBolsaoNegativo(){
        Bolsao bolsaoNegativo = new Bolsao("sauloteste",-1000);
        assertTrue( bolsaoController.post(bolsaoNegativo).getStatusCode()== HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testPostBolsaoSemNome(){
        Bolsao bolsaoSemNome = new Bolsao("",1000);
        assertTrue( bolsaoController.post(bolsaoSemNome).getStatusCode()== HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testPostBolsaoValido(){
        Bolsao bolsaoValido = new Bolsao("saulovalido",100.2f);
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsaoController.post(bolsaoValido).getBody();
        Bolsao bolsaoResponse = bolsaoDtoResponse.getBolsao();
        assertTrue(bolsaoResponse.getStorage()==100.2f);
        assertTrue(bolsaoResponse.getName().equals("saulovalido"));
    }

    @Test
    public void testGetBolsao(){
        Bolsao bolsao = new Bolsao("sauloteste",1000);
        bolsaoController.post(bolsao);
        ResponseEntity<Object> bolsao1 = bolsaoController.getById(bolsao.getIdBolsao());
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsao1.getBody();
        Bolsao bolsaoTeste = bolsaoDtoResponse.getBolsao();
        assertTrue(bolsao.getName().equals(bolsaoTeste.getName()));
        assertTrue(bolsao.getStorage()==bolsaoTeste.getStorage());

    }

    @Test
    public void testBorrowValido(){
        Bolsao bolsao = new Bolsao("sauloteste",1000);
        Bolsao bolsao2 = new Bolsao("sauloteste2",1000);
        bolsaoController.post(bolsao);
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsaoController.post(bolsao2).getBody();
        bolsao2.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        BorrowBodyRequest borrowBodyRequest = new BorrowBodyRequest(bolsao2.getIdBolsao(),100);
        BolsaoDtoResponse bolsaoDtoResponse2 = (BolsaoDtoResponse) bolsaoController.borrow(bolsao.getIdBolsao(),borrowBodyRequest).getBody();
        bolsao.setStorage(bolsao.getStorage()+100);
        bolsao2.setStorage(bolsao2.getStorage()-100);
        assertTrue(bolsaoDtoResponse2.equals(bolsao.getBolsaoDtoResponse()));
        BolsaoDtoResponse bolsaoDtoResponse3 = (BolsaoDtoResponse)bolsaoController.getById(bolsao2.getIdBolsao()).getBody();
        assertTrue(bolsaoDtoResponse3.equals(bolsao2.getBolsaoDtoResponse()));
    }

    @Test
    public void testBorrowNoStorage(){
        Bolsao bolsao = new Bolsao("sauloteste",1000);
        Bolsao bolsao2 = new Bolsao("sauloteste2",1000);
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsaoController.post(bolsao).getBody();
        bolsao.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        bolsaoController.post(bolsao2);
        bolsao2.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        BorrowBodyRequest borrowBodyRequest = new BorrowBodyRequest(bolsao2.getIdBolsao(),10000);
        assertTrue( bolsaoController.borrow(bolsao.getIdBolsao(),borrowBodyRequest).getStatusCode()== HttpStatus.BAD_REQUEST);

    }

    @Test
    public void testBorrowQuantityNegative(){
        Bolsao bolsao = new Bolsao("sauloteste",1000);
        Bolsao bolsao2 = new Bolsao("sauloteste2",1000);
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsaoController.post(bolsao).getBody();
        bolsao.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        bolsaoController.post(bolsao2);
        bolsao2.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        BorrowBodyRequest borrowBodyRequest = new BorrowBodyRequest(bolsao2.getIdBolsao(),-100);
        assertTrue(bolsaoController.borrow(bolsao.getIdBolsao(),borrowBodyRequest).getStatusCode()== HttpStatus.BAD_REQUEST);
        borrowBodyRequest.setQuantity(100);
    }

    @Test
    public void testBorrowQuantityNotFound() {
        Bolsao bolsao = new Bolsao("sauloteste",1000);
        Bolsao bolsao2 = new Bolsao("sauloteste2",1000);
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsaoController.post(bolsao).getBody();
        bolsao.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        bolsaoController.post(bolsao2);
        bolsao2.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        BorrowBodyRequest borrowBodyRequest = new BorrowBodyRequest(bolsao2.getIdBolsao(), -100);
        assertTrue(bolsaoController.borrow(bolsao.getIdBolsao()+10,borrowBodyRequest).getStatusCode()==HttpStatus.NOT_FOUND);

    }

    @Test
    public void testBorrowQuantityNotFoundFrom() {
        Bolsao bolsao = new Bolsao("sauloteste",1000);
        Bolsao bolsao2 = new Bolsao("sauloteste2",1000);
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsaoController.post(bolsao).getBody();
        bolsao.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        bolsaoController.post(bolsao2);
        bolsao2.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        BorrowBodyRequest borrowBodyRequest = new BorrowBodyRequest(bolsao2.getIdBolsao(), -100);
        borrowBodyRequest.setFrom(borrowBodyRequest.getFrom()+10);
        assertTrue(bolsaoController.borrow(bolsao.getIdBolsao(),borrowBodyRequest).getStatusCode()==HttpStatus.NOT_FOUND);
    }

    @SneakyThrows
    @Test
    public void testSettleValido(){
        Bolsao bolsao = new Bolsao("sauloteste",1000);
        Bolsao bolsao2 = new Bolsao("sauloteste2",1000);
        bolsaoController.post(bolsao);
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsaoController.post(bolsao2).getBody();
        bolsao2.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        BorrowBodyRequest borrowBodyRequest = new BorrowBodyRequest(bolsao2.getIdBolsao(),100);
        bolsaoController.borrow(bolsao.getIdBolsao(),borrowBodyRequest).getBody();
        bolsao.setStorage(bolsao.getStorage()+100);
        bolsao2.setStorage(bolsao2.getStorage()-100);
        SettleBodyRequest settleBodyRequest = new SettleBodyRequest(bolsao2.getIdBolsao(),100);
        bolsaoController.settle(bolsao.getIdBolsao(),settleBodyRequest).getBody();
        BolsaoDtoResponse bolsaoDtoResponse3 = (BolsaoDtoResponse)bolsaoController.getById(bolsao.getIdBolsao()).getBody();
        BolsaoDtoResponse bolsaoDtoResponse4 = (BolsaoDtoResponse)bolsaoController.getById(bolsao2.getIdBolsao()).getBody();
        bolsao.setStorage(bolsao.getStorage()-100);
        bolsao2.setStorage(bolsao2.getStorage()+100);
        assertTrue(bolsaoDtoResponse3.equals(bolsao.getBolsaoDtoResponse()));
        assertTrue(bolsaoDtoResponse4.equals(bolsao2.getBolsaoDtoResponse()));

    }

    @SneakyThrows
    @Test
    public void testSettleValidoNotFound() {
        Bolsao bolsao = new Bolsao("sauloteste", 1000);
        Bolsao bolsao2 = new Bolsao("sauloteste2", 1000);
        bolsaoController.post(bolsao);
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsaoController.post(bolsao2).getBody();
        bolsao2.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        BorrowBodyRequest borrowBodyRequest = new BorrowBodyRequest(bolsao2.getIdBolsao(), 100);
        bolsaoController.borrow(bolsao.getIdBolsao(), borrowBodyRequest).getBody();
        bolsao.setStorage(bolsao.getStorage() + 100);
        bolsao2.setStorage(bolsao2.getStorage() - 100);
        SettleBodyRequest settleBodyRequest = new SettleBodyRequest(bolsao2.getIdBolsao(), 100);
        assertTrue(bolsaoController.settle(bolsao.getIdBolsao() + 10, settleBodyRequest).getStatusCode() == HttpStatus.NOT_FOUND);
    }

    @SneakyThrows
    @Test
    public void testSettleValidoNotFoundTo() {
        Bolsao bolsao = new Bolsao("sauloteste", 1000);
        Bolsao bolsao2 = new Bolsao("sauloteste2", 1000);
        bolsaoController.post(bolsao);
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsaoController.post(bolsao2).getBody();
        bolsao2.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        BorrowBodyRequest borrowBodyRequest = new BorrowBodyRequest(bolsao2.getIdBolsao(), 100);
        bolsaoController.borrow(bolsao.getIdBolsao(), borrowBodyRequest).getBody();
        bolsao.setStorage(bolsao.getStorage() + 100);
        bolsao2.setStorage(bolsao2.getStorage() - 100);
        SettleBodyRequest settleBodyRequest = new SettleBodyRequest(bolsao2.getIdBolsao()+10, 100);
        assertTrue(bolsaoController.settle(bolsao.getIdBolsao(), settleBodyRequest).getStatusCode() == HttpStatus.NOT_FOUND);
    }

    @SneakyThrows
    @Test
    public void testSettleNoStorage() {
        Bolsao bolsao = new Bolsao("sauloteste", 1000);
        Bolsao bolsao2 = new Bolsao("sauloteste2", 1000);
        bolsaoController.post(bolsao);
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsaoController.post(bolsao2).getBody();
        bolsao2.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        BorrowBodyRequest borrowBodyRequest = new BorrowBodyRequest(bolsao2.getIdBolsao(), 100);
        bolsaoController.borrow(bolsao.getIdBolsao(), borrowBodyRequest).getBody();
        bolsao.setStorage(bolsao.getStorage() + 100);
        bolsao2.setStorage(bolsao2.getStorage() - 100);
        SettleBodyRequest settleBodyRequest = new SettleBodyRequest(bolsao2.getIdBolsao(), 10000);
        assertTrue(bolsaoController.settle(bolsao.getIdBolsao(),settleBodyRequest).getStatusCode()==HttpStatus.BAD_REQUEST);
    }


    public void testSettleValidoMenorValor() {
        Bolsao bolsao = new Bolsao("sauloteste", 1000);
        Bolsao bolsao2 = new Bolsao("sauloteste2", 1000);
        bolsaoController.post(bolsao);
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsaoController.post(bolsao2).getBody();
        bolsao2.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        BorrowBodyRequest borrowBodyRequest = new BorrowBodyRequest(bolsao2.getIdBolsao(), 100);
        bolsaoController.borrow(bolsao.getIdBolsao(), borrowBodyRequest).getBody();
        bolsao.setStorage(bolsao.getStorage() + 100);
        bolsao2.setStorage(bolsao2.getStorage() - 100);
        SettleBodyRequest settleBodyRequest = new SettleBodyRequest(bolsao2.getIdBolsao(), 40);

        settleBodyRequest.setQuantity(60);
        bolsaoController.settle(bolsao.getIdBolsao(), settleBodyRequest);
        bolsao.somarStorage(-60);
        bolsao2.somarStorage(60);
        BolsaoDtoResponse bolsaoDtoResponse6 = (BolsaoDtoResponse) bolsaoController.getById(bolsao.getIdBolsao()).getBody();
        BolsaoDtoResponse bolsaoDtoResponse7 = (BolsaoDtoResponse) bolsaoController.getById(bolsao2.getIdBolsao()).getBody();
        assertTrue(bolsaoDtoResponse6.equals(bolsao.getBolsaoDtoResponse()));
        assertTrue(bolsaoDtoResponse7.equals(bolsao2.getBolsaoDtoResponse()));
    }

    @SneakyThrows
    public void testSettleTriple() {
        Bolsao bolsao = new Bolsao("sauloteste", 1000);
        Bolsao bolsao2 = new Bolsao("sauloteste2", 1000);
        bolsaoController.post(bolsao);
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsaoController.post(bolsao2).getBody();
        bolsao2.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        BorrowBodyRequest borrowBodyRequest = new BorrowBodyRequest(bolsao2.getIdBolsao(), 100);
        bolsaoController.borrow(bolsao.getIdBolsao(), borrowBodyRequest).getBody();
        bolsao.somarStorage( 100);
        bolsao2.somarStorage(- 100);

        Bolsao bolsao3 = new Bolsao("sauloteste3",1000);
        BolsaoDtoResponse bolsaoDtoResponse1 =(BolsaoDtoResponse) bolsaoController.post(bolsao3).getBody();
        bolsao3 = bolsaoDtoResponse1.getBolsao();
        BorrowBodyRequest borrowBodyRequest2 = new BorrowBodyRequest(bolsao.getIdBolsao(),100);
        bolsaoController.borrow(bolsao3.getIdBolsao(),borrowBodyRequest2);
        bolsao.somarStorage(-100);
        bolsao3.somarStorage(100);
        bolsaoController.settle(bolsao3.getIdBolsao(),new SettleBodyRequest(bolsao.getIdBolsao(),100));
        bolsao3.somarStorage(-100);
        bolsao2.somarStorage(100);

        Bolsao bolsaoNovo = bolsaoController.getBolsao(bolsao.getIdBolsao());
        Bolsao bolsaoNovo2= bolsaoController.getBolsao(bolsao2.getIdBolsao());
        Bolsao bolsaoNovo3 = bolsaoController.getBolsao(bolsao3.getIdBolsao());
        assertTrue(bolsao.getStorage()==bolsaoNovo.getStorage());
        assertTrue(bolsao2.getStorage()==bolsaoNovo2.getStorage());
        assertTrue(bolsao3.getStorage()==bolsaoNovo3.getStorage());
    }

    @Test
    public void testGetAll(){
        Bolsao bolsao = new Bolsao("sauloteste",1000);
        Bolsao bolsao2 = new Bolsao("sauloteste2",1500);
        BolsaoDtoResponse bolsaoDtoResponse1 =(BolsaoDtoResponse)bolsaoController.post(bolsao).getBody();
        BolsaoDtoResponse bolsaoDtoResponse2 =(BolsaoDtoResponse)bolsaoController.post(bolsao2).getBody();
        bolsao = bolsaoDtoResponse1.getBolsao();
        bolsao2 = bolsaoDtoResponse2.getBolsao();
        GetAllDto getAllDto = (GetAllDto) bolsaoController.getAll().getBody();
        List<BolsaoDtoResponse> listBolsoes = (ArrayList)getAllDto.get("water-pockets");
        BolsaoDtoResponse bolsaoGet1 = listBolsoes.get(listBolsoes.size()-2);
        BolsaoDtoResponse bolsaoGet2 = listBolsoes.get(listBolsoes.size()-1);
        assertTrue(bolsaoGet1.getBolsao().equals(bolsao));
        assertTrue(bolsaoGet2.getBolsao().equals(bolsao2));
    }

    @Test
    public void testBalance(){
        BalanceBodyResponse balanceBodyResponse = (BalanceBodyResponse)bolsaoController.balance().getBody();
    }

    @Test
    public void testDebt(){
        Bolsao bolsao = new Bolsao("sauloteste",1000);
        Bolsao bolsao2 = new Bolsao("sauloteste2",1000);
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsaoController.post(bolsao).getBody();
        BolsaoDtoResponse bolsaoDtoResponse2 = (BolsaoDtoResponse) bolsaoController.post(bolsao2).getBody();
        bolsao.setIdBolsao(bolsaoDtoResponse.getBolsao().getIdBolsao());
        bolsao2.setIdBolsao(bolsaoDtoResponse2.getBolsao().getIdBolsao());

        BorrowBodyRequest borrowBodyRequest = new BorrowBodyRequest(bolsao2.getIdBolsao(),100);
        bolsaoController.borrow(bolsao.getIdBolsao(),borrowBodyRequest).getBody();
        bolsao.setStorage(bolsao.getStorage()+100);
        bolsao2.setStorage(bolsao2.getStorage()-100);
        DebtsDtoResponse debtsDtoResponse = (DebtsDtoResponse) bolsaoController.getEmprestimoByDevedor(bolsao.getIdBolsao()).getBody();
        assertTrue(debtsDtoResponse.getDebits().get(0).getQuantity()==100);
        assertTrue(debtsDtoResponse.getDebits().get(0).getId()==bolsao2.getIdBolsao());
    }

    @SneakyThrows
    @Test
    public void testSettleAllValido(){
        Bolsao bolsao = new Bolsao("sauloteste",1000);
        Bolsao bolsao2 = new Bolsao("sauloteste2",1000);
        BolsaoDtoResponse bolsaoDtoResponse = (BolsaoDtoResponse) bolsaoController.post(bolsao).getBody();
        BolsaoDtoResponse bolsaoDtoResponse2 = (BolsaoDtoResponse) bolsaoController.post(bolsao2).getBody();
        bolsao = bolsaoDtoResponse.getBolsao();
        bolsao2 = bolsaoDtoResponse2.getBolsao();

        BorrowBodyRequest borrowBodyRequest = new BorrowBodyRequest(bolsao2.getIdBolsao(),100);
        BolsaoDtoResponse bolsaoDtoResponse4 = (BolsaoDtoResponse) bolsaoController.borrow(bolsao.getIdBolsao(),borrowBodyRequest).getBody();
        bolsaoController.settleAll();
        assertTrue(bolsaoDtoResponse4.equals(bolsao.getBolsaoDtoResponse()));
        BolsaoDtoResponse bolsaoDtoResponse3 = (BolsaoDtoResponse)bolsaoController.getById(bolsao2.getIdBolsao()).getBody();
        assertTrue(bolsaoDtoResponse3.equals(bolsao2.getBolsaoDtoResponse()));

    }

}
