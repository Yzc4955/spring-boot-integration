package com.yangzc.gradle.plugin.mybatis

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.internal.reflect.Instantiator
import org.gradle.invocation.DefaultGradle

import java.nio.file.Files

import static com.yangzc.gradle.constant.Constants.*

class MybatisGenPlugin implements Plugin<Project> {

    private static final String  MYBATIS_GEN = 'mybatisGen';
    private static final String  GEN_SIMPLE_CONFIG = 'genSimpleConfig';
    private static final String  GEN_DETAILED_CONFIG = 'genDetailedConfig';

    /**
     * 项目对象
     */
    private Project project;

    /**
     * `Instantiator` 服务是一个接口，它提供了创建对象的方法。
     * 在 Gradle 中，可以使用 `Instantiator` 服务来创建任何类型的对象。
     */
    private Instantiator instantiator;

    /**
     * 应用插件到项目
     *
     * @param project 要应用插件的项目对象
     */
    @Override
    void apply(Project project) {
        this.project = project;

        // 获取 Instantiator 服务以便在插件中创建对象
        this.instantiator = ((DefaultGradle) project.getGradle()).getServices().get(Instantiator.class)

        // 创建help task
        createHelpTask();

        // 创建 generatorConfigurationGen
        createSimpleConfigGenTask()

        // 创建 createDetailedConfigGenTask
        createDetailedConfigGenTask()

        // 创建 mybatisGen
        createMybatisGenTask()
    }

    /**
     * 创建 help 任务
     */
    void createHelpTask() {
        // 创建任务对象，并在任务执行时打印帮助信息
        Task task = this.project.task('mybatisGenHelp').doLast {
            println "*********************************************";
            println "This is mybatis generate plugin, list tasks: ";
            println "[$MYBATIS_GEN]   mybatis code auto generation";
            println "[$GEN_SIMPLE_CONFIG]   generate a simple configuration file";
            println "[$GEN_DETAILED_CONFIG]   generate a detailed configuration file";
            println "*********************************************";
        };

        // 设置任务分组
        task.setGroup(MYBATIS_GEN_GROUP);
    }

    /**
     * 创建 SimpleConfigGen 任务
     */
    void createSimpleConfigGenTask(){
        // 创建任务对象，并在任务执行时打印帮助信息
        Task task = this.project.task(GEN_SIMPLE_CONFIG).doLast {
            getConfigFile("generatorConfig-simple.xml")
        };

        // 设置任务分组
        task.setGroup(MYBATIS_GEN_GROUP);
    }

    /**
     * 创建 DetailedConfigGen 任务
     */
    void createDetailedConfigGenTask(){
        // 创建任务对象，并在任务执行时打印帮助信息
        Task task = this.project.task(GEN_DETAILED_CONFIG).doLast {
            getConfigFile("generatorConfig-detailed.xml")
        };

        // 设置任务分组
        task.setGroup(MYBATIS_GEN_GROUP);
    }

    /**
     * 创建 mybatisGen 任务
     */
    void createMybatisGenTask() {
        // 创建容器，用于存储类型为 MybatisExtension 的对象
        this.project.container(MybatisExtension.class);
        /**
         * 扩展名称（Extension Name）： 这是扩展对象的名称，你可以在构建脚本中使用这个名称来引用扩展对象
         * 扩展类型（Extension Type）： 这是扩展对象的类型，指定了扩展对象具有的属性和方法
         */
        // 创建一个扩展对象，名称为 mybatisGen，类型为 MybatisExtension
        // MybatisExtension: 用于配置与 Mybatis 生成任务相关属性的容器
        MybatisExtension extension = project.getExtensions().create("mybatisExtension", MybatisExtension.class);

        // 创建任务，名称为 mybatisGen，类型为 MybatisTask
        MybatisTask mybatisTask = this.project.getTasks().create(MYBATIS_GEN, MybatisTask.class);
        // 通过这样的赋值，MybatisTask 任务可以访问 MybatisExtension 对象中定义的属性和配置，以便在任务执行期间使用这些信息。
        mybatisTask.mybatisExtension = extension;

        // 设置任务分组
        mybatisTask.setGroup(MYBATIS_GEN_GROUP);
    }

    private void getConfigFile(String configName){
        def mainSourceSet = project.sourceSets.main
        def resourcesDir = mainSourceSet.resources.srcDirs.first()
        def defaultConfigFile = project.file("$resourcesDir/$configName")
        def defaultConfigContent = defaultConfigFile.text

        // 构建文件路径，将文件生成到当前模块的根目录
        File configFile = project.file(DEFAULT_CONFIG_NAME)

        // 将默认配置写入文件
        Files.write(configFile.toPath(), defaultConfigContent.getBytes())

        println "*********************************************";
        println "** generator config file $DEFAULT_CONFIG_NAME is created"
        println "*********************************************";
    }
}
