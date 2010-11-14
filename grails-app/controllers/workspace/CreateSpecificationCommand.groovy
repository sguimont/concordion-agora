package workspace

class CreateSpecificationCommand {
  def String workspaceIdentifier
  def String folder
  def String name

  static constraints = {
    workspaceIdentifier(nullable: false, blank: false, length: 1..20)
    folder(nullable: false, blank: false, length: 1..255)
    name(nullable: false, blank: false, length: 1..50)
  }

  def execute() {
    Workspace workspace = Workspace.findByIdentifier(workspaceIdentifier);

    def javaFilename = workspace.generatePath("src/test/java/" + folder + "/" + name + ".java");
    File javaFile = new File(javaFilename);
    if(javaFile.exists()) {
      return false;
    }

    def specificationFilename = workspace.generatePath("src/test/resources/" + folder + "/" + name + ".html");
    File specificationFile = new File(specificationFilename);
    if(specificationFile.exists()) {
      return false;
    }

    def formattedName = name.getAt(0).toUpperCase() + name.substring(1);

    javaFile.mkdirs();
    javaFile.write ("""package ${folder.replaceAll("/", ".")};

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class ${formattedName}Test {
  // TODO
}
""");

    specificationFile.mkdirs();
    specificationFile.write("""<html xmlns:concordion="http://www.concordion.org/2007/concordion">
	<head>
        <link href="../concordion.css" rel="stylesheet" type="text/css" />
    </head>
	<body>
   </body>
</html>""");

    return true;
  }
}