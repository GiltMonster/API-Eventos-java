package br.com.apiEventos.DTO;

import br.com.apiEventos.entitys.Evento;

import java.util.Date;

public class EventoInscritoDTO {
    public Long inscricao_id;
    public EventoDTO evento;
    public Date dataInscricao;

    public EventoInscritoDTO(Long inscricao_id, Evento evento, Date dataInscricao) {
        this.inscricao_id = inscricao_id;
        this.evento = new EventoDTO(evento);
        this.dataInscricao = dataInscricao;
    }
}
