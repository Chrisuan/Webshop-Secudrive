
package JavaSrc;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.Part;
import java.util.LinkedList;

public class Article {
    
    //-------Artikel Attribute bilden Datenbank-Spalten ab
    private int AID;
    private double APrice;
    private String ACategory;
    private String AName;
    private boolean AinDB=false;
    
   
    
    //Image-Verwaltung
    private Part UploadedFile;//Benutzer-Upload aus Formular
    private File uploads= new File("../docroot/resources");//Zielordner auf Server festlegen
    private File AImgOnServer;
    private String AUrl;
    private InputStream input;
    
    private String error;
    private boolean updateSuccess=false;
    
    
    //Datenbankzugriff
    private String sqlString;
    private SqlConnector con = new SqlConnector();
    private Statement sqlStatement;
    private ResultSet articleData;
    private ResultSet resultSet;
    
    private LinkedList<Article> aList= new LinkedList<Article>();
    
   
    
    //------Getter und Setter----------------

    public int getAID() {
        return AID;
    }

    public void setAID(int AID) {
        this.AID = AID;
    }

    public double getAPrice() {
        return APrice;
    }

    public void setAPrice(double APrice) {
        this.APrice = APrice;
    }

    public String getACategory() {
        return ACategory;
    }

    public void setACategory(String ACategory) {
        this.ACategory = ACategory;
    }

    public String getAName() {
        return AName;
    }

    public void setAName(String AName) {
        this.AName = AName;
    }

    public boolean isAinDB() {
        return AinDB;
    }

    public void setAinDB(boolean AinDB) {
        this.AinDB = AinDB;
    }

    public Part getUploadedFile() {
        return UploadedFile;
    }

    public void setUploadedFile(Part UploadedFile) {
        this.UploadedFile = UploadedFile;
    }

    public File getUploads() {
        return uploads;
    }

    public void setUploads(File uploads) {
        this.uploads = uploads;
    }

    public File getAImgOnServer() {
        return AImgOnServer;
    }

    public void setAImgOnServer(File AImgOnServer) {
        this.AImgOnServer = AImgOnServer;
    }

    public String getAUrl() {
        return AUrl;
    }

    public void setAUrl(String AUrl) {
        this.AUrl = AUrl;
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

    public LinkedList<Article> getaList() {
        return aList;
    }

    public void setaList(LinkedList<Article> aList) {
        this.aList = aList;
    }

    public boolean isUpdateSuccess() {
        return updateSuccess;
    }

    public void setUpdateSuccess(boolean updateSuccess) {
        this.updateSuccess = updateSuccess;
    }

   
    
    
    private void setProperties(String aname, double aprice, String aurl, int aid){
    
       this.setAName(aname);
       this.setAPrice(aprice);
       this.setAUrl(aurl);
       this.setAID(aid);
    }
    
    
    //--------------Action-Methoden-------------------
    /**
     * Artikel-Bild wird vom Client auf den Server übertragen
     *  
     */
    public void uploadArticleImage() 
    {
        try{
            input=this.getUploadedFile().getInputStream();
            this.setAImgOnServer(new File(this.getUploads(),
                getSubmittedFileName(this.getUploadedFile())));
        
            Files.copy(input, this.getAImgOnServer().toPath());
            this.setAName(this.getSubmittedFileName(UploadedFile));
        }
        catch(Exception ex)
        {
            error=ex.toString();
        }
       
        /*Fehler!!!: this.setAName(this.getAImgOnServer().getName().substring(0, 
                (this.getAImgOnServer().getName().length()) - 4));*/
        
    }//Ende uploadArticleImage()    
    
    
    
    /**
     * Workaround für nicht abrufbare Methode getSubmittedFileName() aus 
     * javax.servlet.http.Part
     * @param filePart
     * @return 
     */
    public String getSubmittedFileName(Part filePart)
    {
        String header = filePart.getHeader("content-disposition");
        for(String headerPart : header.split(";"))
        {
            if(headerPart.trim().startsWith("filename"))
            {
            return headerPart.substring(headerPart.indexOf('=') + 1).trim()
                             .replace("\"", "").replace("#", "");
            }
        }
        return null;
    }//Ende getSubmittedFileName()
    
    /**
     * Schreibt neuen Artikel in die Datenbank
     */
    public void saveArticleInDB()
    {
     
        con.driverMysql();
        con.dbConnect();
        AUrl = this.getAImgOnServer().getName();
        
        try
        {
            sqlStatement=con.getDbConnection().createStatement();
            sqlString = "INSERT INTO article(AName, APrice, APicURL) "
                    + "VALUES"
                    + "('" + AName + "', " + APrice + ", '" + AUrl + "');";
            sqlStatement.executeUpdate(sqlString);
            this.setAinDB(true); 
            con.getDbConnection().close();
        }
        catch(Exception ex)
        {
            error=ex.toString();
        }
       
    }//Ende saveArticle()
    
    
    /**
     * Lädt alle Artikel-Datensätze und schreibt diese in eine verkettete Liste
     * @return Verkettete Liste von Artikeln
     */
    public LinkedList<Article> getArticleList()
    {
       
        con.driverMysql();
        con.dbConnect();
        try
        {
            /*if(resultSet!=null)
                resultSet.close();*/
            
            sqlStatement=con.getDbConnection().createStatement();
            sqlString="SELECT * FROM article;";
            resultSet=sqlStatement.executeQuery(sqlString);
           
        }
        catch(Exception ex)
        {
            error=ex.toString();
        }
        
        //Schreiben von resultSet in aList
        
        try
        {
            
            while(resultSet.next())
            {
                Article a = new Article();
                a.setProperties(resultSet.getString("AName"),
                        Double.parseDouble(resultSet.getString("APrice")), 
                        resultSet.getString("APicURL"),
                        resultSet.getInt("AID"));
                aList.add(a);
            }
           
        }
        catch(Exception ex)
        {
            error=ex.toString();
        }
        return aList;
    }//Ende getArticleList()
    
    /**
     * Lädt Artikeldetails eines einzelnen Artikels aus der Datenbank
     * @param id Artikelnummer aus GET-Anfrage
     * @return Gibt das Artikelobjekt mit der angegebenen Artikelnummer zurück
     */
    public Article getArticleData(int id)
    {
        con.driverMysql();
        con.dbConnect();
        Article a = new Article();
        
        try
        {
            sqlStatement=con.getDbConnection().createStatement();
            sqlString= "SELECT * FROM article WHERE AID=" + id + ";";
            resultSet=sqlStatement.executeQuery(sqlString);
            if(resultSet.first())
            {
                
                a.setProperties(resultSet.getString("AName"),
                        Double.parseDouble(resultSet.getString("APrice")), 
                        resultSet.getString("APicURL"),
                        resultSet.getInt("AID"));
            }
            
        }
        catch(Exception ex)
        {
            error=ex.toString();
        }
        
        return a;
        
    }//Ende getArticleData()
    
    /**
     * Führt ein SQL-Update mit editierten Artikeldetails des Views aus
     */
    public void updateArticle()
    {
        con.driverMysql();
        con.dbConnect();
        if(UploadedFile!=null)
            this.AUrl=getSubmittedFileName(UploadedFile);
        
        try
        {
            sqlStatement=con.getDbConnection().createStatement();
            sqlString = "UPDATE article SET AName='" + AName 
                    + "', APrice=" + APrice + ", APicURL='" + AUrl 
                    + "' WHERE AID=" + AID + ";";
            sqlStatement.executeUpdate(sqlString);
            con.getDbConnection().close();
            updateSuccess=true;
        }
        catch(Exception ex)
        {
            error=ex.toString();
        }
    }//Ende updateArticle()
    
    public LinkedList<Article> searchArticles(String searchParam)
    {
        con.driverMysql();
        con.dbConnect();
        
        try
        {
            sqlStatement=con.getDbConnection().createStatement();
            sqlString="SELECT * FROM article WHERE AName LIKE '" + searchParam 
                    + "%' OR APicURL LIKE '" + searchParam + "%';";
            resultSet=sqlStatement.executeQuery(sqlString);
        }
        catch(Exception ex)
        {
            error=ex.toString();
        }
        
        //Schreiben von resultSet in aList
        
        try
        {
            
            while(resultSet.next())
            {
                Article a = new Article();
                a.setProperties(resultSet.getString("AName"),
                        Double.parseDouble(resultSet.getString("APrice")), 
                        resultSet.getString("APicURL"),
                        resultSet.getInt("AID"));
                aList.add(a);
            }
           
        }
        catch(Exception ex)
        {
            error=ex.toString();
        }
        return aList;
    }//Ende searchArticles()
    
    
}//Ende Article


