package br.com.apiEventos.resource.V2.otherResource;

import br.com.apiEventos.DTO.AtualizarSenhaUsuario;
import br.com.apiEventos.DTO.LoginRequest;
import br.com.apiEventos.entitys.Usuario;
import br.com.apiEventos.utils.Messages;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "USER"})
public class AuthResource implements Messages {

    @POST
    @Path("/login")
    @Operation(
            summary = "Realiza o login do usuário",
            description = "Verifica se o usuário está cadastrado e retorna os dados do usuário"
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Login realizado com sucesso",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            implementation = String.class
                                    )
                            )
                    ),
                    @APIResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @APIResponse(responseCode = "401", description = "Senha incorreta"),
                    @APIResponse(responseCode = "400", description = "Parâmetros obrigatórios: email e senha")

            }
    )
    @Transactional
    public Response loginUsuario(
            @Parameter(
                    description = "Dados do usuário para login",
                    required = true
            ) LoginRequest loginRequest) {

        if (loginRequest == null || loginRequest.email == null || loginRequest.senha == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(mensagemToJSON("Parâmetros obrigatórios: email e senha")).build();
        }

        Usuario usuarioEncontrado = Usuario.find("email", loginRequest.email).
                firstResult();

        if (usuarioEncontrado == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(mensagemToJSON(Messages.MSG_CADASTRO_NAO_ENCONTRADO)).build();
        }

        if (!BCrypt.checkpw(loginRequest.senha, usuarioEncontrado.getSenha())) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(mensagemToJSON("Senha incorreta")).build();
        }

        return Response.ok(usuarioEncontrado).
                build();
    }

    @PUT
    @Path("atualizarSenha/{id}")
    @Operation(
            summary = "Atualizar senha do usuário",
            description = """
                    Essa rota é responsável por atualizar a senha de um usuário no sistema.\
                    Se o usuário for encontrado, retorna 200.\
                    Se o usuário não for encontrado, retorna 404."""
    )
    @Transactional
    public Response atualizarSenha(@PathParam("id") int id, AtualizarSenhaUsuario senha) {
        Usuario usuarioToUpdate = Usuario.findById(id);

        if (usuarioToUpdate == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_NAO_ENCONTRADO_ID))
                    .build();
        }

        // Verifica se a senha atual está correta
        if (!BCrypt.checkpw(senha.senhaAtual, usuarioToUpdate.getSenha())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(mensagemToJSON("Senha atual incorreta"))
                    .build();
        }

        // Verifica se a nova senha é igual à atual
        if (BCrypt.checkpw(senha.novaSenha, usuarioToUpdate.getSenha())) {
            return Response.status(Response.Status.NOT_MODIFIED)
                    .entity(mensagemToJSON("Nova senha não pode ser igual à atual"))
                    .build();
        }

        // Atualiza a senha
        usuarioToUpdate.setSenha(senha.novaSenha);
        usuarioToUpdate.persist();

        return Response.ok(usuarioToUpdate).build();
    }


    @Override
    public String mensagemToJSON(String msg) {
        return "";
    }

    public Response getFallbackLogin(LoginRequest loginRequest) {
        return Response.status(Response.Status.TOO_MANY_REQUESTS)
                .entity(mensagemToJSON(Messages.MSG_SERVICO_INDISPONIVEL_RATE_LIMITADA))
                .build();
    }
    public Response getFallback(int id, AtualizarSenhaUsuario senha) {
        return Response.status(Response.Status.TOO_MANY_REQUESTS)
                .entity(mensagemToJSON(Messages.MSG_SERVICO_INDISPONIVEL_RATE_LIMITADA))
                .build();
    }
}
