package br.com.alpha7.server.repository;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import br.com.alpha7.server.entity.Publisher;

/**
 * <b>Descrição:</b><br>
 * Repositório responsável por operações de persistência da entidade {@link Publisher}.
 * <p>
 * Estende o repositório genérico, herdando operações CRUD básicas
 * e adicionando consultas específicas do domínio de editoras.
 *
 * <b>Contexto de uso:</b>
 * Utilizado na camada de serviço para manipulação e consulta de editoras.
 *
 * @author Fernando Kramer De Souza.
 * @since 1.0.0
 */
@Stateless
public class PublisherRepository extends AbstractRepository<Publisher, Long> {

    /**
     * Construtor padrão.
     * <p>
     * Define a classe da entidade associada a este repositório.
     *
     * @since 1.0.0
     */
	public PublisherRepository() {
		super(Publisher.class);
	}
	
    /**
     * Busca uma editora pelo nome.
     *
     * @param name - nome da editora. Não pode ser nulo.
     *
     * @return {@link Publisher} correspondente ao nome informado
     *         ou {@code null} caso não exista registro.
     *
     * @since 1.0.0
     */
    public Publisher findByName(String name) {
        try {
            return em.createQuery(
                    "SELECT p FROM Publisher p WHERE p.name = :name", Publisher.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
