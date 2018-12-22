package utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nasyx
 */
public class DBConnection {
    
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    //private static final String DB_CONNECTION = "jdbc:oracle:thin:@localhost:1521:MKYONG";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";
    private final static  String nameDB = "pd2018";
    
    private static DBConnection instance;
    private Connection connection;

    public DBConnection(String urlBD, int port){
        connection = connectToDB(urlBD, port, this.nameDB);
    }

    public static DBConnection getInstance(String urlBD, int port) {
        if (instance == null){
            instance = new DBConnection(urlBD, port);
        }
        return instance;
    }

    private Connection connectToDB(String url, int port, String bdName){
        
        Connection dbConnection = null;
        
        //jdbc:mysql://localhost/test?" +"user=minty&password=greatsqldb"
        
        String DB_CONNECTION = "jdbc:mysql://" + url +"/"+ bdName+"?user=" + DB_USER + "&password=" + DB_PASSWORD ;
        System.out.println(DB_CONNECTION);
        try {
            Class.forName(DB_DRIVER).newInstance();
        } catch (ClassNotFoundException e) {
            System.out.println("No driver found.... baddd");
            System.out.println(e.getMessage());
        } catch (InstantiationException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        System.out.println("Conncetion estabelished");
        return dbConnection;


    }
    
    public User loginUser(String username, String password) throws SQLException, SQLSyntaxErrorException {
        
        User p =  new User();
        
        String selectTableSQL = "SELECT password, name, username FROM users WHERE (password like '" + password + "') AND (username like '" + username + "')";
        System.out.println(selectTableSQL);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(selectTableSQL);
        if (!rs.isBeforeFirst() ) { 
            System.out.println("No data"); 
            return null;
        }
        
        while (rs.next()) {
            p.setName(rs.getString("name"));
            p.setUsername(rs.getString("username"));
        }
        
        if(this.setUserLoggedIn(username, true)){
            p.setLoggedIn(true);
            System.out.println("User: " + p.getName() + "set as logged in..");
            return p;
        }
        return null;
    }
    
    public boolean userLogout(String username){
        
        try {
            String sql = "UPDATE users SET loggedIn = 0 WHERE username like '" + username + "'";
            
            Statement statement = connection.createStatement();
            int rs = statement.executeUpdate(sql);
            if(rs == 0) 
                return false;
            return true;
        } catch (SQLException ex) {
            System.out.println("error updating table player.. :" + ex);
            return false;
        }
    }
    
    public boolean registerUser(User user) throws SQLException {
        
        //TODO insert user on database
        String selectTableSQL = "INSERT INTO users(name, username, password) VALUES (?,?,?)" ;

        PreparedStatement pst = connection.prepareCall(selectTableSQL);
        pst.setString(1, user.getName());
        pst.setString(2, user.getUsername());
        pst.setString(3, user.getPassword());
        //pst.setInt(4, 1);

        int rs = pst.executeUpdate();
        if(rs == 0) 
            return false;
        return true;
    }
    
    public boolean setUserLoggedIn(String username, boolean isLogged){
        
        try {
            //String sql = "UPDATE users SET isLogged = 1 WHERE username like '" + username + "'";
            String sql = "UPDATE users SET isLogged = ? WHERE username like ?";
            //String selectTableSQL = "INSERT INTO users(name, username, password) VALUES (?,?,?)" ;
            
            PreparedStatement pst = connection.prepareCall(sql);
            
            pst.setBoolean(1, isLogged);
            pst.setString(2, username);
            
            
            
//            Statement statement = connection.createStatement();
//            int rs = statement.executeUpdate(sql);

            int rs = pst.executeUpdate();
            pst.close();
            if(rs == 0) 
                return false;
            return true;
        } catch (SQLException ex) {
            System.out.println("error updating status of user - isLogged.. :" + ex);
            return false;
        }
    }

    public List<User> listUsers(boolean loggedIn){
        
        
        List<User> listUser = new ArrayList<>();
        User p;

        if(loggedIn){
            try {
                //get all users loggedIn
                String sql = "SELECT name, username, loggedIn FROM users WHERE loggedIn = 1";
                //String sql = "SELECT * FROM player";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                System.out.println("RS List logged users: " + rs);                
                if (!rs.isBeforeFirst() ) { 
                    System.out.println("No data"); 
                    return null;
                }
                
                while (rs.next()) {
                    p = new User();
                    p.setName(rs.getString("name"));
                    p.setUsername(rs.getString("username"));
                    p.setLoggedIn(rs.getBoolean("loggedIn"));
                    System.out.println("RS Player: " + p.toString());
                    listUser.add(p);
                    
                }                
                return listUser;
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }else{
            //TODO get list of all users
        }
        
        //TODO create query to list all users on DB
        return null;
    }

    private User getUser(int id){

        //TODO create query to get informatuion of one user
        return null;
    }
}
