package com.prototype.type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.prototype.http.constants.RequestMethod;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Request {

	String url();

	RequestMethod method() default RequestMethod.GET;
}
