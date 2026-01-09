package br.com.alpha7.server.infrastructure.exception.mapper;

import java.time.LocalDateTime;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.alpha7.server.infrastructure.exception.IsbnNotFoundException;
import br.com.alpha7.server.infrastructure.exception.response.ErrorResponse;

/**
 * Mapeador responsável por tratar {@link IsbnNotFoundException}
 * e convertê-la em uma resposta HTTP adequada no contexto JAX-RS.
 *
 * <p>
 * Este mapper intercepta exceções de negócio relacionadas à ausência
 * de registros vinculados a um ISBN informado e transforma a ocorrência
 * em uma resposta padronizada de erro HTTP, retornando status
 * {@code 400 - BAD REQUEST}.
 * </p>
 *
 * <h3>Comportamento</h3>
 * <ul>
 *     <li>Status HTTP: 400 - BAD REQUEST</li>
 *     <li>Inclui código funcional {@code ISBN_NAO_ENCONTRADO}</li>
 *     <li>Retorna mensagem detalhada proveniente da exceção</li>
 *     <li>Inclui o caminho da requisição e timestamp do ocorrido</li>
 * </ul>
 *
 * <h3>Formato da Resposta</h3>
 * <pre>
 * {
 *   "status": 400,
 *   "error": "ISBN_NAO_ENCONTRADO",
 *   "message": "...",
 *   "path": "/livros/isbn/123",
 *   "timestamp": "2026-01-08T10:35:22"
 * }
 * </pre>
 *
 * <h3>Integração</h3>
 * <ul>
 *     <li>Registrado automaticamente pelo JAX-RS via {@link Provider}</li>
 *     <li>Aplica-se globalmente a todos os endpoints REST</li>
 * </ul>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@Provider
public class IsbnNotFoundExceptionMapper implements ExceptionMapper<IsbnNotFoundException> {

	/** Informações sobre a requisição atual. */
	@Context
    private UriInfo uriInfo;
	
    /**
     * Converte uma {@link IsbnNotFoundException} em uma resposta HTTP.
     *
     * @param exception exceção capturada durante o processamento da requisição
     * @return resposta HTTP contendo detalhes do erro padronizado
     */
	@Override
	public Response toResponse(IsbnNotFoundException exception) {
		
		ErrorResponse error = ErrorResponse.builder()
				.status(Response.Status.BAD_REQUEST.getStatusCode())
				.error("ISBN_NAO_ENCONTRADO")
				.message(exception.getMessage())
				.path(uriInfo.getPath())
				.timestamp(LocalDateTime.now())
				.build();

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(error)
                .build();
	}

}
