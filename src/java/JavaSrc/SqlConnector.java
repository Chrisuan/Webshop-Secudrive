
package JavaSrc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnector {
    
    private Connection dbConnection;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Connection getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }
      
   
    public void driverMysql()
    {
        try
        {
             Class.forName("com.mysql.jdbc.Driver");
             
           
            //status = "Treiber erfolgreich geladen...";
        
        }
        catch(ClassNotFoundException e)
        {
            //status = "Fehler beim Laden des Treibers: " + e;
        }
		
		
		
    }//Ende driverMysql()
    
    public void dbConnect()
    {    	
        try
        {
        dbConnection = DriverManager.getConnection("jdbc:mysql://localhost/secudrive?" +
                                   "user=root&password=");
        
            status="Verbindung hergestellt";
        }
        catch(SQLException e)
        {
            status = "Verbindung fehlgeschlagen: " + e;
        
        }		
		
    }//Ende dbConnect()
    
    
}
