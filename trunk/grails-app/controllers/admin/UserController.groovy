package admin

class UserController {
  def scaffold = ShiroUser

  def startCreate = {
    CreateUserCommand cmd = new CreateUserCommand();
    render(view: "create", model: [cmd: cmd])
  }

  def completeCreate = {CreateUserCommand cmd ->
    if (cmd.hasErrors()) {
      render(view: "create", model: [cmd: cmd])
    }
    else {
      if (cmd.execute()) {
        redirect(action: "list");
      }
      else {
        render(view: "create", model: [cmd: cmd])
      }
    }
  }
}
