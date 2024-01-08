package com.yangzc.gradle.plugin

import com.yangzc.gradle.plugin.mybatis.MybatisGenPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginManager implements Plugin<Project> {

    // 项目对象
    private Project project;

    @Override
    void apply(Project project) {
        this.project = project;

        // 应用自定义插件
        applyCustomPlugin();

    }

    /**
     * 将自定义插件应用到项目。
     *
     * @param project 要应用插件的项目。
     */
    private void applyCustomPlugin() {
        // 应用 MybatisGenPlugin 插件
        this.project.getPlugins().apply(MybatisGenPlugin.class);
    }
}
