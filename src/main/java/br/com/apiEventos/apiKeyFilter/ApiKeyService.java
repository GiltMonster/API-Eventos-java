package br.com.apiEventos.apiKeyFilter;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApiKeyService {

    // Removido: @ConfigProperty(name = "quarkus.api-key.value")
    // String configuredApiKey;

    public boolean isValid(String apiKey) {
        // Sempre retorna false, pois não há mais chave fixa
        return false;
    }

    //Metodo opcional para obter informações associadas a uma API key
    public ApiKeyInfo getApiKeyInfo(String apiKey) {
        // Não há mais chave fixa
        return null;
    }

    // Classe interna para representar informações da API key
    public static class ApiKeyInfo {
        private final String username;
        private final String[] roles;

        public ApiKeyInfo(String username, String[] roles) {
            this.username = username;
            this.roles = roles;
        }

        public String getUsername() {
            return username;
        }

        public String[] getRoles() {
            return roles;
        }
    }
}
