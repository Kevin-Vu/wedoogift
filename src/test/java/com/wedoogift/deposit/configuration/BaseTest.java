package com.wedoogift.deposit.configuration;
import org.junit.ClassRule;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Base test class that must be extended in order to run test with embedded database support
 */
@ContextConfiguration(initializers = BaseTest.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BaseTest {

	private static final String DB_NAME_DATA = "wedoogift";
	private static final String DB_USER = "postgress";
	private static final String DB_PASS = "password";

	/**
	 * Instantiate a new postgresql container for testing purposes
	 */
	@ClassRule
	public static PostgreSQLContainer postgreSQLContainerData = (PostgreSQLContainer) new PostgreSQLContainer("postgres:11.1")
			.withInitScript("dump_challenge.sql")
			.withDatabaseName(DB_NAME_DATA)
			.withUsername(DB_USER)
			.withPassword(DB_PASS);

	/**
	 * Bind current container configuration to application.properties
	 */
	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of(
					"spring.datasource.data.jdbc-url=" + postgreSQLContainerData.getJdbcUrl(),
					"spring.datasource.data.username=" + postgreSQLContainerData.getUsername(),
					"spring.datasource.data.password=" + postgreSQLContainerData.getPassword()
			).applyTo(configurableApplicationContext.getEnvironment());
		}
	}
}
