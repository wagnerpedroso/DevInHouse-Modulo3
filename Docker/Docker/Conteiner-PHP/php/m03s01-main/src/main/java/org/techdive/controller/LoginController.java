package org.techdive.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.techdive.dto.LoginRequest;
import org.techdive.security.Segredo;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
public class LoginController {

    @POST
    public Response autenticar(@Valid LoginRequest credenciais) {
        boolean autenticado = autenticarUsuario(credenciais.getEmail(), credenciais.getSenha());
        if (!autenticado)
            return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciais inválidas").build();
        String token = gerarTokenJWT("james@kirk.com", "James T. Kirk");
        return Response.created(URI.create(token)).entity(token).build();
    }

    private String gerarTokenJWT(String email, String user) {
        String jwtToken = Jwts.builder()
                .setSubject(user)
                .setIssuer("TechTube")
                .setIssuedAt(new Date())
                .setExpiration(
                        Date.from(LocalDateTime.now().plusMinutes(30L).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(Segredo.CHAVE_SECRETA, SignatureAlgorithm.HS256)
                .compact();
        return jwtToken;
    }

    private boolean autenticarUsuario(String email, String senha) {
        // autenticacao fake, deve-se acesasr BD e conferir credenciais de usuario e senha
        return (email.equals("james@kirk.com") && senha.equals("1234"));
    }

}
