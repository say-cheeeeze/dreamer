'use client'
import '@/app/css/teacher-regist.css';
import { Button, Form } from "react-bootstrap";
import { useState } from "react";
import { redirect, useRouter } from "next/navigation";
import Link from "next/link";
import axios from "axios";
import { StatusCodes } from 'http-status-codes';

/**
 * 신규 교사 등록 화면
 */
export default function TeacherRegist() {
	
	const router = useRouter();
	const LENGTH_ID = 6;
	const LENGTH_NAME = 1;
	const LENGTH_PASSWORD = 8;
	const [ formData, setFormData ] = useState( {
		id         : '',
		name       : '',
		pwd        : '',
		pwdConfirm : '',
		phone      : '',
		email      : '',
		valid      : {
			id         : false,
			name       : false,
			pwd        : false,
			pwdConfirm : false,
			phone      : true,
			email      : true,
		}
	} )
	
	const onTeacherRegistration = ( event ) => {
		
		event.preventDefault();
		
		if ( !formValidation() ) {
			alert( "필수 입력 항목(*)을 확인해주세요." );
			return;
		}
		
		requestSignupAPI();
	}
	
	// 회원가입 요청
	function requestSignupAPI() {
		
		const URL_SIGNUP = '/auth/signup';
		const teacherDTO = {
			loginId  : formData.id,
			name     : formData.name,
			password : formData.pwd,
			phone    : formData.phone,
			email    : formData.email
		}
		
		axios.post( URL_SIGNUP, teacherDTO ).then( res => {
			
			console.log( res );
			
			if ( StatusCodes.CONFLICT === res.data.status ) {
				return alert( res.data.msg );
			}
			
			if ( StatusCodes.OK === res.data.status ) {
				localStorage.setItem( "authToken", res.data.token.accessToken );
				localStorage.setItem( "userId", res.data.user.loginId );
				router.push( '/regist/welcome', { scroll : false } );
			}
			
		} ).catch( e => {
			console.log( e );
			alert( "오류가 발생했습니다" );
			
		} ).finally( () => {
		} );
	}
	
	const formValidation = () => {
		
		// 객체의 특정 value 들을 모두 검사한다.
		// every 의 첫번째 파라미터인 function 에 Boolean 이 들어가는데,
		// 생성자로 만들어진 Boolean 객체는 기본값 false 이지만,
		// 조건식에 쓰여져 객체로 평가될 때에는 true 로 간주된다!
		
		// 즉 모든 값들이 true 로 간주될 때 반환값이 true 가 된다.
		const val = Object.values( formData.valid ).every( Boolean );
		return val;
	}
	
	const onChangeForm = ( e ) => {
		const { name, value } = e.target;
		
		if ( name === 'id' ) {
			const regExp = /^[a-zA-Z0-9]*$/;
			let regTest = regExp.test( value );
			if ( false === regTest ) {
				return;
			}
		}
		
		if ( name === 'phone' ) {
			const regExp = /^[0-9]*$/;
			let regTest = regExp.test( value );
			if ( false === regTest ) {
				return;
			}
		}
		
		setFormData( ( prev ) => ( {
			...prev,
			[ name ] : value,
			valid    : {
				...prev.valid,
				[ name ] : validateForm( name, value )
			}
		} ) );
	};
	
	const validateForm = ( name, value ) => {
		switch ( name ) {
			case 'id' :
				return value.length >= LENGTH_ID;
			case 'name' :
				return value.length >= LENGTH_NAME;
			case 'pwd' :
				return value.length >= LENGTH_PASSWORD;
			case 'pwdConfirm' :
				return ( formData.pwd === value ) && ( formData.valid.pwd === true );
			case 'phone' :
				return true;
			case 'email' :
				return true;
			default :
				return false;
		}
	};
	
	return <>
		<div className={ 'teacher-regist-form-wrapper bg-img' }>
			<div className={ 'form-wrapper' }>
				<h3 className={ 'header' }>교사 등록</h3>
				<div className={ 'mb-2 text-align-right'}>
					<span className={ 'color-red'}>*</span>
					<span>표시는 필수 항목입니다.</span>
				</div>
				<Form noValidate onSubmit={ onTeacherRegistration }>
					<Form.Group className="input-container" controlId="name">
						<div className={ "left-container" }>
							<Form.Label className={ "form-label" }>
								<span>이름</span>
								<span className={ "color-red" }>*</span>
							</Form.Label>
						</div>
						<div className={ "right-container" }>
							<Form.Control required
							              type="text"
							              name="name"
							              maxLength={ 10 }
							              value={ formData.name }
							              onChange={ onChangeForm }
							              isValid={ formData.valid.name }
							/>
						</div>
					</Form.Group>
					<Form.Group className="input-container" controlId="id">
						<div className={ "left-container" }>
							<Form.Label className={ "form-label" }>
								<span>로그인에 사용할 ID</span>
								<span className={ "color-red" }>*</span>
							</Form.Label>
						</div>
						<div className={ "right-container" }>
							<Form.Control required
							              type="text"
							              name="id"
							              maxLength={ 20 }
							              value={ formData.id }
							              onChange={ onChangeForm }
							              isValid={ formData.valid.id }
							/>
							<div className={ "text-align-left" }>
								<Form.Text muted>{ LENGTH_ID }자 이상 영어와 숫자 가능</Form.Text>
							</div>
						</div>
					</Form.Group>
					<Form.Group className="input-container" controlId="pwd">
						<div className={ "left-container" }>
							<Form.Label className={ "form-label" }>
								<span>비밀번호</span>
								<span className={ "color-red" }>*</span>
							</Form.Label>
						</div>
						<div className={ "right-container" }>
							<Form.Control required
							              type="password"
							              name="pwd"
							              maxLength={ 20 }
							              value={ formData.pwd }
							              onChange={ onChangeForm }
							              isValid={ formData.valid.pwd }
							              autoComplete={ "off" }
							/>
							<div className={ "text-align-left" }>
								<Form.Text muted>{ LENGTH_PASSWORD }자 이상</Form.Text>
							</div>
						</div>
					</Form.Group>
					<Form.Group className="input-container" controlId="pwd2">
						<div className={ "left-container" }>
							<Form.Label className={ "form-label" }>
								<span>비밀번호 확인</span>
								<span className={ "color-red" }>*</span>
							</Form.Label>
						</div>
						<div className={ "right-container" }>
							<Form.Control required
							              type="password"
							              name="pwdConfirm"
							              maxLength={ 20 }
							              value={ formData.pwdConfirm }
							              onChange={ onChangeForm }
							              isValid={ formData.valid.pwdConfirm }
							              autoComplete={ "off" }
							/>
						</div>
					</Form.Group>
					<Form.Group className="input-container" controlId="phone">
						<div className={ "left-container" }>
							<Form.Label className={ "form-label" }>
								<span>연락처</span>
							</Form.Label>
						</div>
						<div className={ "right-container" }>
							<Form.Control type="text"
							              name="phone"
							              maxLength={ 11 }
							              value={ formData.phone }
							              onChange={ onChangeForm }
							/>
							<div className={ "text-align-left" }>
								<Form.Text muted>숫자만</Form.Text>
							</div>
						</div>
					</Form.Group>
					<Form.Group className="input-container" controlId="email">
						<div className={ "left-container" }>
							<Form.Label className={ "form-label" }>
								<span>email</span>
							</Form.Label>
						</div>
						<div className={ "right-container" }>
							<Form.Control type="text"
							              name="email"
							              maxLength={ 30 }
							              value={ formData.email }
							              onChange={ onChangeForm }
							/>
						</div>
					</Form.Group>
					<div className={ "submit-area" }>
						<Button type={ "submit" }>등록</Button>
					</div>
				</Form>
				<div className={ 'mt-10' }>
					<Link href={ "/" } scroll={false}>홈으로</Link>
				</div>
			</div>
		</div>
	</>;
}