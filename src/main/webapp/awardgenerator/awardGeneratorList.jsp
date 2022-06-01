<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.awardgenerator.AwardGenerator"%> 
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="awardGeneratorListDelete">
  <html:hidden property="method" />
	<beacon:client-state>
		<beacon:client-state-entry name="awardGeneratorId" value="${awardGeneratorListForm.awardGeneratorId}"/>
	</beacon:client-state>

   <table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
		<td>
        <span class="headline"><cms:contentText key="TITLE" code="awardgenerator.list"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="awardgenerator.list"/>'); </script>	  
    
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="awardgenerator.list"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>

		<table>
			<tr class="form-buttonrow">
            	<td align="left">
             		 <html:button property="Add" styleClass="content-buttonstyle" onclick="window.location='awardGeneratorMaintain.do?method=display'">
				 		 <cms:contentText key="CREATE_AWARD_GEN_BTN" code="awardgenerator.list"/>
			 		 </html:button>
            	</td>
          	</tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>

          <tr class="form-row-spacer">				  
			<td>
				<table width="100%">
      				<tr>
            			<td align="right">
              			<c:set var="rowNum" value="0"/>
              			<% 	Map parameterMap = new HashMap();
							AwardGenerator temp;
						%>
              			<display:table defaultsort="1" defaultorder="ascending" name="awardGeneratorList" id="awardGenerator" style="width: 100%" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
				        	<display:setProperty name="basic.msg.empty_list">
			        			<tr class="crud-content" align="left">
								    <td colspan="{0}">
			                          <cms:contentText key="NO_ELEMENTS" code="awardgenerator.list"/>
			                        </td>
			                    </tr>
		        			</display:setProperty>
		        			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
		        			<display:column titleKey="awardgenerator.list.SETUP_NAME_HEADER" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="name">
                				<%	temp = (AwardGenerator)pageContext.getAttribute( "awardGenerator" );
									parameterMap.put( "awardGeneratorId", temp.getId() );
									pageContext.setAttribute("maintainUrl", ClientStateUtils.generateEncodedLink( "", "awardGeneratorMaintain.do?method=display", parameterMap ) );
								%>
                				   <a href="<c:out value="${maintainUrl}"/>" class="crud-content-link">
                     				     <c:out value="${awardGenerator.name}"/>
                     			   </a>
                			</display:column>	
							<display:column titleKey="awardgenerator.list.REMOVE_HEADER" style="width: 75px" class="crud-content center-align" headerClass="crud-table-header-row">
        		                     		<html:checkbox property="deleteAwardGeneratorIds" value="${awardGenerator.id}" />
         		            </display:column>
        		    		</display:table>
            			</td>
					  </tr>
			          <tr class="form-buttonrow">
    			        <td>
        			      <table width="100%">
	            			 <tr>
	              			  	<td align="right">
									<html:submit styleClass="content-buttonstyle" onclick="setDispatch('deleteAwardGenerators')">
	            		 				<cms:contentText code="system.button" key="REMOVE_SELECTED" />
	          		   				</html:submit>

					              	<html:button property="homePageButton" styleClass="content-buttonstyle" onclick="callUrl('../homePage.do')">
					                	<cms:contentText key="BACK_TO_HOME" code="system.button"/>
					              	</html:button> 
	                			</td>
	              			 </tr>
              			  </table>
          				</td>
          			  </tr>
            	 </table>
			   </td>
		     </tr>
	       </table>
		 </td>
		</tr>
	 </table>
</html:form>