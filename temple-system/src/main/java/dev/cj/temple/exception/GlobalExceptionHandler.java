package dev.cj.temple.exception;

import dev.cj.temple.common.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler(Exception.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // public Result<String> handleException(HttpServletRequest request, Exception e) {
    //     return Result.error(500, e.getMessage());
    // }

    /**
     * Handle 403 Forbidden errors
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<String> handleAccessDeniedException(AccessDeniedException e) {
        return Result.error(403, "Access denied: " + e.getMessage());
    }

    /**
     * Handle 404 Not Found errors
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<String> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        return Result.error(404, "Resource not found: " + request.getRequestURI());
    }

}