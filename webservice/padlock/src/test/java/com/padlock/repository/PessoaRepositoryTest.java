package com.padlock.repository;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.padlock.entity.Pessoa;
import com.padlock.repository.PessoaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PessoaRepositoryTest {
	
	
	@Autowired
	PessoaRepository pessoaRepository;
	
	@Test
	public void findContatosTest() {
		Pessoa pessoa = new Pessoa("email@email.com", "12345");
		pessoaRepository.save(pessoa);
		assertTrue(pessoaRepository.buscarPessoa(pessoa.getEmail(), pessoa.getSenha()) != null);
	}
}
