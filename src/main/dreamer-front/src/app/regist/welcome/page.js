'use client'
import '@/app/css/teacher-regist.css';
import { useRouter } from "next/navigation";
import Link from "next/link";
import axios from "axios";
import { useEffect, useState } from "react";
import Loading from "@/app/loading";

export default function TeacherWelcomePage() {
	
	const router = useRouter();
	const [ loading, setLoading ] = useState( true );
	
	function validateToken() {
		axios.get('/api/validate-token', {
			headers: {
				'Authorization': `Bearer ${localStorage.getItem('authToken')}`
			},
			params : {
				'userId' : localStorage.getItem( "userId" )
			}
			
		}).then(response => {
			if ( !response.data.isValid ) {
				goHome();
			}
			else {
				setLoading( false );
			}
			
		}).catch(()=> {
			goHome();
		});
	}
	
	function goHome() {
		router.push( "/", { scroll : false } );
	}
	
	useEffect( () => {
		validateToken();
	}, [] );
	
	if ( loading ) {
		return <Loading/>
	}
	else {
		return (
			<div className={ 'teacher-regist-form-wrapper bg-img' }>
				<div className={ 'form-wrapper' }>
					<h3 className={ 'header' }>등록 완료</h3>
					<div className={ 'text-align-left' }>
						<p>환영합니다.</p>
						<p>렘넌트 메뉴에서 렘넌트를 관리해주세요.</p>
						<p>본인의 정보를 수정하려면 마이페이지에서 가능합니다.</p>
					</div>
					<div className={ 'mt-3 fw-bold color-cornblue' }>
						<Link href={ "/" } scroll={false}>홈으로</Link>
					</div>
				</div>
			</div>
		)
	}
}