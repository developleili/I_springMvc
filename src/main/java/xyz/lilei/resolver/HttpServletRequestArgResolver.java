package xyz.lilei.resolver;

import xyz.lilei.annotation.LlService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @ClassName HttpServletRequestA
 * @Description TODO
 * @Author lilei
 * @Date 31/08/2019 20:07
 * @Version 1.0
 **/
@LlService("httpServletRequestArgResolver")
public class HttpServletRequestArgResolver implements ArgumentResolver{
    @Override
    public boolean support(Class<?> type, int index, Method method) {
        return ServletRequest.class.isAssignableFrom(type);
    }

    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int index, Method method) {
        return request;
    }
}
