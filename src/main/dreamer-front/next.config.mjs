/** @type {import('next').NextConfig} */
const nextConfig = {
    // async headers() {
    //     return [
    //         {
    //             // matching all API routes
    //             source  : "/api/:path*",
    //             headers : [
    //                 { key : "Access-Control-Allow-Credentials", value : "true" },
    //                 { key : "Access-Control-Allow-Origin", value : "http://localhost:8080/api:path*" }, // replace this your actual origin
    //                 { key : "Access-Control-Allow-Methods", value : "GET,DELETE,PATCH,POST,PUT" },
    //                 { key     : "Access-Control-Allow-Headers",
    //                     value : "X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, X-Api-Version"
    //                 },
    //             ]
    //         }
    //     ]
    // },
    
	async rewrites() {
        return [
            {
                source : '/api/:path*',
                destination : 'http://localhost:8080/api/:path*'
            },
            {
                source      : '/images/:path*', // image 저장 uri 를 spring 에서 활용하기 위해.
                destination : 'http://localhost:8080/images/:path*'
            }
        ]
    },
    
    env : {
        // local3000 : 'http://localhost:3000' not use ...
    }
};

export default nextConfig;
