package xyz.lilei.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.lang.reflect.Method;

/**
 * @ClassName ArgumentResolver
 * @Description TODO
 * @Author lilei
 * @Date 31/08/2019 19:47
 * @Version 1.0
 **/
public interface ArgumentResolver {
    // 判断是否为当前需要解析的类
    boolean support(Class<?> type, int index, Method method);

    // 参数解析
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?>type, int index, Method method);
}
