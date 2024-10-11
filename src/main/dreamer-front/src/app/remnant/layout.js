import DreamerNav from "@/app/components/nav";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

export default function RemnantLayout({children}){
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
						{ children }
					</Col>
				</Row>
			</Container>
			<div style={{height:"15vw", textAlign:"center"}}>
				<div style={{height:"100%", alignContent:"center"}}>Footer</div>
			</div>
		</>
	)
}