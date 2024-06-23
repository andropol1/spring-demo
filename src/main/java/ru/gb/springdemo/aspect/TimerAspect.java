package ru.gb.springdemo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimerAspect {
	@Pointcut("within(@ru.gb.springdemo.aspect.Timer *)")
	public void annotatedClass() {
	}

	@Pointcut("@annotation(ru.gb.springdemo.aspect.Timer)")
	public void annotatedMethod() {
	}

	@Around("annotatedClass() || annotatedMethod()")
	public void timerImpl(ProceedingJoinPoint joinPoint) throws Throwable {
		Long start = System.currentTimeMillis();
		try {
			joinPoint.proceed();
			Long end = System.currentTimeMillis() - start;
			log.info("Название класса: {}, название метода: {}, время выполнения: {}",
					joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), end);

		} catch (Throwable e) {
			log.info(e.getMessage());
			throw e;
		}

	}
}
