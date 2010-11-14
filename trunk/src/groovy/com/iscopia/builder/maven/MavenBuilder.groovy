package com.iscopia.builder.maven

import com.iscopia.builder.IBuilder
import workspace.Builder
import workspace.Workspace

class MavenBuilder implements IBuilder {

  def String runOneTest(Builder builder, Workspace workspace, File filePath) {
    def command = builder.runOneTestCommand.replace("[TEST]", filePath.getName().substring(filePath.getName().lastIndexOf("/") + 1).replace(".html", "").replace("/", ".") + "Test");
    def result = command.execute(null, new File(workspace.generatePath())).text;
    return result;
  }

  def String runAllTests(Builder builder, Workspace workspace) {
    def result = builder.runAllTestCommand.execute(null, new File(workspace.generatePath())).text;
    return result;
  }

  def String build(Builder builder, Workspace workspace) {
    def result = builder.buildCommand.execute(null, new File(workspace.generatePath("platform" + File.separator + "build"))).text;
    return result;
  }
}
