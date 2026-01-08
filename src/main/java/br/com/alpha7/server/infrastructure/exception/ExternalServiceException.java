package br.com.alpha7.server.infrastructure.exception;

import javax.ejb.ApplicationException;

/**
 * Exceção de negócio lançada quando ocorre uma falha ao comunicar-se
 * com um serviço externo à aplicação.
 *
 * <p>
 * Esta exceção representa problemas relacionados a integrações externas,
 * tais como indisponibilidade do serviço remoto, erro de resposta,
 * tempo de execução excedido (timeout) ou falha inesperada na comunicação.
 * </p>
 *
 * <h3>Comportamento</h3>
 * <ul>
 *     <li>Não provoca rollback automático de transações
 *     ({@code rollback = false})</li>
 *     <li>Permite mensagens descritivas contextualizadas</li>
 *     <li>Suporta encadeamento de exceções para preservação da causa raiz</li>
 * </ul>
 *
 * <h3>Uso Comum</h3>
 * <pre>
 * if (!servicoExternoDisponivel) {
 *     throw new ExternalServiceException("Falha ao consultar serviço externo.");
 * }
 * </pre>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@ApplicationException(rollback = false)
public class ExternalServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    /**
     * Constrói a exceção com mensagem descritiva da falha.
     *
     * @param message descrição da falha na comunicação com o serviço externo
     */
    public ExternalServiceException(String message) {
        super(message);
    }

    /**
     * Constrói a exceção com mensagem e causa raiz.
     *
     * @param message descrição da falha
     * @param cause exceção original que originou o erro
     */
    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
