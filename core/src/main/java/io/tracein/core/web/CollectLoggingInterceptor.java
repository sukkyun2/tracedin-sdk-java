package io.tracein.core.web;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class CollectLoggingInterceptor implements HandlerInterceptor {
    private final Tracer tracer;

    public CollectLoggingInterceptor() {
        this(System.getProperty("tracein.tracer.name", "app"));
    }

    public CollectLoggingInterceptor(String tracerName) {
        log.info("initialize open telemetry tracer name :" + tracerName);

        OpenTelemetry openTelemetry = OpenTelemetryConfiguration.initOpenTelemetry();
        this.tracer = openTelemetry.getTracer(tracerName);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        Span span = tracer.spanBuilder(request.getRequestURI()).startSpan();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("REQUEST_METHOD", request.getMethod());
            span.setAttribute("REQUEST_URI", request.getRequestURI());
            span.setAttribute("STATUS_CODE", response.getStatus());

            return true;
        } finally {
            span.end();
        }
    }
}
