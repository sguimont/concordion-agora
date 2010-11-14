<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <title>Login</title>
  <jq:jquery>
    $('#username').focus();
  </jq:jquery>
</head>
<body>
<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>
<g:form action="signIn">
  <g:hiddenField name="targetUri" value="${targetUri}"/>
  <table>
    <tbody>
    <tr>
      <td>Username:</td>
      <td><g:textField name="username" value="${username}"/></td>
    </tr>
    <tr>
      <td>Password:</td>
      <td><g:passwordField name="password" value=""/></td>
    </tr>
    <tr>
      <td>Remember me?:</td>
      <td><g:checkBox name="rememberMe" value="${rememberMe}"/></td>
    </tr>
    <tr>
      <td><input type="submit" value="Sign in"/></td>
    </tr>
    </tbody>
  </table>
</g:form>
</body>
</html>
