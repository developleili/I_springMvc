package xyz.lilei.contrller;

import xyz.lilei.annotation.LlController;
import xyz.lilei.annotation.LlQualifier;
import xyz.lilei.annotation.LlRequestMapping;
import xyz.lilei.annotation.LlRequestParam;
import xyz.lilei.service.LilService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName LlController
 * @Description TODO
 * @Author lilei
 * @Date 30/08/2019 06:53
 * @Version 1.0
 **/
@LlController
@LlRequestMapping("/ll")
public class LilController {

    @LlQualifier("LilServiceImpl")
    private LilService lilService;

    @LlRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response
            , @LlRequestParam("name")String name, @LlRequestParam("age")String age) {

        try {
            PrintWriter pw = response.getWriter();
            String result = lilService.query(name, age);
            pw.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @LlRequestMapping("/insert")
    public void insert(HttpServletRequest request,
                       HttpServletResponse response) {
        try {
            PrintWriter pw = response.getWriter();
            String result = lilService.insert("0000");
            pw.write(result);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
