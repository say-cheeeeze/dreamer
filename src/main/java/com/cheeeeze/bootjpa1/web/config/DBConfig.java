package com.cheeeeze.bootjpa1.web.config;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @methodName   : Hikari setting
 * @author       : 남윤재
 * @date         : 2024-10-07
 * @description  : application.yml 을 읽는다.
 */
@Configuration
public class DBConfig {
	
	@Bean
	@ConfigurationProperties( "spring.datasource.hikari" )
	public HikariConfig hikariConfig() {
		HikariConfig hikariConfig = new HikariConfig();
		
		// 수정이 필요한 경우에 세팅한다.
//		hikariConfig.setConnectionTimeout(...);
//		hikariConfig.setMaximumPoolSize( ... );
		
		return hikariConfig;
	}
	
	/**
	 * @methodName   : dataSource
	 * @author       : 남윤재
	 * @date         : 2024-10-07
	 * @description  : 위 설정 정보를 파라미터로 받아서 DataSource 로 쓰겠다.
	 */
	@Bean
	public DataSource dataSource( HikariConfig hikariConfig ) {
		HikariDataSource dataSource = new HikariDataSource( hikariConfig );
		return dataSource;
	}
}
