package org.techdive.util;

import java.util.ArrayList;
import java.util.List;

public class Paginador<T> {

    private List<T> registros;

    private int tamanhoPagina;

    private int qtdPaginas;

    public Paginador(List<T> registros, int tamanhoPagina) {
        this.registros = registros;
        this.tamanhoPagina = tamanhoPagina;
        this.qtdPaginas = (int) Math.ceil( Float.valueOf(registros.size()) / Float.valueOf(tamanhoPagina) );
    }

    public List<T> obterPagina(int nroPag) {
        if (nroPag > qtdPaginas)
            return new ArrayList<>();
        int indiceInicial = (tamanhoPagina * (nroPag - 1));
        boolean isUltimaPag = qtdPaginas == nroPag;
        int regsSobrando = registros.size() % tamanhoPagina;
        int indiceFinal;
        if (!isUltimaPag || regsSobrando == 0) {
            indiceFinal = indiceInicial + tamanhoPagina;
        } else {
            indiceFinal = (indiceInicial + regsSobrando);
        }
        return new ArrayList<>(registros.subList(indiceInicial, indiceFinal)); // cópia por causa da imutabilidade
    }

    public int obterQtdPaginas() {
        return qtdPaginas;
    }

}
