'use client'
import React, { useEffect, useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import CommonJs from "@lib/common";
import CustomLink from "@/app/components/customLink";

export default function DreamerNav() {
	
	const router = useRouter();
	const [ isLogin, setIsLogin ] = useState( false );
	
	const logout = () => {
		localStorage.removeItem( 'authToken' );
		localStorage.removeItem( 'userId' );
		router.push( '/' );
	}
	
	useEffect( () => {
		
		let authToken = localStorage.getItem( 'authToken' );
		let userId = localStorage.getItem( 'userId' );
		
		if ( CommonJs.isNotEmpty( authToken ) && CommonJs.isNotEmpty( userId ) ) {
			setIsLogin( true );
		}
		
	}, [] );
	
	return (
		<>
			<div className={ "navi-wrapper-div" }>
				<div>
					<CustomLink url='/' text='Home'/>
				</div>
				<div>
					<CustomLink url='/remnant' text='Remnant'/>
				</div>
				<div>
					<CustomLink url='/teacher' text='Teacher'/>
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
