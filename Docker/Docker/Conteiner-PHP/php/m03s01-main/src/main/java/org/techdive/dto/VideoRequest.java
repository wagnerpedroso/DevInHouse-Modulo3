package org.techdive.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Schema(name = "VideoRequisicao")
public class VideoRequest implements Serializable  {

    @NotNull(message = "Campo obrigatório: URL")
    private String url;

    @NotNull(message = "Campo obrigatório: Assunto")
    private String assunto;

    @NotNull(message = "Campo obrigatório: Usuario")
    private String usuario;

    @NotNull(message = "Campo obrigatório: Duração")
    private Integer duracao;

    public VideoRequest() { }

    public VideoRequest(String url, String assunto, String usuario, Integer duracao) {
        this.url = url;
        this.assunto = assunto;
        this.usuario = usuario;
        this.duracao = duracao;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }
}
