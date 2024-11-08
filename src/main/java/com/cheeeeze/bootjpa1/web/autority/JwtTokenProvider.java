package com.cheeeeze.bootjpa1.web.autority;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
public class JwtTokenProvider {
	
	@Value( "${spring.application.name}" )
	private String applicationName;
	
	@Value( "${service.jwt.expiration}" )
	private Long expiration;
	
	private final SecretKey SECRET_KEY;
	private final String AUTH_KEY_NAME = "auth";
	
	public JwtTokenProvider( @Value( "${service.jwt.token-source}" ) String tokenSource ) {
		this.SECRET_KEY = Keys.hmacShaKeyFor( tokenSource.getBytes( StandardCharsets.UTF_8 ) );
	}
	
	public JwtTokenInfo generateToken( Authentication authentication ) {
		String authorities = authentication.getAuthorities().stream()
										   .map( GrantedAuthority::getAuthority )
										   .collect( Collectors.joining( "," ) );
		
		Date expDate = new Date( System.currentTimeMillis() + expiration );
		
		String accessToken = Jwts.builder()
								 .subject( authentication.getName() )
								 .claim( AUTH_KEY_NAME, authorities )
								 .issuedAt( new Date() )
								 .issuer( applicationName )
								 .expiration( expDate )
								 .signWith( SECRET_KEY )
								 .compact();
		
		log.info( "accessToken...{}", accessToken );
		
		return JwtTokenInfo.builder()
						   .grantType( "bearer" )
						   .accessToken( accessToken )
						   .tokenExpiresIn( expDate.getTime() )
						   .build();
	}
	
	public Authentication getAuthentication( String token ) {
		
		Claims claims = parseClaims( token );
		
		if ( claims.get( AUTH_KEY_NAME ) == null ) {
			throw new RuntimeException( "권한 정보가 없는 토큰입니다." );
		}
		
		List<SimpleGrantedAuthority> authList = Arrays.stream( claims.get( AUTH_KEY_NAME ).toString().split( "," ) )
													  .map( SimpleGrantedAuthority::new )
													  .toList();
		
		UserDetails userDetails = new User( claims.getSubject(), "", authList );
		
		return new UsernamePasswordAuthenticationToken( userDetails, "", userDetails.getAuthorities() );
	}
	
	public Claims parseClaims( String token ) {
		
		try {
			return Jwts.parser()
					   .verifyWith( SECRET_KEY )
					   .build()
					   .parseSignedClaims( token )
					   .getPayload();
		}
		catch ( ExpiredJwtException e ) {
			log.error( e.getMessage() );
			return e.getClaims();
		}
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
				   .signWith( SECRET_KEY )
				   .compact();
	}
	
	public boolean validateToken( String token ) {
		try {
			Jwts.parser()
				.verifyWith( SECRET_KEY )
				.build()
				.parseSignedClaims( token );
			return true;
		}
		catch ( MalformedJwtException e ) {
			log.error( "유효하지 않은 서명입니다." );
		}
		catch ( SignatureException e ) {
			log.error( "잘못된 Key 입니다" );
		}
		catch ( ExpiredJwtException e ) {
			log.error( "만료된 토큰입니다" );
		}
		
		return false;
	}
	
	public boolean validateToken( String token, String userId ) {
		
		try {
			JwtParser jwtParser = Jwts.parser().verifyWith( SECRET_KEY ).build();
			Jws<Claims> claimsJws = jwtParser.parseSignedClaims( token );
			
			// 추가로 Issuer 가 다른 경우에도 예외처리해주면 될 것 같다.
			Claims payload = claimsJws.getPayload();
			
			Date expiration = payload.getExpiration();
			long remainingTimeMillis = expiration.getTime() - System.currentTimeMillis();
			long remainingTimeMinutes = remainingTimeMillis / (1000 * 60);
			log.info( "remainingTimeMinutes : {} minutes ", remainingTimeMinutes  );
			
			
			String issuer = payload.getIssuer();
			if ( !issuer.equals( applicationName ) ) {
				throw new RuntimeException( "발급처가 유효하지 않습니다" );
			}
			
			if ( !userId.equals( payload.getSubject() ) ) {
				throw new RuntimeException( "사용자가 일치하지 않습니다" );
			}
		}
		catch ( MalformedJwtException e ) {
			log.error( e.getMessage() );
			throw new RuntimeException( "토큰이 유효하지 않습니다" );
		}
		catch ( SignatureException e ) {
			log.error( e.getMessage() );
			throw new RuntimeException( "잘못된 Key 입니다" );
		}
		catch ( ExpiredJwtException e ) {
			log.error( e.getMessage() );
			throw new RuntimeException( "만료된 토큰입니다" );
		}
		
		return true;
	}
	
}
