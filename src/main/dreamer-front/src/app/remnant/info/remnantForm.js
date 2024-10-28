'use client'
import { Button, Form, Modal } from "react-bootstrap";
import { useEffect, useRef, useState } from "react";
import CommonJs from "@lib/common";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import Image from "react-bootstrap/Image";
import axios from "axios";

export default function RemnantForm( { mode } ) {
	
	const [ remnantObj, setRemnantObj ] = useState( {
		name   : '',
		id     : 0,
		grade  : '',
		gender : ''
	} );
	const [ imageObj, setImageObj ] = useState({
		id : 0,
		fileFullPath : '',
		uploadFileName : '',
		saveFileName : '',
		fileSize : 0,
	})
	const defaultImgFileName = '/user.png';
	const fileRef = useRef( null ); // fileInputRef
	const router = useRouter();
	const isInsertMode = mode === 'insert';
	const isUpdateMode = mode === 'update';
	const isViewMode = mode === 'view';
	const [ modalShow, setModalShow ] = useState( false );
	const [ modalBodyTxt, setModalBodyTxt ] = useState( '' );
	const pathname = usePathname(); // 현재 경로를 가져옴
	const searchParams = useSearchParams(); // next/navigation의 useSearchParams 사용
	const showModal = () => setModalShow( true );
	const closeModal = () => setModalShow( false );
	const [ imageUrl, setImageUrl ] = useState( defaultImgFileName );
	const [ hasImg, setHasImg ] = useState( false );
	const [ file, setFile ] = useState( null );
	
	useEffect( () => {
		
		setHasImg( imageUrl !== defaultImgFileName );
		
	}, [ imageUrl ] );
	
	
	useEffect( () => {
		
		let id = searchParams.get( "id" );
		if ( !isInsertMode && null !== id ) {
			getRemnantInfoById( id );
		}
	}, [] );
	
	const onChangeName = ( e ) => {
		setRemnantObj( {
			...remnantObj,
			name : e.target.value
		} )
	}
	const onChangeGender = ( e ) => {
		setRemnantObj( {
			...remnantObj,
			gender : e.target.value
		} );
	}
	const onChangeGrade = ( e ) => {
		setRemnantObj( {
			...remnantObj,
			grade : e.target.value
		} );
	}
	
	function getRemnantInfoById( id ) {
		
		const url = '/api/remnant/get';
		const param = { id };
		
		axios.post( url, param ).then( res => {
			
			setRemnantData( res.data.info );
			
		} ).catch( e => {
			console.error( e );
			alert( "오류가 발생했습니다" );
			
		} ).finally( () => {
		} );
	}
	
	function setRemnantData( serverData ) {
		setRemnantObj( serverData );
		setImageObj( serverData.imageDto );
		setImageThumbnail( serverData.imageDto );
	}
	
	function setImageThumbnail( imageDto ) {
		console.log( imageDto );
		if ( null === imageDto ) {
			return;
		}
		setImageUrl( '/images/' + imageDto.fileFullPath );
	}
	
	function showModalWithMessage( msg ) {
		setModalBodyTxt( msg );
		showModal();
	}
	
	function onClickImg() {
		if ( !hasImg ) {
			return;
		}
		window.open( imageUrl, "_blank", "noopener, noreferrer" );
	}
	
	// file upload 를 위해 useRef 로 input tag(display none)를 Button onClick 에 연결했다.
	function onFileUpload( e ) {
		
		if ( CommonJs.isEmpty( e.target.files ) ) {
			return;
		}
		
		let fileList = e.target.files; // []
		let file = fileList[ 0 ]; // 파일은 하나만 가능하여.
		let type = file.type;
		
		if ( type.indexOf( "image" ) === -1 ) {
			alert( "사진파일형식이 아닙니다" );
			return;
		}
		let urlFile = URL.createObjectURL( file );
		
		setFile( file );
		setImageUrl( urlFile );
	}
	
	function onClearImage() {
		setImageUrl( defaultImgFileName )
	}
	
	async function onSaveRemnant() {
		
		if ( CommonJs.isEmpty( remnantObj.name ) ) {
			showModalWithMessage( '이름을 입력해주세요.' );
			return;
		}
		
		if ( CommonJs.isEmpty( remnantObj.gender ) ) {
			showModalWithMessage( '성별을 선택해주세요.' );
			return;
		}
		if ( CommonJs.isEmpty( remnantObj.grade ) ) {
			showModalWithMessage( '학년을 선택해주세요.' );
			return;
		}
		
		const url = '/api/remnant/save';
		const formData = new FormData();
		
		let remInfo = {
			id     : remnantObj.id,
			name   : remnantObj.name,
			grade  : remnantObj.grade,
			gender : remnantObj.gender,
			imageDto : imageObj
		}
		formData.append( "data", JSON.stringify( remInfo ) );
		
		console.log( [...formData] );
		
		if ( file !== null ) {
			formData.append( 'file', file );
		}
		
		await axios.post( url, formData, {
			headers : {
				'Content-Type' : 'multipart/form-data'
			}
		} ).then( res => {
			
			if ( 200 === res.data.status ) {
				router.replace( '/remnant/info?id=' + res.data.saveInfo.id + '&mode=view' );
			}
			else {
				alert( "오류가 발생했습니다" );
				location.reload();
			}
		}).catch( e => {
			console.error( e );
			alert( "오류가 발생했습니다" );
		});
	}
	
	return (
		<>
			<div className={ 'rt-area-wrapper' }>
				<div className={ 'rt-area-left' }>
					<div className={ 'm-3' }>
						<div className={ 'd-inline-block min-width-5rem' }>
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
									                value={ remnantObj.name }
									                onChange={ onChangeName }/>
									: <span>{ remnantObj.name }</span>
							}
						</div>
					</div>
					<div className={ 'm-3' }>
						<div className={ 'd-inline-block min-width-5rem' }>
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
									             value={ remnantObj.grade }
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
									<span>{ remnantObj.grade }</span>
							}
						</div>
					</div>
					<div className={ 'm-3' }>
						<div className={ 'd-inline-block min-width-5rem' }>
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
									            checked={ remnantObj.gender === '남자' }
									            value={ '남자' }
									            type={ 'radio' }
									/>
									<Form.Check inline required onChange={ onChangeGender }
									            label="여"
									            checked={ remnantObj.gender === '여자' }
									            value={ '여자' }
									            type={ 'radio' }
									/>
								</>
								:
								<>{ remnantObj.gender }</>
							}
						</div>
					</div>
				</div>
				<div className={ 'rt-area-right' }>
					<div style={ { width : '100%' } }>
						<div className={ "rt-img-wrapper" }>
							<Image src={ imageUrl }
							       rounded
							       className={
								       !hasImg
									       ? 'rt-img-default'
									       : 'rt-img-upload'
							       }
							       onClick={ () => onClickImg() }
							/>
						</div>
						{
							!isViewMode && (
								<div>
									<div className={ "d-grid gap-2" }>
										<input type={ "file" }
										       accept={ "image/*" }
										       style={ { display : "none" } }
										       onChange={ ( e ) => onFileUpload( e ) }
										       ref={ fileRef }/>
										<Button variant={ 'outline-primary' }
										        onClick={ () => fileRef.current.click() }>
											사진 등록
										</Button>
										<Button variant={ 'outline-primary' }
										        disabled={ !hasImg }
										        onClick={ () => onClearImage() }>
											제거
										</Button>
									</div>
								</div>
							)
						}
					</div>
				</div>
			</div>
			{ ( isInsertMode || isUpdateMode ) &&
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