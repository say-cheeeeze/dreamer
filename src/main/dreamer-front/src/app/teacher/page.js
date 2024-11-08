'use client';
import { useEffect, useRef, useState } from "react";
import moment from "moment";
import { Button, Form, Table } from "react-bootstrap";
import Container from "react-bootstrap/Container";
import EmptyTD from "@/app/components/EmptyTD";
import MyPagination from "@/app/components/pagination";
import LoadingSpinTD from "@/app/components/LoadingSpinTD";
import { usePathname, useRouter, useSearchParams, } from "next/navigation";
import axios from "axios";
import constant from '@/app/utils/constant';
import axiosProvider from "@lib/axiosProvider";
import CommonJs from "@lib/common";

export default function TeacherListPage() {
	
	const $axios = axiosProvider();
	const router = useRouter();
	const pathname = usePathname(); // 현재 경로를 가져옴
	const searchParams = useSearchParams(); // next/navigation의 useSearchParams 사용
	
	const [ totalCount, setTotalCount ] = useState( 0 );
	const [ totalPage, setTotalPage ] = useState( 1 );
	const [ curPage, setCurPage ] = useState( 0 );
	const [ pageSize, setPageSize ] = useState( 15 );
	const [ paginationCnt, setPaginationCnt ] = useState( 10 ); // 초기값 10
	const [ teacherList, setTeacherList ] = useState( [] );
	const [ loading, setLoading ] = useState( true );
	const [ isFirst, setIsFirst ] = useState( false );
	const [ isLast, setIsLast ] = useState( false );
	const [ searchText, setSearchText ] = useState( '' );
	const [ searchFlag, setSearchFlag ] = useState( true );
	
	const searchInputRef = useRef( null );
	const columnCnt = Object.keys( constant.teacherColumnObj ).length;
	
	// init 1 page if queryParam is null
	useEffect( () => {
		let page = searchParams.get( 'page' );
		if ( null === page ) {
			pageHandler( 1 );
		}
	}, [] );
	
	useEffect( () => {
		
		if ( curPage > 0 ) {
			getTeacherList( curPage );
		}
	}, [ curPage, searchFlag ] );
	
	// query string page
	useEffect( () => {
		
		const page = searchParams.get( 'page' );
		
		if ( page ) {
			setCurPage( resetCurPage( page ) );
		}
		
	}, [ searchParams ] );  // searchParams 변경될 때마다 반영
	
	function resetCurPage( page ) {
		if ( page < 1 || page === undefined ) {
			return 1;
		}
		return parseInt( page, 10 );
	}
	
	function getTeacherList( page ) {
		
		const url = '/api/teacher/list';
		
		let param = {
			page : page,
			size : pageSize,
			name : searchText
		}
		
		$axios.post( url, param ).then( res => {
			
			if ( CommonJs.isEmpty( res.data.listInfo ) ) {
				return;
			}
			
			setIsFirst( res.data.listInfo.isFirst );
			setIsLast( res.data.listInfo.isLast );
			setTotalCount( res.data.listInfo.totalCount );
			reformTeacherList( res.data.listInfo.list );
			
			// 마지막 페이지네이션 번호 계산
			setTotalPage( Math.ceil( res.data.listInfo.totalCount / pageSize ) );
			
		} ).catch( e => {
			alert( "오류가 발생했습니다." );
			console.log( e );
			
		} ).finally( () => {
			setLoading( false );
		} );
	}
	
	function reformTeacherList( list ) {
		let _list = [ ...list ];
		
		_list.forEach( function ( item ) {
			item.inputDate = moment( item.inputDate ).format( 'YYYY-MM-DD HH:mm' );
			item.updateDate = moment( item.updateDate ).format( 'YYYY-MM-DD HH:mm' );
		} );
		
		setTeacherList( _list );
	}
	
	function pageHandler( clickPage ) {
		
		clickPage = resetCurPage( clickPage );
		// query string을 업데이트하면서 페이지 이동
		const newParams = new URLSearchParams( searchParams );
		newParams.set( 'page', clickPage );
		
		router.replace(
			`${ pathname }?${ newParams.toString() }`,
			{ shallow : true }
		);
		setCurPage( clickPage );  // 상태 업데이트
	}
	
	function onClickSearchBtn() {
		setSearchText( searchInputRef.current.value );
		setSearchFlag( !searchFlag );
		pageHandler( 1 );
	}
	
	function onKeyDownSearchForm( e ) {
		if ( "Enter" === e.key ) {
			onClickSearchBtn();
		}
	}
	
	function goTeacherViewPage( teacherId ) {
		console.log( teacherId );
		// location.href = `/teacher/info?id=${ teacherId }&mode=view`;
	}
	
	function setTeacherDataRow() {
		
		if ( loading ) {
			return (
				<tr>
					<LoadingSpinTD colSpan={ columnCnt }/>
				</tr>
			);
		}
		else if ( teacherList.length < 1 ) {
			return (
				<tr>
					<EmptyTD colSpan={ columnCnt }/>
				</tr>
			)
		}
		else {
			
			return teacherList.map( ( teacherInfo, idx ) => (
				<tr key={ idx }>
					<td>
						<span>{ teacherInfo.id }</span>
					</td>
					<td>
						<span className="rt-name"
						      onClick={ () => goTeacherViewPage( teacherInfo.id ) }>{ teacherInfo.name }</span>
					</td>
					<td>
						<span>{ teacherInfo.loginId }</span>
					</td>
					<td>
						<span>{ teacherInfo.email }</span>
					</td>
					<td>
						<span>{ teacherInfo.phone }</span>
					</td>
					<td>
						<span>{ teacherInfo.inputDate }</span>
					</td>
					<td>
						<span>{ teacherInfo.updateDate }</span>
					</td>
				</tr> ) );
		}
	}
	
	return (
		<>
			<Container>
				<div className={ 'container-header' }>
					<h3>교사 현황</h3>
				</div>
				<div className={ 'text-align-right mb-1' }>
					<div style={ { display : "inline-block", float : "left" } }>
						<span>총&nbsp;</span>
						<span style={ { margin : "0px 2px" } }>:</span>
						<span>{ totalCount }</span>
						<span>&nbsp;건</span>
					</div>
				</div>
				<div>
					<Table bordered className={ "tb-rt" }>
						<thead>
						<tr>
							<th width={ constant.teacherColumnObj.id.width }>등록번호</th>
							<th width={ constant.teacherColumnObj.name.width }>이름</th>
							<th width={ constant.teacherColumnObj.loginId.width }>아이디</th>
							<th width={ constant.teacherColumnObj.email.width }>이메일</th>
							<th width={ constant.teacherColumnObj.phone.width }>연락처</th>
							<th width={ constant.teacherColumnObj.inputDate.width }>등록일</th>
							<th width={ constant.teacherColumnObj.updateDate.width }>수정일</th>
						</tr>
						</thead>
						<tbody>
						{ setTeacherDataRow() }
						</tbody>
					</Table>
				</div>
				<div>
					<MyPagination paginationCnt={ paginationCnt }
					              totalPage={ totalPage }
					              totalCount={ totalCount }
					              curPage={ curPage }
					              onChangePage={ pageHandler }
					              isFirst={ isFirst }
					              isLast={ isLast }
					/>
				</div>
				<div style={ { textAlign : "center" } }>
					<Form.Control type="text"
					              placeholder=""
					              ref={ searchInputRef }
					              onKeyDown={ onKeyDownSearchForm }
					              style={ { width : "30vw", display : "inline-block" } }
					/>
					<Button className={ "ml-1" }
					        variant="outline-primary"
					        onClick={ onClickSearchBtn }>
						검색
					</Button>
				</div>
				<div className={ "text-align-center" }>
					<Form.Text muted>
						이름 검색
					</Form.Text>
				</div>
			</Container>
		</>
	);
}