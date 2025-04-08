package br.com.apiEventos.resource;

import br.com.apiEventos.DTO.AtualizarSenhaUsuario;
import br.com.apiEventos.DTO.AtualizarUsuarioDTO;
import br.com.apiEventos.DTO.LoginRequest;
import br.com.apiEventos.entitys.Usuario;
import br.com.apiEventos.utils.Messages;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource implements Messages {

    @GET
    @Operation(
            summary = "Listar todos os usuários",
            description = "Essa rota é responsável por listar todos os usuários do sistema."
    )
    public Response getAllEntities() {
        if (Usuario.listAll().isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .build();
        } else {
            return Response.ok(Usuario.listAll()).build();
        }
    }

    @GET
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
            @RequestBody(
                    description = "Dados do usuário a serem cadastrados",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(
                                    implementation = Usuario.class
                            )
                    )
            )

            Usuario usuario
    ) {
        usuario.

                persist();

        if (usuario.user_id != null) {
            return Response.

                    status(Response.Status.CREATED)
                            .

                    entity(usuario)
                            .

                    build();
        } else {
            return Response.

                    status(Response.Status.BAD_REQUEST)
                            .

                    entity(mensagemToJSON(Messages.MSG_CADASTRO_NAO_ENCONTRADO))
                            .

                    build();
        }
    }


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
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(mensagemToJSON("Senha atual incorreta"))
                    .build();
        }

        // Verifica se a nova senha é igual à atual
        if (BCrypt.checkpw(senha.novaSenha, usuarioToUpdate.getSenha())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(mensagemToJSON("Nova senha não pode ser igual à atual"))
                    .build();
        }

        // Atualiza a senha
        usuarioToUpdate.setSenha(senha.novaSenha);
        usuarioToUpdate.persist();

        return Response.ok(usuarioToUpdate).build();
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
}
