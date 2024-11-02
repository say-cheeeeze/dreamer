import { Button } from "react-bootstrap";
import { useDaumPostcodePopup } from "react-daum-postcode";

export default function DaumPost( prop ) {
	
	const open = useDaumPostcodePopup();
	
	function onClickSearchAddress() {
		open( { onComplete : handleComplete } );
	}
	
	function handleComplete( data ) {
		
		prop.onComplete( data );
	}
	return (
		<Button variant="secondary" onClick={ onClickSearchAddress }>검색</Button>
	)
}