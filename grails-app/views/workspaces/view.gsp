<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>View Workspace - ${workspace.name}</title>
  <meta name="layout" content="main"/>
  <g:javascript>
  function cleanUp (data){
    $('#readingPane style').remove();
    $('#readingPane .footer a img').attr('src', '${resource(dir: 'images', file: 'concordion-logo.png')}');

    $("#readingPane a").each( function() {
      if($(this).attr('concordion:run') == 'concordion') {
         this.href = '<g:createLink controller="workspaces" action="viewFileContent" id="${workspace.identifier}"/>?path=target/concordion/portal/' + $(this).attr('href').split('editor/')[1];
         $(this).click(function(e) {
            e.preventDefault();
            $('#readingPane').load(this.href, function(data2) {
                cleanUp(data2);
            });
         });
      }
    });
  }
  </g:javascript>
  <jq:jquery>
    $('#currentWorkspace').addClass('current');

    $('#tabs').tabs();
    $('#specTabs').tabs();

    $('textarea.tinymce').tinymce({
      language : "en",
      theme : "advanced",
      plugins : "pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,advlist,concordion,example",

      // Theme options
      theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,styleselect,formatselect,fontselect,fontsizeselect,concordion,|,code",
      theme_advanced_buttons2 : "cut,copy,paste,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,|,print,preview,|,forecolor,backcolor,|,visualchars,template",
      theme_advanced_buttons3 : "tablecontrols,|,hr,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,removeformat,visualaid,|,sub,sup,|,charmap,|,fullscreen",
      theme_advanced_buttons4 : "",
      theme_advanced_toolbar_location : "top",
      theme_advanced_toolbar_align : "left",
      theme_advanced_statusbar_location : "bottom",
      theme_advanced_resizing : true,

      // Example content CSS (should be your site CSS)
      content_css : "${resource(dir: 'css', file: 'concordion.css')}",

      // Drop lists for link/image/media/template dialogs
      template_external_list_url : "${resource(dir: 'tiny_mce/lists', file: 'template_list.js')}",
      external_link_list_url : "${resource(dir: 'tiny_mce/lists', file: 'link_list.js')}",
      external_image_list_url : "${resource(dir: 'tiny_mce/lists', file: 'image_list.js')}",
      media_external_list_url : "${resource(dir: 'tiny_mce/lists', file: 'media_list.js')}",

      valid_elements : "@[concordion::set|concordion::assertEquals|concordion::execute|concordion::run|id|class|style|title|dir|lang|xml::lang|onclick|ondblclick|"
      + "onmousedown|onmouseup|onmouseover|onmousemove|onmouseout|onkeypress|"
      + "onkeydown|onkeyup],a[rel|rev|charset|hreflang|tabindex|accesskey|type|"
      + "name|href|target|title|class|onfocus|onblur],strong/b,em/i,strike,u,"
      + "#p,-ol[type|compact],-ul[type|compact],-li,br,img[longdesc|usemap|"
      + "src|border|alt=|title|hspace|vspace|width|height|align],-sub,-sup,"
      + "-blockquote,-table[border=0|cellspacing|cellpadding|width|frame|rules|"
      + "height|align|summary|bgcolor|background|bordercolor],-tr[rowspan|width|"
      + "height|align|valign|bgcolor|background|bordercolor],tbody,thead,tfoot,"
      + "#td[colspan|rowspan|width|height|align|valign|bgcolor|background|bordercolor"
      + "|scope],#th[colspan|rowspan|width|height|align|valign|scope],caption,-div,"
      + "-span,-code,-pre,address,-h1,-h2,-h3,-h4,-h5,-h6,hr[size|noshade],-font[face"
      + "|size|color],dd,dl,dt,cite,abbr,acronym,del[datetime|cite],ins[datetime|cite],"
      + "object[classid|width|height|codebase|*],param[name|value|_value],embed[type|width"
      + "|height|src|*],script[src|type],map[name],area[shape|coords|href|alt|target],bdo,"
      + "button,col[align|char|charoff|span|valign|width],colgroup[align|char|charoff|span|"
      + "valign|width],dfn,fieldset,form[action|accept|accept-charset|enctype|method],"
      + "input[accept|alt|checked|disabled|maxlength|name|readonly|size|src|type|value],"
      + "kbd,label[for],legend,noscript,optgroup[label|disabled],option[disabled|label|selected|value],"
      + "q[cite],samp,select[disabled|multiple|name|size],small,"
      + "textarea[cols|rows|disabled|name|readonly],tt,var,big",

      // Replace values for the template plugin
      template_replace_values : {
          username : "TEST",
          staffid : "TEST"
      }
    });

    $("#contents").treeview({
        url: "<g:createLink controller="workspaces" action="viewTreeContents" id="${workspace.identifier}"/>"
	});

    $('#contents ul li span.folder').livequery(function() {
      var menuChoice = 'folderMenu';
      $(this).contextMenu(
        menuChoice, {
          bindings: {
            'open': function(t) {
              alert(t.id);
            }
          }
         });
    });

    $('#contents ul li span.file').livequery(function() {
      var menuChoice = 'normalMenu';

      if($(this).parent().hasClass('modified')) {
        menuChoice = 'modifiedMenu';
      }
      if($(this).parent().hasClass('unversioned')) {
        menuChoice = 'unversionedMenu';
      }

      $(this).contextMenu(
        menuChoice, {
          bindings: {
            'open': function(t) {
              alert(t.id);
            }
          }
        }
      );
    });

    $('#contents a.tree').live('click', function(e) {
      e.preventDefault();

      $('#resultPane').empty();
      $('#resultPane').hide();
      $('#message').hide();
      $('#message').empty();

      $('#readingPane').html('<div><img src="${resource(dir: 'images', file: 'busy.gif')}"></div>');
      $('#readingPane').show();

      $.getJSON(this.href, function(data) {
        $('#path').val(data.path);
        $('#editor').html(data.value);

        $('#editorForm').show();

        var url = '<g:createLink controller="workspaces" action="viewFileContent" id="${workspace.identifier}"/>?path=${(workspace.specificationResultFolder + File.separator).encodeAsJavaScript()}' + data.path.split('${(workspace.specificationRootFolder + File.separator).encodeAsJavaScript()}')[1];
        $('#readingPane').load(url, function(data) {
          $('#readingPane').show();
          cleanUp(data);
        });
      });
    })

    $('#editorForm').submit(function() {
        $('#message').css('display', 'none');
        $('#message').empty();
        $(this).ajaxSubmit({
              success: function(data) {
                $('#message').html(data.message);
                $('#message').css('display', 'block');

                $('#path').val(data.path);
                $('#editor').html(data.value);
                $('#resultPane').empty();
              }
          });
        return false;
    });

    $('#execute').click(function(e) {
      e.preventDefault();

      $('#readingPane').html('<div><img src="${resource(dir: 'images', file: 'loading.gif')}"></div>');
      $('#resultPane').empty();
      $('#resultPane').show();

      $('#specTabs').tabs( "select", '#previewTab');

      $.getJSON('<g:createLink controller="workspaces" action="execute" id="${workspace.identifier}"/>?path=' + $('#path').val(), function(data) {
          $('#message').html(data.message);
          $('#resultPane').html('<pre>' + data.value + '</pre>');

          var path = $('#path').val();
          var url = '<g:createLink controller="workspaces" action="viewFileContent" id="${workspace.identifier}"/>?path=${(workspace.specificationResultFolder + File.separator).encodeAsJavaScript()}' + path.split('${(workspace.specificationRootFolder + File.separator).encodeAsJavaScript()}')[1];
          $('#readingPane').load(url, function(data) {
            cleanUp(data);
          });
      });
    });

    $('#showLog').click(function(e) {
      e.preventDefault();

      $('#readingPane').html('<div><img src="${resource(dir: 'images', file: 'loading.gif')}"></div>');
      $('#resultPane').empty();
      $('#resultPane').show();

      $.getJSON('<g:createLink controller="workspaces" action="showFileLog" id="${workspace.identifier}"/>?path=' + $('#path').val(), function(data) {
          $('#message').html(data.message);
          $('#readingPane').empty();
          $('#resultPane').html('<pre>' + data.value + '</pre>');
      });
    });

    $('#revert').click(function(e) {
      e.preventDefault();

      $('#readingPane').html('<div><img src="${resource(dir: 'images', file: 'loading.gif')}"></div>');
      $('#resultPane').empty();
      $('#resultPane').show();

      $.getJSON('<g:createLink controller="workspaces" action="revertFile" id="${workspace.identifier}"/>?path=' + $('#path').val(), function(data) {
          $('#message').html(data.message);
          $('#readingPane').empty();
          $('#editor').html(data.specification);
          $('#resultPane').html('<pre>' + data.value + '</pre>');
      });
    });

    $('#commit').click(function(e) {
      e.preventDefault();

      $('#readingPane').html('<div><img src="${resource(dir: 'images', file: 'loading.gif')}"></div>');
      $('#resultPane').empty();
      $('#resultPane').show();

      $.getJSON('<g:createLink controller="workspaces" action="commitFile" id="${workspace.identifier}"/>?comment=Test&path=' + $('#path').val(), function(data) {
          $('#message').html(data.message);
          $('#readingPane').empty();
          $('#resultPane').html('<pre>' + data.value + '</pre>');
      });
    });

    $('#addTest').click(function(e) {
      e.preventDefault();
      window.location = '<g:createLink controller="workspaces" action="createSpecification"/>';
    });

    var s1 = [
    <g:each var="data" in="${graphResults}">
      ['${data.date}',${data.value}],
    </g:each>
    ];
    var yticks = [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110];
    var plot1 = $.jqplot('chart',[s1],{
       title: 'Success',
       series:[ {
            showMarker:true,
            lineWidth:3,
            shadowAngle:1,
            shadowOffset:1.5,
            shadowAlpha:.09,
            shadowDepth:5
      } ],
      axes: {
           xaxis: {
               renderer: $.jqplot.DateAxisRenderer,
               rendererOptions:{tickRenderer:$.jqplot.CanvasAxisTickRenderer},
               tickOptions:{
                  formatString: '%Y-%m-%d',
                  fontSize: '8pt',
                  fontFamily: 'Tahoma',
                  angle: -30
               }
           },
           yaxis: {
               ticks:yticks, 
               tickOptions: {
                   formatString: '%0.0f'
               }
           }
       },
       highlighter: {
           sizeAdjust: 10,
           showMarker: true, 
           tooltipLocation: 'n',
           tooltipAxes: 'y', 
           useAxesFormatters: true,
           formatString: '%s'
       },
       cursor: {
           show: false
       }
   });
  </jq:jquery>
</head>
<body>

<div id="pageHeader" style="width: 100%;padding-bottom: 5px;margin: 10px;">
  <div style="float:left;" class="pageTitle">${workspace.name}</div>
  <div style="float:right;" class="actions">
    <ul>
      <li><g:link controller="build" action="buildWorkspace" id="${workspace.identifier}">Run Test</g:link></li>
      <li><g:link controller="repository" action="createWorkspace" id="${workspace.identifier}">Update</g:link></li>
      <shiro:hasRole name="Editor">
        <li><g:link controller="repository" action="revertDirectory" id="${workspace.identifier}" params="${[path:'']}">Revert Changes</g:link></li>
        <li><g:link controller="repository" action="commitDirectory" id="${workspace.identifier}" params="${[path:'', comment:'TEST WORKPSACE']}">Commit Changes</g:link></li>
      </shiro:hasRole>
    </ul>
  </div>
  <div style="clear:both;"></div>
</div>

<div id="tabs" style="width: 100%;padding-bottom: 5px;margin: 10px; height: 550px;" class="ui-tabs">
  <ul>
    <li><a href="#summary"><span>Summary</span></a></li>
    <li><a href="#specifications"><span>Specifications</span></a></li>
    <li><a href="#configuration"><span>Configuration</span></a></li>
  </ul>

  <div id="summary" class="summarySection">
    <div style="float:left;" id="chart" style="width:500px; height:300px;"></div>
    <div style="float:left;" class="summary ${workspace?.lastResult?.status?.toLowerCase()}">${workspace?.lastResult?.percentSuccess}%</div>
    <div style="float:left;" class="detail">
      Successes: ${workspace?.lastResult?.successes}<br/>
      Failures: ${workspace?.lastResult?.failures}<br/>
      Exception: ${workspace?.lastResult?.exceptions}
    </div>
    <div style="clear:both;"></div>
  </div>

  <div id="configuration" class="ui-tabs-hide">
    <div class="info">Configuration</div>
  </div>

  <div id="specifications" class="ui-tabs-hide">

    <div id="editorPane" style="float: left;">
      <div id="message" class="message" style="display:none;"></div>

      <div id="specTabs" style="width: 90%;height: 450px;" class="ui-tabs">
        <ul>
          <li><a href="#editorTab"><span>Specification</span></a></li>
          <li><a href="#previewTab"><span>Results</span></a></li>
        </ul>

        <div id="editorTab">
          <g:form name="editorForm" controller="workspaces" action="save" style="display: none;">
            <g:hiddenField name="id" value="${workspace.identifier}"/>
            <g:hiddenField name="path"/>
            <textarea id="editor" name="editor" style="width: 90%; height: 90%; padding: 5px; margin: 5px;" class="tinymce">
            </textarea>

            <div class="actions" style="margin-top: 10px;">
              <shiro:hasRole name="Editor">
                <g:submitButton name="Save"/>
                <input id="execute" type="button" value="Execute"/>
                <input id="showLog" type="button" value="Show Log"/>
                <input id="revert" type="button" value="Revert"/>
                <input id="commit" type="button" value="Commit"/>
              </shiro:hasRole>
            </div>
          </g:form>
        </div>

        <div id="previewTab" style="overflow: auto;">
          <div id="readingPane" style="display:none; margin: 10px; width: 100%;"></div>
        </div>
      </div>

      <div style="clear: both;"></div>

      <shiro:hasRole name="Editor">
        <input id="addTest" type="button" style="margin-top: 20px;" value="Add Specification"/>
      </shiro:hasRole>
      <div id="trees" class="dialog" style="float:left; width: 400px; height: 300px; margin-top: 20px; overflow-y:auto; overflow-x:hidden; background-color: white; border: 1px solid gray;">
        <ul id="contents" class="filetree treeview-famfamfam"></ul>
      </div>

      <div id="resultPane" style="float: left; width: 750px; height: 300px;  margin-left: 20px; margin-top: 20px;overflow-y:auto; overflow-x:hidden; background-color: white; border: 1px solid gray;"></div>
    </div>

    <div style="clear: both;"></div>
  </div>
</div>

<shiro:hasRole name="Editor">
  <div style="display:none">
    <div id="normalMenu" class="contextMenu">
      <ul>
        <li id="open" class="execute">TBD</li>
      </ul>
    </div>

    <div id="folderMenu" class="contextMenu">
      <ul>
        <li class="add"><a href="#add">TBD</a></li>
      </ul>
    </div>

    <div id="modifiedMenu" class="contextMenu">
      <ul>
        <li class="commit"><a href="#commit">TBD</a></li>
        <li class="revert separator"><a href="#revert">TBD</a></li>
        <li class="execute"><a href="#execute">TBD</a></li>
      </ul>
    </div>

    <div id="unversionedMenu" class="contextMenu">
      <ul>
        <li class="commit"><a href="#commit">TBD</a></li>
        <li class="execute"><a href="#execute">TBD</a></li>
      </ul>
    </div>
  </div>
</shiro:hasRole>

</body>
</html>
