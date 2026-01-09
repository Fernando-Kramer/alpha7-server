package br.com.alpha7.server.repository;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import br.com.alpha7.server.entity.Author;

/**
 * <b>Descrição:</b><br>
 * Repositório responsável por operações de persistência da entidade {@link Author}.
 * <p>
 * Estende o repositório genérico, herdando operações CRUD básicas
 * e adicionando consultas específicas do domínio de autores.
 *
 * <b>Contexto de uso:</b>
 * Utilizado na camada de serviço para manipulação e consulta de autores.
 *
 * @author Fernando Kramer De Souza.
 * @since 1.0.0
 */
@Stateless
public class AuthorRepository extends AbstractRepository<Author, Long> {

    /**
     * Construtor padrão.
     * <p>
     * Define a classe da entidade associada a este repositório.
     *
     * @since 1.0.0
     */
    public AuthorRepository() {
        super(Author.class);
    }

    /**
     * Busca um autor pelo nome.
     *
     * @param name - nome do autor. Não pode ser nulo.
     *
     * @return {@link Author} correspondente ao nome informado
     *         ou {@code null} caso não exista registro.
     *
     * @since 1.0.0
     */
    public Author findByName(String name) {
        try {
            return em.createQuery(
                    "SELECT a FROM Author a WHERE a.name = :name", Author.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}