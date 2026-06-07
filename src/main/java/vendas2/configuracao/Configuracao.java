package vendas2.configuracao;

import javax.sql.DataSource;

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

	@Bean
	@Primary
	public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

}
