import axios from "axios";
import { StatusCodes } from "http-status-codes";
import { useRouter } from "next/navigation";

const $axios = () => {
	const router = useRouter();
	const _axios = axios.create();
	
	_axios.interceptors.request.use(
		res => {
			return res;
		},
		error => {
			console.log( error );
			return Promise.reject( error );
		}
	)
	
	_axios.interceptors.response.use(
		res => {
			return res;
		},
		error => {
			
			const status = error.response ? error.response.status : null;
			
			if ( StatusCodes.UNAUTHORIZED === status ) {
				router.push( '/login' );
			}
			// 에러를 계속 처리
			return Promise.reject( error );
		},
	);
	return _axios;
};

export default $axios;
