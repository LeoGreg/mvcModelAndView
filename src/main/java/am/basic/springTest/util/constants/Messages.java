package am.basic.springTest.util.constants;

public interface Messages {

    String INTERNAL_ERROR_MESSAGE = "Something went wrong, please try later";

    String THE_SAME_NUMBER_MESSAGE = "make sure you entered correct number";

    String NO_CARD_MESSAGE = "there is no card found by given card number";

    String CARD_EXPIRATION_MESSAGE = "card is not valid";

    String NEW_OLD_PASSWORD_NOT_MATCH="passwords don't match";


    String CARD_SAMENESS_ERROR_MESSAGE = "there is already card signed up by 16_size card_number ";

    String PASSWORD_INVALID_MESSAGE = "Password  must be more then 8 characters";

    String MONEY_SUCCESSFULLY_TRANSFERRED_MESSAGE = "money's been successfully transferred";

    String NOT_VALID_CARD_NUMBER_MESSAGE = "card number must be 16 characters";

    String USERNAME_INVALID_MESSAGE = "Username must be more then 5 characters";

    String CODE_INVALID_MESSAGE = "Code must be equal 5 digits";

    String DUPLICATE_USER_MESSAGE = "There is user with such username";

    String INVALID_CREDENTIALS_MESSAGE = "Wrong username or password";

    String UNVERIFIED_MESSAGE = "Your account is not verified,please verify !";

    String USER_NOT_EXIST_MESSAGE = "There is some problem with your account";

    String SESSION_EXPIRED_MESSAGE = "Your session expired please log in ";

    String WRONG_PASSWORD_MESSAGE = "Incorrect password";

    String WRONG_CODE_MESSAGE = "Incorrect verification code";

    String PASSWORD_CHANGE_SUCCESS_MESSAGE = "Your password successfully changed";

    String VERIFICATION_SUCCESS_MESSAGE = "Verification was success, please log in";

    String REGISTRATION_SUCCESS_MESSAGE = "Registration was success, now please verify your email";

    String CODE_SUCCESSFULLY_SEND_MESSAGE = "Your code is successfully sent to your email !";
}
