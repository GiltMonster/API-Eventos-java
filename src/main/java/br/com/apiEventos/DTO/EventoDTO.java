package br.com.apiEventos.DTO;

import br.com.apiEventos.entitys.Evento;

import java.util.Date;

public class EventoDTO {
    public Long evento_id;
    public String nome;
    public String descricao;
    public Date dataInicio;
    public Date dataFim;
    public String localizacao;

    public EventoDTO(Evento evento) {
        this.evento_id = evento.evento_id;
        this.nome = evento.nome;
        this.descricao = evento.descricao;
        this.dataInicio = evento.dataInicio;
        this.dataFim = evento.dataFim;
        this.localizacao = evento.localizacao;
    }
}

