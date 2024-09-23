package io.github.tracedin.logging;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class JoinPointExtractAdapter {
    private final ProceedingJoinPoint joinPoint;

    public String extractMethodName() {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getName();
    }

    public Map<String, Object> extractParameters() {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        return IntStream.range(0, parameterNames.length)
                .boxed()
                .filter(i -> Objects.nonNull(args[i]))
                .collect(Collectors.toMap(i -> parameterNames[i], i -> args[i]));
    }
}
