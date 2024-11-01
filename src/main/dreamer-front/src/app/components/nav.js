'use client'
import React, { useEffect, useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import CommonJs from "@lib/common";

export default function DreamerNav() {
	
	const router = useRouter();
	const [ isLogin, setIsLogin ] = useState( false );
	
	
	const logout = () => {
		localStorage.removeItem( 'authToken' );
		localStorage.removeItem( 'userId' );
		location.href = '/';
	}
	
	useEffect( () => {
		
		let authToken = localStorage.getItem( 'authToken' );
		let userId = localStorage.getItem( 'userId' );
		console.log( authToken );
		console.log( userId );
		
		if ( CommonJs.isNotEmpty( authToken ) && CommonJs.isNotEmpty( userId ) ) {
			setIsLogin( true );
		}
		
	}, [] );
	
	return (
		<>
			<div className={ "navi-wrapper-div" }>
				<div>
					<Link href="/" scroll={ false }>Home</Link>
				</div>
				<div>
					<Link href="/remnant" scroll={ false }>Remnant</Link>
				</div>
				<div>
					<Link href="/teacher" scroll={ false }>Teacher</Link>
				</div>
				<div className={ "login-wrapper" }>
					{ isLogin ?
						<span onClick={ logout }>로그아웃</span>
						:
						<span onClick={ () => router.push( '/login' ) }>로그인</span>
					}
				</div>
			</div>
		</>
	)
}
