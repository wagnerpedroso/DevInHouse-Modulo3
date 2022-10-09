package org.techdive.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ComentarioRequest  implements Serializable {

    @NotNull(message = "Campo obrigatório: Texto")
    private String texto;


    public ComentarioRequest() { }

    public ComentarioRequest(String texto) {
        this.texto = texto;
    }


    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

}