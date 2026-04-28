package com.botbye.springdemo.filter;

import com.botbye.Botbye;
import com.botbye.model.BotbyeValidationEvent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BotbyeFilter extends OncePerRequestFilter {
    private final Botbye botbye;

    public BotbyeFilter(Botbye botbye) {
        this.botbye = botbye;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Map<String, String> headers = Collections.list(request.getHeaderNames()).stream()
            .collect(Collectors.toMap(h -> h, request::getHeader));

        // Extract the token from wherever you pass it: query param, header, body, etc.
        var result = botbye.evaluate(BotbyeValidationEvent.of(
            request.getRemoteAddr(),
            request.getParameter("botbye_token"),
            headers,
            request.getMethod(),
            request.getRequestURI(),
            Collections.emptyMap()
        ));

        if (result.isBlocked()) {
            response.setStatus(403);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
