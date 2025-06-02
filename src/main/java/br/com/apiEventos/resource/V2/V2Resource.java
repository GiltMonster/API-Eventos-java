package br.com.apiEventos.resource.V2;

import br.com.apiEventos.resource.V2.otherResource.*;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.Operation;

public class V2Resource {

    @Path("usuarios")
    @Operation(
        summary = "Listar Usuários",
        description = "Essa rota é responsável por gerenciar os usuários do sistema."
    )
    public Class<UsuarioResource> users(){
        return UsuarioResource.class;
    }

    @Path("eventos")
    @Operation(
        summary = "Listar Eventos",
        description = "Essa rota é responsável por gerenciar os eventos do sistema."
    )
    public Class<EventoResource> eventos() {
        return EventoResource.class;
    }

    @Operation(
        summary = "Listar Inscrições",
        description = "Essa rota é responsável por gerenciar as inscrições dos usuários nos eventos."
    )
    @Path("inscricoes")
    public Class<InscricaoResource> inscricoes() {
        return InscricaoResource.class;
    }

    @Path("auth")
    @Operation(
        summary = "Autenticação",
        description = "Essa rota é responsável por autenticar usuários no sistema."
    )
    public Class<AuthResource> auth() {
        return AuthResource.class;
    }

    @Path("eventosFav")
    @Operation(
        summary = "Favoritos",
        description = "Essa rota é responsável por gerenciar os eventos favoritos dos usuários."
    )
    public Class<EventosFavoritosResource> eventosFavoritos() {
        return EventosFavoritosResource.class;
    }
}
