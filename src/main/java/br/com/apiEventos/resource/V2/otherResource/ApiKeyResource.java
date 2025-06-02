package br.com.apiEventos.resource.V2.otherResource;

import br.com.apiEventos.entitys.ApiKey;
import br.com.apiEventos.entitys.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@ApplicationScoped
@Path("/v2/apikeys")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiKeyResource {

    @POST
    @Path("/generate/{userId}")
    @Transactional
    public Response generateApiKey(@PathParam("userId") Long userId, @QueryParam("accessLevel") @DefaultValue("USER") String accessLevel) {
        Usuario usuario = Usuario.findById(userId);
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\": \"Usuário não encontrado\"}").build();
        }
        String key = generateRandomKey();
        ApiKey apiKey = new ApiKey();
        apiKey.apiKey = key;
        apiKey.usuario = usuario;
        apiKey.accessLevel = accessLevel;
        apiKey.persist();
        return Response.ok(apiKey).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response listApiKeys(@PathParam("userId") Long userId) {
        List<ApiKey> keys = ApiKey.list("usuario.id", userId);
        return Response.ok(keys).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteApiKey(@PathParam("id") Long id) {
        ApiKey key = ApiKey.findById(id);
        if (key == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\": \"Chave não encontrada\"}").build();
        }
        key.delete();
        return Response.noContent().build();
    }

    private String generateRandomKey() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
