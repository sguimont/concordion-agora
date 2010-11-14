package workspace

import com.iscopia.builder.IBuilder
import com.iscopia.builder.maven.MavenBuilder

class Builder {

  String code;
  String name;
  String builderImplementationClass;

  String runOneTestCommand;
  String runAllTestCommand;
  String buildCommand;

  static constraints = {
    code(nullable: false, blank: false, length: 1..20)
    name(nullable: false, blank: false, length: 1..50)
    builderImplementationClass(nullable: false, blank: false, length: 1..255, inList: [MavenBuilder.class.getName(), AntBuilder.class.getName()])

    runOneTestCommand(nullable: false, blank: false, length: 1..255)
    runAllTestCommand(nullable: false, blank: false, length: 1..255)
    buildCommand(length: 0..255)
  }

  def build(Workspace workspace) {
    IBuilder builder = Thread.currentThread().contextClassLoader.loadClass(builderImplementationClass).newInstance();
    return builder.build(this, workspace);
  }

  def runAllTests(Workspace workspace) {
    IBuilder builder = Thread.currentThread().contextClassLoader.loadClass(builderImplementationClass).newInstance();
    return builder.runAllTests(this, workspace);
  }

  def runOneTest(Workspace workspace, File testFile) {
    IBuilder builder = Thread.currentThread().contextClassLoader.loadClass(builderImplementationClass).newInstance();
    return builder.runOneTest(this, workspace, testFile);
  }
}
