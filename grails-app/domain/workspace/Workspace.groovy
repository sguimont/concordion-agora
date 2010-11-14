package workspace

import admin.ShiroUser
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.tmatesoft.svn.core.wc.SVNStatus

class Workspace {
  ShiroUser user;
  String identifier
  String code
  String name

  Builder builder;
  SourceRepository sourceRepository;

  String sourceRepositoryUrl
  String sourceRepositoryUsername
  String sourceRepositoryPassword

  String specificationRootFolder;
  String specificationResultFolder;

  SpecificationResult lastResult;

  static belongsTo = ShiroUser

  static hasMany = [results: SpecificationResult]

  static constraints = {
    user(nullable: false)
    identifier(nullable: false, blank: false, length: 1..50, unique: true)
    code(nullable: false, blank: false, length: 1..20)
    name(nullable: false, blank: false, length: 1..50)

    builder(nullable: false)
    sourceRepository(nullable: false)

    sourceRepositoryUrl(nullable: false, blank: false, length: 1..255)
    sourceRepositoryUsername(nullable: false, blank: false, length: 1..50)
    sourceRepositoryPassword(nullable: false, blank: false, length: 1..50)

    specificationRootFolder(nullable: false, blank: false, length: 1..255)
    specificationResultFolder(nullable: false, blank: false, length: 1..255)

    lastResult(nullable: true)
  }

  String generateIdentifier() {
    this.identifier = user.username.toUpperCase() + "-" + this.code.toUpperCase();
  }

  String generatePath() {
    return ConfigurationHolder.config.concordion.agora.workspace.base.path + File.separator + user.username + File.separator + name;
  }

  String generatePath(String path) {
    return ConfigurationHolder.config.concordion.agora.workspace.base.path + File.separator + user.username + File.separator + name + File.separator + path;
  }

  String generateRootSpecificationFolder() {
    return ConfigurationHolder.config.concordion.agora.workspace.base.path + File.separator + user.username + File.separator + name + File.separator + specificationRootFolder;
  }

  String generateSpecificationPath(String path) {
    return ConfigurationHolder.config.concordion.agora.workspace.base.path + File.separator + user.username + File.separator + name + File.separator + specificationRootFolder + File.separator + path;
  }

  String generateResultSpecificationFolder() {
    return ConfigurationHolder.config.concordion.agora.workspace.base.path + File.separator + user.username + File.separator + name + File.separator + specificationResultFolder;
  }

  String generateResultSpecificationPath(String path) {
    return ConfigurationHolder.config.concordion.agora.workspace.base.path + File.separator + user.username + File.separator + name + File.separator + specificationResultFolder + File.separator + path;
  }

  String build() {
    return builder.build(this);
  }

  String runAllTests() {
    return builder.runAllTests(this);
  }

  String runOneTest(File test) {
    return builder.runOneTest(this, test);
  }


  SVNStatus status(String filePath) {
    return sourceRepository.status(this, filePath);
  }

  String log(String filePath) {
    return sourceRepository.log(this, filePath);
  }

  String revertFile(String filePath) {
    return sourceRepository.revertFile(this, filePath);
  }

  String revertDirectory(String filePath) {
    return sourceRepository.revertDirectory(this, filePath);
  }

  String commit(String filePath, String comment) {
    return sourceRepository.commit(this, filePath, comment);
  }

  String checkout() {
    return sourceRepository.checkout(this, generatePath());
  }
}
