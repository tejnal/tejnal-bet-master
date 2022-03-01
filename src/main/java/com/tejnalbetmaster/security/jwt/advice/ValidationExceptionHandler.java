package com.tejnalbetmaster.security.jwt.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tejnalbetmaster.security.exception.CustomInputException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ValidationExceptionHandler {

  private static final Pattern ENUM_MSG =
      Pattern.compile("values accepted for Enum class: [\\s\\S]*?(?=])");

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorMessage methodArgumentNotValidException(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();
    return processFieldErrors(fieldErrors);
  }

  /*@ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ErrorMessage methodInvalidRequestEnum(HttpMessageNotReadableException ex, WebRequest request) {
  	return new ErrorMessage(HttpStatus.FORBIDDEN.value(), new Date(), ex.getMessage(),
  request.getDescription(false));
  }*/

  /* @ExceptionHandler(InvalidFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorMessage handleException(InvalidFormatException e) {


      return new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), "REQUEST VALIDATION FAILED!", e.getLocalizedMessage());
  }*/

  @ExceptionHandler(CustomInputException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorMessage handleException(CustomInputException e) {
    return new ErrorMessage(
        HttpStatus.BAD_REQUEST.value(),
        new Date(),
        "REQUEST PARAM VALIDATION FAILED!",
        e.getMessage());
  }

  private ErrorMessage processFieldErrors(
      List<org.springframework.validation.FieldError> fieldErrors) {
    String errorMessage = "Field Error - ";
    for (org.springframework.validation.FieldError fieldError : fieldErrors) {
      errorMessage =
          errorMessage + " | " + fieldError.getField() + " : " + fieldError.getDefaultMessage();
    }
    return new ErrorMessage(
        HttpStatus.BAD_REQUEST.value(), new Date(), "REQUEST VALIDATION FAILED!", errorMessage);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ErrorMessage handleJsonErrors(HttpMessageNotReadableException exception) {
    if (exception.getCause() != null && exception.getCause() instanceof InvalidFormatException) {
      Matcher match = ENUM_MSG.matcher(exception.getCause().getMessage());
      if (match.find()) {
        return new ErrorMessage(
            HttpStatus.BAD_REQUEST.value(),
            new Date(),
            "REQUEST VALIDATION FAILED!",
            "Value should be: " + match.group(0) + "]");
      }
    }

    return new ErrorMessage(
        HttpStatus.BAD_REQUEST.value(),
        new Date(),
        "REQUEST VALIDATION FAILED!",
        exception.getMessage());
  }
}
