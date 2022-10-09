package org.techdive.security;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class Segredo {

    // chave tem q ter pelo menos 48 bits/caracteres, senao dá pau de seguranca
    public static final SecretKey CHAVE_SECRETA = Keys.hmacShaKeyFor("A!@#$%ASDFGHJKLQWERTYUIOMNBVCXJHGFDUYTR"
            .getBytes(StandardCharsets.UTF_8));

}
