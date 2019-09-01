package xyz.lilei.service.impl;

import xyz.lilei.annotation.LlService;
import xyz.lilei.service.LilService;

/**
 * @ClassName LilServerImpl
 * @Description TODO
 * @Author lilei
 * @Date 30/08/2019 07:14
 * @Version 1.0
 **/
@LlService("LilServiceImpl")
public class LilServerImpl implements LilService {
    @Override
    public String query(String name, String age) {
        return "name:"+name + ",age:" +age;
    }

    @Override
    public String add(String param) {
        return param;
    }

    @Override
    public String update(String param) {
        return param;
    }

    @Override
    public String insert(String param) {
        return param;
    }
}
