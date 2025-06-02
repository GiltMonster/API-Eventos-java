package br.com.apiEventos.apiKeyFilter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class ApiKeyFilter implements ContainerRequestFilter {

    // Removido: @ConfigProperty(name = "quarkus.api-key.value")
    // String apiKey;

    // Removido: @ConfigProperty(name = "quarkus.api-key.header-name", defaultValue = "X-API-Key")
    // String apiKeyHeader;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // Filtro desativado, não faz nada
        // Se desejar, pode remover completamente este filtro do projeto
    }
}
