package necoapi.exceptions;

import necoapi.helpers.APIConstants;

public class AccountAlreadyExistsException extends RuntimeException{
    public AccountAlreadyExistsException(String accountName, String value) {
        super(String.format(
                APIConstants.ACCOUNT_ALREADY_EXISTS,
                accountName, value));
    }
}
