package br.com.apiEventos.entitys;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "evento")
public class Evento extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long evento_id;
    public String nome;
    public String descricao;
    public Date dataInicio;
    public Date dataFim;
    public String localizacao;

    public Evento() {
    }

    public Evento(String nome, Date data, String descricao, String localizacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = data;
        this.dataFim = data;
        this.localizacao = localizacao;
    }

    public Long getEvento_id() {
        return evento_id;
    }

    public void setEvento_id(Long evento_id) {
        this.evento_id = evento_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
}
