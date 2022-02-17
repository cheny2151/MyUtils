package maven;

import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.impl.VersionResolver;
import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.util.repository.AuthenticationBuilder;

/**
 * @author by chenyi
 * @date 2022/1/7
 */
public class Test {

    public static void main(String[] args) throws ArtifactResolutionException {
        RepositoryInfo repositoryInfo = new RepositoryInfo();
        repositoryInfo.setRepository("http://nexus.duowan.com/music/content/repositories/releases");
        repositoryInfo.setTarget("/Users/chenyi/Downloads/repo/");
        ArtifactInfo artifactInfo = new ArtifactInfo("com.joyyinc", "address-matching-api", "0.0.3");
        downloadJar(repositoryInfo, artifactInfo);
    }

    /**
     * 建立RepositorySystem
     *
     * @return RepositorySystem
     */
    private static RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = new DefaultServiceLocator();
//        locator.addService(VersionResolver.class,)
        RepositorySystem service = locator.getService(RepositorySystem.class);
        return service;
    }

    private static RepositorySystemSession newSession(RepositorySystem system, String target) {
        DefaultRepositorySystemSession session = new DefaultRepositorySystemSession();

        LocalRepository localRepo = new LocalRepository(target);
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));

        return session;
    }

    /**
     * 从指定maven地址下载指定jar包
     */
    public static void downloadJar(RepositoryInfo repositoryInfo, ArtifactInfo artifactInfo) throws ArtifactResolutionException {
        String groupId = artifactInfo.getGroupId();
        String artifactId = artifactInfo.getArtifactId();
        String version = artifactInfo.getVersion();
        String repositoryUrl = repositoryInfo.getRepository();
        String target = repositoryInfo.getTarget();
        String username = repositoryInfo.getUsername();
        String password = repositoryInfo.getPassword();


        RepositorySystem repoSystem = newRepositorySystem();
        RepositorySystemSession session = newSession(repoSystem, target);
        RemoteRepository central;
        if (username == null && password == null) {
            central = new RemoteRepository.Builder("central", "default", repositoryUrl).build();
        } else {
            Authentication authentication = new AuthenticationBuilder().addUsername(username).addPassword(password).build();
            central = new RemoteRepository.Builder("central", "default", repositoryUrl).setAuthentication(authentication).build();
        }
        /**
         * 下载一个jar包
         */
        Artifact artifact = new DefaultArtifact(groupId + ":" + artifactId + ":" + version);
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.addRepository(central);
        artifactRequest.setArtifact(artifact);
        ArtifactResult result = repoSystem.resolveArtifact(session, artifactRequest);
        System.out.println("success");


        /**
         * 下载该jar包及其所有依赖jar包
         */
        /*
         *
       	Dependency dependency =
            new Dependency( new DefaultArtifact( artifact ),null);
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot( dependency );
        collectRequest.addRepository( central );
        DependencyNode node = repoSystem.collectDependencies( session, collectRequest ).getRoot();

        DependencyRequest dependencyRequest = new DependencyRequest();
        dependencyRequest.setRoot( node );

        repoSystem.resolveDependencies( session, dependencyRequest  );

        PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
        node.accept( nlg );
        System.out.println( nlg.getClassPath() );*/
    }

}
