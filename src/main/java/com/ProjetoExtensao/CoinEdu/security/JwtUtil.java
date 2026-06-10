package com.ProjetoExtensao.CoinEdu.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {


    @Value("${jwt.secret}")
    private String SECRET;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }


    // gera token para o email
    public String gerarToken(String email) {
        return Jwts.builder()  // começa a buildar
                .setSubject(email) // captura o email
                .setIssuedAt(new Date()) // define a data da criacao do token para o meial
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // define a expiracao da data da criacao do token
                .signWith(SignatureAlgorithm.HS256 , SECRET) /* Assina o token usando a chave secreta
                 garantindo que ninguem possa alterar o email sem ter o conhecimento da chave */
                .compact(); // transforma tudo em JWT
    }

    // extrai o token para
    public String extrairEmail(String token) {
        return Jwts.parser()// cria um leito para JWT
                .setSigningKey(SECRET) // informa para qual chave deve ser usada para validar
                .parseClaimsJws(token) // decodifica o token
                .getBody() // ele retorna o email
                .getSubject(); // recupera o campo do email
    }



    public boolean tokenValido(String token) {
        try {
            extrairEmail(token);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }




}
