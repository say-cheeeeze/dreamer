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

let domain = process.env.local3000;
let alignCenter = {
	textAlign : "center"
}

export default function Home() {
	
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
	
	const searchInputRef = useRef(null );
	
	// init 1 page if queryParam is null
	useEffect( () => {
		let page = searchParams.get( 'page' );
		if ( null === page ) {
			pageHandler( 1 );
		}
	}, []);
	
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
		
		const url = domain + '/api/remnant/list';
		const requestObj = {
			method  : 'POST',
			headers : {
				"Content-Type" : "application/json",
			},
			body    : JSON.stringify( {
				page : page,
				size : pageSize,
				name : searchText,
				grade : searchText
				
			} )
		}
		
		fetch( url, requestObj ).then( ( response ) => {
			
			response.json().then( data => {
				
				setIsFirst( data.listInfo.isFirst );
				setIsLast( data.listInfo.isLast );
				setTotalCount( data.listInfo.totalCount );
				reformRtList( data.listInfo.list );
				
				// 마지막 페이지네이션 번호 계산
				setTotalPage( Math.ceil( data.listInfo.totalCount / pageSize ) );
				setLoading( false );
			} );
			
		} ).catch( e => {
			console.error( e );
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
			{ shallow:true}
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
	function onChangeSearchText( e ) {
		setSearchText( e.target.value );
	}
	
	function setRemnantDataRow() {
		
		if ( loading ) {
			return (
				<tr>
					<LoadingSpinTD colSpan={ 6 }/>
				</tr>
			);
		}
		else if ( rtList.length < 1 ) {
			return (
				<tr>
					<EmptyTD colSpan={ 6 }/>
				</tr>
			)
		}
		else {
			
			return rtList.map( ( post, idx ) => (
				<tr key={ idx }>
					<td style={ alignCenter }>{ post.id }</td>
					<td style={ alignCenter }>{ post.name }</td>
					<td style={ alignCenter }>{ post.gender }</td>
					<td style={ alignCenter }>{ post.grade }</td>
					<td style={ alignCenter }>{ post.inputDate }</td>
					<td style={ alignCenter }>{ post.updateDate }</td>
				</tr> ) );
		}
	}
	
	return (
		<>
			<Container>
				<div style={ { margin : "1rem 0 3rem" } }>
					<h3>Remnant Home</h3>
				</div>
				<div className={ 'text-align-right mb-1rem' }>
					<div style={ { display : "inline-block", float : "left" } }>
						<span>총 게시글</span>
						<span style={ { margin : "0px 2px" } }>:</span>
						<span>{ totalCount }</span>
						<span>건</span>
					</div>
					<MyButton link="/remnant/regist"
					          color="primary"
					          text="신규"
					/>
				</div>
				<div>
					<Table bordered>
						<thead>
						<tr>
							<th style={ alignCenter }>No.</th>
							<th style={ alignCenter }>이름</th>
							<th style={ alignCenter }>성별</th>
							<th style={ alignCenter }>학년</th>
							<th style={ alignCenter }>등록일</th>
							<th style={ alignCenter }>수정일</th>
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
					<Button className={ "ml-05vw "}
					        variant={ "dark" }
					        onClick={ onClickSearchBtn }>
						검색
					</Button>
				</div>
			</Container>
		</>
	);
}
