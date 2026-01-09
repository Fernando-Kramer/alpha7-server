package br.com.alpha7.server.infrastructure.exception.mapper;

import java.time.LocalDateTime;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.alpha7.server.infrastructure.exception.ExternalServiceException;
import br.com.alpha7.server.infrastructure.exception.response.ErrorResponse;

/**
 * <b>Descrição:</b><br>
 * Mapeador responsável por tratar {@link ExternalServiceException}.
 *
 * <p>
 * Esta classe converte exceções lançadas quando ocorre uma falha
 * na comunicação com serviços externos em uma resposta HTTP padronizada.
 *
 * <p>Quando acionada, retorna uma resposta JSON contendo:
 * <ul>
 *   <li>status HTTP 502 (Bad Gateway)</li>
 *   <li>código de erro EXTERNAL_SERVICE_ERROR</li>
 *   <li>mensagem descritiva da falha</li>
 *   <li>URI do recurso que originou a requisição</li>
 *   <li>timestamp do momento do erro</li>
 * </ul>
 *
 * <p>Esse mapper ajuda a manter respostas consistentes para erros
 * provenientes de integrações externas, facilitando o consumo e
 * tratamento pela aplicação cliente.
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 * @see ExternalServiceException
 * @see javax.ws.rs.ext.ExceptionMapper
 */
@Provider
public class ExternalServiceExceptionMapper implements ExceptionMapper<ExternalServiceException> {

	@Context
    private UriInfo uriInfo;
	
	/**
	 * Converte a {@link ExternalServiceException} em uma resposta HTTP 502 (Bad Gateway)
	 * contendo um objeto {@link ErrorResponse} com detalhes do erro.
	 *
	 * @param exception exceção lançada durante a comunicação com serviço externo
	 * @return resposta HTTP padronizada representando a falha externa
	 */
	@Override
	public Response toResponse(ExternalServiceException exception) {
		
        ErrorResponse error = ErrorResponse.builder()
        		.status(Response.Status.BAD_GATEWAY.getStatusCode())
        		.error("EXTERNAL_SERVICE_ERROR")
        		.message(exception.getMessage())
        		.path(uriInfo.getPath())
        		.timestamp(LocalDateTime.now())
        		.build();

        return Response
        		.status(Response.Status.BAD_GATEWAY)
                .entity(error)
                .build();
    }

}
