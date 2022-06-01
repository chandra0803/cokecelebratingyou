<%@page import="java.util.List"%>
<%@page import="com.biperf.core.ui.promotion.PromotionSSIAwardsForm"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionWizardManager"%>
	
	<tr class="form-row-spacer">			
		<td>
	         &nbsp;&nbsp;&nbsp;<b><cms:contentText key="DIY_CONTEST_BADGES" code="promotion.ssi.awards" /></b>
	    </td>
	 </tr>
	 <tr>
	     <td class="content-field">
	       	<table>
	        	<html:hidden property="badgeId" name="promotionSSIAwardsForm" value="${promotionSSIAwardsForm.badgeId}"/>
	        	<logic:iterate name="badgeList" id="content" indexId="index">
		         	<html:hidden property="promotionSSIBadgeFormBean[${index}].badgeRuleId" name="promotionSSIAwardsForm"/>
		         	<% 
		         		PromotionSSIAwardsForm basicsFormBean = (PromotionSSIAwardsForm)request.getAttribute( "promotionSSIAwardsForm" );
		         		String isBadgeDisabled = basicsFormBean.getPromotionSSIBadgeFormBean( index ).getDisable(  );
		         	%>
		         	<tr>
		         		<td colspan=2 class="content-field">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td colspan=2 class="content-field">
							<html:checkbox property="promotionSSIBadgeFormBean[${index}].selected" name="promotionSSIAwardsForm" value="true"><c:out value='${content.libraryname}'  /></html:checkbox>
						</td>
						<td colspan=2 class="content-field">
							<img src="<%=RequestUtils.getBaseURI(request)%>${content.earnedImageSmall}"/>							
						</td>
						<td colspan=2 class="content-field">
							<html:text property="promotionSSIBadgeFormBean[${index}].badgeName" name="promotionSSIAwardsForm" size="30" maxlength="50" styleClass="content-field"/>					
						</td>
					</tr>
					<html:hidden property="promotionSSIBadgeFormBean[${index}].cmAssetKey" name="promotionSSIAwardsForm" value="${content.badgeLibraryId}"/>
				</logic:iterate>
	      	</table>
	     </td>
	</tr>