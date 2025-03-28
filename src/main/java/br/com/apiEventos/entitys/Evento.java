package br.com.apiEventos.entitys;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "evento")
public class Evento extends PanacheEntity {

    public String nome;
    public Date data;
    public String descricao;

    public Evento() {
    }

    public Evento(String nome, Date data, String descricao) {
        this.nome = nome;
        this.data = data;
        this.descricao = descricao;
    }
}
