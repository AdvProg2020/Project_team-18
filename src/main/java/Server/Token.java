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
    private boolean disposable;
    private int usageTimes;

    public int getUsageTimes() {
        return usageTimes;
    }

    public void addUsageTimes() {
        this.usageTimes++;
    }

    public void setDisposable(){
        this.disposable = true;
    }
    public Token(long validPeriod){
        this.JWS = createJWS(validPeriod);
    }
    private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private static final String secretCode = "secretCodeFromServer";
  /*  private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
    private static Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
   */
    private static String createJWS(long validPeriod){
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
                               signatureAlgorithm,secretCode.getBytes("UTF-8")
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
                                secretCode.getBytes("UTF-8")
                        )
                        .compact();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return jws;
    }
    public static void readJWS(Token token) throws Exception{
        if (token.disposable){
            if (token.getUsageTimes()>0) {
                throw new Exception("Your disposable token has already been used !");
            }
        }
        try {
            Jwts.parser()
                    .setSigningKey(secretCode.getBytes("UTF-8"))
                    .parseClaimsJws(token.getJWS()).getBody();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        token.addUsageTimes();
    }

    public String getJWS() {
        return JWS;
    }

 /*  public static void main(String[] args){
        Token token = new Token(0);
        token.setDisposable();
        System.out.println(token.getUsageTimes());
       try {
           readJWS(token);
           System.out.println(token.getUsageTimes());
           System.out.println("first time");
           readJWS(token);
           System.out.println(token.getUsageTimes());
           System.out.println("second time");
       } catch (Exception e) {
           System.out.println(e.getMessage());
       }
       System.out.println("finished");
    }*/
}
