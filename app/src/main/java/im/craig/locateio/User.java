package im.craig.locateio;

import java.util.Date;


public class User {
    String username;
    String fName;
    Date sessionExpiryDate;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fName) {
        this.fName = fName;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getUsername() {
        return username;
    }

    public String getFName() {
        return fName;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }
}