'use client'
import { Button, FloatingLabel, Form, Modal } from "react-bootstrap";
import { useEffect, useRef, useState } from "react";
import CommonJs from "@lib/common";
import { useRouter, useSearchParams } from "next/navigation";
import Image from "react-bootstrap/Image";
import axios from "axios";
import { StatusCodes } from "http-status-codes";
import DaumPost from "@/app/components/daumPost";
import AlertModal from "@/app/components/alertmodal";

export default function RemnantForm( { mode } ) {
	
	const [ remnantObj, setRemnantObj ] = useState( {
		id     : 0, name : '', // 필수
		gender : '', // 필수
		birth  : '', // 필수
		grade  : '', // 필수
		school : '', // 필수
		phone  : '', favorite : '', friend : '', history : '', roadAddr : '', jibunAddr : '', zoneCode : '', fullAddr : '', etcAddr : '',
	} );
	const [ imageObj, setImageObj ] = useState( {
		id : 0, fileFullPath : '', uploadFileName : '', saveFileName : '', fileSize : 0,
	} );
	
	const [ reply, setReply ] = useState( '' );
	const [ replyList, setReplyList ] = useState( [
		{
			id        : 1,
			content   : '전능하사 천지를 만드신 하나님 아버지를 믿사오니 이는\n\n전능하사 천지를 만드신 하나님 아버지를 믿사오니 이는전능하사 천지를 만드신 하나님 아버지를 믿사오니 이는전능하사 천지를 만드신 하나님 아버지를 믿사오니 이는전능하사 천지를 만드신 하나님 아버지를 믿사오니 이는전능하사 천지를 만드신 하나님 아버지를 믿사오니 이는전능하사 천지를 만드신 하나님 아버지를 믿사오니 이는',
			inputDate : '2024-11-03 21:03',
			name      : '남윤재'
		},
	] );
	
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
	const [ replyEditId, setReplyEditId ] = useState( -1 );
	const [ updateReplyText, setUpdateReplyText ] = useState('');
	
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
			...remnantObj, [ name ] : value
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
	
	function setReplyDOM() {
		
		return replyList.map( ( info, idx ) => (
			<div key={ idx } className="reply-item">
				<div className="reply-head d-flex align-items-center justify-content-between mb-3">
					<div className="f-8">
						<div className="d-inline-block mr-5">
							<span>{ info.name }</span>
						</div>
						<div className="d-inline-block color-gray-50">
							<span>{ info.inputDate }</span>
						</div>
					</div>
					<div className="f-1 text-align-right">
						{
							replyEditId === info.id
								? <>
									<div className="reply-btn-area mr-1"
									     onClick={ () => cancelEditReply() }>
										<span className="reply-btn-span">취소</span>
									</div>
									<div className="reply-btn-area"
									     onClick={ () => updateReply( info ) }>
										<span className="reply-btn-span">저장</span>
									</div>
								</>
								: <>
									<div className="reply-btn-area mr-1"
									     onClick={ () => onEditModeReply( info ) }>
										<span className="reply-btn-span">수정</span>
									</div>
									<div className="reply-btn-area"
									     onClick={ () => deleteReply( info.id ) }>
										<span className="reply-btn-span">삭제</span>
									</div>
								</>
						}
					</div>
				</div>
				<div>
					{
						replyEditId === info.id
							? <Form.Control as="textarea"
							                value={ updateReplyText }
							                onChange={ onChangeUpdateReply }
							                style={ { height : '100px' } }/>
							: <pre>{ info.content }</pre>
					}
				</div>
			</div>
		) );
	}
	
	function onChangeReply( e ) {
		setReply( e.target.value );
	}
	
	function onEditModeReply( info ) {
		setReplyEditId( info.id );
		setUpdateReplyText( info.content );
	}
	
	function cancelEditReply() {
		setReplyEditId( -1 );
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
		
		setRemnantObj( {
			...remnantObj, roadAddr : data.roadAddress, jibunAddr : data.jibunAddress, zoneCode : data.zonecode, fullAddr : fullAddress,
		} )
	}
	
	function onClickImg() {
		if ( !hasImg ) {
			return;
		}
		// window.open( imageUrl, "_blank", "noopener, noreferrer" ); // new tab
		window.open( imageUrl, "_blank", "popup=true" ); // new window
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
	
	function deleteReply( id ) {
		
		if ( !confirm( "댓글을 삭제하시겠습니까?" ) ) {
			return;
		}
		
		console.log( id );
		
		//TODO
		// api response success...
		
		// 원래는 그냥 list 를 다시 불러오면 끝난다.
		// 아래는 UI test code 이다.
		let list = [ ...replyList ];
		list.forEach( (item, idx ) => {
			if ( item.id === id ) {
				list.splice( idx, 1 );
			}
		});
		setReplyList( list );
	}
	
	function onChangeUpdateReply(e) {
		let value = e.target.value;
		console.log( value );
		setUpdateReplyText( value );
	}
	
	function updateReply( info ) {
		if ( CommonJs.isEmpty( updateReplyText ) ) {
			alert( '내용을 입력해주세요.' );
			return;
		}
		info.content = updateReplyText;
		requestUpdateReplyAPI( info );
	}
	function requestUpdateReplyAPI(info) {
	
		//TODO
		// api...response success
		
		let list = [ ...replyList ];
		let findInfo = list.filter( i => i.id === info.id )[0];
		findInfo = {
			...findInfo,
			content : info.content
		}
		console.log( "변경된 findInfo :", findInfo );
		console.log( list );
		setReplyList( list );
		setUpdateReplyText( '' );
		setReplyEditId( -1 );
	}
	
	function onClearImage() {
		setFile( null );
		setImageUrl( defaultImgFileName )
	}
	
	async function requestSaveReplyAPI() {
		
		if ( CommonJs.isEmpty( reply ) ) {
			alert( "내용을 입력해주세요" );
			return;
		}
		
		//TODO
		// api... response...success...
		
		setReplyList( [
			...replyList,
			{
				id        : replyList.length + 1,
				name      : 'test',
				content   : reply,
				inputDate : '2024-11-03 21:03'
			}
		] );
		
		setReply( '' );
	}
	
	
	async function requestSaveRemnantAPI() {
		
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
		
		if ( CommonJs.isEmpty( remnantObj.birth ) ) {
			showModalWithMessage( '생년월일을 입력해주세요.' );
			return;
		}
		if ( CommonJs.isEmpty( remnantObj.school ) ) {
			showModalWithMessage( '학교를 입력해주세요.' );
			return;
		}
		
		const url = '/api/remnant/save';
		const formData = new FormData();
		
		let param = {
			...remnantObj,
			imageDto : imageObj
		}
		
		if ( !hasImg ) {
			param.imageDto = null;
		}
		
		console.log( param );
		
		formData.append( "data", JSON.stringify( param ) );
		
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
	
	return ( <>
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
							<Image className={ isViewMode ? "rt-image view" : "rt-image" }
							       src={ imageUrl }
							       onClick={ onClickImg }
							/>
							{ ( !isViewMode && !hasImg ) && ( <button className="overlay-button"
							                                          type="button"
							                                          onClick={ () => fileRef.current.click() }
							>
								등록
							</button> ) }
							{ !isViewMode && hasImg && ( <button className="overlay-button"
							                                     type="button"
							                                     onClick={ onClearImage }
							>
								제거
							</button> ) }
						</div>
					</td>
					<td className="w-35p">
						<div className="d-flex justify-content-between align-items-center">
							<div className="f-1">
								<span>이름</span>
								{ !isViewMode && <span className={ 'color-red' }>*</span> }
							</div>
							<div className="f-2">
								{ isInsertMode || isUpdateMode ? <Form.Control type="text"
								                                               required
								                                               placeholder=""
								                                               name="name"
								                                               maxLength={ 20 }
								                                               value={ remnantObj.name }
								                                               onChange={ onChangeInputHandler }/> :
									<span>{ remnantObj.name }</span> }
							</div>
						</div>
					</td>
					<td className="w-35p">
						<div className="d-flex justify-content-between align-items-center">
							<div className="f-1">
								<span>성별</span>
								{ !isViewMode && <span className={ 'color-red' }>*</span> }
							</div>
							<div className="f-2">
								{ !isViewMode ? <>
									<Form.Check inline
									            required
									            name="gender"
									            onChange={ onChangeInputHandler }
									            label="남"
									            checked={ remnantObj.gender === '남' }
									            value={ '남' }
									            type={ 'radio' }
									/>
									<Form.Check inline
									            required
									            name="gender"
									            onChange={ onChangeInputHandler }
									            label="여"
									            checked={ remnantObj.gender === '여' }
									            value={ '여' }
									            type={ 'radio' }
									/>
								</> : <span>{ remnantObj.gender }</span> }
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td className="w-35p">
						<div className="d-flex justify-content-between align-items-center">
							<div className="f-1">
								<span>생년월일</span>
								{ !isViewMode && <span className={ 'color-red' }>*</span> }
							</div>
							<div className="f-2">
								{ !isViewMode ? <>
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
								</> : <span>{ remnantObj.birth }</span> }
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
								{ !isViewMode && <span className={ 'color-red' }>*</span> }
							</div>
							<div className="f-2">
								{ !isViewMode ? <Form.Select required
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
								</Form.Select> : <span>{ remnantObj.grade }</span> }
							</div>
						</div>
					</td>
					<td className="w-35p">
						<div className="d-flex justify-content-between align-items-center">
							<div className="f-1">
								<span>학교</span>
								{ !isViewMode && <span className={ 'color-red' }>*</span> }
							</div>
							<div className="f-2">
								{ !isViewMode ? <Form.Control type="text"
								                              name="school"
								                              placeholder=""
								                              required
								                              maxLength={ 20 }
								                              value={ remnantObj.school }
								                              onChange={ onChangeInputHandler }/> : <span>{ remnantObj.school }</span> }
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
								{ isViewMode
									? <span>{ remnantObj.fullAddr.concat( ' ' ).concat( remnantObj.etcAddr ) }</span>
									: <>
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
												              disabled={ true }
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
												              name="fullAddr"
												              onChange={ onChangeInputHandler }
												              value={ remnantObj.fullAddr }
												              placeholder="상세주소"/>
											</div>
											<div className="f-1">
												<Form.Control type="text"
												              name="etcAddr"
												              onChange={ onChangeInputHandler }
												              value={ remnantObj.etcAddr }
												              placeholder="참고항목"/>
											</div>
										</div>
									</>
								}
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
								{
									isViewMode
										? <span>{ remnantObj.phone }</span>
										: <FloatingLabel label="예)01023705000" controlId="floatingPhoneLabel">
											<Form.Control type="text"
											              placeholder=""
											              name="phone"
											              maxLength={ 11 }
											              onChange={ onChangeInputHandler }
											              value={ remnantObj.phone }/>
										</FloatingLabel>
								}
							</div>
						</div>
					</td>
					<td className="w-35p">
						<div className="d-flex justify-content-between align-items-center">
							<div className="f-1">
								<span>좋아하는 것</span>
							</div>
							<div className="f-2">
								{
									isViewMode
										? <span>{ remnantObj.favorite }</span>
										: <Form.Control type="text"
										                name="favorite"
										                maxLength={ 30 }
										                onChange={ onChangeInputHandler }
										                value={ remnantObj.favorite }
										                placeholder=""
										/>
								}
							</div>
						</div>
					</td>
					<td className="w-35p">
						<div className="d-flex justify-content-between align-items-center">
							<div className="f-1">
								<span>친한 친구</span>
							</div>
							<div className="f-2">
								{
									isViewMode
										? <span>{ remnantObj.friend }</span>
										: <Form.Control type="text"
										                name="friend"
										                onChange={ onChangeInputHandler }
										                value={ remnantObj.friend }
										                placeholder=""
										/>
									
								}
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td className="w-auto" colSpan={ 3 }>
						<div className="d-flex justify-content-between align-items-center">
							<div className="f-9">
								{
									isViewMode
										? <>
											<div className={ 'text-align-left' }>
												<p>{ remnantObj.history }</p>
											</div>
										</>
										: <FloatingLabel controlId="floatingTextarea2" label="언약의 여정">
											<Form.Control
												as="textarea"
												name="history"
												value={ remnantObj.history }
												placeholder="Leave a comment here"
												onChange={ onChangeInputHandler }
												style={ { height : '100px' } }
											/>
										</FloatingLabel>
								}
							</div>
						</div>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
		{
			isViewMode &&
			<div className="reply-container-wrapper">
				<div className="reply-input-area d-flex">
					<div className={ 'f-9' }>
						<Form.Control
							as="textarea"
							name="reply"
							value={ reply }
							onChange={ onChangeReply }
							className="textarea-reply"
							placeholder=""
						/>
					</div>
					<div className={ 'f-1 p-1' }>
						<Button variant="secondary"
						        className="btn-save-reply"
						        size="sm"
						        onClick={ requestSaveReplyAPI }>
							저장
						</Button>
					</div>
				</div>
				<div className="reply-listcnt-area">
					<span className="fw-bold">댓글&nbsp;{ replyList.length }</span>
				</div>
				<div className="reply-list-area">
					{ setReplyDOM() }
				</div>
			</div>
		}
		{
			!isViewMode
			&&
			<div className={ "mt-5rem" }>
				<div className="d-grid gap-2">
					<Button variant="primary"
					        size="lg"
					        onClick={ requestSaveRemnantAPI }>
						저장
					</Button>
				</div>
			</div>
		}
		<AlertModal show={ modalShow }
		            onHide={ closeModal }
		            body={ modalBodyTxt }
		/>
	</> )
}