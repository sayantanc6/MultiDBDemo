package dummy.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(
		entityManagerFactoryRef = "actualEntityManager", 
		transactionManagerRef = "actualTransactionManager", 
		basePackages = "dummy.actual.*"
)
public class MyActualConfiguration {

	@Autowired
	Environment env;

	@Bean(name = "actualDatasource")
	@Primary
	@ConfigurationProperties(prefix="spring.actual.datasource")
	public DataSource actualDatasource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
	    dataSource.setUrl(env.getProperty("spring.actual-datasource.url"));
	    dataSource.setUsername(env.getProperty("spring.actual-datasource.username"));
	    dataSource.setPassword(env.getProperty("spring.actual-datasource.password"));

	    return dataSource;
	}
	
	@Bean(name = "actualEntityManager")
	@Primary
	public LocalContainerEntityManagerFactoryBean actualEntityManager() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(actualDatasource());
		emf.setPackagesToScan("dummy.actual.*");
		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		emf.setJpaVendorAdapter(vendorAdapter);
		final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("spring.actual-jpa.hibernate.ddl-auto", env.getProperty("spring.actual-jpa.hibernate.ddl-auto"));
        properties.put("spring.jpa.database-platform", env.getProperty("spring.jpa.database-platform"));
        properties.put("spring.actual-jpa.generate-ddl", env.getProperty("spring.actual-jpa.generate-ddl")); 
        emf.setJpaPropertyMap(properties);

        return emf;
	}
	
	@Bean(name = "actualTransactionManager")
	@Primary
	public PlatformTransactionManager actualTransactionManager() {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(actualEntityManager().getObject());
        return transactionManager;
	}
}
