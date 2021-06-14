package com.example.demo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Bolsao;
import com.example.demo.entity.Emprestimo;

import java.util.List;

@Repository
public interface EmprestimoRepository extends CrudRepository<Emprestimo, Long> {

    public List<Emprestimo> findAllByBolsaoDevedorOrderByIdEmprestimo(Bolsao bolsaoDevedor);
    
    public List<Emprestimo> findAllByBolsaoCredorOrderByIdEmprestimo(Bolsao bolsaoCredor);

    public Emprestimo findEmprestimoByBolsaoCredorAndAndBolsaoDevedor(Bolsao bolsaoCredor,Bolsao bolsaoDevedor);
    
    @Query("select e from Emprestimo e join Bolsao b on b.idBolsao = ?1 and ( b= e.bolsaoDevedor or b= e.bolsaoCredor)")
    List<Emprestimo> emprestimosBolsao(long bolsao);



}
