package me.ezjs.core.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zero-mac on 16/7/8.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TableName {

    String value() default "";
    
}
