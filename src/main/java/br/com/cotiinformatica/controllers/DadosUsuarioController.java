package br.com.cotiinformatica.controllers;
 
import java.text.SimpleDateFormat;
 
import javax.servlet.http.HttpServletRequest;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
 
import br.com.cotiinformatica.dtos.DadosUsuarioGetDto;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.services.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
 
@Api(tags = "Consulta dos dados do usuário")
@RestController
public class DadosUsuarioController {
 
	@Autowired
	private UsuarioService usuarioService;
	
	@ApiOperation("Serviço para consulta dos dados do usuário autenticado")
	@GetMapping("/api/dados-usuario")
	public ResponseEntity<DadosUsuarioGetDto> get(HttpServletRequest request) {
 
		try {
			
			//capturando o TOKEN enviado
			//[Header] => [Authorization] Bearer <<TOKEN>>
			String accessToken = request.getHeader("Authorization").replace("Bearer", "").trim();
			
			Usuario usuario = usuarioService.obterUsuario(accessToken);
			
			DadosUsuarioGetDto dto = new DadosUsuarioGetDto();
			dto.setIdUsuario(usuario.getIdUsuario());
			dto.setNome(usuario.getNome());
			dto.setEmail(usuario.getEmail());
			dto.setDataHoraCadastro(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(usuario.getDataHoraCadastro()));
			
			return ResponseEntity.status(HttpStatus.OK)
					.body(dto);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}		
	}
}
 
 
 
 
 
 
 

