package hr.ja.st.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "hr.ja.st",
        repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class
)
public class DataTablesConfiguration {
}
