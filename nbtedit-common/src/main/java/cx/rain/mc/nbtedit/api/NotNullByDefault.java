package cx.rain.mc.nbtedit.api;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.PACKAGE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
public @interface NotNullByDefault {
}
