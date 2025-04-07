package br.com.apiEventos.resource;

import br.com.apiEventos.entitys.Evento;
import br.com.apiEventos.utils.Messages;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventoResource implements Messages {

    @GET
    @Operation(
            summary = "Listar todos os eventos",
            description = "Essa rota é responsável por listar todos os eventos do sistema."
    )
    public Response getAllEntities() {
        if (Evento.listAll().isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .build();
        } else {
            return Response.ok(Evento.listAll()).build();
        }
    }

    @GET
    @Path("findEvento/{id}")
    @Operation(
            summary = "Buscar evento por ID",
            description = """
                    Essa rota é responsável por buscar um evento pelo ID.\
                    Se o ID não for encontrado, retorna 404.\
                    OBS: O ID é gerado automaticamente pelo banco de dados, é unico e é numérico."""
    )
    public Response getEventoyById(@PathParam("id") int id) {
        Evento evento = Evento.findById(id);
        if (evento == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_NAO_ENCONTRADO_ID))
                    .build();
        } else {
            return Response.ok(evento).build();
        }
    }

    @POST
    @Operation(
            summary = "Cadastrar evento",
            description = """
                    Essa rota é responsável por cadastrar um evento no sistema.\
                    Se o cadastro for realizado com sucesso, retorna 201.\
                    Se o cadastro não for realizado, retorna 400."""
    )
    @Transactional
    public Response cadastrarEvento(Evento evento) {
        if (evento == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_VAZIO))
                    .build();
        } else {
            evento.persist();
            return Response.status(Response.Status.CREATED)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO))
                    .build();
        }
    }

    @PUT
    @Operation(
            summary = "Atualizar evento",
            description = """
                    Essa rota é responsável por atualizar um evento no sistema.\
                    Se o evento for atualizado com sucesso, retorna 200.\
                    Se o evento não for encontrado, retorna 404."""
    )
    @Transactional
    public Response atualizarEvento(Evento evento) {
        Evento eventoToUpdate = Evento.findById(evento.getEvento_id());
        if (eventoToUpdate == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_ATUALIZADO_ERRO))
                    .build();
        } else {
            eventoToUpdate.setNome(evento.getNome());
            eventoToUpdate.setDescricao(evento.getDescricao());
            eventoToUpdate.setDataInicio(evento.getDataInicio());
            eventoToUpdate.setDataFim(evento.getDataFim());
            eventoToUpdate.setLocalizacao(evento.getLocalizacao());
            eventoToUpdate.persist();
            return Response.ok(mensagemToJSON(Messages.MSG_ATUALIZADO)).build();
        }
    }

    @DELETE
    @Operation(
            summary = "Deletar evento",
            description = """
                    Essa rota é responsável por deletar um evento do sistema.\
                    Se o evento for deletado com sucesso, retorna 200.\
                    Se o evento não for encontrado, retorna 404."""
    )
    @Path("deletarEvento/{id}")
    @Transactional
    public Response deletarEvento(@PathParam("id") int id) {
        Evento eventoToDelete = Evento.findById(id);

        if (eventoToDelete == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_NAO_ENCONTRADO_ID))
                    .build();
        } else {
            eventoToDelete.delete();
            return Response.ok(mensagemToJSON(Messages.MSG_CADASTRO_DELETADO)).build();
        }
    }

    @Override
    public String mensagemToJSON(String msg) {
        return "{" +
                "\"message\": \"" + msg + "\"" +
                "}";
    }
}
