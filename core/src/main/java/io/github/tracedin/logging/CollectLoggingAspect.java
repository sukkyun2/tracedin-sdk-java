package io.github.tracedin.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class CollectLoggingAspect {
    public static final ThreadLocal<Stack<CollectLoggingResult>> methodStack = ThreadLocal.withInitial(Stack::new);
    public static ThreadLocal<AtomicInteger> methodCounter = ThreadLocal.withInitial(() -> new AtomicInteger(1));

    @Around("execution(* io.github.tracedin..*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        AtomicInteger methodCounter = getMethodCounter();
        Stack<CollectLoggingResult> methodStack = getMethodStack();

        int currentOrder = methodCounter.getAndDecrement();
        Integer parentOrder = methodStack.isEmpty() ? null : methodStack.peek().getOrder();

        CollectLoggingResult methodCall = new CollectLoggingResult(currentOrder, parentOrder, joinPoint);
        methodStack.push(methodCall);

        Object result = joinPoint.proceed();

        methodCall.end();

        return result;
    }

    public static Stack<CollectLoggingResult> getMethodStack() {
        return CollectLoggingAspect.methodStack.get();
    }

    private AtomicInteger getMethodCounter() {
        return CollectLoggingAspect.methodCounter.get();
    }
}
