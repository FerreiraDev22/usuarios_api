package br.com.cotiinformatica.controllers;

import br.com.cotiinformatica.dtos.*;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.services.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

@Api(tags = "Usuários")
@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    @Autowired //injeção de dependência
    private UsuarioService usuarioService; //atributo

    @ApiOperation("Serviço para recuperação de senha de usuário.")
    @PostMapping("recuperar-senha")
    public ResponseEntity<String> recuperarSenha(@RequestBody RecuperarSenhaPostDto dto) {

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

    @ApiOperation("Serviço para consulta dos dados do usuário autenticado")
    @GetMapping("dados-usuario")
    public ResponseEntity<DadosUsuarioGetDto> obterUsuario(HttpServletRequest request) {

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

    @ApiOperation("Serviço para cadastro de conta de usuário.")
    @PostMapping("criar-conta")
    public ResponseEntity<String> criarUsuario(@RequestBody CriarContaPostDto dto) {

        try {
            //poderia ser melhor passar o dto para o service e fazer o fluxo pelo proprio service mesmo
            var usuario = Usuario.builder()
                    .nome(dto.getNome())
                    .email(dto.getEmail())
                    .senha(dto.getSenha())
                    .build();

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

    @ApiOperation("Serviço para atualização da senha de acesso do usuário")
    @PutMapping("alterar-senha")
    public ResponseEntity<String> atualizarSenha(@RequestBody AlterarSenhaPutDto dto, HttpServletRequest request) {

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

    @ApiOperation("Serviço para autenticação de usuário.")
    @PostMapping("acessar-conta")
    public ResponseEntity<String> autenticarUsuario(@RequestBody AcessarContaPostDto dto) {

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
