'use client'
import { useEffect, useState, useRef } from "react";
import moment from "moment";
import { Button, Table } from "react-bootstrap";
import Container from "react-bootstrap/Container";
import EmptyTD from "@/app/components/EmptyTD";
import MyButton from "@/app/components/MyButton";
import MyPagination from "@/app/components/pagination";
import LoadingSpinTD from "@/app/components/LoadingSpinTD";
import { usePathname, useRouter, useSearchParams, } from "next/navigation";
import { Form } from "react-bootstrap";
import axios from "axios";
import constant from '@/app/utils/constant';

export default function RemnantListPage() {
	
	const router = useRouter();
	const pathname = usePathname(); // 현재 경로를 가져옴
	const searchParams = useSearchParams(); // next/navigation의 useSearchParams 사용
	
	const [ totalCount, setTotalCount ] = useState( 0 );
	const [ totalPage, setTotalPage ] = useState( 1 );
	const [ curPage, setCurPage ] = useState( 0 );
	const [ pageSize, setPageSize ] = useState( 15 );
	const [ paginationCnt, setPaginationCnt ] = useState( 10 ); // 초기값 10
	const [ rtList, setRtList ] = useState( [] );
	const [ loading, setLoading ] = useState( true );
	const [ isFirst, setIsFirst ] = useState( false );
	const [ isLast, setIsLast ] = useState( false );
	const [ searchText, setSearchText ] = useState( '' );
	const [ searchFlag, setSearchFlag ] = useState( true );
	
	const searchInputRef = useRef( null );
	const columnCnt = Object.keys( constant.remnantColumnObj ).length;
	
	// init 1 page if queryParam is null
	useEffect( () => {
		let page = searchParams.get( 'page' );
		if ( null === page ) {
			pageHandler( 1 );
		}
	}, [] );
	
	useEffect( () => {
		
		if ( curPage > 0 ) {
			getRemnantList( curPage );
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
	
	function getRemnantList( page ) {
		
		const url = '/api/remnant/list';
		
		let param = {
			page  : page,
			size  : pageSize,
			name  : searchText,
			grade : searchText
		}
		axios.post( url, param ).then( res => {
			
			setIsFirst( res.data.listInfo.isFirst );
			setIsLast( res.data.listInfo.isLast );
			setTotalCount( res.data.listInfo.totalCount );
			reformRtList( res.data.listInfo.list );
			
			// 마지막 페이지네이션 번호 계산
			setTotalPage( Math.ceil( res.data.listInfo.totalCount / pageSize ) );
			
		} ).catch( e => {
			alert( "오류가 발생했습니다." );
			console.error( e );
			
		} ).finally( () => {
			setLoading( false );
		} );
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
	
	function reformRtList( list ) {
		let _list = [ ...list ];
		
		_list.forEach( function ( item ) {
			item.inputDate = moment( item.inputDate ).format( 'YYYY-MM-DD' );
			item.updateDate = moment( item.updateDate ).format( 'YYYY-MM-DD' );
		} );
		
		setRtList( _list );
	}
	
	function goRemnantViewPage( remId ) {
		location.href = `/remnant/info?id=${ remId }&mode=view`;
	}
	
	function setRemnantDataRow() {
		
		if ( loading ) {
			return (
				<tr>
					<LoadingSpinTD colSpan={ columnCnt }/>
				</tr>
			);
		}
		else if ( rtList.length < 1 ) {
			return (
				<tr>
					<EmptyTD colSpan={ columnCnt }/>
				</tr>
			)
		}
		else {
			
			return rtList.map( ( rtInfo, idx ) => (
				<tr key={ idx }>
					<td>
						<span>{ rtInfo.id }</span>
					</td>
					<td>
						<span className="rt-name"
						      onClick={ () => goRemnantViewPage( rtInfo.id ) }>{ rtInfo.name }</span>
					</td>
					<td>
						<span>{ rtInfo.gender }</span>
					</td>
					<td>
						<span>{ rtInfo.school }</span>
					</td>
					<td>
						<span>{ rtInfo.grade }</span>
					</td>
					<td>
						<span>{ rtInfo.phone }</span>
					</td>
					<td>
						<span>{ rtInfo.birth }</span>
					</td>
					<td>
						<span>{ rtInfo.inputDate }</span>
					</td>
					<td>
						<span>{ rtInfo.updateDate }</span>
					</td>
				</tr> ) );
		}
	}
	
	return (
		<>
			<Container>
				<div className={ 'container-header' }>
					<h3>렘넌트 현황</h3>
				</div>
				<div className={ 'text-align-right mb-1' }>
					<div style={ { display : "inline-block", float : "left" } }>
						<span>총&nbsp;</span>
						<span style={ { margin : "0px 2px" } }>:</span>
						<span>{ totalCount }</span>
						<span>&nbsp;건</span>
					</div>
					<MyButton link="/remnant/info?mode=insert"
					          color="outline-primary"
					          text="신규 등록"
					/>
				</div>
				<div>
					<Table bordered className={ "tb-rt" }>
						<thead>
						<tr>
							<th width={ constant.remnantColumnObj.id.width }>No.</th>
							<th width={ constant.remnantColumnObj.name.width }>이름</th>
							<th width={ constant.remnantColumnObj.gender.width }>성별</th>
							<th width={ constant.remnantColumnObj.school.width }>학교</th>
							<th width={ constant.remnantColumnObj.grade.width }>학년</th>
							<th width={ constant.remnantColumnObj.phone.width }>연락처</th>
							<th width={ constant.remnantColumnObj.birth.width }>생년월일</th>
							<th width={ constant.remnantColumnObj.inputDate.width }>등록일</th>
							<th width={ constant.remnantColumnObj.updateDate.width }>수정일</th>
						</tr>
						</thead>
						<tbody>
						{ setRemnantDataRow() }
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
						이름 또는 학년 검색
					</Form.Text>
				</div>
			</Container>
		</>
	);
}
