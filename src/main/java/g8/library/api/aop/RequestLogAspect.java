package g8.library.api.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

@Aspect
@Configuration
public class RequestLogAspect {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Before("execution(* g8.library.api.controller.LibraryController.*(..))")
	public void traceIncommingRequest(JoinPoint joinpoint) {
		logger.info("Method=" + joinpoint.getSignature().getName() + ": " + joinpoint.getArgs());
	}

	@AfterReturning(pointcut="execution(* g8.library.api.controller.LibraryController.*(..)))", returning="response")
	public void traceReturnMessage(ResponseEntity<?> response) {
		logger.info("Returned: " + response);
	}
}
