package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Bolsao;

import java.util.List;

@Repository
public interface BolsaoRepository extends JpaRepository<Bolsao, Long> {

    @Query("select distinct b from Bolsao b join Emprestimo e on b = e.bolsaoDevedor where b.storage > e.quantity")
    List<Bolsao> bolsoesContemDividasEStorage();
    
    @Query("select distinct b from Bolsao b join Emprestimo e on (b = e.bolsaoDevedor or b = e.bolsaoCredor)")
    List<Bolsao> bolsoesContemDividas();

}

