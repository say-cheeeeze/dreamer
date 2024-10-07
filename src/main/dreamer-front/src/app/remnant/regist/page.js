import Container from "react-bootstrap/Container";
import MyButton from "@/app/components/MyButton";
import MyForm from "@/app/components/MyForm";

export default function page() {
	return (
		<>
			<Container>
				<h3>Regist Remnant</h3>
				<div className={ 'text-align-right' }>
					<MyButton link='/remnant'
					          color='primary'
					          text='취소'
					/>
				</div>
				<div>
					<MyForm></MyForm>
				</div>
			</Container>
		</>
)
}