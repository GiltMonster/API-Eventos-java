package br.com.apiEventos.DTO;

import br.com.apiEventos.entitys.Evento;
import br.com.apiEventos.entitys.Usuario;

import java.util.List;

public class InscricaoDTO {
    public Long inscricao_id;
    public Usuario usuarios;
    public Evento eventosDisponiveis;
    public List<Evento> eventos;

    public InscricaoDTO() {
    }

    public InscricaoDTO(Long inscricao_id, Usuario usuarios, Evento eventosDisponiveis) {
        this.inscricao_id = inscricao_id;
        this.usuarios = usuarios;
        this.eventosDisponiveis = eventosDisponiveis;
    }

    public InscricaoDTO(Usuario usuarios, List<Evento> eventos) {
        this.usuarios = usuarios;
        this.eventos = eventos;
    }


}
