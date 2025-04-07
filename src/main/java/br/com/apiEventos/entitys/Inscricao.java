package br.com.apiEventos.entitys;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "inscricao")
public class Inscricao extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long inscricao_id;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public String getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(String dataInscricao) {
        this.dataInscricao = dataInscricao;
    }
}
