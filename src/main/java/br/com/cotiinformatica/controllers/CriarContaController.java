package br.com.cotiinformatica.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.CriarContaPostDto;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.services.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Criar Conta de usuário")
@RestController
public class CriarContaController {

	@Autowired //injeção de dependência
	private UsuarioService usuarioService; //atributo
	
	@ApiOperation("Serviço para cadastro de conta de usuário.")
	@PostMapping("/api/criar-conta")
	public ResponseEntity<String> post(@RequestBody CriarContaPostDto dto) {

		try {
			
			Usuario usuario = new Usuario();
			
			usuario.setNome(dto.getNome());
			usuario.setEmail(dto.getEmail());
			usuario.setSenha(dto.getSenha());
			
			usuarioService.criarUsuario(usuario);
			
			//Sucesso! 201 (CREATED)
			return ResponseEntity.status(HttpStatus.CREATED)
					.body("Conta de usuário cadastrada com sucesso.");
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



