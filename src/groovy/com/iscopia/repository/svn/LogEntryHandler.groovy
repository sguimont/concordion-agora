package com.iscopia.repository.svn

import org.tmatesoft.svn.core.ISVNLogEntryHandler
import org.tmatesoft.svn.core.SVNLogEntry

class LogEntryHandler implements ISVNLogEntryHandler {
  private PrintWriter pw;

  public LogEntryHandler(PrintWriter pw) {
    this.pw = pw;
  }

  void handleLogEntry(SVNLogEntry svnLogEntry) {
    pw.println(svnLogEntry.getDate().toString() + " - " + svnLogEntry.getAuthor() + " : " + svnLogEntry.getMessage());
  }
}
