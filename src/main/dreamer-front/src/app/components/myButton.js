'use client'
import { Button } from "react-bootstrap";

export default function MyButton({ link, color, text }){
	
	return (
		<a href={ link }>
			<Button variant={ color }>{text}</Button>
		</a>
	)
}