package com.iscopia.repository.svn;


import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions
import workspace.Workspace
import org.tmatesoft.svn.core.*
import org.tmatesoft.svn.core.wc.*
import com.iscopia.repository.ISourceRepository

public class SubversionSourceRepository implements ISourceRepository {

  private SVNClientManager ourClientManager;
  private ISVNEventHandler myCommitEventHandler;
  private ISVNEventHandler myUpdateEventHandler;
  private ISVNEventHandler myWCEventHandler;

  public String checkout(Workspace workspace, String workingPath) {
    SVNURL repositoryURL = null;
    try {
      repositoryURL = SVNURL.parseURIEncoded(workspace.sourceRepositoryUrl);
    } catch (SVNException e) {
      throw new RuntimeException("Cannot create url " + workspace.sourceRepositoryUrl, e);
    }

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    myCommitEventHandler = new CommitEventHandler(pw);

    myUpdateEventHandler = new UpdateEventHandler(pw);

    myWCEventHandler = new WCEventHandler(pw);

    DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);

    ourClientManager = SVNClientManager.newInstance(options, workspace.sourceRepositoryUsername, workspace.sourceRepositoryPassword);
    ourClientManager.getCommitClient().setEventHandler(myCommitEventHandler);
    ourClientManager.getUpdateClient().setEventHandler(myUpdateEventHandler);
    ourClientManager.getWCClient().setEventHandler(myWCEventHandler);

    File wcDir = new File(workingPath);
    if (wcDir.exists()) {
      System.out.println("the destination directory '"
              + wcDir.getAbsolutePath() + "' already exists!");
      System.out.println("Updating '" + wcDir.getAbsolutePath() + "'...");
      try {
        update(wcDir, SVNRevision.HEAD, true);
      } catch (SVNException svne) {
        error("error while recursively updating the working copy at '"
                + wcDir.getAbsolutePath() + "'", svne);
      }
      System.out.println("");
    } else {
      System.out.println("the destination directory '"
              + wcDir.getAbsolutePath() + "' doest not exists!");
      wcDir.mkdirs();

      System.out.println("Checking out a working copy from '" + repositoryURL + "'...");
      try {
        checkout(repositoryURL, SVNRevision.HEAD, wcDir, true);
      } catch (SVNException svne) {
        error("error while checking out a working copy for the location '" + repositoryURL + "'", svne);
      }
      System.out.println("");
    }

    return sw.toString();
  }

  public String revertFile(Workspace workspace, String filePath) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    myCommitEventHandler = new CommitEventHandler(pw);

    myUpdateEventHandler = new UpdateEventHandler(pw);

    myWCEventHandler = new WCEventHandler(pw);

    DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);

    ourClientManager = SVNClientManager.newInstance(options, workspace.sourceRepositoryUsername, workspace.sourceRepositoryPassword);
    ourClientManager.getCommitClient().setEventHandler(myCommitEventHandler);
    ourClientManager.getUpdateClient().setEventHandler(myUpdateEventHandler);
    ourClientManager.getWCClient().setEventHandler(myWCEventHandler);

    File wcDir = new File(filePath);
    if (wcDir.exists()) {
      try {
        doRevert(wcDir, SVNRevision.HEAD, false);
      }
      catch (SVNException e) {
        error(e.getMessage(), e);
      }
    } else {
      error("File not exist", null);
    }

    return sw.toString();
  }

  public String log(Workspace workspace, String filePath) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    myCommitEventHandler = new CommitEventHandler(pw);

    myUpdateEventHandler = new UpdateEventHandler(pw);

    myWCEventHandler = new WCEventHandler(pw);

    DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);

    ourClientManager = SVNClientManager.newInstance(options, workspace.sourceRepositoryUsername, workspace.sourceRepositoryPassword);
    ourClientManager.getCommitClient().setEventHandler(myCommitEventHandler);
    ourClientManager.getUpdateClient().setEventHandler(myUpdateEventHandler);
    ourClientManager.getWCClient().setEventHandler(myWCEventHandler);

    File wcDir = new File(filePath);
    if (wcDir.exists()) {
      try {
        doLog(wcDir, new LogEntryHandler(pw));
      }
      catch (SVNException e) {
        error(e.getMessage(), e);
      }
    } else {
      error("File not exist", null);
    }

    return sw.toString();
  }

  public String revertDirectory(Workspace workspace, String filePath) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    myCommitEventHandler = new CommitEventHandler(pw);

    myUpdateEventHandler = new UpdateEventHandler(pw);

    myWCEventHandler = new WCEventHandler(pw);

    DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);

    ourClientManager = SVNClientManager.newInstance(options, workspace.sourceRepositoryUsername, workspace.sourceRepositoryPassword);
    ourClientManager.getCommitClient().setEventHandler(myCommitEventHandler);
    ourClientManager.getUpdateClient().setEventHandler(myUpdateEventHandler);
    ourClientManager.getWCClient().setEventHandler(myWCEventHandler);

    File wcDir = new File(filePath);
    if (wcDir.exists()) {
      try {
        doRevert(wcDir, SVNRevision.HEAD, true);
      }
      catch (SVNException e) {
        error(e.getMessage(), e);
      }
    } else {
      error("File not exist", null);
    }

    return sw.toString();
  }

  public String commit(Workspace workspace, String filePath, String commitComment) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    myCommitEventHandler = new CommitEventHandler(pw);

    myUpdateEventHandler = new UpdateEventHandler(pw);

    myWCEventHandler = new WCEventHandler(pw);

    DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);

    ourClientManager = SVNClientManager.newInstance(options, workspace.sourceRepositoryUsername, workspace.sourceRepositoryPassword);
    ourClientManager.getCommitClient().setEventHandler(myCommitEventHandler);
    ourClientManager.getUpdateClient().setEventHandler(myUpdateEventHandler);
    ourClientManager.getWCClient().setEventHandler(myWCEventHandler);

    File wcDir = new File(filePath);
    if (wcDir.exists()) {
      try {
        commit(wcDir, false, commitComment);
      }
      catch (SVNException e) {
        error(e.getMessage(), e);
      }
    } else {
      error("File not exist", null);
    }

    return sw.toString();
  }

  public SVNStatus status(Workspace workspace, String filePath) {
    DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
    ourClientManager = SVNClientManager.newInstance(options, workspace.sourceRepositoryUsername, workspace.sourceRepositoryPassword);

    SVNStatus result = null;

    try {
      result = ourClientManager.getStatusClient().doStatus(new File(filePath), false);
    }
    catch (SVNException e) {
      error(e.getMessage(), e);
    }

    return result;
  }

  /*
  * Creates a new version controlled directory (doesn't create any intermediate
  * directories) right in a repository. Like 'svn mkdir URL -m "some comment"'
  * command. It's done by invoking
  *
  * SVNCommitClient.doMkDir(SVNURL[] urls, String commitMessage)
  *
  * which takes the following parameters:
  *
  * urls - an array of URLs that are to be created;
  *
  * commitMessage - a commit log message since a URL-based directory creation is
  * immediately committed to a repository.
  */

  private SVNCommitInfo makeDirectory(SVNURL url, String commitMessage) throws SVNException {
    /*
    * Returns SVNCommitInfo containing information on the new revision committed
    * (revision number, etc.)
    */
    return ourClientManager.getCommitClient().doMkDir(new SVNURL[1] {url}, commitMessage);
  }

  /*
  * Imports an unversioned directory into a repository location denoted by a
  * destination URL (all necessary parent non-existent paths will be created
  * automatically). This operation commits the repository to a new revision.
  * Like 'svn import PATH URL (-N) -m "some comment"' command. It's done by
  * invoking
  *
  * SVNCommitClient.doImport(File path, SVNURL dstURL, String commitMessage, boolean recursive)
  *
  * which takes the following parameters:
  *
  * path - a local unversioned directory or singal file that will be imported into a
  * repository;
  *
  * dstURL - a repository location where the local unversioned directory/file will be
  * imported into; this URL path may contain non-existent parent paths that will be
  * created by the repository server;
  *
  * commitMessage - a commit log message since the new directory/file are immediately
  * created in the repository;
  *
  * recursive - if true and path parameter corresponds to a directory then the directory
  * will be added with all its child subdirictories, otherwise the operation will cover
  * only the directory itself (only those files which are located in the directory).
  */

  private SVNCommitInfo importDirectory(File localPath, SVNURL dstURL, String commitMessage,
                                        boolean isRecursive) throws SVNException {
    /*
    * Returns SVNCommitInfo containing information on the new revision committed
    * (revision number, etc.)
    */
    return ourClientManager.getCommitClient().doImport(localPath, dstURL, commitMessage, null, true, false,
            SVNDepth.fromRecurse(isRecursive));
  }

  /*
  * Committs changes in a working copy to a repository. Like
  * 'svn commit PATH -m "some comment"' command. It's done by invoking
  *
  * SVNCommitClient.doCommit(File[] paths, boolean keepLocks, String commitMessage,
  * boolean force, boolean recursive)
  *
  * which takes the following parameters:
  *
  * paths - working copy paths which changes are to be committed;
  *
  * keepLocks - if true then doCommit(..) won't unlock locked paths; otherwise they will
  * be unlocked after a successful commit;
  *
  * commitMessage - a commit log message;
  *
  * force - if true then a non-recursive commit will be forced anyway;
  *
  * recursive - if true and a path corresponds to a directory then doCommit(..) recursively
  * commits changes for the entire directory, otherwise - only for child entries of the
  * directory;
  */

  private SVNCommitInfo commit(File wcPath, boolean keepLocks, String commitMessage)
  throws SVNException {
    /*
    * Returns SVNCommitInfo containing information on the new revision committed
    * (revision number, etc.)
    */
    return ourClientManager.getCommitClient().doCommit([wcPath] as File[], keepLocks, commitMessage,
            null, null, false, false, SVNDepth.INFINITY);
  }

  /*
  * Checks out a working copy from a repository. Like 'svn checkout URL[@REV] PATH (-r..)'
  * command; It's done by invoking
  *
  * SVNUpdateClient.doCheckout(SVNURL url, File dstPath, SVNRevision pegRevision,
  * SVNRevision revision, boolean recursive)
  *
  * which takes the following parameters:
  *
  * url - a repository location from where a working copy is to be checked out;
  *
  * dstPath - a local path where the working copy will be fetched into;
  *
  * pegRevision - an SVNRevision representing a revision to concretize
  * url (what exactly URL a user means and is sure of being the URL he needs); in other
  * words that is the revision in which the URL is first looked up;
  *
  * revision - a revision at which a working copy being checked out is to be;
  *
  * recursive - if true and url corresponds to a directory then doCheckout(..) recursively
  * fetches out the entire directory, otherwise - only child entries of the directory;
  */

  private long checkout(SVNURL url,
                        SVNRevision revision, File destPath, boolean isRecursive)
  throws SVNException {

    SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
    /*
    * sets externals not to be ignored during the checkout
    */
    updateClient.setIgnoreExternals(false);
    /*
    * returns the number of the revision at which the working copy is
    */
    return updateClient.doCheckout(url, destPath, revision, revision, SVNDepth.fromRecurse(isRecursive),
            false);
  }

  /*
  * Updates a working copy (brings changes from the repository into the working copy).
  * Like 'svn update PATH' command; It's done by invoking
  *
  * SVNUpdateClient.doUpdate(File file, SVNRevision revision, boolean recursive)
  *
  * which takes the following parameters:
  *
  * file - a working copy entry that is to be updated;
  *
  * revision - a revision to which a working copy is to be updated;
  *
  * recursive - if true and an entry is a directory then doUpdate(..) recursively
  * updates the entire directory, otherwise - only child entries of the directory;
  */

  private long update(File wcPath, SVNRevision updateToRevision, boolean isRecursive) throws SVNException {

    SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
    /*
    * sets externals not to be ignored during the update
    */
    updateClient.setIgnoreExternals(false);
    /*
    * returns the number of the revision wcPath was updated to
    */
    return updateClient.doUpdate(wcPath, updateToRevision, SVNDepth.fromRecurse(isRecursive), false, false);
  }

  /*
  * Updates a working copy to a different URL. Like 'svn switch URL' command.
  * It's done by invoking
  *
  * SVNUpdateClient.doSwitch(File file, SVNURL url, SVNRevision revision, boolean recursive)
  *
  * which takes the following parameters:
  *
  * file - a working copy entry that is to be switched to a new url;
  *
  * url - a target URL a working copy is to be updated against;
  *
  * revision - a revision to which a working copy is to be updated;
  *
  * recursive - if true and an entry (file) is a directory then doSwitch(..) recursively
  * switches the entire directory, otherwise - only child entries of the directory;
  */

  private long switchToURL(File wcPath, SVNURL url, SVNRevision updateToRevision, boolean isRecursive) throws SVNException {
    SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
    /*
    * sets externals not to be ignored during the switch
    */
    updateClient.setIgnoreExternals(false);
    /*
    * returns the number of the revision wcPath was updated to
    */
    return updateClient.doSwitch(wcPath, url, SVNRevision.UNDEFINED, updateToRevision,
            SVNDepth.getInfinityOrFilesDepth(isRecursive), false, false);
  }

  /*
  * Collects status information on local path(s). Like 'svn status (-u) (-N)'
  * command. It's done by invoking
  *
  * SVNStatusClient.doStatus(File path, boolean recursive,
  * boolean remote, boolean reportAll, boolean includeIgnored,
  * boolean collectParentExternals, ISVNStatusHandler handler)
  *
  * which takes the following parameters:
  *
  * path - an entry which status info to be gathered;
  *
  * recursive - if true and an entry is a directory then doStatus(..) collects status
  * info not only for that directory but for each item inside stepping down recursively;
  *
  * remote - if true then doStatus(..) will cover the repository (not only the working copy)
  * as well to find out what entries are out of date;
  *
  * reportAll - if true then doStatus(..) will also include unmodified entries;
  *
  * includeIgnored - if true then doStatus(..) will also include entries being ignored;
  *
  * collectParentExternals - if true then externals definitions won't be ignored;
  *
  * handler - an implementation of ISVNStatusHandler to process status info per each entry
  * doStatus(..) traverses; such info is collected in an SVNStatus object and
  * is passed to a handler's handleStatus(SVNStatus status) method where an implementor
  * decides what to do with it.
  */

  private void showStatus(File wcPath, boolean isRecursive, boolean isRemote, boolean isReportAll,
                          boolean isIncludeIgnored, boolean isCollectParentExternals, PrintWriter pw)
  throws SVNException {
    /*
    * StatusHandler displays status information for each entry in the console (in the
    * manner of the native Subversion command line client)
    */
    ourClientManager.getStatusClient().doStatus(wcPath, SVNRevision.HEAD, SVNDepth.fromRecurse(isRecursive),
            isRemote, isReportAll, isIncludeIgnored, isCollectParentExternals, new StatusHandler(isRemote, pw),
            null);
  }

  /*
  * Collects information on local path(s). Like 'svn info (-R)' command.
  * It's done by invoking
  *
  * SVNWCClient.doInfo(File path, SVNRevision revision,
  * boolean recursive, ISVNInfoHandler handler)
  *
  * which takes the following parameters:
  *
  * path - a local entry for which info will be collected;
  *
  * revision - a revision of an entry which info is interested in; if it's not
  * WORKING then info is got from a repository;
  *
  * recursive - if true and an entry is a directory then doInfo(..) collects info
  * not only for that directory but for each item inside stepping down recursively;
  *
  * handler - an implementation of ISVNInfoHandler to process info per each entry
  * doInfo(..) traverses; such info is collected in an SVNInfo object and
  * is passed to a handler's handleInfo(SVNInfo info) method where an implementor
  * decides what to do with it.
  */

  private void showInfo(File wcPath, SVNRevision revision, boolean isRecursive, PrintWriter pw) throws SVNException {
    /*
    * InfoHandler displays information for each entry in the console (in the manner of
    * the native Subversion command line client)
    */
    ourClientManager.getWCClient().doInfo(wcPath, SVNRevision.UNDEFINED, revision,
            SVNDepth.getInfinityOrEmptyDepth(isRecursive), null, new InfoHandler(pw));
  }

  private void doRevert(File wcPath, SVNRevision revision, boolean isRecursive) throws SVNException {
    /*
    * InfoHandler displays information for each entry in the console (in the manner of
    * the native Subversion command line client)
    */
    ourClientManager.getWCClient().doRevert([wcPath] as File[], SVNDepth.getInfinityOrEmptyDepth(isRecursive), null);
  }

  private void doLog(File wcPath, ISVNLogEntryHandler isvnLogEntryHandler) throws SVNException {
    /*
    * InfoHandler displays information for each entry in the console (in the manner of
    * the native Subversion command line client)
    */
    ourClientManager.getLogClient().doLog([wcPath] as File[], SVNRevision.create(0), SVNRevision.HEAD, true, false, 0, isvnLogEntryHandler);
  }

  /*
  * Puts directories and files under version control scheduling them for addition
  * to a repository. They will be added in a next commit. Like 'svn add PATH'
  * command. It's done by invoking
  *
  * SVNWCClient.doAdd(File path, boolean force,
  * boolean mkdir, boolean climbUnversionedParents, boolean recursive)
  *
  * which takes the following parameters:
  *
  * path - an entry to be scheduled for addition;
  *
  * force - set to true to force an addition of an entry anyway;
  *
  * mkdir - if true doAdd(..) creates an empty directory at path and schedules
  * it for addition, like 'svn mkdir PATH' command;
  *
  * climbUnversionedParents - if true and the parent of the entry to be scheduled
  * for addition is not under version control, then doAdd(..) automatically schedules
  * the parent for addition, too;
  *
  * recursive - if true and an entry is a directory then doAdd(..) recursively
  * schedules all its inner dir entries for addition as well.
  */

  private void addEntry(File wcPath) throws SVNException {
    ourClientManager.getWCClient().doAdd(wcPath, false, false, false, SVNDepth.INFINITY, false, false);
  }

  /*
  * Locks working copy paths, so that no other user can commit changes to them.
  * Like 'svn lock PATH' command. It's done by invoking
  *
  * SVNWCClient.doLock(File[] paths, boolean stealLock, String lockMessage)
  *
  * which takes the following parameters:
  *
  * paths - an array of local entries to be locked;
  *
  * stealLock - set to true to steal the lock from another user or working copy;
  *
  * lockMessage - an optional lock comment string.
  */

  private void lock(File wcPath, boolean isStealLock, String lockComment) throws SVNException {
    ourClientManager.getWCClient().doLock([wcPath] as File[], isStealLock, lockComment);
  }

  /*
  * Schedules directories and files for deletion from version control upon the next
  * commit (locally). Like 'svn delete PATH' command. It's done by invoking
  *
  * SVNWCClient.doDelete(File path, boolean force, boolean dryRun)
  *
  * which takes the following parameters:
  *
  * path - an entry to be scheduled for deletion;
  *
  * force - a boolean flag which is set to true to force a deletion even if an entry
  * has local modifications;
  *
  * dryRun - set to true not to delete an entry but to check if it can be deleted;
  * if false - then it's a deletion itself.
  */

  private void delete(File wcPath, boolean force) throws SVNException {
    ourClientManager.getWCClient().doDelete(wcPath, force, false);
  }

  /*
  * Duplicates srcURL to dstURL (URL->URL)in a repository remembering history.
  * Like 'svn copy srcURL dstURL -m "some comment"' command. It's done by
  * invoking
  *
  * doCopy(SVNURL srcURL, SVNRevision srcRevision, SVNURL dstURL,
  * boolean isMove, String commitMessage)
  *
  * which takes the following parameters:
  *
  * srcURL - a source URL that is to be copied;
  *
  * srcRevision - a definite revision of srcURL
  *
  * dstURL - a URL where srcURL will be copied; if srcURL & dstURL are both
  * directories then there are two cases:
  * a) dstURL already exists - then doCopy(..) will duplicate the entire source
  * directory and put it inside dstURL (for example,
  * consider srcURL = svn://localhost/rep/MyRepos,
  * dstURL = svn://localhost/rep/MyReposCopy, in this case if doCopy(..) succeeds
  * MyRepos will be in MyReposCopy - svn://localhost/rep/MyReposCopy/MyRepos);
  * b) dstURL doesn't exist yet - then doCopy(..) will create a directory and
  * recursively copy entries from srcURL into dstURL (for example, consider the same
  * srcURL = svn://localhost/rep/MyRepos, dstURL = svn://localhost/rep/MyReposCopy,
  * in this case if doCopy(..) succeeds MyRepos entries will be in MyReposCopy, like:
  * svn://localhost/rep/MyRepos/Dir1 -> svn://localhost/rep/MyReposCopy/Dir1...);
  *
  * isMove - if false then srcURL is only copied to dstURL what
  * corresponds to 'svn copy srcURL dstURL -m "some comment"'; but if it's true then
  * srcURL will be copied and deleted - 'svn move srcURL dstURL -m "some comment"';
  *
  * commitMessage - a commit log message since URL->URL copying is immediately
  * committed to a repository.
  */

  private SVNCommitInfo copy(SVNURL srcURL, SVNURL dstURL, boolean isMove, String commitMessage) throws SVNException {
    /*
    * SVNRevision.HEAD means the latest revision.
    * Returns SVNCommitInfo containing information on the new revision committed
    * (revision number, etc.)
    */
    return ourClientManager.getCopyClient().doCopy(new SVNCopySource[1] {new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, srcURL)},
            dstURL, isMove, true, false, commitMessage, null);
  }

  /*
  * Displays error information and exits.
  */

  private void error(String message, Exception e) {
    System.err.println(message + (e != null ? ": " + e.getMessage() : ""));
    throw new RuntimeException(message, e);
  }

  /*
  * This method does not relate to SVNKit API. Just a method which creates
  * local directories and files :)
  */

  private final void createLocalDir(File aNewDir, File[] localFiles, String[] fileContents) {
    if (!aNewDir.mkdirs()) {
      error("failed to create a new directory '" + aNewDir.getAbsolutePath() + "'.", null);
    }
    for (int i = 0; i < localFiles.length; i++) {
      File aNewFile = localFiles[i];
      try {
        if (!aNewFile.createNewFile()) {
          error("failed to create a new file '"
                  + aNewFile.getAbsolutePath() + "'.", null);
        }
      } catch (IOException ioe) {
        aNewFile.delete();
        error("error while creating a new file '"
                + aNewFile.getAbsolutePath() + "'", ioe);
      }

      String contents = null;
      if (i > fileContents.length - 1) {
        continue;
      }
      contents = fileContents[i];

      /*
      * writing a text into the file
      */
      FileOutputStream fos = null;
      try {
        fos = new FileOutputStream(aNewFile);
        fos.write(contents.getBytes());
      } catch (FileNotFoundException fnfe) {
        error("the file '" + aNewFile.getAbsolutePath() + "' is not found", fnfe);
      } catch (IOException ioe) {
        error("error while writing into the file '"
                + aNewFile.getAbsolutePath() + "'", ioe);
      } finally {
        if (fos != null) {
          try {
            fos.close();
          } catch (IOException ioe) {
            //
          }
        }
      }
    }
  }
}