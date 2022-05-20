package com.lms.librarymanagementsystem;
@SuppressWarnings("ALL")
public class Constants {

    /*
    Constant variables for passing fxml views between controllers
    and for accessing database
     */

//  FXML docs
    public static final String MAIN = "main.fxml";
    public static final String LOGIN = "login.fxml";
    public static final String SIGN_UP = "sign-up.fxml";
    public static final String SEARCH = "search.fxml";
    public static final String LOAN = "loan.fxml";
    public static final String ACCOUNT = "account.fxml";
    public static final String INVENTORY = "inventory.fxml";
    public static final String sample = ".fxml";
//  DB login credentials
    public final static String URL = "jdbc:mysql://localhost:3306/d0024e";
    public final static String USER = "root";
    public final static String PWD = "1234";
//  Scene titles
    public static final String MAIN_TITLE = "D0024E Bibliotekssystem - Välkommen! ";
    public static final String LOGIN_TITLE = "D0024E Bibliotekssystem - Inloggad ";
    public static final String SIGN_UP_TITLE = "D0024E Bibliotekssystem - Kontoregistrering ";
    public static final String SEARCH_TITLE = "D0024E Bibliotekssystem - Sök artiklar ";
    public static final String LOAN_TITLE = "D0024E Bibliotekssystem - Låna ";
    public static final String ACCOUNT_TITLE = "D0024E Bibliotekssystem - Konto ";
    public static final String INVENTORY_TITLE = "D0024E Bibliotekssystem - Artikelhantering ";
    public static final String sample_TITLE = ".fxml";
}
