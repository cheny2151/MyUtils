package expression.cheney;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import reflect.methodHolder.DefaultMethodHolderFactory;
import reflect.methodHolder.MethodHolderFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 反射表达式解析器
 * 通过解析表达式后用反射的方式结合Aviator执行表达式
 *
 * @author cheney
 * @date 2019-12-06
 */
@Slf4j
public class ReflectExpressionParser extends BaseExpressionParser {

    /**
     * 反射方法工厂
     */
    private MethodHolderFactory methodHolderFactory;

    /**
     * 执行反射的类
     */
    private Set<Class<?>> functionClasses;

    /**
     * ReflectExpressionParser单例
     */
    private static ReflectExpressionParser defaultReflectExpressionParser;

    private ReflectExpressionParser() {
        ClassLoader classLoader = ReflectExpressionParser.class.getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("func-config.conf");
        functionClasses = new HashSet<>();
        if (resourceAsStream != null) {
            try {
                // 读取func-config.conf配置中的类
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
                String classesStr = bufferedReader.readLine();
                String[] classStrArray = classesStr.split(",");
                for (String classStr : classStrArray) {
                    functionClasses.add(classLoader.loadClass(classStr));
                }
            } catch (Exception e) {
                log.error("ReflectExpressionParser初始化异常", e);
            }
        }
        methodHolderFactory = new DefaultMethodHolderFactory();
    }

    public ReflectExpressionParser(MethodHolderFactory methodHolderFactory, Collection<Class<?>> classes) {
        this.methodHolderFactory = methodHolderFactory;
        this.functionClasses = new HashSet<>(classes);
    }

    public ExpressionExecutor parseExpression(String expression) {
        ParseResult parseResult = parse(expression);
        return parseResult.isNoFunc() ? AviatorExpressionParser.getInstance().parseExpression(expression)
                : new ReflectExpressionExecutor(expression, parseResult.getFuncName(),
                parseResult.getArgs(), this.methodHolderFactory, this.functionClasses);
    }

    /**
     * 动态添加方法反射类
     *
     * @param clazz 类
     */
    public void addFunctionClass(Class<?> clazz) {
        functionClasses.add(clazz);
    }

    /**
     * 获取默认单例
     */
    public static ReflectExpressionParser getInstance() {
        if (defaultReflectExpressionParser == null) {
            synchronized (ReflectExpressionParser.class) {
                if (defaultReflectExpressionParser == null) {
                    defaultReflectExpressionParser = new ReflectExpressionParser();
                }
            }
        }
        return defaultReflectExpressionParser;
    }

    /**
     * 获取新的ReflectExpressionParser解析器实例
     */
    public static ReflectExpressionParser getInstance(MethodHolderFactory methodHolderFactory, Collection<Class<?>> classes) {
        if (methodHolderFactory == null || CollectionUtils.isEmpty(classes)) {
            throw new NullPointerException();
        }
        return new ReflectExpressionParser(methodHolderFactory, classes);
    }

}
