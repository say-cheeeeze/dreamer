import { Button, Modal } from "react-bootstrap";

export default function AlertModal(prop) {
	return (
		<Modal show={ prop.show } onHide={ prop.onHide }>
			<Modal.Header closeButton>
				<Modal.Title>알림</Modal.Title>
			</Modal.Header>
			<Modal.Body>{ prop.body }</Modal.Body>
			<Modal.Footer>
				<Button variant="secondary" onClick={ prop.onHide }>
					닫기
				</Button>
			</Modal.Footer>
		</Modal>
		)
}