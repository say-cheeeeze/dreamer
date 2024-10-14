'use client'
import { Button, Form, Modal, Spinner } from "react-bootstrap";
import { useEffect, useState } from "react";
import CommonJs from "@lib/common";
import { redirect, usePathname, useRouter, useSearchParams } from "next/navigation";

export default function RemnantForm( { mode } ) {
	
	const [ remnantInfo, setRemnantInfo ] = useState( {
		name   : '',
		id     : 0,
		grade  : '',
		gender : ''
	} );
	const router = useRouter();
	const isInsertMode = mode === 'insert';
	const isUpdateMode = mode === 'update';
	const [ modalShow, setModalShow ] = useState( false );
	const [ modalBodyTxt, setModalBodyTxt ] = useState( '' );
	const pathname = usePathname(); // 현재 경로를 가져옴
	const searchParams = useSearchParams(); // next/navigation의 useSearchParams 사용
	
	let minWidth = 'min-width-5rem';
	
	const onChangeName = ( e ) => {
		setRemnantInfo( {
			...remnantInfo,
			name : e.target.value
		} )
	}
	const onChangeGender = ( e ) => {
		setRemnantInfo( {
			...remnantInfo,
			gender : e.target.value
		} );
	}
	const onChangeGrade = ( e ) => {
		setRemnantInfo( {
			...remnantInfo,
			grade : e.target.value
		} );
	}
	const showModal = () => setModalShow( true );
	const closeModal = () => setModalShow( false );
	const [ loading, setLoading ] = useState( false );
	
	useEffect( () => {
		
		let id = searchParams.get( "id" );
		if ( !isInsertMode && null !== id ) {
			setLoading( true );
			getRemnantInfoById( id );
		}
	}, [] );
	
	function getRemnantInfoById( id ) {
		const url = '/api/remnant/get';
		const requestObj = {
			method  : 'POST',
			headers : {
				"Content-Type" : "application/json",
			},
			body    : JSON.stringify( {
				id
			} )
		}
		fetch( url, requestObj ).then( ( response ) => {
			
			response.json().then( data => {
				setRemnantInfo( data.info );
			} );
			
		} ).catch( e => {
			console.error( e );
		} ).finally( () => {
			setLoading( false )
		} );
		
	}
	
	function showModalWithMessage( msg ) {
		setModalBodyTxt( msg );
		showModal();
	}
	
	function onSaveRemnant() {
		
		if ( CommonJs.isEmpty( remnantInfo.name ) ) {
			showModalWithMessage( '이름을 입력해주세요.' );
			return;
		}
		
		if ( CommonJs.isEmpty( remnantInfo.gender ) ) {
			showModalWithMessage( '성별을 선택해주세요.' );
			return;
		}
		if ( CommonJs.isEmpty( remnantInfo.grade ) ) {
			showModalWithMessage( '학년을 선택해주세요.' );
			return;
		}
		
		let url = '/api/remnant/save';
		let requestObj = {
			headers : {
				'content-Type' : 'application/json',
			},
			method  : 'post',
			body    : JSON.stringify( {
				id     : remnantInfo.id,
				name   : remnantInfo.name,
				grade  : remnantInfo.grade,
				gender : remnantInfo.gender
			} )
		};
		
		fetch( url, requestObj ).then( ( response ) => {
			
			response.json().then( data => {
				
				if ( 200 === data.status ) {
					alert( "등록 완료" );
					let url = '/remnant/info?id=' + data.saveInfo.id + '&mode=view';
					router.push( url );
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
			{
				loading ? (
						<div className={ "loadingDivWrapper" }>
							<Spinner animation="border" role="status" variant={ "primary" }>
							</Spinner>
						</div>
					)
					: <div>
						<div className={ 'm-3' }>
							<div className={ 'd-inline-block ' + minWidth }>
								<span>이름</span>
								{ isInsertMode || isUpdateMode
									? <span className={ 'red' }>*</span>
									: null
								}
							</div>
							<div className={ 'd-inline-block' }>
								{
									isInsertMode || isUpdateMode
										? <Form.Control type="text"
										                placeholder=""
										                required
										                value={ remnantInfo.name }
										                onChange={ onChangeName }/>
										: <span>{ remnantInfo.name }</span>
								}
							</div>
						</div>
						<div className={ 'm-3' }>
							<div className={ 'd-inline-block ' + minWidth }>
								<span>성별</span>
								{ isInsertMode || isUpdateMode
									? <span className={ 'red' }>*</span>
									: null
								}
							</div>
							<div className={ 'd-inline-block' }>
								{ isInsertMode || isUpdateMode ?
									<>
										<Form.Check inline required onChange={ onChangeGender }
										            label="남"
										            checked={ remnantInfo.gender === '남자' }
										            value={ '남자' }
										            type={ 'radio' }
										/>
										<Form.Check inline required onChange={ onChangeGender }
										            label="여"
										            checked={ remnantInfo.gender === '여자' }
										            value={ '여자' }
										            type={ 'radio' }
										/>
									</>
									:
									<>{ remnantInfo.gender }</>
								}
							</div>
						</div>
						<div className={ 'm-3' }>
							<div className={ 'd-inline-block ' + minWidth }>
								<span>학년</span>
								{ isInsertMode || isUpdateMode
									? <span className={ 'red' }>*</span>
									: null
								}
							</div>
							<div className={ 'd-inline-block' }>
								{
									isInsertMode || isUpdateMode ?
										<Form.Select required
										             value={ remnantInfo.grade }
										             onChange={ onChangeGrade }>
											<option value={ '' }>--선택--</option>
											<option value="1">1</option>
											<option value="2">2</option>
											<option value="3">3</option>
											<option value="4">4</option>
											<option value="5">5</option>
											<option value="6">6</option>
										</Form.Select>
										:
										<span>{ remnantInfo.grade }</span>
								}
							</div>
						</div>
						{ isInsertMode || isUpdateMode &&
							<div className={ "mt-5rem" }>
								<div className="d-grid gap-2">
									<Button variant="primary"
									        size="lg"
									        onClick={ onSaveRemnant }>
										저장
									</Button>
								</div>
							</div>
						}
					</div>
				
			}
				<Modal show={ modalShow } onHide={ closeModal }>
					<Modal.Header closeButton>
						<Modal.Title>알림</Modal.Title>
					</Modal.Header>
					<Modal.Body>{ modalBodyTxt }</Modal.Body>
					<Modal.Footer>
						<Button variant="secondary" onClick={ closeModal }>
							닫기
						</Button>
					</Modal.Footer>
				</Modal>
		</>
	)
}