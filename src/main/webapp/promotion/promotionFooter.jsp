<%@ page import="java.util.*" %>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceForm"%>

<script type="text/javascript">
<!--
  function confirmed ()
  {
    var MESSAGE = "<cms:contentText key="EXIT_WIZARD_WARNING" code="promotion.errors"/>";

    var answer = confirm(MESSAGE);
    if (answer)
      return true;
    else
      return false;
  }

  function parentChangeConfirmed ()
  {
    var MESSAGE = "<cms:contentText key="PARENT_CHANGE_WARNING" code="promotion.errors"/>";

    var answer = confirm(MESSAGE);
    if (answer)
      return true;
    else
      return false;
  }

  function clearDatesAndSetDispatch( dispatch )
  {
	selectCombos();
    if( getContentForm().endDate != null )
    {
      clearDateMask(getContentForm().endDate);
    }
    setDispatch(dispatch);
  }

  function selectCombos(){
	  var comboxs=document.getElementsByTagName('select');
	  if(comboxs){
		  var comlen=comboxs.length;
		  var i=0;
		  for(i=0;i<comlen;i++) {
			  var cid=comboxs[i].id;
			  if(cid.indexOf('selectedBox')>-1) {
				  selectAll(cid);
			  }
		  }
	  }
  }

  function clearDatesAndSetActionAndDispatch(action, dispatch)
  {
    if( getContentForm().endDate != null )
    {
      clearDateMask(getContentForm().endDate);
    }
    setActionAndDispatch(action, dispatch);
  }

  function toConfirm()
  {
    return needToConfirm;
  }

 function deletePendigStackRankPayouts ()
  {
    var MESSAGE = "<cms:contentText key="DELETE_PEND_STACK_RANK_LIST_CONFIRM" code="promotion.payout.errors"/>";
    var answer = confirm(MESSAGE);
      if (answer)
        return true;
      else
        return false;
  }

  function confirmAndSetDispatch( )
  {
    if( toConfirm())
    {
     if (deletePendigStackRankPayouts())
      {
       setDispatch('updateStackRankPayoutsAndSave');
      } else
      {
        return false;
      }
    }
    else
    {
     setDispatch('save');
    }

  }
//-->
</script>

<table width="100%">
  <tr>
    <td align="center">
      <table>
        <tr class="form-buttonrow">
          <td class="headline" align="left" nowrap="nowrap">
          <c:choose>
 			<c:when test="${promotionAudienceForm.validateAudience}">
 				<beacon:authorize ifNotGranted="LOGIN_AS"> 
                  <html:submit styleClass="content-buttonstyle" onclick="clearDatesAndSetDispatch('save')" >
                    <cms:contentText code="promotion.overview" key="SAVE_AUDIENCE"/>
                  </html:submit>
                  </beacon:authorize>
                  <beacon:authorize ifNotGranted="LOGIN_AS"> 
					<html:button property="markAsComplete" styleClass="content-buttonstyle" onclick="callUrl('${completeUrl}')" >                                       
                      <cms:contentText code="promotion.overview" key="YES_SAVE_ONLINE_PROMO" />
                    </html:button>
                  </beacon:authorize>
                  <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('save')">
                    <cms:contentText code="promotion.overview" key="NO_TAKE_BACK" />
                  </html:cancel>
           </c:when>
           <c:otherwise>
           <c:choose>           
            <%--  add buttons for the wizard mode --%>
            <c:when test="${s_pageMode == 'c_wizard'}">
              <%-- Show "save and continue" or continue --%>
              <c:if test="${ !isLastPage }">
                <c:choose>
                  <c:when test="${isPageEditable}">
                    <beacon:authorize ifNotGranted="LOGIN_AS">
                        <html:submit property="saveAndContinue" styleClass="content-buttonstyle" onclick="clearDatesAndSetDispatch('save')">
                          <cms:contentText code="system.button" key="SAVE_CONTINUE" />
                        </html:submit>
                    </beacon:authorize>
                  </c:when>
                  <c:otherwise>
                      <html:submit property="saveAndContinue" styleClass="content-buttonstyle" onclick="clearDatesAndSetDispatch('continueNoSave')">
                        <cms:contentText code="system.button" key="CONTINUE" />
                      </html:submit>
                  </c:otherwise>
                </c:choose>
              </c:if>

              <%-- Show "save and exit" or exit --%>
              <c:choose>
                <c:when test="${isPageEditable}">
                  <beacon:authorize ifNotGranted="LOGIN_AS">
                      <html:submit property="saveAndExit" styleClass="content-buttonstyle" onclick="clearDatesAndSetDispatch('save')">
                        <cms:contentText code="system.button" key="SAVE_EXIT" />
                      </html:submit>
                  </beacon:authorize>
                </c:when>
                <c:otherwise>
                    <html:submit property="saveAndContinue" styleClass="content-buttonstyle" onclick="clearDatesAndSetDispatch('exit')">
                      <cms:contentText code="system.button" key="EXIT" />
                    </html:submit>
                </c:otherwise>
              </c:choose>

              <c:if test="${!isFirstPage && !isBackToAwards && !isBackToSweepStake && !isBackToAudience && !isBackToApproval }">
                  <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('back')" >
                    <cms:contentText code="system.button" key="BACK" />
                  </html:cancel>
              </c:if>
              <c:if test="${isBackToAudience}">
                  <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('backDIYQuiz')" >
                    <cms:contentText code="system.button" key="BACK" />
                  </html:cancel>
              </c:if>              
              <c:if test="${isBackToAwards}">
                  <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('backToAwards')" >
                    <cms:contentText code="system.button" key="BACK" />
                  </html:cancel>
              </c:if>
              <c:if test="${isBackToSweepStake}">
                  <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('backToSweepStakes')" >
                    <cms:contentText code="system.button" key="BACK" />
                  </html:cancel>
              </c:if>
              <c:if test="${isBackToApproval}">
                  <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('backToApproval')" >
                    <cms:contentText code="system.button" key="BACK" />
                  </html:cancel>
              </c:if>
                <html:cancel styleClass="content-buttonstyle" onclick="if (confirmed()) {setDispatch('save')} else {return false}">
                  <cms:contentText code="system.button" key="CANCEL" />
                </html:cancel>
            </c:when>
            <c:otherwise>

              <%--  add buttons for the non-wizard mode --%>
              <c:choose>
                <c:when test="${ isPageEditable}">
                  <beacon:authorize ifNotGranted="LOGIN_AS">
                    <c:if test="${ hasChildren }">
                      <html:submit styleClass="content-buttonstyle" onclick="if (parentChangeConfirmed()) {clearDatesAndSetDispatch('save')} else {return false}" >
                        <cms:contentText code="system.button" key="SAVE"/>
                      </html:submit>
                    </c:if>
                    <c:if test="${ !hasChildren }">
                      <html:submit styleClass="content-buttonstyle" onclick="clearDatesAndSetDispatch('save')" >
                        <cms:contentText code="system.button" key="SAVE"/>
                      </html:submit>
                    </c:if>
                  </beacon:authorize>
                  <beacon:authorize ifAnyGranted="LOGIN_AS"> 
                   <c:if test="${isPageEditable && eCard }">
                   <html:submit styleClass="content-buttonstyle" onclick="clearDatesAndSetDispatch('save')" >
                        <cms:contentText code="system.button" key="SAVE"/>
                      </html:submit>
                   </c:if>
                  </beacon:authorize>
                  <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('save')">
                    <cms:contentText code="system.button" key="CANCEL" />
                  </html:cancel>
                </c:when>
                <c:otherwise>
                  <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('save')">
                    <cms:contentText code="system.button" key="CLOSE" />
                  </html:cancel>
                </c:otherwise>
              </c:choose>

            </c:otherwise>
          </c:choose>
         </c:otherwise>
         </c:choose>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>