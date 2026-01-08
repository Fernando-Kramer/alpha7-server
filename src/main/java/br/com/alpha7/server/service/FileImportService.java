package br.com.alpha7.server.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import br.com.alpha7.server.infrastructure.dto.AuthorDTO;
import br.com.alpha7.server.infrastructure.dto.BookDTO;
import br.com.alpha7.server.infrastructure.dto.ImportReportDTO;
import br.com.alpha7.server.infrastructure.dto.PublisherDTO;
import br.com.alpha7.server.infrastructure.exception.FileImportException;

/**
 * <b>Descrição:</b><br>
 * Serviço responsável por realizar a importação de arquivos CSV contendo
 * informações de livros, efetuando validações, leitura do conteúdo e
 * processamento dos registros para persistência.
 *
 * <p>
 * Este serviço garante que o arquivo seja válido, não esteja vazio e
 * possua layout adequado. Cada linha válida é convertida em {@link BookDTO}
 * e enviada para processamento no {@link BookService}. Linhas inválidas
 * são registradas em relatório de erros.
 * </p>
 *
 * <h3>Responsabilidades</h3>
 * <ul>
 *     <li>Validar existência e formato do arquivo</li>
 *     <li>Ler conteúdo do CSV linha a linha</li>
 *     <li>Converter dados do arquivo em objetos de domínio</li>
 *     <li>Persistir registros válidos</li>
 *     <li>Gerar relatório de sucesso e falhas</li>
 * </ul>
 *
 * <h3>Formato Esperado do Arquivo</h3>
 * <pre>
 * isbn;titulo;autor1,autor2;editora1,editora2;yyyy-MM-dd
 * </pre>
 *
 * <h3>Tratamento de Erros</h3>
 * <ul>
 *     <li>Arquivo inexistente ou inválido gera {@link FileImportException}</li>
 *     <li>Erros por linha são registrados em {@link ImportReportDTO}</li>
 *     <li>Leitura de arquivo inválida gera exceção específica</li>
 * </ul>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@Stateless
public class FileImportService {
	
	 /** Serviço responsável por persistir e gerenciar os livros importados. */
	@EJB
	private BookService bookService;

    /**
     * <b>Descrição:</b><br>
     * Realiza o processo completo de importação do arquivo CSV recebido.
     *
     * <p>
     * O método valida a existência do arquivo, garante que o formato seja CSV,
     * realiza a leitura do conteúdo e encaminha cada registro para processamento.
     * Ao final, retorna um relatório contendo os livros importados e
     * eventuais erros encontrados.
     * </p>
     *
     * @param files lista contendo o arquivo enviado via requisição multipart
     * @return relatório contendo registros importados e erros ocorridos
     * @throws FileImportException caso o arquivo seja inválido ou leitura falhe
     */
	public ImportReportDTO fileImport(List<InputPart> files) {

	    validateFileExists(files);
	    
	    InputPart filePart = files.get(0);
	    
	    validateCsvFile(filePart);

	    try (InputStream is = filePart.getBody(InputStream.class, null)) {

	        if (is == null || is.available() == 0) {
	            throw new FileImportException("O arquivo informado está vazio");
	        }

	        return processCsv(is);

	    } catch (IOException e) {
	        throw new FileImportException("Erro ao ler o arquivo: " + e.getMessage(), e);
	        
	    } catch (Exception e) {
	        throw new RuntimeException("Erro ao importar arquivo", e);
	        
	    }
	}

    /**
     * Valida a existência do arquivo enviado.
     *
     * <p>
     * Caso não exista arquivo, ou o conteúdo esteja nulo,
     * uma exceção de importação é lançada.
     * </p>
     *
     * @param files arquivos enviados na requisição
     * @throws FileImportException caso o arquivo seja inválido
     */
	private static void validateFileExists(List<InputPart> files) {
	    if (files == null || files.isEmpty()) {
	        throw new FileImportException("O arquivo informado é nulo ou está vazio");
	    }
	    if (files.get(0) == null) {
	        throw new FileImportException("O arquivo informado é nulo");
	    }
	}
	
    /**
     * Verifica se o arquivo enviado possui extensão CSV.
     *
     * @param filePart parte do arquivo recebido
     * @throws FileImportException caso o arquivo não seja CSV
     */
	private static void validateCsvFile(InputPart filePart) {
	    String contentDisposition = filePart.getHeaders().getFirst("Content-Disposition");
	    if (contentDisposition == null || !contentDisposition.toLowerCase().contains(".csv")) {
	        throw new FileImportException("O arquivo informado não é um CSV");
	    }
	}
	
    /**
     * <b>Descrição:</b><br>
     * Realiza a leitura linha a linha do arquivo CSV, convertendo registros
     * válidos em {@link BookDTO} e registrando erros quando necessário.
     *
     * @param is stream do arquivo recebido
     * @return relatório contendo livros importados e erros
     * @throws IOException caso ocorra falha de leitura
     */
	private ImportReportDTO processCsv(InputStream is) throws IOException {
		
		ImportReportDTO report = new ImportReportDTO();
		List<BookDTO> books = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			
			String line;
			int lineNumber = 0;
			
			while ((line = reader.readLine()) != null) {
				
				lineNumber++;
				
				if (line.trim().isEmpty()) {
				    report.addError(lineNumber, line, 
				        String.format("A linha %d está vazia e não contém nenhuma informação.", lineNumber)
				    );
				    continue;
				}
				
				try {
					
					BookDTO book = parseLine(line);
					books.add(bookService.createOrUpdate(book));
					
				} catch (Exception e) {
	                report.addError(lineNumber, line, e.getMessage());
	                
	            }
			}
		}

		report.setBooks(books);

		report.getBooks().forEach(book -> System.out.println(book.toString()));
		
		report.getErrors().forEach(erro -> System.out.println(erro.toString()));
		
		return report;
	}
	
    /**
     * Converte uma linha do arquivo CSV em um objeto {@link BookDTO}.
     *
     * @param line linha do CSV
     * @return DTO representando o livro
     * @throws IllegalArgumentException caso o layout seja inválido
     */
	private BookDTO parseLine(String line) {

	    String[] columns = line.split(";");

	    if (columns.length < 5) {
	        throw new IllegalArgumentException("Layout inválido");
	    }
	    
	    return BookDTO.builder()
	    		.isbn(columns[0].trim())
	    		.title(columns[1].trim())
	    		.authors(parseAuthors(columns[2]))
	    		.publishers(parsePublishers(columns[3]))
	    		.publicationDate(parseDate(columns[4]))
	    		.build();
	}
	
    /**
     * Converte a coluna de autores em uma lista de {@link AuthorDTO}.
     *
     * @param value texto contendo os autores separados por vírgula
     * @return lista de autores
     */
	private List<AuthorDTO> parseAuthors(String value) {

	    List<AuthorDTO> authors = new ArrayList<>();

	    if (value == null || value.trim().isEmpty()) {
	        return authors;
	    }

	    String[] names = value.split(",");

	    for (String name : names) {
	    	authors.add(
	    			AuthorDTO.builder()
	    			.name(name.trim())
	    			.build()
	    			);
	    }

	    return authors;
	}
	
    /**
     * Converte a coluna de editoras em uma lista de {@link PublisherDTO}.
     *
     * @param value texto contendo as editoras separadas por vírgula
     * @return lista de editoras
     */
	private List<PublisherDTO> parsePublishers(String value) {

	    List<PublisherDTO> publishers = new ArrayList<>();

	    if (value == null || value.trim().isEmpty()) {
	        return publishers;
	    }

	    String[] names = value.split(",");

	    for (String name : names) {
	    	
	    	publishers.add(
	    			PublisherDTO.builder()
	    			.name(name.trim())
	    			.build()
	    			);
	    }

	    return publishers;
	}
	
    /**
     * Converte o valor textual da data em {@link LocalDate}.
     *
     * @param value valor textual da data
     * @return data convertida ou null caso vazio
     */
	private LocalDate parseDate(String value) {

	    if (value == null || value.trim().isEmpty()) {
	        return null;
	    }

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    return LocalDate.parse(value.trim(), formatter);
	}
	
}
