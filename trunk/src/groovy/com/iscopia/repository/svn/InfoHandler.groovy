/*
 * ====================================================================
 * Copyright (c) 2004-2009 TMate Software Ltd.  All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.  The terms
 * are also available at http://svnkit.com/license.html
 * If newer versions of this license are posted there, you may use a
 * newer version instead, at your option.
 * ====================================================================
 */
package com.iscopia.repository.svn;


import org.tmatesoft.svn.core.SVNNodeKind
import org.tmatesoft.svn.core.wc.ISVNInfoHandler
import org.tmatesoft.svn.core.wc.SVNInfo

/*
* An implementation of ISVNInfoHandler that is  used  in  WSubversionSourceRepositoryjava  to
* display  info  on  a  working  copy path.  This implementation is passed  to
*
* SVNWCClient.doInfo(File path, SVNRevision revision, boolean recursive,
* ISVNInfoHandler handler)
*
* For each item to be processed doInfo(..) collects information and creates an
* SVNInfo which keeps that information. Then  doInfo(..)  calls  implementor's
* handler.handleInfo(SVNInfo) where it passes the gathered info.
*/
public class InfoHandler implements ISVNInfoHandler {

    private PrintWriter pw;

    public InfoHandler(PrintWriter pw) {
        this.pw = pw;
    }

    /*
     * This is an implementation  of  ISVNInfoHandler.handleInfo(SVNInfo info).
     * Just prints out information on a Working Copy path in the manner of  the
     * native SVN command line client.
     */
    public void handleInfo(SVNInfo info) {
        pw.println("-----------------INFO-----------------");
        pw.println("Local Path: " + info.getFile().getPath());
        pw.println("URL: " + info.getURL());
        if (info.isRemote() && info.getRepositoryRootURL() != null) {
            pw.println("Repository Root URL: "
                    + info.getRepositoryRootURL());
        }
        if(info.getRepositoryUUID() != null){
            pw.println("Repository UUID: " + info.getRepositoryUUID());
        }
        pw.println("Revision: " + info.getRevision().getNumber());
        pw.println("Node Kind: " + info.getKind().toString());
        if(!info.isRemote()){
            pw.println("Schedule: "
                    + (info.getSchedule() != null ? info.getSchedule() : "normal"));
        }
        pw.println("Last Changed Author: " + info.getAuthor());
        pw.println("Last Changed Revision: "
                + info.getCommittedRevision().getNumber());
        pw.println("Last Changed Date: " + info.getCommittedDate());
        if (info.getPropTime() != null) {
            pw
                    .println("Properties Last Updated: " + info.getPropTime());
        }
        if (info.getKind() == SVNNodeKind.FILE && info.getChecksum() != null) {
            if (info.getTextTime() != null) {
                pw.println("Text Last Updated: " + info.getTextTime());
            }
            pw.println("Checksum: " + info.getChecksum());
        }
        if (info.getLock() != null) {
            if (info.getLock().getID() != null) {
                pw.println("Lock Token: " + info.getLock().getID());
            }
            pw.println("Lock Owner: " + info.getLock().getOwner());
            pw.println("Lock Created: "
                    + info.getLock().getCreationDate());
            if (info.getLock().getExpirationDate() != null) {
                pw.println("Lock Expires: "
                        + info.getLock().getExpirationDate());
            }
            if (info.getLock().getComment() != null) {
                pw.println("Lock Comment: "
                        + info.getLock().getComment());
            }
        }
    }
}