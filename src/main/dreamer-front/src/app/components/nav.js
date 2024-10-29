import React from "react";
import Link from "next/link";

export default function DreamerNav() {
	
	return (
		<>
			<div className={ "navi-wrapper-div" }>
				<div>
					<Link href="/">Home</Link>
				</div>
				<div>
					<Link href="/remnant">Remnant</Link>
				</div>
				<div>
					<Link href="/teacher">Teacher</Link>
				</div>
				<div className={ "login-wrapper"}>
					<div>
						<Link href="/login">로그인</Link>
					</div>
					<div className={ "ml-1"}>
						<Link href="/logout">로그아웃</Link>
					</div>
				</div>
			</div>
		</>
	)
}
