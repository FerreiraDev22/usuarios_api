package br.com.cotiinformatica.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.cotiinformatica.entities.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {

	// Consulta JPQL para obter 1 usuário através do email
	Usuario findByEmail(String email);

	// Consulta JPQL para obter 1 usuário através do email e da senha
	Usuario findByEmailAndSenha(String email, String senha);
}



