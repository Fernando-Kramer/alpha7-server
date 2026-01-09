package br.com.alpha7.server.infrastructure.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.alpha7.server.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>Descrição:</b><br>
 * DTO (Data Transfer Object) responsável por representar informações de
 * {@link Book} na comunicação entre camadas da aplicação e exposição
 * em serviços REST, evitando o acoplamento direto com a entidade de domínio.
 *
 * <p>
 * Esta classe transporta os dados essenciais de um livro, incluindo ISBN,
 * título, autores, editoras e data de publicação. É utilizada em operações
 * de consulta, cadastro, atualização e listagem.
 * </p>
 *
 * <h3>Campos</h3>
 * <ul>
 *     <li><b>id</b> — Identificador único do livro</li>
 *     <li><b>isbn</b> — Código ISBN do livro</li>
 *     <li><b>title</b> — Título do livro</li>
 *     <li><b>authors</b> — Lista de autores associados</li>
 *     <li><b>publishers</b> — Lista de editoras associadas</li>
 *     <li><b>publicationDate</b> — Data de publicação</li>
 * </ul>
 *
 * <h3>Conversão</h3>
 * <p>
 * Possui o método {@link #fromEntity(Book)} que converte uma entidade
 * {@link Book} para um {@code BookDTO}, garantindo isolamento entre
 * a camada de domínio e a camada de transporte de dados.
 * </p>
 *
 * <h3>Recursos</h3>
 * <ul>
 *     <li>Gerada via Lombok: {@code @Data}, {@code @Builder},
 *         {@code @NoArgsConstructor}, {@code @AllArgsConstructor}</li>
 *     <li>Suporte ao padrão Builder para criação fluente</li>
 * </ul>
 *
 * @author Fernando Kramer
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

	/** Identificador único do livro. */
	private Long id;
	
	/** Código ISBN do livro. */
	private String isbn;
	
	/** Título do livro. */
	private String title;
	
	/** Lista de autores associados ao livro. */
	private List<AuthorDTO> authors;
	
	 /** Lista de editoras associadas ao livro. */
	private List<PublisherDTO> publishers;
	
	/** Data de publicação do livro. */
	private LocalDate publicationDate;
	
    /**
     * Converte uma entidade {@link Book} em uma instância de {@link BookDTO}.
     *
     * @param book entidade de origem
     * @return representação DTO do livro
     */
	public static BookDTO fromEntity(Book book) {
		return BookDTO.builder()
				.id(book.getId())
				.isbn(book.getIsbn())
				.title(book.getTitle())
				.authors(
						book.getAuthors()
						.stream()
						.map(a -> AuthorDTO.builder()
								.id(a.getId())
								.name(a.getName())
								.build()
								)
						.collect(Collectors.toList())
						)
				.publishers(
						book.getPublishers()
						.stream()
						.map(p -> PublisherDTO.builder()
								.id(p.getId())
								.name(p.getName())
								.build()
								)
						.collect(Collectors.toList())
						)
				.publicationDate(book.getPublicationDate())
				.build();
	}
	
}
