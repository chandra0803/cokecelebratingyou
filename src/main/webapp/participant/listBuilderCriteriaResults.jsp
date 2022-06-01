<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
  function preSubmit()
  {
    <%-- nothing to do --%>
  }

  function callUrl( urlToCall )
  {
    window.location = urlToCall;
  }
//-->
</script>

<c:set var="counter" value="${-1}"/>

<c:choose>
  <c:when test="${criteriaAudienceValue == null}">
    <table width="100%">
      <tr>
        <td class="content-bold">
          <cms:contentText key="SEARCH_RESULTS" code="participant.list.builder.details"/>&nbsp;
          (<cms:contentText key="NO_RESULTS" code="participant.list.builder.details"/>)
          <span id="selectedCount" style="display: none;">0</span>
        </td>
      </tr>
    </table>
  </c:when>

  <c:otherwise>
    <c:choose>
      <c:when test="${criteriaAudienceValue.currentAudienceCriteriaValue == null}">
        <table>
          <tr>
            <td class="content-bold">
              <cms:contentText key="CURRENT_SEARCH_RESULTS" code="participant.list.builder.details"/>&nbsp;
              (<cms:contentText key="NO_RESULTS" code="participant.list.builder.details"/>)
            </td>
          </tr>
        </table>
      </c:when>

      <c:otherwise>
        <table>
          <tr>
            <td class="content-bold">
              <cms:contentText key="CURRENT_SEARCH_RESULTS" code="participant.list.builder.details"/>&nbsp;
              ( <c:out value="${1}"/> )
            </td>
          </tr>
        </table>

        <c:choose>
          <c:when test="${criteriaAudienceValue.currentAudienceCriteriaValue.paxListSize < 1}">
            <c:set var="paxCount" value="${1}"/>
          </c:when>

          <c:when test="${criteriaAudienceValue.currentAudienceCriteriaValue.paxListSize < 11}">
            <c:set var="paxCount" value="${criteriaAudienceValue.currentAudienceCriteriaValue.paxListSize}"/>
          </c:when>

          <c:otherwise>
            <c:set var="paxCount" value="${10}"/>
          </c:otherwise>
        </c:choose>

        <table class="lb-searchresults-table" style="width: 100%">
          <tr>
            <td class="content" valign="top" nowrap>
              <cms:contentText key="SEARCH_CRITERIA" code="participant.list.builder.details"/>:
            </td>
            <td class="content" valign="top" nowrap>
              <c:forEach items="${criteriaAudienceValue.currentAudienceCriteriaValue.criteriaList}" var="criteria">
                <c:out value="${criteria}"/><br/>
             </c:forEach>
            </td>
            <td class="content" valign="top" nowrap>
              <cms:contentText key="EXCLUDE_CRITERIA" code="participant.list.builder.details"/>:
            </td>
            <td class="content" valign="top" nowrap>
              <c:forEach items="${criteriaAudienceValue.currentAudienceCriteriaValue.exclusionCriteriaList}" var="excludeCriteria">
                <c:out value="${excludeCriteria}"/><br/>
             </c:forEach>
            </td>
            <td class="lb-searchresults-table-right" valign="top">
              <nowrap>
              <c:choose>
              <c:when test="${criteriaAudienceValue.currentAudienceCriteriaValue.paxListSize < 1}"> 
              <cms:contentText key="PARTICIPANT_RESULTS_TO_BE_CALCULATED" code="participant.list.builder.details"/>
              </c:when>
              <c:otherwise> 
              <c:out value="${criteriaAudienceValue.currentAudienceCriteriaValue.paxListSize}"/>&nbsp;
              <cms:contentText key="PARTICIPANTS" code="participant.list.builder.details"/>
              </c:otherwise>
              </c:choose>
              </nowrap>
              <br/>
              <c:if test="${criteriaAudienceValue.currentAudienceCriteriaValue.paxListSize > 0}">
                <c:choose>
                  <c:when test="${criteriaAudienceValue.currentAudienceCriteriaValue.viewPaxList == true}">
                    <c:url var="hideListUrl" value="listBuilder.do" >
                      <c:param name="audienceCriteriaValueViewIndex" value="${counter}" />
                    </c:url>
                    <a href="javascript:setActionDispatchAndSubmit('<c:out value="${hideListUrl}"/>','hideList');" class="content-link">
                      <cms:contentText key="HIDE_LIST" code="participant.list.builder.details"/>
                    </a>
                  </c:when>
                  <c:when test="${criteriaAudienceValue.currentAudienceCriteriaValue.viewPaxList == false}">
                    <c:url var="viewListUrl" value="listBuilder.do" >
                      <c:param name="audienceCriteriaValueViewIndex" value="${counter}" />
                    </c:url>
                    <a href="javascript:setActionDispatchAndSubmit('<c:out value="${viewListUrl}"/>','viewList');" class="content-link">
                      <cms:contentText key="VIEW_LIST" code="participant.list.builder.details"/>
                    </a>
                  </c:when>
                </c:choose>
              </c:if>
            </td>
          </tr>

          <c:if test="${criteriaAudienceValue.currentAudienceCriteriaValue.paxListSize > 0}">
            <c:if test="${criteriaAudienceValue.currentAudienceCriteriaValue.viewPaxList == true}">
              <tr>
                <td colspan="3" align="center">
                  <select size="<c:out value='${paxCount}'/>" class="lb-select">
                    <c:forEach items="${criteriaAudienceValue.currentAudienceCriteriaValue.paxList}" var="listBuilderResult">
                      <option><c:out value="${listBuilderResult}"/></option>
                    </c:forEach>
                  </select>
                </td>
              </tr>
            </c:if>
          </c:if>
        </table>
        <br/>
      </c:otherwise>
    </c:choose>

    <div align="center">
      <c:choose>
        <c:when test="${criteriaAudienceValue.currentAudienceCriteriaValue == null}">
          <br/>
          <html:button property="add" disabled="true" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('listBuilder.do','addAudienceCriteriaToList')">
            <cms:contentText key="ADD_GROUP" code="participant.list.builder.details"/>
          </html:button>
          <br/>
        </c:when>

        <c:when test="${criteriaAudienceValue.currentAudienceCriteriaValue != null}">
          <br/>
          <html:button property="add" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('listBuilder.do','addAudienceCriteriaToList')">
            <cms:contentText key="ADD_GROUP" code="participant.list.builder.details"/>
          </html:button>
          <br/>
        </c:when>
      </c:choose>
    </div>
    <br/>
    <br/>
    <br/>

    <html:hidden property="audienceCriteriaValueListCount" value="${criteriaAudienceValue.audienceCriteriaValueListSize}"/>

    <c:choose>
      <c:when test="${criteriaAudienceValue.audienceCriteriaValueListSize < 1}">
        <table>
          <tr>
            <td class="content-bold" nowrap>
              <cms:contentText key="SELECTED" code="participant.list.builder.details"/>&nbsp;(<span id="selectedCount">0</span>):
            </td>
          </tr>
        </table>
      </c:when>

      <c:otherwise>
        <table>
          <tr>
            <td class="content-bold" nowrap>
              <cms:contentText key="SELECTED" code="participant.list.builder.details"/>&nbsp;
              (<span id="selectedCount"><c:out value="${criteriaAudienceValue.audienceCriteriaValueListSize}"/></span>):
            </td>
            <c:set var="conditionVariable" value ="true"/>                  
            <c:forEach items="${criteriaAudienceValue.audienceCriteriaValueList}" var="audienceCriteriaPaxValue">
            <c:if test="${conditionVariable eq 'true'}">
            <c:set var="counterPax" value="${0}"/>
            
            <td class="lb-searchresults-table-right" valign="top" nowrap>
                    <c:choose>
                    <c:when test="${audienceCriteriaPaxValue.paxListSize < 1 && audienceCriteriaPaxValue.audienceCriteria.id == null}"> 
                    <cms:contentText key="PARTICIPANT_RESULTS_TO_BE_CALCULATED" code="participant.list.builder.details"/>
                    </c:when> 
                    <c:otherwise> 
                    <c:out value="${audienceCriteriaPaxValue.paxListSize}"/>&nbsp;
                    <cms:contentText key="PARTICIPANTS" code="participant.list.builder.details"/>
                    </c:otherwise>
                    </c:choose>
                      <br/>
                      <c:if test="${audienceCriteriaPaxValue.paxListSize > 0}">
                        <c:choose>
                          <c:when test="${audienceCriteriaPaxValue.viewPaxList == true}">
                            <c:url var="hideListUrl" value="listBuilder.do" >
                              <c:param name="audienceCriteriaValueViewIndex" value="${counterPax}" />
                            </c:url>
                            <a href="javascript:setActionDispatchAndSubmit('<c:out value="${hideListUrl}"/>','hideList');" class="content-link">
                              <cms:contentText key="HIDE_LIST" code="participant.list.builder.details"/>
                            </a>
                          </c:when>

                          <c:when test="${audienceCriteriaPaxValue.viewPaxList == false}">
                            <c:url var="viewListUrl" value="listBuilder.do" >
                              <c:param name="audienceCriteriaValueViewIndex" value="${counterPax}" />
                            </c:url>
                            <a href="javascript:setActionDispatchAndSubmit('<c:out value="${viewListUrl}"/>','viewList');" class="content-link">
                              <cms:contentText key="VIEW_LIST" code="participant.list.builder.details"/>
                            </a>
                          </c:when>
                        </c:choose>
                      </c:if>
                    </td>
                  <c:if test="${audienceCriteriaPaxValue.viewPaxList == true}">
                  <c:if test="${audienceCriteriaPaxValue.paxListSize > 0}">
                    <c:choose>
                      <c:when test="${audienceCriteriaPaxValue.paxListSize < 1}">
                        <c:set var="paxCount" value="${1}"/>
                      </c:when>

                      <c:when test="${audienceCriteriaPaxValue.paxListSize < 11}">
                        <c:set var="paxCount" value="${audienceCriteriaPaxValue.paxListSize}"/>
                      </c:when>

                      <c:otherwise>
                        <c:set var="paxCount" value="${10}"/>
                      </c:otherwise>
                    </c:choose>

                    <tr>
                      <td colspan="3" align="center">
                        <select size="<c:out value="${paxCount}"/>" class="lb-select">
                          <c:forEach items="${audienceCriteriaPaxValue.paxList}" var="listBuilderResult">
                            <option><c:out value="${listBuilderResult}"/></option><br/>
                          </c:forEach>
                        </select>
                      </td>
                    </tr>
                  </c:if>
                </c:if>
                </c:if>
                <c:set var="conditionVariable" value="false"/>
                </c:forEach>
          </tr>
        </table>

        <table>
          <c:forEach items="${criteriaAudienceValue.audienceCriteriaValueList}" var="audienceCriteriaValue">
            <c:set var="counter" value="${counter + 1}"/>
            
            <tr>
              <td>
                <table class="lb-searchresults-table" style="width: 100%">
                  <tr>
                    <td class="content" valign="top" nowrap>
                      <cms:contentText key="SEARCH_CRITERIA" code="participant.list.builder.details"/>:
                    </td>
                
                    <td class="content" valign="top" nowrap width="200">
                      <c:forEach items="${audienceCriteriaValue.criteriaList}" var="criteria">
                        <c:out value="${criteria}"/><br/>
                      </c:forEach>
                    </td>
                    
                    <td class="content" valign="top" nowrap>
                      <cms:contentText key="EXCLUDE_CRITERIA" code="participant.list.builder.details"/>:
                    </td>
                    
                    <td class="content" valign="top" nowrap width="200">
                      <c:forEach items="${audienceCriteriaValue.exclusionCriteriaList}" var="excludeCriteria">
                        <c:out value="${excludeCriteria}"/><br/>
                      </c:forEach>
                    </td>

                    <td class="content" valign="top">
                      <input type="checkbox" name="remove_checkbox_<c:out value="${counter}"/>">
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </c:forEach>

	<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
        <c:if test="${criteriaAudienceValue.audienceCriteriaValueListSize > 0}">
          <tr>
            <td width="50%">
              <table width="100%">
                <tr>
                  <td align="right" valign="top" width="100%">
                    <html:button property="add" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('listBuilder.do','removeAudienceCriteriaFromList')">
                      <cms:contentText key="REMOVE_SELECTED" code="participant.list.builder.details"/>
                    </html:button>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </c:if>
    </beacon:authorize>
      </table>
    </c:otherwise>
  </c:choose>

  </c:otherwise>
</c:choose>
<table>
  <tr class="form-row-spacer">
	<beacon:label property="audienceName">
	  <cms:contentText key="AUDIENCE_PUBLIC" code="participant.list.builder.details"/>
    </beacon:label>
    <td class="content-field">
      <html:checkbox property="publicAudience" value="TRUE" />
    </td>
  </tr>
</table>
        
