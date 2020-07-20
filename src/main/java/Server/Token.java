package Server;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static javax.crypto.Cipher.SECRET_KEY;

public class Token {
    private String JWS;
    public Token(long validPeriod){
        this.JWS = createJWS(validPeriod);
    }
    private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
  /*  private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
    private static Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
   */
    public static String createJWS(long validPeriod){
        String jws = null;
        if (validPeriod>0){
        long nowMillis = System.currentTimeMillis();
            long expMillis = nowMillis + validPeriod;
           Date exp = new Date(expMillis);

            try {
                jws = Jwts.builder()
                       .setSubject("timedToken")
                       .setId("id")
                       .setExpiration(exp)
                       .signWith(
                               signatureAlgorithm,"secret".getBytes("UTF-8")
                       )
                       .compact();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                jws = Jwts.builder()
                        .setSubject("authenticationToken")
                        .setId("id")
                        .signWith(
                                signatureAlgorithm,
                                "secret".getBytes("UTF-8")
                        )
                        .compact();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return jws;
    }
    public static void readJWS(String jws){
        try {
            Jwts.parser()
                    .setSigningKey("secret".getBytes("UTF-8"))
                    .parseClaimsJws(jws).getBody();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getJWS() {
        return JWS;
    }

 /*   public static void main(String[] args){
        String temp = new Token(1000).getJWS();
        System.out.println(temp);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        readJWS(temp);
        System.out.println("finished");
    }*/
}
