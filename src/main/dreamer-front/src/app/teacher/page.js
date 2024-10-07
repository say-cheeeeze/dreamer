import Link from "next/link";
import React from "react";

export default function TeacherPage() {
	
	let styleName = 'd-inline-block m-1';
	
	return (
		<>
			<div>
				<div>
					<div className={ styleName }>
						<Link href="/teacher/info">info</Link>
					</div>
				</div>
				Teacher page
			</div>
		</>
	)
}