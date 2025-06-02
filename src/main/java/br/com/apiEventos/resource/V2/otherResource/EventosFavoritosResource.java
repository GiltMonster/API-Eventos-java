package br.com.apiEventos.resource.V2.otherResource;

import br.com.apiEventos.entitys.Evento;
import br.com.apiEventos.entitys.EventosFavoritos;
import br.com.apiEventos.entitys.Usuario;
import br.com.apiEventos.utils.Messages;
import br.com.apiEventos.utils.IdempotencyUtil;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.faulttolerance.Fallback;
import java.time.temporal.ChronoUnit;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"USER", "ADMIN"})
public class EventosFavoritosResource implements Messages {

    @GET
    @RateLimit(
            value = 5,
            window = 10,
            minSpacing = 5,
            windowUnit = ChronoUnit.MINUTES
    )
    @Fallback(fallbackMethod = "fallbackParaRateLimitAllEventosFavoritos")
    @Operation(
            summary = "Listar eventos favoritos",
            description = "Essa rota é responsável por verificar se um usuário tem um evento favorito."
    )
    @Path("verificarFavorito/{userId}/{eventoId}/")
    public Response isUsuarioTemEventosFavoritos(
            @Parameter(
                    description = "ID do usuário",
                    required = true,
                    example = "1"
            )
            @PathParam("userId") Long userId,
            @Parameter(
                    description = "ID do evento",
                    required = true,
                    example = "1"
            )
            @PathParam("eventoId") Long eventoId
    ){
        if (userId == null || eventoId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Parâmetros obrigatórios: userId e eventoId").build();
        }

        boolean favorito = EventosFavoritos.count("usuarioFavorito.id = ?1 AND eventoFavorito.id = ?2", userId, eventoId) > 0;

        if (favorito) {
            return Response.ok("{\"favorito\": true}").build();
        } else {
            return Response.ok("{\"favorito\": false}").build();
        }
    }

    @GET
    @RateLimit(
            value = 5,
            window = 10,
            minSpacing = 5,
            windowUnit = ChronoUnit.MINUTES
    )
    @Fallback(fallbackMethod = "fallbackParaRateLimitAllEventosFavoritos")
    @Operation(
            summary = "Listar todos os eventos favoritos de um usuário",
            description = "Essa rota é responsável por listar todos os eventos favoritos de um usuário."
    )
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Lista de eventos favoritos retornada com sucesso"),
                    @APIResponse(responseCode = "404", description = "Nenhum evento favorito encontrado para o usuário")
            }
    )
    @Path("getAllEventosFavoritos/{userId}")
    public Response getAllEventosFavoritos(
            @Parameter(
                    description = "ID do usuário",
                    required = true,
                    example = "1"
            )
            @PathParam("userId") Long userId
    ) {
        if (userId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Parâmetro obrigatório: userId").build();
        }

        var eventosFavoritos = EventosFavoritos.list("usuarioFavorito.id", userId);
        if (eventosFavoritos.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_NAO_ENCONTRADO_ID))
                    .build();
        } else {
            return Response.ok(eventosFavoritos).build();
        }
    }

    @POST
    @Operation(
            summary = "Adicionar evento aos favoritos",
            description = "Essa rota é responsável por adicionar um evento aos favoritos de um usuário."
    )
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "201", description = "Evento adicionado aos favoritos com sucesso"),
                    @APIResponse(responseCode = "400", description = "Parâmetros inválidos")
            }
    )
    @Path("addEventoFavorito/{userId}/{eventoId}")
    @Transactional
    public Response addEventoFavorito(
            @HeaderParam("Idempotency-Key") String idempotencyKey,
            @Parameter(
                    description = "ID do usuário",
                    required = true,
                    example = "1"
            )
            @PathParam("userId") Long userId,
            @Parameter(
                    description = "ID do evento",
                    required = true,
                    example = "1"
            )
            @PathParam("eventoId") Long eventoId
    ) {
        if (idempotencyKey == null || idempotencyKey.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Cabeçalho Idempotency-Key obrigatório").build();
        }
        Response cached = IdempotencyUtil.getResponseIfExists(idempotencyKey);
        if (cached != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_JA_REALIZADO))
                    .build();
        }
        EventosFavoritos favorito = new EventosFavoritos();
        favorito.usuarioFavorito = Usuario.findById(userId);
        favorito.eventoFavorito = Evento.findById(eventoId);

        Response response;
        if (favorito.usuarioFavorito == null || favorito.eventoFavorito == null) {
            response = Response.status(Response.Status.BAD_REQUEST).entity(Messages.MSG_SEM_EVENTOS).build();
        } else {
            favorito.persist();
            response = Response.status(Response.Status.CREATED).entity(Messages.MSG_EVENTO_ADICIONADO_FAVORITOS).build();
        }
        IdempotencyUtil.storeResponse(idempotencyKey, response);
        return response;
    }

    @DELETE
    @Operation(
            summary = "Deletar evento favorito",
            description = "Essa rota é responsável por deletar um evento favorito de um usuário."
    )
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "204", description = "Evento favorito deletado com sucesso"),
                    @APIResponse(responseCode = "404", description = "Evento favorito não encontrado")
            }
    )
    @Transactional
    @Path("deleteEventoFavorito/{id}")
    public Response deleteEventoFavorito(
            @Parameter(
                    description = "ID do conjunto de eventos favorito",
                    required = true,
                    example = "1"
            )
            @PathParam("id") Long id
    ) {
        EventosFavoritos favorito = EventosFavoritos.findById(id);
        if (favorito == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_CADASTRO_NAO_ENCONTRADO_ID))
                    .build();
        } else {
            favorito.delete();
            return Response.noContent().build();
        }
    }

    @Override
    public String mensagemToJSON(String msg) {
        return "{" +
                "\"message\": \"" + msg + "\"" +
                "}";
    }

    public Response fallbackParaRateLimitAllEventosFavoritos(Long userId) {
        return Response.status(Response.Status.TOO_MANY_REQUESTS)
                .entity(mensagemToJSON(Messages.MSG_SERVICO_INDISPONIVEL_RATE_LIMITADA))
                .build();
    }
    public Response fallbackParaRateLimitAllEventosFavoritos(Long userId, Long eventoId) {
        return Response.status(Response.Status.TOO_MANY_REQUESTS)
                .entity(mensagemToJSON(Messages.MSG_SERVICO_INDISPONIVEL_RATE_LIMITADA))
                .build();
    }
}

