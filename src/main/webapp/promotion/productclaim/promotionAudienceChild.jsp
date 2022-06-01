<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
If you add new types other than recognition and claim, you might want to refactor as per requirements.
As this doen't comes under any of our standard layouts, most of the layout is specific to this page (whichever looks good) and changed the content wherever necessary as
per refactoring requirements.
--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
  <% String disableSelectSubAud = "false"; 
     String disableSelectTeamAud = "false";
     String disableSelectTeamPos = "false";
     String disabledFlag = "false";
     String teamUsedDisabledFlag = "false";
     String disabledSameAsSubmitterFlag = "true";
     String disabledPaxFromNodeFlag = "true";
     String disabledGroupFlag = "true";
     String disabledNoMaxFlag = "true";
     String disabledHasMaxFlag = "true";
  %>
  <c:if test="${ empty promotionAudienceForm.primaryAudienceAsList }">
    <% disableSelectSubAud = "true"; %>
  </c:if>
  <c:if test="${ empty promotionAudienceForm.secondaryAudienceAsList }">
    <% disableSelectTeamAud = "true"; %>
  </c:if>
  <c:if test="${ empty promotionAudienceForm.promotionTeamPositionAsList }">
    <% disableSelectTeamPos = "true"; %>
  </c:if>
  <c:if test="${ ! promotionAudienceForm.parentTeamUsed}">
    <% teamUsedDisabledFlag = "true"; %>
  </c:if>
  <c:choose>
    <c:when test="${ promotionAudienceForm.secondaryAudienceType == 'sameasprimaryaudience'}">
      <% disabledSameAsSubmitterFlag = "false"; %>
    </c:when>
    <c:when test="${ promotionAudienceForm.secondaryAudienceType == 'activepaxfromprimarynodeaudience'}">
      <% disabledPaxFromNodeFlag = "false"; %>
    </c:when>
  </c:choose>
  <c:if test="${promotionAudienceForm.teamCollectedAsGroup}">
    <% disabledGroupFlag = "false"; %>
  </c:if>
  <c:choose>
    <c:when test="${promotionAudienceForm.teamHasMax}">
      <% disabledHasMaxFlag = "false"; %>
    </c:when>
    <c:otherwise>
      <% disabledNoMaxFlag = "false"; %>
    </c:otherwise>
  </c:choose>
  <c:if test="${promotionStatus=='expired'}">
    <% 
    disabledFlag="true";
    disableSelectTeamPos = "true"; 
    disableSelectTeamAud = "true";
    disableSelectSubAud = "true";  
    teamUsedDisabledFlag = "true";
    %>
  </c:if>
      <table>
        <tr class="form-row-spacer">
          <beacon:label property="primaryAudienceType" required="true" styleClass="content-field-label-top">
	              <cms:contentText key="SUBMITTERS" code="promotion.audience"/>
	      </beacon:label>	
          <td class="content-field"><html:radio property="primaryAudienceType" styleId="primaryAudienceType[1]" value="entireparentaudience" onclick="hideLayer('submittersaudience');" disabled="<%=disabledFlag%>"/></td>      
          <td align="left" class="content-field"><cms:contentText key="ENTIRE_PARENT" code="promotion.audience"/></td>
        </tr>
        <tr class="form-row-spacer">
          <td  colspan="2"></td>
          <td class="content-field"><html:radio property="primaryAudienceType" styleId="primaryAudienceType[2]" value="specificparentaudiences" onclick="showLayer('submittersaudience');" disabled="<%=disableSelectSubAud%>"/></td>      
          <td align="left" class="content-field"><cms:contentText key="SELECT_AUDIENCE_FROM_PARENT" code="promotion.audience"/></td>
        </tr>
        <tr>
          <td colspan="3"></td>
          <td>
            <DIV id="submittersaudience">
              <table>
                <tr class="form-row-spacer">
                  <td>
                    <table class="crud-table" width="100%">
                      <tr>
      	                <th valign="top" colspan="3" class="crud-table-header-row"><cms:contentText code="promotion.audience" key="PARENT_AUDIENCE"/></th>
 		                <th class="crud-table-header-row"><cms:contentText code="system.general" key="SELECT"/>
 		                <br><a href="javascript:selectAllCheckBox('submitter');" class="crud-content-link"><cms:contentText code="system.general" key="SELECT_ALL"/></a></th>
 		              </tr>
 		              <c:set var="switchColor" value="false"/>  
                    <nested:iterate id="promoSubmitterAudience" name="promotionAudienceForm" property="primaryAudienceAsList">   
                      <nested:hidden property="id"/>
                      <nested:hidden property="audienceId"/>
                      <nested:hidden property="audienceType"/>
                      <nested:hidden property="name"/>
                      <nested:hidden property="size"/>
                  <c:choose>
	                <c:when test="${switchColor == 'false'}">
		              <tr class="crud-table-row1">
		  			  <c:set var="switchColor" scope="page" value="true"/>
	     			</c:when>
	     			<c:otherwise>
		  		      <tr class="crud-table-row2">
		  			  <c:set var="switchColor" scope="page" value="false"/>
	     			</c:otherwise>
	    		  </c:choose>
	    		  <%-- in general we use content-field-review for no editables, but for this screen class is content-field--%>
                        <td class="content-field">
                          <c:out value="${promoSubmitterAudience.name}"/>
                        </td>
                        <td class="content-field">
                          <c:if test="${ promoSubmitterAudience.audienceType == 'pax'}">
                            <&nbsp;
                            <c:out value="${promoSubmitterAudience.size}"/>
                            &nbsp;>
                          </c:if>
                        </td>
                        <td class="content-field">
													<%	Map paramMap = new HashMap();
															PromotionAudienceFormBean temp = (PromotionAudienceFormBean)pageContext.getAttribute( "promoSubmitterAudience" );
															paramMap.put( "audienceId", temp.getAudienceId() );
															pageContext.setAttribute("popupUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotion/promotionAudience.do?method=displayPaxListPopup", paramMap, true ) );
													%>
                          <a href="javascript:popUpWin('<c:out value="${popupUrl}"/>', 'console', 750, 500, false, true);"><cms:contentText key="VIEW_LIST" code="promotion.audience"/></a>
                        </td>
                        <td align="center" class="content-field">
                          <nested:checkbox property="selected"/>
                        </td>
                      </tr>
                    </nested:iterate>
                    </table>
                  </td>
                </tr>
              </table>
            </DIV> <%-- end of submittersaudience DIV --%>
          </td>
        </tr>
        
        <tr class="form-blank-row">
            <td></td>
       </tr>
        
        <tr class="form-row-spacer">
          <beacon:label property="teamUsed" required="true" styleClass="content-field-label-top">
	              <cms:contentText key="TEAM_MEMBERS" code="promotion.audience"/>
	      </beacon:label>	
          <td class="content-field"><html:radio property="teamUsed" value="false" onclick="hideLayer('teamaudience');" disabled="<%=disabledFlag%>"/></td>     
          <td class="content-field"><cms:contentText key="TEAM_NOT_COLLECTED" code="promotion.audience"/></td>
        </tr>
        <tr class="form-row-spacer">
          <td colspan="2"></td>
          <td class="content-field"><html:radio property="teamUsed" value="true" onclick="showLayer('teamaudience');" disabled="<%=teamUsedDisabledFlag%>"/></td>     
          <td class="content-field"><cms:contentText key="TEAM_IS_COLLECTED" code="promotion.audience"/></td>
        </tr>
        <tr>
          <td colspan="3"></td>
          <td>
            <DIV id="teamaudience">
              <table>
                <tr class="form-row-spacer">                 
                    <beacon:label property="secondaryAudienceType" required="false" styleClass="content-field-label-top">
	              		<cms:contentText key="TEAM_MBR_AUDIENCE" code="promotion.audience"/>
	       			</beacon:label>	
                  <td class="content-field"><html:radio property="secondaryAudienceType" styleId="secondaryAudienceType[0]" value="sameasprimaryaudience" onclick="hideLayer('teamaudiencelist');" disabled="<%=disabledSameAsSubmitterFlag%>"/></td>     
                  <td class="content-field"><cms:contentText code="promotion.audience" key="SAME_AS_SUBMITTER"/></td>
                </tr>
                <tr class="form-row-spacer">
                  <td colspan="2"></td>
                  <td class="content-field"><html:radio property="secondaryAudienceType" styleId="secondaryAudienceType[1]" value="activepaxfromprimarynodeaudience" onclick="hideLayer('teamaudiencelist');" disabled="<%=disabledPaxFromNodeFlag%>"/></td>     
                  <td class="content-field"><cms:contentText code="promotion.audience" key="ALL_PAX_FROM_NODE"/></td>
                </tr>
                <tr class="form-row-spacer">
                  <td colspan="2"></td>
                  <td class="content-field"><html:radio property="secondaryAudienceType" styleId="secondaryAudienceType[2]" value="specifyaudience" onclick="showLayer('teamaudiencelist');" disabled="<%=disableSelectTeamAud%>"/></td>
                  <td class="content-field"><cms:contentText code="promotion.audience" key="SPECIFY_AUDIENCE"/></td>
                </tr>
                <tr>
                  <td colspan="3"></td>
                  <td>
                    <DIV id="teamaudiencelist">
                      <table>
                        <tr class="form-row-spacer">
                          <td>
                            <table class="crud-table" width="100%">
                      		  <tr>
      	                		<th valign="top" align="left" colspan="3" class="crud-table-header-row"><cms:contentText code="promotion.audience" key="PARENT_AUDIENCE"/></th>
 		                		<th class="crud-table-header-row"><cms:contentText code="system.general" key="SELECT"/>
 		                		<br><a href="javascript:selectAllCheckBox('team');"><cms:contentText code="system.general" key="SELECT_ALL"/></a>
 		                		</th>
 		              		  </tr>
 		          			  <c:set var="switchColor" value="false"/>  
                	        <nested:iterate id="promoTeamAudience" name="promotionAudienceForm" property="secondaryAudienceAsList">   
                  		      <nested:hidden property="id"/>
                              <nested:hidden property="audienceId"/>
                              <nested:hidden property="audienceType"/>                  		      
                              <nested:hidden property="name"/>
                              <nested:hidden property="size"/>
                  		  <c:choose>
	                        <c:when test="${switchColor == 'false'}">
		                      <tr class="crud-table-row1">
		  			          <c:set var="switchColor" scope="page" value="true"/>
	     			        </c:when>
	     			        <c:otherwise>
		  		              <tr class="crud-table-row2">
		  			          <c:set var="switchColor" scope="page" value="false"/>
	     			        </c:otherwise>
	    		          </c:choose>
	    		          <%-- in general we use content-field-review for no editables, but for this screen class is content-field--%>
                                <td class="content-field">
                                  <c:out value="${promoTeamAudience.name}"/>
                                </td>
                                <td class="content-field">
                                  < <c:out value="${promoTeamAudience.size}"/> >
                                </td>
                                <td class="content-field">
																	<%	Map parameterMap = new HashMap();
																			PromotionAudienceFormBean temp2 = (PromotionAudienceFormBean)pageContext.getAttribute( "promoTeamAudience" );
																			parameterMap.put( "audienceId", temp2.getAudienceId() );
																			pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotion/promotionAudience.do?method=displayPaxListPopup", parameterMap, true ) );
																	%>
                                  <a href="javascript:popUpWin('<c:out value="${linkUrl}"/>', 'console', 750, 500, false, true);"><cms:contentText key="VIEW_LIST" code="promotion.audience"/></a>
                                </td>
                                <td align="center" class="content-field">
                                  <nested:checkbox property="selected"/>
                                </td>
                              </tr>
                            </nested:iterate>
                            </table>
                          </td>
                        </tr>
                      </table>
                    </DIV> <%-- end of TeamAudienceList DIV --%>
                  </td>
                </tr>
                <tr class="form-row-spacer">
                   	<beacon:label property="teamCollectedAsGroup" required="false" styleClass="content-field-label-top">
	              		<cms:contentText key="COLLECT_TEAM" code="promotion.audience"/>
	       			</beacon:label>	
                  <td class="content-field"><html:radio property="teamCollectedAsGroup" value="true" onclick="showLayer('maxpeople');hideLayer('jobpositions');" disabled="<%=disabledGroupFlag%>"/></td>     
                  <td class="content-field"><cms:contentText code="promotion.audience" key="AS_GROUP"/></td>
                </tr>
                <tr>
                  <td colspan="3"></td>
                  <td>
                    <DIV id="maxpeople">
                      <table>
                        <tr class="form-row-spacer">
                          <td class="content-field"><html:radio property="teamHasMax" value="false" disabled="<%=disabledNoMaxFlag%>"/></td>     
                          <td colspan="2" class="content"><cms:contentText code="promotion.audience" key="NO_MAX"/></td>
                        </tr>
                        <tr class="form-row-spacer">
                          <td class="content-field"><html:radio property="teamHasMax" value="true" disabled="<%=disabledHasMaxFlag%>"/></td>     
                          <td class="content-field"><cms:contentText code="promotion.audience" key="MAX_VALUE"/></td>
                          <td class="content-field"><html:text property="teamMaxCount" size="4" maxlength="3" styleClass="content-field" disabled="true"/></td>     
                          <html:hidden property="teamMaxCount"/>
                        </tr>
                      </table>
                    </DIV> <%-- end of maxpeople DIV --%>
                  </td>
                </tr>
                <tr class="form-row-spacer">
                  <td colspan="2"></td>
                  <td class="content-field"><html:radio property="teamCollectedAsGroup" value="false" onclick="hideLayer('maxpeople');showLayer('jobpositions');" disabled="<%=disableSelectTeamPos%>"/></td>
                  <td class="content-field"><cms:contentText code="promotion.audience" key="SELECT_JOB_FROM_PARENT"/></td>
                </tr>
                <tr>
                  <td colspan="3"></td>
                  <td>
                    <DIV id="jobpositions">
                      <table class="crud-table" width="100%">
                        <tr>
                          <td valign="top" class="crud-table-header-row"><cms:contentText key="POSITION_LABEL" code="promotion.audience"/></td>
                          <td align="center" class="crud-table-header-row"><cms:contentText key="SELECT" code="system.general"/>
                          <br><a href="javascript:selectAllCheckBox('position');" class="crud-content-link"><cms:contentText code="system.general" key="SELECT_ALL"/></a></td>
                          <td valign="top" align="center" class="crud-table-header-row"><cms:contentText key="CRUD_REQUIRED_LABEL" code="promotion.audience"/></td>
                        </tr>
                        <c:set var="switchColor" value="false"/>  
                	  <nested:iterate id="promoTeamPosition" name="promotionAudienceForm" property="promotionTeamPositionAsList">   
                  		<nested:hidden property="id"/>
                        <nested:hidden property="name"/>
                        <nested:hidden property="teamPositionCode"/>
                    <c:choose>
	                  <c:when test="${switchColor == 'false'}">
		                <tr class="crud-table-row1">
		  			    <c:set var="switchColor" scope="page" value="true"/>
	     			  </c:when>
	     			  <c:otherwise>
		  		        <tr class="crud-table-row2">
		  			    <c:set var="switchColor" scope="page" value="false"/>
	     			  </c:otherwise>
	    		    </c:choose>
                          <td class="content-field">
                            <c:out value="${promoTeamPosition.name}"/>
                          </td>
                          <td align="center" class="content-field">
                            <nested:checkbox property="selected"/>
                          </td>
                          <td align="center" class="content-field">
                            <nested:checkbox property="required"/>
                          </td>
                        </tr>
                      </nested:iterate>
                      </table>
                    </DIV> <%-- end of jobpositions DIV --%>
                  </td>
                </tr>
              </table>
            </DIV><%-- end of teamaudience DIV--%>
          </td>
        </tr>
        <tr>
          <td colspan="3">&nbsp;</td>
        </tr>
      </table>