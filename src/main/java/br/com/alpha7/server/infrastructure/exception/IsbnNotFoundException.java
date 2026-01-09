package br.com.alpha7.server.infrastructure.exception;

import javax.ejb.ApplicationException;

/**
 * <b>Descrição:</b><br>
 * Exceção de negócio lançada quando um ISBN informado
 * não corresponde a nenhum registro existente na aplicação.
 *
 * <p>
 * Esta exceção é utilizada para representar cenários onde o
 * recurso buscado pelo ISBN não foi localizado, permitindo
 * mensagens mais claras e orientadas ao domínio.
 * </p>
 *
 * <h3>Comportamento</h3>
 * <ul>
 *     <li>Não gera rollback automático de transações
 *     ({@code rollback = false})</li>
 *     <li>Carrega mensagem descritiva informada pelo chamador</li>
 * </ul>
 *
 * <h3>Uso Comum</h3>
 * <pre>
 * throw new IsbnNotFoundException("Livro não encontrado para o ISBN informado.");
 * </pre>
 *
 * @author Seu Nome
 * @since 1.0
 */
@ApplicationException(rollback = false)
public class IsbnNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    /**
     * Constrói a exceção com mensagem descritiva.
     *
     * @param message mensagem explicando o motivo da ausência do recurso
     */
	public IsbnNotFoundException(String message) {
		super(message);
	}

}

