package xyz.lilei.servlet;

import xyz.lilei.annotation.LlController;
import xyz.lilei.annotation.LlQualifier;
import xyz.lilei.annotation.LlRequestMapping;
import xyz.lilei.annotation.LlService;
import xyz.lilei.contrller.LilController;
import xyz.lilei.hand.HandToolService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DispatherServlet
 * @Description TODO
 * @Author lilei
 * @Date 30/08/2019 07:22
 * @Version 1.0
 **/
public class DispatcherServlet extends HttpServlet {

    List<String> classNames = new ArrayList<String>(16);
    Map<String, Object> beans = new HashMap<>(16);
    Map<String, Object> handleMap = new HashMap<>(16);

    public DispatcherServlet() {
        System.out.println("实例化成功!");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 拿到浏览器请求的地址
        String uri = req.getRequestURI();
        String context = req.getContextPath();
        String path = uri.replaceAll(context, "");
        Method method = (Method) handleMap.get(path);
        LilController instance = (LilController) beans.get("/" + path.split("/")[1]);

        // 处理器
        HandToolService handTool = (HandToolService) beans.get("LlHandTool");
        Object[] args = handTool.hand(req, resp, method, beans);

        try {
            method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 扫描哪些类需要被实例化 class
        doScanPackage("xyz.lilei");
        classNames.forEach(className->{
            System.out.println(className);
        });
        // 创建所有bean
        doInstance();
        // 依赖注入, 把service反射出来的对象注入controller
        iocDi();
        // 建立一个url和method的映射关系
        llHandlerMapper();
        handleMap.entrySet().forEach(entry->{
            System.out.println(entry.getKey()+ ":" +entry.getValue());
        });
    }

    private void llHandlerMapper() {
        if (beans.entrySet().size() <= 0){
            System.out.println("类没有实例化....");
            return;
        }
        beans.entrySet().forEach(entry->{
            Object instance = entry.getValue();
            // 获取类, 获取类声明了哪些注解
            Class<?> clazz = instance.getClass();
            if (clazz.isAnnotationPresent(LlController.class)){
                LlRequestMapping requestMapping = clazz.getAnnotation(LlRequestMapping.class);
                String classPath = requestMapping.value();
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(LlRequestMapping.class)){
                        LlRequestMapping mapping= method.getAnnotation(LlRequestMapping.class);
                        String methodUrl = mapping.value();
                        handleMap.put(classPath+methodUrl, method);
                    }else {
                        continue;
                    }
                }
            }
        });
    }

    private void iocDi() {
        if (beans.entrySet().size()<=0){
            System.out.println("类没有实例化...........");
            return;
        }
        // 把service注入controller
        beans.entrySet().forEach(entry->{
            Object instance = entry.getValue();
            // 获取类,获取类声明了哪些注解
            Class<?> clazz = instance.getClass();
            if (clazz.isAnnotationPresent(LlController.class)){
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(LlQualifier.class)){
                        LlQualifier qualifier = field.getAnnotation(LlQualifier.class);
                        field.setAccessible(true); // 放开权限
                        String value = qualifier.value();
                        try {
                            field.set(instance, beans.get(value));

                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }else {
                        continue;
                    }

                }
            }
        });

    }

    private void doInstance() {
        if (classNames.size() == 0){
            System.out.println("doScanFailed");
            return;
        }

        //遍历扫描到的class全类名路径, 通过反射创建对象
        classNames.forEach(className ->{
            String cn = className.replaceAll(".class", "");
            try {
                Class<?> clazz = Class.forName(cn);
                if (clazz.isAnnotationPresent(LlController.class)){
                    LlController controller = clazz.getAnnotation(LlController.class);
                    Object instance = clazz.newInstance();
                    LlRequestMapping requestMapping = clazz.getAnnotation(LlRequestMapping.class);
                    String key = requestMapping.value();
                    beans.put(key, instance);
                }else if (clazz.isAnnotationPresent(LlService.class)){
                    LlService service = clazz.getAnnotation(LlService.class);
                    Object instance = clazz.newInstance();
                    beans.put(service.value(), instance);
                }else {
                    // continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // 扫描class类
    private void doScanPackage(String basePackage) {
        // 扫描编译好的项目路径下的所有类
        URL url = this.getClass().getClassLoader().getResource("/" + basePackage.replaceAll("\\.", "/"));
        String fileStr = url.getFile();
        File file = new File(fileStr);
        String[] files = file.list();
        for (String path : files) {
            File filePath= new File(fileStr + path);
            // 如果是路径
            if (filePath.isDirectory())
                doScanPackage(basePackage + "."+path);
            else
                classNames.add(basePackage + "." + filePath.getName());
        }

    }
}
