
package JavaSrc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class Customer {
    
    //-----Kunden-Attribute bilden Spalten aus DB ab-----------
    private String CAlias;
    private String CPwd;
    private String CFirstName;
    private String CLastName;
    private String CStreetHNr;
    private String CZipCode;
    private String CCity;
    private String CEmail;
    private int CAccessLevel=0;
    private int CID;
    
    //Status-Attribute eines Kunden-Objekts
    private boolean loggedIn=false;
    private boolean registered=false;
    private boolean validatedCustomer=false;
    
    
    //Attribute zur Datenbank-Anbindung
    private SqlConnector con = new SqlConnector();
    private String sqlString;
    private Statement sqlStatement;
    private ResultSet resultSet;
    
    //Sonstige Klassenattribute
    private String error;
    private boolean errorOccured=false;
    
    
    //-----Getter- und Setter-Methoden

    public String getCAlias() {
        return CAlias;
    }

    public void setCAlias(String CAlias) {
        this.CAlias = CAlias;
    }

    public String getCPwd() {
        return CPwd;
    }

    public void setCPwd(String CPwd) {
        this.CPwd = CPwd;
    }

    public String getCFirstName() {
        return CFirstName;
    }

    public void setCFirstName(String CFirstName) {
        this.CFirstName = CFirstName;
    }

    public String getCLastName() {
        return CLastName;
    }

    public void setCLastName(String CLastName) {
        this.CLastName = CLastName;
    }

    public String getCStreetHNr() {
        return CStreetHNr;
    }

    public void setCStreetHNr(String CStreetHNr) {
        this.CStreetHNr = CStreetHNr;
    }

    public String getCZipCode() {
        return CZipCode;
    }

    public void setCZipCode(String CZipCode) {
        this.CZipCode = CZipCode;
    }

    public String getCCity() {
        return CCity;
    }

    public void setCCity(String CCity) {
        this.CCity = CCity;
    }

    public String getCEmail() {
        return CEmail;
    }

    public void setCEmail(String CEmail) {
        this.CEmail = CEmail;
    }

    public int getCAccessLevel() {
        return CAccessLevel;
    }

    public void setCAccessLevel(int CAccessLevel) {
        this.CAccessLevel = CAccessLevel;
    }

    public int getCID() {
        return CID;
    }

    public void setCID(int CID) {
        this.CID = CID;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSqlString() {
        return sqlString;
    }

    public void setSqlString(String sqlString) {
        this.sqlString = sqlString;
    }

    public boolean isValidatedCustomer() {
        return validatedCustomer;
    }

    public void setValidatedCustomer(boolean validatedCustomer) {
        this.validatedCustomer = validatedCustomer;
    }

    public boolean isErrorOccured() {
        return errorOccured;
    }

    public void setErrorOccured(boolean errorOccured) {
        this.errorOccured = errorOccured;
    }
    
    
    
    
    
    
    /**
     * Überprüft die eingegebenen Login-Daten
     * @param CAlias Benutzername
     * @param CPwd Passwort
     * @return Ein Kundenobjekt, das entweder validiert ist oder nicht. Dies
     * geschieht über den Boolschen Wert validatedCustomer
     */
    public Customer checkLogin(String CAlias, String CPwd)
    {
        con.driverMysql();
        con.dbConnect();
        try
          {
            /*if(resultSet!=null)
                resultSet.close();*/
            sqlStatement=con.getDbConnection().createStatement();
            sqlString="SELECT * FROM customer WHERE CAlias = " + "'" + CAlias + "'" + " AND CPwd = " + "'" + CPwd + "';";
            resultSet=sqlStatement.executeQuery(sqlString);
            sqlString=null;
          }
        catch (Exception ex)
          {
              error=ex.toString();
              
          }
        try
        {
            if(resultSet.first())
            {
                this.setCFirstName(resultSet.getString("CFirstName"));
                this.setCLastName(resultSet.getString("CLastName"));
                this.setCStreetHNr(resultSet.getString("CStreetHnr"));
                this.setCZipCode(resultSet.getString("CZipCode"));
                this.setCCity(resultSet.getString("CCity"));
                this.setCEmail(resultSet.getString("CEmail"));
                this.setCID(resultSet.getInt("CID"));
                this.setCAccessLevel(resultSet.getInt("CAccessLevel"));
                this.setLoggedIn(true);
                this.setRegistered(true);
                this.setValidatedCustomer(true);
            }
            else
            {
                this.setLoggedIn(false);
                this.setValidatedCustomer(false);
            }
        } catch (Exception ex)
        {
            error=ex.toString();
        }
        
        
        try 
        {
            con.getDbConnection().close();
            
        } catch (SQLException ex) 
        {
            error=ex.toString();
        }
        return this;  
  }//Ende checkLogin()
    
    /**
     * Ruft zunächst die Methode isDuplicate() auf. Je nach deren Rückgabe
     * wird das Anlegen eines neuen Accounts eingeleitet oder eine Fehlermeldung
     * generiert.
     * @return Gibt zurück, ob der Benutzeraccount erfolgreich angelegt werden
     * konnte.
     */
    public boolean createNewCustomerAccount()
   {
       errorOccured=false;
       error=null;
       boolean registrationSuccess;
       
       try
       {
           if(isDuplicate(this.getCAlias(), 
                   this.getCEmail()))
           {
               error="Benutzername oder E-Mail Adresse bereits vergeben";
               errorOccured=true;
               registrationSuccess=false;
               
           }
           else
           {
               //Aufruf der Methode addToDB() mit Kundenparametern
               addToDB(this.getCAlias(), this.getCPwd(),
               this.getCFirstName(), this.getCLastName(),
               this.getCStreetHNr(), this.getCZipCode(),
               this.getCCity(), this.getCEmail());
               registrationSuccess=true;
               this.setRegistered(true);
               
           }
           
       
       }
       catch(Exception ex)
       {
           errorOccured=true;
           registrationSuccess=false;
       }
       
       return registrationSuccess;
       
    }//Ende createNewCustomerAccount
    
    /**
     * Prüft, ob Benutzername oder E-Mail bereits in der Datenbank sind.
     * @param CAlias Benutzername
     * @param CEmail E-Mail
     * @return Ist Duplikat oder nicht
     */
    private boolean isDuplicate(String CAlias, String CEmail)
    {
        boolean isDuplicate=false;
        con.driverMysql();
        con.dbConnect();
        
        try
        {
            sqlStatement=con.getDbConnection().createStatement();
            sqlString="SELECT * FROM customer WHERE CAlias like " 
                    + "'" + CAlias + "' OR CEmail like '" + CEmail + "';";
            resultSet=sqlStatement.executeQuery(sqlString);
            
            if(resultSet.first())
                isDuplicate=true;
            
            con.getDbConnection().close();
        }
        catch(Exception ex)
        {
            error=ex.toString();
        }
        return isDuplicate;
    }//Ende isDuplicate()
    
    
    /**
     * Schreibt die im Formular eingetragenen Parameter als neuen Datensatz
     * in die Datenbank.
     * @param CAlias Benutzername
     * @param CPwd Passwort
     * @param CFirstName Vorname
     * @param CLastName Nachname
     * @param CStreet Strasse und Hausnummer
     * @param CZipCode PLZ
     * @param CCity Stadt
     * @param CEmail E-Mail
     */
    private void addToDB(String CAlias, String CPwd, String CFirstName,
            String CLastName, String CStreet, String CZipCode, String CCity,
            String CEmail)
    {
        con.driverMysql();
        con.dbConnect();
        
        try 
        {
            sqlStatement=con.getDbConnection().createStatement();
            sqlString="INSERT INTO customer (CAlias, CFirstName, CLastName, CPwd, "
                    + "CStreetHNr, CZipCode, CCity, CEmail, CAccessLevel)"
                    + " VALUES"
                    + "('" + CAlias + "', '" + CFirstName + "', '" + CLastName
                    + "', '" + CPwd + "', '" + CStreet + "', '" 
                    + CZipCode + "', '" + CCity + "', '" + CEmail + "', 1);";
            
            sqlStatement.executeUpdate(sqlString);
            sqlString=null;
            con.getDbConnection().close();
        } 
        catch (Exception ex) 
        {
            error=ex.toString();
        }
    }//Ende addToDB()
    
    /**Editiert Kunden-Datensätze in der Datenbank aus View myAccount
     */
    public void updateCustomerData()
    {
        con.driverMysql();
        con.dbConnect();
        
        try {
            sqlStatement=con.getDbConnection().createStatement();
            sqlString="UPDATE customer SET CAlias='" + CAlias 
                    + "', CFirstName='" + CFirstName + "', CLastName='" 
                    + CLastName + "', CPwd='" + CPwd + "', CStreetHNr='"
                    + CStreetHNr + "', CZipCode='" + CZipCode 
                    + "', CCity='" + CCity + "', CEmail='" + CEmail 
                    + "' WHERE CID=" + CID + ";";
            sqlStatement.executeUpdate(sqlString);
            sqlString=null;
            con.getDbConnection().close();
            
        } catch (Exception ex) {
            error=ex.toString();
        }
    }//Ende updateCustomerData()
}//Ende Customer
