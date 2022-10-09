package org.techdive.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.techdive.dto.VideoRequest;
import org.techdive.dto.VideoResponse;
import org.techdive.exception.RegistroExistenteException;
import org.techdive.exception.RegistroNaoEncontradoException;
import org.techdive.mapper.VideoMapper;
import org.techdive.model.Video;
import org.techdive.service.VideosService;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.techdive.TestHelper.gerarVideo;
import static org.techdive.TestHelper.gerarVideoRequest;

@ExtendWith(MockitoExtension.class)
class VideosControllerTest {

    @Mock
    private VideosService service;

    @InjectMocks
    private VideosController controller;


    @Test
    @DisplayName("Quando requisição com id não existente, Deve retornar status NOT FOUND")
    void obterPorId_falhaNaoEncontrado() {
        Mockito.when(service.obterVideoPorId(anyString())).thenThrow(RegistroNaoEncontradoException.class);
        assertThrows(RegistroNaoEncontradoException.class, () -> controller.obterPorId("id"));
    }

    @Test
    @DisplayName("Quando requisição com id válido e existente, Deve retornar status OK e objeto de video")
    void obterPorId_sucesso() {
        // given
        Video video = gerarVideo();
        String id = video.getId();
        Mockito.when(service.obterVideoPorId(anyString())).thenReturn(video);
        // when
        Response result = controller.obterPorId(id);
        // then
        assertNotNull(result);
        assertNotNull(result.getEntity());
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        assertInstanceOf(VideoResponse.class, result.getEntity());
        VideoResponse resp = (VideoResponse) result.getEntity();
        assertFalse(resp.getLinks().isEmpty());
    }

    @Test
    @DisplayName("Quando sem videos cadastrados, Deve retornar status 200 com lista vazia")
    void obter_falha() {
        Mockito.when(service.obterVideos(anyString(), anyString(), anyInt(), anyInt())).thenReturn(new ArrayList<>());
        // when
        Response result = controller.obter("assunto", "ordenadoPor", 1, 1);
        // then
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        assertNotNull(result.getEntity(), "Deveria retornar objeto não nulo no response");
        List<VideoResponse> lista = (List<VideoResponse>) result.getEntity();
        assertTrue(lista.isEmpty(), "Deveria retornar lista vazia");
    }

    @Test
    @DisplayName("Quando existe videos cadastrados, Deve retornar status 200 com lista preenchida")
    void obter_sucesso() {
        List<Video> videos = Arrays.asList(gerarVideo(), gerarVideo());
        Mockito.when(service.obterVideos(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(videos);
        // when
        Response result = controller.obter("assunto", "ordenadoPor", 1, 1);
        // then
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        assertNotNull(result.getEntity(), "Deveria retornar objeto não nulo no response");
        List<VideoResponse> lista = (List<VideoResponse>) result.getEntity();
        assertFalse(lista.isEmpty(), "Deveria retornar lista não vazia");
        assertEquals(videos.size(), lista.size());
    }

    @Test
    @DisplayName("Quando requisição com URL já cadastrada, Deve retornar status CONFLICT")
    void inserir_jahExistente() {
        Mockito.when(service.inserirVideo(Mockito.any(Video.class))).thenThrow(RegistroExistenteException.class);
        assertThrows(RegistroExistenteException.class, () -> controller.inserir(new VideoRequest()));
    }

    @Test
    @DisplayName("Quando requisição com dados válidos, Deve retornar status OK e objeto de Video com id preenchido")
    void inserir_sucesso() {
        // given
        VideoRequest request = gerarVideoRequest();
        Video video = VideoMapper.INSTANCE.toModel(request);
        video.setId("id");
        Mockito.when(service.inserirVideo(Mockito.any(Video.class))).thenReturn(video);
        // when
        Response result = controller.inserir(request);
        // then
        assertNotNull(result);
        assertNotNull(result.getEntity());
        assertEquals(Response.Status.CREATED.getStatusCode(), result.getStatus());
        assertInstanceOf(VideoResponse.class, result.getEntity());
        VideoResponse resp = (VideoResponse) result.getEntity();
        assertNotNull(resp.getId());
    }

    @Test
    @DisplayName("Quando id não existente, Deve lançar exceção")
    void remover_falha() {
        Mockito.doThrow(new RegistroNaoEncontradoException("Video", "id")).when(service).remover(anyString());
        assertThrows(RegistroNaoEncontradoException.class, () -> controller.remover("id"));
    }

    @Test
    @DisplayName("Quando id existente, Deve retornar status NO CONTENT e remover video")
    void remover_sucesso() {
        Response result = controller.remover("id");
        assertNotNull(result);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), result.getStatus());
        assertNull(result.getEntity(), "Não deveria conter objeto no response (decisao de projeto)");
    }
    
    @Test
    void alterar_falha_registroNaoEncontrado() {
        String id = "id";
        VideoRequest request = new VideoRequest();
        Mockito.when(service.alterar(Mockito.any(Video.class))).thenThrow(new RegistroNaoEncontradoException("Video", id));
        RegistroNaoEncontradoException result = assertThrows(RegistroNaoEncontradoException.class, () -> controller.alterar(id, request));
        assertTrue(result.getMessage().contains(id));
    }

    @Test
    void alterar_falha_registroExistente() {
        String id = "id";
        VideoRequest request = new VideoRequest();
        Mockito.when(service.alterar(Mockito.any(Video.class))).thenThrow(new RegistroExistenteException("Video", id));
        RegistroExistenteException result = assertThrows(RegistroExistenteException.class, () -> controller.alterar(id, request));
        assertTrue(result.getMessage().contains(id));
    }

    @Test
    void alterar_sucesso() {
        // given
        String id = "id";
        VideoRequest request = new VideoRequest("url", "assunto", "usuario", 30);
        Video video = VideoMapper.INSTANCE.toModel(request);
        video.setId(id);
        Mockito.when(service.alterar(Mockito.any(Video.class))).thenReturn(video);
        // when
        Response result = controller.alterar(id, request);
        // then
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        assertNotNull(result.getEntity());
        assertInstanceOf(VideoResponse.class, result.getEntity());
    }

    @Test
    @DisplayName("Quando adiciona like em video não existente, Deve lançar exceção")
    void adicionarLike_falha() {
        Mockito.when(service.adicionarLike(Mockito.anyString())).thenThrow(new RegistroNaoEncontradoException("Video", "id"));
        assertThrows(RegistroNaoEncontradoException.class, () -> controller.adicionarLike("id"));
    }

    @Test
    @DisplayName("Quando adiciona like em video existente, Deve incrementar quantidade de likes e retornar Video response com id e qtd likes")
    void adicionarLike_sucesso() {
        Mockito.when(service.adicionarLike(Mockito.anyString())).thenReturn(1);
        Response result = controller.adicionarLike("id");
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus(), "Deveria retornar http status code 200");
        assertNotNull(result.getEntity(), "Deveria retornar um objeto Video Response");
        VideoResponse resp = (VideoResponse) result.getEntity();
        assertNotNull(resp.getId());
        assertNotNull(resp.getLikes());
    }

    @Test
    @DisplayName("Quando retira like em video não existente, Deve lançar exceção")
    void retirarLike_falha() {
        Mockito.when(service.retirarLike(Mockito.anyString())).thenThrow(new RegistroNaoEncontradoException("Video", "id"));
        assertThrows(RegistroNaoEncontradoException.class, () -> controller.retirarLike("id"));
    }

    @Test
    @DisplayName("Quando retira like em video existente, Deve decrementar quantidade de likes e retornar Video response com id e qtd likes")
    void retirarLike_sucesso() {
        Mockito.when(service.retirarLike(Mockito.anyString())).thenReturn(0);
        Response result = controller.retirarLike("id");
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus(), "Deveria retornar http status code 200");
        assertNotNull(result.getEntity(), "Deveria retornar um objeto Video Response");
        VideoResponse resp = (VideoResponse) result.getEntity();
        assertNotNull(resp.getId());
        assertNotNull(resp.getLikes());
    }

    @Test
    @DisplayName("Quando tenta visualizar video não existente, Deve lançar exceção")
    void visualizar_falha() {
        Mockito.when(service.adicionarVisualizacao(Mockito.anyString())).thenThrow(new RegistroNaoEncontradoException("Video", "id"));
        assertThrows(RegistroNaoEncontradoException.class, () -> controller.visualizar("id"));
    }

    @Test
    @DisplayName("Quando visualiza video existente, Deve incrementar quantidade de visualização e retornar Video response com id e qtd visualizacoes")
    void visualizar_sucesso() {
        Mockito.when(service.adicionarVisualizacao(Mockito.anyString())).thenReturn(1);
        Response result = controller.visualizar("id");
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus(), "Deveria retornar http status code 200");
        assertNotNull(result.getEntity(), "Deveria retornar um objeto Video Response");
        VideoResponse resp = (VideoResponse) result.getEntity();
        assertNotNull(resp.getId());
        assertNotNull(resp.getVisualizacoes());
    }

}