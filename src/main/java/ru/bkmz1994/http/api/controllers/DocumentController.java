package ru.bkmz1994.http.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bkmz1994.http.annotations.ThrottleDefense;
import ru.bkmz1994.http.dto.Response;
import ru.bkmz1994.http.models.Document;

@RestController
@RequestMapping("/api/v3/lk/documents")
public class DocumentController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @PostMapping("/create")
    @ThrottleDefense(limit = 3, duration = 3000)
    public ResponseEntity<Response<Object>> create(@RequestBody Document payload) {
        logger.info(payload.toString());
        Response<Object> response = Response.builder().message("Success").build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
