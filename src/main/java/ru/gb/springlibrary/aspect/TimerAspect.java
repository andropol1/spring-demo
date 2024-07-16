package ru.gb.springlibrary.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Аспект, указывающий время выполнения метода, над которым стоит аннотация Timer
 */
@Aspect
@Component
@Slf4j
public class TimerAspect {
	@Pointcut("within(@ru.gb.springlibrary.aspect.Timer *)")
	public void annotatedClass() {
	}

	@Pointcut("@annotation(ru.gb.springlibrary.aspect.Timer)")
	public void annotatedMethod() {
	}

	@Around("annotatedClass() || annotatedMethod()")
	public Object timerImpl(ProceedingJoinPoint joinPoint) throws Throwable {
		Long start = System.currentTimeMillis();
		try {
			Object proceed = joinPoint.proceed();
			Long end = System.currentTimeMillis() - start;
			log.info("Название класса: {}, название метода: {}, время выполнения: {}",
					joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), end);
			return proceed;

		} catch (Throwable e) {
			log.info(e.getMessage());
			throw e;
		}

	}
}
