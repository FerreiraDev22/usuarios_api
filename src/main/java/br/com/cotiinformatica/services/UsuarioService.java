package br.com.cotiinformatica.services;
 
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
 
import javax.mail.internet.MimeMessage;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
 
import com.github.javafaker.Faker;
 
import br.com.cotiinformatica.config.JwtFilter;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.repositories.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
 
@Service
public class UsuarioService {
 
	@Autowired // injeção de dependência
	private UsuarioRepository usuarioRepository; // atributo
 
	@Autowired // injeção de dependência
	private JavaMailSender javaMailSender;
 
	// método para realizar o cadastro do usuário
	public void criarUsuario(Usuario usuario) {
 
		// condição de segurança para verificar se já existe um usuário
		// cadastrado no banco de dados com o email informado
		if (usuarioRepository.findByEmail(usuario.getEmail()) != null)
			throw new IllegalArgumentException("O email informado já está cadastrado no sistema.");
 
		// criptografar a senha do usuário
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
 
		// gerar a data e hora de cadastro
		usuario.setDataHoraCadastro(new Date());
 
		// salvar no banco de dados
		usuarioRepository.save(usuario);
	}
 
	// método para realizar a autenticação do usuário
	public String autenticarUsuario(String email, String senha) {
 
		// criptografar a senha do usuário
		senha = criptografarSenha(senha);
 
		// consultar o usuário no banco de dados através do email e senha
		Usuario usuario = usuarioRepository.findByEmailAndSenha(email, senha);
 
		// verificar se o usuário foi encontrado
		if (usuario != null) {
			return gerarTokenJwt(usuario);
		} else {
			throw new IllegalArgumentException("Usuário não encontrado. Acesso negado.");
		}
	}
 
	// método para fazer a recuperação da senha do usuário
	public String recuperarSenha(String email) {
 
		// consultar o usuário no banco de dados através do email
		Usuario usuario = usuarioRepository.findByEmail(email);
 
		// verificar se o usuário foi encontrado
		if (usuario != null) {
 
			//gerando uma nova senha para o usuário
			Faker faker = new Faker();
			String novaSenha = faker.internet().password();
			
			//enviando o email com a senha nova para o usuário
			enviarEmailDeRecuperacaoDeSenha(novaSenha, usuario);
			
			//atualizando a senha no banco de dados
			usuario.setSenha(criptografarSenha(novaSenha));
			usuarioRepository.save(usuario);
			
			return "Recuperação de senha realizada com sucesso";
		} else {
			throw new IllegalArgumentException("Usuário não encontrado, verifique o email informado.");
		}
	}
	
	//método para realizar a atualização da senha do usuário no banco de dados
	public String atualizarSenha(String senhaAtual, String novaSenha, String token) throws Exception {
		
		//extrair o email do usuário contido no token
		String email = obterIdentificacaoDoUsuario(token);
		
		//criptografar as senhas obtidas
		senhaAtual = criptografarSenha(senhaAtual);
		novaSenha = criptografarSenha(novaSenha);
		
		//procurar o usuário no banco de dados
		Usuario usuario = usuarioRepository.findByEmailAndSenha(email, senhaAtual);
		
		//verificar se o usuário foi encontrado
		if(usuario != null) {
			
			//atualizar a senha
			usuario.setSenha(novaSenha);
			usuarioRepository.save(usuario);
			
			return "Senha atualizada com sucesso.";
		}
		else {
			return "Usuário não encontrado, por favor verifique a senha atual informada.";
		}			
	}
	
	//método para consultar os dados do usuário através do email
	public Usuario obterUsuario(String token) {
		
		//Obter o email do usuário gravado no token
		String email = obterIdentificacaoDoUsuario(token);
 
		//consultar e retornar os dados do usuário do banco de dados
		//baseado no email
		return usuarioRepository.findByEmail(email);
	}
 
	// método para fazer a criptografia da senha
	private String criptografarSenha(String senha) {
 
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
 
			return hash.toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
 
	private String gerarTokenJwt(Usuario usuario) {
 
		try {
 
			String secretKey = JwtFilter.SECRET; // chave antifalsificação do TOKEN
			List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
 
			String token = Jwts.builder().setId("usuarios_api").setSubject(usuario.getEmail())
					.claim("authorities",
							grantedAuthorities.stream().map(GrantedAuthority::getAuthority)
									.collect(Collectors.toList()))
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + 6000000))
					.signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();
 
			return token;
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
 
	private void enviarEmailDeRecuperacaoDeSenha(String novaSenha, Usuario usuario) {
 
		String assunto = "Recuperação de senha de acesso - API Usuários";
		String texto = "<div style='border: 2px solid #ccc; padding: 40px; margin: 40px;'>"
				 + "<center>"
				 + "<img src='https://www.cotiinformatica.com.br/imagens/logo-coti-informatica.png'/>"
				 + "<h2>Olá, <strong>" + usuario.getNome() + "</strong></h2>"
				 + "<p>Uma nova senha foi gerada com sucesso!</p>"					
				 + "<p>Acesse o sistema com a senha: <strong>" + novaSenha + "</strong></p>"
				 + "<p>Att,</p>"
				 + "<p>Equipe COTI Informática</p>"
				 + "</center>"
				 + "</div>";
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		try {
			helper.setFrom("cotiaulajava@outlook.com"); //remetente
			helper.setTo(usuario.getEmail()); //destinatário
			helper.setSubject(assunto); //assunto da mensagem
			helper.setText(texto, true); //corpo da mensagem
		}
		catch(Exception e) {
			e.printStackTrace();
		}
 
		javaMailSender.send(message); //enviando a mensagem
	}
	
	public String obterIdentificacaoDoUsuario(String token) {
		return getContentFromToken(token, Claims::getSubject);
	}
	
	private <T> T getContentFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = Jwts.parser().setSigningKey(JwtFilter.SECRET.getBytes())
				.parseClaimsJws(token).getBody();
		return claimsResolver.apply(claims);
	}
}
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 

