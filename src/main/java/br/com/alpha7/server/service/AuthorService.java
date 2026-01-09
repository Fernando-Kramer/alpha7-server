package br.com.alpha7.server.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.alpha7.server.entity.Author;
import br.com.alpha7.server.entity.Book;
import br.com.alpha7.server.infrastructure.dto.AuthorDTO;
import br.com.alpha7.server.repository.AuthorRepository;

/**
 * <b>Descrição:</b><br>
 * Serviço responsável por processar autores associados a um livro,
 * garantindo a persistência dos registros e o relacionamento correto
 * entre entidades {@link Author} e {@link Book}.
 *
 * <p>
 * Este serviço verifica autores informados via {@link AuthorDTO},
 * reutiliza registros existentes quando encontrados e cria novos
 * autores quando necessário, mantendo a integridade da base de dados.
 * </p>
 *
 * <h3>Responsabilidades</h3>
 * <ul>
 *     <li>Localizar autores já cadastrados</li>
 *     <li>Cadastrar novos autores quando inexistentes</li>
 *     <li>Associar autores ao livro informado</li>
 *     <li>Evitar duplicidade de relacionamentos</li>
 * </ul>
 *
 * <h3>Transação</h3>
 * <ul>
 *     <li>Métodos executados em contexto transacional</li>
 *     <li>Persistência garantida pela JPA</li>
 * </ul>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@Stateless
public class AuthorService {

	/** Repositório responsável por operações de persistência de autores. */
    @Inject
    private AuthorRepository authorRepository;

    /**
     * Processa a lista de autores recebida e garante que cada um deles
     * esteja devidamente persistido e associado ao livro informado.
     *
     * <p>
     * Caso o autor já exista na base, ele é reutilizado. Caso contrário,
     * um novo registro é criado. Autores já vinculados ao livro não são
     * adicionados novamente.
     * </p>
     *
     * <h3>Regras de Negócio</h3>
     * <ul>
     *     <li>Se a lista for nula ou vazia, nada é feito</li>
     *     <li>Autor é identificado unicamente pelo nome</li>
     *     <li>Evita duplicidade na relação livro ↔ autor</li>
     * </ul>
     *
     * @param authorDTOs lista de autores informados na requisição
     * @param book entidade {@link Book} que receberá os autores processados
     */
    @Transactional
    public void processAuthorsForBook(List<AuthorDTO> authorDTOs, Book book) {
        if (authorDTOs == null || authorDTOs.isEmpty()) {
            return;
        }

        for (AuthorDTO dto : authorDTOs) {

            Author author = authorRepository.findByName(dto.getName());

            if (author == null) {
                author = new Author();
                author.setName(dto.getName());
                authorRepository.save(author);
            }

            if (!book.getAuthors().contains(author)) {
                book.getAuthors().add(author);
            }
        }
    }
}
