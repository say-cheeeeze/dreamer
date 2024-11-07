import React from "react";
import CommonJs from "@lib/common";
import { useRouter } from "next/navigation";

export default function CustomLink( prop ) {
	
	const router = useRouter();
	
	const toPage = ( url ) => {
		
		const token = localStorage.getItem( "authToken" );
		
		if ( '/' === url ) {
			router.push( url );
		}
		else if ( CommonJs.isEmpty( token ) ) {
			location.href = '/login';
		}
	}
	
	return <span className='navi-span' onClick={ () => toPage( prop.url ) }>{prop.text}</span>
}