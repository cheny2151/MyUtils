package reflect.methodHolder.model;

import reflect.methodHolder.exception.FindNotUniqueMethodException;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
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
     * 相同的方法签名会通过继承关系，方法所属类等选中最优方法
     *
     * @param method 方法
     * @return 添加结果
     * @throws IllegalArgumentException 添加方法，出现不合法的相同签名的方法时抛出异常
     */
    public boolean add(Method method) {
        if (equalMethodName(method.getName()) && isOwnerMethod(method)) {
            MetaMethod newMethod = new MetaMethod(method);
            Class<?> returnType = newMethod.getReturnType();
            Class<?> declaringClass = method.getDeclaringClass();
            boolean add = true;
            for (Iterator<MetaMethod> iterator = metaMethods.iterator(); iterator.hasNext(); ) {
                MetaMethod next = iterator.next();
                if (next.getSignature().equals(newMethod.getSignature())) {
                    // 签名相同(方法名，参数类型相同)
                    Class<?> nextReturnType = next.getReturnType();
                    if (nextReturnType.equals(returnType)) {
                        // 返回类型相同,证明存在方法重写，优先取方法所属类是子类重写的方法
                        Class<?> nextDeclaringClass = next.getMethod().getDeclaringClass();
                        if (nextDeclaringClass.isAssignableFrom(declaringClass)) {
                            iterator.remove();
                        } else if (declaringClass.isAssignableFrom(nextDeclaringClass)) {
                            add = false;
                        } else {
                            throw new IllegalArgumentException("Can not add method.Because the method signature and return type is the same");
                        }
                    } else if (nextReturnType.isAssignableFrom(returnType)) {
                        // 返回类型不同并且存在重写方法并修改返回类型为子类，取子类重写的方法
                        iterator.remove();
                    } else if (returnType.isAssignableFrom(nextReturnType)) {
                        add = false;
                    } else {
                        // 一个类中，排除继承方法重写后，不应该出现签名相同，但是返回类型不同的方法
                        throw new IllegalArgumentException("Can not add method.Because the method signature is the same,but the return type has no parent-child relationship");
                    }
                    break;
                }
            }
            return add && metaMethods.add(newMethod);
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
     * 桥接方法：桥接{@link #exactMethodByArgs(java.util.Set, java.lang.Class[])}
     *
     * @param parameterTypes 参数类型数组
     * @return 匹配的方法
     */
    public Method exactMethodByArgs(Class<?>... parameterTypes) {
        return exactMethodByArgs(metaMethods, parameterTypes);
    }

    /**
     * 完全精确匹配方法:
     * 通过方法名和参数类型确切获取方法
     * 1.完全匹配方法签名（最佳）
     * 2.匹配方法参数是入参类型或入参的父类
     * 匹配1成功立刻返回，匹配2成功一次后继续尝试匹配1，最终无法匹配1则返回第一次匹配2成功的方法
     *
     * @param metaMethods    用于查询的元方法集合
     * @param parameterTypes 参数类型数组
     * @return 匹配的方法
     */
    private Method exactMethodByArgs(Set<MetaMethod> metaMethods, Class<?>... parameterTypes) {
        Method bestMatch = null;
        String targetSignature = mockSignature(methodName, parameterTypes);
        int tarArgCount = parameterTypes == null ? 0 : parameterTypes.length;
        for (MetaMethod metaMethod : metaMethods) {
            if (metaMethod.getSignature().equals(targetSignature)) {
                // 完全匹配为最匹配,参数个数为0也会在此匹配到
                bestMatch = metaMethod.getMethod();
                break;
            } else if (bestMatch == null) {
                // 只需匹配过一次
                if (metaMethod.getArgsNum() == tarArgCount) {
                    Class<?>[] curArgTypes = metaMethod.getMethod().getParameterTypes();
                    boolean match = true;
                    for (int i = 0; i < tarArgCount; i++) {
                        // 匹配入参是方法参数类型或者方法参数类型的子类
                        if (!curArgTypes[i].isAssignableFrom(parameterTypes[i])) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        bestMatch = metaMethod.getMethod();
                    }
                }
            }
        }
        return bestMatch;
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
        Set<MetaMethod> selectMethods = metaMethods.stream().filter(e -> e.getReturnType().equals(returnType)).collect(Collectors.toSet());
        return selectMethods.size() == 0 ? null : exactMethodByArgs(selectMethods, parameterTypes);
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
        if (parameterTypes != null && parameterTypes.length > 0) {
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

}
