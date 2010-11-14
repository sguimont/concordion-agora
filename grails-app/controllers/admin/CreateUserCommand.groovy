package admin

import org.apache.shiro.crypto.hash.Sha512Hash

class CreateUserCommand {
  def String username
  def String password
  def String email

  static constraints = {
    username(nullable: false, blank: false, length: 1..25)
    password(nullable: false, blank: false, length: 1..50)
    email(nullable: false, blank: false, email: true, length: 1..255)
  }

  def execute() {
    def adminRole = ShiroRole.findByName("Administrator");

    ShiroUser user = new ShiroUser(username: username, passwordHash: new Sha512Hash(password).toHex(), email: email)
    user.addToRoles(adminRole);
    user.addToPermissions("*:*")
    user.save()

    return true;
  }
}