package com.cheeeeze.bootjpa1.web.userauth.service;

import java.util.Optional;

import com.cheeeeze.bootjpa1.web.autority.SecurityUtil;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherInfo;
import com.cheeeeze.bootjpa1.web.userauth.domain.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional( readOnly = true )
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserResponseDTO getUserInfoBySecurity() {
		Optional<TeacherInfo> byLoginId = userRepository.findByLoginId( SecurityUtil.getCurrentUserId() );
		if ( byLoginId.isEmpty() ) {
			throw new RuntimeException( "로그인 유저 정보가 없습니다" );
		}
		return UserResponseDTO.fromInfo( byLoginId.get() );
	}
//	@Transactional
//	public MemberResponseDto changeMemberNickname( String email, String nickname ) {
//		Member member = memberRepository.findByEmail( email )
//										.orElseThrow( () -> new RuntimeException( "로그인 유저 정보가 없습니다" ) );
//		member.setNickname( nickname );
//		return MemberResponseDto.of( memberRepository.save( member ) );
//	}
//
//	@Transactional
//	public MemberResponseDto changeMemberPassword( String email, String exPassword, String newPassword ) {
//		Member member = memberRepository.findById( SecurityUtil.getCurrentMemberId() )
//										.orElseThrow( () -> new RuntimeException( "로그인 유저 정보가 없습니다" ) );
//		if ( !passwordEncoder.matches( exPassword, member.getPassword() ) ) {
//			throw new RuntimeException( "비밀번호가 맞지 않습니다" );
//		}
//		member.setPassword( passwordEncoder.encode( (newPassword) ) );
//		return MemberResponseDto.of( memberRepository.save( member ) );
}
