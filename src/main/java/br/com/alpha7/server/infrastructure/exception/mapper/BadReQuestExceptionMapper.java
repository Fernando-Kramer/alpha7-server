package br.com.alpha7.server.infrastructure.exception.mapper;

import java.time.LocalDateTime;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.alpha7.server.infrastructure.exception.BadReQuestException;
import br.com.alpha7.server.infrastructure.exception.response.ErrorResponse;

/**
 * <b>Descrição:</b><br>
 * Mapeador responsável por tratar {@link BadReQuestException}
 * e convertê-la em uma resposta HTTP padronizada no contexto JAX-RS.
 *
 * <p>
 * Este mapper intercepta erros de requisição inválida e transforma
 * a exceção em uma resposta estruturada de erro, retornando o status
 * {@code 400 - BAD REQUEST}, informando detalhes claros sobre a falha
 * ocorrida durante a solicitação.
 * </p>
 *
 * <h3>Comportamento</h3>
 * <ul>
 *     <li>Status HTTP: 400 - BAD REQUEST</li>
 *     <li>Código funcional: {@code BAD_REQUEST}</li>
 *     <li>Mensagem proveniente da exceção</li>
 *     <li>Inclui caminho da requisição e timestamp</li>
 * </ul>
 *
 * <h3>Formato da Resposta</h3>
 * <pre>
 * {
 *   "status": 400,
 *   "error": "BAD_REQUEST",
 *   "message": "...",
 *   "path": "/recurso/acao",
 *   "timestamp": "2026-01-08T10:45:12"
 * }
 * </pre>
 *
 * <h3>Integração</h3>
 * <ul>
 *     <li>Registrado automaticamente via {@link Provider}</li>
 *     <li>Aplica-se globalmente às requisições REST</li>
 * </ul>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@Provider
public class BadReQuestExceptionMapper implements ExceptionMapper<BadReQuestException> {

	/** Informações do contexto da requisição atual. */
	@Context
    private UriInfo uriInfo;
	
    /**
     * Converte uma {@link BadReQuestException} em resposta HTTP 400
     * contendo detalhes estruturados do erro.
     *
     * @param exception exceção capturada durante o processamento da requisição
     * @return resposta HTTP padronizada com informações do erro
     */
	@Override
	public Response toResponse(BadReQuestException exception) {
		
		ErrorResponse response = ErrorResponse.builder()
				.status(Response.Status.BAD_REQUEST.getStatusCode())
				.error("BAD_REQUEST")
				.message(exception.getMessage())
				.path(uriInfo.getPath())
				.timestamp(LocalDateTime.now())
				.build();

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity(response)
				.build();
	}

}
