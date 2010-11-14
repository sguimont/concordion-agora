package workspace

import admin.ShiroUser
import com.iscopia.repository.svn.SubversionSourceRepository
import org.apache.shiro.SecurityUtils

class CreateWorkspaceCommand {
  def String identifier
  def String code
  def String name
  def String sourceRepositoryUrl
  def String sourceRepositoryUsername
  def String sourceRepositoryPassword

  static constraints = {
    code(nullable: false, blank: false, length: 1..20)
    name(nullable: false, blank: false, length: 1..50)
    sourceRepositoryUrl(nullable: false, blank: false, length: 1..255)
    sourceRepositoryUsername(nullable: false, blank: false, length: 1..50)
    sourceRepositoryPassword(nullable: false, blank: false, length: 1..50)
  }

  def execute() {
    Workspace workspace = new Workspace(properties);

    workspace.user = ShiroUser.findByUsername(SecurityUtils.subject.principal);
    workspace.generateIdentifier();

    workspace.save();
    identifier = workspace.identifier;

    SubversionSourceRepository workingCopy = new SubversionSourceRepository();
    def repositoryResult = workingCopy.checkout(workspace, workspace.generatePath());
    println repositoryResult;

    return true;
  }
}
