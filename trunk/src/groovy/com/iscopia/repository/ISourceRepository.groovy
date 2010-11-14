package com.iscopia.repository

import org.tmatesoft.svn.core.wc.SVNStatus
import workspace.Workspace

public interface ISourceRepository {
  SVNStatus status(Workspace workspace, String filePath)

  String log(Workspace workspace, String filePath)

  String revertFile(Workspace workspace, String filePath)

  String revertDirectory(Workspace workspace, String filePath)

  String commit(Workspace workspace, String filePath, String commitComment)

  String checkout(Workspace workspace, String workingPath)

}