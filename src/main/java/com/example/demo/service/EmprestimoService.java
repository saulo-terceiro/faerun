package com.example.demo.service;

import com.example.demo.dtos.OperationResponseDto;
import com.example.demo.dtos.OperationSettleDto;
import com.example.demo.entity.BolsaoBalance;
import com.example.demo.entity.OperationBalance;
import com.example.demo.entity.Bolsao;
import com.example.demo.entity.Emprestimo;
import com.example.demo.exceptions.BolsaoNotFoundException;
import com.example.demo.exceptions.EmprestimoNotFoundException;
import com.example.demo.exceptions.EmprestimoValidadorException;
import com.example.demo.repository.BolsaoRepository;
import com.example.demo.repository.EmprestimoRepository;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class EmprestimoService {


    @Autowired
    BolsaoService bolsaoService;

    @Autowired
    EmprestimoRepository emprestimoRepository;

    @Autowired
    BolsaoRepository bolsaoRepository;

    public List<Emprestimo> getDividasBolsao(long id) throws BolsaoNotFoundException {
        Bolsao bolsaoDevedor = bolsaoService.getBolsao(id);

        List<Emprestimo> emprestimos = emprestimoRepository.findAllByBolsaoDevedorOrderByIdEmprestimo(bolsaoDevedor);
        return emprestimos;
    }
    
    public JSONObject getEmprestimoJson(Emprestimo emprestimo) {
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put("id", emprestimo.getBolsaoCredor().getIdBolsao());
    	jsonObject.put("quantity", emprestimo.getQuantity());
    	return jsonObject;
    	
    }

    public Bolsao criarEmprestimo(long id, long idBolsaoCredor, float quantity) throws BolsaoNotFoundException, EmprestimoValidadorException {
        Bolsao bolsaoDevedor = bolsaoService.getBolsao(id);
        Bolsao bolsaoCredor = bolsaoService.getBolsao(idBolsaoCredor);
        Emprestimo novoEmprestimo = new Emprestimo(bolsaoDevedor,bolsaoCredor,quantity);
        novoEmprestimo.validaEmprestimoASerPago(quantity);
        this.transferenciaBolsao(quantity,bolsaoCredor,bolsaoDevedor);
        Emprestimo emprestimoAntigo = emprestimoRepository. findEmprestimoByBolsaoCredorAndAndBolsaoDevedor(bolsaoCredor, bolsaoDevedor);
        if(emprestimoAntigo==null) {
            this.salvarEmprestimo(novoEmprestimo,bolsaoDevedor,bolsaoCredor,quantity);
        }else {
        	emprestimoAntigo.setQuantity(emprestimoAntigo.getQuantity()+novoEmprestimo.getQuantity());
        	emprestimoRepository.save(emprestimoAntigo);
        }
        return bolsaoDevedor;
    }

    private void transferenciaBolsao(float quantity, Bolsao bolsaoCredor, Bolsao bolsaoDevedor) {
        this.somarStorageBolsao(quantity,bolsaoDevedor);
        this.subtrairStorageBolsao(quantity,bolsaoCredor);
    }

    public Bolsao pagarEmprestimo(long idBolsaoDevedor, long idBolsaoCredor, float valorASerPago) throws BolsaoNotFoundException, EmprestimoValidadorException, EmprestimoNotFoundException {
        Bolsao bolsaoCredor = bolsaoService.getBolsao(idBolsaoCredor);
        Bolsao bolsaoDevedor = bolsaoService.getBolsao(idBolsaoDevedor);

        Emprestimo aSerPagoEmprestimo = emprestimoRepository.findEmprestimoByBolsaoCredorAndAndBolsaoDevedor(bolsaoCredor,bolsaoDevedor);
        if(aSerPagoEmprestimo==null){
            throw new EmprestimoNotFoundException();
        }
        aSerPagoEmprestimo.validaEmprestimoASerPago(valorASerPago);
        this.somarStorageBolsaoPagamento(valorASerPago,bolsaoCredor,Emprestimo.TIPO_PAGAMENTO_EMPRESTIMO_INDIVIDUAL);
        this.subtrairStorageBolsao(valorASerPago,bolsaoDevedor);
        this.somarEmprestimo(aSerPagoEmprestimo,valorASerPago);
        return bolsaoDevedor;
    }


    public OperationResponseDto pagarTodosEmprestimos() throws BolsaoNotFoundException, EmprestimoNotFoundException {
        List<Bolsao> bolsoesParaPagar = bolsaoRepository.bolsoesContemDividasEStorage();
        OperationResponseDto operationResponseDto = this.pagarBolsoes(bolsoesParaPagar);
        return operationResponseDto;
    }

    public OperationResponseDto pagarBolsoes(List<Bolsao> bolsoesParaPagar) throws BolsaoNotFoundException, EmprestimoNotFoundException {
        OperationResponseDto operationResponseDto = new OperationResponseDto();
        List<HashMap<String,List<OperationSettleDto>>> operationsJsonObject = new ArrayList<HashMap<String,List<OperationSettleDto>>>();
        JSONObject storageJson = new JSONObject();

        for(Bolsao bolsaoParaPagar : bolsoesParaPagar){
            HashMap<String,List<OperationSettleDto>> operationsHash = new HashMap<String,List<OperationSettleDto>>();
            operationsHash.put(bolsaoParaPagar.getIdBolsao().toString(),somarStorageBolsaoPagamentoJson(bolsaoParaPagar.getStorage(),bolsaoParaPagar));
            operationsJsonObject.add(operationsHash);
            storageJson.put(bolsaoParaPagar.getIdBolsao().toString(),bolsaoParaPagar.getStorage());
        }
        operationResponseDto.put("operations", operationsJsonObject);
        operationResponseDto.put("storage", storageJson);
        return operationResponseDto;
    }



    public void somarEmprestimo(Emprestimo aSerPagoEmprestimo, float valorASerPago){
        aSerPagoEmprestimo.setQuantity(aSerPagoEmprestimo.getQuantity()-valorASerPago);
        if(aSerPagoEmprestimo.getQuantity()<=0){
            emprestimoRepository.delete(aSerPagoEmprestimo);
        }else{
            emprestimoRepository.save(aSerPagoEmprestimo);
        }
    }




	private void salvarEmprestimo(Emprestimo novoEmprestimo, Bolsao bolsaoDevedor, Bolsao bolsaoCredor, float quantity) {
        novoEmprestimo.setBolsaoDevedor(bolsaoDevedor);
        novoEmprestimo.setBolsaoCredor(bolsaoCredor);
        novoEmprestimo.setQuantity(quantity);
        emprestimoRepository.save(novoEmprestimo);

    }

    public void somarStorageBolsao(float quantity,Bolsao bolsao){
        bolsao.setStorage(bolsao.getStorage()+quantity);
        bolsaoRepository.save(bolsao);
    }

    public void subtrairStorageBolsao(float quantity, Bolsao bolsao){

        bolsao.setStorage(bolsao.getStorage()-quantity);
        bolsaoRepository.save(bolsao);
    }

    public void somarStorageBolsaoPagamento(float quantity, Bolsao bolsao, String tipoOperacao){
        List<Emprestimo> emprestimos = emprestimoRepository.findAllByBolsaoDevedorOrderByIdEmprestimo(bolsao);
        for(Emprestimo emprestimo : emprestimos){
            if(bolsao.getStorage()>=emprestimo.getQuantity()){
                if(Emprestimo.TIPO_PAGAMENTO_TODOS.equals(tipoOperacao)){
                    this.somarStorageBolsaoPagamento(bolsao.getStorage(),emprestimo.getBolsaoCredor(),Emprestimo.TIPO_PAGAMENTO_TODOS);
                }else{
                    this.somarStorageBolsaoPagamento(emprestimo.getQuantity(),emprestimo.getBolsaoCredor(),Emprestimo.TIPO_PAGAMENTO_EMPRESTIMO_INDIVIDUAL);
                    this.subtrairStorageBolsao(emprestimo.getQuantity(),emprestimo.getBolsaoDevedor());
                    this.somarEmprestimo(emprestimo,emprestimo.getQuantity());
                }
                quantity = quantity-emprestimo.getQuantity();
            }
        }

        //Só salva se tiver algo para subtrair no bolsao
        if(quantity>0){
            if(Emprestimo.TIPO_PAGAMENTO_TODOS.equals(tipoOperacao)){
                bolsao.setStorage(quantity);
            }else{
                bolsao.setStorage(bolsao.getStorage()+quantity);
            }
            bolsaoRepository.save(bolsao);
        }
    }

	public List<OperationSettleDto> somarStorageBolsaoPagamentoJson(float quantity, Bolsao bolsao) throws BolsaoNotFoundException, EmprestimoNotFoundException {
        List<Emprestimo> emprestimos = emprestimoRepository.findAllByBolsaoDevedorOrderByIdEmprestimo(bolsao);
        List<OperationSettleDto> listaOperationSettleDto = new ArrayList<OperationSettleDto>();
        for(Emprestimo emprestimo : emprestimos){
                try{
                    emprestimo.validaEmprestimoASerPago(emprestimo.getQuantity());
                    OperationSettleDto operationSettleDto = new OperationSettleDto(Emprestimo.TIPO_OPERATION_SEND,emprestimo.getBolsaoCredor().getIdBolsao(),emprestimo.getQuantity());
                    listaOperationSettleDto.add(operationSettleDto);
                    this.pagarEmprestimo(bolsao.getIdBolsao(), emprestimo.getIdBolsaoCredor(), emprestimo.getQuantity());
                    quantity = quantity-emprestimo.getQuantity();
                }catch (EmprestimoValidadorException emprestimoValidadorException){
                    continue;
                }
        }
        return listaOperationSettleDto;
    }

	public Bolsao borrow(long id, long from, float quantity) throws BolsaoNotFoundException, EmprestimoValidadorException {
    	return this.criarEmprestimo(id,from,quantity);
	}

	public List<BolsaoBalance> balance() {
		List<Bolsao> bolsoes = bolsaoRepository.bolsoesContemDividas();
		List<BolsaoBalance> bolsoesBalance = new ArrayList<BolsaoBalance>();
        for(Bolsao bolsao : bolsoes) {
            List<Emprestimo> emprestimosBolsao = this.getEmprestimosBolsao(bolsao);
            List<OperationBalance> listOperationsBalance = new ArrayList<OperationBalance>();

            BolsaoBalance bolsaoBalance = new BolsaoBalance();
            bolsaoBalance.setIdBolsao(bolsao.getIdBolsao().toString());
            bolsaoBalance.setStorage(bolsao.getStorage());
            for(Emprestimo emprestimo : emprestimosBolsao) {
                OperationBalance operationsBalance = this.getOperationBalance(emprestimo,bolsao);
                listOperationsBalance.add(operationsBalance);
            }
            bolsaoBalance.setOperationBalance(listOperationsBalance);
            bolsoesBalance.add(bolsaoBalance);

        }
		// TODO Auto-generated method stub
		return bolsoesBalance;
	}

	public OperationBalance getOperationBalance(Emprestimo emprestimo, Bolsao bolsao){
            OperationBalance operationBalance = new OperationBalance();
            if(emprestimo.isCredor(bolsao.getIdBolsao())) {
                operationBalance.setTipoOperation(OperationBalance.TIPO_RECEIVE);
                operationBalance.setDestination(emprestimo.getBolsaoDevedor());
            }else {
                operationBalance.setTipoOperation(OperationBalance.TIPO_PAY);
                operationBalance.setDestination(this.getRecursiveDestination(emprestimo));
            }
        operationBalance.setQuantity(emprestimo.getQuantity());
            return operationBalance;
    }

    public Bolsao getRecursiveDestination(Emprestimo emprestimo){
        Bolsao bolsaoCredor = emprestimo.getBolsaoCredor();
        List<Emprestimo> emprestimosBolsao = this.getEmprestimosBolsao(bolsaoCredor);
        for(Emprestimo emprestimoCredor : emprestimosBolsao){
            if(emprestimoCredor.getIdBolsaoDevedor()==bolsaoCredor.getIdBolsao() && emprestimoCredor.getQuantity()==emprestimo.getQuantity()){
                return this.getRecursiveDestination(emprestimoCredor);
            }
        }
        return bolsaoCredor;
    }

	public List<Emprestimo> getEmprestimosBolsao(Bolsao bolsao){
        return emprestimoRepository.emprestimosBolsao(bolsao.getIdBolsao());
    }



	public Iterable<Emprestimo> emprestimos() {
		// TODO Auto-generated method stub
		
		return emprestimoRepository.findAll();
	}
}
