/**
 * Persistent Driver interface
 * @author agun
 */

package org.flyJenkins.component.persistent;

import java.sql.Connection;
import java.sql.Statement;

public interface PersistentDriver {
	public void initDevice();
	public void initSchema();
	public Connection getConn();
	public Statement getStatement();
}
