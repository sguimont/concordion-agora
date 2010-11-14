package workspace

import admin.ShiroUser
import grails.converters.JSON
import org.apache.shiro.SecurityUtils
import org.tmatesoft.svn.core.wc.SVNStatus
import org.tmatesoft.svn.core.wc.SVNStatusType

class WorkspacesController {
  def index = {
    def currentUser = ShiroUser.findByUsername(SecurityUtils.subject.principal);
    def workspaces = Workspace.findAllByUser(currentUser)
    render(view: "list", model: [workspaces: workspaces])
  }

  def startJob = {
    jobs.DailyRunJob.triggerNow();
    render "DailyRunJob started"
  }

  def createSpecification = {CreateSpecificationCommand cmd ->
    cmd.workspaceIdentifier = session.currentWorkspaceIdentifier
    render(view: "createSpec", model: [cmd: cmd])
  }

  def completeCreateSpecification = {CreateSpecificationCommand cmd ->
    if (cmd.hasErrors()) {
      render(view: "createSpec", model: [cmd: cmd])
    }
    else {
      if (cmd.execute()) {
        redirect(action: "view", id: cmd.workspaceIdentifier);
      }
      else {
        render(view: "createSpec", model: [cmd: cmd])
      }
    }
  }

  def create = {
    CreateWorkspaceCommand cmd = new CreateWorkspaceCommand();
    render(view: "create", model: [cmd: cmd])
  }

  def completeCreate = {CreateWorkspaceCommand cmd ->
    if (cmd.hasErrors()) {
      render(view: "create", model: [cmd: cmd])
    }
    else {
      if (cmd.execute()) {
        redirect(action: "index");
      }
      else {
        render(view: "create", model: [cmd: cmd])
      }
    }
  }

  def view = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    session.currentWorkspaceIdentifier = params.id;

    def rawGraphResults = SpecificationResult.findAllByWorkspace(workspace);
    def graphResults = [];
    for (result in rawGraphResults) {
      graphResults << ["date": g.formatDate(date: result.runDate, format: "yyyy-MM-dd"), "value": result.percentSuccess]
    }

    render(view: "view", model: [workspace: workspace, graphResults: graphResults])
  }

  def viewFileContent = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    File pathFile = new File(params.path, new File(workspace.generatePath()));
    render pathFile.exists() ? pathFile.text : "DON'T EXIST";
  }

  def viewTreeContents = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    if (params.root == 'source') {
      def root = [];

      File rootFolder = new File(workspace.generateRootSpecificationFolder());
      def files = rootFolder.listFiles(
              [accept: {File file -> return file.isDirectory() ? file.name != '.svn' : file.name.endsWith(".html") }] as FileFilter
      );

      files.each() {
        if (it.isDirectory()) {
          root.push(['text': it.name, 'classes': 'folder', 'hasChildren': 'true', 'id': it.name]);
        }
        else {
          SVNStatus status = workspace.status(it.getAbsolutePath());
          def classes = "file";
          if (status.getContentsStatus().getID() == SVNStatusType.STATUS_MODIFIED.getID()) {
            classes += " modified";
          }
          if (status.getContentsStatus().getID() == SVNStatusType.STATUS_UNVERSIONED.getID()) {
            classes += " unversioned";
          }

          root.push(['text': "<a id='${workspace.specificationResultFolder + File.separator + it.name}' class='tree' href='${g.createLink(controller: 'workspaces', action: 'edit', id: params.id, params: [path: workspace.specificationRootFolder + File.separator + it.name])}'>${it.name}</a>", 'classes': classes]);
        }
      }

      render root as JSON;
    }
    else {
      File rootFolder = new File(workspace.generateSpecificationPath(params.root));

      def root = [];
      def files = rootFolder.listFiles(
              [accept: {file -> return file.isDirectory() ? file.name != '.svn' : file.name.endsWith(".html") }] as FileFilter
      );
      files.each() {
        if (it.isDirectory()) {
          root.push(['text': it.name, 'classes': 'folder', 'hasChildren': 'true', 'id': params.root + "\\" + it.name]);
        }
        else {
          SVNStatus status = workspace.status(it.getAbsolutePath());
          def classes = "file";
          if (status.getContentsStatus().getID() == SVNStatusType.STATUS_MODIFIED.getID()) {
            classes += " modified";
          }
          if (status.getContentsStatus().getID() == SVNStatusType.STATUS_UNVERSIONED.getID()) {
            classes += " unversioned";
          }

          root.push(['text': "<a id='${workspace.specificationResultFolder + File.separator + params.root + File.separator + it.name}' class='tree' href='${g.createLink(controller: 'workspaces', action: 'edit', id: params.id, params: [path: workspace.specificationRootFolder + File.separator + params.root + File.separator + it.name])}'>${it.name}</a>", 'classes': classes]);
        }
      }

      render root as JSON;
    }
  }

  def edit = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    File pathFile = new File(params.path, new File(workspace.generatePath()));
    if (pathFile.exists()) {
      def result = [path: params.path, value: pathFile.text];
      render result as JSON;
    }
    else {
      def result = [path: params.path, value: "", message: "DONT EXIST"];
      render result as JSON;
    }
  }

  def save = {
    jobs.DailyRunJob.triggerNow();

    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    File pathFile = new File(params.path, new File(workspace.generatePath()));
    if (pathFile.exists()) {
      def before = """<html xmlns:concordion="http://www.concordion.org/2007/concordion">
	<head>
        <link href="../concordion.css" rel="stylesheet" type="text/css" />
    </head>
	<body>
"""
      def after = """</body>
</html>
"""
      pathFile.write(before + params.editor + after);

      def result = [path: params.path, value: before + params.editor + after, message: "File saved..."];
      render result as JSON;
    }
    else {
      def result = [path: params.path, value: '', message: "NOT FOUND!!!"];
      render result as JSON;
    }
  }

  def execute = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    File pathFile = new File(params.path, new File(workspace.generatePath()));
    if (pathFile.exists()) {
      def testResult = workspace.runOneTest(pathFile)
      def result = [path: params.path, value: testResult, message: "Execute tests"];
      render result as JSON;
    }
    else {
      render "DON'T EXIST";
    }
  }

  def showFileLog = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    def repositoryResult = workspace.log(workspace.generatePath(params.path));

    def result = [path: params.path, value: repositoryResult, message: "File logs retreived successfully"];
    render result as JSON;
  }

  def revertFile = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    def repositoryResult = workspace.revertFile(workspace.generatePath(params.path));

    File pathFile = new File(params.path, new File(workspace.generatePath()));

    def result = [path: params.path, specification: pathFile.text, value: repositoryResult, message: "File reverted successfully"];
    render result as JSON;
  }

  def commitFile = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    def repositoryResult = workspace.commit(workspace.generatePath(params.path), params.comment);

    def result = [path: params.path, value: repositoryResult, message: "File commited successfully"];
    render result as JSON;
  }
}