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
 * 表达式解析器
 *
 * @author cheney
 * @date 2019-12-06
 */
@Slf4j
public class ReflectExpressionParser extends BaseExpressionParser {

    private MethodHolderFactory methodHolderFactory;

    private Set<Class<?>> functionClasses;

    private static ReflectExpressionParser defaultReflectExpressionParser;

    private ReflectExpressionParser() {
        ClassLoader classLoader = ReflectExpressionParser.class.getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("func-config.conf");
        functionClasses = new HashSet<>();
        if (resourceAsStream != null) {
            try {
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

    public void addFunctionClass(Class<?> clazz) {
        functionClasses.add(clazz);
    }

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

    public static ReflectExpressionParser getInstance(MethodHolderFactory methodHolderFactory, Collection<Class<?>> classes) {
        if (methodHolderFactory == null || CollectionUtils.isEmpty(classes)) {
            throw new NullPointerException();
        }
        return new ReflectExpressionParser(methodHolderFactory, classes);
    }

}
