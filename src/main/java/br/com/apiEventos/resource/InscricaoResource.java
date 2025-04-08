package br.com.apiEventos.resource;

import br.com.apiEventos.DTO.*;
import br.com.apiEventos.entitys.Evento;
import br.com.apiEventos.entitys.Inscricao;
import br.com.apiEventos.entitys.Usuario;
import br.com.apiEventos.utils.Messages;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InscricaoResource implements Messages {

    @GET
    @Path("/inscricoesUsuario/estaInscrito/{userId}/{eventoId}")
    @Operation(
            summary = "Verifica se um usuário está inscrito em um evento",
            description = "Retorna true se o usuário estiver inscrito no evento, false caso contrário"
    )
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Verificação realizada com sucesso"),
                    @APIResponse(responseCode = "400", description = "Parâmetros inválidos")
            }
    )
    public Response isUsuarioInscritoEmEvento(
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
        if (userId == null || eventoId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Parâmetros obrigatórios: userId e eventoId").build();
        }

        boolean inscrito = Inscricao.count("usuarioInscrito.user_id = ?1 AND eventosDisponiveis.evento_id = ?2", userId, eventoId) > 0;

        return Response.ok(inscrito).build();
    }


    @GET
    @Path("/inscricoesUsuario/{userId}")
    @Operation(
            summary = "Retorna todas as inscrições de um usuário",
            description = "Retorna todas as inscrições de um usuário com base no ID do usuário"
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Inscrições retornadas com sucesso"
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "Usuário não cadastrado"
                    )
            }
    )
    public Response getInscricoesUsuario(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathParam("userId") Long userId
    ) {
        Usuario usuario = Usuario.findById(userId);

        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(mensagemToJSON(Messages.MSG_CADASTRO_NAO_ENCONTRADO_ID)).build();
        }

        List<Inscricao> inscricoes = Inscricao.list("usuarioInscrito.user_id", userId);

        List<EventoInscritoDTO> eventosInscritos = inscricoes.stream().map(inscricao ->
                new EventoInscritoDTO(
                        inscricao.inscricao_id,
                        inscricao.eventosDisponiveis,
                        inscricao.dataInscricao
                )
        ).collect(Collectors.toList());

        UsuarioComEventosDTO resultado = new UsuarioComEventosDTO(usuario, eventosInscritos);

        return Response.ok(resultado).build();
    }

    @POST
    @Path("/realizaInscricao/{userId}/{eventoId}")
    @Operation(
            summary = "Inscreve um usuário em um evento",
            description = "Inscreve um usuário em um evento com base no ID do usuário e do evento"
    )
    @Transactional
    public Response postInscricao(
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
        Usuario usuario = Usuario.findById(userId);
        Evento evento = Evento.findById(eventoId);

        if (usuario == null || evento == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensagemToJSON(Messages.MSG_LISTA_ITENS_VAZIA))
                    .build();
        } else {
            var inscricao = new Inscricao(usuario, evento, new java.util.Date());
            inscricao.persist();
            return Response.ok(inscricao)
                    .build();
        }
    }

    @DELETE
    @Path("/inscricoesUsuario/{userId}/{eventoId}")
    @Operation(
            summary = "Remove a inscrição de um usuário em um evento",
            description = "Deleta a inscrição com base no ID do usuário e do evento"
    )
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Inscrição removida com sucesso"),
                    @APIResponse(responseCode = "404", description = "Inscrição não encontrada"),
                    @APIResponse(responseCode = "400", description = "Parâmetros inválidos")
            }
    )
    @Transactional
    public Response removerInscricao(
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
        if (userId == null || eventoId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(mensagemToJSON(mensagemToJSON("Parâmetros obrigatórios: userId e eventoId"))).build();
        }

        Inscricao inscricao = Inscricao.find("usuarioInscrito.user_id = ?1 AND eventosDisponiveis.evento_id = ?2", userId, eventoId).firstResult();

        if (inscricao == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(mensagemToJSON(Messages.MSG_CADASTRO_DELETADO_ERRO)).build();
        }

        inscricao.delete();

        return Response.ok(Messages.MSG_CADASTRO_DELETADO).build();
    }


    @PUT
    @Path("/inscricoesUsuario")
    @Operation(
            summary = "Atualiza a inscrição de um usuário",
            description = "Permite alterar o evento de uma inscrição existente"
    )
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Inscrição atualizada com sucesso"),
                    @APIResponse(responseCode = "404", description = "Inscrição ou novo evento não encontrados"),
                    @APIResponse(responseCode = "400", description = "Parâmetros inválidos")
            }
    )
    @Transactional
    public Response atualizarInscricao(
            @Parameter(
                    description = "Dados da inscrição a ser atualizada",
                    required = true
            )
            @RequestBody(
                    description = "Payload contendo userId, eventoAntigoId e eventoNovoId",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Map.class),
                            example = "{\"userId\": 1, \"eventoAntigoId\": 2, \"eventoNovoId\": 3}"
                    )
            )
            Map<String, Long> payload
    ) {
        Long userId = payload.get("userId");
        Long eventoAntigoId = payload.get("eventoAntigoId");
        Long eventoNovoId = payload.get("eventoNovoId");

        if (userId == null || eventoAntigoId == null || eventoNovoId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(mensagemToJSON("Parâmetros obrigatórios: userId, eventoAntigoId e eventoNovoId")).build();
        }

        Inscricao inscricao = Inscricao.find("usuarioInscrito.user_id = ?1 AND eventosDisponiveis.evento_id = ?2", userId, eventoAntigoId).firstResult();

        if (inscricao == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(mensagemToJSON(Messages.MSG_LISTA_ITENS_VAZIA)).build();
        }

        Evento novoEvento = Evento.findById(eventoNovoId);
        if (novoEvento == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(mensagemToJSON(Messages.MSG_LISTA_ITENS_VAZIA)).build();
        }

        inscricao.eventosDisponiveis = novoEvento;
        inscricao.persist();

        return Response.ok(mensagemToJSON(Messages.MSG_ATUALIZADO)).build();
    }


    @Override
    public String mensagemToJSON(String msg) {
        return "{\"message\": \"" + msg + "\"}";
    }
}
