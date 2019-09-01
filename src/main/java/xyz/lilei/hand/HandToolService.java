package xyz.lilei.hand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @ClassName HandToolService
 * @Description TODO
 * @Author lilei
 * @Date 31/08/2019 18:53
 * @Version 1.0
 **/
public interface HandToolService {
    Object[] hand(HttpServletRequest request, HttpServletResponse response, Method method, Map<String, Object>map);

}
