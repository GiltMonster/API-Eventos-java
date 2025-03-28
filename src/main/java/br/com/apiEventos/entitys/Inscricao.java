package br.com.apiEventos.entitys;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "inscricao")
public class Inscricao extends PanacheEntity {
    @ManyToOne
    public Usuario usuario;
    @ManyToOne
    public Evento evento;
    public String dataInscricao;

    public Inscricao() {
    }

    public Inscricao(Usuario usuario, Evento evento, String dataInscricao) {
        this.usuario = usuario;
        this.evento = evento;
        this.dataInscricao = dataInscricao;
    }
}
