package br.com.alpha7.server.infrastructure.exception.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>Descrição:</b><br>
 * Representa o modelo padrão de resposta de erro retornado pela API.
 *
 * <p>
 * Esta classe encapsula informações detalhadas sobre erros ocorridos
 * durante o processamento de requisições REST, permitindo que o cliente
 * receba dados estruturados para tratamento adequado.
 * </p>
 *
 * <h3>Estrutura do Retorno</h3>
 * <ul>
 *   <li>{@code status} — código HTTP do erro</li>
 *   <li>{@code error} — identificação do tipo de erro</li>
 *   <li>{@code message} — descrição detalhada do erro</li>
 *   <li>{@code path} — endpoint em que o erro ocorreu</li>
 *   <li>{@code timestamp} — momento exato em que o erro foi gerado</li>
 * </ul>
 *
 * <h3>Uso Típico</h3>
 * Utilizado por {@code ExceptionMapper} para padronizar respostas de erro
 * enviadas ao cliente em casos de validação, exceções de negócio ou falhas
 * de requisição.
 *
 * @author Fernando Kramer De Souza.
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

	 /** Código HTTP retornado pela requisição. */
	private Integer status;
	
	/** Código identificador do tipo de erro (ex.: ISBN_INVALIDO). */
	private String error;
	
	/** Mensagem detalhada descrevendo o erro ocorrido. */
	private String message;
	
	/** Caminho (endpoint) da requisição que gerou o erro. */
	private String path;
	
	/** Data e hora em que o erro foi registrado. */
	private LocalDateTime timestamp;
	
}
