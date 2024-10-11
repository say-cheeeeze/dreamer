import { Spinner } from "react-bootstrap";

export default function LoadingSpinTD( prop ) {
	
	return (
		<td colSpan={ prop.colSpan } style={ { textAlign : "center", verticalAlign : "middle", height : "130px" } }>
			<Spinner animation="border" role="status" variant={ "primary" }>
				<span className="visually-hidden">Loading...</span>
			</Spinner>
		</td>
	)
}