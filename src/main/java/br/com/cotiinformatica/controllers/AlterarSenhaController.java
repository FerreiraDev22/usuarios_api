package br.com.cotiinformatica.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.AlterarSenhaPutDto;
import br.com.cotiinformatica.services.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Alteração de senha do usuário")
@RestController
public class AlterarSenhaController {

	@Autowired
	private UsuarioService usuarioService;
	
	@ApiOperation("Serviço para atualização da senha de acesso do usuário")
	@PutMapping("/api/alterar-senha")
	public ResponseEntity<String> put(@RequestBody AlterarSenhaPutDto dto, HttpServletRequest request) {

		try {
			
			//capturando o TOKEN enviado
			//[Header] => [Authorization] Bearer <<TOKEN>>
			String accessToken = request.getHeader("Authorization").replace("Bearer", "").trim();
			
			//executando a atualização da senha
			String mensagem = usuarioService.atualizarSenha(dto.getSenhaAtual(), dto.getNovaSenha(), accessToken);
			
			return ResponseEntity.status(HttpStatus.OK)
					.body(mensagem);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage());
		}
		
	}

}


