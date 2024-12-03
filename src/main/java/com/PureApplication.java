package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.core.security.database.bind.DynamicDataSourceRegister;
import com.core.security.filter.SecurityFilter;
import com.core.tools.PropertiesTool;
import com.core.tools.SpringContextTool;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({DynamicDataSourceRegister.class})
@EnableCaching
@EnableScheduling
public class PureApplication {

	@Bean
	public FilterRegistrationBean<SecurityFilter> filterRegistrationBean(){
		FilterRegistrationBean<SecurityFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		SecurityFilter filter = new SecurityFilter();
		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}

	public static void main(String[] args) {
		PropertiesTool.loadAllProperties("config/config.properties");
		ApplicationContext applicationContext = SpringApplication.run(PureApplication.class, args);  
		SpringContextTool.setContext(applicationContext);
	}
}
