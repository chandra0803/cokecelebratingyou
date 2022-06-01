<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils" %>


<div class="page-content" id="seamlessLogonPage">
    <div class="row">
        <div class="span12">

  <form id="contentForm" action="<c:url value='/seamlessLogon.do'/>" method="POST">

    <table border="0" cellpadding="0" cellspacing="5">
      <tr align="left">
        <td align="left">
          <table border="0" cellpadding="3" cellspacing="1">
            <tr class="form-row-spacer">
	            	<td class="content-field-label-error" colspan="3" align="left">
  						<cms:errors/>
              		</td>
            </tr>

            <tr class="form-row-spacer">
            	<td align="left" class="content-field">
              	<cms:contentText code="login.seamlesslogon" key="LOGIN_ID"/>
            	</td>
              <td align="left">
                  <input type="text" name="loginId" size="40" class="content-field" tabindex="1">
              </td>
            </tr>

            <tr class="form-row-spacer">
            	<td align="left" class="content-field">
              	<cms:contentText code="login.seamlesslogon" key="ENCRYPTED_LOGIN_ID"/>
            	</td>
            	<td align="left" class="content-field">
              	<c:out value="${LoginId_encrypted}"/>
            	</td>
            </tr>

            <tr class="form-blank-row">
  		  	<td align="left" colspan="2"></td>
  	  	  </tr>

            <tr class="form-row-spacer">
            	<td align="left" class="content-field">
              	<cms:contentText code="login.seamlesslogon" key="SSO_ID"/>
            	</td>
              <td align="left">
                  <input type="text" name="ssoId" size="40" class="content-field" tabindex="2">
              </td>
            </tr>

            <tr class="form-row-spacer">
            	<td align="left" class="content-field">
              	<cms:contentText code="login.seamlesslogon" key="ENCRYPTED_SSO_ID"/>
            	</td>
            	<td align="left" class="content-field">
              	<c:out value="${SSOId_encrypted}"/>
            	</td>
            </tr>

            <tr class="form-blank-row">
  		  	<td align="left" colspan="2"></td>
  	  	  </tr>

            <tr class="form-row-spacer">
            	<td align="left" class="content-field">
              	<cms:contentText code="login.seamlesslogon" key="TIME_STAMP"/>
            	</td>
              <td align="left">
                  <input id="timeStampInput" type="text" name="timeStamp" size="40" class="content-field" tabindex="3">
              </td>
              <td class="content-field">
                <button type="button" onclick="generateTimestamp();">
                <cms:contentText code="login.seamlesslogon" key="GENERATE_TIME_STAMP"/>
                </button>
              </td>
            </tr>

            <tr class="form-row-spacer">
            	<td align="left" class="content-field">
              	<cms:contentText code="login.seamlesslogon" key="ENCRYPTED_TIME_STAMP"/>
            	</td>
            	<td align="left" class="content-field">
              	<c:out value="${TimeStamp_encrypted}"/>
            	</td>
            </tr>

            <tr class="form-blank-row">
  		  	<td align="left" colspan="2"></td>
  	  	  </tr>

            <tr class="form-row-spacer">
            	<td align="left" class="content-field">
              	<cms:contentText code="login.seamlesslogon" key="HASH_STRING"/> =
            	</td>
            	<td align="left" class="content-field">
              	<c:out value="${HashString}"/>
            	</td>
            </tr>

            <tr class="form-blank-row">
  		  	<td align="left" colspan="2"></td>
  	  	  </tr>

            <tr class="form-row-spacer">
              <td align="center" colspan="2">
  		        <html:submit styleClass="login-buttonstyle" tabindex="4" onclick="setActionAndDispatch('seamlessLogon.do?method=encryptParmsTest', 'encryptParmsTest')">
                      <cms:contentText code="login.seamlesslogon" key="ENCRYPT_PARAMS"/>
                  </html:submit>
              </td>
            </tr>

            <tr class="form-blank-row">
  		  	<td align="left" colspan="2"></td>
  	  	  </tr>

  	  	  <tr class="form-blank-row">
  		  	<td align="left" colspan="2"></td>
  	  	  </tr>

  	  	  <tr class="form-row-spacer">
            	<td align="left" class="content-field">
              	<cms:contentText code="login.seamlesslogon" key="UNIQUE_ID"/>
            	</td>
              <td align="left">
                  <input type="text" name="UniqueID" size="40" class="content-field" tabindex="5" value="${LoginId_encrypted}">
              </td>
            </tr>

            <tr class="form-row-spacer">
            	<td align="left" class="content-field">
              	<cms:contentText code="login.seamlesslogon" key="TIME_STAMP"/>
            	</td>
              <td align="left">
                  <input type="text" name="TimeStamp" size="40" class="content-field" tabindex="6" value="${TimeStamp_encrypted}">
              </td>
            </tr>

            <tr class="form-row-spacer">
            	<td align="left" class="content-field">
              	<cms:contentText code="login.seamlesslogon" key="HASH_STRING"/>
            	</td>
              <td align="left">
                  <input type="text" name="HashString" size="40" class="content-field" tabindex="7" value="${HashString}">
              </td>
            </tr>

            <tr class="form-blank-row">
  		  	<td align="left" colspan="2"></td>
  	  	  </tr>

            <tr class="form-row-spacer">
  			<td align="center" colspan="2">
                	<html:submit styleClass="login-buttonstyle" tabindex="8">
                		<cms:contentText code="system.button" key="LOGIN"/>
                  </html:submit>
             	</td>
  	      </tr>
          </table>
        </td>
      </tr>
    </table>
  </form>

        </div><!-- /.span12 -->
    </div><!-- /.row -->
</div><!-- /#seamlessLogonPage.page-content -->

<script>
    $(document).ready(function() {
       slp = new PageView({
            el : $('#seamlessLogonPage'),
            mode : 'help',
            pageNav : {
                back : {}
            },
            pageTitle : '',
            loggedIn : false
        });
    });
    
    function generateTimestamp(event) {
    	var dateString = new Date(Date.now()).toISOString().replace("T", " ").replace(/\.\d{3}Z/, "");
    	$('#timeStampInput').val(dateString);
    }
</script>
<%request.getSession().removeAttribute("isSSO"); %>