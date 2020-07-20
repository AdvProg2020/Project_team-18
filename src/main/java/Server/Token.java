package Server;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

//import javax.xml.bind.DatatypeConverter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static javax.crypto.Cipher.SECRET_KEY;

public class Token {
    private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    public static String createJWS(long validPeriod){
        String jws = null;
        if (validPeriod>0){
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
            long expMillis = nowMillis + validPeriod;
           Date exp = new Date(expMillis);

         jws = Jwts.builder()
                .setSubject("authenticationToken")
                .setId("id")
                .setExpiration(exp)
                .signWith(
                        signatureAlgorithm,
                        TextCodec.BASE64.decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")
                )
                .compact();}
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
}
