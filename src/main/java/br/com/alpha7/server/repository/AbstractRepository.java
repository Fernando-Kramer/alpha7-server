package br.com.alpha7.server.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * <b>Descrição:</b>
 * Repositório base genérico para operações de persistência JPA.
 * <p>
 * Fornece operações comuns de CRUD para qualquer entidade,
 * permitindo reutilização e padronização do acesso a dados.
 *
 * <b>Regras / Contrato:</b>
 * <ul>
 *   <li>As classes concretas devem estender este repositório.</li>
 *   <li>O tipo da entidade deve ser informado no construtor.</li>
 * </ul>
 *
 * <b>Contexto de uso:</b>
 * Utilizado como camada base para os repositórios específicos do sistema.
 *
 * @param <T> Tipo da entidade.
 * @param <ID> Tipo do identificador da entidade.
 *
 * @author Fernando Kramer De Souza.
 * @since 1.0.0
 */
public abstract class AbstractRepository<T, ID> {

    /**
     * Gerenciador de entidades responsável pela integração com o JPA.
     */
    @PersistenceContext
    protected EntityManager em;

    /**
     * Classe da entidade manipulada pelo repositório.
     */
    private final Class<T> entityClass;

    /**
     * Construtor que define o tipo da entidade a ser gerenciada.
     *
     * @param entityClass classe da entidade.
     *
     * @since 1.0.0
     */
    public AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Persiste uma nova entidade no banco de dados.
     * <p>
     * Após a persistência é realizado um flush para garantir
     * sincronização imediata com o banco.
     *
     * @param entity entidade a ser salva.
     * @return entidade persistida.
     *
     * @throws IllegalArgumentException caso a entidade seja inválida.
     *
     * @since 1.0.0
     */
    public T save(T entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }

    /**
     * Atualiza uma entidade existente no banco.
     *
     * @param entity entidade com dados atualizados.
     * @return entidade gerenciada após o merge.
     *
     * @since 1.0.0
     */
    public T update(T entity) {
        return em.merge(entity);
    }

    /**
     * Busca uma entidade pelo identificador.
     *
     * @param id identificador da entidade.
     * @return entidade encontrada ou {@code null} caso não exista.
     *
     * @since 1.0.0
     */
    public T findById(ID id) {
        return em.find(entityClass, id);
    }

    /**
     * Remove uma entidade pelo identificador.
     * <p>
     * Caso a entidade não exista, nenhuma ação é realizada.
     *
     * @param id identificador da entidade.
     *
     * @since 1.0.0
     */
    public void delete(ID id) {
        T entity = findById(id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    /**
     * Retorna todas as entidades do tipo gerenciado por este repositório.
     *
     * @return lista de entidades existentes.
     *
     * @since 1.0.0
     */
    public List<T> findAll() {
        return em.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName() + " e",
                entityClass
        ).getResultList();
    }
}

