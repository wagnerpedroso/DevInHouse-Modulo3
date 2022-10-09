package org.techdive.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COMENTARIOS")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String  texto;

    private LocalDateTime dataInclusao;

    private LocalDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_VIDEO", referencedColumnName = "ID_VIDEO")
    private Video video;


    public Comentario() { }

    public Comentario(Long id, String texto, LocalDateTime dataInclusao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.texto = texto;
        this.dataInclusao = dataInclusao;
        this.dataAtualizacao = dataAtualizacao;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(LocalDateTime dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
