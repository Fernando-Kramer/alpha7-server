package br.com.alpha7.server.infrastructure.exception;

import javax.ejb.ApplicationException;

/**
 * <b>Descrição:</b><br>
 * Exceção de negócio personalizada para cenários onde ocorrer uma requisição inválida (HTTP 400).
 * 
 * <p>
 * É lançada quando os dados fornecidos pelo cliente são inválidos,
 * incompletos ou não atendem aos critérios de validação.
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
 * throw new BadReQuestException("Data inválida.");
 * </pre>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@ApplicationException(rollback = false)
public class BadReQuestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    /**
     * Constrói a exceção com mensagem descritiva.
     *
     * @param message mensagem explicando o motivo do erro do recurso
     */
	public BadReQuestException(String message) {
        super(message);
    }

    /**
     * Constrói a exceção com mensagem e causa raiz.
     *
     * @param message mensagem explicativa
     * @param cause exceção original que originou o erro
     */
    public BadReQuestException(String message, Throwable cause) {
        super(message, cause);
    }
}
