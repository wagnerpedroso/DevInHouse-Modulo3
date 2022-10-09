package org.techdive.dao;

import org.techdive.model.Comentario;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;

@RequestScoped
@Transactional
public class ComentariosDao implements Serializable {

    @PersistenceContext(unitName = "VIDEOPU")
    EntityManager em;

    public Comentario inserir(Comentario comentario) {
        if (comentario.getDataInclusao() == null)
            comentario.setDataInclusao(LocalDateTime.now());
        em.persist(comentario);
        return comentario;
    }

    public void remover(Long idComentario) {
        Comentario comentario = em.find(Comentario.class, idComentario);
        em.remove(comentario);
    }

}
