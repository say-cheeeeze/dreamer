'use client'
import '@/app/css/login.css';
import { Button, Form } from "react-bootstrap";
import { useState } from "react";
import Link from "next/link";
import CommonJs from "@lib/common";
import axios from "axios";
import { StatusCodes } from "http-status-codes";
import { useRouter } from "next/navigation";
import axiosProvider from "@lib/axiosProvider";

export default function Login() {
	const $axios = axiosProvider();
	
	const [ user, setUser ] = useState( {
		userId   : '',
		password : ''
	} );
	const router = useRouter();
	
	const submitLogin = ( event ) => {
		
		event.preventDefault();
		
		// loginAPI
		if ( CommonJs.isEmpty( user.userId ) ||
			CommonJs.isEmpty( user.password ) ) {
			return;
		}
		
		let param = {
			loginId  : user.userId,
			password : user.password
		}
		
		$axios.post( '/auth/login', param ).then( res => {
			
			if ( StatusCodes.OK === res.status ) {
			  localStorage.setItem( "authToken", res.data.accessToken );
			  localStorage.setItem( "userId", user.userId );
			  router.push( '/' );
			}
			
		} ).catch( e => {
		  
		  if ( StatusCodes.UNAUTHORIZED === e.response.status ) {
		      alert( e.response.data.msg );
		  }
		  else {
		      alert( "오류가 발생했습니다" );
		  }
		} )
		.finally( () => {
		
		} );
	}
	
	const onChangeInputEvent = ( e ) => {
		const { name, value } = e.target;
		
		// id 는 영어,소문자만 입력가능
		if ( name === 'userId' ) {
			const regExp = /^[a-zA-Z0-9]*$/;
			let regTest = regExp.test( value );
			if ( false === regTest ) {
				return;
			}
		}
		setUser( {
			...user,
			[ name ] : value
		} );
	}
	
	return <>
		<div className={ 'login-form-wrapper bg-img' }>
			<div className={ 'form-wrapper' }>
				<h3 className={ 'header' }>로그인</h3>
				<Form noValidate onSubmit={ submitLogin }>
					<Form.Group className="input-container" controlId="id">
						<Form.Label className={ "form-label" }>ID</Form.Label>
						<Form.Control required
						              className={ 'form-control' }
						              type="text"
						              name="userId"
						              value={ user.userId }
						              onChange={ onChangeInputEvent }
						              placeholder=""/>
					</Form.Group>
					<Form.Group className="input-container" controlId="pwd">
						<Form.Label className={ 'form-label' }>Password</Form.Label>
						<Form.Control required
						              className={ 'form-control' }
						              type="password"
						              autoComplete={ 'off' }
						              name="password"
						              value={ user.password }
						              onChange={ onChangeInputEvent }
						/>
					</Form.Group>
					<Button type={ "submit" }>로그인</Button>
				</Form>
				<div className={ 'mt-3' }>
					<Link href={ "/regist/teacher" } scroll={ true }>교사등록</Link>
					<Link className={ 'ml-10' } href={ "/" } scroll={ false }>홈으로</Link>
				</div>
			</div>
		</div>
	</>;
}