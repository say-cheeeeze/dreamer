package com.cheeeeze.bootjpa1.web.authority;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

import com.cheeeeze.bootjpa1.web.autority.JwtTokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles( "test" )
class JwtTokenProviderTest {
	
	@Value( "${service.jwt.token-source}" )
	String tokenSource;
	
	@Value( "${spring.application.name}" )
	String applicationName;
	
	@Value( "${service.jwt.expiration}" )
	Long expiration;
	
	@Test
	void createToken_TEST() {
		
		// secretKey format :: https://github.com/jwtk/jjwt?tab=readme-ov-file#secretkey-formats
		SecretKey secretKey = Keys.hmacShaKeyFor( tokenSource.getBytes( StandardCharsets.UTF_8 ) );
		
		String jwt = Jwts.builder()                     // (1)
						 .header()                                   // (2) optional
						 .keyId( "aKeyId" )
						 .and()
						 .subject( "nyj9349" )                             // (3) JSON Claims, or
						 .issuer( applicationName )
						 .issuedAt( new Date() )
						 .expiration( new Date( System.currentTimeMillis() + expiration ) )// 10min
						 // .content(aByteArray, "text/plain")        //     any byte[] content, with media type
						 .signWith( secretKey )                       // (4) if signing, or
						 // .encryptWith( signingKey, Jwts.SIG.HS512, )  //     if encrypting
						 .compact();                                 // (5)
		System.out.println( "jwt = " + jwt );
		
	}
	
	@Test
	void illegalToken_TEST() {
		
		String token1 = generateToken( "nyj9349" );
		
		String illegalValue = "somethingsomethingsomethingsomethingsomething";
		SecretKey illegalKey = Keys.hmacShaKeyFor( illegalValue.getBytes( StandardCharsets.UTF_8 ) );
		
		JwtParserBuilder parser = Jwts.parser();
		
		JwtParser build = parser.verifyWith( illegalKey ).build();
		
		Assertions.assertThrows( SignatureException.class, () -> build.parseSignedClaims( token1 ) );
		
	}
	@Test
	void expiredToken_TEST() {
		
		String token1 = generateExpiredToken( "nyj9349" );
		
		SecretKey secretKey = Keys.hmacShaKeyFor( tokenSource.getBytes( StandardCharsets.UTF_8 ) );
		
		JwtParserBuilder parser = Jwts.parser();
		
		JwtParser build = parser.verifyWith( secretKey ).build();
		Assertions.assertThrows( ExpiredJwtException.class, () -> build.parseSignedClaims( token1 ) );
		
	}
	
	@Test
	@DisplayName( "토큰 검증하기" )
	void validate_Token_TEST() {
		
		String token1 = generateToken( "nyj9349" );
		
		SecretKey secretKey = Keys.hmacShaKeyFor( tokenSource.getBytes( StandardCharsets.UTF_8 ) );
		
		JwtParserBuilder parser = Jwts.parser();
		
		JwtParser build = parser.verifyWith( secretKey ).build();
		Jws<Claims> claimsJws = build.parseSignedClaims( token1 );
		Claims payload = claimsJws.getPayload();
		Date expiration1 = payload.getExpiration();
		String issuer = payload.getIssuer();
		Date issuedAt = payload.getIssuedAt();
		System.out.println( "issuedAt = " + issuedAt );
		System.out.println( "issuer = " + issuer );
		System.out.println( "expiration1 = " + expiration1 );
		
		Assertions.assertEquals( issuer, applicationName );
		Assertions.assertEquals( issuedAt, payload.getIssuedAt() );
		Assertions.assertEquals( expiration1, payload.getExpiration() );
		
	}
	
	@Test
	@DisplayName( "토큰 검증2" )
	void validate_Token_TEST2() {
		
		// given
		String token = "eyJraWQiOiJkcmVhbWVyIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJ5b29uamFlIiwiaXNzIjoiYm9vdGpwYTEiLCJpYXQiOjE3MzAzNDY4NjEsImV4cCI6MTczMDM0NzQ2MX0.4_apu48ZFEsW3t8lLQB0e-lCRQifyGlp1UEsUgFgKWI";
		
		//when
		SecretKey secretKey = Keys.hmacShaKeyFor( tokenSource.getBytes( StandardCharsets.UTF_8 ) );
//		SecretKey secretKey = Keys.hmacShaKeyFor( "secretKeysecretKeysecretKeysecretKey".getBytes( StandardCharsets.UTF_8 ) );
		// key 의 길이가 너무 짧으면 여기서 io.jsonwebtoken.security.WeakKeyException
		
		JwtParserBuilder parser = Jwts.parser();
		JwtParserBuilder jwtParserBuilder = parser.verifyWith( secretKey );
		JwtParser build = jwtParserBuilder.build();
		Jws<Claims> claimsJws = build.parseSignedClaims( token ); // 잘못된 시크릿 키라면 여기서 에러가 난다.
		// SignatureException : 잘못된 key 인 경우
		// ExpiredJwtException : 만료된 경우
		
		// 추가로 Issuer 가 다른 경우에도 예외처리해주면 될 것 같다.
		Claims payload = claimsJws.getPayload();
		Date expiration1 = payload.getExpiration();
		Date issuedAt = payload.getIssuedAt();
		String issuer = payload.getIssuer();
		System.out.println( "issuer = " + issuer );
		System.out.println( "issuedAt = " + issuedAt );
		System.out.println( "expiration1 = " + expiration1 );
	}
	
	
	public String generateToken( String userId ) {
		
		SecretKey secretKey = Keys.hmacShaKeyFor( tokenSource.getBytes( StandardCharsets.UTF_8 ) );
		
		String jwt = Jwts.builder()
						 .header()
						 .keyId( "dreamer" )
						 .and()
						 .subject( userId )
						 .issuer( applicationName )
						 .issuedAt( new Date() )
						 .expiration( new Date( System.currentTimeMillis() + expiration ) )
						 .signWith( secretKey )
						 .compact();
		
		return jwt;
	}
	
	public String generateExpiredToken( String userId ) {
		
		SecretKey secretKey = Keys.hmacShaKeyFor( tokenSource.getBytes( StandardCharsets.UTF_8 ) );
		
		return Jwts.builder()
				   .header()
				   .keyId( "dreamer" )
				   .and()
				   .subject( userId )
				   .issuer( applicationName )
				   .issuedAt( new Date( System.currentTimeMillis() - expiration ) )
				   .expiration( new Date( System.currentTimeMillis() - 1000 * 60 * 5 ) )
				   .signWith( secretKey )
				   .compact();
	}
}