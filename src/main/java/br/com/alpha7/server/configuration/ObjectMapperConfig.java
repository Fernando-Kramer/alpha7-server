package br.com.alpha7.server.configuration;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * <b>Descrição:</b><br>
 * Configura o {@link ObjectMapper} utilizado pelo JAX-RS (Jackson) na aplicação.
 * <br>
 * Responsável por registrar módulos necessários e ajustar o formato de
 * serialização de datas, garantindo compatibilidade com Java Time API e
 * evitando escrita em timestamp.
 * 
 * <b>Regras de configuração:</b>
 * <ul>
 *   <li>Registra o módulo {@link JavaTimeModule} para suporte a {@code LocalDate}, {@code LocalDateTime}, etc.</li>
 *   <li>Desabilita {@link SerializationFeature#WRITE_DATES_AS_TIMESTAMPS} para serialização em formato ISO-8601.</li>
 * </ul>
 *  
 * <b>Contexto de uso:</b>
 * Esta classe é automaticamente detectada pelo JAX-RS devido à anotação
 * {@link Provider} e fornece a configuração padrão de JSON para toda a aplicação.
 * 
 * @author Fernando Kramer De Souza
 * @since 1.0.0
 */
@Provider
public class ObjectMapperConfig implements ContextResolver<ObjectMapper> {

	private final ObjectMapper mapper;
	
	/**
	 * Constrói a configuração padrão do {@link ObjectMapper}.
	 * <p>
	 * Efetua registro dos módulos necessários e aplica os ajustes
	 * relacionados à serialização de datas.
	 * 
	 * @since 1.0.0
	 */
    public ObjectMapperConfig() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    /**
     * Retorna a instância configurada do {@link ObjectMapper} que será utilizada
     * pelo JAX-RS para serialização e desserialização JSON.
     * 
     * @param type classe solicitante do contexto.
     * @return instância configurada do {@link ObjectMapper}.
     * 
     * @since 1.0.0
     */
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
