<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Dashboard</title>
  <meta name="layout" content="main"/>
  <jq:jquery>
    $('#dashboard').addClass('current');
  </jq:jquery>
</head>
<body>
<div id="pageHeader" style="width: 100%;padding-bottom: 5px;margin: 10px;">
  <div style="float:left;" class="pageTitle">Dashboard</div>
  <div style="float:right;" class="actions">
    <ul>
    </ul>
  </div>
  <div style="clear:both;"></div>
</div>
<div id="pageBody">
  <div id="controllerList" class="dialog">
    <ul>
      <shiro:hasRole name="Admin">
        <li class="controller"><g:link url="index.gsp">Old Index</g:link></li>
      </shiro:hasRole>
    </ul>
  </div>
</div>
</body>
</html>