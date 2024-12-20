'use client'
import '@/app/css/remnant.css';
import Container from "react-bootstrap/Container";
import MyButton from "@/app/components/MyButton";
import RemnantForm from "@/app/remnant/info/remnantForm";
import { useRouter, useSearchParams } from "next/navigation";
import { Button } from "react-bootstrap";
import CommonJs from "@lib/common";
import axios from "axios";
import { StatusCodes } from "http-status-codes";

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
			
			if ( res.data.status === StatusCodes.OK ) {
				// current page will not save session in history.
				// so user can't navigate previous page with back button.
				location.replace( '/remnant' );
			}
		} ).catch( e => {
			console.log( e );
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
									<div className={ "d-inline-block mr-1" }>
										<Button variant={ "outline-primary" }
										        onClick={ () => onClickDeleteBtn() }
										>삭제</Button>
									</div>
									<div className={ "d-inline-block mr-1" }>
										<Button onClick={ () => router.replace( '/remnant/info?id=' + _id + '&mode=update') }
										        variant={ "outline-primary" }>수정
										</Button>
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