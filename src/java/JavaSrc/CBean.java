
package JavaSrc;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean
@SessionScoped
public class CBean {

    
    public CBean() {
    }
    
    //-------Klassenvariablen--------
    private Customer currentCustomer = new Customer();
    
    private String redirect;
    private String error;
    private boolean registrationSuccess=false;
    private boolean errorOccured=false;
    
    
    //-----Getter und Setter

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isRegistrationSuccess() {
        return registrationSuccess;
    }

    public void setRegistrationSuccess(boolean registrationSuccess) {
        this.registrationSuccess = registrationSuccess;
    }

    public boolean isErrorOccured() {
        return errorOccured;
    }

    public void setErrorOccured(boolean errorOccured) {
        this.errorOccured = errorOccured;
    }
    
    
    
    
    //-------Action-Methoden------------------
    
    /**
     * Leitet eine Überprüfung der eingegebenen Login-Daten ein.
     * Wenn die Methode checkLogin() der Klasse Customer ein validiertes
     * Kundenobjekt zurückliefert wird zur Startseite geführt, ansonsten
     * das Login-Formular mit Fehlermeldung erneut aufgerufen
     * @return Navigation zur Startseite oder zum Login-Formular
     */
    public String validateLogin()
    {
        
        if(currentCustomer.checkLogin(currentCustomer.getCAlias(), currentCustomer.getCPwd()).isValidatedCustomer())
            redirect="index";
        
        else
            redirect="login";
        
        return redirect;
        
    }//Ende validateLogin()
    
    /**
    * Der angemeldete User wird ausgeloggt und es wird
    * ein neues Kundenobjekt erzeugt
    * @return Navigation zur Startseite
    */ 
    public String logout()
    {
       currentCustomer=new Customer();
       //currentCustomer.setLoggedIn(false);
       return "index";
   }//Ende logout()
    
    
    /**
     * Leitet das Anlegen eines neuen Benutzer-Accounts ein und überprüft,
     * ob der Account erfolgreich angelegt werden konnte.
     * @return Navigation
     */
    public String createAccount()
    {
       if(currentCustomer.createNewCustomerAccount())
           registrationSuccess=true;
       else
           registrationSuccess=false;
       
       return "registration";
    } //Ende createAccount()
    
    /**
     * Leitet das Editieren eines Kunden-Accounts ein
     * @return Navigation
     */
    public String modifyAccount()
    {
       error=null;
       currentCustomer.updateCustomerData();
       return "myAccount";
    }//Ende modifyAccount()
    
    
    
    
}//Ende CBean
