package workspace

import com.iscopia.repository.ISourceRepository
import org.tmatesoft.svn.core.wc.SVNStatus
import com.iscopia.repository.svn.SubversionSourceRepository

class SourceRepository {

  String code;
  String name;
  String sourceRepositoryImplementationClass;

  static constraints = {
    code(nullable: false, blank: false, length: 1..20)
    name(nullable: false, blank: false, length: 1..50)
    sourceRepositoryImplementationClass(nullable: false, blank: false, length: 1..255, inList: [SubversionSourceRepository.class.getName()])
  }

  SVNStatus status(Workspace workspace, String filePath) {
    ISourceRepository sourceRepository = Thread.currentThread().contextClassLoader.loadClass(sourceRepositoryImplementationClass).newInstance();
    return sourceRepository.status(workspace, filePath);
  }

  String log(Workspace workspace, String filePath) {
    ISourceRepository sourceRepository = Thread.currentThread().contextClassLoader.loadClass(sourceRepositoryImplementationClass).newInstance();
    return sourceRepository.log(workspace, filePath);
  }

  String revertFile(Workspace workspace, String filePath) {
    ISourceRepository sourceRepository = Thread.currentThread().contextClassLoader.loadClass(sourceRepositoryImplementationClass).newInstance();
    return sourceRepository.revertFile(workspace, filePath);
  }

  String revertDirectory(Workspace workspace, String filePath) {
    ISourceRepository sourceRepository = Thread.currentThread().contextClassLoader.loadClass(sourceRepositoryImplementationClass).newInstance();
    return sourceRepository.revertDirectory(workspace, filePath);
  }

  String commit(Workspace workspace, String filePath, String commitComment) {
    ISourceRepository sourceRepository = Thread.currentThread().contextClassLoader.loadClass(sourceRepositoryImplementationClass).newInstance();
    return sourceRepository.commit(workspace, filePath, commitComment);
  }

  String checkout(Workspace workspace, String workingPath) {
    ISourceRepository sourceRepository = Thread.currentThread().contextClassLoader.loadClass(sourceRepositoryImplementationClass).newInstance();
    return sourceRepository.checkout(workspace, workingPath);
  }

}