import React from "react";
import Link from "next/link";

export default function DreamerNav() {
	
	return (
		<>
			<div className={ "navi-wrapper-div" }>
				<div>
					<Link href="/" scroll={false}>Home</Link>
				</div>
				<div>
					<Link href="/remnant" scroll={false}>Remnant</Link>
				</div>
				<div>
					<Link href="/teacher" scroll={false}>Teacher</Link>
				</div>
				<div className={ "login-wrapper"}>
					<div>
						<Link href="/login" scroll={false}>로그인</Link>
					</div>
					<div className={ "ml-1"}>
						<Link href="/logout" scroll={false}>로그아웃</Link>
					</div>
				</div>
			</div>
		</>
	)
}
