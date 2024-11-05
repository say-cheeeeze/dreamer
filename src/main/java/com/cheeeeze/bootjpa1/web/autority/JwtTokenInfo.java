package com.cheeeeze.bootjpa1.web.autority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokenInfo {
	private String grantType;
	private String accessToken;
	private Long tokenExpiresIn;
}
