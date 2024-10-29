'use client'
import '@/app/css/login.css';
import { Button, Form } from "react-bootstrap";
import { useState } from "react";
import Link from "next/link";

export default function Login() {
	const [ validated, setValidated ] = useState( false );
	
	const handleSubmit = ( event ) => {
		const form = event.currentTarget;
		
		if ( !form.checkValidity() ) {
			event.preventDefault();
			event.stopPropagation();
		}
		setValidated( true );
		
		console.log( "폼 전송" );
		
		// event 막고 axios 하면 될것같다.
	}
	
	return <>
		<div className={ 'login-form-wrapper bg-img' }>
			<div className={ 'form-wrapper' }>
				<h3 className={ 'header' }>로그인</h3>
				<Form noValidate validated={ validated } onSubmit={ handleSubmit }>
					<Form.Group className="input-container" controlId="id">
						<Form.Label className={ "form-label" }>ID</Form.Label>
						<Form.Control className={ 'form-control' } required type="text" placeholder=""/>
					</Form.Group>
					<Form.Group className="input-container" controlId="pwd">
						<Form.Label>Password</Form.Label>
						<Form.Control required type="password"/>
					</Form.Group>
					<Button type={ "submit" }>로그인</Button>
				</Form>
				<div className={'mt-3'}>
					<Link href={ "/regist/teacher" }>교사등록</Link>
					<Link className={'ml-1'} href={ "/" }>홈으로</Link>
				</div>
			</div>
		</div>
	</>;
}