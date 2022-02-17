package maven;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author by chenyi
 * @date 2022/1/7
 */
@Data
@AllArgsConstructor
public class ArtifactInfo {

    /**
     * jar包在maven仓库中的groupId
     */
    private String groupId;
    /**
     * jar包在maven仓库中的artifactId
     */
    private String artifactId;
    /**
     * jar包在maven仓库中的version
     */
    private String version;
}
