package br.com.alpha7.server.integration;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

import br.com.alpha7.server.infrastructure.dto.BookDTO;
import br.com.alpha7.server.infrastructure.dto.PublisherDTO;
import br.com.alpha7.server.infrastructure.exception.ExternalServiceException;
import br.com.alpha7.server.infrastructure.exception.IsbnNotFoundException;
import br.com.alpha7.server.infrastructure.validation.IsbnValidator;

/**
 * <b>Descrição:</b><br>
 * Componente responsável por integrar com o serviço externo
 * <a href="https://openlibrary.org">OpenLibrary</a>, permitindo a consulta
 * de informações de livros a partir do ISBN.
 *
 * <p>
 * Esta classe realiza a comunicação HTTP, tratamento de respostas,
 * conversão do JSON retornado para {@link BookDTO} e mapeamento de
 * exceções funcionais para erros padronizados da aplicação.
 * </p>
 *
 * <h3>Comportamento</h3>
 * <ul>
 *     <li>Valida o ISBN antes da consulta</li>
 *     <li>Realiza chamada HTTP ao serviço OpenLibrary</li>
 *     <li>Retorna status funcional adequado em caso de falhas</li>
 *     <li>Registra logs estruturados para auditoria</li>
 * </ul>
 *
 * <h3>Possíveis Exceções</h3>
 * <ul>
 *     <li>{@link IsbnNotFoundException} — ISBN não encontrado na OpenLibrary</li>
 *     <li>{@link ExternalServiceException} — Erros de comunicação externa</li>
 *     <li>{@link InvalidIsbnException} — ISBN informado é inválido</li>
 * </ul>
 *
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@Stateless
public class OpenLibraryIntegration {

	/** Logger responsável pelos registros de processamento da integração. */
	private static final Logger LOG = 
			Logger.getLogger(OpenLibraryIntegration.class.getName());
	
    /**
     * URL base do serviço OpenLibrary.
     * Obtida via variável de ambiente {@code OPEN_LIBRARY_BASE_URL},
     * utilizando endereço padrão caso não configurada.
     */
	private static final String BASE_URL = 
			System.getenv().getOrDefault(
					"OPEN_LIBRARY_BASE_URL", 
					"https://openlibrary.org/isbn/"
					);
	
    /**
     * Realiza consulta na OpenLibrary para obter informações de um livro
     * com base no ISBN informado.
     *
     * <p>
     * O método valida o ISBN, executa a requisição HTTP, interpreta
     * o JSON retornado e converte para {@link BookDTO}.
     * </p>
     *
     * @param isbn código ISBN informado pelo cliente
     * @return objeto {@link BookDTO} contendo os dados retornados pelo serviço
     *
     * @throws InvalidIsbnException caso o ISBN seja inválido
     * @throws IsbnNotFoundException caso o ISBN não exista na OpenLibrary
     * @throws ExternalServiceException falhas de comunicação ou erros inesperados
     */
	public BookDTO findByIsbn(String isbn) {
		
		LOG.info(String.format("[OPEN_LIBRARY] INICIO | Consulta ISBN: %s", isbn));
		
		long start = System.currentTimeMillis();
		
		String validIsbn = IsbnValidator.validate(isbn);
		
		HttpURLConnection connection = null;
		
		try {
			
			URL url = new URL(BASE_URL + validIsbn + ".json");
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			int status = connection.getResponseCode();
			
			if (status == HttpURLConnection.HTTP_NOT_FOUND) {
				String message = "ISBN não encontrado na OpenLibrary";
				LOG.warning(String.format(
						"[OPEN_LIBRARY] ERRO | ISBN: %s | Motivo: %s", isbn, message));
	            throw new IsbnNotFoundException(message);
	        }
			
			if (status != HttpURLConnection.HTTP_OK) {
				String message = "Erro ao consultar OpenLibrary. Status HTTP: " + status;
				LOG.warning(String.format(
						"[OPEN_LIBRARY] ERRO | Motivo: %s", message));
	            throw new ExternalServiceException(message);
	        }
			
	        try (InputStream is = connection.getInputStream();
	                JsonReader reader = Json.createReader(is)) {

	               JsonObject json = reader.readObject();

	               BookDTO book = new BookDTO();
	               book.setIsbn(validIsbn);
	               book.setTitle(json.getString("title", null));
	               book.setPublishers(extractPublishers(json));
	               book.setPublicationDate(extractPublicationDate(json));
	               return book;
	           }
			
		} catch (IOException | JsonException e) {
			LOG.severe(String.format(
					"[OPEN_LIBRARY] ERRO | ISBN: %s | Falha inesperada: %s", isbn, e.getMessage()));
			throw new ExternalServiceException("Falha de comunicação com a OpenLibrary", e);
			
		} finally {
			if(connection != null) 
				connection.disconnect();
			LOG.info(String.format(
					"[OPEN_LIBRARY]    FIM | Consulta ISBN: %s | TIME: %d ms", isbn, System.currentTimeMillis() - start));
		}
		
	}
	
    /**
     * Extrai e converte a lista de editoras do JSON retornado pela OpenLibrary.
     *
     * @param json objeto JSON de resposta da OpenLibrary
     * @return lista de {@link PublisherDTO} ou lista vazia caso não existam editoras
     */
	private List<PublisherDTO> extractPublishers(JsonObject json) {

	    if (!json.containsKey("publishers") || json.isNull("publishers")) {
	        return Collections.emptyList();
	    }

	    JsonArray publishersArray = json.getJsonArray("publishers");

	    if (publishersArray.isEmpty()) {
	        return Collections.emptyList();
	    }

	    List<PublisherDTO> publishers = new ArrayList<>();

	    for (JsonValue value : publishersArray) {
	        if (value.getValueType() == JsonValue.ValueType.STRING) {
	            PublisherDTO publisher = new PublisherDTO();
	            publisher.setName(((JsonString) value).getString());
	            publishers.add(publisher);
	        }
	    }

	    return publishers;
	}
	
    /**
     * Extrai e converte a data de publicação do JSON retornado pela OpenLibrary.
     *
     * <p>
     * Caso a data esteja em formato inesperado ou inválido, o método
     * não lança exceção e retorna {@code null}.
     * </p>
     *
     * @param json objeto JSON retornado pela OpenLibrary
     * @return data de publicação ou {@code null} se ausente ou inválida
     */
	private LocalDate extractPublicationDate(JsonObject json) {

	    if (!json.containsKey("publish_date") || json.isNull("publish_date")) {
	        return null;
	    }

	    String publishDate = json.getString("publish_date").trim();

	    try {
	        DateTimeFormatter formatter =
	                DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);

	        return LocalDate.parse(publishDate, formatter);

	    } catch (DateTimeParseException e) {
	        return null;
	        
	    }
	}
	
}
