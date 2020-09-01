package cache.factory;

import cache.queryer.EntityQueryer;
import cache.queryer.JpaEntityQueryer;
import cache.queryer.MybatisEntityQueryer;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import spring.SpringUtils;

import java.util.Collection;

/**
 * 实体查询器的自动选择器
 *
 * @author cheney
 * @date 2020-09-01
 */
public class EntityQueryerChooser {

    private boolean inSprintEnv;

    private Object sqlSession;

    public EntityQueryerChooser() {
        inSprintEnv = SpringUtils.getEnvironment() != null;
    }

    public EntityQueryerChooser(Object sqlSession) {
        this.inSprintEnv = SpringUtils.getEnvironment() != null;
        this.sqlSession = sqlSession;
    }

    public EntityQueryer getEntityQueryer() {
        MybatisEntityQueryer mybatisEntityQueryer = testMybatis();
        if (mybatisEntityQueryer != null) {
            return mybatisEntityQueryer;
        }
        JpaEntityQueryer jpaEntityQueryer = testJpa();
        if (jpaEntityQueryer != null) {
            return jpaEntityQueryer;
        }
        // todo other support
        return null;
    }

    /**
     * 测试是否为mybatis环境
     *
     * @return mybatis实体查询器
     */
    private MybatisEntityQueryer testMybatis() {
        Class<?> sqlSessionFactoryClass;
        try {
            sqlSessionFactoryClass = Class.forName("org.apache.ibatis.session.SqlSessionFactory");
        } catch (ClassNotFoundException e) {
            return null;
        }
        // spring environment
        if (inSprintEnv) {
            Collection<?> sqlSessionFactoryInSpringEnv = SpringUtils.getBeansOfType(SqlSessionTemplate.class);
            if (sqlSessionFactoryInSpringEnv.size() > 0) {
                SqlSession sqlSession = (SqlSession) sqlSessionFactoryInSpringEnv.iterator().next();
                return new MybatisEntityQueryer(sqlSession);
            }
        }
        if (this.sqlSession != null && this.sqlSession instanceof SqlSession) {
            return new MybatisEntityQueryer((SqlSession) this.sqlSession);
        }
        return null;
    }

    /**
     * 测试是否为jpa环境
     *
     * @return jpa实体查询器
     */
    private JpaEntityQueryer testJpa() {
        Class<?> entityManagerClass;
        try {
            entityManagerClass = Class.forName("javax.persistence.EntityManager");
        } catch (ClassNotFoundException e) {
            return null;
        }
        return null;
    }
}
