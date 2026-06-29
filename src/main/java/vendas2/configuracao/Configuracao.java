package vendas2.configuracao;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@Order(1)
public class Configuracao {

	@Bean(name="appName")
	public String applicationName(){
		return "Futuro sistema de vendas...(configuracao.java)";
	}

	@Value("${postgres.datasource.driverclassname}")
	private String driver;
	@Value("${postgres.datasource.url}")
	private String url;
	@Value("${postgres.datasource.username}")
	private String user;
	@Value("${postgres.datasource.password}")
	private String password;
	@Value("${postgres.jpa.database-platform}")
	private String dialect;
	@Value("${spring.datasource.hikari.pool-name}")
	private String poolName;
	@Value("${spring.datasource.hikari.maximum-pool-size}")
	private int maximumPoolSize;
	@Value("${spring.datasource.hikari.minimum-idle}")
	private int minimumIdle;
	@Value("${spring.datasource.hikari.idle-timeout}")
	private long idleTimeout;
	@Value("${spring.datasource.hikari.max-lifetime}")
	private long maxLifetime;
	@Value("${spring.datasource.hikari.connection-timeout}")
	private long connectionTimeout;

//	@Bean
//	@Primary
//	public DataSource dataSource(){
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(driver);
//        dataSource.setUrl(url);
//        dataSource.setUsername(user);
//        dataSource.setPassword(password);
//        return dataSource;
//    }

	@Bean
	@Primary
	public DataSource hikariDataSource(){
		HikariConfig config = new HikariConfig();

		// Configurações básicas de conexão
		config.setJdbcUrl(url);
		config.setUsername(user);
		config.setPassword(password);
		config.setDriverClassName(driver);

		// Configurações do pool
		config.setPoolName(poolName);
		config.setMaximumPoolSize(maximumPoolSize);
		config.setMinimumIdle(minimumIdle);
		config.setIdleTimeout(idleTimeout);     // 30 seg
		config.setMaxLifetime(maxLifetime);    // 1 minutos
		config.setConnectionTimeout(connectionTimeout); // 30 segundos
		return new HikariDataSource(config);
	}
}
