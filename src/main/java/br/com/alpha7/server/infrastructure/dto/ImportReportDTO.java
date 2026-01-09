package br.com.alpha7.server.infrastructure.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>Descrição:</b><br>
 * DTO responsável por representar o resultado do processo de importação
 * de livros, agrupando tanto os registros importados com sucesso quanto
 * os erros ocorridos durante o processamento.
 *
 * <p>
 * Esta classe permite que a aplicação retorne um relatório estruturado
 * ao cliente, facilitando a identificação de dados válidos e problemas
 * encontrados linha a linha no arquivo importado.
 * </p>
 *
 * <h3>Conteúdo do Relatório</h3>
 * <ul>
 *     <li><b>books</b> — Lista de livros importados com sucesso</li>
 *     <li><b>errors</b> — Lista de erros detalhados ocorridos na importação</li>
 * </ul>
 *
 * <h3>Utilização</h3>
 * <ul>
 *     <li>Retorno em APIs REST após importação</li>
 *     <li>Geração de relatórios funcionais</li>
 *     <li>Apoio a tratamento de falhas e feedback ao usuário</li>
 * </ul>
 *
 * <h3>Recursos</h3>
 * <ul>
 *     <li>Implementação simplificada via Lombok</li>
 *     <li>Manipulação dinâmica de erros via método utilitário</li>
 * </ul>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportReportDTO {

	/** Lista de livros importados com sucesso. */
	private List<BookDTO> books;
	
	/** Lista de erros ocorridos durante o processo de importação. */
	private List<ImportErrorDTO> errors = new ArrayList<>();
	
    /**
     * Adiciona um novo erro ao relatório de importação.
     *
     * @param line número da linha onde o erro foi identificado
     * @param content conteúdo original da linha processada
     * @param message descrição clara do erro ocorrido
     */
    public void addError(int line, String content, String message) {
        errors.add(new ImportErrorDTO(line, content, message));
    }
	
}
