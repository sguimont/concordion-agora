<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Create User</title>
  <meta name="layout" content="main"/>

  <jq:jquery>
	$('#createUser form').validate({
	rules : {
		'username' : {
			required : true
		},
		'password' : {
			required : true
		},
		'email' : {
			required : true
		}
	}});
  </jq:jquery>
</head>
<body>

<div id="pageHeader">
  <div class="pageTitle">Create User</div>
  <div class="actions"></div>
  <div style="clear:both;"></div>
</div>

<div id="createUser">
  <div id="container"><div id="container-inner">
  <g:form id="createUserForm" controller="user" action="completeCreate">
    <fieldset>
      <p class="introduction">Create an user account to enable the user to runs/creates specifications.</p>
      <p class="note">Fields marked with an asterisk (<abbr title="Required field">*</abbr>) are required.</p>
    </fieldset>
    <fieldset>
      <legend>General</legend>
      <div>
        <label for="username">Username <abbr title="Required field">*</abbr></label>
        <input type="text" name="username" id="username"/>
      </div>
      <div>
        <label for="password">Password <abbr title="Required field">*</abbr></label>
        <input type="password" name="password" id="password"/>
      </div>
      <div>
        <label for="email">Email <abbr title="Required field">*</abbr></label>
        <input type="email" name="email" id="email"/>
      </div>
    </fieldset>
      <div class="controls">
        <input id="submit" name="submit" type="submit" value="Save"/>
      </div>
    </fieldset>
  </g:form>
  </div></div>
</div>

</body>
</html>