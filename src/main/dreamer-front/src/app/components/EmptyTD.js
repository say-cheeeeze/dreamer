export default function EmptyTD( prop ) {
	
	return (
		<td colSpan={ prop.colSpan } style={ { textAlign : "center", verticalAlign : "middle", height : "130px" } }>
			조회내역이 없습니다.
		</td>
	)
}