package br.com.apiEventos.DTO;

import br.com.apiEventos.entitys.Usuario;

public class UsuarioDTO {
    public Long user_id;
    public String nome;
    public String sobreNome;
    public String email;

    public UsuarioDTO(Usuario usuario) {
        this.user_id = usuario.user_id;
        this.nome = usuario.nome;
        this.sobreNome = usuario.sobreNome;
        this.email = usuario.email;
    }
}
