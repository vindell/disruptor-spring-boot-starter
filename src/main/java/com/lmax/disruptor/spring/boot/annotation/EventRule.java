package com.lmax.disruptor.spring.boot.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented	
@Inherited		
public @interface EventRule {
	
	/**
	 * Ant风格的事件分发规则表达式,格式为：/event/tags/keys，如：/Event-DC-Output/TagA-Output/**
	 * @return 规则表达式
	 */
	String value() default "*";
	
}
