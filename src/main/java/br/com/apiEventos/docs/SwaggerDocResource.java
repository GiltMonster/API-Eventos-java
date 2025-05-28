package br.com.apiEventos.docs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;

@Path("/docs")
public class SwaggerDocResource {

    @GET
    @Path("/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSwaggerDoc(@PathParam("version") String version) {
        String path = "/swagger/" + version + ".json";
        InputStream inputStream = getClass().getResourceAsStream(path);

        if (inputStream == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Documentação não encontrada para a versão: " + version)
                    .build();
        }

        return Response.ok(inputStream).build();
    }
}
