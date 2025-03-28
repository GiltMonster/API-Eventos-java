package br.com.apiEventos;

import br.com.apiEventos.entitys.Usuario;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class GreetingResource {

    @Path("users")
    public Class<Usuario> users() {
        return Usuario.class;
    }


}
