'use client'
import { useRouter } from 'next/navigation';

export default function NotFoundPage() {
	const router = useRouter();

	return (
		<div style={{
			display: 'flex',
			flexDirection: 'column',
			alignItems: 'center',
			justifyContent: 'center',
			height: '100vh',
			textAlign: 'center'
		}}>
			<h1 style={{ fontSize: '2rem', marginBottom: '1rem' }}>페이지를 찾을 수 없습니다.</h1>
			<p style={{ fontSize: '1rem', color: '#666', marginBottom: '2rem' }}>요청하신 페이지가 존재하지 않습니다.</p>
			<button
				onClick={() => router.back()}
				style={{
					padding: '0.5rem 1.5rem',
					backgroundColor: '#0070f3',
					color: 'white',
					border: 'none',
					borderRadius: '4px',
					cursor: 'pointer'
				}}
			>
				뒤로가기
			</button>
		</div>
	);
}
