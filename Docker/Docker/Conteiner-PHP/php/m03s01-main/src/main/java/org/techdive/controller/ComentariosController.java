package org.techdive.controller;

import org.techdive.dto.ComentarioRequest;
import org.techdive.dto.ComentarioResponse;
import org.techdive.mapper.ComentarioMapper;
import org.techdive.model.Comentario;
import org.techdive.security.Authorize;
import org.techdive.service.VideosService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("/videos/{idVideo}/comentarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ComentariosController {

    @Inject
    private VideosService service;

    @Authorize
    @POST
    public Response inserir(@PathParam("idVideo") String idVideo, @Valid ComentarioRequest request) {
        Comentario comentario = ComentarioMapper.INSTANCE.toModel(request);
        Comentario inserido = service.inserirComentario(idVideo, comentario);
        ComentarioResponse resp = ComentarioMapper.INSTANCE.toResponse(inserido);
        return Response.created(URI.create(resp.getId().toString())).entity(resp).build();
    }

    @Authorize
    @DELETE
    @Path("/{idComentario}")
    public Response remover(@PathParam("idVideo") String idVideo, @PathParam("idComentario") Long idComentario) {
        service.removerComentario(idVideo, idComentario);
        return Response.noContent().build();
    }

    @GET
    public Response obter(@PathParam("idVideo") String idVideo) {
        List<Comentario> comentarios = service.obterComentarios(idVideo);
        List<ComentarioResponse> resp = comentarios.stream().map(ComentarioMapper.INSTANCE::toResponse).collect(Collectors.toList());
        return Response.ok(resp).build();
    }

    @GET
    @Path("/{idComentario}")
    public Response obter(@PathParam("idVideo") String idVideo, @PathParam("idComentario") Long idComentario) {
        Comentario comentario = service.obterComentario(idVideo, idComentario);
        ComentarioResponse resp = ComentarioMapper.INSTANCE.toResponse(comentario);
        resp.adicionarLinks(idVideo);
        return Response.ok(resp).links(resp.getLinks().toArray(new Link[0])).build();
    }

}
