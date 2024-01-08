package com.yangzc.gradle.plugin.mybatis

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import static com.yangzc.gradle.constant.Constants.*

/**
 * MybatisTask 是一个自定义的 Gradle 任务，用于生成 Mybatis 代码。
 */
class MybatisTask extends DefaultTask {

    /**
     * 可选属性：将 @Optional 注解应用于任务的属性，那么这个属性就变成了可选属性。这表示在构建脚本中不设置这个属性也不会导致构建失败。
     * 可选方法：将 @Optional 注解应用于任务的方法，那么这个方法变成了可选方法。在任务执行时，Gradle 不会要求这个方法一定要被调用。
     * Mybatis 插件的扩展对象
     */
    @Optional
    public MybatisExtension mybatisExtension

    /**
     * Nested：标记 Gradle 任务（Task）中的属性，指示这些属性是嵌套的，即它们是另一个对象的属性
     * 获取 Mybatis 插件的扩展对象
     *
     * @return Mybatis 插件的扩展对象
     */
    @Nested
    MybatisExtension getMybatisExtension() {
        return mybatisExtension;
    }

    /**
     * 任务执行的动作方法
     */
    @TaskAction
    void generate() {
        // 获取生成器配置文件路径
        String generatorConfig = mybatisExtension.configFile;
        if (null == generatorConfig || 0 == generatorConfig.length()) {
            // 如果未指定生成器配置文件，则使用默认配置文件 'generatorConfig.xml'
            println "use default generator config file $DEFAULT_CONFIG_NAME "

            generatorConfig = DEFAULT_CONFIG_NAME

            // 输出提示信息
            println "Generated default configuration file: $generatorConfig"
        }

        println "** configfile $generatorConfig";
        println "** overwrite $mybatisExtension.overwrite";

        // 构建命令行参数列表
        List<String> args = new ArrayList<>();
        args.add("-configfile");
        args.add(generatorConfig);
        if (mybatisExtension.overwrite) {
            args.add("-overwrite");
        }

        // 调用 MybatisGenerator 的 exec 方法执行代码生成
        MybatisGenerator.exec(args.toArray(new String[args.size()]));
    }

    /**
     * 设置生成器配置文件
     *
     * @param generatorConfig 生成器配置文件
     */
    void setGeneratorConfig(String generatorConfig) {
        this.generatorConfig = generatorConfig;
    }
}
