import 'bootstrap/dist/css/bootstrap.min.css';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import DreamerNav from "@/app/components/nav";

export default function Home() {
	
	const fetchConfig = {
		method  : 'POST',
		headers : {
			'Accept'       : 'application/json',
			'Content-Type' : 'application/json',
		},
	}
	
	async function getData() {
		try {
			const response = await fetch( '/api/remnant/test', fetchConfig );
			
			if ( !response.ok ) {
				throw new Error( `통신오류가 발생했습니다. Response status: ${ response.status }` );
			}
			
			const json = await response.json();
			console.log( json );
		} catch ( error ) {
			console.error( error.message );
		}
	}
	
	async function fetchTeam() {
		
		const result = await fetch( '', fetchConfig );
		let promise = await result.json();
		console.log( promise );
	}
	
	return (
		<>
		<Container fluid>
			<Row>
				<Col>
					<div>
						<h3>Dreamer</h3>
					</div>
				</Col>
			</Row>
			<Row>
				<Col>
					<DreamerNav/>
				</Col>
			</Row>
			<Row>
				<Col>
					<div>
						<h3>home</h3>
					</div>
				</Col>
			</Row>
		</Container>
		</>
	);
}
