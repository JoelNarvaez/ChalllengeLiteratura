package com.challengeAlura.ChallengeLiteratura;

import com.challengeAlura.ChallengeLiteratura.principal.Principal;
import com.challengeAlura.ChallengeLiteratura.repositorio.AutorRepositorio;
import com.challengeAlura.ChallengeLiteratura.repositorio.LibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeLiteraturaApplication implements CommandLineRunner {

	@Autowired
	private LibroRepositorio libroRepositorio;

	@Autowired
	private AutorRepositorio autorRepositorio;

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(libroRepositorio, autorRepositorio);
		principal.mostrarMenu();
	}
	public static void main(String[] args) {
		SpringApplication.run(ChallengeLiteraturaApplication.class, args);
	}

}
