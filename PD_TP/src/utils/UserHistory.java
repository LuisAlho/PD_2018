/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author Nasyx
 */
public class UserHistory implements Serializable {
    
     private static final long serialVersionUID = 1L;
     
     String username;
     Files file;
     String remoteUser;
     boolean received;
     Calendar date;

    public UserHistory(String username, Files file, String remoteUser, boolean received, Calendar date) {
        this.username = username;
        this.file = file;
        this.remoteUser = remoteUser;
        this.received = received;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Files getFile() {
        return file;
    }

    public void setFile(Files file) {
        this.file = file;
    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public void setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Override
    public String toString() {
        
        if (this.received)
            return "Download UserHistory{" + "username=" + username + ", file=" + file + ", remoteUser=" + remoteUser + ", received=" + received + ", date=" + date + '}';
        else
            return "Upload UserHistory{" + "username=" + username + ", file=" + file + ", remoteUser=" + remoteUser + ", received=" + received + ", date=" + date + '}';
    }
     
     
     
     
    
}
