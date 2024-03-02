package com.ez4bk.eztakeout.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Global exception handler
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * Handle SQLIntegrityConstraintViolationException
     *
     * @param e Expecting SQLIntegrityConstraintViolationException
     * @return R<String> Error message
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(Exception e) {
        if (e.getMessage().contains("Duplicate entry")) {
            log.error("Duplicate entry in database");
            String duplicateEntry = e.getMessage().split("Duplicate entry")[1].split(" ")[1];
            String msg = duplicateEntry + " already exists";
            return R.error(msg);
        }

        return R.error("Unknown error in database operation.");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException e) {
        log.error(e.getMessage());

        return R.error(e.getMessage());
    }
}
