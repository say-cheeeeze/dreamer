'use client'
import Container from "react-bootstrap/Container";
import MyButton from "@/app/components/MyButton";
import RemnantForm from "@/app/remnant/info/remnantForm";
import { useSearchParams } from "next/navigation";
import { useRouter } from "next/navigation";
import { Button, Card } from "react-bootstrap";
import CommonJs from "@lib/common";
import Image from 'react-bootstrap/Image';
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

export default function page() {
	
	const router = useRouter();
	let searchParams = useSearchParams();
	let _id = parseInt( searchParams.get( "id" ) );
	let _mode = searchParams.get( "mode" );
	const isViewMode = _mode === 'view';
	const isUpdateMode = _mode === 'update';
	const isInsertMode = _mode === 'insert';
	
	function onClickDeleteBtn() {
		console.log( _id );
		if ( CommonJs.isEmpty( _id ) ) {
			return;
		}
		
		if ( !confirm( "정말 삭제하시겠습니까?" ) ) {
			return;
		}
		
		let url = '/api/remnant/delete';
		let requestObj = {
			headers : {
				'content-Type' : 'application/json',
			},
			method  : 'post',
			body    : JSON.stringify({ id : _id } )
		};
		
		fetch( url, requestObj ).then( ( response ) => {
			
			response.json().then( data => {
				
				console.log( data );
				
				if ( 200 === data.status ) {
					alert( "정상적으로 삭제하였습니다." );
					router.push( '/remnant' );
				}
				else {
					alert( "오류가 발생했습니다" );
					location.reload();
				}
			} );
			
		} ).catch( e => {
			alert( "오류가 발생했습니다" );
			location.reload();
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
										        onClick={() => onClickDeleteBtn() }
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
				<div>
					<RemnantForm mode={ _mode }/>
					{
						isViewMode &&
						<>
							<Container>
								<Row>
									<Col xs={ 6 } md={ 4 }>
										<Image src="/user1.png" width={100} height={100} rounded/>
									</Col>
								</Row>
							</Container>
						</>
					}
				</div>
			</Container>
		</>
	)
}