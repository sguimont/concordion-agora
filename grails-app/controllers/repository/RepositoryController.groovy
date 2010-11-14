package repository

import workspace.Workspace

class RepositoryController {

  def createWorkspace = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    def repositoryResult = workspace.checkout();

    render(view: "results", model: [result: repositoryResult + "\r\nDONE"])
  }

  def revertFile = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    def repositoryResult = workspace.revertFile(workspace.generatePath(params.path));

    render(view: "results", model: [result: repositoryResult + "\r\nDONE"]);
  }

  def revertDirectory = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    def repositoryResult = workspace.revertDirectory(workspace.generatePath(params.path));

    render(view: "results", model: [result: repositoryResult + "\r\nDONE"]);
  }

  def commitFile = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    def repositoryResult = workspace.commit(workspace.generatePath(params.path), params.comment);

    render(view: "results", model: [result: repositoryResult + "\r\nDONE"]);
  }

  def commitDirectory = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    def repositoryResult = workspace.commit(workspace.generatePath(params.path), params.comment);

    render(view: "results", model: [result: repositoryResult + "\r\nDONE"]);
  }

  def showFileLog = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    def repositoryResult = workspace.log(workspace.generatePath(params.path));

    render(view: "results", model: [result: repositoryResult + "\r\nDONE"]);
  }
}
