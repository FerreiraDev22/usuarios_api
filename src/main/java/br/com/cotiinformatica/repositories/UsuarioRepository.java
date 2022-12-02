package br.com.cotiinformatica.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.cotiinformatica.entities.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {

	// Consulta JPQL para obter 1 usuário através do email
	@Query("from Usuario u where u.email = :param1")
	Usuario findByEmail(@Param("param1") String email);

	// Consulta JPQL para obter 1 usuário através do email e da senha
	@Query("from Usuario u where u.email = :param1 and u.senha = :param2")
	Usuario findByEmailAndSenha(@Param("param1") String email, @Param("param2") String senha);
}



