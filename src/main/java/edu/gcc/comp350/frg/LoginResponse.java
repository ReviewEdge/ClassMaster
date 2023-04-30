package edu.gcc.comp350.frg;

public class LoginResponse {
    private boolean wasSuccessfulLogin;
    private int id;
    private Account loggedInAccount;

    public LoginResponse(boolean wasSuccessfulLogin, Account loggedInAccount) {
        this.wasSuccessfulLogin = wasSuccessfulLogin;
        this.id = loggedInAccount.getId();
        this.loggedInAccount = loggedInAccount;
    }

    public LoginResponse(boolean wasSuccessfulLogin, int id, Account loggedInAccount) {
        this.wasSuccessfulLogin = wasSuccessfulLogin;
        this.id = id;
        this.loggedInAccount = loggedInAccount;
    }

    public static LoginResponse makeFailedLogin() {
        return new LoginResponse(false, -1, null);
    }
}
