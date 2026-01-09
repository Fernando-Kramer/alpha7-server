package br.com.alpha7.server.infrastructure.exception;

import javax.ejb.ApplicationException;

/**
 * <b>Descrição:</b><br>
 * Exceção de negócio lançada durante falhas no processo de importação
 * de arquivos na aplicação.
 *
 * <p>
 * Esta exceção representa problemas ocorridos ao processar arquivos
 * enviados pelo usuário, podendo envolver erros de leitura, formato
 * inválido, conteúdo inconsistente ou falha interna durante o tratamento.
 * </p>
 *
 * <h3>Comportamento</h3>
 * <ul>
 *     <li>Não provoca rollback automático de transações
 *     ({@code rollback = false})</li>
 *     <li>Permite mensagens descritivas e contextualizadas</li>
 *     <li>Suporta encadeamento de exceções para preservação da causa raiz</li>
 * </ul>
 *
 * <h3>Uso Comum</h3>
 * <pre>
 * if (arquivoInvalido) {
 *     throw new FileImportException("Formato do arquivo inválido.");
 * }
 * </pre>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@ApplicationException(rollback = false)
public class FileImportException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    /**
     * Constrói a exceção com mensagem explicativa.
     *
     * @param message descrição do erro ocorrido na importação
     */
	public FileImportException(String message) {
        super(message);
    }

    /**
     * Constrói a exceção com mensagem e causa raiz.
     *
     * @param message descrição do problema ocorrido
     * @param cause exceção original que originou a falha
     */
    public FileImportException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
