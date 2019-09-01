package xyz.lilei.resolver;

import xyz.lilei.annotation.LlRequestParam;
import xyz.lilei.annotation.LlService;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @ClassName HttpServletRequestA
 * @Description TODO
 * @Author lilei
 * @Date 31/08/2019 20:07
 * @Version 1.0
 **/
@LlService("requestParamArgResolver")
public class RequestParamArgResolver implements ArgumentResolver{
    @Override
    public boolean support(Class<?> type, int index, Method method) {
        Annotation[][] anno = method.getParameterAnnotations();
        Annotation[] paramAnnos = anno[index];
        for (Annotation paramAnno : paramAnnos) {
            if (LlRequestParam.class.isAssignableFrom(paramAnno.getClass())){
                return true;
            }
        }
        return false;
    }

    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int index, Method method) {
        Annotation[][] anno = method.getParameterAnnotations();
        Annotation[] paramAnnos = anno[index];
        for (Annotation paramAnno : paramAnnos) {
            if (LlRequestParam.class.isAssignableFrom(paramAnno.getClass())){
                LlRequestParam ll = (LlRequestParam) paramAnno;
                String value = ll.value();
                return request.getParameter(value);
            }
        }
        return null;
    }
}
