'use client'
import { Spinner } from "react-bootstrap";

export default function Loading() {
	
	const styles = {
		loadingSpinner : {
			position        : 'fixed',
			top             : 0,
			left            : 0,
			width           : '100%',
			height          : '100%',
			display         : 'flex',
			flexDirection   : 'column',
			alignItems      : 'center',
			justifyContent  : 'center',
			backgroundColor : 'rgba(255, 255, 255, 0.8)', // 반투명 배경
			zIndex          : 1000 // 다른 요소 위에 표시
		},
		spinner        : {
			width  : '3rem',
			height : '3rem',
			color: 'cornflowerblue'
		},
	};
	
	return (
		
		<div style={ styles.loadingSpinner }>
			<Spinner animation="border" role="status" style={ styles.spinner }/>
		</div>
	)
}