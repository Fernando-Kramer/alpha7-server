package br.com.alpha7.server.infrastructure.exception.mapper;

import java.time.LocalDateTime;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.alpha7.server.infrastructure.exception.NotFoundPersonalizedException;
import br.com.alpha7.server.infrastructure.exception.response.ErrorResponse;

/**
 * <b>Descrição:</b><br>
 * Mapeador responsável por tratar {@link NotFoundPersonalizedException}
 * e convertê-la em uma resposta HTTP adequada no contexto JAX-RS.
 *
 * <p>
 * Este mapper intercepta exceções de negócio que representam ausência
 * de recursos e transforma a ocorrência em uma resposta padronizada
 * de erro HTTP 404 (Not Found), utilizando o modelo {@link ErrorResponse}.
 * </p>
 *
 * <h3>Comportamento</h3>
 * <ul>
 *     <li>Status HTTP: 404 - NOT FOUND</li>
 *     <li>Inclui código de erro funcional {@code NOT_FOUND}</li>
 *     <li>Retorna mensagem proveniente da exceção</li>
 *     <li>Inclui o caminho da requisição e timestamp do ocorrido</li>
 * </ul>
 *
 * <h3>Formato da Resposta</h3>
 * <pre>
 * {
 *   "status": 404,
 *   "error": "NOT_FOUND",
 *   "message": "...",
 *   "path": "/recurso/123",
 *   "timestamp": "2026-01-08T10:20:35"
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
public class NotFoundPersonalizedExceptionMapper implements ExceptionMapper<NotFoundPersonalizedException> {

	/** Informações da requisição atual. */
	@Context
    private UriInfo uriInfo;
	
    /**
     * Converte uma {@link NotFoundPersonalizedException} em uma resposta HTTP 404.
     *
     * @param exception exceção capturada
     * @return resposta HTTP padronizada contendo detalhes do erro
     */
	@Override
	public Response toResponse(NotFoundPersonalizedException exception) {
		
		ErrorResponse response = ErrorResponse.builder()
				.status(Response.Status.NOT_FOUND.getStatusCode())
				.error("NOT_FOUND")
				.message(exception.getMessage())
				.path(uriInfo.getPath())
				.timestamp(LocalDateTime.now())
				.build();
		
		return Response
				.status(Response.Status.NOT_FOUND)
				.entity(response)
				.build();
	}
	
}
