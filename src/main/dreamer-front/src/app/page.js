import 'bootstrap/dist/css/bootstrap.min.css';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import DreamerNav from "@/app/components/nav";

export default function Home() {
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
