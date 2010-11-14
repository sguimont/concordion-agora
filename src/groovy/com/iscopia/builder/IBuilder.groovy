package com.iscopia.builder

import workspace.Builder
import workspace.Workspace

public interface IBuilder {
  String runOneTest(Builder builder, Workspace workspace, File filePath);
  String runAllTests(Builder builder, Workspace workspace);
  String build(Builder builder, Workspace workspace);
}