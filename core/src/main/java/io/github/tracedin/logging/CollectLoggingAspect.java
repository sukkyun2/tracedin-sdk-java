package io.github.tracedin.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class CollectLoggingAspect {
    public final Tracer tracer;

    public CollectLoggingAspect(Tracer tracer) {
        this.tracer = tracer;
    }

    @Around("execution(* io.github.tracedin.example..*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        JoinPointExtractAdapter joinPointAdapter = new JoinPointExtractAdapter(joinPoint);
        Span span = tracer.spanBuilder(joinPointAdapter.extractMethodName()).startSpan();
        addAllParameter(span, joinPointAdapter.extractParameters());


        Object result;
        try (Scope scope = span.makeCurrent()) {
            result = joinPoint.proceed();
        } finally {
            span.end();
        }

        return result;
    }

    private void addAllParameter(Span span, Map<String, Object> parameters) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            span.setAttribute("method.args."+entry.getKey(), objectMapper.writeValueAsString(entry.getValue()));
        }
    }
}
