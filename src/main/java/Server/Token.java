package Server;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
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
        Date now = new Date(nowMillis);
            long expMillis = nowMillis + validPeriod;
           Date exp = new Date(expMillis);

            try {
                jws = Jwts.builder()
                       .setSubject("timedToken")
                       .setId("id")
                       .setExpiration(exp)
                       .signWith(
                               signatureAlgorithm,"secret".getBytes("UTF-8")
                               //TextCodec.BASE64.decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")
                       )
                       .compact();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else {
            jws = Jwts.builder()
                    .setSubject("authenticationToken")
                    .setId("id")
                    .signWith(
                            signatureAlgorithm,
                            TextCodec.BASE64.decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")
                    )
                    .compact();
        }

        return jws;
    }
    public static void readJWS(String jws){
        Jwts.parser()
                .setSigningKey(TextCodec.BASE64.decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E="))
                .parseClaimsJws(jws).getBody();
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
