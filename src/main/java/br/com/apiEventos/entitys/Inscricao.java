package br.com.apiEventos.entitys;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "inscricao")
public class Inscricao extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long inscricao_id;
    @ManyToOne
    public Usuario usuarioInscrito;
    @ManyToOne
    public Evento eventosDisponiveis;
    public Date dataInscricao;

    public Inscricao() {
    }

    public Inscricao(Usuario usuarioInscrito, Evento eventosDisponiveis, Date dataInscricao) {
        this.usuarioInscrito = usuarioInscrito;
        this.eventosDisponiveis = eventosDisponiveis;
        this.dataInscricao = dataInscricao;
    }
}