package me.ezjs.core.model.orm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * * 对使用该注解的field忽略camel(驼峰式) -> _(下划线) 的转化
 * <p>
 * Created by zero-mac on 17/8/13.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreCamelFormat {
}
