package front

class DashboardController {

  def index = {
    render(view: "dashboard", model: [])
  }
}
