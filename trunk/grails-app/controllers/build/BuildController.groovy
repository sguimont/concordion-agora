package build

import workspace.SpecificationResult
import workspace.Workspace

class BuildController {

  def index = { }

  def buildWorkspace = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    def result = workspace.runAllTests();

    SpecificationResult specResult = new SpecificationResult(workspace:workspace, runDate: new Date());
    specResult.parseBuildResult(result);
    specResult.save();

    workspace.lastResult = specResult;

    render(view: "results", model: [result: result])
  }

  def buildMasterWorkspace = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if (!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    def result = workspace.build();
    render(view: "results", model: [result: result])
  }
}
