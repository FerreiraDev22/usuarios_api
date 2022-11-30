package br.com.cotiinformatica.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.RecuperarSenhaPostDto;

@RestController
public class RecuperarSenhaController {

	@PostMapping("/api/recuperar-senha")
	public void post(@RequestBody RecuperarSenhaPostDto dto) {
		//TODO
	}
}
