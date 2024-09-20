package io.github.tracedin.logging;

import lombok.Getter;
import lombok.ToString;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@ToString
public class CollectLoggingResult {
    private final int order;
    private final String methodName;
    private final Integer parentOrder;
    private final Map<String, Object> parameters;
    private final LocalDateTime startTime;
    private LocalDateTime endTime;

    public CollectLoggingResult(int order, Integer parentOrder, ProceedingJoinPoint joinPoint) {
        this.order = order;
        this.parentOrder = parentOrder;
        this.startTime = LocalDateTime.now();
        this.parameters = extractParameters(joinPoint);
        this.methodName = extractMethodName(joinPoint);
    }

    public void end() {
        this.endTime = LocalDateTime.now();
    }

    private String extractMethodName(ProceedingJoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getName();
    }

    private Map<String, Object> extractParameters(ProceedingJoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        return IntStream.range(0, parameterNames.length)
                .boxed()
                .collect(Collectors.toMap(i -> parameterNames[i], i -> args[i]));
    }
}
