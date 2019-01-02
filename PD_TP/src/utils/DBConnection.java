package utils;


import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        
        String DB_CONNECTION = "jdbc:mysql://" + url + ":" + port + "/" + bdName;
        //String DB_CONNECTION = "jdbc:mysql://" + url +"/"+ bdName+"?user=" + DB_USER;
        System.out.println(DB_CONNECTION);
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("No driver found.... baddd");
            System.out.println(e.getMessage());
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
    
    public User loginUser(User user) throws SQLException, SQLSyntaxErrorException {
        
        User p =  new User();
        
        String selectTableSQL = "SELECT password, name, username FROM users WHERE (password like '" + user.getPassword() + "') AND (username like '" + user.getUsername() + "') AND (isLogged != 1)";
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
        
        if(this.setUserDetails(user)){
            System.out.println("User update");
        }
        
        if(this.setUserLoggedIn(user.getUsername(), true)){
            p.setLoggedIn(true);
            //System.out.println("User: " + p.getName() + " set as logged in..");
            return p;
        }
        return null;
    }
    
    public boolean userLogout(String username){
        
        try {
            String sql = "UPDATE users SET isLogged = 0 WHERE username like '" + username + "'";
            
            Statement statement = connection.createStatement();
            int rs = statement.executeUpdate(sql);
            
            if(rs == 0) 
                return false;
            return true;
        } catch (SQLException ex) {
            System.out.println("error logout client.. :" + ex);
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

        int rs = pst.executeUpdate();
        pst.close();
        if(rs == 0) 
            return false;
        return true;
    }
    
    public boolean setUserLoggedIn(String username, boolean isLogged){
        
        
        
        try {
            String sql = "UPDATE users SET isLogged = ? WHERE username like ?";

            PreparedStatement pst = connection.prepareCall(sql);
            
            pst.setBoolean(1, isLogged);
            pst.setString(2, username);
            
            int rs = pst.executeUpdate();
            
            System.out.println("SET USER " + username + " logged to: " + isLogged);
            pst.close();
            if(rs == 0) 
                return false;
            return true;
        } catch (SQLException ex) {
            System.out.println("error updating status of user - isLogged.. :" + ex);
            return false;
        }
    }
    
    public boolean setUserDetails(User user){
        
        try {
            String sql = "UPDATE users SET ip = ?, udp_port = ?, tcp_port = ?  WHERE username like ?";

            PreparedStatement pst = connection.prepareCall(sql);
            
            pst.setString(1, user.getIp());
            pst.setInt(2, user.getPorto_udp());
            pst.setInt(3, user.getPorto_tcp());
            pst.setString(4, user.getUsername());
            
            int rs = pst.executeUpdate();
            pst.close();
            if(rs == 0) 
                return false;
            return true;
        } catch (SQLException ex) {
            System.out.println("error updating status of user - details.. :" + ex);
            return false;
        }
     
    }
    
    public boolean incUserCount(String username){
        
        try {
            String sql = "UPDATE users SET count = count + 1 WHERE username like ?";

            PreparedStatement pst = connection.prepareCall(sql);
            
            pst.setString(1, username);
            
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
    
    public boolean resetUserCount(String username){
        
     try {
            String sql = "UPDATE users SET count = 0 WHERE username like ?";

            PreparedStatement pst = connection.prepareCall(sql);
            
            pst.setString(1, username);
            
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
    
    public void setUserFilesList(User user, List listOfFiles) {
        
        
        //TODO primeiro remover todos os ficheiros do utilizador se existirem
        
        String sql = "DELETE FROM ficheiros WHERE username = ?";
        
        try {
            PreparedStatement delFiles = connection.prepareStatement(sql);
            
            delFiles.setString(1, user.getUsername());
            
            int rs = delFiles.executeUpdate();
            System.out.println("Detlete rows: " + rs);
            delFiles.close();
            
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Insert files to DB

        sql = "insert into ficheiros(username,nome,size) values (?,?,?)";
        
        try {
            //connection.setAutoCommit(true);        
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            Iterator<Files> it = listOfFiles.iterator();
            while(it.hasNext()){
                Files f = it.next();
                prepStmt.setString(1,user.getUsername());            
                prepStmt.setString(2, f.getName() );
                prepStmt.setLong(3, f.getSize());
                prepStmt.addBatch();                      
            }
            
            int [] numUpdates = prepStmt.executeBatch();
            for (int i=0; i < numUpdates.length; i++) {
              if (numUpdates[i] == -2)
                System.out.println("Execution " + i + ": unknown number of rows updated");
              else
                System.out.println("Execution " + i + "successful: " + numUpdates[i] + " rows updated");
            }
            //connection.commit();
            prepStmt.close();
        }catch(BatchUpdateException b) {
            System.out.println(b.getMessage());

        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    

    public List<User> listUsersLoggedIn(boolean loggedIn){
        
        
        List<User> listUser = new ArrayList<>();
        User p;

        if(loggedIn){
            try {
                //get all users loggedIn
                String sql = "SELECT * FROM users WHERE isLogged = 1";
                //String sql = "SELECT * FROM player";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                System.out.println("RS List logged users: " + rs);                
                if (!rs.isBeforeFirst() ) { 
                    System.out.println("No data"); 
                    return listUser;
                }
                
                while (rs.next()) {
                    p = new User();
                    p.setName(rs.getString("name"));
                    p.setUsername(rs.getString("username"));
                    p.setLoggedIn(rs.getBoolean("isLogged"));
                    System.out.println("User: " + p.toString());
                    listUser.add(p);
                    
                }                
                return listUser;
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }else{
            try {
                //get all users loggedIn
                String sql = "SELECT * FROM users";
                //String sql = "SELECT * FROM player";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                System.out.println("RS List logged users: " + rs);                
                if (!rs.isBeforeFirst() ) { 
                    System.out.println("No data"); 
                    return listUser;
                }
                
                while (rs.next()) {
                    p = new User();
                    p.setName(rs.getString("name"));
                    p.setUsername(rs.getString("username"));
                    p.setLoggedIn(rs.getBoolean("isLogged"));
                    listUser.add(p);
                    
                }                
                return listUser;
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //TODO create query to list all users on DB
        return null;
    }

    private User getUser(int id){

        //TODO create query to get information of one user
        return null;
    }

    public List<Files> getDownloadsFiles() {
        
        String sql = "Select * FROM ficheiros";
        
        List<Files> list = new ArrayList();
        
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            
            ResultSet rs = pst.executeQuery();
        
            System.out.println("Detlete rows: " + rs);
            
            if (!rs.isBeforeFirst() ) { 
                System.out.println("No data"); 
                return null;
            }
        
            while (rs.next()) {
                Files f = new Files();
                f.setName(rs.getString("nome"));
                f.setSize(rs.getInt("size"));
                f.setUsername(rs.getString("username"));
                list.add(f);
            }
            pst.close();
            return list;
            
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return list;
        }
    }

    
}
