package ru.gb.springdemo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RecoverExceptionAspect {
	@Pointcut("@annotation(ru.gb.springdemo.aspect.RecoverException)")
	public void annotatedMethod() {
	}

	@Around("annotatedMethod()")
	public Object recoverExImpl(ProceedingJoinPoint joinPoint) throws Throwable {
		RecoverException annotation = ((MethodSignature) joinPoint.getSignature()).getMethod()
				.getAnnotation(RecoverException.class);
		Class<? extends RuntimeException>[] classes = annotation.noRecoverFor();
		try {
			return joinPoint.proceed();
		} catch (Throwable e) {
			if (!Arrays.asList(classes).contains(e)) {
				log.info("Возвращаемое значение: null");
				return null;
			} else {
				throw e;
			}
		}
	}
}
