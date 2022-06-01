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

<c:set var="displayFlag" value="${promotionStatus == 'expired' || promotionStatus == 'live'}" />

<c:choose>
  <c:when test="${promotionAudienceForm.fileLoadEntry}"> 
    <table>
      <tr class="form-row-spacer">
	    <beacon:label property="teamUsed" required="false" styleClass="content-field-label-top">
	      <cms:contentText key="TEAM_MEMBERS" code="promotion.audience"/>
	    </beacon:label>
        <td class="content-field"><cms:contentText key="NO_TEAM_FOR_FILE_LOAD" code="promotion.audience"/></td>
      </tr>
    </table>
  </c:when>
  <c:otherwise>
    <table>
      <tr class="form-row-spacer">
        <beacon:label property="teamUsed" required="true" styleClass="content-field-label-top">
          <cms:contentText key="TEAM_MEMBERS" code="promotion.audience"/>
        </beacon:label>  
        <td class="content-field"><html:radio disabled="${displayFlag}" property="teamUsed" styleId="teamUsed[0]" value="false" onclick="hideLayer('teamaudience');"/></td>
        <td class="content-field"><cms:contentText key="TEAM_NOT_COLLECTED" code="promotion.audience"/></td>
      </tr>
         
      <tr class="form-row-spacer">
        <td colspan="2"></td>
        <td class="content-field"><html:radio disabled="${displayFlag}" property="teamUsed" styleId="teamUsed[1]" value="true" onclick="showLayer('teamaudience');"/></td>
        <td class="content-field"><cms:contentText key="TEAM_IS_COLLECTED" code="promotion.audience"/></td>
      </tr>
        
      <tr class="form-blank-row"><td></td></tr>
        
      <tr class="form-row-spacer">
        <td colspan="3"></td>
        <td>
          <DIV id="teamaudience">
            <table>
              <tr class="form-row-spacer">
                <beacon:label property="secondaryAudienceType" required="false" styleClass="content-field-label-top">
                  <cms:contentText key="TEAM_MBR_AUDIENCE" code="promotion.audience"/>
                </beacon:label>
                <td class="content-field"><html:radio disabled="${displayFlag}" property="secondaryAudienceType" styleId="secondaryAudienceType[0]" value="sameasprimaryaudience" onclick="hideLayer('teamaudiencelist');"/></td>
                <td class="content-field"><cms:contentText code="promotion.audience" key="SAME_AS_SUBMITTER"/></td>
              </tr>
              <tr class="form-row-spacer">
                <td colspan="2"></td>
                <td class="content-field"><html:radio disabled="${displayFlag}" property="secondaryAudienceType" styleId="secondaryAudienceType[1]" value="activepaxfromprimarynodeaudience" onclick="hideLayer('teamaudiencelist');"/></td>
                <td class="content-field"><cms:contentText code="promotion.audience" key="ALL_PAX_FROM_NODE"/></td>
              </tr>
              <tr class="form-row-spacer">
                <td colspan="2"></td>
                <td class="content-field"><html:radio disabled="${displayFlag}" property="secondaryAudienceType" styleId="secondaryAudienceType[2]" value="specifyaudience" onclick="showLayer('teamaudiencelist');"/></td>
                <td class="content-field"><cms:contentText code="promotion.audience" key="SPECIFY_AUDIENCE"/></td>
              </tr>
                
              <tr class="form-row-spacer">
                <td colspan="3"></td>
                <td>
                  <DIV id="teamaudiencelist">
                    <table>
                      <tr>
                        <td>
                          <table class="crud-table" width="100%">
                            <tr>
                              <th colspan="3" class="crud-table-header-row">
                                <cms:contentText key="AUDIENCE_LIST_LABEL" code="promotion.audience"/>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <html:select property="secondaryAudienceId" styleClass="content-field" disabled="${displayFlag}">
                                  <html:options collection="availableSecondaryAudiences" property="id" labelProperty="name" />
                                </html:select>
                                <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('promotionAudience.do', 'addTeamAudience')" disabled="${displayFlag}" >
                                  <cms:contentText code="system.button" key="ADD" />
                                </html:submit>
                                <br>
                                <cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience"/>
                                <a href="javascript:setActionDispatchAndSubmit('promotionAudience.do', 'prepareTeamAudienceLookup');" class="crud-content-link">
                                  <cms:contentText key="LIST_BUILDER_LABEL" code="promotion.audience"/>
                                </a>
                              </th>
                              <c:if test="${promotionAudienceForm.canRemoveAudience}">
                                <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL"/></th>
                              </c:if>
                            </tr>
                            <c:set var="switchColor" value="false"/>
                            <nested:iterate id="promoTeamAudience" name="promotionAudienceForm" property="secondaryAudienceAsList">
                              <nested:hidden property="id"/>
                              <nested:hidden property="audienceId"/>
                              <nested:hidden property="name"/>
                              <nested:hidden property="size"/>
                              <nested:hidden property="audienceType"/>
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
                                <c:out value="${promoTeamAudience.name}"/>
                              </td>
                              <td class="content-field">                                
                                  <&nbsp;
                                  <c:out value="${promoTeamAudience.size}"/>
                                  &nbsp;>                                
                              </td>
                              <td class="content-field">
																<%	Map paramMap = new HashMap();
																		PromotionAudienceFormBean temp = (PromotionAudienceFormBean)pageContext.getAttribute( "promoTeamAudience" );
																		paramMap.put( "audienceId", temp.getAudienceId() );
																		pageContext.setAttribute("popupUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotion/promotionAudience.do?method=displayPaxListPopup", paramMap, true ) );
																%>
                                <a href="javascript:popUpWin('<c:out value="${popupUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link">view list</a>
                              </td>
                              <c:if test="${promotionAudienceForm.canRemoveAudience}">
                                <td align="center" class="content-field">
                                  <nested:checkbox property="removed"/>
                                </td>
                              </c:if>
                            </tr>
                            </nested:iterate>
                          </table>
                        </td>
                      </tr>
                      <c:if test="${promotionAudienceForm.canRemoveAudience}">
                        <tr>
                          <td align="right">
                            <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('promotionAudience.do', 'removeTeamAudience')" disabled="${displayFlag}">
                              <cms:contentText key="REMOVE" code="system.button"/>
                            </html:submit>
                          </td>
                        </tr>
                      </c:if>
                      <c:if test="${promotionAudienceForm.hasChildren && (promotionAudienceForm.secondaryAudienceCount > 0)}">
                        <tr>
                          <td>
                            <table>
                              <tr class="form-row-spacer">
                                <beacon:label property="addSecondaryAudiencesToChildPromotions" required="true" styleClass="content-field-label-top">
                                  <cms:contentText key="ADD_NEW_TEAM_MEMBERS_TO_CHILD_PROMOS" code="promotion.audience"/>
                                </beacon:label>
                                <td class="content-field"><html:radio property="addSecondaryAudiencesToChildPromotions" value="true"/></td>
                                <td class="content-field"><cms:contentText key="YES" code="system.common.labels"/></td>
                              </tr>
                              <tr class="form-row-spacer">
                                <td colspan="2"></td>
                                <td class="content-field"><html:radio property="addSecondaryAudiencesToChildPromotions" value="false"/></td>
                                <td class="content-field"><cms:contentText key="NO" code="system.common.labels"/></td>
                              </tr>
                            </table>
                          </td>
                        </tr>
                      </c:if>
                    </table>
                  </DIV> <%-- end of TeamAudienceList DIV --%>
                </td>
              </tr>

              <tr class="form-row-spacer">
                <beacon:label property="teamCollectedAsGroup" required="false" styleClass="content-field-label-top">
                  <cms:contentText key="COLLECT_TEAM" code="promotion.audience"/>
                </beacon:label>
                  
                <td class="content-field"><html:radio disabled="${displayFlag}" property="teamCollectedAsGroup" value="true" onclick="showLayer('maxpeople');hideLayer('jobpositions');"/></td>
                <td class="content-field"><cms:contentText code="promotion.audience" key="AS_GROUP"/></td>
              </tr>
                
              <tr>
                <td colspan="3"></td>
                <td>
                  <DIV id="maxpeople">
                    <table>
                      <tr class="form-row-spacer">
                        <td class="content-field"><html:radio onclick="enableTeamHasMaxFields();" disabled="${displayFlag}" property="teamHasMax" value="false" /></td>
                        <td colspan="2" class="content-field"><cms:contentText code="promotion.audience" key="NO_MAX"/></td>
                      </tr>
                      <tr class="form-row-spacer">
                        <td class="content-field"><html:radio onclick="enableTeamHasMaxFields();" disabled="${displayFlag}" property="teamHasMax" value="true" /></td>
                        <td class="content-field"><cms:contentText code="promotion.audience" key="MAX_VALUE"/></td>
                        <td class="content-field"><html:text disabled="${displayFlag}" property="teamMaxCount" size="4" maxlength="3" styleClass="content-field"/></td>
                      </tr>
                    </table>
                  </DIV> <%-- end of maxpeople DIV --%>
                </td>
              </tr>
              <tr class="form-row-spacer">
                <td colspan="2"></td>
                <td class="content-field"><html:radio disabled="${displayFlag}" property="teamCollectedAsGroup" value="false" onclick="hideLayer('maxpeople');showLayer('jobpositions');"/></td>
                <td class="content-field"><cms:contentText code="promotion.audience" key="SPECIFIC_POSITIONS"/></td>
              </tr>
              <tr class="form-row-spacer">
                <td colspan="3"></td>
                <td>
                  <DIV id="jobpositions">
                    <table>
                      <tr>
                        <td>
                          <table class="crud-table" width="100%">
                            <tr>
                              <td class="crud-table-header-row"><cms:contentText key="POSITION_LABEL" code="promotion.audience"/></td>
                              <td class="crud-table-header-row"><cms:contentText key="CRUD_REQUIRED_LABEL" code="promotion.audience"/></td>
                              <td class="crud-table-header-row"><cms:contentText key="CRUD_REMOVE_LABEL" code="promotion.audience"/></td>
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
                                <nested:checkbox property="required"/>
                              </td>
                              <td align="center" class="content-field">
                                <nested:checkbox property="removed"/>
                              </td>
                            </tr>
                            </nested:iterate>
                          </table>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <table>
                            <tr>
                            <c:if test="${not empty typeList}" >
                              <td class="content-field"><cms:contentText key="ADD" code="system.button"/></td>
                              <td>
                                <html:select disabled="${displayFlag}" property="jobPositionId" styleClass="content-field" style="min-width:100px;">
                                  <html:options collection="typeList" property="code" labelProperty="name"  />
                                </html:select>
                              </td>
                              <td>
                                <html:submit disabled="${displayFlag}" styleClass="content-buttonstyle" onclick="setActionAndDispatch('promotionAudience.do', 'addPromotionTeamPosition')">
                                  <cms:contentText key="GO" code="system.button"/>
                                </html:submit>
                              </td>
                              </c:if>
                              <td align="right">
                                <html:submit disabled="${displayFlag}" styleClass="content-buttonstyle" onclick="setActionAndDispatch('promotionAudience.do', 'removeTeamPosition')" >
                                  <cms:contentText key="REMOVE" code="system.button"/>
                                </html:submit>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </DIV> <%-- end of jobpositions DIV --%>
                </td>
              </tr>
            </table>
          </DIV><%-- end of teamaudience DIV--%>
        </td>
      </tr>
    </table>
  </c:otherwise>
</c:choose>