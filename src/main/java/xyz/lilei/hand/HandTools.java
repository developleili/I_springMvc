package xyz.lilei.hand;

import xyz.lilei.annotation.LlService;
import xyz.lilei.resolver.ArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName handTools
 * @Description TODO
 * @Author lilei
 * @Date 31/08/2019 18:51
 * @Version 1.0
 **/
@LlService("LlHandTool")
public class HandTools implements HandToolService{
    @Override
    public Object[] hand(HttpServletRequest request, HttpServletResponse response, Method method
            , Map<String, Object> beans) {
        Class<?>[] paramClazzs = method.getParameterTypes();
        Object[] args = new Object[paramClazzs.length];
        // 拿到所有实现了ArgumentResolver接口的实例
        Map<String, Object> argResolvers = getInstanceType(beans, ArgumentResolver.class);
        int index = 0;
        AtomicInteger i = new AtomicInteger();
        for (Class<?> param : paramClazzs) {
            // 哪个参数对应了哪个解析类, 使用策略模式
            for (Map.Entry<String, Object> entry : argResolvers.entrySet()) {
                ArgumentResolver ar = (ArgumentResolver) entry.getValue();
                if (ar.support(param, index, method)){
                    args[i.getAndIncrement()] = ar.argumentResolver(request, response, param, index, method);
                }
            }
            index++;
        }
        return args;
    }

    private Map<String, Object> getInstanceType(Map<String, Object> beans, Class<?> type) {
        Map<String, Object> resultBeans = new HashMap<>();
        beans.entrySet().forEach(entry->{
            Class<?>[] interfaces = entry.getValue().getClass().getInterfaces();
            if (interfaces != null && interfaces.length > 0){
                for (Class<?> inf : interfaces) {
                    if (inf.isAssignableFrom(type))
                      resultBeans.put(entry.getKey(), entry.getValue());
                }
            }
        });
        return resultBeans;
    }
}
