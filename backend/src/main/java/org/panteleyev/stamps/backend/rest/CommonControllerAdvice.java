// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.rest;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.panteleyev.stamps.backend.exception.BadRequestException;
import org.panteleyev.stamps.backend.exception.ConflictException;
import org.panteleyev.stamps.backend.exception.IssueItemNotFoundException;
import org.panteleyev.stamps.backend.exception.IssueNotFoundException;
import org.panteleyev.stamps.backend.exception.TagNotFoundException;
import org.panteleyev.stamps.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
@Hidden
public class CommonControllerAdvice {
    @ExceptionHandler({
            IssueItemNotFoundException.class,
            IssueNotFoundException.class,
            TagNotFoundException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO notFoundHandler(Exception exception, HttpServletRequest request) {
        return new ErrorDTO()
                .code(HttpStatus.NOT_FOUND.value())
                .error(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI());
    }

    @ExceptionHandler({
            BadRequestException.class,
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO badRequestHandler(Exception exception, HttpServletRequest request) {
        return new ErrorDTO()
                .code(HttpStatus.BAD_REQUEST.value())
                .error(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI());
    }

    @ExceptionHandler({
            ConflictException.class,
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO conflictHandler(Exception exception, HttpServletRequest request) {
        return new ErrorDTO()
                .code(HttpStatus.CONFLICT.value())
                .error(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI());
    }
}
