package pr7.Selenium;

public enum ErrorMessage {
    INVALID_LOGIN("Login must be alphanumeric string with length => 6 and <= 50."),
    //В документации: The user with such username has been already registered. Please fill out another username.
    REGISTERED_LOGIN("User with such login already exists."),
    PASSWORD_LENGTH("Password length must me >= 8 and <= 50."),
    PASSWORD_UPPER_LETTER("At least one upper letter must be in password"),
    PASSWORD_ALPHANUMERIC("At least one non alpha numeric symbol must be in password"),
    PASSWORD_MATCH("Password must match confirm password."),
    INVALID_EMAIL("Email address is incorrect."),
    REGISTERED_EMAIL("The user with such email address has been already registered. Please fill out another email address.");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}