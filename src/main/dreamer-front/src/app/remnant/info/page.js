'use client'
import Container from "react-bootstrap/Container";
import MyButton from "@/app/components/MyButton";
import RemnantForm from "@/app/remnant/info/remnantForm";
import { redirect, useSearchParams } from "next/navigation";
import { useRouter } from "next/navigation";
import { Button, Card } from "react-bootstrap";
import CommonJs from "@lib/common";
import axios from "axios";

export default function page() {
	
	const router = useRouter();
	let searchParams = useSearchParams();
	let _id = parseInt( searchParams.get( "id" ) );
	let _mode = searchParams.get( "mode" );
	const isViewMode = _mode === 'view';
	const isUpdateMode = _mode === 'update';
	const isInsertMode = _mode === 'insert';
	
	function onClickDeleteBtn() {
		if ( CommonJs.isEmpty( _id ) ) {
			return;
		}
		if ( !confirm( "정말 삭제하시겠습니까?" ) ) {
			return;
		}
		let url = '/api/remnant/delete';
		let param = {
			id : _id
		}
		axios.post( url, param ).then( res => {
			
			if ( 200 === res.data.status ) {
				// current page will not save session in history.
				// so user can't navigate previous page with back button.
				location.replace( '/remnant' );
			}
		} ).catch( e => {
			console.error( e );
			alert( "오류가 발생했습니다." );
			location.reload();
		} ).finally( () => {
		} );
	}
	
	return (
		<>
			<Container>
				<div className={ 'container-header' }>
					<div className={ "d-inline-block" }>
						<h3>{ isInsertMode ? 'Remnant 신규'
							: isViewMode ? 'Remnant 정보' : 'Remnant 수정' }</h3>
					</div>
					<div className={ "d-inline-block float-right" }>
						{
							isViewMode &&
							(
								<>
									<div className={ "d-inline-block mr-5px" }>
										<Button variant={ "outline-primary" }
										        onClick={ () => onClickDeleteBtn() }
										>삭제</Button>
									</div>
									<div className={ "d-inline-block mr-5px" }>
										<MyButton link={ '/remnant/info?id=' + _id + '&mode=update' }
										          color="outline-primary"
										          text="수정"
										/>
									</div>
								</>
							)
						}
						<Button onClick={ () => router.back() }
						        variant={ "outline-primary" }>이전
						</Button>
					</div>
				</div>
				<RemnantForm mode={ _mode }/>
			</Container>
		</>
	)
}