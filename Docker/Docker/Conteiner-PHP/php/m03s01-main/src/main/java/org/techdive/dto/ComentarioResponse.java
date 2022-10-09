package org.techdive.dto;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Link;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ComentarioResponse implements Serializable {

    private Long id;

    private String texto;

    private LocalDateTime dataAtualizacao;

    private List<Link> links;


    public void adicionarLinks(String idVideo) {
        Link[] links = {
                Link.fromUri("/api/videos/{idVideo}/comentarios/{idComentario}").rel("self").type(HttpMethod.GET).build(idVideo, id),
                Link.fromUri("/api/videos/{idVideo}").rel("video").type(HttpMethod.GET).build(idVideo)
        };
        this.setLinks(Arrays.asList(links));
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

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}