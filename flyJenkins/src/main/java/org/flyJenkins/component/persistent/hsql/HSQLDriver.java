/**
 * Persistent Driver for hsql
 * @author agun
 */
package org.flyJenkins.component.persistent.hsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.flyJenkins.component.persistent.PersistentDriver;

/**
 * TODO refactoring
 * @author agun
 *
 */
public class HSQLDriver implements PersistentDriver {
	
	Connection conn = null; 
	Statement st = null;
	
	@Override
	public void initDevice() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection("jdbc:hsqldb:file:testdb", "sa", "");
			st = conn.createStatement();    // statements
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void initSchema() {
		int i;
		try {
			i = st.executeUpdate("CREATE TABLE productions ( id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL, job_id INTEGER, job_name VARCHAR(255), build INTEGER, output VARCHAR(255), create_at TIMESTAMP DEFAULT now() )");
			if (i == -1) {
		            System.out.println("db error : can't  create table productions " );
			 }
			
			i = st.executeUpdate("CREATE TABLE serviceGroups ( id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL, group_name VARCHAR(255), create_at TIMESTAMP DEFAULT now() )");
			if (i == -1) {
		            System.out.println("db error : can't  create table serviceGroups " );
			 }
			
			i = st.executeUpdate("CREATE TABLE serviceMetas ( id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL, group_id INTEGER, host VARCHAR(255), destination VARCHAR(255), service_type INTEGER, command VARCHAR(255), weight INTEGER, service_name VARCHAR(255), test_url VARCHAR(255),  create_at TIMESTAMP DEFAULT now() )");
			if (i == -1) {
		            System.out.println("db error : can't  create table serviceMetas " );
			 }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}         
		
	}

	public Connection getConn() {
		return conn;
	}

	public Statement getStatement() {
		try {
			return conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
