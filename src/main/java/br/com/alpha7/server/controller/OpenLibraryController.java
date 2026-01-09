package br.com.alpha7.server.controller;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.alpha7.server.integration.OpenLibraryIntegration;

/**
 * <b>Descrição:</b><br>
 * Controlador REST responsável por integrar com o serviço externo
 * OpenLibrary, permitindo consulta de informações de livros por ISBN.
 *
 * <p>
 * Este endpoint recebe o ISBN via parâmetro de requisição,
 * encaminha a busca para a integração {@link OpenLibraryIntegration}
 * e retorna os dados do livro encontrados.
 * </p>
 *
 * <h3>Responsabilidades</h3>
 * <ul>
 *     <li>Receber requisições HTTP de consulta</li>
 *     <li>Delegar processamento à camada de integração</li>
 *     <li>Retornar resposta padronizada em JSON</li>
 * </ul>
 *
 * <h3>Formato da Requisição</h3>
 * <pre>
 * GET /open-library?isbn=9780134685991
 * </pre>
 *
 * <h3>Formato da Resposta</h3>
 * <p>Em caso de sucesso:</p>
 * <pre>
 * {
 *   "isbn": "9780134685991",
 *   "title": "...",
 *   "publishers": [...],
 *   "publicationDate": "yyyy-MM-dd"
 * }
 * </pre>
 *
 * <h3>Tratamento de Erros</h3>
 * <ul>
 *     <li>{@code IsbnNotFoundException} → ISBN não encontrado</li>
 *     <li>{@code ExternalServiceException} → Falha de comunicação</li>
 * </ul>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@Path("/open-library")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OpenLibraryController {
	
	/** Serviço responsável por consultar a OpenLibrary. */
	@EJB
	private OpenLibraryIntegration openLibraryIntegration;
	
	/**
	 * Realiza a consulta de livro na OpenLibrary a partir do ISBN informado.
	 *
	 * @param isbn código ISBN do livro a ser consultado
	 * @return resposta HTTP contendo o {@link BookDTO} do livro encontrado
	 */
	@GET
	public Response findByIsbn(@QueryParam("isbn") String isbn) {
		return Response.ok(this.openLibraryIntegration.findByIsbn(isbn)).build();
	}

}
