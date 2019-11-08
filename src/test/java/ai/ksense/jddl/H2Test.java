package ai.ksense.jddl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class H2Test {
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:" + getClass().getSimpleName() + ";DB_CLOSE_DELAY=-1");
    }

}
