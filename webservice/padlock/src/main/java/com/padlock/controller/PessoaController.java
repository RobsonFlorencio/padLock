package com.padlock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.padlock.entity.Pessoa;
import com.padlock.repository.PessoaRepository;

@RestController
@CrossOrigin(origins = "*")
public class PessoaController {

	@Autowired
	private PessoaRepository _pessoaRepository;

	@GetMapping(value = "/acesso/pessoa")
	public boolean getPessoa(@RequestBody Pessoa pessoa) {
		return _pessoaRepository.buscarPessoa(pessoa.getEmail(), pessoa.getSenha()) != null;
	}
	
	@PostMapping(value = "/cadastro")
	public Pessoa addPessoa(@RequestBody Pessoa pessoa) {
		return _pessoaRepository.save(pessoa);
	}

}