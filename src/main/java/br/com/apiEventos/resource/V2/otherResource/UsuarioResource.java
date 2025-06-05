package br.com.apiEventos.resource.V2.otherResource;

import br.com.apiEventos.DTO.AtualizarUsuarioDTO;
import br.com.apiEventos.entitys.Usuario;
import br.com.apiEventos.utils.IdempotencyUtil;
import br.com.apiEventos.utils.Messages;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import java.time.temporal.ChronoUnit;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"USER", "ADMIN"})
public class UsuarioResource implements Messages {

    @GET
    @RateLimit(
            value = 5,
            window = 10,
            minSpacing = 5,
            windowUnit = ChronoUnit.MINUTES
    )
    @Fallback(
       fallbackMethod = "getAllUsuariosFallback"
    )
    @Operation(
            summary = "Listar todos os usuários",
            description = "Essa rota é responsável por listar todos os usuários do sistema."
    )
    public Response getAllUsuarios() {
        if (Usuario.listAll().isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .build();
        } else {
            return Response.ok(Usuario.listAll()).build();
        }
    }


    @GET
    @RateLimit(
            value = 5,
            window = 10,
            minSpacing = 5,
            windowUnit = ChronoUnit.MINUTES
    )
    @Fallback(fallbackMethod = "getEntityByIdFallback")
    @Operation(
            summary = "Buscar usuário por ID",
            description = """
                    Essa rota é responsável por buscar um usuário pelo ID.\
                    Se o ID não for encontrado, retorna 404.\
                    OBS: O ID é gerado automaticamente pelo banco de dados, é unico e é numérico."""
    )
    @Path("findUsuario/{id}")
    public Response getEntityById(@PathParam("id") int id) {
        Usuario usuario = Usuario.findById(id);
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_NAO_ENCONTRADO_ID))
                    .build();
        } else {
            return Response.ok(usuario).build();
        }
    }

    @POST
    @Operation(
            summary = "Cadastrar usuário",
            description = """
                    Essa rota é responsável por cadastrar um usuário no sistema.\
                    Se o cadastro for realizado com sucesso, retorna 201.\
                    Se o cadastro não for realizado, retorna 400."""
    )
    @Transactional
    public Response cadastrar(
            @HeaderParam("Idempotency-Key") String idempotencyKey,
            @RequestBody(
                    description = "Dados do usuário a serem cadastrados",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(
                                    implementation = Usuario.class,
                                    requiredProperties = {"nome", "sobreNome", "email", "senha"},
                                    example = """
                                            {
                                                "nome": "João",
                                                "sobreNome": "Silva",
                                                "email": "joao@gmail.com",
                                                "senha": "senha123"
                                            }
                                            """
                            )
                    )
            )
            Usuario usuario
    ) {
        if (idempotencyKey == null || idempotencyKey.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(mensagemToJSON(Messages.MSG_KEY_IDEPENDECIA_USADA)).build();
        }
        Response cached = IdempotencyUtil.getResponseIfExists(idempotencyKey);
        if (cached != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_JA_REALIZADO))
                    .build();
        }
        usuario.persist();
        Response response;
        if (usuario.user_id != null) {
            response = Response.status(Response.Status.CREATED).entity(usuario).build();
        } else {
            response = Response.status(Response.Status.BAD_REQUEST).entity(mensagemToJSON(Messages.MSG_CADASTRO_NAO_ENCONTRADO)).build();
        }
        IdempotencyUtil.storeResponse(idempotencyKey, response);
        return response;
    }

    @PUT
    @Path("atualizarUsuario/{id}")
    @Operation(
            summary = "Atualizar usuário",
            description = """
                    Essa rota é responsável por atualizar um usuário no sistema.\
                    Se o usuário for encontrado, retorna 200.\
                    Se o usuário não for encontrado, retorna 404."""
    )
    @Transactional
    public Response atualizar(
            @Parameter(
                    description = "ID do usuário a ser atualizado",
                    required = true,
                    example = "1"
            )
            @PathParam("id") int id,
            AtualizarUsuarioDTO entity
    ) {

        Usuario usuarioToUpdate = Usuario.findById(id);
        if (usuarioToUpdate == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_NAO_ENCONTRADO_ID))
                    .build();
        } else {
            usuarioToUpdate.nome = entity.nome;
            usuarioToUpdate.sobreNome = entity.sobreNome;
            usuarioToUpdate.email = entity.email;
            usuarioToUpdate.persist();
            return Response.ok(usuarioToUpdate).build();
        }
    }

    @DELETE
    @Operation(
            summary = "Deletar usuário",
            description = """
                    Essa rota é responsável por deletar um usuário do sistema.\
                    Se o usuário for encontrado, retorna 200.\
                    Se o usuário não for encontrado, retorna 404."""
    )
    @Transactional
    @Path("deleteUsuario/{id}")
    public Response deleteUser(@PathParam("id") int id) {
        Usuario usuario = Usuario.findById(id);
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_DELETADO_ERRO))
                    .build();
        } else {
            usuario.delete();
            return Response.ok(mensagemToJSON(Messages.MSG_CADASTRO_DELETADO))
                    .build();
        }
    }

    @Override
    public String mensagemToJSON(String msg) {
        return "{" +
                "\"message\": \"" + msg + "\"" +
                "}";
    }

    public Response getAllUsuariosFallback() {
        return Response.status(Response.Status.TOO_MANY_REQUESTS)
                .entity(mensagemToJSON(Messages.MSG_SERVICO_INDISPONIVEL_RATE_LIMITADA))
                .build();
    }

    public Response getEntityByIdFallback(int id) {
        return Response.status(Response.Status.TOO_MANY_REQUESTS)
                .entity(mensagemToJSON(Messages.MSG_SERVICO_INDISPONIVEL_RATE_LIMITADA))
                .build();
    }
}
