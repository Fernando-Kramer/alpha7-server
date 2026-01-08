package br.com.alpha7.server.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>Descrição:</b><br>
 * DTO responsável por representar erros ocorridos durante o processo
 * de importação de arquivos, permitindo retornar informações detalhadas
 * sobre falhas identificadas em linhas específicas do arquivo.
 *
 * <p>
 * Esta classe é utilizada para compor relatórios de importação,
 * auxiliando na identificação e correção dos problemas encontrados
 * pelo usuário.
 * </p>
 *
 * <h3>Campos</h3>
 * <ul>
 *     <li><b>lineNumber</b> — Número da linha onde ocorreu o erro</li>
 *     <li><b>lineContent</b> — Conteúdo bruto da linha processada</li>
 *     <li><b>message</b> — Descrição clara do erro encontrado</li>
 * </ul>
 *
 * <h3>Utilização</h3>
 * <ul>
 *     <li>Composição de relatórios de importação</li>
 *     <li>Retorno estruturado para APIs REST</li>
 *     <li>Auxílio em diagnósticos de falhas de processamento</li>
 * </ul>
 *
 * <h3>Recursos</h3>
 * <ul>
 *     <li>Implementação via Lombok</li>
 *     <li>Apoio ao padrão Builder</li>
 * </ul>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportErrorDTO {

	/** Número da linha do arquivo onde o erro foi detectado. */
	private int lineNumber;
	
	/** Conteúdo original da linha que gerou o erro. */
	private String lineContent;
	
	/** Mensagem descritiva do erro ocorrido. */
	private String message;
	
}

