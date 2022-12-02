package br.com.cotiinformatica.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.AcessarContaPostDto;
import br.com.cotiinformatica.services.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Acessar Conta de usuário")
@RestController
public class AcessarContaController {
	
	@Autowired //injeção de dependência
	private UsuarioService usuarioService; //atributo

	@ApiOperation("Serviço para autenticação de usuário.")
	@PostMapping("/api/acessar-conta")
	public ResponseEntity<String> post(@RequestBody AcessarContaPostDto dto) {
		
		try {
			
			//gerando a chave de autenticação do usuário
			String accessToken = usuarioService.autenticarUsuario(dto.getEmail(), dto.getSenha());
			
			//Sucesso 200 (Ok)
			return ResponseEntity.status(HttpStatus.OK)
					.body(accessToken);
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



