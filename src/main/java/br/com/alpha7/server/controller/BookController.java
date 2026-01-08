package br.com.alpha7.server.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import br.com.alpha7.server.infrastructure.dto.BookDTO;
import br.com.alpha7.server.service.BookService;
import br.com.alpha7.server.service.FileImportService;

/**
 * <b>Descrição:</b><br>
 * Controlador REST responsável pelo gerenciamento de livros,
 * disponibilizando operações de CRUD, pesquisa avançada e
 * importação de dados via arquivo CSV.
 *
 * <p>
 * Este controlador expõe endpoints para criação, atualização,
 * consulta, exclusão e importação de registros de livros,
 * delegando as regras de negócio para {@link BookService}
 * e o processamento de arquivos para {@link FileImportService}.
 * </p>
 *
 * <h3>Responsabilidades</h3>
 * <ul>
 *     <li>Receber requisições HTTP relacionadas a livros</li>
 *     <li>Encaminhar processamento para a camada de serviço</li>
 *     <li>Retornar respostas padronizadas em JSON</li>
 * </ul>
 *
 * <h3>Endpoints</h3>
 * <ul>
 *     <li><b>GET /book/{id}</b> – Consulta livro por ID</li>
 *     <li><b>POST /book</b> – Cria ou atualiza um livro</li>
 *     <li><b>DELETE /book/{id}</b> – Remove um livro</li>
 *     <li><b>GET /book</b> – Busca por filtros</li>
 *     <li><b>POST /book/import</b> – Importa livros via CSV</li>
 * </ul>
 *
 * <h3>Tratamento de Erros</h3>
 * <ul>
 *     <li>{@code NotFoundPersonalizedException} – Registro não localizado</li>
 *     <li>{@code BadReQuestException} – Dados inválidos</li>
 *     <li>{@code FileImportException} – Erros de importação</li>
 * </ul>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@Path("/book")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookController {

	/** Serviço responsável pelas operações de negócio de livros. */
	@EJB
	private BookService bookService;
	
	/** Serviço responsável pela importação de arquivos CSV. */
	@EJB
	private FileImportService fileImportService;
	
	/**
	 * Busca um livro pelo seu identificador.
	 *
	 * @param id identificador único do livro
	 * @return resposta contendo o {@link BookDTO} encontrado
	 */
	@GET
	@Path("/{id}")
	public Response findById(@PathParam("id") Long id) {
		return Response.ok(bookService.findById(id)).build();
	}
	
	/**
	 * Cria ou atualiza um registro de livro.
	 *
	 * @param dto dados do livro enviado na requisição
	 * @return resposta HTTP 201 com o livro persistido
	 */
	@POST
	public Response createOrUpdate(BookDTO dto) {
		return Response.status(Response.Status.CREATED).entity(
				bookService.createOrUpdate(dto)
				).build();
	}
	
	/**
	 * Remove um livro pelo seu identificador.
	 *
	 * @param id identificador do livro a ser removido
	 * @return resposta HTTP 200 em caso de sucesso
	 */
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") Long id) {
		bookService.deleteById(id);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Realiza pesquisa de livros através de múltiplos parâmetros opcionais.
	 *
	 * @param id filtro por identificador
	 * @param isbn filtro por ISBN
	 * @param title filtro por título
	 * @param author filtro por autor
	 * @param publisher filtro por editora
	 * @param publicationDate filtro por data de publicação (yyyy-MM-dd)
	 * @return lista de livros encontrados conforme filtros aplicados
	 */
	@GET
	public Response findByParameters(
			@QueryParam("id") Long id,
			@QueryParam("isbn") String isbn,
			@QueryParam("title") String title,
			@QueryParam("author") String author,
			@QueryParam("publisher") String publisher,
			@QueryParam("publicationDate") String publicationDate
			) {
		return Response.ok(
				bookService.findByParameters(
						id, isbn, title, author, publisher, publicationDate)
				).build();
	}

	/**
	 * Realiza a importação de livros através de um arquivo CSV.
	 *
	 * @param input multipart contendo o arquivo enviado na requisição
	 * @return relatório contendo itens importados e erros ocorridos
	 */
	@POST
	@Path("/import")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response importCsv(MultipartFormDataInput input) {
		
		List<InputPart> files = input.getFormDataMap().get("file");
		
		return Response.ok(
				fileImportService.fileImport(files)
				).build();
	}
}
