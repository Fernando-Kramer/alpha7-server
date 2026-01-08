package br.com.alpha7.server.infrastructure.exception;

import javax.ejb.ApplicationException;

/**
 * <b>Descrição:</b><br>
 * Exceção aplicada quando um ISBN informado é considerado inválido
 * durante processos de validação de livros no sistema.
 * <p>
 * Esta exceção é anotada com {@link jakarta.ejb.ApplicationException}
 * com {@code rollback = false}, indicando que sua ocorrência não deve
 * provocar rollback automático de transações.
 * </p>
 *
 * <h3>Quando é lançada?</h3>
 * <ul>
 *   <li>Formato do ISBN inválido</li>
 *   <li>Quantidade de dígitos incorreta</li>
 *   <li>Dígito verificador inconsistente</li>
 * </ul>
 *
 * @author Fernando Kramer De Souza.
 * @since 1.0.0
 * @see RuntimeException
 */
@ApplicationException(rollback = false)
public class InvalidIsbnException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    /**
     * Cria uma nova exceção para ISBN inválido.
     *
     * @param message mensagem descritiva do erro ocorrido
     */
	public InvalidIsbnException(String message) {
        super(message);
    }
	
}
