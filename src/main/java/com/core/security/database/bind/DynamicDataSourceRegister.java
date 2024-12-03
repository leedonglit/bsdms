package com.core.security.database.bind;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashMap;
import java.util.Map;

public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware{

	//如配置文件中未指定数据源类型，使用该默认值
	private static final Object DATASOURCE_TYPE_DEFAULT = "com.alibaba.druid.pool.DruidDataSource";

	// 默认数据源
	private DruidDataSource defaultDataSource;

	//其他数据源
	private Map<String, DruidDataSource> customDataSources = new HashMap<>();

	/**
	 * 加载多数据源配置
	 */
	public void setEnvironment(Environment environment) {
		initDefaultDataSource(environment);
		initCustomDataSources(environment);
	}

	/**
	 * 加载主数据源配置.
	 * @param env
	 */
	public void initDefaultDataSource(Environment env){
		// 读取主数据源
		//RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
		Map<String, Object> dsMap = new HashMap<>();
		dsMap.put("driverClassName", env.getProperty("jdbc.driver"));
		dsMap.put("url", env.getProperty("jdbc.url"));
		dsMap.put("username", env.getProperty("jdbc.username"));
		dsMap.put("password", env.getProperty("jdbc.password"));
		DynamicDataSourceContextHolder.dbType.put("datasource", env.getProperty("jdbc.type"));
		//创建数据源;
		defaultDataSource = buildDataSource(dsMap);
		Binder dataBinder = Binder.get(env);
		dataBinder.bind(env.getProperty("datasource.main.dbname"), Bindable.ofInstance(defaultDataSource));
	}

	/**
	 * 初始化更多数据源
	 */
	private void initCustomDataSources(Environment env) {
		// 读取配置文件获取更多数据源，也可以通过defaultDataSource读取数据库获取更多数据源
		//RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "custom.datasource.");
		Binder dataBinder = null;
		String dsPrefix = env.getProperty("more.datasource.names");
		if (StringUtils.isEmpty(dsPrefix)){
			return;
		}
		for (String dp : dsPrefix.split(",")) {// 多个数据源
			Map<String, Object> dsMap = new HashMap<>();//propertyResolver.getSubProperties(dsPrefix + ".");
			dsMap.put("driverClassName", env.getProperty(dp + ".jdbc.driver"));
			dsMap.put("url", env.getProperty(dp + ".jdbc.url"));
			dsMap.put("username", env.getProperty(dp + ".jdbc.username"));
			dsMap.put("password", env.getProperty(dp + ".jdbc.password"));
			DynamicDataSourceContextHolder.dbType.put(dp, env.getProperty(dp + ".jdbc.type"));
			DruidDataSource ds = buildDataSource(dsMap);
			customDataSources.put(dp, ds);
			dataBinder = Binder.get(env);
			dataBinder.bind(dp, Bindable.ofInstance(ds));
		}
	}

	/**
	 * 创建datasource.
	 * @param dsMap
	 * @return
	 */
	public DruidDataSource buildDataSource(Map<String, Object> dsMap) {
		Object type = dsMap.get("type");
		if (type == null){
			type = DATASOURCE_TYPE_DEFAULT;// 默认DataSource
		}
		String driverClassName = dsMap.get("driverClassName").toString();
		String url = dsMap.get("url").toString();
		String username = dsMap.get("username").toString();
		String password = dsMap.get("password").toString();
		DruidDataSource datasource = new DruidDataSource();  
		datasource.setUrl(url);
		datasource.setUsername(username);
		datasource.setPassword(password);
		datasource.setDriverClassName(driverClassName);
		return datasource;
	}

	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		// 将主数据源添加到更多数据源中
		targetDataSources.put("dataSource", defaultDataSource);
		DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
		// 添加更多数据源
		targetDataSources.putAll(customDataSources);
		for (String key : customDataSources.keySet()) {
			DynamicDataSourceContextHolder.dataSourceIds.add(key);
		}

		// 创建DynamicDataSource
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(DynamicDataSource.class);
		beanDefinition.setSynthetic(true);
		MutablePropertyValues mpv = beanDefinition.getPropertyValues();
		//添加属性：AbstractRoutingDataSource.defaultTargetDataSource
		mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
		mpv.addPropertyValue("targetDataSources", targetDataSources);
		registry.registerBeanDefinition("dataSource", beanDefinition);
	}
}
