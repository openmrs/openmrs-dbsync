/*
 * Add Copyright
 */
package org.openmrs.eip;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.openmrs.eip.app.management.config.Constants;
import org.openmrs.eip.component.common.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Custom TestExecutionListener that resets all tables in the database by deleting all rows in them.
 * Typically, this listener should be configured to run after every test method has executed
 */
public class DeleteDataTestExecutionListener extends AbstractTestExecutionListener {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private static final String DELETE = "DELETE FROM ";
	
	private static final String DISABLE_KEYS = "SET FOREIGN_KEY_CHECKS=0";
	
	private static final String ENABLE_KEYS = "SET FOREIGN_KEY_CHECKS=1";
	
	/**
	 * @see AbstractTestExecutionListener#afterTestMethod(TestContext)
	 */
	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		ApplicationContext ctx = testContext.getApplicationContext();
		DataSource dataSource = ctx.getBean(Constants.MGT_DATASOURCE_NAME, DataSource.class);
		
		log.debug("Deleting all data from management DB tables...");
		try (Connection c = dataSource.getConnection()) {
			deleteAllData(c);
		}
		
		dataSource = ctx.getBean(CommonConstants.OPENMRS_DATASOURCE_NAME, DataSource.class);
		
		log.debug("Deleting all data from OpenMRS DB tables...");
		
		try (Connection c = dataSource.getConnection()) {
			deleteAllData(c);
		}
		
	}
	
	/**
	 * Resets all tables in the test database by deleting all the rows in them
	 *
	 * @param connection JDBC Connection object
	 */
	private void deleteAllData(Connection connection) throws SQLException {
		List<String> tables = getTableNames(connection);
		Statement statement = connection.createStatement();
		try {
			statement.execute(DISABLE_KEYS);
			for (String tableName : tables) {
				statement.executeUpdate(DELETE + tableName);
			}
		}
		finally {
			if (statement != null) {
				statement.execute(ENABLE_KEYS);
				statement.close();
			}
		}
	}
	
	private static List<String> getTableNames(Connection connection) throws SQLException {
		DatabaseMetaData dbmd = connection.getMetaData();
		ResultSet tables = dbmd.getTables(null, null, null, new String[] { "TABLE" });
		List<String> tableNames = new ArrayList<>();
		while (tables.next()) {
			tableNames.add(tables.getString("TABLE_NAME"));
		}
		return tableNames;
	}
	
}
