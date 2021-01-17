package org.wso2.micro.gateway.enforcer.external.auth.websocket;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.uuid.Generators;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;

public class TokenService {

    private static final String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";

    public static String createJWT(){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        UUID id = Generators.randomBasedGenerator().generate();
        JwtBuilder builder = Jwts.builder().setId(id.toString())
                                            .setIssuedAt(now)
                                            .setSubject("websocketDemo")
                                            .setIssuer("TokenHandler")
                                            .signWith(signatureAlgorithm, signingKey);
        long expMillis = nowMillis + 1000 * 60 * 24;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        return builder.compact();
    }

    public static Claims decodeJWT(String jwt){
        return Jwts.parser().setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                                        .parseClaimsJws(jwt).getBody();
    }
}
