package ru.bkmz1994.http.exceptions.classes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class ThrottleException extends RuntimeException {
    public ThrottleException() {
        super("Too many attempts");
    }
}
