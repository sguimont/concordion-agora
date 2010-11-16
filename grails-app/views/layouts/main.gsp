<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.codehaus.groovy.grails.commons.ApplicationHolder" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title><g:layoutTitle default="Concordion Agora"/></title>

  <meta http-equiv="Pragma" content="no-cache"/>
  <meta http-equiv="Expires" content="-1"/>

  <link rel="shortcut icon" type="image/x-icon" href="${resource(dir: 'images', file: 'favicon.ico')}"/>

  <g:javascript library="jquery"/>
  <jqui:resources />

  <link rel="stylesheet" type="text/css" href="${resource(dir: 'css/treeview', file: 'jquery.treeview.css')}"/>
  <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'main.css')}"/>
  <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'styles.css')}"/>

  <!--[if IE]><script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery', file: 'excanvas.js')}"></script><![endif]-->
  <link rel="stylesheet" type="text/css" href="${resource(dir: 'css/jqplot', file: 'jquery.jqplot.css')}"/>

  <!--[if lte IE 8]> <link rel="stylesheet" type="text/css" href="${resource(dir: 'css/form', file: 'ie.css')}"/></script><![endif]-->
  <link rel="stylesheet" type="text/css" href="${resource(dir: 'css/form', file: 'screen.css')}"/>


  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery', file: 'jquery.jqplot.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery/jqplot-plugins', file: 'jqplot.categoryAxisRenderer.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery/jqplot-plugins', file: 'jqplot.barRenderer.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery/jqplot-plugins', file: 'jqplot.dateAxisRenderer.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery/jqplot-plugins', file: 'jqplot.highlighter.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery/jqplot-plugins', file: 'jqplot.canvasTextRenderer.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery/jqplot-plugins', file: 'jqplot.canvasAxisTickRenderer.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery/jqplot-plugins', file: 'jqplot.cursor.js')}"></script>

  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery', file: 'jquery.prettydate.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery', file: 'jquery.treeview.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery', file: 'jquery.treeview.async.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery', file: 'jquery.form.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery', file: 'jquery.contextmenu.r2.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery', file: 'jquery.livequery.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery', file: 'jquery.passroids.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery', file: 'jquery.selectbox.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/jquery', file: 'jquery.validate.pack.js')}"></script>

  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/tiny_mce', file: 'tiny_mce.js')}"></script>
  <script language="javascript" type="text/javascript" src="${resource(dir: 'js/tiny_mce', file: 'jquery.tinymce.js')}"></script>

  <p:dependantJavascript>
    <jq:jquery>
  $('tr:even').css('background-color', '#fafafa');

  $("button, input:submit, input:button, #pageHeader .actions a").button();

  // Selectbox styling
  if ($('form select').size()) {
    $('select').selectbox();
    // Wrap inputs with styling helper
    $('input.selectbox').each(function() {
        $(this).wrap('<span id="wrapper_' + $(this).attr('id') + '" class="selectbox-input-wrapper"></span>');
          });
      }

	// Set Defaults
	jQuery.validator.setDefaults({
		errorElement : 'a',
		wrapper : 'li',
		errorLabelContainer : '#form-messages ul',
		focusInvalid: false,
		onfocusout: false,
		highlight: function(element, errorClass) {
			var errorContainer = $(element).parents('div').eq(0),
				existingIcon = $('img.icon', errorContainer);

			// Account for groups of questions
			if ($(element).parents('.group').size()) {
				errorContainer = $(element).parents('.group');
			}

			// Replace any existing icon with error icon
			if (existingIcon.size()) {
				existingIcon.replaceWith('<img src="${resource(dir: 'css/form/images', file: 'icon-error.gif')}" alt="error" class="icon"/>');
			}
			// Otherwise append to container
			else {
				errorContainer.append('<img src="${resource(dir: 'css/form/images', file: 'icon-error.gif')}" alt="error" class="icon"/>');
			}

			// Highlight field
			$(element).addClass(errorClass);

		},
		unhighlight: function(element, errorClass) {
			var errorContainer = $(element).parents('div').eq(0);

			// Account for groups of questions
			if ($(element).parents('.group').size()) {
				errorContainer = $(element).parents('.group');
			}

			// Replace icon with that of success
			if ($(':input.error', errorContainer).size() <= 1) {
				$('img.icon', errorContainer).replaceWith('<img src="${resource(dir: 'css/form/images', file: 'icon-valid.gif')}" alt="Valid" class="icon"/>');
			}

			// Unhighlight field
			$(element).removeClass(errorClass);
		},
		showErrors: function(errorMap, errorList) {
			var numErrors = this.numberOfInvalids();

			this.defaultShowErrors();

			// Populate/update error message
			if (!$('h2', errorContainer).size()) {
				errorContainer.prepend('<h2></h2>');
			}
			if (numErrors) {
				$('h2', errorContainer).html('<strong>Oops!</strong> Your form contains ' + numErrors + " error" + ((numErrors == 1) ? '' : 's') + ':');
				$(this.currentForm).removeClass('valid');
			}
			// Success is ours!
			else {
				$('h2', errorContainer).text('All errors have been corrected, please continue');
				$(this.currentForm).addClass('valid');
			}
			// Setup links
			$('a', errorContainer).each( function() {
				var el = $(this),
					fieldID = el.attr('htmlfor'),
					field = $('#' + fieldID);

				// Add href attribute to linsk
				el.attr('href', '#' + fieldID);

				// Focus on click
				el.bind('click', function() {
					field.trigger('focus');
					$('html,body').animate(
						{scrollTop: field.offset().top - 20}, 100
					);
					return false;
				});
			});
		}
	});

	// Add a placeholder for form messages
	var errorContainer = $('<div id="form-messages"><ul></ul></div>').hide();
	errorContainer.insertAfter('fieldset:first');

	// Bind event to invalid form submission
	$("form").bind("invalid-form.validate", function(e, validator) {
		errorContainer.show();
		$('html,body').animate(
			{scrollTop: errorContainer.offset().top - 20}, 100
		);

		errorContainer.focus();
	});

	// Override default messages
	$.extend($.validator.messages, {
		required : "This field is required",
		email : "Please enter a valid email",
		digits : "Please enter a numeric value"
	});
    </jq:jquery>
  </p:dependantJavascript>
  <g:layoutHead/>
</head>
<body>
<div id="header">
  <div id="logo"><a href="/${ApplicationHolder.application.metadata['app.name']}">Concordion Agora</a></div>
  <div id="menu">
    <shiro:user>
      <ul>
        <shiro:hasRole name="Administrator">
          <li><g:link controller="admin">Administrator</g:link></li>
        </shiro:hasRole>
        <li><g:link controller="preference">Preferences</g:link></li>
        <li><g:link controller="auth" action="signOut">Sign Out <shiro:principal/></g:link></li>
      </ul>
    </shiro:user>
  </div>
  <div id="spinner" class="spinner" style="display:none;"><img src="${resource(dir: 'images', file: 'spinner.gif')}" alt="Spinner"/></div>
  <div style="clear:both;"></div>
</div>
<div id="navigation">
  <ul id="navlist">
    <li id="dashboard"><g:link controller="dashboard">Dashboard</g:link></li>
    <li id="workspaces"><g:link controller="workspaces">Workspaces</g:link></li>
    <g:if test="${session.currentWorkspaceIdentifier}">
      <li id="currentWorkspace"><g:link controller="workspaces" action="view" id="${session.currentWorkspaceIdentifier}">Current Workspace</g:link></li>
    </g:if>
  </ul>
</div>
<div id="main">
  <g:layoutBody/>
</div>
<div id="footer">
  <div id="copyright">Copyright &copy; Smart Developer 2010 - Version ${ApplicationHolder.application.metadata['app.version']}</div>
  <div style="clear:both;"></div>
</div>
<p:renderDependantJavascript/>
</body>
</html>
