package mss.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
  entityManagerFactoryRef = "logdbEntityManagerFactory",
  transactionManagerRef = "logdbTransactionManager",
  basePackages = { "mss.domain.repository" }
)
public class LogdbConfig {
 
  @Bean(name = "logdbDataSource")
  @ConfigurationProperties(prefix = "logdb.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }
  
  @Bean(name = "logdbEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean
  logdbEntityManagerFactory(
    EntityManagerFactoryBuilder builder,
    @Qualifier("logdbDataSource") DataSource dataSource
  ) {
    return
      builder
        .dataSource(dataSource)
        .packages("mss.domain")
        .persistenceUnit("logdb")
        .build();
  }
  @Bean(name = "logdbTransactionManager")
  public PlatformTransactionManager logdbTransactionManager(
    @Qualifier("logdbEntityManagerFactory") EntityManagerFactory
    logdbEntityManagerFactory
  ) {
    return new JpaTransactionManager(logdbEntityManagerFactory);
  }
}