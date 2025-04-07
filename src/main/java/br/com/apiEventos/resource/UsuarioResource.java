package br.com.apiEventos.resource;

import br.com.apiEventos.entitys.Usuario;
import br.com.apiEventos.utils.Messages;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.List;

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
    public Response cadastrar(Usuario usuario) {
        usuario.persist();

        if (usuario.user_id != null) {
            return Response.status(Response.Status.CREATED)
                    .entity(usuario)
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_NAO_ENCONTRADO))
                    .build();
        }
    }

    @PUT
    @Operation(
            summary = "Atualizar usuário",
            description = """
                    Essa rota é responsável por atualizar um usuário no sistema.\
                    Se o usuário for encontrado, retorna 200.\
                    Se o usuário não for encontrado, retorna 404."""
    )
    @Transactional
    public Response atualizar(Usuario entity) {

        Usuario usuarioToUpdate = Usuario.findById(entity.user_id);
        if (usuarioToUpdate == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_NAO_ENCONTRADO_ID))
                    .build();
        } else {
            usuarioToUpdate.nome = entity.nome;
            usuarioToUpdate.sobreNome = entity.sobreNome;
            usuarioToUpdate.email = entity.email;
            usuarioToUpdate.setSenha(entity.getSenha());
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
}
