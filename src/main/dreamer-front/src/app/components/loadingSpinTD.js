import LoadingSpinner from "@/app/components/LoadingSpinner";

export default function LoadingSpinTD( prop ) {
	
	return (
		<td colSpan={ prop.colSpan } style={ { textAlign : "center", verticalAlign : "middle", height : "130px" } }>
			<LoadingSpinner/>
		</td>
	)
}