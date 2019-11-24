package com.padlock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.padlock.entity.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> { 
	
	@Query("select p from Pessoa p where p.email = ?1 and p.senha = ?2 ") 
	Pessoa buscarPessoa(String email, String senha);

}
	
