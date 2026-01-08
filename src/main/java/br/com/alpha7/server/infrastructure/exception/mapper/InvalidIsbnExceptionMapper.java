package br.com.alpha7.server.infrastructure.exception.mapper;

import java.time.LocalDateTime;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.alpha7.server.infrastructure.exception.InvalidIsbnException;
import br.com.alpha7.server.infrastructure.exception.response.ErrorResponse;

/**
 * <b>Descrição:</b><br>
 * {@code ExceptionMapper} responsável por tratar {@link InvalidIsbnException}
 * e convertê-la em uma resposta HTTP padronizada para o cliente.
 *
 * <p>
 * Esta classe é registrada como um {@link jakarta.ws.rs.ext.Provider},
 * permitindo que o JAX-RS a utilize automaticamente sempre que uma
 * {@code InvalidIsbnException} for lançada dentro do contexto de uma
 * requisição REST.
 * </p>
 *
 * <h3>Comportamento</h3>
 * <ul>
 *   <li>Retorna o status {@code 400 Bad Request}</li>
 *   <li>Retorna um corpo JSON do tipo {@code ErrorResponse}</li>
 *   <li>Inclui mensagem, código do erro, path e timestamp</li>
 * </ul>
 *
 * <h3>Uso típico</h3>
 * Lançada quando o cliente fornece um ISBN inválido em operações de
 * criação ou atualização de livros.
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 * @see InvalidIsbnException
 * @see jakarta.ws.rs.ext.ExceptionMapper
 */
@Provider
public class InvalidIsbnExceptionMapper implements ExceptionMapper<InvalidIsbnException> {

	/** Informações da requisição atual. */
    @Context
    private UriInfo uriInfo;

    /**
     * Converte a {@link InvalidIsbnException} em uma resposta HTTP 400 contendo
     * informações detalhadas sobre o erro ocorrido.
     *
     * @param exception exceção capturada pelo JAX-RS.
     * @return resposta HTTP contendo os detalhes do erro.
     */
    @Override
    public Response toResponse(InvalidIsbnException exception) {
    	
    	ErrorResponse error = ErrorResponse.builder()
    			.status(Response.Status.BAD_REQUEST.getStatusCode())
    			.error("ISBN_INVALIDO")
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

