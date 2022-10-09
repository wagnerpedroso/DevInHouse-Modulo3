package org.techdive.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Date;

@Provider
@Authorize
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        try {
            String token = authorizationHeader.substring("Bearer".length()).trim();
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(Segredo.CHAVE_SECRETA).build().parseClaimsJws(token);
            Date expiration = claims.getBody().getExpiration();
            Date agora = new Date();
            if (expiration.before(agora))
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Token Expirado").build());
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("NÃO AUTORIZADO!").build());
        }
    }

}
