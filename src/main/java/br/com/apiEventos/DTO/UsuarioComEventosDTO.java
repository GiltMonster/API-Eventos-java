package br.com.apiEventos.DTO;

import br.com.apiEventos.entitys.Usuario;

import java.util.List;

public class UsuarioComEventosDTO {
    public UsuarioDTO usuario;
    public List<EventoInscritoDTO> eventosInscritos;

    public UsuarioComEventosDTO(Usuario usuario, List<EventoInscritoDTO> eventosInscritos) {
        this.usuario = new UsuarioDTO(usuario);
        this.eventosInscritos = eventosInscritos;
    }
}
