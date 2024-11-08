import React from "react";
import CommonJs from "@lib/common";
import { useRouter } from "next/navigation";

export default function CustomLink( prop ) {
	
	const router = useRouter();
	
	const toPage = ( url ) => {
		
		const token = localStorage.getItem( "authToken" );
		
		if ( CommonJs.isEmpty( token ) ) {
			location.href = '/login';
			return;
		}
		
		router.push( url );
		
	}
	
	return <span className='navi-span' onClick={ () => toPage( prop.url ) }>{prop.text}</span>
}