package br.com.cotiinformatica.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.CriarContaPostDto;

@RestController
public class CriarContaController {

	@PostMapping("/api/criar-conta")
	public void post(@RequestBody CriarContaPostDto dto) {
		//TODO
	}
}
