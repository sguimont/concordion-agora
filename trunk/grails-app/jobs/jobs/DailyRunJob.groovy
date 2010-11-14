package jobs

import workspace.SpecificationResult
import workspace.Workspace


class DailyRunJob {
  def mailService;

  static triggers = {
    cron name: 'daily', cronExpression: "0 0 1 * * ?"
  }

  def execute() {
    println "Start daily jobs at " + new Date().toString();

    def workspaceList = Workspace.findAll();
    for (Workspace workspace in workspaceList) {
      try {
        println "Update all tests for '${workspace.name}'";
        def updateLog = workspace.checkout();
        println "Run all tests for '${workspace.name}'";
        def testLog = workspace.runAllTests();

        SpecificationResult specResult = new SpecificationResult(workspace: workspace, runDate: new Date());
        specResult.parseBuildResult(testLog);
        specResult.save();

        workspace.lastResult = specResult;
        workspace.save();

        def mailResult = mailService.sendMail {
          from "sguimont@iscopia.com"
          to "sguimont@iscopia.com"
          subject "Daily Tests Job : ${workspace.name}"
          body updateLog + "\r\n\r\n\r\n" + testLog
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      finally {
        println "'${workspace.name}' tests completed.";
      }
    }

    println "Daily job completed.";
  }
}
