package br.com.cotiinformatica.dtos;
 
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
 
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DadosUsuarioGetDto {
 
	private Integer idUsuario;
	private String nome;
	private String email;
	private String dataHoraCadastro;
	
}
 
 

