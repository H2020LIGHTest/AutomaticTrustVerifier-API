package at.tugraz.iaik.lightest.atv.api.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gavinkim at 2018-12-05
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface TestDescription {
    String value();
}
