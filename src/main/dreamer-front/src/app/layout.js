import "@/app/globals.css"
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/css/common.css'
export const metadata = {
	title       : "Dreamer",
	description : "초등문화 캠프를 위한 모임",
};

export default function RootLayout( { children } ) {
	return (
		<html lang="en">
			<body>
				<main>
					{ children }
				</main>
			</body>
		</html>
	);
}
