import { Table } from "react-bootstrap";
import Container from "react-bootstrap/Container";
import remnant_list from '@/app/lib/remnant-data';
import MyPagination from "@/app/components/pagination";
import MyButton from "@/app/components/MyButton";

export default function Home() {
	
	// let remnant = await fetch( 'http://localhost:8080/api/remnant/test', {
	// 	method : 'post'
	// } );
	// let remnant2 = await remnant.json();
	// let remnantList = remnant2.list;
	
	// console.log( remnant_list )
	
	return (
		<>
			<Container>
				<h3>Remnant Home</h3>
				<div className={ 'text-align-right' }>
					<MyButton link='/remnant/regist'
					          color='primary'
					          text='신규'
					/>
				</div>
				<div>
					<Table bordered hover>
						<thead>
						<tr>
							<th>No.</th>
							<th>이름</th>
							<th>성별</th>
							<th>학년</th>
							<th>생년월일</th>
							<th>등록일</th>
							<th>수정일</th>
						</tr>
						</thead>
						<tbody>
						{ remnant_list.map( ( post, idx ) => (
							<tr key={ idx }>
								<td>{ idx }</td>
								<td>{ post.name }</td>
								<td>{ post.grade }</td>
								<td>{ post.sex }</td>
								<td>{ post.birth }</td>
								<td>{ post.inputDate }</td>
								<td>{ post.updateDate }</td>
							</tr>
						) ) }
						</tbody>
					</Table>
				</div>
				<div>
					<MyPagination/>
				</div>
			</Container>
		</>
	);
}
