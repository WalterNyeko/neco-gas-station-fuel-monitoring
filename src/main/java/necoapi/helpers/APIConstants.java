package necoapi.helpers;

import java.util.Locale;

public class APIConstants {
    public static final String ACCOUNT_ID = "account_id";
    public static final String TRANSACTION_ACCOUNT_ID = "transaction_account_id";
    public static final String TOPUP_ACCOUNT_ID = "topup_account_id";
    public static final String PRODUCT_ID = "product_id";
    public static final String TRANSACTION_PRODUCT_ID = "transaction_product_id";
    public static final String GENERIC_ERROR = "Generic error occurred";
    public static final String ERROR_IS_RELATED_TO_FIELD = "This error is related to field: ";
    public static final String PARAMETER_IS_MISSING = " parameter is missing";
    public static final String PLEASE_PROVIDE_THE_MISSING_PARAMETER = "Please provide the missing parameter: ";
    public static final String THIS_ERROR_IS_CAUSED_BY_BEAN = "This error is caused by bean: ";
    public static final String TYPE_MISMATCH_FOR = "Type mismatch for ";
    public static final String SHOULD_BE_OF_TYPE = " should be of type ";
    public static final String METHOD_IS_NOT_SUPPORTED_FOR_THIS_REQUEST = " method is not supported for this request.";
    public static final String SUPPORTED_METHODS_ARE = "Supported methods are [";
    public static final String MEDIA_TYPE_IS_NOT_SUPPORTED = " media type is not supported";
    public static final String SUPPORTED_MEDIA_TYPES_ARE = "Supported media types are [";

    public static final String PROVIDE_VALID_PRODUCT = "Provide valid product details";
    public static final String PROVIDE_VALID_ACCOUNT = "Provide valid account details";
    public static final String PROVIDE_VALID_TRANSACTION = "Provide valid transaction details";
    public static final String ACCOUNT_NOT_FOUND_REASON = "Provided account was not found";
    public static final String TRANSACTION_NOT_FOUND_REASON = "Provided transaction was not found";
    public static final String PRODUCT_NOT_FOUND_REASON = "Provided product was not found";
    public static final String PROVIDE_UNIQUE_VALUE = "Please provide a unique value";
    public static final String PRODUCT_NAME_IS_MISSING_OR_INVALID = "Product name is either missing or invalid";
    public static final String PRODUCT_PRICE_IS_MISSING_OR_INVALID = "Product unit price is either missing or invalid";
    public static final CharSequence UNIQUE_INDEX_VIOLATION = "Unique index or primary key violation";
    public static final String PRODUCT_NAME = "productName";
    public static final String PRODUCT_ALREADY_EXISTS = "Product with property %s and value %s already exists.";
    public static final String ACCOUNT_NAME = "accountName";
    public static final String MISSING_OR_INVALID_ACCOUNT_NAME = "Missing or invalid account name";
    public static final String MISSING_OR_INVALID_OPENING_DATE = "Missing or invalid account opening date";
    public static final String ACCOUNT_NOT_FOUND = "Account was not found";
    public static final String MESSAGE = "message";
    public static final String ACCOUNT_DELETED_SUCCESSFULLY = "Account deleted successfully";
    public static final String SUCCESS = "success";
    public static final Object TRANSACTION_DELETED_SUCCESSFULLY = "Transaction deleted successfully";
    public static final String TRANSACTION_NOT_FOUND = "Transaction was not found";
    public static final String TRANSACTION_ACCOUNT_IS_INVALID = "Transaction account is either missing or invalid";
    public static final String TRANSACTION_PRODUCT_IS_INVALID = "Transaction product is either missing or invalid";
    public static final String TRANSACTION_AMOUNT_IS_INVALID = "Transaction amount is either missing or invalid";
    public static final String TRANSACTION_AMOUNT_EXCEEDS_BALANCE = "Transaction amount exceeds current balance";
    public static final String PRODUCT_NOT_FOUND = "Product was not found";
    public static final String ACCOUNT_ALREADY_EXISTS = "Account already exists";
    public static final String VIOLATION_ERROR = "Ensure that valid data is provided";
    public static final String FETCH_ACCOUNTS = "http://localhost:8080/neco/v1/accounts";
    public static final String SEARCH_ACCOUNTS = "http://localhost:8080/neco/v1/search/accounts?searchKey=";

    public static final String SAVE_TOP_UP = "http://localhost:8080/account/{accountId}/topup";
    public static final String POST_USER = "http://localhost:8080/register";
}
