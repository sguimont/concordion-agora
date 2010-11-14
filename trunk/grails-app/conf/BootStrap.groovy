import org.apache.shiro.crypto.hash.Sha512Hash
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl
import workspace.Workspace
import admin.ShiroRole
import admin.ShiroUser
import workspace.Builder
import com.iscopia.builder.maven.MavenBuilder
import workspace.SpecificationResult
import workspace.SourceRepository
import com.iscopia.repository.svn.SubversionSourceRepository

class BootStrap {

  def init = {servletContext ->
    println "Initializing SVN DAV repository (http:// and https://)...";
    DAVRepositoryFactory.setup();

    println "Initializing SVN repository (svn:// and svn+xxx://)...";
    SVNRepositoryFactoryImpl.setup();

    println "Creating admin role...";
    def adminRole = ShiroRole.findByName("Administrator");
    if (!adminRole) {
      adminRole = new ShiroRole(name: "Administrator")
      adminRole.addToPermissions("admin")
      adminRole.save()
    }

    println "Creating editor role...";
    def editorRole = ShiroRole.findByName("Editor");
    if (!editorRole) {
      editorRole = new ShiroRole(name: "Editor")
      editorRole.addToPermissions("editor")
      editorRole.save()
    }

    println "Creating maven builder...";
    def mavenBuilder = Builder.findByCode("maven");
    if (!mavenBuilder) {
      println "Check MAVEN_HOME = " + System.getenv("MAVEN_HOME");
      String mavenHome = System.getenv("MAVEN_HOME");
      mavenBuilder = new Builder(code: "maven", name: "Maven Builder", builderImplementationClass: MavenBuilder.class.getName(),
              runOneTestCommand: mavenHome + File.separator + "bin" + File.separator + "mvn.bat -B -Dtest=[TEST] test",
              runAllTestCommand: mavenHome + File.separator + "bin" + File.separator + "mvn.bat -B clean test",
              buildCommand: mavenHome + File.separator + "bin" + File.separator + "mvn.bat -B -f poms/pom-java.xml clean install -Dmaven.test.skip=true -Dintegration.test.skip=true"
      )
      mavenBuilder.save()
    }

    println "Creating ant builder...";
    def antBuilder = Builder.findByCode("ant");
    if (!antBuilder) {
      println "Check ANT_HOME = " + System.getenv("ANT_HOME");
      String antHome = System.getenv("ANT_HOME");
      antBuilder = new Builder(code: "ant", name: "Ant Builder", builderImplementationClass: AntBuilder.class.getName(),
              runOneTestCommand: antHome + File.separator + "bin" + File.separator + "ant.bat -Dtest=[TEST] test",
              runAllTestCommand: antHome + File.separator + "bin" + File.separator + "ant.bat test",
              buildCommand: antHome + File.separator + "bin" + File.separator + "ant.bat"
      )
      antBuilder.save()
    }

    println "Creating svn source repository...";
    def svnSourceRepository = SourceRepository.findByCode("svn");
    if (!svnSourceRepository) {
      svnSourceRepository = new SourceRepository(code: "svn", name: "Subversion", sourceRepositoryImplementationClass: SubversionSourceRepository.class.getName());
      svnSourceRepository.save()
    }

    println "Creating admin user...";
    def userAdmin = ShiroUser.findByUsername("admin");
    if (!userAdmin) {
      userAdmin = new ShiroUser(username: "admin", passwordHash: new Sha512Hash("admin").toHex(), email: "sguimont@iscopia.com")
      userAdmin.addToRoles(adminRole);
      userAdmin.addToRoles(editorRole);
      userAdmin.addToPermissions("*:*")
      userAdmin.save()
    }

    println "Creating editor user...";
    def editorViewer = ShiroUser.findByUsername("editor");
    if (!editorViewer) {
      editorViewer = new ShiroUser(username: "editor", passwordHash: new Sha512Hash("editor").toHex(), email: "sguimont@iscopia.com")
      editorViewer.addToPermissions("*:*")
      editorViewer.addToRoles(editorRole);
      editorViewer.save()
    }

    println "Creating view user...";
    def userViewer = ShiroUser.findByUsername("viewer");
    if (!userViewer) {
      userViewer = new ShiroUser(username: "viewer", passwordHash: new Sha512Hash("viewer").toHex(), email: "sguimont@iscopia.com")
      userViewer.addToPermissions("*:*")
      userViewer.save()
    }

    println "Creating viewer workspaces...";
    def viewerWorkspace = Workspace.findByIdentifier("VIEWER-PORTAL");
    if (!viewerWorkspace) {
      viewerWorkspace = new Workspace(code: "PORTAL", name: "Portal", user: userViewer, builder: mavenBuilder, sourceRepository: svnSourceRepository, sourceRepositoryUrl: "https://hrasvn1.hra.local/svn/development/V3/portal/trunk/Business/Java/portal-specifications", sourceRepositoryUsername: "seguimont", sourceRepositoryPassword: "Salut000", specificationRootFolder: "src/test/specs", specificationResultFolder: "target/concordion")
      viewerWorkspace.generateIdentifier();
      viewerWorkspace.save()
    }

    println "Creating admin workspaces...";
    def masterWorkspace = Workspace.findByIdentifier("ADMIN-MASTER");
    if (!masterWorkspace) {
      masterWorkspace = new Workspace(code: "MASTER", name: "Master", user: userAdmin, builder: mavenBuilder, sourceRepository: svnSourceRepository, sourceRepositoryUrl: "https://hrasvn1.hra.local/svn/Development/V3/portal/trunk", sourceRepositoryUsername: "seguimont", sourceRepositoryPassword: "Salut000", specificationRootFolder: "src/test/resources", specificationResultFolder: "target/concordion")
      masterWorkspace.generateIdentifier();
      masterWorkspace.save()
    }
    def testWorkspace = Workspace.findByIdentifier("ADMIN-TEST");
    if (!testWorkspace) {
      testWorkspace = new Workspace(code: "TEST", name: "Test", user: userAdmin, builder: mavenBuilder, sourceRepository: svnSourceRepository, sourceRepositoryUrl: "https://hrasvn1.hra.local/svn/Development/V3/Spikes/sguimont/specifications", sourceRepositoryUsername: "seguimont", sourceRepositoryPassword: "Salut000", specificationRootFolder: "src/test/resources", specificationResultFolder: "target/concordion")
      testWorkspace.generateIdentifier();
      testWorkspace.save()

      Random random = new Random()

      SpecificationResult specResult = null;
      (1..20).each {
        specResult = new SpecificationResult(workspace: testWorkspace, runDate: new Date() - (it + 1));
        if (random.nextInt(10) > 4) {
          specResult.successes = 30;
          specResult.failures = 0;
          specResult.exceptions = 0;
          specResult.percentSuccess = 100;
          specResult.status = "PASSED";
        }
        else {
          def failures = random.nextInt(30);
          specResult.successes = 30 - failures;
          specResult.failures = failures;
          specResult.exceptions = 0;
          specResult.percentSuccess = (specResult.successes / (specResult.successes + specResult.failures + specResult.exceptions)) * 100;
          specResult.status = "FAILED";
        }
        specResult.save();
      }

      specResult = new SpecificationResult(workspace: testWorkspace, runDate: new Date() - 1);
      specResult.successes = 27;
      specResult.failures = 3;
      specResult.exceptions = 0;
      specResult.percentSuccess = 85;
      specResult.status = "FAILED";
      specResult.save();

      testWorkspace.lastResult = specResult;
      testWorkspace.save();
    }
  }

  def destroy = {
  }
} 