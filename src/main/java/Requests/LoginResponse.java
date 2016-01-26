package Requests;


public class LoginResponse {
    public String greeting;
    public boolean loginSuccesful;
    public boolean partOfTeam;
    public boolean gotInvites;


    public LoginResponse(String greeting, boolean loginSuccesful, boolean partOfTeam, boolean gotInvites) {
        this.greeting = greeting;
        this.loginSuccesful = loginSuccesful;
        this.partOfTeam = partOfTeam;
        this.gotInvites = gotInvites;
    }

    public String getGreeting() {
        return greeting;
    }

    public boolean isLoginSuccesful() {
        return loginSuccesful;
    }

    public boolean isPartOfTeam() {
        return partOfTeam;
    }

    public boolean isGotInvites() {
        return gotInvites;
    }
}
