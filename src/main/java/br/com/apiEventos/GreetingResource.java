package br.com.apiEventos;

import br.com.apiEventos.resource.EventoResource;
import br.com.apiEventos.resource.UsuarioResource;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/")
public class GreetingResource {

    @Path("usuarios")
    @Operation(
            summary = "Rotas referentes a usuários",
            description = "Essa rota é responsável por gerenciar os usuários do sistema."
    )
    public Class<UsuarioResource> users() {
        return UsuarioResource.class;
    }

    @Path("eventos")
    @Operation(
        summary = "Rotas referentes a eventos",
        description = "Essa rota é responsável por gerenciar os eventos do sistema."
    )
    public Class<EventoResource> eventos() {
        return EventoResource.class;
    }


}
