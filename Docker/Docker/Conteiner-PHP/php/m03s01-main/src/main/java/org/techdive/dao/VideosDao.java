package org.techdive.dao;

import org.techdive.model.Video;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequestScoped
@Transactional
public class VideosDao implements Serializable {

    @PersistenceContext(unitName = "VIDEOPU")
    EntityManager em;

    public List<Video> obter() {
        List<Video> videos = em.createQuery("SELECT v FROM Video v", Video.class).getResultList();
        return videos;
    }

    public void salvar(Video video) {
        em.persist(video);
    }

    public Optional<Video> obterPorURL(String urlParam) {
        TypedQuery<Video> query = em.createQuery("SELECT v FROM Video v WHERE v.url = ?1", Video.class);
        try {
            Video video = query.setParameter(1, urlParam).getSingleResult();
            return Optional.of(video);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Video> obterPorId(String id) {
        Video video = em.find(Video.class, id);
        return video != null ? Optional.of(video) : Optional.empty();
    }

    public void alterar(Video alterado) {
        Video videoBD = em.find(Video.class, alterado.getId());
        videoBD.setDataAtualizacao(LocalDateTime.now());
        videoBD.setUrl(alterado.getUrl());
        videoBD.setAssunto(alterado.getAssunto());
        videoBD.setUsuario(alterado.getUsuario());
        videoBD.setDuracao(alterado.getDuracao());
        videoBD.setLikes(alterado.getLikes());
        videoBD.setVisualizacoes(alterado.getVisualizacoes());
        em.merge(videoBD);
    }

    public void remover(String id) {
        Video video = em.find(Video.class, id);
        em.remove(video);
    }

}
