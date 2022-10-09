package org.techdive.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.techdive.TestHelper;
import org.techdive.dao.ComentariosDao;
import org.techdive.dao.VideosDao;
import org.techdive.exception.RegistroExistenteException;
import org.techdive.exception.RegistroNaoEncontradoException;
import org.techdive.model.Video;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.techdive.TestHelper.gerarVideo;

@ExtendWith(MockitoExtension.class)
class VideosServiceTest {

    @Mock
    private VideosDao videosDao;
    @Mock
    private ComentariosDao comentariosDao;

    @InjectMocks
    private VideosService service;


    @Test
    @DisplayName("Quando video nao existente, Deve lançar exceção")
    void obterVideoPorId_falha() {
        Mockito.when(videosDao.obterPorId(anyString())).thenReturn(Optional.empty());
        assertThrows(RegistroNaoEncontradoException.class, () -> service.obterVideoPorId("id"));
    }

    @Test
    @DisplayName("Quando id existente existente, Deve retornar Video instanciado")
    void obterVideoPorId_sucesso() {
        // given
        Video video = gerarVideo();
        Mockito.when(videosDao.obterPorId(anyString())).thenReturn(Optional.of(video));
        // when
        Video result = service.obterVideoPorId("id");
        assertNotNull(result);
        assertInstanceOf(Video.class, result);
    }

    @Test
    @DisplayName("Quando URL jah existe, Deve lançar exceção")
    void inserirVideo_falhaURL() {
        Video video = gerarVideo();
        Mockito.when(videosDao.obterPorURL(anyString())).thenReturn(Optional.of(video));
        assertThrows(RegistroExistenteException.class, () -> service.inserirVideo(video));
    }

    @Test
    @DisplayName("Quando dados válidos de video, Deve gravar e retornar Video com id e data de inclusão")
    void inserirVideo_sucesso() {
        // given
        Video video = gerarVideo();
        video.setId(null);            // pre-condicao que o id esteja nulo
        video.setDataInclusao(null);  // pre-condicao que data inclusao esteja nula
        Mockito.when(videosDao.obterPorURL(anyString())).thenReturn(Optional.empty());
        // when
        Video result = service.inserirVideo(video);
        // then
        assertNotNull(result);
        assertInstanceOf(Video.class, result);
        assertNotNull(result.getId());
        assertNotNull(result.getDataInclusao());
    }

    @Test
    @DisplayName("Quando id existente, Deve apagar o video")
    void removerVideo_sucesso() {
        Mockito.when(videosDao.obterPorId(Mockito.anyString())).thenReturn(Optional.of(new Video()));
        assertDoesNotThrow(() -> service.remover("id"));
    }

    @Test
    @DisplayName("Quando dados de visualizacao validos, Deve adicionar visualizacao no video")
    void adicionarVisualizacao() {
        // given
        Video video = gerarVideo();
        int nroDeVisualizacoesOriginal = 10;
        video.setVisualizacoes(nroDeVisualizacoesOriginal);
        video.setDataUltimaVisualizacao(LocalDateTime.now());
        Mockito.when(videosDao.obterPorId(anyString())).thenReturn(Optional.of(video));
        // when
        Integer result = service.adicionarVisualizacao("id");
        // then
        assertNotNull(result);
        assertEquals(nroDeVisualizacoesOriginal + 1, result);
    }

    @Test
    @DisplayName("Quando não informado parametros de pesquisa, deve retornar todos os resultados")
    void obterVideos_semParametros() {
        List<Video> videosMock = TestHelper.gerarVideos();
        Mockito.when(videosDao.obter()).thenReturn(videosMock);
        List<Video> result = service.obterVideos(null, null, null, null);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(videosMock.size(), result.size());
    }

    @Test
    @DisplayName("Quando informado parametro de assunto, Deve retornar somente os resultados com este filtro")
    void obterVideos_paramAssunto() {
        // given
        List<Video> videosMock = TestHelper.gerarVideos();
        String assuntoPesquisado = videosMock.get(5).getAssunto();
        Mockito.when(videosDao.obter()).thenReturn(videosMock);
        // when
        List<Video> result = service.obterVideos(assuntoPesquisado, null, null, null);
        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(assuntoPesquisado, result.get(0).getAssunto());
    }

    @Test
    @DisplayName("Quando informado parametro de ordenacao, Deve retornar os resultados ordenados")
    void obterVideos_paramOrdenacao() {
        List<Video> videosMock = TestHelper.gerarVideos();
        videosMock.sort(Comparator.comparing(Video::getUsuario).reversed());
        assertTrue(videosMock.get(0).getUsuario().contains("9"));
        assertTrue(videosMock.get(videosMock.size()-1).getUsuario().contains("0"));
        Mockito.when(videosDao.obter()).thenReturn(videosMock);
        // when
        List<Video> result = service.obterVideos(null, "usuario", null, null);
        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(videosMock.size(), result.size());
        assertTrue(videosMock.get(0).getUsuario().contains("0"));
        assertTrue(videosMock.get(1).getUsuario().contains("1"));
        assertTrue(videosMock.get(videosMock.size()-1).getUsuario().contains("9"));
    }

    @Test
    @DisplayName("Quando informado parametro de limite, Deve retornar resultados limitados")
    void obterVideos_paramLimite() {
        List<Video> videosMock = TestHelper.gerarVideos();
        Mockito.when(videosDao.obter()).thenReturn(videosMock);
        int limite = 3;
        // when
        List<Video> result = service.obterVideos(null, null, limite, null);
        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(limite, result.size());
    }

    @Test
    @DisplayName("Quando informado parametro de paginas, Deve retornar pagina solicitada")
    void obterVideos_paramPagina() {
        List<Video> videosMock = TestHelper.gerarVideos();
        Mockito.when(videosDao.obter()).thenReturn(videosMock);
        int pagina = 3;
        // when
        List<Video> result = service.obterVideos(null, null, null, pagina);
        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertTrue(result.get(0).getUsuario().contains("8"));
        assertTrue(result.get(1).getUsuario().contains("9"));
    }

}
