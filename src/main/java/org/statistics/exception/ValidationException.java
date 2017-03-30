package org.statistics.exception;

import org.statistics.common.APIError;

/**
 * Unchecked exception which is supposed to be
 * thrown if request body is not valid or it has
 * some invalid fields
 *
 * @author zaur.guliyev
 * @since 1.0.0
 */
public class ValidationException extends RuntimeException {
    private Integer errorCode;
    private String errorMessage;

    public ValidationException() {
        super(APIError.VALIDATION.code()+"-"+APIError.VALIDATION.message());

        this.errorCode = APIError.VALIDATION.code();
        this.errorMessage = APIError.VALIDATION.message();
    }

    public ValidationException(APIError apiError){
        super(apiError.code() + "-" + apiError.message());

        this.errorCode = apiError.code();
        this.errorMessage = apiError.message();
    }

    public ValidationException(Integer errorCode, String errorMessage) {
        super(errorMessage);

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
