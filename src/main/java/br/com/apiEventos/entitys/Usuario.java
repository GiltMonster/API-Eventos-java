package br.com.apiEventos.entitys;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.ws.rs.POST;

@Entity
@Table(name = "usuario")
public class Usuario extends PanacheEntity {

    public String nome;
    public String sobreNome;
    public String email;
    private String senha;

    public Usuario() {
    }

    public Usuario(String nome, String sobreNome, String email, String senha) {
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.email = email;
        this.senha = senha;
    }

    @POST
    public void salvar() {


    }

}
