package com.myblog.controller;

import com.myblog.request.PostCreate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class PostController {

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate params, BindingResult result) throws Exception {
        log.info("params = {}", params.toString());
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);
            String fieldName = firstFieldError.getField(); // title
            String errorMessage = firstFieldError.getDefaultMessage(); // .. 에러메세지

             Map<String, String> error = new HashMap<>();
             error.put(fieldName, errorMessage);
             return error;
        }

        return Map.of();
    }

}
