package workspace

class SpecificationResult {

  Workspace workspace;
  Date runDate;

  String status;

  Integer percentSuccess;
  Integer successes;
  Integer failures;
  Integer exceptions;

  static belongsTo = Workspace

  static constraints = {
    workspace(nullable: false)
    runDate(nullable: false, blank: false)
    status(nullable: false, blank: false, inList:["PASSED", "FAILED", "ERROR"] )
  }

  void parseBuildResult(String buildResult) {
    def regexResults = buildResult =~ /Successes: (\d+), Failures: (\d+)(?:, Exceptions: )?(\d*)/

    successes = 0;
    failures = 0;
    exceptions = 0;

    for (result in regexResults) {
      successes += result[1].toInteger()
      failures += result[2].toInteger()
      if(result[3]) {
        exceptions += result[3].toInteger()
      }
    }

    percentSuccess = 0;
    if(successes + failures + exceptions) {
      percentSuccess = (successes / (successes + failures + exceptions)) * 100;
    }

    if(!failures && !exceptions && !successes) {
      status = "ERROR";
    }
    else {
      if(!failures && !exceptions) {
        status = "PASSED";
      }
      else {
        status = "FAILED";
      }
    }
  }
}
