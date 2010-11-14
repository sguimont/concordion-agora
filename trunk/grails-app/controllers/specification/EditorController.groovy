package specification

import workspace.Workspace

class EditorController {
  def index = { }

  def view = {
    Workspace workspace = Workspace.findByIdentifier(params.id)
    if(!workspace) {
      render "Workspace '${params.id}' don't exist!"
    }

    render(view:"viewTree", model: [workspace:workspace])
  }

}
