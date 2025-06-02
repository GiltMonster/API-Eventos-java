package br.com.apiEventos.requestsConfigs;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Filtro de rate limiting para endpoints REST.
 *
 * <p>Exemplo de uso:</p>
 * <pre>
 * - O filtro é aplicado automaticamente em todos os endpoints JAX-RS.
 * - Limites podem ser configurados por endpoint em ENDPOINT_LIMITS.
 * - Para testar:
 *   1. Faça várias requisições rápidas para um endpoint (ex: /v2/eventos).
 *   2. Quando exceder o limite, receberá HTTP 429 e headers:
 *      X-RateLimit-Limit: [limite]
 *      X-RateLimit-Remaining: [restante]
 *   3. Para simular usuário ADMIN, envie o header:
 *      X-User-Type: ADMIN
 * </pre>
 *
 * <p>Exemplo curl:</p>
 * <pre>
 *     curl -H "X-RateLimit-Limit: 10" http://localhost:8080/v2/apikeys/generate
 * curl -H "X-User-Type: ADMIN" http://localhost:8080/v2/eventos
 * * - Faz 10 requisições rápidas para o endpoint /v2/eventos.
 *
 * </pre>
 */
@Provider
public class RateLimitingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final long WINDOW_MILLIS = 60000; // 1 minuto
    private static final String HEADER_LIMIT = "X-RateLimit-Limit";
    private static final String HEADER_REMAINING = "X-RateLimit-Remaining";

    private static final Map<String, Window> ipWindows = new ConcurrentHashMap<>();

    // Exemplo de configuração de limites diferentes por endpoint ou tipo de usuário
    private static final Map<String, Integer> ENDPOINT_LIMITS = Map.of(
            "/v2/apikeys/generate", 10, // Exemplo: endpoint de geração de apikeys
            "/v2/eventos", 50 // Exemplo: endpoint de eventos
    );
    private static final int DEFAULT_LIMIT = 100;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String ip = getClientIp(requestContext);
        if (ip == null || ip.isEmpty()) {
            ip = requestContext.getUriInfo().getRequestUri().getHost(); // fallback
        }
        String path = requestContext.getUriInfo().getPath();
        int limit = ENDPOINT_LIMITS.getOrDefault(path, DEFAULT_LIMIT);
        // Exemplo: se quiser diferenciar por usuário, pode usar um header ou contexto de segurança
        String userType = requestContext.getHeaderString("X-User-Type");
        if (userType != null && userType.equals("ADMIN")) {
            limit = 200; // Exemplo: ADMIN tem limite maior
        }
        long now = Instant.now().toEpochMilli();
        String key = ip + ":" + path + (userType != null ? ":" + userType : "");
        Window window = ipWindows.computeIfAbsent(key, k -> new Window());
        synchronized (window) {
            if (now - window.windowStart > WINDOW_MILLIS) {
                window.count = 0;
                window.windowStart = now;
            }
            window.count++;
            if (window.count > limit) {
                requestContext.abortWith(Response.status(429)
                        .header(HEADER_LIMIT, limit)
                        .header(HEADER_REMAINING, 0)
                        .entity("Rate limit exceeded. Try again later.")
                        .build());
                return;
            }
        }
        requestContext.setProperty("rateLimit", limit);
        requestContext.setProperty("rateLimitKey", key);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Object limitObj = requestContext.getProperty("rateLimit");
        Object keyObj = requestContext.getProperty("rateLimitKey");
        int limit = DEFAULT_LIMIT;
        String key = null;
        if (limitObj instanceof Integer) {
            limit = (Integer) limitObj;
        }
        if (keyObj instanceof String) {
            key = (String) keyObj;
        }
        int remaining = limit;
        if (key != null) {
            Window window = ipWindows.get(key);
            if (window != null) {
                remaining = Math.max(0, limit - window.count);
            }
        }
        responseContext.getHeaders().add(HEADER_LIMIT, limit);
        responseContext.getHeaders().add(HEADER_REMAINING, remaining);
    }

    private String getClientIp(ContainerRequestContext requestContext) {
        String ip = requestContext.getHeaderString("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            return ip.split(",")[0].trim();
        }
        return requestContext.getHeaderString("X-Real-IP");
    }

    private static class Window {
        long windowStart = Instant.now().toEpochMilli();
        int count = 0;
    }
}
