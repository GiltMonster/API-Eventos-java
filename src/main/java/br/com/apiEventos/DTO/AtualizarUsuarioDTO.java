package br.com.apiEventos.DTO;

public class AtualizarUsuarioDTO {

    public String nome;
    public String sobreNome;
    public String email;

    public AtualizarUsuarioDTO(String nome, String sobreNome, String email) {
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.email = email;
    }
}
