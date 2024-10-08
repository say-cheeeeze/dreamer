'use client'
import { Button, Form } from "react-bootstrap";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import { useState } from "react";

export default function MyForm() {
	
	const [ name, setName ] = useState( '' );
	const [ sex, setSex ] = useState( '' );
	const [ grade, setGrade ] = useState( '5' );
	
	let minWidth = 'min-width-5rem';
	
	function onChangeName( e ) {
		setName( e.target.value )
	}
	
	function onChangeSex( e ) {
		setSex( e.target.value )
	}
	
	function onChangeGrade( e ) {
		setGrade( e.target.value )
	}
	
	async function onSaveRemnant() {
		
		let url = '/api/remnant/regist'
		let param = {
			headers : {
				'content-Type' : 'application/json',
			},
			method : 'post',
			body   : JSON.stringify( {
				name,sex,grade
			} )
		};
		
		let returnVal = await fetch( url, param );
		returnVal = await returnVal.json()
		
		console.log( returnVal );
	}
	
	return (
		<>
			<div>
				name : { name }
				sex : { sex }
				grade : { grade }
			</div>
			<div>
				<div className={ 'm-3' }>
					<div className={ 'd-inline-block ' + minWidth }>
						<span>이름</span>
						<span className={ 'red' }>*</span>
					</div>
					<div className={ 'd-inline-block' }>
						<Form.Control type="text" placeholder=""
						              required
						              name={ name }
						              onChange={ onChangeName }/>
					</div>
				</div>
				<div className={ 'm-3' }>
					<div className={ 'd-inline-block ' + minWidth }>
						<span>성별</span>
						<span>*</span>
					</div>
					<div className={ 'd-inline-block' }>
						<Form.Check inline required onChange={ onChangeSex }
						            label="남"
						            value={ 'male' }
						            name={ sex }
						            type={ 'radio' }
						            id={ `inline-radio-1` }
						/>
						<Form.Check inline required onChange={ onChangeSex }
						            label="여"
						            value={ 'female' }
						            name={ sex }
						            type={ 'radio' }
						            id={ `inline-radio-2` }
						/>
					</div>
				</div>
				<div className={ 'm-3' }>
					<div className={ 'd-inline-block ' + minWidth }>
						<span>학년</span>
						<span>*</span>
					</div>
					<div className={ 'd-inline-block' }>
						<Form.Select required
						             name={grade}
						             value={ grade } aria-label="Default select example" onChange={ onChangeGrade }>
							<option value={ '' }>--선택--</option>
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
						</Form.Select>
					</div>
				</div>
				<Button variant="primary" type="button" onClick={ onSaveRemnant }>
					Submit
				</Button>
			</div>
		</>
	)
}