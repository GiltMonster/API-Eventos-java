package br.com.apiEventos.resource.V2.otherResource;

import br.com.apiEventos.entitys.Evento;
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
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.time.temporal.ChronoUnit;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"USER", "ADMIN"}) // Opcional: para níveis de acesso
public class EventoResource implements Messages {

    @GET
    @RateLimit(
            value = 5,
            window = 10,
            minSpacing = 5,
            windowUnit = ChronoUnit.MINUTES
    )
    @Fallback(fallbackMethod = "getAllEventosFallback")
    @Operation(
            summary = "Listar todos os eventos",
            description = "Essa rota é responsável por listar todos os eventos do sistema."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Eventos encontrados com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Evento.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Eventos encontrados",
                                                    description = "Lista de eventos encontrados",
                                                    value = """
                                                            [
                                                                {
                                                                    "evento_id": 1,
                                                                    "nome": "Evento 1",
                                                                    "descricao": "Descrição do evento 1",
                                                                    "dataInicio": "2023-10-01T00:00:00Z",
                                                                    "dataFim": "2023-10-02T00:00:00Z",
                                                                    "localizacao": "Localização do evento 1"
                                                                },
                                                                {
                                                                    "evento_id": 2,
                                                                    "nome": "Evento 2",
                                                                    "descricao": "Descrição do evento 2",
                                                                    "dataInicio": "2023-10-03T00:00:00Z",
                                                                    "dataFim": "2023-10-04T00:00:00Z",
                                                                    "localizacao": "Localização do evento 2"
                                                                }
                                                            ]
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Nenhum evento encontrado",
                                                    description = "Nenhum evento encontrado no sistema"
                                            )
                                    }
                            )
                    ),
                    @APIResponse(
                            responseCode = "204",
                            description = "Nenhum evento encontrado"
                    )
            }
    )
    public Response getAllEvento() {
        if (Evento.listAll().isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .build();
        } else {
            return Response.ok(Evento.listAll()).build();
        }
    }


    @GET
    @RateLimit(
            value = 5,
            window = 10,
            minSpacing = 5,
            windowUnit = ChronoUnit.MINUTES
    )
    @Fallback(fallbackMethod = "getAllEventosFallback")
    @Path("findEvento/{id}")
    @Operation(
            summary = "Buscar evento por ID",
            description = """
                    Essa rota é responsável por buscar um evento pelo ID.\
                    Se o ID não for encontrado, retorna 404.\
                    OBS: O ID é gerado automaticamente pelo banco de dados, é unico e é numérico."""
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Evento encontrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Evento.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Evento encontrado",
                                                    description = "Evento encontrado com sucesso",
                                                    value = """
                                                            {
                                                                "evento_id": 1,
                                                                "nome": "Evento 1",
                                                                "descricao": "Descrição do evento 1",
                                                                "dataInicio": "2023-10-01T00:00:00Z",
                                                                "dataFim": "2023-10-02T00:00:00Z",
                                                                "localizacao": "Localização do evento 1"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "Evento não encontrado"
                    )
            }
    )
    public Response getEventoyById(
            @Parameter(
                    description = "ID do evento a ser buscado",
                    required = true,
                    example = "1"
            )
            @PathParam("id") int id
    ) {
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
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "201",
                            description = "Evento cadastrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Evento.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Evento cadastrado",
                                                    description = "Evento cadastrado com sucesso",
                                                    value = """
                                                            {
                                                                "nome": "Evento 1",
                                                                "descricao": "Descrição do evento 1",
                                                                "dataInicio": "2023-10-01T00:00:00Z",
                                                                "dataFim": "2023-10-02T00:00:00Z",
                                                                "localizacao": "Localização do evento 1"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @APIResponse(
                            responseCode = "400",
                            description = "Erro ao cadastrar evento",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Evento.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Erro ao cadastrar evento",
                                                    description = "Erro ao cadastrar evento",
                                                    value = """
                                                            {
                                                                "message": "Erro ao cadastrar evento"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    @Operation(
            summary = "Cadastrar evento",
            description = """
                    Essa rota é responsável por cadastrar um evento no sistema.\
                    Se o cadastro for realizado com sucesso, retorna 201.\
                    Se o cadastro não for realizado, retorna 400."""
    )
    @Transactional
    public Response cadastrarEvento(
            @HeaderParam("Idempotency-Key") String idempotencyKey,
            @Parameter(
                    description = "Evento a ser cadastrado",
                    required = true
            )
            Evento evento
    ) {
        if (idempotencyKey == null || idempotencyKey.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Messages.MSG_KEY_IDEPENDECIA_USADA).build();
        }
        Response cached = IdempotencyUtil.getResponseIfExists(idempotencyKey);
        if (cached != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_JA_REALIZADO))
                    .build();
        }
        if (evento == null) {
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_VAZIO))
                    .build();
            IdempotencyUtil.storeResponse(idempotencyKey, response);
            return response;
        } else {
            evento.persist();
            Response response = Response.status(Response.Status.CREATED)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO))
                    .build();
            IdempotencyUtil.storeResponse(idempotencyKey, response);
            return response;
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
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Evento atualizado com sucesso"
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "Evento não encontrado"
                    )
            }
    )
    @Transactional
    public Response atualizarEvento(
            @Parameter(
                    description = "Evento a ser atualizado",
                    required = true
            )
            Evento evento
    ) {
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
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Evento deletado com sucesso"
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "Evento não encontrado"
                    )
            }
    )
    @Operation(
            summary = "Deletar evento",
            description = """
                    Essa rota é responsável por deletar um evento do sistema.\
                    Se o evento for deletado com sucesso, retorna 200.\
                    Se o evento não for encontrado, retorna 404."""
    )
    @Path("deletarEvento/{id}")
    @Transactional
    public Response deletarEvento(
            @Parameter(
                    description = "ID do evento a ser deletado",
                    required = true,
                    example = "1"
            )
            @PathParam("id") int id
    ) {
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

    public Response getAllEventosFallback() {
        return Response.status(Response.Status.TOO_MANY_REQUESTS)
                .entity(mensagemToJSON(Messages.MSG_SERVICO_INDISPONIVEL_RATE_LIMITADA))
                .build();
    }
    public Response getAllEventosFallback(int id) {
        return Response.status(Response.Status.TOO_MANY_REQUESTS)
                .entity(mensagemToJSON(Messages.MSG_SERVICO_INDISPONIVEL_RATE_LIMITADA))
                .build();
    }
}
