<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="listBuilder">
<html:hidden property="method" value="returnAudienceExclusion" styleId="method"/>
<html:hidden property="pageType" value="addAudience"/>
<html:hidden property="admin" value="true"/>
<html:hidden property="searchType" value="criteria"/>
<table width="100%" style="margin-bottom:0"> 
<tbody>
   <tr>
     <td>
         <span class="headline"><cms:contentText key="AUDIENCE_EXCLUSION" code="participant.list.builder.details"/></span>
     </td>
   </tr>
   <tr class="form-blank-row"><td colspan="3"></td></tr>
   <tr class="other">
        <td colspan="2"><div align="left"><b><cms:contentText key="BASIC_PARAMETERS" code="participant.list.builder.details"/></b></div></td>
    </tr>   
   <tr class="form-blank-row"><td colspan="3"></td></tr>
   
   <tr>
      <!-- left -->
      <td width="50%" style="vertical-align:top">

         <table width="25%" border="0" cellpadding="5" cellspacing="0" style="margin-bottom:0">
           <tbody>
            <tr>
              <td width="4%"><html:checkbox property="excludeCountry" styleClass="content-field"/></td>
              <td width="96%">
                <beacon:label property="excludeCountry" required="false">
			     <cms:contentText key="COUNTRY" code="participant.participant"/>
			  </beacon:label>
              </td>
            </tr>
            <tr>
              <td width="4%"><html:checkbox property="excludeNodeName" styleClass="content-field"/></td>
              <td width="96%">
               <beacon:label property="excludeNodeName">
            	<cms:contentText key="NODE_NAME" code="participant.list.builder.details"/>
          		</beacon:label>
              </td>
            </tr>
            <tr>
              <td width="4%"><html:checkbox property="excludeNodeRole" styleClass="content-field"/></td>
              <td width="96%">
                <beacon:label property="excludeNodeRole">
            		<cms:contentText key="NODE_ROLE" code="participant.list.builder.details"/>
          		</beacon:label>
              </td>
            </tr>
            <tr>
              <td width="4%"><html:checkbox property="excludeNodeCharacteristic" styleClass="content-field"/></td>
              <td width="96%">
               <beacon:label property="excludeNodeCharacteristic">
            		<cms:contentText key="NODE_CHARACTERISTICS" code="participant.list.builder.details"/>
          		</beacon:label>
              </td>
            </tr>
          </tbody>
        </table>
      </td>

      <!-- right -->
      <td width="50%" style="vertical-align:top">
        <table width="25%" border="0" cellpadding="5" cellspacing="0" style="margin-bottom:0">
            <tbody>
            <tr class="form-blank-row"><td colspan="3"></td></tr>
            
          	 <tr>
              <td width="4%"><html:checkbox property="excludeJobPosition" styleClass="content-field"/></td>
              <td width="96%">
               <beacon:label property="excludeJobPosition">
            	<cms:contentText key="JOB_POSITION" code="participant.list.builder.details"/>
          		</beacon:label>
              </td>
            </tr>
            <tr>
              <td width="4%"><html:checkbox property="excludeDepartment" styleClass="content-field"/></td>
              <td width="96%">
                <beacon:label property="excludeDepartment">
            		<cms:contentText key="DEPARTMENT" code="participant.list.builder.details"/>
          		</beacon:label>
              </td>
            </tr>
            <tr>
              <td width="4%"><html:checkbox property="excludePaxCharacteristic" styleClass="content-field"/></td>
              <td width="96%">
                <beacon:label property="excludePaxCharacteristic">
            		<cms:contentText key="PAX_CHARACTERISTICS" code="participant.list.builder.details"/>
          		</beacon:label>
              </td>
            </tr>              
          </tbody>
        </table>
      </td>

    </tr> </tbody> </table>
    
    <table width="100%">
		<tr class="form-buttonrow">
			<td class="headline" align="center">
			<html:submit styleClass="content-buttonstyle">
				<cms:contentText code="system.button" key="SELECT" />
			</html:submit>
			 
			  <html:cancel styleClass="content-buttonstyle">
				<cms:contentText code="system.button" key="CANCEL" />
			  </html:cancel>
			</td>
		</tr>
	</table>
</html:form>