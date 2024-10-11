'use client'
import Pagination from "react-bootstrap/Pagination";

export default function MyPagination( prop ) {
	
	let curPage = prop.curPage;
	let items = [];
	
	// 1,2,3,4 [5], 6,7,8,9...
	// ...5,6,7,8 [9], 10,11,12,13...
	// ...6,7,8,9 [10] 11(끝.)
	// 1,2, [3] (끝.)
	let pagePadding = 4; // 현재 페이지 기준 앞뒤로 최대 4개씩 보여주겠다.
	
	// 시작페이지 설정
	let firstPageNum = curPage - pagePadding;
	// 현재페이지 - pagePadding 가 1 보다 작을 때는 시작페이지를 1 로 한다.
	if ( curPage - pagePadding < 1 ) {
		firstPageNum = 1;
	}
	
	let lastPageNum = curPage + pagePadding; // 페이지네이션 마지막 보여줄 숫자
	// 마지막페이지 = 현재페이지5 이고 총페이지가 엄청 많음. 13임 이러면 지금 보여줄 마지막 페이지는 5 + pagePadding 4 = 9
	// 마지막페이지 = 현재페이지5 이고 총페이지가 8 임. 현재페이지+pagePadding 이 총페이지보다 클 경우에는 총페이지로 한다.
	if ( curPage + pagePadding > prop.totalPage ) {
		lastPageNum = prop.totalPage;
	}
	
	// 1 페이지 버튼
	if ( !prop.isFirst && firstPageNum !== 1 ) {
		items.push( <Pagination.First key={ 'first' }
		                              onClick={ () => prop.onChangePage( 1 ) }>{ 1 }</Pagination.First> );
	}
	// 이전... 버튼
	if ( !prop.isFirst && firstPageNum !== 1 && firstPageNum > 2 ) {
		items.push( <Pagination.Ellipsis key={ 'preEllipsis' }
		                                 onClick={ () => prop.onChangePage( firstPageNum ) }/> );
	}
	
	// 구간 내 pagination item 목록 생성
	for ( let number = firstPageNum; number <= lastPageNum; number++ ) {
		
		items.push( <Pagination.Item key={ number }
		                             active={ number === curPage }
		                             onClick={ () => prop.onChangePage( number ) }
			>
				{ number }
			</Pagination.Item>,
		);
	}
	
	// 다음... 버튼
	if ( !prop.isLast && lastPageNum !== prop.totalPage && lastPageNum + 1 < prop.totalPage ) {
		items.push( <Pagination.Ellipsis key={ 'nextEllipsis' }
		                                 onClick={ () => prop.onChangePage( lastPageNum ) }/> );
	}
	// 마지막 페이지 버튼
	if ( !prop.isLast && lastPageNum !== prop.totalPage ) {
		items.push( <Pagination.Last key={ 'last' }
		                             onClick={ () => prop.onChangePage( prop.totalPage ) }>{ prop.totalPage }</Pagination.Last> );
	}
	
	return (
		<>
			<div>
				<Pagination className={ 'justify-content-center' }>{ items }</Pagination>
			</div>
		</>
	)
}