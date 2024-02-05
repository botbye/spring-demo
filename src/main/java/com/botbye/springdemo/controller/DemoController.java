package com.botbye.springdemo.controller;

import com.botbye.Botbye;
import com.botbye.model.BotbyeResponse;
import com.botbye.model.ConnectionDetails;
import com.botbye.model.Headers;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/demo")
public class DemoController {
    private final Botbye botbye;

    @Autowired
    public DemoController(Botbye botbye) {
        this.botbye = botbye;
    }

    @PostMapping
    public ResponseEntity<Object> post(@RequestHeader(name = "X-Token") String token) { // token can be sent in any convenient way
        HttpServletRequest request = (
                (ServletRequestAttributes) Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes()
                )
        ).getRequest();

        BotbyeResponse botbyeResponse = botbye.validateRequest(
                token,
                new ConnectionDetails(request.getServerPort(), request.getRemoteAddr(), request.getServerName(), request.getMethod(), request.getRequestURI()),
                extractHeaders(request)
        );

        if (botbyeResponse.getResult().isBanRequired() || botbyeResponse.getResult().isBot()) {
            return ResponseEntity
                    .status(403)
                    .body(botbyeResponse.getError().getMessage());
        }

        return ResponseEntity.ok().body("hello world!");
    }

    private Headers extractHeaders(HttpServletRequest request) {
        Map<String, List<String>> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();

            List<String> valuesList = Collections.list(request.getHeaders(headerName));

            headers.put(headerName, valuesList);
        }

        return new Headers(headers.entrySet());
    }
}
