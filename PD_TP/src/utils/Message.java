
package utils;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    String type; //Type of message 
    String username;
    String password;
    String text;
    User user;
    List<Files> listOfFiles;
    List<UserHistory> listHistory;
    List<User> listOfUsers;
    
    //Add more atributes as needed
   
    
    public Message(){}

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public User getUser() {
        return this.user;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Files> getListOfFiles() {
        return listOfFiles;
    }

    public void setListOfFiles(List<Files> listOfFiles) {
        this.listOfFiles = listOfFiles;
    }

    public List<UserHistory> getListHistory() {
        return listHistory;
    }

    public void setListHistory(List<UserHistory> listHistory) {
        this.listHistory = listHistory;
    }

    public List<User> getListOfUsers() {
        return listOfUsers;
    }

    public void setListOfUsers(List<User> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }
    
    
    
}
