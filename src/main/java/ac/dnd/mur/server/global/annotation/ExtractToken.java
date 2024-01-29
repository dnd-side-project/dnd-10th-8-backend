package ac.dnd.mur.server.global.annotation;

import ac.dnd.mur.server.auth.domain.model.TokenType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtractToken {
    TokenType tokenType() default TokenType.ACCESS;
}
