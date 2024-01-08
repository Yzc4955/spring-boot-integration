package com.yangzc.gradle.plugin.mybatis

import org.gradle.api.GradleException
import org.mybatis.generator.api.MyBatisGenerator
import org.mybatis.generator.api.ProgressCallback
import org.mybatis.generator.api.VerboseProgressCallback
import org.mybatis.generator.config.Configuration
import org.mybatis.generator.config.xml.ConfigurationParser
import org.mybatis.generator.exception.InvalidConfigurationException
import org.mybatis.generator.exception.XMLParserException
import org.mybatis.generator.internal.DefaultShellCallback
import org.mybatis.generator.internal.util.messages.Messages
import org.mybatis.generator.logging.LogFactory

import java.sql.SQLException

/**
 * MyBatis 代码生成器类
 */
public class MybatisGenerator {

    /**
     * 配置文件参数
     */
    private static final String CONFIG_FILE = "-configfile"; //$NON-NLS-1$

    /**
     * 覆盖参数
     */
    private static final String OVERWRITE = "-overwrite"; //$NON-NLS-1$

    /**
     * 上下文 ID 参数
     */
    private static final String CONTEXT_IDS = "-contextids"; //$NON-NLS-1$

    /**
     * 表参数
     */
    private static final String TABLES = "-tables"; //$NON-NLS-1$

    /**
     * 详细参数
     */
    private static final String VERBOSE = "-verbose"; //$NON-NLS-1$

    /**
     * 强制使用 Java 日志参数
     */
    private static final String FORCE_JAVA_LOGGING = "-forceJavaLogging"; //$NON-NLS-1$

    /**
     * 帮助参数 1
     */
    private static final String HELP_1 = "-?"; //$NON-NLS-1$

    /**
     * 帮助参数 2
     */
    private static final String HELP_2 = "-h"; //$NON-NLS-1$

    /**
     * 执行主方法
     *
     * @param args 命令行参数
     */
    static void exec(String[] args) {
        // 如果没有参数，打印用法信息并退出
        if (args.length == 0) {
            usage();
            // 用于终止当前 Java 虚拟机的运行。括号中的参数表示程序的退出状态码。通常，一个非零的状态码表示程序发生了错误，而0表示正常退出。
            System.exit(0);
            return;
        }

        // 解析命令行参数
        Map<String, String> arguments = parseCommandLine(args);

        // 如果包含帮助参数，打印用法信息并退出
        if (arguments.containsKey(HELP_1)) {
            usage();
            System.exit(0);
            return;
        }

        // 如果未指定配置文件路径，打印错误信息并退出
        if (!arguments.containsKey(CONFIG_FILE)) {
            writeLine("错误：未指定配置文件路径，请使用 '-configfile' 参数指定配置文件路径。");
            return;
        }

        // 初始化警告信息列表
        List<String> warnings = new ArrayList<String>();

        // 获取配置文件路径
        String configfile = arguments.get(CONFIG_FILE);
        File configurationFile = new File(configfile);

        // 如果配置文件不存在，打印错误信息并退出
        if (!configurationFile.exists()) {
            writeLine("错误：配置文件不存在: " + configfile);
            throw new GradleException("Error: Configuration file 'generatorConfig.xml' not found.")
        }

        // 初始化表名和上下文集合
        Set<String> fullyqualifiedTables = new HashSet<String>();
        Set<String> contexts = new HashSet<String>();

        // 如果包含表参数，解析并添加到表名集合
        if (arguments.containsKey(TABLES)) {
            StringTokenizer st = new StringTokenizer(arguments.get(TABLES), ",");
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    fullyqualifiedTables.add(s);
                }
            }
        }

        // 如果包含上下文 ID 参数，解析并添加到上下文集合
        if (arguments.containsKey(CONTEXT_IDS)) {
            StringTokenizer st = new StringTokenizer(arguments.get(CONTEXT_IDS), ",");
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    contexts.add(s);
                }
            }
        }

        try {
            // 解析配置文件
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(configurationFile);

            // 初始化回调
            DefaultShellCallback shellCallback = new DefaultShellCallback(arguments.containsKey(OVERWRITE));
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);

            // 初始化进度回调
            ProgressCallback progressCallback = arguments.containsKey(VERBOSE) ? new VerboseProgressCallback() : null;

            // 执行代码生成
            myBatisGenerator.generate(progressCallback, contexts, fullyqualifiedTables);
        } catch (XMLParserException e) {
            writeLine("XML 解析错误: " + e.getMessage());
            writeLine();
            for (String error : e.getErrors()) {
                writeLine(error);
            }
            return;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return;
        } catch (InvalidConfigurationException e) {
            writeLine("配置无效: " + e.getMessage());
            for (String error : e.getErrors()) {
                writeLine(error);
            }
            return;
        } catch (InterruptedException e) {
            // 忽略（使用 DefaultShellCallback 永远不会发生）
        }

        // 打印警告信息
        for (String warning : warnings) {
            writeLine(warning);
        }

        // 打印生成成功或警告信息
        if (warnings.size() == 0) {
            writeLine("代码生成成功。");
        } else {
            writeLine();
            writeLine("代码生成过程中发生警告，请检查详细日志。");
        }
    }

    /**
     * 显示用法信息
     */
    private static void usage() {
        String lines = "10";
        int iLines = Integer.parseInt(lines);
        for (int i = 0; i < iLines; i++) {
            String key = "Usage." + i;
            writeLine(Messages.getString(key));
        }
    }

    /**
     * 打印一行消息
     *
     * @param message 消息内容
     */
    private static void writeLine(String message) {
        System.out.println(message);
    }

    /**
     * 打印空行
     */
    private static void writeLine() {
        System.out.println();
    }

    /**
     * 解析命令行参数
     *
     * @param args 命令行参数
     * @return 解析后的参数映射
     */
    private static Map<String, String> parseCommandLine(String[] args) {
        // 初始化错误信息列表和参数映射
        List<String> errors = new ArrayList<String>();
        Map<String, String> arguments = new HashMap<String, String>();

        // 遍历命令行参数
        for (int i = 0; i < args.length; i++) {
            if (CONFIG_FILE.equalsIgnoreCase(args[i])) {
                if ((i + 1) < args.length) {
                    arguments.put(CONFIG_FILE, args[i + 1]);
                } else {
                    errors.add("缺少配置文件路径参数: " + CONFIG_FILE);
                }
                i++;
            } else if (OVERWRITE.equalsIgnoreCase(args[i])) {
                arguments.put(OVERWRITE, "Y");
            } else if (VERBOSE.equalsIgnoreCase(args[i])) {
                arguments.put(VERBOSE, "Y");
            } else if (HELP_1.equalsIgnoreCase(args[i])) {
                arguments.put(HELP_1, "Y");
            } else if (HELP_2.equalsIgnoreCase(args[i])) {
                // 将 HELP_1 也放入映射中，这样我们只需在主线中检查一个条目
                arguments.put(HELP_1, "Y");
            } else if (FORCE_JAVA_LOGGING.equalsIgnoreCase(args[i])) {
                LogFactory.forceJavaLogging();
            } else if (CONTEXT_IDS.equalsIgnoreCase(args[i])) {
                if ((i + 1) < args.length) {
                    arguments.put(CONTEXT_IDS, args[i + 1]);
                } else {
                    errors.add("缺少上下文 ID 参数: " + CONTEXT_IDS);
                }
                i++;
            } else if (TABLES.equalsIgnoreCase(args[i])) {
                if ((i + 1) < args.length) {
                    arguments.put(TABLES, args[i + 1]);
                } else {
                    errors.add("缺少表参数: " + TABLES);
                }
                i++;
            } else {
                errors.add("未知的参数: " + args[i]);
            }
        }

        // 如果存在错误，打印错误信息并退出
        if (!errors.isEmpty()) {
            for (String error : errors) {
                writeLine(error);
            }
            System.exit(-1);
        }

        return arguments;
    }
}
