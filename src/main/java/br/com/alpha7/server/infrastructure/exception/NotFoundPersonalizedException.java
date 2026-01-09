package br.com.alpha7.server.infrastructure.exception;

import javax.ejb.ApplicationException;

/**
 * <b>Descrição:</b><br>
 * Exceção de negócio personalizada para cenários onde um recurso
 * solicitado não é encontrado na aplicação.
 *
 * <p>
 * Esta exceção representa situações de ausência de dados em consultas
 * ou operações onde determinado registro deveria existir.
 * É utilizada para fornecer mensagens mais claras e orientadas ao domínio,
 * substituindo exceções genéricas.
 * </p>
 *
 * <h3>Comportamento</h3>
 * <ul>
 *     <li>Não provoca rollback automático de transação
 *     ({@code rollback = false})</li>
 *     <li>Permite mensagem descritiva personalizada</li>
 *     <li>Suporta encadeamento de exceções para preservação da causa raiz</li>
 * </ul>
 *
 * <h3>Uso Comum</h3>
 * <pre>
 * throw new NotFoundPersonalizedException("Autor não encontrado.");
 * </pre>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@ApplicationException(rollback = false)
public class NotFoundPersonalizedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    /**
     * Constrói a exceção com mensagem descritiva.
     *
     * @param message mensagem explicando o motivo da ausência do recurso
     */
	public NotFoundPersonalizedException(String message) {
        super(message);
    }

    /**
     * Constrói a exceção com mensagem e causa raiz.
     *
     * @param message mensagem explicativa
     * @param cause exceção original que originou o erro
     */
    public NotFoundPersonalizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
