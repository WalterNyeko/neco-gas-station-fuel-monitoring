package necoapi.exceptions;

import lombok.extern.slf4j.Slf4j;
import necoapi.helpers.APIConstants;
import necoapi.helpers.APIError;
import necoapi.helpers.ErrorMappers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

@ControllerAdvice
@Slf4j
public class SpringBootExceptions extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
            Optional<FieldError> error = ex.getBindingResult()
                    .getFieldErrors()
                    .stream().filter(each ->
                            each.getDefaultMessage() != null)
                    .findFirst();
            String theError = error.isPresent() ? error.get().getDefaultMessage() : null;
            String theMessage = error.isPresent() ? APIConstants.ERROR_IS_RELATED_TO_FIELD +error.get().getField(): null;
            return handleExceptionInternal(
                    ex, ErrorMappers
                            .mapErrors(
                                    theMessage,
                                    theError),
                    headers, HttpStatus.BAD_REQUEST, request);
        }
        Optional<ObjectError> objectError = ex.getBindingResult()
                .getGlobalErrors()
                .stream().filter(each ->
                        each.getDefaultMessage() != null)
                .findFirst();
        String theError = objectError.isPresent() ? objectError.get().getDefaultMessage() : null;
        String theMessage = objectError.isPresent() ? APIConstants.ERROR_IS_RELATED_TO_FIELD+objectError.get().getObjectName(): null;
        return handleExceptionInternal(
                ex, ErrorMappers
                        .mapErrors(
                                theMessage,
                                theError),
                headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + APIConstants.PARAMETER_IS_MISSING;
        String reason = APIConstants.PLEASE_PROVIDE_THE_MISSING_PARAMETER + ex.getParameterName();
        return returnError(error, reason, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        Optional<ConstraintViolation<?>> errors =
                ex.getConstraintViolations()
                        .stream().filter(each ->
                        each.getMessage() != null)
                        .findFirst();
        String error = errors.isPresent() ? errors.get().getMessage() : null;
        String reason = errors.isPresent() ? APIConstants.THIS_ERROR_IS_CAUSED_BY_BEAN +errors.get().getRootBeanClass().getName() : null;
        return returnError(error, reason, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error = APIConstants.TYPE_MISMATCH_FOR +ex.getName();
        String reason =
                ex.getName() + APIConstants.SHOULD_BE_OF_TYPE + ex.getRequiredType().getName();

        return returnError(error, reason, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(
                APIConstants.METHOD_IS_NOT_SUPPORTED_FOR_THIS_REQUEST);
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(APIConstants.SUPPORTED_METHODS_ARE);
        ex.getSupportedHttpMethods().forEach(t -> messageBuilder.append(t+", "));
        messageBuilder.append("]");

        APIError apiError = new APIError();
        apiError.setMessage(builder.toString());
        apiError.setReason(messageBuilder.toString());

        return returnError(builder.toString(), messageBuilder.toString(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        StringBuilder builder = new StringBuilder();

        builder.append(ex.getContentType());
        builder.append(
                APIConstants.MEDIA_TYPE_IS_NOT_SUPPORTED);

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(APIConstants.SUPPORTED_MEDIA_TYPES_ARE);
        ex.getSupportedMediaTypes().forEach(t -> messageBuilder.append(t+", "));
        messageBuilder.append("]");

        return returnError(builder.toString(), messageBuilder.toString(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        String exceptionClass = ex.getClass().getSimpleName();
        if (exceptionClass.equalsIgnoreCase("DataIntegrityViolationException")) {
            return returnError(ex.getMessage(), APIConstants.VIOLATION_ERROR, HttpStatus.BAD_REQUEST);
        }
        if (exceptionClass.equalsIgnoreCase("ProductAlreadyExistsException")) {
            return returnError(ex.getMessage(), APIConstants.PRODUCT_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }
        if (exceptionClass.equalsIgnoreCase("AccountAlreadyExistsException")) {
            return returnError(ex.getMessage(), APIConstants.ACCOUNT_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }
        if (exceptionClass.equalsIgnoreCase("TransactionNotFoundException")) {
            return returnError(ex.getMessage(), APIConstants.TRANSACTION_NOT_FOUND_REASON, HttpStatus.NOT_FOUND);
        }
        if (exceptionClass.equalsIgnoreCase("AccountNotFoundException")) {
            return returnError(ex.getMessage(), APIConstants.ACCOUNT_NOT_FOUND_REASON, HttpStatus.NOT_FOUND);
        }
        if (exceptionClass.equalsIgnoreCase("ProductNotFoundException")) {
            return returnError(ex.getMessage(), APIConstants.PRODUCT_NOT_FOUND_REASON, HttpStatus.NOT_FOUND);
        }
        if (exceptionClass.equalsIgnoreCase("InvalidTransactionException")) {
            return returnError(ex.getMessage(), APIConstants.PROVIDE_VALID_TRANSACTION, HttpStatus.BAD_REQUEST);
        }
        if (exceptionClass.equalsIgnoreCase("InvalidAccountDetailsException")) {
            return returnError(ex.getMessage(), APIConstants.PROVIDE_VALID_ACCOUNT, HttpStatus.BAD_REQUEST);
        }
        if (exceptionClass.equalsIgnoreCase("InvalidProductException")) {
            return returnError(ex.getMessage(), APIConstants.PROVIDE_VALID_PRODUCT, HttpStatus.BAD_REQUEST);
        }
        return returnError(ex.getMessage(), APIConstants.GENERIC_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> returnError(String message, String genericError, HttpStatus httpStatus) {
        return new ResponseEntity<>(
                ErrorMappers
                        .mapErrors(
                                message,
                                genericError),
                new HttpHeaders(), httpStatus);
    }
}
