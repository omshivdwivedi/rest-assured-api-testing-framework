package api.payloads;

public class AuthReqPayload {

    private String username;
    private String password;

    public AuthReqPayload(){
        
    }

    public AuthReqPayload(String username, String password) {
        this.username = username;
        this.password = password;
    }

    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
