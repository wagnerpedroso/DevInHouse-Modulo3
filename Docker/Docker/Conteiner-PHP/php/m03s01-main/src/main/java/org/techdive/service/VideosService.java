package org.techdive.service;

import org.techdive.dao.ComentariosDao;
import org.techdive.dao.VideosDao;
import org.techdive.exception.RegistroExistenteException;
import org.techdive.exception.RegistroNaoEncontradoException;
import org.techdive.model.Comentario;
import org.techdive.model.Video;
import org.techdive.util.Paginador;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class VideosService {

    @Inject
    private VideosDao videosDao;
    @Inject
    private ComentariosDao comentariosDao;

    private static int TAMANHO_PAGINA = 4;


    public List<Video> obterVideos(String assunto, String ordenadoPor, Integer limite, Integer pagina) {
        // o ideal é aplicar os filtros e demais operacoes via SQL
        List<Video> videos = videosDao.obter();
        if (assunto != null && !assunto.isEmpty())
            videos = videos.stream().filter(v -> v.getAssunto().equals(assunto)).collect(toList());
        if (ordenadoPor != null)
            ordenarResultadoPor(ordenadoPor, videos);
        if (limite != null)
            videos = videos.subList(0, limite);
        if (pagina != null)
            videos = obterPagina(pagina, videos);
        return videos;
    }

    public Video inserirVideo(Video video) {
        verificarSeExisteVideoComURL(video);
        video.setId(UUID.randomUUID().toString());
        video.setDataInclusao(LocalDateTime.now());
        videosDao.salvar(video);
        return video;
    }

    public Video alterar(Video video) {
        verificarSeExisteVideoPorId(video.getId());
        verificarSeExisteOutroVideoComURL(video);
        videosDao.alterar(video);
        return video;
    }

    public void remover(String id) {
        verificarSeExisteVideoPorId(id);
        videosDao.remover(id);
    }

    public Video obterVideoPorId(String id) {
        Optional<Video> videoOpt = videosDao.obterPorId(id);
        return videoOpt.orElseThrow(() -> new RegistroNaoEncontradoException("Video", id));
    }

    public Integer adicionarLike(String id) {
        Video video = obterVideoPorId(id);
        video.incrementarLikes();
        videosDao.alterar(video);
        return video.getLikes();
    }

    public Integer retirarLike(String id) {
        Video video = obterVideoPorId(id);
        video.decrementarLikes();
        videosDao.alterar(video);
        return video.getLikes();
    }

    public Integer adicionarVisualizacao(String id) {
        Video video = obterVideoPorId(id);
        video.incrementarVisualizacao();
        video.setDataUltimaVisualizacao(LocalDateTime.now());
        videosDao.alterar(video);
        return video.getVisualizacoes();
    }

    public Comentario inserirComentario(String idVideo, Comentario comentario) {
        Video video = this.obterVideoPorId(idVideo);
        comentario.setVideo(video);
        return comentariosDao.inserir(comentario);
    }

    public List<Comentario> obterComentarios(String idVideo) {
        Video video = this.obterVideoPorId(idVideo);
        return video.getComentarios();
    }

    public void removerComentario(String idVideo, Long idComentario) {
        List<Comentario> comentarios = this.obterComentarios(idVideo);
        boolean existe = comentarios.stream().anyMatch(c -> c.getId().longValue() == idComentario.longValue());
        if (!existe)
            throw new RegistroNaoEncontradoException("Comentario", idComentario.toString());
        comentariosDao.remover(idComentario);
    }

    public Comentario obterComentario(String idVideo, Long idComentario) {
        List<Comentario> comentarios = this.obterComentarios(idVideo);
        Optional<Comentario> comentarioOpt = comentarios.stream().filter(c -> c.getId().longValue() == idComentario).findFirst();
        return comentarioOpt.orElseThrow(() -> new RegistroNaoEncontradoException("Comentario", idComentario.toString()));
    }

    private void verificarSeExisteVideoPorId(String id) {
        videosDao.obterPorId(id).orElseThrow(() -> new RegistroNaoEncontradoException("Video", id));
    }

    private void verificarSeExisteVideoComURL(Video video) {
        Optional<Video> videoOpt = videosDao.obterPorURL(video.getUrl());
        if (videoOpt.isPresent())
            throw new RegistroExistenteException("Video", video.getUrl());
    }

    private void verificarSeExisteOutroVideoComURL(Video video) {
        Optional<Video> videoOpt = videosDao.obterPorURL(video.getUrl());
        if (videoOpt.isPresent() && !videoOpt.get().getId().equals(video.getId()))
            throw new RegistroExistenteException("Video", video.getUrl());
    }

    private List<Video> obterPagina(Integer pagina, List<Video> videos) {
        Paginador<Video> paginador = new Paginador<>(videos, TAMANHO_PAGINA);
        List<Video> videosPaginados = paginador.obterPagina(pagina);
        return videosPaginados;
    }

    private void ordenarResultadoPor(String ordenadoPor, List<Video> videos) {
        if (ordenadoPor.equals("assunto"))
            Collections.sort(videos, Comparator.comparing(Video::getAssunto));
        else if (ordenadoPor.equals("usuario"))
            Collections.sort(videos, Comparator.comparing(Video::getUsuario));
    }
}
