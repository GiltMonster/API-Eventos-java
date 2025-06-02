package br.com.apiEventos;

import br.com.apiEventos.resource.V1.V1Resource;
import br.com.apiEventos.resource.V2.V2Resource;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/")
public class GreetingResource {

    @Deprecated
    @Path("v1")
    @Operation(
            summary = "Versão 1 da API",
            description = "Essa rota é a versão 1 da API, onde estão localizadas as rotas principais do sistema."
    )
    public Class<V1Resource> v1() {
        return V1Resource.class;
    }

    @Path("v2")
    @Operation(
            summary = "Versão 2 da API",
            description = "Essa rota é a versão 2 da API, onde estão localizadas as rotas principais do sistema."
    )
    public Class<V2Resource> v2() {
        return V2Resource.class;
    }


}
