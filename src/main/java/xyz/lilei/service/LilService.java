package xyz.lilei.service;

import xyz.lilei.annotation.LlService;

/**
 * @ClassName LlService
 * @Description TODO
 * @Author lilei
 * @Date 30/08/2019 06:52
 * @Version 1.0
 **/

public interface LilService {
    String query(String name, String age);
    String add(String param);
    String update(String param);

    String insert(String param);
}
