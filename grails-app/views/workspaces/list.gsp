<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Workspaces</title>
  <meta name="layout" content="main"/>

  <jq:jquery>
    $('#workspaces').addClass('current');
  </jq:jquery>
</head>
<body>

<div id="pageHeader">
  <div class="pageTitle">Workspaces</div>
  <div class="actions">
    <ul>
      <shiro:hasRole name="Editor">
        <li><g:link controller="workspaces" action="create">Create Workspace</g:link></li>
      </shiro:hasRole>
    </ul>
  </div>
  <div style="clear:both;"></div>
</div>

<table class="workspaceList">
  <thead>
  <tr>
    <th>Name</th>
    <th>User</th>
    <th>Last Result</th>
  </tr>
  </thead>
  <tbody>
  <g:each var="workspace" in="${workspaces}">
    <tr>
      <td><g:link class="listAction" controller="workspaces" action="view" id="${workspace.identifier}">${workspace.name}</g:link></td>
      <td>${workspace.user.username}</td>
      <td class="${workspace?.lastResult?.status?.toLowerCase()}">
        <g:if test="${workspace?.lastResult}">
          <g:formatDate format="yyyy-MM-dd @ HH:mm" date="${workspace?.lastResult?.runDate}"/> - ${workspace?.lastResult?.status} : ${workspace?.lastResult?.percentSuccess}%
        </g:if>
        <g:else>
          &nbsp;
        </g:else>
      </td>
    </tr>
  </g:each>
  </tbody>
</table>

</body>
</html>