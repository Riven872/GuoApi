package com.guoapi.project.provider.impl;

import com.guoapi.project.provider.DemoProvider;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class DemoProviderImpl implements DemoProvider {
    @Override
    public String foo(String name) {
        System.out.println("调用成功" + name);
        return "foo" + name;
    }
}
