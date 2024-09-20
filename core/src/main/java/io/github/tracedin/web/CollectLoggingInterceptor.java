package io.github.tracedin.web;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static io.opentelemetry.semconv.trace.attributes.SemanticAttributes.*;

@Slf4j
public class CollectLoggingInterceptor implements HandlerInterceptor {
    private final Tracer tracer;

    public CollectLoggingInterceptor() {
        this(System.getenv("TRACER_NAME"));
    }

    public CollectLoggingInterceptor(String tracerName) {
        log.info(String.format("Initializing Tracer name [%s]", tracerName));

        OpenTelemetry openTelemetry = OpenTelemetryConfiguration.initOpenTelemetry();
        this.tracer = openTelemetry.getTracer(tracerName);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        Span span = tracer.spanBuilder(request.getRequestURI()).startSpan();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute(HTTP_METHOD, request.getMethod());
            span.setAttribute(HTTP_URL, request.getRequestURI());
            span.setAttribute(HTTP_STATUS_CODE, response.getStatus());
            span.setAttribute(HTTP_CLIENT_IP, request.getRemoteAddr());
        } finally {
            span.end();
        }
    }
}
