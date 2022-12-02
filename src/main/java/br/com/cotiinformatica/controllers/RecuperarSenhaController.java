package br.com.cotiinformatica.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.RecuperarSenhaPostDto;
import br.com.cotiinformatica.services.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Recuperação de senha de usuário")
@RestController
public class RecuperarSenhaController {

	@Autowired //injeção de dependência
	private UsuarioService usuarioService; //atributo
	
	@ApiOperation("Serviço para recuperação de senha de usuário.")
	@PostMapping("/api/recuperar-senha")
	public ResponseEntity<String> post(@RequestBody RecuperarSenhaPostDto dto) {

		try {
			
			String result = usuarioService.recuperarSenha(dto.getEmail());
			
			//Sucesso 200 (OK)
			return ResponseEntity.status(HttpStatus.OK)
					.body(result);
		}
		catch(IllegalArgumentException e) {
			//Erro 400 (Bad Request)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(e.getMessage());
		}
		catch(Exception e) {
			//Erro 500 (Internal Server Error)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage());
		}
	}
}



