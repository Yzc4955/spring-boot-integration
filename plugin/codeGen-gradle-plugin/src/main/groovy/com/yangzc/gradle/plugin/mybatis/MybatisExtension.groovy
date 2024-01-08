package com.yangzc.gradle.plugin.mybatis

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * 用于配置 mybatis 生成代码的插件扩展
 */
class MybatisExtension {
    /**
     * mybatis 配置文件路径
     */
    @Optional
    String configFile

    /**
     * 是否覆盖已有文件
     */
    @Optional
    Boolean overwrite = true

    /**
     * 获取 mybatis 配置文件路径
     *
     * @return mybatis 配置文件路径
     */
    @Input
    String getConfigFile() {
        return configFile
    }

    /**
     * 获取是否覆盖已有文件
     *
     * @return 是否覆盖已有文件
     */
    @Input
    Boolean getOverwrite() {
        return overwrite
    }

    /**
     * 设置是否覆盖已有文件
     *
     * @param overwrite 是否覆盖已有文件
     */
    void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite
    }

    /**
     * 设置 mybatis 配置文件路径
     *
     * @param configFile mybatis 配置文件路径
     */
    void setConfigFile(String configFile) {
        this.configFile = configFile
    }
}
