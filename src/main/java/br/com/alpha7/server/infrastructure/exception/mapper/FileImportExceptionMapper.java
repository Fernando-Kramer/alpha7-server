package br.com.alpha7.server.infrastructure.exception.mapper;

import java.time.LocalDateTime;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.alpha7.server.infrastructure.exception.FileImportException;
import br.com.alpha7.server.infrastructure.exception.response.ErrorResponse;

/**
 * <b>Descrição:</b><br>
 * Mapeador responsável por tratar {@link FileImportException}
 * e convertê-la em uma resposta HTTP padronizada no contexto JAX-RS.
 *
 * <p>
 * Este mapper intercepta falhas de importação de arquivos e transforma
 * a exceção em uma resposta estruturada de erro, retornando o status
 * {@code 400 - BAD REQUEST}, informando detalhes claros sobre o problema.
 * </p>
 *
 * <h3>Comportamento</h3>
 * <ul>
 *     <li>Status HTTP: 400 - BAD REQUEST</li>
 *     <li>Código funcional: {@code READ_FILE_ERROR}</li>
 *     <li>Mensagem proveniente da exceção</li>
 *     <li>Inclui caminho da requisição e timestamp</li>
 * </ul>
 *
 * <h3>Formato da Resposta</h3>
 * <pre>
 * {
 *   "status": 400,
 *   "error": "READ_FILE_ERROR",
 *   "message": "...",
 *   "path": "/import/csv",
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
public class FileImportExceptionMapper implements ExceptionMapper<FileImportException> {

	/** Informações do contexto da requisição atual. */
	@Context
    private UriInfo uriInfo;
	
    /**
     * Converte uma {@link FileImportException} em resposta HTTP 400
     * contendo detalhes estruturados do erro.
     *
     * @param exception exceção capturada durante o processamento do arquivo
     * @return resposta HTTP padronizada com informações do erro
     */
	@Override
	public Response toResponse(FileImportException exception) {
		
		ErrorResponse response = ErrorResponse.builder()
				.status(Response.Status.BAD_REQUEST.getStatusCode())
				.error("READ_FILE_ERROR")
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
