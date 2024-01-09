package com.yangzc.mybatisGen.plugin;

import org.mybatis.generator.api.PluginAdapter;

import java.util.List;

public class MyCustomPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> list) {
        return false;
    }
}
