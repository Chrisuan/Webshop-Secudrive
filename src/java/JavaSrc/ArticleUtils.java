
package JavaSrc;

import java.util.LinkedList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class ArticleUtils {

   
    public ArticleUtils() {
    }
    
    //Bean-Attribute
    private String searchParam;
    private Article article =new Article();
    private LinkedList<Article> articleList = new LinkedList<Article>();
    
    
    //---------------Getter und Setter----------------------------
    public String getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(String searchParam) {
        this.searchParam = searchParam;
    }

    public Article getArt() {
        return article;
    }

    public void setArt(Article art) {
        this.article = art;
    }

    public LinkedList<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(LinkedList<Article> articleList) {
        this.articleList = articleList;
    }
    
    
    
    //-------------------------Action-Methoden--------------------
    
    
    /**
     * Nimmt eine Such-Anfrage entgegen, leitet die Anfrage an die Artikel
     *  Klasse weiter und erhält von dieser eine verkettete Liste von Artikeln
     */
    public void searchArt()
    {
        
        articleList=article.searchArticles(searchParam);
    }//Ende searchArt()
    
}//Ende ArticleUtils
