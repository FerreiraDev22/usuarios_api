package br.com.cotiinformatica.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.AcessarContaPostDto;

@RestController
public class AcessarContaController {

	@PostMapping("/api/acessar-conta")
	public void post(@RequestBody AcessarContaPostDto dto) {
		// TODO
	}

}



