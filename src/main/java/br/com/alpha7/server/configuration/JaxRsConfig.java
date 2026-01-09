package br.com.alpha7.server.configuration;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * <b>Descrição:</b><br>
 * Configuração principal do JAX-RS.
 * <br>
 * 
 * Define o caminho base para todos os endpoints REST da aplicação,
 * utilizando o contexto "/api".
 *
 * @author Fernando Kramer De Souza.
 * 
 * @since 1.0.0
 */
@ApplicationPath("/api")
public class JaxRsConfig extends Application {

}