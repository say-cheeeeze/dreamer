import React from "react";
import Link from "next/link";

export default function DreamerNav() {
	
	let styleName = 'd-inline-block m-1';
	
	return (
		<>
			<div>
				<div className={ styleName }>
					<Link href="/">Home</Link>
				</div>
				<div className={ styleName }>
					<Link href="/remnant">Remnant</Link>
				</div>
				<div className={ styleName }>
					<Link href="/teacher">Teacher</Link>
				</div>
			</div>
		</>
	)
}
