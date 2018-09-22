package nebula.intellij.util.annotations;

public @interface Contract {

	boolean pure() default false;

	String value() default "";

}
