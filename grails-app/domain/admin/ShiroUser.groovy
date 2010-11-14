package admin

import workspace.Workspace

class ShiroUser {
  String username
  String passwordHash
  String email

  static hasMany = [roles: ShiroRole, permissions: String, workspaces: Workspace]

  static constraints = {
    username(nullable: false, blank: false, length: 1..25)
    email(nullable: false, blank: false, email: true, length: 1..255)
    passwordHash(nullable: false, blank: false, length: 1..255)
  }
}
