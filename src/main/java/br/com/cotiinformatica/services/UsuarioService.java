package br.com.cotiinformatica.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired //injeção de dependência
	private UsuarioRepository usuarioRepository; //atributo
	
	//método para realizar o cadastro do usuário
	public void criarUsuario(Usuario usuario) {
		
		//condição de segurança para verificar se já existe um usuário
		//cadastrado no banco de dados com o email informado
		if(usuarioRepository.findByEmail(usuario.getEmail()) != null)
			throw new IllegalArgumentException("O email informado já está cadastrado no sistema.");
		
		//criptografar a senha do usuário
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
		
		//gerar a data e hora de cadastro
		usuario.setDataHoraCadastro(new Date());
		
		//salvar no banco de dados
		usuarioRepository.save(usuario);
	}
	
	//método para realizar a autenticação do usuário
	public String autenticarUsuario(String email, String senha) {
	
		//criptografar a senha do usuário
		senha = criptografarSenha(senha);
		
		//consultar o usuário no banco de dados através do email e senha
		Usuario usuario = usuarioRepository.findByEmailAndSenha(email, senha);
		
		//verificar se o usuário foi encontrado
		if(usuario != null) {
			//TODO Implementar a geração do TOKEN JWT
			return "<<CHAVE TOKEN JWT>>";
		}
		else {
			throw new IllegalArgumentException("Usuário não encontrado. Acesso negado.");
		}
	}	
	
	//método para fazer a recuperação da senha do usuário
	public String recuperarSenha(String email) {
	
		//consultar o usuário no banco de dados através do email
		Usuario usuario = usuarioRepository.findByEmail(email);
		
		//verificar se o usuário foi encontrado
		if(usuario != null) {
			//TODO Implementar a recuperação da senha do usuário
			return "Recuperação de senha realizada com sucesso";
		}
		else {
			throw new IllegalArgumentException("Usuário não encontrado, verifique o email informado.");
		}
	}	
	
	//método para fazer a criptografia da senha
	private String criptografarSenha(String senha) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");			
			BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
			
			return hash.toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}



