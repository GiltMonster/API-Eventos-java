package br.com.apiEventos.entitys;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "APIKEY")
public class ApiKey extends PanacheEntity {

    @Column(name = "apiKey", unique = true, nullable = false)
    public String apiKey;

    @ManyToOne
    public Usuario usuario;

    @Column(nullable = false)
    public String accessLevel; // exemplo: "USER", "ADMIN"

    public static ApiKey findByKey(String key) {
        return find("apiKey", key).firstResult();
    }
}
