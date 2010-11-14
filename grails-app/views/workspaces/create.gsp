<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Create Workspace</title>
  <meta name="layout" content="main"/>

  <jq:jquery>
    $('#workspaces').addClass('current');

	$('#createWorkspace form').validate({
	rules : {
		'name' : {
			required : true
		},
		'code' : {
			required : true
		},
		'sourceRepositoryUrl' : {
			required : true
		},
		'sourceRepositoryUsername' : {
			required : true
		},
		'sourceRepositoryPassword' : {
			required : true
		}
	}});
  </jq:jquery>
</head>
<body>

<div id="pageHeader">
  <div class="pageTitle">Create Workspace</div>
  <div class="actions"></div>
  <div style="clear:both;"></div>
</div>

<div id="createWorkspace">
  <div id="container"><div id="container-inner">
  <g:form id="createWorkspaceForm" controller="workspaces" action="completeCreate">
    <fieldset>
      <p class="introduction">Hi there! We're excited to have you as a part of our community. To get started, please create an account.</p>
      <p class="note">Fields marked with an asterisk (<abbr title="Required field">*</abbr>) are required.</p>
    </fieldset>
    <fieldset>
      <legend>General</legend>
      <div>
        <label for="name">Name <abbr title="Required field">*</abbr></label>
        <input type="text" name="name" id="name"/>
      </div>
      <div>
        <label for="code">Code <abbr title="Required field">*</abbr></label>
        <input type="text" name="code" id="code"/>
      </div>
    </fieldset>
    <fieldset>
      <legend>Source Repository</legend>
      <div>
        <label for="sourceRepositoryUrl">Url <abbr title="Required field">*</abbr></label>
        <input type="text" name="sourceRepositoryUrl" id="sourceRepositoryUrl"/>
      </div>
      <div>
        <label for="sourceRepositoryUsername">Username <abbr title="Required field">*</abbr></label>
        <input type="text" name="sourceRepositoryUsername" id="sourceRepositoryUsername"/>
      </div>
      <div>
        <label for="sourceRepositoryPassword">Password <abbr title="Required field">*</abbr></label>
        <input type="text" name="sourceRepositoryPassword" id="sourceRepositoryPassword"/>
      </div>
      <div class="controls">
        <input id="submit" name="submit" type="submit" value="Save"/>
      </div>
    </fieldset>
  </g:form>
  </div></div>
</div>

</body>
</html>