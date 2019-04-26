package os.server.note;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import os.server.type.RequestMethod;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Request {

	String url();

	RequestMethod method() default RequestMethod.GET;
}
