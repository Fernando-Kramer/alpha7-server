package br.com.alpha7.server.infrastructure.validation;

import java.util.List;

import br.com.alpha7.server.infrastructure.exception.NotFoundPersonalizedException;

/**
 * <b>Descrição:</b><br>
 * Utilitário responsável por validações de existência de registros.
 *
 * <p>
 * Esta classe centraliza validações relacionadas à verificação de entidades
 * retornadas de operações de persistência ou consulta no banco de dados,
 * garantindo respostas de erro consistentes na aplicação.
 * </p>
 *
 * <h3>Responsabilidades</h3>
 * <ul>
 *     <li>Validar se um registro único existe</li>
 *     <li>Validar se uma lista contém resultados</li>
 *     <li>Lançar exceções funcionais padronizadas em caso de ausência</li>
 * </ul>
 *
 * <h3>Exceções</h3>
 * <ul>
 *     <li>{@link jakarta.ws.rs.NotFoundException}
 *     lançada quando não há registros compatíveis com a consulta.</li>
 * </ul>
 *
 * <h3>Design</h3>
 * <ul>
 *     <li>Classe final — não pode ser estendida</li>
 *     <li>Métodos utilitários estáticos — não necessita instância</li>
 * </ul>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
public final class ExistsRecordValidation {

    /**
     * Valida se uma entidade existe.
     *
     * <p>
     * Deve ser utilizada em operações de consulta individual (ex: buscar por ID).
     * Caso a entidade seja {@code null}, uma {@link NotFoundPersonalizedException} é lançada
     * contendo mensagem descritiva e contextualizada.
     * </p>
     *
     * @param entity entidade retornada da consulta
     * @param id identificador consultado
     * @param entityName nome amigável ou técnico da entidade (ex.: "Autor", "Livro")
     *
     * @throws NotFoundPersonalizedException caso a entidade não exista
     */
	public static void validateExistsRegister(Object entity, Long id, String entityName) {
	    if (entity == null) {
	        throw new NotFoundPersonalizedException(
	            String.format("%s não encontrado com o ID [%d] especificado.", entityName, id)
	        );
	    }
	}
	
    /**
     * Valida se uma lista contém registros.
     *
     * <p>
     * Deve ser utilizada em consultas que retornam coleções.
     * Caso a lista esteja vazia, uma {@link NotFoundPersonalizedException} é lançada
     * indicando ausência de resultados.
     * </p>
     *
     * @param entity lista retornada da consulta
     * @param entityName nome da entidade pesquisada (opcional, caso queira personalizar mensagens futuras)
     *
     * @throws NotFoundPersonalizedException caso a lista não possua resultados
     */
	public static void validateExistsListRegister(List<?> entity, String entityName) {
        if (entity.isEmpty()) {
	        throw new NotFoundPersonalizedException(
	        		"Não encontramos nenhum resultado com os parâmetros fornecidos."
	        		);
	    }
	}

}
