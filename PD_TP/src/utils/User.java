
package utils;

import java.io.Serializable;

public class User implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private String password;
    private String username;
    private boolean loggedIn;
    private int porto_udp;
    private int porto_tcp;
    private String ip;
    
    public User(){}
    
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public int getPorto_udp() {
        return porto_udp;
    }

    public void setPorto_udp(int porto_udp) {
        this.porto_udp = porto_udp;
    }

    public int getPorto_tcp() {
        return porto_tcp;
    }

    public void setPorto_tcp(int porto_tcp) {
        this.porto_tcp = porto_tcp;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    @Override
    public String toString() {
        return "User{" + "name=" + name + ", id=" + id + ", password=" + password + ", username=" + username + '}';
    }

    
}
