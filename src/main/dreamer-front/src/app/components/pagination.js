'use client'
import Pagination from "react-bootstrap/Pagination";
import '@/app/css/common.css';

export default function MyPagination() {
	
	let active = 1;
	let items = [];
	for ( let number = 1; number <= 10; number++ ) {
		items.push(
			<Pagination.Item key={ number } active={ number === active }>
				{ number }
			</Pagination.Item>,
		);
	}
	
	return (
		<>
			<div>
				<Pagination className={ 'justify-content-center' }>{ items }</Pagination>
			</div>
		</>
	)
}