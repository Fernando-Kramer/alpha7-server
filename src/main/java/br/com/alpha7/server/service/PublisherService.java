package br.com.alpha7.server.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.alpha7.server.entity.Book;
import br.com.alpha7.server.entity.Publisher;
import br.com.alpha7.server.infrastructure.dto.PublisherDTO;
import br.com.alpha7.server.repository.PublisherRepository;

/**
 * <b>Descrição:</b><br>
 * Serviço responsável por processar editoras associados a um livro,
 * garantindo a persistência dos registros e o relacionamento correto
 * entre entidades {@link Publisher} e {@link Book}.
 *
 * <p>
 * Este serviço verifica editoras informados via {@link PublisherDTO},
 * reutiliza registros existentes quando encontrados e cria novas
 * editoras quando necessário, mantendo a integridade da base de dados.
 * </p>
 *
 * <h3>Responsabilidades</h3>
 * <ul>
 *     <li>Localizar editoras já cadastradas</li>
 *     <li>Cadastrar novas editoras quando inexistentes</li>
 *     <li>Associar editoras ao livro informado</li>
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
public class PublisherService {

	/** Repositório responsável por operações de persistência de autores. */
    @Inject
    private PublisherRepository publisherRepository;
    
    /**
     * Processa a lista de editoras recebida e garante que cada uma delas
     * esteja devidamente persistida e associado ao livro informado.
     *
     * <p>
     * Caso a editora já exista na base, ela é reutilizada. Caso contrário,
     * um novo registro é criado. Editoras já vinculados ao livro não são
     * adicionados novamente.
     * </p>
     *
     * <h3>Regras de Negócio</h3>
     * <ul>
     *     <li>Se a lista for nula ou vazia, nada é feito</li>
     *     <li>Editora é identificada unicamente pelo nome</li>
     *     <li>Evita duplicidade na relação livro ↔ editora</li>
     * </ul>
     *
     * @param publisherDTOs lista de editoras informadas na requisição
     * @param book entidade {@link Book} que receberá as editoras processadas
     */
    @Transactional
    public void processPublishersForBook(List<PublisherDTO> publisherDTOs, Book book) {
        if (publisherDTOs == null || publisherDTOs.isEmpty()) {
            return;
        }

        for (PublisherDTO dto : publisherDTOs) {
            
            Publisher publisher = publisherRepository.findByName(dto.getName());

            if (publisher == null) {
                publisher = new Publisher();
                publisher.setName(dto.getName());
                publisherRepository.save(publisher);
            }

            if (!book.getPublishers().contains(publisher)) {
                book.getPublishers().add(publisher);
            }
        }
    }
}