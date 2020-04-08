package reflect.methodHolder.model;

import org.apache.commons.lang.StringUtils;
import reflect.methodHolder.exception.FindNotUniqueMethodException;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 以方法名为分组的方法元数据
 *
 * @author cheney
 * @date 2020-04-01
 */
public class MetaMethodCollect {

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法所属类(注意不一定是DeclaringClass)
     */
    private Class<?> owner;

    /**
     * 此方法名下的所有方法
     */
    private Set<MetaMethod> metaMethods;

    public MetaMethodCollect(Class<?> owner, String methodName) {
        if (owner == null || methodName == null) {
            throw new IllegalArgumentException();
        }
        this.owner = owner;
        this.methodName = methodName;
        this.metaMethods = new HashSet<>();
    }

    public MetaMethodCollect(Class<?> owner, Method method) {
        if (owner == null || method == null) {
            throw new IllegalArgumentException();
        }
        this.owner = owner;
        this.methodName = method.getName();
        this.metaMethods = new HashSet<>();
        this.metaMethods.add(new MetaMethod(method));
    }

    /**
     * 添加方法
     *
     * @param method 方法
     * @return 添加结果
     */
    public boolean add(Method method) {
        if (equalMethodName(method.getName()) && isOwnerMethod(method)) {
            return metaMethods.add(new MetaMethod(method));
        }
        return false;
    }

    /**
     * 方式是否为owner或子类的方法
     *
     * @param method 方法
     * @return 是否为owner的方法
     */
    private boolean isOwnerMethod(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        return declaringClass.equals(owner) || declaringClass.isAssignableFrom(owner);
    }

    /**
     * 方法名与入参是否相同
     *
     * @param methodName 方法名
     * @return 是否相同
     */
    public boolean equalMethodName(String methodName) {
        return this.methodName.equals(methodName);
    }

    /**
     * 通过方法名确切获取方法，获取到多个方法时抛异常
     *
     * @return 方法
     * @throws FindNotUniqueMethodException 获取到多个方法时抛异常
     */
    public Method exactMethodByName() {
        int size = metaMethods.size();
        if (size > 1) {
            throw new FindNotUniqueMethodException("method name '" + methodName + "' has not unique method in class :" + this.owner.getName());
        }
        return metaMethods.stream().findFirst().map(MetaMethod::getMethod).orElse(null);
    }

    /**
     * 通过方法名和返回类型，确切获取方法
     *
     * @param returnType 返回类型
     * @return 方法
     * @throws FindNotUniqueMethodException 获取到多个方法时抛异常
     */
    public Method exactMethodByReturn(Class<?> returnType) {
        List<MetaMethod> findResult = metaMethods.stream()
                .filter(metaMethod -> returnType.equals(metaMethod.getReturnType()))
                .collect(Collectors.toList());
        if (findResult.size() > 1) {
            throw new FindNotUniqueMethodException("method name '" + methodName +
                    "' and return type '" + returnType.getSimpleName() +
                    "' has not unique method in class :" + this.owner.getName());
        }
        return findResult.size() == 0 ? null : findResult.get(0).getMethod();
    }

    /**
     * 通过方法名和参数个数确切获取方法，获取到多个方法时抛异常
     *
     * @param argsNum 参数个数
     * @return 方法
     * @throws FindNotUniqueMethodException 获取到多个方法时抛异常
     */
    public Method exactMethodByArgsNum(int argsNum) {
        List<MetaMethod> findResult = metaMethods.stream()
                .filter(metaMethod -> metaMethod.getArgsNum() == argsNum)
                .collect(Collectors.toList());
        if (findResult.size() > 1) {
            throw new FindNotUniqueMethodException("method name '" + methodName +
                    "' and args number = " + argsNum +
                    "' has not unique method in class :" + this.owner.getName());
        }
        return findResult.size() == 0 ? null : findResult.get(0).getMethod();
    }

    /**
     * 完全精确匹配方法:
     * 通过方法名和参数类型确切获取方法
     *
     * @param parameterTypes 参数类型
     * @return 方法
     */
    public Method exactMethodByArgs(Class<?>... parameterTypes) {
        List<MetaMethod> findResult = metaMethods.stream()
                .filter(e -> e.getSign().equals(mockSignature(methodName, parameterTypes)))
                .collect(Collectors.toList());
        return findResult.size() == 0 ? null : findResult.get(0).getMethod();
    }

    /**
     * 完全精确匹配方法：
     * 通过方法名、返回类型和参数类型，确切获取方法
     * 此方法必定匹配一个或者匹配不到
     *
     * @param parameterTypes 参数类型
     * @param methodName     方法名
     * @param returnType     返回类型
     * @return 方法
     */
    public Method exactMethod(Class<?> returnType, Class<?>... parameterTypes) {
        List<MetaMethod> findResult = metaMethods.stream()
                .filter(metaMethod -> metaMethod.getSign().equals(mockSignature(methodName, parameterTypes)) && returnType.equals(metaMethod.getReturnType()))
                .collect(Collectors.toList());
        return findResult.size() == 0 ? null : findResult.get(0).getMethod();
    }

    /**
     * 根据用户提供的信息推测方式匹配方法，不保证结果百分比准确
     * 除了方法名外 其他非必填
     *
     * @param returnType 返回类型，可为null
     * @param args       参数类型，可为空
     * @return 推测匹配的方法
     */
    public Method speculateMethod(Class<?> returnType, Class<?>... args) {
        // 尝试精准匹配返回类型
        if (returnType != null) {
            // 通过返回类型和参数类型精准匹配,null当作空处理
            Method result = exactMethod(returnType, args == null ? new Class[]{} : args);
            if (result != null) {
                // 完全精确匹配成功
                return result;
            } else {
                try {
                    // 匹配不中时尝试通过返回值匹配唯一方法
                    return exactMethodByReturn(returnType);
                } catch (FindNotUniqueMethodException e) {
                    // if args type contains Object.class,try by args number
                    if (args != null && containsObject(args)) {
                        try {
                            result = exactMethodByArgsNum(args.length);
                            if (returnType.equals(result.getReturnType())) {
                                return result;
                            }
                        } catch (FindNotUniqueMethodException e2) {
                            // try next;
                        }
                    }
                }
            }
        } else if (args != null) {
            // 通过参数类型精准匹配
            Method result;
            result = exactMethodByArgs(args);
            if (result != null) {
                return result;
            } else {
                try {
                    if (containsObject(args)) {
                        return exactMethodByArgsNum(args.length);
                    }
                } catch (FindNotUniqueMethodException e) {
                    // try next
                }
            }
        }

        // 无法通过返回值或者参数类型推断,直接匹配方法名,当有多个方法时抛出异常，信息不足，推断失败。
        return exactMethodByName();
    }

    /**
     * 通过方法名与参数类型拼接方法签名，不包含返回类型
     *
     * @param methodName     方法名
     * @param parameterTypes 参数类型
     * @return 方法签名
     */
    public String mockSignature(String methodName, Class<?>... parameterTypes) {
        StringBuilder builder = new StringBuilder();
        builder.append(methodName);
        if (parameterTypes.length > 0) {
            builder.append(":");
            String args = Stream.of(parameterTypes)
                    .map(Class::getName)
                    .collect(Collectors.joining(","));
            builder.append(args);
        }
        return builder.toString();
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?> getOwner() {
        return owner;
    }

    public Set<MetaMethod> getMetaMethods() {
        return metaMethods;
    }

    /**
     * 是否包含Object
     *
     * @param classes 类型数组
     * @return 是否包含
     */
    private boolean containsObject(Class<?>[] classes) {
        for (Class<?> c : classes) {
            if (Object.class.equals(c)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Class<StringUtils> clazz = StringUtils.class;
        MetaMethodCollect methodCollect = new MetaMethodCollect(clazz, clazz.getMethod("strip", String.class));
        for (Method method : clazz.getMethods()) {
            methodCollect.add(method);
            methodCollect.add(method);
        }
        Method strip = methodCollect.speculateMethod(null, Object.class);
        System.out.println(strip);
    }
}
