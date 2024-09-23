package io.github.tracedin.web;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static io.opentelemetry.semconv.trace.attributes.SemanticAttributes.*;

@Slf4j
@Component
public class CollectLoggingFilter implements Filter {
    private final Tracer tracer;

    public CollectLoggingFilter(@Lazy Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Span span = tracer.spanBuilder(httpRequest.getRequestURI()).startSpan();

        try (Scope scope = span.makeCurrent()) {
            span.setAttribute(HTTP_METHOD, httpRequest.getMethod());
            span.setAttribute(HTTP_URL, httpRequest.getRequestURI());

            chain.doFilter(request, response);

            span.setAttribute(HTTP_STATUS_CODE, httpResponse.getStatus());
            span.setAttribute(HTTP_CLIENT_IP, httpRequest.getRemoteAddr());
        } finally {
            span.end();
        }
    }
}
