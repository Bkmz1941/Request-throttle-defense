package ru.bkmz1994.http.aop.throttle;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.bkmz1994.http.annotations.ThrottleDefense;
import ru.bkmz1994.http.exceptions.classes.ThrottleException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class ThrottleAspect {
    private final ConcurrentHashMap<String, List<Long>> requestCounts = new ConcurrentHashMap<>();

    @Before("@annotation(ru.bkmz1994.http.annotations.ThrottleDefense)")
    public void rateLimit(JoinPoint joinPoint) {
        ThrottleDefense throttleDefenseAnnotation = this.retrieveThrottleDefenseAnnotation(joinPoint);

        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final String IP = requestAttributes.getRequest().getRemoteAddr();
        final long currentTime = System.currentTimeMillis();
        requestCounts.putIfAbsent(IP, new ArrayList<>());
        requestCounts.get(IP).add(currentTime);
        cleanUpRequestCounts(currentTime, throttleDefenseAnnotation.duration());
        if (requestCounts.get(IP).size() > throttleDefenseAnnotation.limit()) throw new ThrottleException();
    }

    private ThrottleDefense retrieveThrottleDefenseAnnotation(final JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(ThrottleDefense.class);
    }

    private void cleanUpRequestCounts(final long currentTime, final long duration) {
        requestCounts.values().forEach(l -> {
            l.removeIf(t -> timeIsTooOld(currentTime, t, duration));
        });
    }

    private boolean timeIsTooOld(final long currentTime, final long timeToCheck, final long duration) {
        return currentTime - timeToCheck > duration;
    }
}
