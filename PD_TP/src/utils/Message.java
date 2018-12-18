
package utils;

import java.io.Serializable;

public class Message implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    String type; //Type of message 
    String username;
    String password;
    String text;
    
    //Add more atributes as needed
   
    
    public Message(){}

    public String getType() {
        return type;
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
    
}
