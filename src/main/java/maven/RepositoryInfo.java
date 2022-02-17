package maven;

import lombok.Data;

/**
 * @author by chenyi
 * @date 2022/1/7
 */
@Data
public class RepositoryInfo {

    /**
     * 远程maven仓库的URL地址
     */
    private String repository;
    /**
     * 下载的jar包存放的目标地址
     */
    private String target;
    /**
     * 登录远程maven仓库的用户名，若远程仓库不需要权限，设为null，默认为null
     */
    private String username;
    /**
     * 登录远程maven仓库的密码，若远程仓库不需要权限，设为null，默认为null
     */
    private String password;

}
