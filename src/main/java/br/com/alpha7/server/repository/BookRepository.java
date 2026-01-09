package br.com.alpha7.server.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.alpha7.server.entity.Author;
import br.com.alpha7.server.entity.Book;
import br.com.alpha7.server.entity.Publisher;

/**
 * <b>Descrição:</b>
 * Repositório responsável pelas operações de persistência e consulta
 * da entidade {@link Book}.
 * <p>
 * Estende o repositório genérico, herdando operações CRUD básicas
 * e adicionando consultas específicas para livros.
 *
 * <b>Contexto de uso:</b>
 * Utilizado pela camada de serviço para consultas, cadastro, atualização
 * e filtros de livros.
 *
 * @author Fernando Kramer De Souza.
 * @since 1.0.0
 */
@Stateless
public class BookRepository extends AbstractRepository<Book, Long> {

    /**
     * Construtor padrão.
     * <p>
     * Define a entidade gerenciada por este repositório.
     *
     * @since 1.0.0
     */
    public BookRepository() {
        super(Book.class);
    }
    
    /**
     * Busca um livro pelo seu ISBN.
     *
     * @param isbn código ISBN do livro. Não pode ser nulo ou vazio.
     *
     * @return {@link Book} correspondente ao ISBN informado
     *         ou {@code null} caso não exista registro.
     *
     * @since 1.0.0
     */
    public Book findByIsbn(String isbn) {
        try {
            return em.createQuery(
                    "SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class)
                    .setParameter("isbn", isbn)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Realiza busca de livros com base em múltiplos parâmetros opcionais.
     * <p>
     * Os filtros são aplicados dinamicamente conforme parâmetros informados.
     * Caso nenhum parâmetro seja informado, retorna todos os registros.
     * <p>
     * A consulta utiliza Criteria API para construção dinâmica,
     * incluindo joins com autores e editoras quando necessário.
     *
     * @param id identificador do livro.
     * @param isbn código ISBN.
     * @param title título ou parte do título do livro.
     * @param author nome ou parte do nome do autor.
     * @param publisher nome ou parte do nome da editora.
     * @param publicationDate data de publicação do livro.
     *
     * @return lista de livros que atendem aos critérios informados.
     *
     * @since 1.0.0
     */
    public List<Book> findByParameters(
            Long id,
            String isbn,
            String title,
            String author,
            String publisher,
            LocalDate publicationDate
    ) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);

        Root<Book> book = cq.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();

        if (id != null) {
            predicates.add(cb.equal(book.get("id"), id));
        }

        if (isbn != null && !isbn.trim().isEmpty()) {
            predicates.add(cb.equal(book.get("isbn"), isbn));
        }

        if (title != null && !title.trim().isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(book.get("title")),
                    "%" + title.toLowerCase() + "%"
            ));
        }

        if (author != null && !author.trim().isEmpty()) {
            Join<Book, Author> authorJoin = book.join("authors", JoinType.LEFT);
            predicates.add(cb.like(
                    cb.lower(authorJoin.get("name")),
                    "%" + author.toLowerCase() + "%"
            ));
        }

        if (publisher != null && !publisher.trim().isEmpty()) {
            Join<Book, Publisher> publisherJoin = book.join("publishers", JoinType.LEFT);
            predicates.add(cb.like(
                    cb.lower(publisherJoin.get("name")),
                    "%" + publisher.toLowerCase() + "%"
            ));
        }

        if (publicationDate != null) {
            predicates.add(cb.equal(
                    book.get("publicationDate"),
                    publicationDate
            ));
        }

        cq.select(book)
          .distinct(true);

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return em.createQuery(cq).getResultList();
    }
}
