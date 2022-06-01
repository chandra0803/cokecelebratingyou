<%--  UI Refactored --%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.promotion.PromotionECard"%>
<%@ page import="com.biperf.core.ui.utils.CardUtilties"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<%@ include file="/include/yui-imports.jspf"%>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tooltip.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/cards/cards.js" type="text/javascript"></script>


<script language="JavaScript">

function selectCardFromRadio( tdElement, index )
{
	var dom = YAHOO.util.Dom ;
	var els = dom.getElementsByClassName  ( "outerCardOn" , null , null , null ) ;
	for ( i=0;i<els.length;i++)
		unhighlight( els[i] ) ;
	highlight( index ) ;
}

function selectCard( tdElement, index )
{	
	var isFirstAccess = (getCurrentSelectedElement()==null) ;
	var option = document.getElementById( "radio_"+index );
	
	// unhighlight the current selection if necessary
	if ( !isFirstAccess )
	{
		var currentSelected = getCurrentSelectedElement() ;	
		if ( null != currentSelected )
		{
		
			unhighlight( currentSelected ) ;
			// uncheck the current radio button if necessary
			currentSelected.checked = false ;	
		}
	}
	
	// check the correct radio button	
	option.checked = true ;
	// highlight the selected area
	highlight( index ) ;	
}

function getIndex( str )
{
	return str.substring( str.indexOf("_")+1, str.length ) ;
}

function unhighlight( btn )
{
	var id = btn.id ;
	var index = getIndex( id ) ;
	var el = document.getElementById("cardOuterTd_"+index);
	YAHOO.util.Dom.replaceClass(el, "outerCardOn", "outerCard");

	var el = document.getElementById("cardTd_"+index);
	YAHOO.util.Dom.replaceClass(el, "cardOn", "card");
}

function highlight( index )
{
	var el = document.getElementById("cardOuterTd_"+index);
	YAHOO.util.Dom.replaceClass(el, "outerCard", "outerCardOn");

	var el = document.getElementById("cardTd_"+index);
	YAHOO.util.Dom.replaceClass(el,"card", "cardOn");
}

function getCurrentSelectedElement()
{
	var radioList = document.recognitionOptionsForm.cardId ;
	for ( i=0;i<radioList.length; i++ )
	{
		if ( radioList[i].checked )
			return radioList[i];
	}
}

YAHOO.namespace("cardToolTip");
									
</script>

<c:if test="${flashNeeded}">
  <script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/flash.js"></script>
</c:if>

<html:hidden property="electronicCardsBeginIndex"/>
<html:hidden property="electronicCardsEndIndex"/>
<html:hidden property="electronicCardsMaxIndex"/>
<%--
<beacon:client-state>
 	<beacon:client-state-entry name="cardId" value="${recognitionSelectCardForm.cardId}"/>
</beacon:client-state>
--%>
<%-- Select a Card Column --%>
<table border="0" cellpadding="3" cellspacing="1" width="100%">
	<tr>
    	<td valign="top" width="50%" align="left">
            <span class="subheadline"><cms:contentText key="TITLE" code="recognition.select.card"/></span>
              <%--INSTRUCTIONS--%>
              <br/>
              <span class="content-instruction">
                <cms:contentText key="INSTRUCTIONS" code="recognition.select.card"/>
              </span>
              <br/><br/>
              <%--END INSTRUCTIONS--%>
        </td>
	</tr>
      
	<%-- BEGIN cards row --%>
    <tr>
		<td align="left" width="100%">
			<table cellpadding="3" cellspacing="1" width="100%">

            <c:if test="${electronicCardsCount > 0 or certificatesCount > 0}">
              <%-- eCards --%>
              <tr>
                <td class="content-bold" width="100%"><cms:contentText code="recognition.select.card" key="ECARDS" />
                  <table width="100%">
                    <tr>
                      <c:set var="ecardIndex" value="1"/>
                      <%-- always start with the no card option  --%>
                      <td id="cardOuterTd_-1" class="outerCard" align="center" width="20%" 
                      		onmouseover="YAHOO.util.Dom.replaceClass(this, 'outerCard', 'outerCardOn');" 
                      		onmouseout="if( !document.recognitionOptionsForm.cardId[0].checked ){ YAHOO.util.Dom.replaceClass(this, 'outerCardOn', 'outerCard'); }" 
                      		onclick="selectCard( this, '-1' );" >   	
							<table align="center" cellpadding="10" valign="center">
								<tr>
									<td id="cardTd_-1" class="card" align="center" valign="center">
			                         	<img  src="<%=RequestUtils.getBaseURI(request)%>/<cms:contentText code="recognition.ecards.imgSrc.prefix" key="IMG_SRC_PREFIX"/>no-ecard.gif"
			                            	   alt="<cms:contentText key="NO_CARD" code="recognition.select.card"/>" border="0"/>
			    					</td>
			    				</tr>
			    			</table>
                          	<b><cms:contentText key="NO_CARD" code="recognition.select.card"/></b>
                          	<br>
                          	<html:radio onclick="selectCardFromRadio( document.getElementById('cardTd_-1'), '-1' );" name="recognitionOptionsForm" value="-1" property="cardId" styleId="radio_-1"/>
                          	<%-- highlight this card if it was previously selected --%>
                          	<c:if test="${ recognitionOptionsForm.cardId == '-1' || null==recognitionOptionsForm.cardId }">
                          		<script language="JavaScript">
                         			selectCard(null, -1);
                          		</script>
                          	</c:if>
                      </td>
                      <%-- end No Card content --%>
                      <%-- The ecard image --%>
                      <c:forEach items="${electronicCards}" var="promotionECard" varStatus='status'>
                        <%-- Wrap 4 images per row --%>
                        <c:if test='${ecardIndex % 5 == 0  && ecardIndex != 0}'>
                          </tr>
                          <tr class="form-row-spacer"></tr>
                          <tr>
                        </c:if>
                                                
                        <td id="cardOuterTd_<c:out value="${ecardIndex}"/>" class="outerCard" align="center" width="20%" onclick="selectCard( this, '<c:out value="${ecardIndex}"/>' );" >
                          	
                          	<cms:content var="image" code="recognition.ecards.imgSrc.prefix"/>
							<%  
								PromotionECard temp = (PromotionECard)pageContext.getAttribute("promotionECard");
							%>
							<table align="center" cellpadding="10" valign="center">
								<tr>
									<td id="cardTd_<c:out value="${ecardIndex}"/>" class="card" align="center" valign="center">
			                         	<img id="card_img_<c:out value="${ecardIndex}"/>" src="<c:out value='${promotionECard.eCard.smallImageName}'/>"
			                            	   alt="<c:out value='${promotionECard.eCard.name}'/>" border="0"/>
			                          	
			                          	<script language="JavaScript">
				                          	YAHOO.cardToolTip.cardToolTipWidget = new YAHOO.bi.widget.Tooltip("cardToolTipWidget", { context:"cardOuterTd_<c:out value="${ecardIndex}"/>", 
				                          		text:"<table border='0' align='center'><tr><td align='center'><%=CardUtilties.getJavaScriptEscapedCardPopupContent( temp.geteCard(), request )%></td></tr><tr><td align='left'><center><b><c:out escapeXml="false" value="${ promotionECard.eCard.name }" /></b></center></td></tr></table>",showDelay:750,autodismissdelay:999999,width:'375px' });
			    						</script>
			    					</td>
			    				</tr>
			    			</table>
                          	<b><c:out value="${promotionECard.eCard.name}"/></b>
                          	<br>
                          	<c:set var="formattedId" value="card:${promotionECard.eCard.id}" />
                          	<html:radio onclick="selectCardFromRadio( document.getElementById('cardTd_${ecardIndex}'), '${ecardIndex}' );" name="recognitionOptionsForm" value="${ formattedId  }" property="cardId" styleId="radio_${ecardIndex}"  />
                          	<%-- highlight this card if it was previously selected --%>
                          	<c:if test="${ recognitionOptionsForm.cardId == formattedId }">
                          		<script language="JavaScript"> highlight( "<c:out value="${ecardIndex}"/>"  ); </script>
                          	</c:if>
                        </td>
                        
                        <c:set var="ecardIndex" value="${ecardIndex+1}"/>
                      </c:forEach>
                      <c:forEach items="${certificatesSet}" var="certificate">
                        <%-- Wrap 4 images per row --%>
                        <c:if test='${ecardIndex % 5 == 0  && ecardIndex != 0}'>
                          </tr>
                          <tr class="form-row-spacer"></tr>
                          <tr>
                        </c:if>
                                                
                        <td id="cardOuterTd_<c:out value="${ecardIndex}"/>" class="outerCard" align="center" width="20%" onclick="selectCard( this, '<c:out escapeXml='false' value="${ecardIndex}"/>' );" >
                          	
                          	<cms:content var="image" code="recognition.ecards.imgSrc.prefix"/>
							<table align="center" cellpadding="10" valign="center">
								<tr>
									<td id="cardTd_<c:out value="${ecardIndex}"/>" class="card" align="center" valign="center">
			                         	<img id="card_img_<c:out value="${ecardIndex}"/>" src="<%=RequestUtils.getBaseURI(request)%>/<cms:contentText code="recognition.ecards.imgSrc.prefix" key="CERTIFICATE_IMG_SRC_PREFIX"/><c:out escapeXml='false' value='${certificate.previewImageName}' />"
			                            	   alt="<c:out value='${certificate.name}'/>" border="0"/>
			                          	
			                          	<script language="JavaScript">
				                          	YAHOO.cardToolTip.cardToolTipWidget = new YAHOO.bi.widget.Tooltip("cardToolTipWidget", { context:"cardOuterTd_<c:out value="${ecardIndex}"/>", 
				                          		text:"<table border='0' align='center'><tr><td align='center'><img id='card_img_<c:out value="${ecardIndex}"/>' src='<%=RequestUtils.getBaseURI(request)%>/<cms:contentText code="recognition.ecards.imgSrc.prefix" key="CERTIFICATE_IMG_SRC_PREFIX"/><c:out value="${certificate.imageName}"/>' alt='<c:out value="${certificate.name}"/>' border='0'/></td></tr><tr><td align='left'><center><b><c:out escapeXml="false" value="${ certificate.name }" /></b></center></td></tr></table>",showDelay:750,autodismissdelay:999999,width:'375px' });
			    						</script>
			    					</td>
			    				</tr>
			    			</table>
                          	<b><c:out value="${certificate.name}"/></b>
                          	<br>
                          	<c:set var="formattedId" value="certificate:${ certificate.certificateId  }" />
                          	<html:radio onclick="selectCardFromRadio( document.getElementById('cardTd_${ecardIndex}'), '${ecardIndex}' );" name="recognitionOptionsForm" value="${ formattedId  }" property="cardId" styleId="radio_${ecardIndex}"  />
                          	<%-- highlight this card if it was previously selected --%>
                          	<c:if test="${ recognitionOptionsForm.cardId == formattedId }">
                          		<script language="JavaScript"> highlight( "<c:out value="${ecardIndex}"/>"  ); </script>
                          	</c:if>
                        </td>
                        
                        <c:set var="ecardIndex" value="${ecardIndex+1}"/>
                      </c:forEach>                      
                    </tr>
                  </table>
                </td>
              </tr>
            </c:if>
          </table>
        </td>
      </tr>
      <%-- END cards row --%>
      <tr class="form-row-spacer"></tr>
</table>
<script>
	YAHOO.util.Event.addListener(window, "load", function(){ YAHOO.util.Dom.addClass(document.body,"bi-yui"), YAHOO.bi.Hover.addAllElements("outerCard") });
</script>