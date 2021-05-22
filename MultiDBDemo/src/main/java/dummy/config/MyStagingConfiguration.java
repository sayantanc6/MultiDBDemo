package dummy.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
		entityManagerFactoryRef = "stagingEntityManager", 
		transactionManagerRef = "stagingTransactionManager", 
		basePackages = "dummy.staging.*"
)
public class MyStagingConfiguration {
	
	@Autowired
	Environment env;

	@Bean(name = "stagingDatasource")
	@ConfigurationProperties(prefix="spring.staging-datasource")
	public DataSource stagingDatasource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
	    dataSource.setUrl(env.getProperty("spring.staging-datasource.url"));
	    dataSource.setUsername(env.getProperty("spring.staging-datasource.username"));
	    dataSource.setPassword(env.getProperty("spring.staging-datasource.password"));

	    return dataSource;
	}
	
	@Bean(name = "stagingEntityManager")
	public LocalContainerEntityManagerFactoryBean stagingEntityManager() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(stagingDatasource());
		emf.setPackagesToScan("dummy.staging.*");
		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true); 
		emf.setJpaVendorAdapter(vendorAdapter);
		final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("spring.staging-jpa.hibernate.ddl-auto", env.getProperty("spring.staging-jpa.hibernate.ddl-auto"));
        properties.put("spring.jpa.database-platform", env.getProperty("spring.jpa.database-platform"));
        properties.put("spring.staging-jpa.generate-ddl", env.getProperty("spring.staging-jpa.generate-ddl")); 
        emf.setJpaPropertyMap(properties);

        return emf;
		
	}
	
	@Bean(name = "stagingTransactionManager")
	public PlatformTransactionManager stagingTransactionManager() {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(stagingEntityManager().getObject());
        return transactionManager;
	}
}
