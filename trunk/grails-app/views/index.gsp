<html>
<head>
  <title>Welcome to Concordion Agora</title>
  <meta name="layout" content="main"/>
  <style type="text/css">
  #nav {
    margin-top: 20px;
    margin-left: 30px;
    width: 228px;
    float: left;
  }

  .homePagePanel * {
    margin: 0px;
  }

  .homePagePanel .panelBody ul {
    list-style-type: none;
    margin-bottom: 10px;
  }

  .homePagePanel .panelBody h1 {
    text-transform: uppercase;
    font-size: 1.1em;
    margin-bottom: 10px;
  }

  .homePagePanel .panelBody {
    background: url(images/leftnav_midstretch.png) repeat-y top;
    margin: 0px;
    padding: 15px;
  }

  .homePagePanel .panelBtm {
    background: url(images/leftnav_btm.png) no-repeat top;
    height: 20px;
    margin: 0px;
  }

  .homePagePanel .panelTop {
    background: url(images/leftnav_top.png) no-repeat bottom;
    height: 10px;
    margin: 0px;
  }

  h2 {
    margin-top: 15px;
    margin-bottom: 15px;
    font-size: 1.2em;
  }

  #pageBody {
    margin-left: 280px;
    margin-right: 20px;
  }
  </style>
</head>
<body>
<div id="nav">
  <div class="homePagePanel">
    <div class="panelTop"></div>
    <div class="panelBody">
      <h1>Application Status</h1>
      <ul>
        <li>App version: <g:meta name="app.version"></g:meta></li>
        <li>Grails version: <g:meta name="app.grails.version"></g:meta></li>
        <li>JVM version: ${System.getProperty('java.version')}</li>
        <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
        <li>Domains: ${grailsApplication.domainClasses.size()}</li>
        <li>Services: ${grailsApplication.serviceClasses.size()}</li>
        <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
      </ul>
      <h1>Installed Plugins</h1>
      <ul>
        <g:set var="pluginManager" value="${applicationContext.getBean('pluginManager')}"></g:set>
        <g:each var="plugin" in="${pluginManager.allPlugins}">
          <li>${plugin.name} - ${plugin.version}</li>
        </g:each>
      </ul>
    </div>
    <div class="panelBtm"></div>
  </div>
</div>
<div id="pageBody">
  <h1>Welcome to Concordion Agora</h1>

  <div id="controllerList" class="dialog">
    <h2>Master Workspace</h2>
    <ul>
      <li class="controller"><g:link controller="repository" action="createWorkspace" params="${[workspaceName:'master']}">Create/Update Master Workspace</g:link></li>
      <li class="controller"><g:link controller="build" action="buildMasterWorkspace" params="${[workspaceName:'master']}">Build Master Workspace</g:link></li>
    </ul>
    <h2>Test Workspace</h2>
    <ul>
      <li class="controller"><g:link controller="repository" action="createWorkspace" params="${[workspaceName:'test']}">Create/Update Test Workspace</g:link></li>
      <li class="controller"><g:link controller="build" action="buildWorkspace" params="${[workspaceName:'test']}">Build Test Workspace</g:link></li>
    </ul>
    <h2>Test Workspace File Action</h2>
    <ul>
      <li class="controller"><g:link controller="repository" action="revertDirectory" params="${[workspaceName:'test', path:'']}">Revert Workspace</g:link></li>
      <li class="controller"><g:link controller="repository" action="revertFile" params="${[workspaceName:'test', path:'src/test/resources/portal/activity/ActivityStatusTransitions.html']}">Revert File</g:link></li>
      <li class="controller"><g:link controller="repository" action="commitDirectory" params="${[workspaceName:'test', path:'', comment:'TEST WORKPSACE']}">Commit Workspace</g:link></li>
      <li class="controller"><g:link controller="repository" action="commitFile" params="${[workspaceName:'test', path:'src/test/resources/portal/activity/ActivityStatusTransitions.html', comment:'TEST FILE']}">Commit File</g:link></li>
      <li class="controller"><g:link controller="repository" action="showFileLog" params="${[workspaceName:'test', path:'src/test/resources/portal/activity/ActivityStatusTransitions.html', comment:'TEST FILE']}">Show File Log</g:link></li>
    </ul>
    <h2>Specification in Test Workspace</h2>
    <ul>
      <li class="controller"><g:link controller="editor" action="viewTree" params="${[workspaceName:'test']}">View Tree</g:link></li>
    </ul>
    <h2>Administration</h2>
    <ul>
      <li class="controller"><g:link controller="user" action="list">Users</g:link></li>
      <li class="controller"><g:link controller="workspace" action="list">User Workspaces</g:link></li>
    </ul>
  </div>
</div>
</body>
</html>