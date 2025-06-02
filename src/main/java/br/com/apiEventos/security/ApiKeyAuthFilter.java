package br.com.apiEventos.security;

import br.com.apiEventos.entitys.ApiKey;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class ApiKeyAuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String apiKey = requestContext.getHeaderString("X-API-Key");

        if (isPublicRoute(requestContext.getUriInfo().getPath())) {
            return;
        }

        if (apiKey == null || apiKey.isBlank()) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"message\": \"API key obrigatória\"}").build());
            return;
        }

        ApiKey key = ApiKey.findByKey(apiKey);
        if (key == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"message\": \"API key inválida\"}").build());
        }
    }

    private boolean isPublicRoute(String path) {
        // Defina aqui suas rotas públicas que não requerem autenticação
        return path.contains("/v1/") || path.contains("/docs/") || path.contains("/apikeys/");
    }
}
