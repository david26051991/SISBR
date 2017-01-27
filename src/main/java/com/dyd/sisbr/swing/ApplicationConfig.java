package com.dyd.sisbr.swing;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@EnableAutoConfiguration
@ComponentScan(basePackages = "com.dyd.sisbr.*")
@MapperScan(value={"com.dyd.sisbr.dao"})
public class ApplicationConfig {

	@Bean
	public Main main() {
		return new Main();
	}

//	@Bean
//	public static DataSource dataSource() {
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//		dataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=dbsearchdocument");
//		dataSource.setUsername("sa");
//		dataSource.setPassword("123456");
//		return dataSource;
//	}

	@Bean(name = "sqlSessionFactoryName")
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:com/dyd/sisbr/dao/maps/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

}
