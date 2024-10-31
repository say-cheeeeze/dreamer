package com.cheeeeze.bootjpa1.web.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description :Json Web Token 유틸.
 * 사용자 아이디로 토큰을 생성하고
 * 해당 아이디에 맞는 토큰 유효성을 검증한다.
 *
 * @Date        : 2024. 10. 31.
**/
@Slf4j
@Component
public class JWTUtil {
	
	@Value( "${spring.application.name}" )
	private String applicationName;
	
	@Value( "${service.jwt.expiration}" )
	private Long expiration;
	
	private final SecretKey secretKey;
	
	public JWTUtil( @Value( "${service.jwt.token-source}" ) String tokenSource ) {
		this.secretKey = Keys.hmacShaKeyFor( tokenSource.getBytes( StandardCharsets.UTF_8 ) );
	}
	
	public String generateToken( String userId ) {
		
		return Jwts.builder()
				   .header()
				   .keyId( "dreamer" )
				   .and()
				   .subject( userId )
				   .issuer( applicationName )
				   .issuedAt( new Date() )
				   .expiration( new Date( System.currentTimeMillis() + expiration ) )
				   .signWith( secretKey )
				   .compact();
	}
	
	public boolean validateToken( String token, String userId ) {
		
		try {
			JwtParser jwtParser = Jwts.parser().verifyWith( secretKey ).build();
			Jws<Claims> claimsJws = jwtParser.parseSignedClaims( token );
			
			// 추가로 Issuer 가 다른 경우에도 예외처리해주면 될 것 같다.
			Claims payload = claimsJws.getPayload();
			String issuer = payload.getIssuer();
			if ( !issuer.equals( applicationName ) ) {
				throw new RuntimeException( "발급처가 유효하지 않습니다" );
			}
			
			if ( !userId.equals( payload.getSubject() ) ) {
				throw new RuntimeException( "사용자가 일치하지 않습니다" );
			}
		}
		catch( MalformedJwtException e ) {
			log.error( e.getMessage() );
			throw new RuntimeException( "토큰이 유효하지 않습니다" );
		}
		catch( SignatureException e ) {
			log.error( e.getMessage() );
			throw new RuntimeException( "잘못된 Key 입니다" );
		}
		catch( ExpiredJwtException e ) {
			log.error( e.getMessage() );
			throw new RuntimeException( "만료된 토큰입니다" );
		}
		
		return true;
	}
	
}
