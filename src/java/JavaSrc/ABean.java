
package JavaSrc;

import java.util.LinkedList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class ABean {

    public ABean() {
    }
    
    //Klassenattribute
    private Article currentArticle = new Article();
    private String error;
    private LinkedList<Article> articleList = new LinkedList<Article>();
   
   
    
    
    

  
    
    
    //-----------Getter und Setter----------------

    public Article getCurrentArticle() {
        return currentArticle;
    }

    public void setCurrentArticle(Article currentArticle) {
        this.currentArticle = currentArticle;
    }
    
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public LinkedList<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(LinkedList<Article> articleList) {
        this.articleList = articleList;
    }

     //-------------Action-Methoden---------------------

    /**
     * Leitet den Upload eine Artikelbildes ein
     */
    public void uploadPic() 
    {
        try 
        {
            currentArticle.uploadArticleImage();
        } 
        catch (Exception ex) 
        {
            error=ex.toString();
        }
        
    }//Ende uploadPic()
    
    
    /**
     * Leitet das Speichern der Artikeldaten in DB ein
     * 
     */
    public void saveArticle()
    {
        currentArticle.saveArticleInDB();
        
    }//Ende saveArticle()
    
    /**
     * Leitet das Laden einer Artikelliste aus der Datenbank ein
     */
    public void loadArticles()
    {
        articleList=currentArticle.getArticleList();
    }
    
    /**
     * Leitet das Laden der Artikeldetails der angegebenen Artikelnummer ein
     * @param id Artikelnummer(AID aus Datenbank)
     */
    public void loadArticleDetails(int id)
    {
        currentArticle = currentArticle.getArticleData(id);
       
    }//Ende articleDetails()
    
    /**
     * Leitet die Editierung eines Artikels ein
     */
    public void updateArticleDetails()
    {
        currentArticle.updateArticle();
    }
    
    
}//Ende ABean
