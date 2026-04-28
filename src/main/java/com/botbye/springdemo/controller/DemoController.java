package com.botbye.springdemo.controller;

import com.botbye.Botbye;
import com.botbye.model.BotbyeEvaluateResponse;
import com.botbye.model.BotbyeValidationEvent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/demo")
public class DemoController {
    private final Botbye botbye;

    @Autowired
    public DemoController(Botbye botbye) {
        this.botbye = botbye;
    }

    @PostMapping
    public ResponseEntity<Object> post(HttpServletRequest request) {
        Map<String, String> headers = Collections.list(request.getHeaderNames()).stream()
            .collect(Collectors.toMap(h -> h, request::getHeader));

        // Extract the token from wherever you pass it: query param, header, body, etc.
        String token = request.getParameter("botbye_token");

        BotbyeEvaluateResponse response = botbye.evaluate(BotbyeValidationEvent.of(
            request.getRemoteAddr(),
            token,
            headers,
            request.getMethod(),
            request.getRequestURI(),
            Collections.emptyMap()
        ));

        if (response.isBlocked()) {
            return ResponseEntity.status(403).body("Access denied");
        }

        return ResponseEntity.ok().body("hello world!");
    }
}
