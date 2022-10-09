package org.techdive.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários de Paginador")
class PaginadorTest {

    @Test
    @DisplayName("Quando divisão exata Deve retornar quantidade de paginas exata")
    void obterQtdPaginas1() {
        List<Integer> regs = IntStream.rangeClosed(1, 100).mapToObj(Integer::valueOf).collect(Collectors.toList());
        Paginador<Integer> paginador = new Paginador<>(regs, 20);
        int result = paginador.obterQtdPaginas();
        assertEquals(5, result);
    }

    @Test
    @DisplayName("Quando divisão tem resto Deve retornar quantidade de paginas maior")
    void obterQtdPaginas2() {
        List<Integer> regs = IntStream.rangeClosed(1, 102).mapToObj(Integer::valueOf).collect(Collectors.toList());
        Paginador<Integer> paginador = new Paginador<>(regs, 20);
        int result = paginador.obterQtdPaginas();
        assertEquals(6, result);
    }


    @Test
    @DisplayName("Quando pagina inicial Deve retornar corretamente")
    void obterPagina1() {
        List<Integer> regs = IntStream.rangeClosed(1, 102).mapToObj(Integer::valueOf).collect(Collectors.toList());
        Paginador<Integer> paginador = new Paginador<>(regs, 20);
        List<Integer> result = paginador.obterPagina(1);
        assertEquals(1, result.get(0).intValue());
        assertEquals(20, result.get( result.size()-1 ).intValue());
    }

    @Test
    @DisplayName("Quando pagina intermediaria Deve retornar corretamente")
    void obterPagina2() {
        List<Integer> regs = IntStream.rangeClosed(1, 102).mapToObj(Integer::valueOf).collect(Collectors.toList());
        Paginador<Integer> paginador = new Paginador<>(regs, 20);
        List<Integer> result = paginador.obterPagina(2);
        assertEquals(21, result.get(0).intValue());
        assertEquals(40, result.get( result.size()-1 ).intValue());
    }

    @Test
    @DisplayName("Quando penultima pagina Deve retornar corretamente")
    void obterPagina3() {
        List<Integer> regs = IntStream.rangeClosed(1, 102).mapToObj(Integer::valueOf).collect(Collectors.toList());
        Paginador<Integer> paginador = new Paginador<>(regs, 20);
        List<Integer> result = paginador.obterPagina(5);
        assertEquals(81, result.get(0).intValue());
        assertEquals(100, result.get( result.size()-1 ).intValue());
    }

    @Test
    @DisplayName("Quando ultima pagina com registrando sobrando Deve retornar corretamente")
    void obterPagina4() {
        List<Integer> regs = IntStream.rangeClosed(1, 102).mapToObj(Integer::valueOf).collect(Collectors.toList());
        Paginador<Integer> paginador = new Paginador<>(regs, 20);
        int ultimaPagina = paginador.obterQtdPaginas();
        List<Integer> result = paginador.obterPagina(ultimaPagina);
        assertEquals(101, result.get(0).intValue());
        assertEquals(102, result.get( result.size()-1 ).intValue());
    }

    @Test
    @DisplayName("Quando ultima pagina completa Deve retornar corretamente")
    void obterPagina5() {
        List<Integer> regs = IntStream.rangeClosed(1, 100).mapToObj(Integer::valueOf).collect(Collectors.toList());
        Paginador<Integer> paginador = new Paginador<>(regs, 20);
        int ultimaPagina = paginador.obterQtdPaginas();
        List<Integer> result = paginador.obterPagina(ultimaPagina);
        assertEquals(81, result.get(0).intValue());
        assertEquals(100, result.get( result.size()-1 ).intValue());
    }

}