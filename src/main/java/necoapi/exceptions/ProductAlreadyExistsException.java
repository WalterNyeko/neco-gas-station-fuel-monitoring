package necoapi.exceptions;

import necoapi.helpers.APIConstants;

public class ProductAlreadyExistsException extends RuntimeException{
    public ProductAlreadyExistsException(String productName, String value) {
        super(String.format(
                APIConstants.PRODUCT_ALREADY_EXISTS,
                productName, value));
    }
}
