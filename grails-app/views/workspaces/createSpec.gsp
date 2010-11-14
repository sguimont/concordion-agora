<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Create Specification</title>
  <meta name="layout" content="main"/>

  <jq:jquery>
    $('#workspaces').addClass('current');

	$('#createSpecification form').validate({
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
  <div class="pageTitle">Create Specification</div>
  <div class="actions"></div>
  <div style="clear:both;"></div>
</div>

<div id="createWorkspace">
  <div id="container"><div id="container-inner">
  <g:form id="createWorkspaceForm" controller="workspaces" action="completeCreateSpecification">
    <g:hiddenField name="workspaceIdentifier" value="${cmd.workspaceIdentifier}"/>
    <fieldset>
      <p class="introduction">Cool!</p>
      <p class="note">Fields marked with an asterisk (<abbr title="Required field">*</abbr>) are required.</p>
    </fieldset>
    <fieldset>
      <legend>General</legend>
      <div>
        <label for="folder">Folder <abbr title="Required field">*</abbr></label>
        <g:textField name="folder" value="${cmd.folder}"/>
      </div>
      <div>
        <label for="name">Name <abbr title="Required field">*</abbr></label>
        <g:textField name="name" value="${cmd.name}"/>
      </div>
    </fieldset>
      <div class="controls">
        <g:submitButton name="submit" value="Save"/>
      </div>
    </fieldset>
  </g:form>
  </div></div>
</div>

</body>
</html>