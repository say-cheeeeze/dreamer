import DreamerNav from "@/app/components/nav";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import { Suspense } from "react";
import LoadingSpinner from "@/app/components/LoadingSpinner";

export default function RemnantLayout({children}){
	return (
		
		<>
			<Container fluid className={ 'min-height-100vh'}>
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
			<div className={"footer-wrapper"}>
				<div className={"footer-div1"}>Footer</div>
			</div>
		</>
	)
}