package me.ezjs.core.model.orm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 忽略在rootObject对象的字段
 * Created by zero-mac on 17/8/13.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreFieldsInRootObj {

    String[] value() default {};
}
