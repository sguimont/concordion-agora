<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Edit</title>
  <meta name="layout" content="main" />
</head>
<body>
  <div>${path}</div>
  <g:form controller="editor" action="save">
    <g:hiddenField name="path" value="${path}"/>
    <g:textArea name="value">${value}</g:textArea>
    <g:submitButton name="Save"/>
  </g:form>
</body>
</html>