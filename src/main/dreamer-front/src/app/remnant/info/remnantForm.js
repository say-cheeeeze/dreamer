'use client'
import { Button, FloatingLabel, Form, Modal } from "react-bootstrap";
import { useEffect, useRef, useState } from "react";
import CommonJs from "@lib/common";
import { useRouter, useSearchParams } from "next/navigation";
import Image from "react-bootstrap/Image";
import axios from "axios";
import { StatusCodes } from "http-status-codes";
import DaumPost from "@/app/components/daumPost";

export default function RemnantForm( { mode } ) {
	
	const [ remnantObj, setRemnantObj ] = useState( {
		name     : '',
		id       : 0,
		gender   : '',
		birth    : '',
		grade    : '',
		school   : '',
		phone    : '',
		favorite : '',
		friend   : '',
		history  : '',
		roadAddr : '',
		jibunAddr : '',
		zoneCode : '',
		fullAddr : '',
		etcAddr : '',
	} );
	const [ imageObj, setImageObj ] = useState( {
		id             : 0,
		fileFullPath   : '',
		uploadFileName : '',
		saveFileName   : '',
		fileSize       : 0,
	} )
	const defaultImgFileName = '/user.png';
	const fileRef = useRef( null ); // fileInputRef
	const router = useRouter();
	const [ modalShow, setModalShow ] = useState( false );
	const [ modalBodyTxt, setModalBodyTxt ] = useState( '' );
	const searchParams = useSearchParams(); // next/navigation의 useSearchParams 사용
	const showModal = () => setModalShow( true );
	const closeModal = () => setModalShow( false );
	const [ imageUrl, setImageUrl ] = useState( defaultImgFileName );
	const [ hasImg, setHasImg ] = useState( false );
	const [ file, setFile ] = useState( null );
	const isInsertMode = mode === 'insert';
	const isUpdateMode = mode === 'update';
	const isViewMode = mode === 'view';
	
	useEffect( () => {
		
		setHasImg( imageUrl !== defaultImgFileName );
		
	}, [ imageUrl ] );
	
	
	useEffect( () => {
		
		let id = searchParams.get( "id" );
		if ( !isInsertMode && null !== id ) {
			getRemnantInfoById( id );
		}
	}, [] );
	
	const onChangeInputHandler = ( e ) => {
		const { name, value } = e.target;
		
		if ( 'phone' === name ) {
			const phoneExp = /^[0-9]+$/;
			const isOnlyNumber = phoneExp.test( value );
			if ( !isOnlyNumber && !CommonJs.isEmpty( value ) ) {
				return;
			}
		}
		setRemnantObj( {
			...remnantObj,
			[ name ] : value
		} )
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
		if ( null === imageDto || CommonJs.isEmpty( imageDto.fileFullPath ) ) {
			return;
		}
		setImageUrl( '/images/' + imageDto.fileFullPath );
	}
	
	function showModalWithMessage( msg ) {
		setModalBodyTxt( msg );
		showModal();
	}
	
	function onDaumPostComplete( data ) {
		console.log( data );
		
		let fullAddress = data.address;
		let extraAddress = '';
		if ( data.addressType === 'R' ) {
			if ( data.bname !== '' ) {
				extraAddress += data.bname;
			}
			if ( data.buildingName !== '' ) {
				extraAddress += extraAddress !== '' ? `, ${ data.buildingName }` : data.buildingName;
			}
			fullAddress += extraAddress !== '' ? ` (${ extraAddress })` : '';
		}
		
		setRemnantObj({
			...remnantObj,
			roadAddr : data.roadAddress,
			jibunAddr : data.jibunAddress,
			zoneCode : data.zonecode,
			fullAddr : fullAddress,
		})
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
		
		console.log( remnantObj );
		return;
		
		const url = '/api/remnant/save';
		const formData = new FormData();
		
		let remInfo = {
			id       : remnantObj.id,
			name     : remnantObj.name,
			grade    : remnantObj.grade,
			gender   : remnantObj.gender,
			imageDto : imageObj
		}
		formData.append( "data", JSON.stringify( remInfo ) );
		
		if ( file !== null ) {
			formData.append( 'file', file );
		}
		
		await axios.post( url, formData, {
			headers : {
				'Content-Type' : 'multipart/form-data'
			}
		} ).then( res => {
			
			if ( res.data.status === StatusCodes.OK ) {
				router.replace( '/remnant/info?id=' + res.data.saveInfo.id + '&mode=view' );
			}
			else {
				alert( "오류가 발생했습니다" );
				location.reload();
			}
		} ).catch( e => {
			console.error( e );
			alert( "오류가 발생했습니다" );
		} );
	}
	
	return (
		<>
			<div className="table-responsive remnant-regist-area">
				<table className={ 'table' }>
					<tbody>
					<tr>
						<td className="w-auto" rowSpan={ 4 }>
							<div className="img-area">
								<input type={ "file" }
								       accept={ "image/*" }
								       style={ { display : "none" } }
								       onChange={ ( e ) => onFileUpload( e ) }
								       ref={ fileRef }/>
								<Image className="rt-image" src={ imageUrl }/>
								{ !isViewMode && !hasImg &&
									(
										<button className="overlay-button"
										        type="button"
										        onClick={ () => fileRef.current.click() }
										>
											등록
										</button>
									)
								}
								{
									!isViewMode && hasImg &&
									(
										<button className="overlay-button"
										        type="button"
										        onClick={ onClearImage }
										>
											제거
										</button>
									)
								}
							</div>
						</td>
						<td className="w-35p">
							<div className="d-flex justify-content-between align-items-center">
								<div className="f-1">
									<span>이름</span>
									{ !isViewMode
										&& <span className={ 'color-red' }>*</span>
									}
								</div>
								<div className="f-2">
									{
										isInsertMode || isUpdateMode
											? <Form.Control type="text"
											                required
											                placeholder=""
											                name="name"
											                maxLength={ 20 }
											                value={ remnantObj.name }
											                onChange={ onChangeInputHandler }/>
											: <span>{ remnantObj.name }</span>
									}
								</div>
							</div>
						</td>
						<td className="w-35p">
							<div className="d-flex justify-content-between align-items-center">
								<div className="f-1">
									<span>성별</span>
									{ !isViewMode
										&& <span className={ 'color-red' }>*</span>
									}
								</div>
								<div className="f-2">
									{ !isViewMode ?
										<>
											<Form.Check inline
											            required
											            name="gender"
											            onChange={ onChangeInputHandler }
											            label="남"
											            checked={ remnantObj.gender === '남자' }
											            value={ '남자' }
											            type={ 'radio' }
											/>
											<Form.Check inline
											            required
											            name="gender"
											            onChange={ onChangeInputHandler }
											            label="여"
											            checked={ remnantObj.gender === '여자' }
											            value={ '여자' }
											            type={ 'radio' }
											/>
										</>
										:
										<span>{ remnantObj.gender }</span>
									}
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td className="w-35p">
							<div className="d-flex justify-content-between align-items-center">
								<div className="f-1">
									<span>생년월일</span>
									{ !isViewMode
										&& <span className={ 'color-red' }>*</span>
									}
								</div>
								<div className="f-2">
									{
										!isViewMode ?
											<>
												<FloatingLabel controlId="floatingBirthLabel"
												               label="예)20120103">
													<Form.Control type="text"
													              required
													              placeholder=""
													              name="birth"
													              maxLength={ 8 }
													              value={ remnantObj.birth }
													              onChange={ onChangeInputHandler }/>
												</FloatingLabel>
											</>
											: <span>{ remnantObj.birth }</span>
									}
								</div>
							</div>
						</td>
						<td className="w-35p">
						</td>
					</tr>
					<tr>
						<td className="w-35p">
							<div className="d-flex justify-content-between align-items-center">
								<div className="f-1">
									<span>학년</span>
									{ !isViewMode
										&& <span className={ 'color-red' }>*</span>
									}
								</div>
								<div className="f-2">
									{
										!isViewMode ?
											<Form.Select required
											             name="grade"
											             value={ remnantObj.grade }
											             onChange={ onChangeInputHandler }>
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
						</td>
						<td className="w-35p">
							<div className="d-flex justify-content-between align-items-center">
								<div className="f-1">
									<span>학교</span>
									{ !isViewMode
										&& <span className={ 'color-red' }>*</span>
									}
								</div>
								<div className="f-2">
									{
										!isViewMode ?
											<Form.Control type="text"
											              name="school"
											              placeholder=""
											              required
											              maxLength={ 20 }
											              value={ remnantObj.school }
											              onChange={ onChangeInputHandler }/>
											:
											<span>{ remnantObj.school }</span>
									}
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td className="w-auto" colSpan={ 2 }>
							<div className="d-flex justify-content-between align-items-center">
								<div className="f-1">
									<span>주소</span>
								</div>
								<div className="f-5 text-align-left">
									<div className="d-inline-block">
										<Form.Control type="text"
										              disabled={ true }
										              value={ remnantObj.zoneCode }
										              placeholder="우편번호"/>
									</div>
									<div className="d-inline-block">
										<DaumPost onComplete={ onDaumPostComplete }/>
									</div>
									<div className="d-flex mt-custom-sm">
										<div className="f-1">
											<Form.Control type="text"
											              disabled={true}
											              value={ remnantObj.roadAddr }
											              placeholder="도로명주소"/>
										</div>
										<div className="f-1">
											<Form.Control type="text"
											              disabled={ true }
											              value={ remnantObj.jibunAddr }
											              placeholder="지번주소"/>
										</div>
									</div>
									<div className="d-flex mt-custom-sm">
										<div className="f-1">
											<Form.Control type="text"
											              name='fullAddr'
											              onChange={ onChangeInputHandler }
											              value={ remnantObj.fullAddr }
											              placeholder="상세주소"/>
										</div>
										<div className="f-1">
											<Form.Control type="text"
											              name='etcAddr'
											              onChange={ onChangeInputHandler }
											              value={ remnantObj.etcAddr }
											              placeholder="참고항목"/>
										</div>
									</div>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td className="w-auto">
							<div className="d-flex justify-content-between align-items-center">
								<div className="f-1">
									<span>연락처</span>
								</div>
								<div className="f-2">
									<FloatingLabel label="예)01023705000" controlId="floatingPhoneLabel">
										<Form.Control type="text"
										              placeholder=""
										              name="phone"
										              maxLength={ 11 }
										              onChange={ onChangeInputHandler }
										              value={ remnantObj.phone }/>
									</FloatingLabel>
								</div>
							</div>
						</td>
						<td className="w-35p">
							<div className="d-flex justify-content-between align-items-center">
								<div className="f-1">
									<span>좋아하는 것</span>
								</div>
								<div className="f-2">
									<Form.Control type="text"
									              name="favorite"
									              maxLength={ 30 }
									              onChange={ onChangeInputHandler }
									              value={ remnantObj.favorite }
									              placeholder=""/>
								</div>
							</div>
						</td>
						<td className="w-35p">
							<div className="d-flex justify-content-between align-items-center">
								<div className="f-1">
									<span>친한 친구</span>
								</div>
								<div className="f-2">
									<Form.Control type="text"
									              name="friend"
									              onChange={ onChangeInputHandler }
									              value={ remnantObj.friend }
									              placeholder=""/>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td className="w-auto" colSpan={ 3 }>
							<div className="d-flex justify-content-between align-items-center">
								<div className="f-9">
									<FloatingLabel controlId="floatingTextarea2" label="언약의 여정">
										<Form.Control
											as="textarea"
											name="history"
											value={ remnantObj.history }
											placeholder="Leave a comment here"
											onChange={ onChangeInputHandler }
											style={ { height : '100px' } }
										/>
									</FloatingLabel>
								</div>
							</div>
						</td>
					</tr>
					</tbody>
				</table>
			</div>
			{ !isViewMode &&
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