package br.com.alpha7.server.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.alpha7.server.entity.Book;
import br.com.alpha7.server.infrastructure.dto.BookDTO;
import br.com.alpha7.server.infrastructure.exception.BadReQuestException;
import br.com.alpha7.server.infrastructure.validation.ExistsRecordValidation;
import br.com.alpha7.server.repository.BookRepository;

/**
 * <b>Descrição:</b><br>
 * Serviço responsável por gerenciar operações relacionadas à entidade {@link Book},
 * incluindo criação, atualização, consulta e remoção de registros.
 *
 * <p>
 * Atua também no relacionamento com autores e editoras, garantindo que
 * as entidades {@link Author} e {@link Publisher} sejam corretamente
 * processadas e associadas ao livro.
 * </p>
 *
 * <h3>Responsabilidades</h3>
 * <ul>
 *     <li>Buscar livro por identificador</li>
 *     <li>Cadastrar e atualizar livros</li>
 *     <li>Excluir livros com segurança relacional</li>
 *     <li>Realizar consultas filtradas</li>
 *     <li>Integrar autores e editoras ao livro</li>
 * </ul>
 *
 * <h3>Validações</h3>
 * <ul>
 *     <li>Valida existência do registro ao consultar ou excluir</li>
 *     <li>Garante obrigatoriedade de ISBN e título</li>
 *     <li>Verifica e processa relacionamentos antes da persistência</li>
 * </ul>
 *
 * <h3>Transação</h3>
 * <ul>
 *     <li>Métodos executados em contexto transacional</li>
 *     <li>Persistência garantida via JPA</li>
 * </ul>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@Stateless
public class BookService {
	
	 /** Repositório responsável por operações de persistência de livros. */
	@Inject
	private BookRepository bookRepository;
	
	/** Serviço responsável pelo processamento de autores relacionados ao livro. */
	@EJB
	private AuthorService authorService;
	
	/** Serviço responsável pelo processamento de editoras relacionadas ao livro. */
	@EJB
	private PublisherService publisherService;

    /**
     * <b>Descrição:</b><br>
     * Busca um livro pelo seu identificador e retorna seus dados
     * representados por {@link BookDTO}.
     *
     * <p>
     * Caso o registro não seja encontrado, uma exceção de "não encontrado"
     * é lançada.
     * </p>
     *
     * @param id identificador do livro a ser consultado
     * @return DTO contendo os dados do livro encontrado
     * @throws NotFoundException caso o livro não exista
     */
	@Transactional
	public BookDTO findById(Long id) {
		Book book = bookRepository.findById(id);
		ExistsRecordValidation.validateExistsRegister(book, id, "Livro");
		
		return BookDTO.fromEntity(book);
	}
	
    /**
     * <b>Descrição:</b><br>
     * Cria um novo livro ou atualiza um existente com base nas
     * informações recebidas em {@link BookDTO}.
     *
     * <p>
     * Caso o ISBN já esteja cadastrado, os dados do livro são atualizados.
     * Caso contrário, um novo registro é criado. Durante o processo,
     * autores e editoras também são tratados e associados ao livro.
     * </p>
     *
     * <h3>Regras de Negócio</h3>
     * <ul>
     *     <li>ISBN e título são obrigatórios</li>
     *     <li>Livro é localizado prioritariamente pelo ISBN</li>
     *     <li>Relacionamentos com autores e editoras são sempre atualizados</li>
     * </ul>
     *
     * @param dto objeto contendo os dados do livro
     * @return DTO do livro persistido
     * @throws BadReQuestException caso ISBN ou título não sejam informados
     */
	@Transactional
	public BookDTO createOrUpdate(BookDTO dto) {
		
		if (dto.getIsbn() == null || dto.getIsbn().trim().isEmpty() ||
				dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
			throw new BadReQuestException("ISBN e título são obrigatórios");
		}
		
		Book book = bookRepository.findByIsbn(dto.getIsbn());
		
		if (book == null) {
	        book = new Book();
	        book.setIsbn(dto.getIsbn());
	    }
		
		book.setTitle(dto.getTitle());
		book.setPublicationDate(dto.getPublicationDate());
		
		authorService.processAuthorsForBook(dto.getAuthors(), book);
		publisherService.processPublishersForBook(dto.getPublishers(), book);
		
	    if (book.getId() == null) {
	        bookRepository.save(book);
	    } else {
	        bookRepository.update(book);
	    }

	    return BookDTO.fromEntity(book);
	}
	
    /**
     * <b>Descrição:</b><br>
     * Remove um livro da base de dados a partir do seu identificador.
     *
     * <p>
     * Antes da exclusão, os relacionamentos com autores e editoras são
     * limpos, garantindo integridade referencial.
     * </p>
     *
     * @param id identificador do livro a ser removido
     * @throws NotFoundException caso o livro não seja encontrado
     */
	@Transactional
	public void deleteById(Long id) {
		Book book = bookRepository.findById(id);
		ExistsRecordValidation.validateExistsRegister(book, id, "Livro");
	    
        book.getAuthors().clear();
        book.getPublishers().clear();
        
        bookRepository.delete(book.getId());
	}

    /**
     * <b>Descrição:</b><br>
     * Realiza consulta de livros utilizando múltiplos parâmetros
     * de filtro, permitindo buscas flexíveis.
     *
     * <p>
     * A pesquisa pode ser feita utilizando qualquer combinação dos
     * parâmetros informados. Caso nenhum registro seja encontrado,
     * uma exceção é lançada.
     * </p>
     *
     * <h3>Parâmetros Aceitos</h3>
     * <ul>
     *     <li>ID do livro</li>
     *     <li>ISBN</li>
     *     <li>Título</li>
     *     <li>Nome do autor</li>
     *     <li>Nome da editora</li>
     *     <li>Data de publicação</li>
     * </ul>
     *
     * @param id identificador do livro (opcional)
     * @param isbn código ISBN (opcional)
     * @param title título do livro (opcional)
     * @param author autor relacionado (opcional)
     * @param publisher editora relacionada (opcional)
     * @param publicationDate data de publicação no formato ISO (opcional)
     * @return lista de livros encontrados convertidos em DTO
     * @throws NotFoundException caso nenhuma ocorrência seja localizada
     */
	@Transactional
	public List<BookDTO> findByParameters(Long id, String isbn, 
			String title, String author, String publisher, String publicationDate) {
		
		LocalDate date = null;
		
		if (publicationDate != null && !publicationDate.trim().isEmpty()) {
		    date = LocalDate.parse(publicationDate);
		}
		
        List<Book> books = bookRepository.findByParameters(
        		id, isbn, title, author, publisher, date);
        
        ExistsRecordValidation.validateExistsListRegister(books, "Livro");
	
		return books.stream()
                .map(BookDTO::fromEntity)
                .collect(Collectors.toList());
	}

}
