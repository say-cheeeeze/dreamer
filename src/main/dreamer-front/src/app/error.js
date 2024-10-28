'use client' // Error boundaries must be Client Components

import { useEffect } from 'react'
import { Button } from "react-bootstrap";

export default function Error( { error, reset } ) {
	useEffect( () => {
		// Log the error to an error reporting service
		console.error( error )
	}, [ error ] )
	
	return (
		<div className={"container"}>
			<h2>오류가 발생했습니다</h2>
			<Button onClick={()=> reset()}>
				재시도
			</Button>
		</div>
	)
}