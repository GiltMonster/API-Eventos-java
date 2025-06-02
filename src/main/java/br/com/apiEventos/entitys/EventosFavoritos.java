package br.com.apiEventos.entitys;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "eventos_favoritos")
public class EventosFavoritos extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long favId;
    @ManyToOne
    public Usuario usuarioFavorito;
    @ManyToOne
    public Evento eventoFavorito;

    public EventosFavoritos() {
    }

    public EventosFavoritos(Long favId, Usuario usuarioFavorito, Evento eventoFavorito) {
        this.favId = favId;
        this.usuarioFavorito = usuarioFavorito;
        this.eventoFavorito = eventoFavorito;
    }

}
