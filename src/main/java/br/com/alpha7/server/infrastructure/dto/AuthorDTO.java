package br.com.alpha7.server.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>Descrição:</b><br>
 * Representa o DTO (Data Transfer Object) de Autor, utilizado para
 * transporte de dados entre as camadas da aplicação e exposição em
 * APIs sem acoplar diretamente a entidade de domínio.
 *
 * <p>
 * Esta classe encapsula as informações essenciais de um autor,
 * permitindo trafegar dados de forma simples, segura e padronizada.
 * É frequentemente utilizada em operações de criação, consulta,
 * atualização e listagem.
 * </p>
 *
 * <h3>Campos</h3>
 * <ul>
 *     <li><b>id</b> — Identificador único do autor</li>
 *     <li><b>name</b> — Nome completo do autor</li>
 * </ul>
 *
 * <h3>Recursos</h3>
 * <ul>
 *     <li>Gerado via Lombok: {@code @Data}, {@code @Builder},
 *         {@code @NoArgsConstructor} e {@code @AllArgsConstructor}</li>
 *     <li>Suporta criação fluente via padrão Builder</li>
 * </ul>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDTO {

	/** Identificador único do autor. */
	private Long id;
	
	/** Nome completo do autor. */
	private String name;
	
}
