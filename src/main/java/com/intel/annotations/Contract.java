package com.intel.annotations;

public @interface Contract {

	boolean pure() default false;

	String value() default "";

}
