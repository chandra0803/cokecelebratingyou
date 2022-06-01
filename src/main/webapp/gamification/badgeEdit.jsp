<%--UI REFACTORED--%>
<%@page import="com.biperf.core.domain.gamification.BadgeRule"%>
<%@page import="com.biperf.core.utils.MessageUtils"%>
<%@page import="java.util.Map"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.gamification.BadgeForm"%>

<script type="text/javascript"
	src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript"
	src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript"
	src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery-1.4.4.min.js" type="text/javascript"></script>
 <script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery.pngFix.pack.js" type="text/javascript"></script>

<script type="text/javascript">
<%
String fromPage=null;
if(request.getAttribute("fromPage")!=null)
 fromPage=request.getAttribute("fromPage").toString();

String badgeType=null;
if(request.getAttribute("badgeType")!=null)
  badgeType=request.getAttribute("badgeType").toString();

String isPointRange="";
if(request.getAttribute("isPointRange")!=null)
  isPointRange=request.getAttribute("isPointRange").toString();

String isEndDateNull="";
if(request.getAttribute("isEndDateNull")!=null)
  isEndDateNull=request.getAttribute("isEndDateNull").toString();

String isGoalQuest="";
if(request.getAttribute("isGoalQuest")!=null)
  isGoalQuest=request.getAttribute("isGoalQuest").toString();

String hasPartners="";
if(request.getAttribute("isPartners")!=null)
  hasPartners=request.getAttribute("isPartners").toString();

String isThrowDown="";
if(request.getAttribute("isThrowDown")!=null)
  isThrowDown=request.getAttribute("isThrowDown").toString();

String hasStackStandingPayouts="";
if(request.getAttribute("hasStackStandingPayouts")!=null)
  hasStackStandingPayouts=request.getAttribute("hasStackStandingPayouts").toString();

String isShowFileloadNoPromoDiv="";
if(request.getAttribute("isShowFileloadNoPromoDiv")!=null)
  isShowFileloadNoPromoDiv=request.getAttribute("isShowFileloadNoPromoDiv").toString();

%>
var SPLIT_TOKEN = ',';
var REPLACEMENT_TOKEN = '&#664;';
$(document).ready(function(){
	$("#errorShowDiv").hide();
    var selectedBadgeType ="<%=badgeType%>";
    var fromPage="<%=fromPage%>";
    var pointRange="<%=isPointRange%>";
    var isEndDateNull="<%=isEndDateNull%>";
    var isGoalQuest="<%=isGoalQuest%>"
    var isThrowDown="<%=isThrowDown%>"
    var hasPartners="<%=hasPartners%>";
    var hasStackStandingPayouts="<%=hasStackStandingPayouts%>";

    var fileLoadtableSize=${currentFileLoadTableSize};
    var progresstableSize=${currentProgressTableSize};
    var pointRangetableSize=${currentPointRangeTableSize};

    document.getElementById("currentFileLoadTableSize").value=fileLoadtableSize;
    document.getElementById("currentProgressTableSize").value=progresstableSize;
    document.getElementById("currentPointRangeTableSize").value=pointRangetableSize;

    if(isEndDateNull=='Y')
    {
    	 $("#displayDays").val('0');
		 $("#displayDays").attr("disabled", "disabled");
	}
	else
	{
	     $("#displayDays").removeAttr("disabled");
	}
    if(selectedBadgeType=='progress')
    {
    		$("#behaviorBadgeDiv").hide();
			$("#earnedNotEarnedBadgeLevelDiv").hide();
			$("#earnedNotEarnedBadgePointDiv").hide();
			$("#earnedNotEarnedGQBadgeLevelDiv").hide();
			$('#earnedNotEarnedTDBadgeStackLevelDiv').hide();
			$("#earnedNotEarnedPartnerBadgeLevelDiv").hide();
			$("#badgeTypesFileLoad").hide();
			$("#fileLoadBadgeDiv").hide();
			var totalProgressRowsValue=document.getElementById("currentProgressTableSize").value;
			for(var progresIndex=0;progresIndex<totalProgressRowsValue;progresIndex++)
			{
			  	$("#progresscheckBoxDiv"+progresIndex).hide();
			}
			$("#progressBadgeDiv").show();

    }
    else if(selectedBadgeType=='behavior')
    {
    		var totalRows=document.getElementById("badgeRuleListSize").value;
    		for(var tdLen=0, divLen=totalRows; tdLen < divLen; tdLen++)
	     	{
	     		$("#behaviorStringRow"+tdLen).hide();
	     	}
	 		$("#progressBadgeDiv").hide();
	 		$("#earnedNotEarnedBadgeLevelDiv").hide();
	 		$("#earnedNotEarnedBadgePointDiv").hide();
	 		$("#earnedNotEarnedGQBadgeLevelDiv").hide();
	 		$('#earnedNotEarnedTDBadgeStackLevelDiv').hide();
			$("#earnedNotEarnedPartnerBadgeLevelDiv").hide();
	 		$("#badgeTypesFileLoad").hide();
	 		$("#fileLoadBadgeDiv").hide();
	 		$("#behaviorBadgeDiv").show();
    }
    else if(selectedBadgeType=='fileload')
    {
    		$("#behaviorBadgeDiv").hide();
	 		$("#progressBadgeDiv").hide();
	 		$("#earnedNotEarnedBadgeLevelDiv").hide();
	 		$("#earnedNotEarnedBadgePointDiv").hide();
	 		$("#earnedNotEarnedGQBadgeLevelDiv").hide();
	 		$('#earnedNotEarnedTDBadgeStackLevelDiv').hide();
			$("#earnedNotEarnedPartnerBadgeLevelDiv").hide();
	 		var totalFileLoadRowsValue=document.getElementById("currentFileLoadTableSize").value;
	 		for(var fileIndex=0;fileIndex<totalFileLoadRowsValue;fileIndex++)
			{
			  	$("#fileLoadcheckBoxDiv"+fileIndex).hide();
			}
	 		$("#fileLoadBadgeDiv").show();
    }
    else if(selectedBadgeType=='earned')
    {
    		$("#earnedNotEarnedBadgePointDiv").hide();
   			$("#earnedNotEarnedBadgeLevelDiv").hide();
    		if(isGoalQuest=='Y')
    		{

    			$("#earnedNotEarnedTDBadgeStackLevelDiv").hide();
    			$("#earnedNotEarnedGQBadgeLevelDiv").show();
    			if(hasPartners=='Y'){
    				$("#earnedNotEarnedPartnerBadgeLevelDiv").show();
    			} else {
    				$("#earnedNotEarnedPartnerBadgeLevelDiv").hide();
    			}
    		}
    		else if(isThrowDown=='Y' && pointRange=='N')
    		{

    			$("#earnedNotEarnedGQBadgeLevelDiv").hide();
    			$("#earnedNotEarnedPartnerBadgeLevelDiv").hide();
    			$("#earnedNotEarnedTDBadgeStackLevelDiv").show();
    		}
    		else if(pointRange!=null&& pointRange=='Y')
    		{

    			$("#earnedNotEarnedBadgeLevelDiv").hide();
    			var totalPointRangeRowsValue=document.getElementById("currentPointRangeTableSize").value;
    			for(var pointIndex=0;pointIndex<totalPointRangeRowsValue;pointIndex++)
				 {
						  	$("#pointRangeStringRow"+pointIndex).hide();
				 }
    			$("#earnedNotEarnedBadgePointDiv").show();
    			$("#earnedNotEarnedGQBadgeLevelDiv").hide();
    			$("#earnedNotEarnedTDBadgeStackLevelDiv").hide();
    			$("#earnedNotEarnedPartnerBadgeLevelDiv").hide();
    		}
    		else
    		{

    			$("#earnedNotEarnedBadgePointDiv").hide();
    			var totalLevelRows=document.getElementById("badgeRuleListSize").value;
    			for(var leveltdLen=0, leveldivLen=totalLevelRows; leveltdLen < leveldivLen; leveltdLen++)
			    {
			     		$("#levelStringRow"+leveltdLen).hide();
			    }

    			$("#earnedNotEarnedBadgeLevelDiv").show();
    			$("#earnedNotEarnedGQBadgeLevelDiv").hide();
    			$("#earnedNotEarnedTDBadgeStackLevelDiv").hide();
    			$("#earnedNotEarnedPartnerBadgeLevelDiv").hide();
    		}
    		$("#behaviorBadgeDiv").hide();
	 		$("#progressBadgeDiv").hide();
	 		$("#fileLoadBadgeDiv").hide();
    }



});


function validateAllOtherFields(errorMessage)
{
	var tileHighlightPeriod=$("#tileHighlightPeriod").val();
	var intRegex = /^\d+$/;

	if(!intRegex.test(trim(tileHighlightPeriod)))
	{
		 errorMessage='<cms:contentText key="VALID_TILE_HIGHLIGHT_PERIOD" code="gamification.validation.messages" />';
		 $("#errorShowDiv").html(errorMessage);
		 $("#errorShowDiv").show();
		 return false;
	}
	else if(tileHighlightPeriod<1)
	{
		 errorMessage='<cms:contentText key="TILE_HIGHLIGHT_PERIOD_MIN" code="gamification.validation.messages" />';
		 $("#errorShowDiv").html(errorMessage);
		 $("#errorShowDiv").show();
		 return false;
	}
	else if(tileHighlightPeriod>365)
	{
		 errorMessage='<cms:contentText key="TILE_HIGHLIGHT_PERIOD_MAX" code="gamification.validation.messages" />';
		 $("#errorShowDiv").html(errorMessage);
		 $("#errorShowDiv").show();
		 return false;
	}
	else
	{
		$("#errorShowDiv").hide();
	}

    var notificationMessage=$("#notificationMessageId").val();
    if(notificationMessage=='0')
    {
    	errorMessage='<cms:contentText key="VALID_NOTIIFCATION" code="gamification.validation.messages" />';
		$("#errorShowDiv").html(errorMessage);
		$("#errorShowDiv").show();
		return false;
    }

	if(selectedBadgeType=='progress')
	{
		//validate all the fields for progress

		$("input[name=progressStringRow]:checked").each(function(i)
		{
		      val[i] = $(this).val();
		     // alert('values:'+val[i]);
		      individualValueArray = val[i].split(SPLIT_TOKEN);
		      if(individualValueArray[1]=='0')
		      {
		    	  errorMessage='<cms:contentText key="VALID_BADGE_LEVEL" code="gamification.validation.messages" />';
				  $("#errorShowDiv").html(errorMessage);
				  $("#errorShowDiv").show();
				  return false;
		    	 // alert('Badge Level is required in row '+i);
		      }
		      else if(!intRegex.test(trim(individualValueArray[1])))
		  	  {
		    	     errorMessage='<cms:contentText key="VALID_NUMBER_BADGE_LEVEL" code="gamification.validation.messages" />';
			  		 $("#errorShowDiv").html(errorMessage);
			  		 $("#errorShowDiv").show();
			  		 return false;
			  }

		      else if(individualValueArray[3]=='0')
		      {
		    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
				  $("#errorShowDiv").html(errorMessage);
				  $("#errorShowDiv").show();
				  return false;
		      }
		      else if(individualValueArray[4].length>200)
		      {
		    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
				  $("#errorShowDiv").html(errorMessage);
				  $("#errorShowDiv").show();
				  return false;
		      }


		 });

		for(var index=0;index<val.length;index++)
		{
			 var currentRow = val[index].split(SPLIT_TOKEN);
			 var currentBadgeLevel=currentRow[1];
			 for(var nextIndex=index+1;nextIndex<val.length;nextIndex++)
			 {
				 var nextRow=val[nextIndex].split(SPLIT_TOKEN);
				 var nextBadgeLevel=nextRow[1];
				 if(parseInt(nextBadgeLevel)<=parseInt(currentBadgeLevel) && nextBadgeLevel!='0')
			      {
					 	  errorMessage='<cms:contentText key="BADGE_LEVEL_NEXTROW" code="gamification.validation.messages" />';
						  $("#errorShowDiv").html(errorMessage);
						  $("#errorShowDiv").show();
						  return false;
			      }
			 }

		 }


	}
	else if(selectedBadgeType=='fileload')
	{
		$("input[name=fileLoadStringRow]:checked").each(function(j)
		{
		      val[j] = $(this).val();
		      individualValueArray = val[j].split(SPLIT_TOKEN);

		      if(individualValueArray[2]=='0')
		      {
		    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
				  $("#errorShowDiv").html(errorMessage);
				  $("#errorShowDiv").show();
				  return false;
		      }
		      else if(individualValueArray[3].length>200)
		      {
		    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
				  $("#errorShowDiv").html(errorMessage);
				  $("#errorShowDiv").show();
				  return false;
		      }


		 });

	}
	else if(selectedBadgeType=='behavior')
	{
		$("input[name=behaviorStringRow]:checked").each(function(k)
		{
		      val[k] = $(this).val();
		      individualValueArray = val[k].split(SPLIT_TOKEN);
		      if(individualValueArray[4]=='0')
		      {
		    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
				  $("#errorShowDiv").html(errorMessage);
				  $("#errorShowDiv").show();
				  return false;
		      }
		      else if(individualValueArray[5].length>200)
		      {
		    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
				  $("#errorShowDiv").html(errorMessage);
				  $("#errorShowDiv").show();
				  return false;
		      }
		 });



	}
	else if(selectedBadgeType=='earned')
	{
		var pointRange="<%=isPointRange%>";
		if(pointRange!=null&& pointRange=='N')
    	{
				$("input[name=levelStringRow]:checked").each(function(m)
				{
				      val[m] = $(this).val();
				     individualValueArray = val[m].split(SPLIT_TOKEN);
				      if(individualValueArray[4]=='0')
				      {
				    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
						  $("#errorShowDiv").html(errorMessage);
						  $("#errorShowDiv").show();
						  return false;
				      }
				      else if(individualValueArray[5].length>200)
				      {
				    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
						  $("#errorShowDiv").html(errorMessage);
						  $("#errorShowDiv").show();
						  return false;
				      }
				 });
		}
		else
		{

			$("input[name=pointRangeStringRow]:checked").each(function(n)
			{
			      val[n] = $(this).val();
			      individualValueArray = val[n].split(SPLIT_TOKEN);

			     if(!intRegex.test(trim(individualValueArray[1])))
			  	  {
			    	  	 errorMessage='<cms:contentText key="POINT_RANGE_MIN_NUMBER" code="gamification.validation.messages" />';
				  		 $("#errorShowDiv").html(errorMessage);
				  		 $("#errorShowDiv").show();
				  		 return false;
				  }
			      else if(individualValueArray[1]!='0' && individualValueArray[2]=='0')
			      {
			    	  errorMessage='<cms:contentText key="POINT_RANGE_MAX_REQUIRED" code="gamification.validation.messages" />';
					  $("#errorShowDiv").html(errorMessage);
					  $("#errorShowDiv").show();
					  return false;
			      }
			      else if(!intRegex.test(trim(individualValueArray[2])))
			  	  {
			    	 errorMessage='<cms:contentText key="POINT_RANGE_MAX_NUMBER" code="gamification.validation.messages" />';
			  		 $("#errorShowDiv").html(errorMessage);
			  		 $("#errorShowDiv").show();
			  		 return false;
			  	  }
			      else if(parseInt(individualValueArray[2])<parseInt(individualValueArray[1]))
			      {
			    	  errorMessage='<cms:contentText key="POINT_MAXMIN_SAMEROW" code="gamification.validation.messages" />';
				  	  $("#errorShowDiv").html(errorMessage);
				  	  $("#errorShowDiv").show();
				  	  return false;

			      }
			      else if(individualValueArray[4]=='0')
			      {
			    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
					  $("#errorShowDiv").html(errorMessage);
					  $("#errorShowDiv").show();
					  return false;
			      }
			      else if(individualValueArray[5].length>200)
			      {
			    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
					  $("#errorShowDiv").html(errorMessage);
					  $("#errorShowDiv").show();
					  return false;
			      }



			 });

			for(var index=0;index<val.length;index++)
			 {
				 var currentRow = val[index].split(SPLIT_TOKEN);
				 var currentPointMax=currentRow[2];
				 for(var nextIndex=index+1;nextIndex<val.length;nextIndex++)
				 {
					 var nextRow=val[nextIndex].split(SPLIT_TOKEN);
					 var nextPointMin=nextRow[1];
					 if(parseInt(nextPointMin)!=parseInt(currentPointMax)+1 && nextPointMin!='0')
				      {
						 	  errorMessage='<cms:contentText key="POINT_MAXMIN_NEXTROW" code="gamification.validation.messages" />';
							  $("#errorShowDiv").html(errorMessage);
							  $("#errorShowDiv").show();
							  return false;
				      }
				 }

			 }

		}



	}
	//alert('errorMessage:'+errorMessage);
	if(errorMessage=='')
		return true;
	else
		return false;
}
function validateDisplayDays(errorMessage)
{


}
function validateFields(method, promotionType)
{
	var intRegex = /^\d+$/;
	var selectedBadgeType ="<%=badgeType%>";
    var val = [];
    var individualValueArray=[];
    var errorMessage='';
    var badgeNames = [];
    var totRows=0;

    var displayDays=$("#displayDays").val();
	var intRegex = /^\d+$/;

	if(!intRegex.test(trim(displayDays)))
	{
		 errorMessage='<cms:contentText key="VALID_DISPLAY_DAYS" code="gamification.validation.messages" />';
		 $("#errorShowDiv").html(errorMessage);
		 $("#errorShowDiv").show();
		 return false;
	}
	else
	{
		$("#errorShowDiv").hide();
	}
	var selectedPromotion=$("#selectedPromotionIds").val();

		$.ajax({
			url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=validatePromotionEndDate&promotionIds="+selectedPromotion,
			success: function(data){
				if(data != null && data != '' && data != 'null')
				{
				  data=trim(data);
				  if(data=='Y'&&parseInt(displayDays)>0)
				  {
					  $("#displayDays").val('0');
					  $("#displayDays").attr("disabled", "disabled");

				  }
				  else
				  {
					  $("#displayDays").removeAttr("disabled");
				  }
					  var tileHighlightPeriod=$("#tileHighlightPeriod").val();
					  var intRegex = /^\d+$/;

						if(!intRegex.test(trim(tileHighlightPeriod)))
						{
							 errorMessage='<cms:contentText key="VALID_TILE_HIGHLIGHT_PERIOD" code="gamification.validation.messages" />';
							 $("#errorShowDiv").html(errorMessage);
							 $("#errorShowDiv").show();
							 return false;
						}
						else if(tileHighlightPeriod>365)
						{
							 errorMessage='<cms:contentText key="TILE_HIGHLIGHT_PERIOD_MAX" code="gamification.validation.messages" />';
							 $("#errorShowDiv").html(errorMessage);
							 $("#errorShowDiv").show();
							 return false;
						}
						else
						{
							$("#errorShowDiv").hide();
						}

					    var notificationMessage=$("#notificationMessageId").val();
					    if(notificationMessage=='0')
					    {
					    	errorMessage='<cms:contentText key="VALID_NOTIIFCATION" code="gamification.validation.messages" />';
							$("#errorShowDiv").html(errorMessage);
							$("#errorShowDiv").show();
							return false;
					    }

					    var billCodeActiveValue=$("#billCodesActiveTrue").is(':checked');
					     var billCode1 = $("#billCode1").val();
					     var customValue1=$("#customValue1").val();
					     var billCode2 = $("#billCode2").val();
					     var customValue2=$("#customValue2").val();
					     var billCode3 = $("#billCode3").val();
					     var customValue3=$("#customValue3").val();
					     var billCode4 = $("#billCode4").val();
					     var customValue4=$("#customValue4").val();
					     var billCode5 = $("#billCode5").val();
					     var customValue5=$("#customValue5").val();
					     var billCode6 = $("#billCode6").val();
					     var customValue6=$("#customValue6").val();
					     var billCode7 = $("#billCode7").val();
					     var customValue7=$("#customValue7").val();
					     var billCode8 = $("#billCode8").val();
					     var customValue8=$("#customValue8").val();
					     var billCode9 = $("#billCode9").val();
					     var customValue9=$("#customValue9").val();
					     var billCode10 = $("#billCode10").val();
					     var customValue10=$("#customValue10").val();

					     var billCodeMissing = false;
					     var missginBillCodesCount = 0;

					     if (billCodeActiveValue == 'true' || billCodeActiveValue == true)
					 	 {
					    	if( billCode1 == "")
					     	{
					     		missginBillCodesCount++;
					     	}
					     	else if ((billCode1 == 'customValue') && (customValue1 == ''))
					 		{
					 			billCodeMissing = true;
					 		}

					    	if( billCode2 == "")
					     	{
					     		missginBillCodesCount++;
					     	}
					     	else if ((billCode2 == 'customValue') && (customValue2 == ''))
					 		{
					 			billCodeMissing = true;
					 		}

					    	if( billCode3 == "")
					     	{
					     		missginBillCodesCount++;
					     	}
					     	else if ((billCode3 == 'customValue') && (customValue3 == ''))
					 		{
					 			billCodeMissing = true;
					 		}

					    	if( billCode4 == "")
					     	{
					     		missginBillCodesCount++;
					     	}
					     	else if ((billCode4 == 'customValue') && (customValue4 == ''))
					 		{
					 			billCodeMissing = true;
					 		}

					    	if( billCode5 == "")
					     	{
					     		missginBillCodesCount++;
					     	}
					     	else if ((billCode5 == 'customValue') && (customValue5 == ''))
					 		{
					 			billCodeMissing = true;
					 		}

					    	if( billCode6 == "")
					     	{
					     		missginBillCodesCount++;
					     	}
					     	else if ((billCode6 == 'customValue') && (customValue6 == ''))
					 		{
					 			billCodeMissing = true;
					 		}

					    	if( billCode7 == "")
					     	{
					     		missginBillCodesCount++;
					     	}
					     	else if ((billCode7 == 'customValue') && (customValue7 == ''))
					 		{
					 			billCodeMissing = true;
					 		}

					    	if( billCode8 == "")
					     	{
					     		missginBillCodesCount++;
					     	}
					     	else if ((billCode8 == 'customValue') && (customValue8 == ''))
					 		{
					 			billCodeMissing = true;
					 		}

					    	if( billCode9 == "")
					     	{
					     		missginBillCodesCount++;
					     	}
					     	else if ((billCode9 == 'customValue') && (customValue9 == ''))
					 		{
					 			billCodeMissing = true;
					 		}

					    	if( billCode10 == "")
					     	{
					     		missginBillCodesCount++;
					     	}
					     	else if ((billCode10 == 'customValue') && (customValue10 == ''))
					 		{
					 			billCodeMissing = true;
					 		}

					 		if( missginBillCodesCount == 10)
					 		{
					 			errorMessage='<cms:contentText key="NO_BILL_CODES" code="promotion.bill.code" />';
					    		$("#errorShowDiv").html(errorMessage);
					    		$("#errorShowDiv").show();
					    		return false;
					 		}
					 		else if( billCodeMissing == true )
					    	{
					        	errorMessage='<cms:contentText key="CUSTOM_BILL_CODE" code="promotion.bill.code" />';
					    		$("#errorShowDiv").html(errorMessage);
					    		$("#errorShowDiv").show();
					    		return false;
					        }
					 	}

						if(selectedBadgeType=='progress')
						{
							//validate all the fields for progress

							$("input[name=progressStringRow]:checked").each(function(i)
							{
							      val[i] = $(this).val();
							     // alert('values:'+val[i]);
							      individualValueArray = val[i].split(SPLIT_TOKEN);
							      if(individualValueArray[1]=='0')
							      {
							    	  errorMessage='<cms:contentText key="VALID_BADGE_LEVEL" code="gamification.validation.messages" />';
									  $("#errorShowDiv").html(errorMessage);
									  $("#errorShowDiv").show();
									  return false;
							    	 // alert('Badge Level is required in row '+i);
							      }
							      else if(!intRegex.test(trim(individualValueArray[1])))
							  	  {
							    	     errorMessage='<cms:contentText key="VALID_NUMBER_BADGE_LEVEL" code="gamification.validation.messages" />';
								  		 $("#errorShowDiv").html(errorMessage);
								  		 $("#errorShowDiv").show();
								  		 return false;
								  }

							      else if(individualValueArray[3]=='0')
							      {
							    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
									  $("#errorShowDiv").html(errorMessage);
									  $("#errorShowDiv").show();
									  return false;
							      }
							      else if(individualValueArray[4] != 'null' && individualValueArray[4]<=0)
								  {
									   errorMessage='Badge points should be greater than zero';
									   $("#errorShowDiv").html(errorMessage);
									   $("#errorShowDiv").show();
									   return false;
								  }
							      else if(individualValueArray[4] != 'null' && !intRegex.test(trim(individualValueArray[4])))
								  {
									   errorMessage='please enter valid number for all badge points';
									   $("#errorShowDiv").html(errorMessage);
									   $("#errorShowDiv").show();
									   return false;
								  }
							      else if(individualValueArray[6].length>200)
							      {
							    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
									  $("#errorShowDiv").html(errorMessage);
									  $("#errorShowDiv").show();
									  return false;
							      }
							      badgeNames[totRows]=individualValueArray[3];
							      totRows++;


							 });

							for(var index=0;index<val.length;index++)
							{
								 var currentRow = val[index].split(SPLIT_TOKEN);
								 var currentBadgeLevel=currentRow[1];
								 for(var nextIndex=index+1;nextIndex<val.length;nextIndex++)
								 {
									 var nextRow=val[nextIndex].split(SPLIT_TOKEN);
									 var nextBadgeLevel=nextRow[1];
									 if(parseInt(nextBadgeLevel)<=parseInt(currentBadgeLevel) && nextBadgeLevel!='0')
								      {
										 	  errorMessage='<cms:contentText key="BADGE_LEVEL_NEXTROW" code="gamification.validation.messages" />';
											  $("#errorShowDiv").html(errorMessage);
											  $("#errorShowDiv").show();
											  return false;
								      }
								 }

							 }


						}
						else if(selectedBadgeType=='fileload')
						{
							$("input[name=fileLoadStringRow]:checked").each(function(j)
							{
							      val[j] = $(this).val();
							      individualValueArray = val[j].split(SPLIT_TOKEN);

							      if( selectedPromotion==-1 || promotionType == 'Y')
							      {

							      if(individualValueArray[2]=='0')
							      {
							    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
									  $("#errorShowDiv").html(errorMessage);
									  $("#errorShowDiv").show();
									  return false;
							      }
							      else if(individualValueArray[3] != 'null' && individualValueArray[3]<=0)
								  {
									   errorMessage='Badge points should be greater than zero';
									   $("#errorShowDiv").html(errorMessage);
									   $("#errorShowDiv").show();
									   return false;
								  }
							      else if(individualValueArray[3] != 'null' && !intRegex.test(trim(individualValueArray[3])))
								  {
									   errorMessage='please enter valid number for all badge points';
									   $("#errorShowDiv").html(errorMessage);
									   $("#errorShowDiv").show();
									   return false;
								  }
							      else if(individualValueArray[5].length>200)
							      {
							    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
									  $("#errorShowDiv").html(errorMessage);
									  $("#errorShowDiv").show();
									  return false;
							      }
						      }
						      else
						      {
						    	  if(individualValueArray[2]=='0')
							      {
							    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
									  $("#errorShowDiv").html(errorMessage);
									  $("#errorShowDiv").show();
									  return false;
							      }
						    	  else if(individualValueArray[3].length>200)
							      {
							    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
									  $("#errorShowDiv").html(errorMessage);
									  $("#errorShowDiv").show();
									  return false;
							      }
						      }
							      badgeNames[totRows]=individualValueArray[2];
							      totRows++;


							 });

						}
						else if(selectedBadgeType=='behavior')
						{
							$("input[name=behaviorStringRow]:checked").each(function(k)
							{
							      val[k] = $(this).val();
							      individualValueArray = val[k].split(SPLIT_TOKEN);
							      if(individualValueArray[4]=='0')
							      {
							    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
									  $("#errorShowDiv").html(errorMessage);
									  $("#errorShowDiv").show();
									  return false;
							      }
							      else if(individualValueArray[5] != 'null' && individualValueArray[5]<=0)
								  {
									   errorMessage='Badge points should be greater than zero';
									   $("#errorShowDiv").html(errorMessage);
									   $("#errorShowDiv").show();
									   return false;
								  }
							      else if(individualValueArray[5] != 'null' && !intRegex.test(trim(individualValueArray[5])))
								  {
									   errorMessage='please enter valid number for all badge points';
									   $("#errorShowDiv").html(errorMessage);
									   $("#errorShowDiv").show();
									   return false;
								  }
							      else if(individualValueArray[7].length>200)
							      {
							    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
									  $("#errorShowDiv").html(errorMessage);
									  $("#errorShowDiv").show();
									  return false;
							      }
							      badgeNames[totRows]=individualValueArray[4];
							      totRows++;
							 });

							var enableAllBehaviorPoints =$("#includeAllBehaviorPoints").is(':checked');
							var allBehaviorPoints=$("#allBehaviorPoints").val();
							if(enableAllBehaviorPoints=='true' || enableAllBehaviorPoints ==true)
							{
								if(allBehaviorPoints=='')
								{
									  errorMessage='Please enter value for all behavior points';
									  $("#errorShowDiv").html(errorMessage);
									  $("#errorShowDiv").show();
									  return false;
								}
								if(allBehaviorPoints<=0)
								{
									  errorMessage='All behavior points should be greater than zero';
									  $("#errorShowDiv").html(errorMessage);
									  $("#errorShowDiv").show();
									  return false;
								}
								if(!intRegex.test(trim(allBehaviorPoints)))
								{
									  errorMessage='please enter valid number for all behavior points';
									  $("#errorShowDiv").html(errorMessage);
									  $("#errorShowDiv").show();
									  return false;
								}
							}

						}
						else if(selectedBadgeType=='earned')
						{
							var pointRange="<%=isPointRange%>";
							var isGoalQuest="<%=isGoalQuest%>";
							var isThrowDown="<%=isThrowDown%>";
							var hasPartners="<%=hasPartners%>";
							var hasStackStandingPayouts="<%=hasStackStandingPayouts%>";
							if(pointRange!=null&& pointRange=='N'&& isGoalQuest=='N'&& isThrowDown=='N')
					    	{
									$("input[name=levelStringRow]:checked").each(function(m)
									{
									      val[m] = $(this).val();
									     individualValueArray = val[m].split(SPLIT_TOKEN);
									      if(individualValueArray[4]=='0')
									      {
									    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
											  $("#errorShowDiv").html(errorMessage);
											  $("#errorShowDiv").show();
											  return false;
									      }
									      else if(individualValueArray[5].length>200)
									      {
									    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
											  $("#errorShowDiv").html(errorMessage);
											  $("#errorShowDiv").show();
											  return false;
									      }
									      badgeNames[totRows]=individualValueArray[4];
									      totRows++;
									 });
							}
							else if(pointRange!=null&& pointRange=='N'&& isGoalQuest=='Y')
					    	{
								//TODO - Field positions vary from achiever to partner
								/*	$("input[name=levelStringRow]:checked").each(function(m)
									{
									      val[m] = $(this).val();
									     individualValueArray = val[m].split(SPLIT_TOKEN);
									     alert(individualValueArray);
									      if(individualValueArray[4]=='0')
									      {
									    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
											  $("#errorShowDiv").html(errorMessage);
											  $("#errorShowDiv").show();
											  return false;
									      }
									      else if(individualValueArray[5].length>200)
									      {
									    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
											  $("#errorShowDiv").html(errorMessage);
											  $("#errorShowDiv").show();
											  return false;
									      }
									      badgeNames[totRows]=individualValueArray[4];
									      totRows++;
									 });*/
							}
							else if(pointRange=='N'&& isThrowDown=='Y')
					    	{
								if(document.getElementById("totalTDStackLevelsLength")!=null && document.getElementById("totalTDStackLevelsLength")!='undefined')
								{
								var totalStackLevels=document.getElementById("totalTDStackLevelsLength").value;
								 for(var i=0;i<totalStackLevels;i++)
								    {
						    		  if($("#stackLevelsbadgeNameRow"+i).val()=='0')
								      {
								    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
										  $("#errorShowDiv").html(errorMessage);
										  $("#errorShowDiv").show();
										  return false;
								      }
								      else if($("#stackLevelsbadgeDescRow"+i).val().length>200)
								      {
								    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
										  $("#errorShowDiv").html(errorMessage);
										  $("#errorShowDiv").show();
										  return false;
								      }
						    		  badgeNames[totRows]=$("#stackLevelsbadgeNameRow"+i).val();
								      totRows++;

									}
								}
								 //overall
								 if(document.getElementById("totalTDOverallLevelsLength")!=null && document.getElementById("totalTDOverallLevelsLength")!='undefined')
				                {
								 var totalOverallLevels=document.getElementById("totalTDOverallLevelsLength").value;
								  for(var i=0;i<totalOverallLevels;i++)
								    {
						    		  if($("#overallLevelsBadgeNameRow"+i).val()=='0')
								      {
								    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
										  $("#errorShowDiv").html(errorMessage);
										  $("#errorShowDiv").show();
										  return false;
								      }
								      else if($("#overallLevelsBadgeDescRow"+i).val().length>200)
								      {
								    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
										  $("#errorShowDiv").html(errorMessage);
										  $("#errorShowDiv").show();
										  return false;
								      }
						    		  badgeNames[totRows]=$("#overallLevelsBadgeNameRow"+i).val();
								      totRows++;

									}
				                }
								//var undefeated = document.getElementById("undefeatedBadgeRuleId").value;
								var undefeated = document.getElementById("undefeatedTdBadgeRuleListSize").value;

				                if(parseInt(undefeated)>0){
									    //alert("undefeated validation");
										var undefeatedBadgeName = $("#undefeatedBadgeNameRow").val();
										var undefeatedBadgeDescription = $("#undefeatedBadgeDescRow").val();
										if(undefeatedBadgeName=='0')
										{
										    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
												  $("#errorShowDiv").html(errorMessage);
												  $("#errorShowDiv").show();
												  return false;
										 }

										 else if(undefeatedBadgeDescription.length>200)
										 {
										    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
												  $("#errorShowDiv").html(errorMessage);
												  $("#errorShowDiv").show();
												  return false;
										 }
										      badgeNames[totRows]=undefeatedBadgeName;
										      //alert(badgeNames[totRows]);
										      totRows++;

								}
								//alert("error msg "+errorMessage);
								//alert("ending validation");
							}
							else
							{
								$("input[name=pointRangeStringRow]:checked").each(function(n)
								{
								      val[n] = $(this).val();
								      individualValueArray = val[n].split(SPLIT_TOKEN);

								      //if(individualValueArray[1]=='0')
								    	  //{
								    	  //  errorMessage='<cms:contentText key="POINT_RANGE_MIN_REQUIRED" code="gamification.validation.messages" />';
								    	  //$("#errorShowDiv").html(errorMessage);
										  //$("#errorShowDiv").show();
										  //return false;
										  //  }
								    if(!intRegex.test(trim(individualValueArray[1])))
								  	  {
								    	  	 errorMessage='<cms:contentText key="POINT_RANGE_MIN_NUMBER" code="gamification.validation.messages" />';
									  		 $("#errorShowDiv").html(errorMessage);
									  		 $("#errorShowDiv").show();
									  		 return false;
									  }
								      else if(individualValueArray[1]!='0' && individualValueArray[2]=='0')
								      {
								    	  errorMessage='<cms:contentText key="POINT_RANGE_MAX_REQUIRED" code="gamification.validation.messages" />';
										  $("#errorShowDiv").html(errorMessage);
										  $("#errorShowDiv").show();
										  return false;
								      }
								      else if(!intRegex.test(trim(individualValueArray[2])))
								  	  {
								    	 errorMessage='<cms:contentText key="POINT_RANGE_MAX_NUMBER" code="gamification.validation.messages" />';
								  		 $("#errorShowDiv").html(errorMessage);
								  		 $("#errorShowDiv").show();
								  		 return false;
								  	  }
								      else if(parseInt(individualValueArray[2])<parseInt(individualValueArray[1]))
								      {
								    	  errorMessage='<cms:contentText key="POINT_MAXMIN_SAMEROW" code="gamification.validation.messages" />';
									  	  $("#errorShowDiv").html(errorMessage);
									  	  $("#errorShowDiv").show();
									  	  return false;

								      }
								      else if(individualValueArray[4]=='0')
								      {
								    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
										  $("#errorShowDiv").html(errorMessage);
										  $("#errorShowDiv").show();
										  return false;
								      }
								      else if(individualValueArray[5].length>200)
								      {
								    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
										  $("#errorShowDiv").html(errorMessage);
										  $("#errorShowDiv").show();
										  return false;
								      }
								      badgeNames[totRows]=individualValueArray[4];
								      totRows++;



								 });

								for(var index=0;index<val.length;index++)
								 {
									 var currentRow = val[index].split(SPLIT_TOKEN);
									 var currentPointMax=parseInt(currentRow[2]);
									 for(var nextIndex=index+1;nextIndex<val.length;nextIndex++)
									 {
										 var nextRow=val[nextIndex].split(SPLIT_TOKEN);
										 var nextPointMin=parseInt(nextRow[1]);
										 if(nextPointMin!=parseInt(currentPointMax+1) && nextPointMin!='0')
									      {
											 	  errorMessage='<cms:contentText key="POINT_MAXMIN_NEXTROW" code="gamification.validation.messages" />';
												  $("#errorShowDiv").html(errorMessage);
												  $("#errorShowDiv").show();
												  return false;
									      }
										 currentPointMax=parseInt(nextRow[2]);
									 }

								 }

							}



						}

		                var result = [];
					  	$.each(badgeNames, function(i, e) {
					    	if ($.inArray(e, result) == -1)
					    	{
					    		result.push(e);
					    	}
					    	else
					    	{
					    		 errorMessage='<cms:contentText key="DUPLICATE_BADGE_NAME" code="gamification.validation.messages" />';
								  $("#errorShowDiv").html(errorMessage);
								  $("#errorShowDiv").show();
								  return false;
					    	}
					  	});

						if(errorMessage=='')
						{
							 $("#errorShowDiv").hide();
						  	setDispatchAndSubmit(method);
						}
						else
							return false;
				}
			}
		});


}


function addNewRow(type)
{
	var newIndex=0;
	var selectedPromotion = $("#selectedPromotionIds").val();
	if(type=='fileload')
	{
		var totalFileLoadRowsValue=document.getElementById("currentFileLoadTableSize").value;
		newIndex=parseInt(totalFileLoadRowsValue)+1;
		document.getElementById("currentFileLoadTableSize").value=newIndex;
		$.ajax({
			url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=populateBadgeLibraryAddRow&badgeType="+type+"&promotionIds="+selectedPromotion,
			dataType: 'application/json',
			success: function(data){
				if(data != null && data != '' && data != 'null')
				{
					var objectBadge = jQuery.parseJSON(data);
					var objectBadgeLib = objectBadge.badgeLibraryList;
					var htmlfileLoad='<table width="100%" cellspacing="0" cellpadding="10" border="0">';
					htmlfileLoad+='<tr class="crud-table-row2">';
					htmlfileLoad+='<td class="crud-content left-align top-align nowrap"><select name="badgeLibraryId" id="fileLoadBadgeLibraryId'+totalFileLoadRowsValue+'" class="content-field">';

					for(var bLibIndex=0, lenBLib=objectBadgeLib.length;bLibIndex < lenBLib; bLibIndex++)
			     	{
						htmlfileLoad+='<option value="'+objectBadgeLib[bLibIndex].id+'">'+objectBadgeLib[bLibIndex].name+'</option>';
			     	}
					htmlfileLoad+='</select></td>';
					htmlfileLoad+='<td  class="crud-content left-align top-align nowrap"><input type="text" name="badgeName" id="fileLoadBadgeNameRow'+totalFileLoadRowsValue+'" size="40" maxlength="40" class="content-field" /></td>';
					if( selectedPromotion=='-1' || objectBadge.showFileLoadNoPromoDiv == 'Y')
					{
						htmlfileLoad+='<td  class="crud-content left-align top-align nowrap"><input type="text" name="badgePoints" id="fileLoadBadgePointsRow'+totalFileLoadRowsValue+'" size="10" maxlength="5" class="content-field" /></td>';
						htmlfileLoad+='<td  class="crud-content left-align top-align nowrap"><input type="checkbox" name="eligibleForSweepstake" id="fileLoadBadgeSweepRow'+totalFileLoadRowsValue+'" class="content-field"/></td>';
					}
					htmlfileLoad+='<td  class="crud-content left-align top-align nowrap"><textarea name="badgeDescription" id="fileLoadBadgeDescRow'+totalFileLoadRowsValue+'" class="content-field" /></td>';
					htmlfileLoad+='<td id="fileLoadcheckBoxDiv'+totalFileLoadRowsValue+'"><input type="checkbox" name="fileLoadStringRow" id="fileLoadStringRow'+totalFileLoadRowsValue+'" value="0" /></td>';
					htmlfileLoad+='<td><input type="hidden" name="badgeRuleId" id="fileLoadBadgeRuleId'+totalFileLoadRowsValue+'" value="0" /></td>';
					htmlfileLoad+='</tr></table>';

					$("#fileLoadBadgeDiv").append(htmlfileLoad);
					for(var fileIndex=0;fileIndex<newIndex;fileIndex++)
					{
						$("#fileLoadcheckBoxDiv"+fileIndex).hide();
					}

				}
			}
		});

	}
	else if(type=='progress')
	{
		var totalProgressRowsValue=document.getElementById("currentProgressTableSize").value;
		newIndex=parseInt(totalProgressRowsValue)+1;
		document.getElementById("currentProgressTableSize").value=newIndex;
		$.ajax({
			url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=populateBadgeLibraryAddRow&badgeType="+type+"&promotionIds="+selectedPromotion,
			dataType: 'application/json',
			success: function(data){
				if(data != null && data != '' && data != 'null')
				{
					var objectBadge = jQuery.parseJSON(data);
					var objectBadgeLib = objectBadge.badgeLibraryList;
					var htmlProgress='<table>';
					htmlProgress+='<tr class="crud-table-row2">';
					htmlProgress+='<td  class="crud-content left-align top-align nowrap"><input type="text" name="maxQualifier" id="progressMaxQualifier'+totalProgressRowsValue+'" size="12" maxlength="12" class="content-field" /></td>';
					htmlProgress+='<td class="crud-content left-align top-align nowrap"><select name="badgeLibraryId" id="progressBadgeLibraryId'+totalProgressRowsValue+'" class="content-field">';
					for(var bLibIndex=0, lenBLib=objectBadgeLib.length;bLibIndex < lenBLib; bLibIndex++)
			     	{
						htmlProgress+='<option value="'+objectBadgeLib[bLibIndex].id+'">'+objectBadgeLib[bLibIndex].name+'</option>';
			     	}
					htmlProgress+='</select></td>';
					htmlProgress+='<td  class="crud-content left-align top-align nowrap"><input type="text" name="badgeName" id="progressBadgeNameRow'+totalProgressRowsValue+'" size="40" maxlength="40" class="content-field" /></td>';
					htmlProgress+='<td  class="crud-content left-align top-align nowrap"><input type="text" name="badgePoints" id="progressBadgePointsRow'+totalProgressRowsValue+'" size="10" maxlength="5" class="content-field" /></td>';
					htmlProgress+='<td  class="crud-content left-align top-align nowrap"><input type="checkbox" name="eligibleForSweepstake" id="progressBadgeSweepRow'+totalProgressRowsValue+'" class="content-field"/></td>';
					htmlProgress+='<td  class="crud-content left-align top-align nowrap"><textarea name="badgeDescription" id="progressBadgeDescRow'+totalProgressRowsValue+'" class="content-field" /></td>';
					htmlProgress+='<td id="progresscheckBoxDiv'+totalProgressRowsValue+'"><input type="checkbox" name="progressStringRow" id="progressStringRow'+totalProgressRowsValue+'" value="0" /></td>';
					htmlProgress+='<td><input type="hidden" name="badgeRuleId" id="progressBadgeRuleId'+totalProgressRowsValue+'" value="0" /></td>';
					htmlProgress+='</tr></table>';
					 $("#progressBadgeDiv").append(htmlProgress);
					 for(var progIndex=0;progIndex<newIndex;progIndex++)
					 {
							  	$("#progressStringRow"+progIndex).hide();
					 }
				}
			}
		});
	}
	else if(type=='pointRange')
	{
		var totalPointRangeRowsValue=document.getElementById("currentPointRangeTableSize").value;
		newIndex=parseInt(totalPointRangeRowsValue)+1;
		document.getElementById("currentPointRangeTableSize").value=newIndex;
		$.ajax({
			url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=populateBadgeLibraryAddRow&badgeType="+type+"&promotionIds="+selectedPromotion,
			dataType: 'application/json',
			success: function(data){
				if(data != null && data != '' && data != 'null')
				{
					var objectBadge = jQuery.parseJSON(data);
					var objectBadgeLib = objectBadge.badgeLibraryList;
					var htmlPointRange='<table>';
					htmlPointRange+='<tr class="crud-table-row2">';
					htmlPointRange+='<td  class="crud-content left-align top-align nowrap"><input type="text" name="rangeAmountMin" id="rangeAmountMin'+totalPointRangeRowsValue+'" size="5" maxlength="12" class="content-field" />&nbsp;To &nbsp;';
					htmlPointRange+='<input type="text" name="rangeAmountMax" id="rangeAmountMax'+totalPointRangeRowsValue+'" size="5" maxlength="12" class="content-field" /></td>';
					htmlPointRange+='<td class="crud-content left-align top-align nowrap"><select name="badgeLibraryId" id="pointBadgeLibraryId'+totalPointRangeRowsValue+'" class="content-field">';
					for(var bLibIndex=0, lenBLib=objectBadgeLib.length;bLibIndex < lenBLib; bLibIndex++)
			     	{
						htmlPointRange+='<option value="'+objectBadgeLib[bLibIndex].id+'">'+objectBadgeLib[bLibIndex].name+'</option>';
			     	}
					htmlPointRange+='</select></td>';
					htmlPointRange+='<td  class="crud-content left-align top-align nowrap"><input type="text" name="badgeName" id="pointBadgeNameRow'+totalPointRangeRowsValue+'" size="40" maxlength="40" class="content-field" /></td>';
					htmlPointRange+='<td  class="crud-content left-align top-align nowrap"><textarea name="badgeDescription" id="pointBadgeDescRow'+totalPointRangeRowsValue+'" class="content-field" /></td>';
					htmlPointRange+='<td id="pointcheckBoxDiv'+totalPointRangeRowsValue+'"><input type="checkbox" name="pointRangeStringRow" id="pointRangeStringRow'+totalPointRangeRowsValue+'" value="0" /></td>';
					htmlPointRange+='<td><input type="hidden" name="badgeRuleId" id="pointBadgeRuleId'+totalPointRangeRowsValue+'" value="0" /></td>';
					htmlPointRange+='</tr></table>';
					 $("#earnedNotEarnedBadgePointDiv").append(htmlPointRange);
					 for(var pointIndex=0;pointIndex<newIndex;pointIndex++)
					 {
							  	$("#pointRangeStringRow"+pointIndex).hide();
					 }
				}
			}
		});
	}

	for(var generalIndex=0;generalIndex<newIndex;generalIndex++)
	{
			  	$("#pointRangeStringRow"+generalIndex).hide();
			  	$("#fileLoadStringRow"+generalIndex).hide();
			  	$("#progressStringRow"+generalIndex).hide();

	}
}

function setAllValues(method, promotionType)
{
	var selectedBadgeType ="<%=badgeType%>";
	var selectedPromotion = $("#selectedPromotionIds").val();
	var commaIndex=0;
	if(selectedBadgeType=='behavior')
	{
			var behaviorName;
			var badgeLibSelected;
			var badgeName;
			var badgePoints;
			var badgeSweep;
			var badgeDesc;
			var checkBoxBehaviorRow='';
			var badgeRuleId;
			var behaviorCode;
			var totalbehaviors=document.getElementById("badgeRuleListSize").value;
			for(var beh=0;beh<totalbehaviors;beh++)
			{
				commaIndex=0;
				badgeRuleId=$("#behaviorBadgeRuleId"+beh).val();
				behaviorName=$("#behaviorCode"+beh).val();
				badgeLibSelected=$("#badgeLibraryIdRow"+beh).val();
				if( badgeLibSelected == null || badgeLibSelected == '' || badgeLibSelected == 'null' )
			    {
					badgeLibSelected=$("#badgeLibraryIdRowNew"+beh).val();
				}
				badgeName=$("#behaviorbadgeNameRow"+beh).val();
				badgePoints=$("#behaviorbadgePointsRow"+beh).val();
				badgeSweep=$("#behaviorBadgeSweepRow"+beh).is(':checked');
				behaviorCode=$("#behaviorCode"+beh).val();
				if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
					badgeRuleId='0';
				if(badgeName==null|| badgeName=='' || badgeName=='null' )
					badgeName='0';
				if(badgePoints==null|| badgePoints=='' || badgePoints=='null' )
					badgePoints='null';
				//commaIndex=badgeName.indexOf(",");
				badgeName=trim(badgeName);
				for(var i=0;i<badgeName.length;i++)
				{
					if(badgeName.charAt(i)==SPLIT_TOKEN)
					{
						commaIndex++;
						break;
					}
				}
                badgeLibSelected = badgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
				if(commaIndex>0)
				{
					badgeName=badgeName.replace(/\,/g,REPLACEMENT_TOKEN);
				}
				badgeDesc=$("#behaviorbadgeDescRow"+beh).val();
				if(badgeName==null|| badgeName=='' || badgeName=='null' )
					badgeName='0';
				if(badgeDesc==null|| badgeDesc=='' || badgeDesc=='null' )
					badgeDesc='0';
				badgeDesc=badgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);
				checkBoxBehaviorRow=badgeRuleId+SPLIT_TOKEN+behaviorCode+SPLIT_TOKEN+behaviorCode+SPLIT_TOKEN+badgeLibSelected+SPLIT_TOKEN+badgeName+SPLIT_TOKEN+badgePoints+SPLIT_TOKEN+badgeSweep+SPLIT_TOKEN+badgeDesc;
				//alert('checkbox row valuegoing to set as :'+checkBoxBehaviorRow);
				if( document.getElementById("behaviorStringRow"+beh) != null )
			    {
				document.getElementById("behaviorStringRow"+beh).value=checkBoxBehaviorRow;
				if(badgeLibSelected!='-1')
					document.getElementById("behaviorStringRow"+beh).checked=true;
				else
					document.getElementById("behaviorStringRow"+beh).checked=false;
			    }
			}
	}
	else if(selectedBadgeType=='earned')
	{
					//alert('inside earned');
			var levelName='';
			var levelId;
			var countryId;
			var badgeLibSelected;
			var levelBadgeName;
			var levelBadgeDesc;
			var checkBoxLevelRow='';
			var pointMinRange;
			var pointMaxRange;
			var pointBadgeLibSelected;
			var pointBadgeName;
			var pointBadgeDesc;
			var pointcheckBoxRow='';
			var pointRange="<%=isPointRange%>";
			var isGoalQuest="<%=isGoalQuest%>";
			var isThrowDown="<%=isThrowDown%>";
			var hasPartners="<%=hasPartners%>";
			var hasStackStandingPayouts="<%=hasStackStandingPayouts%>";
			var badgeRuleId;
			if(pointRange!=null&& pointRange=='N'&&isGoalQuest=='N'&&isThrowDown=='N')
    		{
					var totalLevels=document.getElementById("badgeRuleListSize").value;
					for(var lev=0;lev<totalLevels;lev++)
					{
							commaIndex=0;
							badgeRuleId=$("#levelsBadgeRuleId"+lev).val();
							countryId=$("#countryRow"+lev).val();
							levelName=$("#levelsNameRow"+lev).val();
									//levelId=$("#levelsIdRow"+lev).val();
							badgeLibSelected=$("#levelsBadgeLibraryIdRow"+lev).val();
							levelBadgeName=$("#levelsbadgeNameRow"+lev).val();
							levelBadgeDesc=$("#levelsbadgeDescRow"+lev).val();
							if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
								badgeRuleId='0';
							if(levelBadgeName==null|| levelBadgeName=='' || levelBadgeName=='null' )
								levelBadgeName='0';
							if(levelBadgeDesc==null|| levelBadgeDesc=='' || levelBadgeDesc=='null' )
							{
									levelBadgeDesc='0';
							}
							//commaIndex=levelBadgeName.indexOf(",");
							levelBadgeName=trim(levelBadgeName);
							for(var i=0;i<levelBadgeName.length;i++)
							{
								if(levelBadgeName.charAt(i)==SPLIT_TOKEN)
								{
									commaIndex++;
									break;
								}
							}
                            badgeLibSelected = badgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
							if(commaIndex>0)
							{
								levelBadgeName=levelBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
							}
							levelBadgeDesc=levelBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);
							checkBoxLevelRow=badgeRuleId+SPLIT_TOKEN+countryId+SPLIT_TOKEN+levelName+SPLIT_TOKEN+badgeLibSelected+SPLIT_TOKEN+levelBadgeName+SPLIT_TOKEN+levelBadgeDesc;
									//alert('checkbox row valuegoing to set as :'+checkBoxLevelRow);
							document.getElementById("levelStringRow"+lev).value=checkBoxLevelRow;
							if(badgeLibSelected!='-1')
								document.getElementById("levelStringRow"+lev).checked=true;
							else
								document.getElementById("levelStringRow"+lev).checked=false;

					}
			 }
			else if(pointRange!=null&& pointRange=='N'&&isGoalQuest=='Y')
			{
				var totalLevels=document.getElementById("badgeRuleListSize").value;
				for(var lev=0;lev<totalLevels;lev++)
				{
						commaIndex=0;
						badgeRuleId=$("#goalLevelsBadgeRuleId"+lev).val();
						countryId=$("#countryRow"+lev).val();
						levelName=$("#goalLevelsNameRow"+lev).val();
								//levelId=$("#levelsIdRow"+lev).val();
						badgeLibSelected=$("#goalLevelsBadgeLibraryIdRow"+lev).val();
						levelBadgeName=$("#goalLevelsbadgeNameRow"+lev).val();
						levelBadgeDesc=$("#goalLevelsbadgeDescRow"+lev).val();
						if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
							badgeRuleId='0';
						if(countryId==null|| countryId=='' || countryId=='null' )
							countryId='0';
						if(levelBadgeName==null|| levelBadgeName=='' || levelBadgeName=='null' )
							levelBadgeName='0';
						if(levelBadgeDesc==null|| levelBadgeDesc=='' || levelBadgeDesc=='null' )
							levelBadgeDesc='0';

						//commaIndex=levelBadgeName.indexOf(",");
						levelBadgeName=trim(levelBadgeName);
						for(var i=0;i<levelBadgeName.length;i++)
						{
							if(levelBadgeName.charAt(i)==SPLIT_TOKEN)
							{
								commaIndex++;
								break;
							}
						}
						if(badgeLibSelected != null && badgeLibSelected != 'undefined'){
							badgeLibSelected = badgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
						}

						if(commaIndex>0)
						{
							levelBadgeName=levelBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
						}
						levelBadgeDesc=levelBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);

						partnerBadgeLibSelected=$("#levelsPartnerBadgeLibraryIdRow"+lev).val();
						partnerLevelBadgeName=$("#levelsPartnerBadgeNameRow"+lev).val();
						partnerLevelBadgeDesc=$("#levelsPartnerBadgeDescRow"+lev).val();
						if(partnerLevelBadgeName==null|| partnerLevelBadgeName=='' || partnerLevelBadgeName=='null' )
							partnerLevelBadgeName='0';
						if(partnerLevelBadgeDesc==null|| partnerLevelBadgeDesc=='' || partnerLevelBadgeDesc=='null' )
						{
							partnerLevelBadgeDesc='0';
						}
						//commaIndex=partnerLevelBadgeName.indexOf(",");
						partnerLevelBadgeName=trim(partnerLevelBadgeName);
						for(var i=0;i<partnerLevelBadgeName.length;i++)
						{
							if(partnerLevelBadgeName.charAt(i)==SPLIT_TOKEN)
							{
								commaIndex++;
								break;
							}
						}

						if(partnerBadgeLibSelected != null && partnerBadgeLibSelected != 'undefined'){
							partnerBadgeLibSelected = partnerBadgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
						}

						if(commaIndex>0)
						{
							partnerLevelBadgeName=partnerLevelBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
						}

						partnerLevelBadgeDesc=partnerLevelBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);
						checkBoxLevelRow=badgeRuleId+SPLIT_TOKEN+countryId+SPLIT_TOKEN+levelName+SPLIT_TOKEN+badgeLibSelected+SPLIT_TOKEN+levelBadgeName+SPLIT_TOKEN+levelBadgeDesc
										+SPLIT_TOKEN+partnerBadgeLibSelected+SPLIT_TOKEN+partnerLevelBadgeName+SPLIT_TOKEN+partnerLevelBadgeDesc;
								//alert('checkbox row valuegoing to set as :'+checkBoxLevelRow);
						if(hasPartners=='N'){
							document.getElementById("levelStringRow"+lev).value=checkBoxLevelRow;

							if(badgeLibSelected!='-1')
								document.getElementById("levelStringRow"+lev).checked=true;
							else
								document.getElementById("levelStringRow"+lev).checked=false;
						}
						else{
							if(lev==0 || lev%2==0) {
							combinedBadgeLevelRow = badgeRuleId+SPLIT_TOKEN+partnerLevelBadgeName+SPLIT_TOKEN+partnerLevelBadgeDesc;
							}
							else if(lev==1 || lev%2!=0) {
								combinedBadgeLevelRow = badgeRuleId+SPLIT_TOKEN+levelBadgeName+SPLIT_TOKEN+levelBadgeDesc+SPLIT_TOKEN+combinedBadgeLevelRow;
								document.getElementById("levelStringRow"+lev).value=combinedBadgeLevelRow;

								if(badgeLibSelected!='-1')
									document.getElementById("levelStringRow"+lev).checked=true;
								else
									document.getElementById("levelStringRow"+lev).checked=false;
							}
						}
				}
			}
			else if(pointRange=='N'&& isThrowDown=='Y')
			{
				//alert("setting the values");
				if(document.getElementById("totalTDStackLevelsLength")!=null && document.getElementById("totalTDStackLevelsLength")!='undefined')
				{
				var totalStackLevels=document.getElementById("totalTDStackLevelsLength").value;

				for(var lev=0;lev<totalStackLevels;lev++)
				{

						commaIndex=0;
						badgeRuleId=$("#stackLevelsBadgeRuleId"+lev).val();
						countryId=$("#countryRow"+lev).val();
						nodeName = $("#stackLevelsNodeNameRow"+lev).val();
						//alert("stack node "+nodeName);
						levelName=$("#stackLevelsNameRow"+lev).val();
						//levelId=$("#levelsIdRow"+lev).val();
                        badgeLibSelected=$("#stackLevelsBadgeLibraryIdRow"+lev).val();
						levelBadgeName=$("#stackLevelsbadgeNameRow"+lev).val();
						levelBadgeDesc=$("#stackLevelsbadgeDescRow"+lev).val();
						if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
							badgeRuleId='0';
						if(countryId==null|| countryId=='' || countryId=='null' )
							countryId='0';
						if(levelBadgeName==null|| levelBadgeName=='' || levelBadgeName=='null' )
							levelBadgeName='0';
						if(levelBadgeDesc==null|| levelBadgeDesc=='' || levelBadgeDesc=='null' )
							levelBadgeDesc='0';

						//commaIndex=levelBadgeName.indexOf(",");
						levelBadgeName=trim(levelBadgeName);
						for(var i=0;i<levelBadgeName.length;i++)
						{
							if(levelBadgeName.charAt(i)==SPLIT_TOKEN)
							{
								commaIndex++;
								break;
							}
						}
                        badgeLibSelected = badgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
						if(commaIndex>0)
						{
							levelBadgeName=levelBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
						}
						levelBadgeDesc=levelBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);

						checkBoxLevelRow=badgeRuleId+SPLIT_TOKEN+countryId+SPLIT_TOKEN+nodeName+SPLIT_TOKEN+levelName+SPLIT_TOKEN+badgeLibSelected+SPLIT_TOKEN+levelBadgeName+SPLIT_TOKEN+levelBadgeDesc;
						//alert(checkBoxLevelRow);
						document.getElementById("stackLevelStringRow"+lev).value=checkBoxLevelRow;
				}

				}

				//Overall Badge Setup
				if(document.getElementById("totalTDOverallLevelsLength")!=null && document.getElementById("totalTDOverallLevelsLength")!='undefined')
				{
				var totalOverallLevels=document.getElementById("totalTDOverallLevelsLength").value;

				for(var lev=0;lev<totalOverallLevels;lev++)
				{

						commaIndex=0;
						var overallBadgeRuleId=$("#overallLevelsBadgeRuleId"+lev).val();
						//alert("obadgeid "+overallBadgeRuleId);
						var overallcountryId=$("#countryRow"+lev).val();
						if(overallcountryId==null || overallcountryId=='undefined')
						{
							overallcountryId='0';
						}
						//alert("obcountry "+overallcountryId);
						var overallnodeName = $("#overallLevelsNodeNameRow"+lev).val();
						//alert("node "+overallnodeName);
						var overalllevelName = $("#overallLevelsNameRow"+lev).val();
						//alert("olevel "+overalllevelName);
						var overallbadgeLibSelected=$("#overallLevelsBadgeLibraryIdRow"+lev).val();
						//alert("olib "+overallbadgeLibSelected);
						var overalllevelBadgeName=$("#overallLevelsBadgeNameRow"+lev).val();
						//alert("obadgename "+overalllevelBadgeName);
						var overalllevelBadgeDesc=$("#overallLevelsBadgeDescRow"+lev).val();
						//alert("obadgedesc "+overalllevelBadgeDesc);
						if( overallBadgeRuleId == null || overallBadgeRuleId == '' || overallBadgeRuleId == 'null' || overallBadgeRuleId == 'undefined')
							overallBadgeRuleId='0';
						if(overalllevelBadgeName==null|| overalllevelBadgeName=='' || overalllevelBadgeName=='null' )
							overalllevelBadgeName='0';
						if(overalllevelBadgeDesc==null|| overalllevelBadgeDesc=='' || overalllevelBadgeDesc=='null' )
						{
							overalllevelBadgeDesc='0';
						}
						//commaIndex=levelBadgeName.indexOf(",");
						overalllevelBadgeName=trim(overalllevelBadgeName);
						for(var i=0;i<overalllevelBadgeName.length;i++)
						{
							if(overalllevelBadgeName.charAt(i)==SPLIT_TOKEN)
							{
								commaIndex++;
								break;
							}
						}
                        overallbadgeLibSelected = overallbadgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
						if(commaIndex>0)
						{
							overalllevelBadgeName=overalllevelBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
						}

						overalllevelBadgeDesc=overalllevelBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);
						var overallcheckBoxLevelRow = overallBadgeRuleId+SPLIT_TOKEN+overallcountryId+SPLIT_TOKEN+overallnodeName+SPLIT_TOKEN+overalllevelName+SPLIT_TOKEN+overallbadgeLibSelected+SPLIT_TOKEN+overalllevelBadgeName+SPLIT_TOKEN+overalllevelBadgeDesc;
						//alert('checkbox row valuegoing to set as :'+checkBoxLevelRow);
						//alert("overall "+overallcheckBoxLevelRow);
						document.getElementById("overallLevelStringRow"+lev).value=overallcheckBoxLevelRow;
				}
				}


                //Undefeated Badge Setup

				var undefeated = document.getElementById("undefeatedTdBadgeRuleListSize").value;

				if(parseInt(undefeated)>0)
			    {
			     commaIndex=0;
			     var undefeatedcountryId=$("#countryRow").val();
			     var undefeatedBadgeRuleId=$("#undefeatedBadgeRuleId").val();
			     if(undefeatedcountryId==null || undefeatedcountryId=='undefined')
			     {
			    	 undefeatedcountryId='0';
			     }
			        var undefeatedBadgeLibSelected=$("#undefeatedBadgeLibraryIdRow").val();
					var undefeatedBadgeName=$("#undefeatedBadgeNameRow").val();
					var undefeatedBadgeDesc=$("#undefeatedBadgeDescRow").val();


					if(undefeatedBadgeName==null|| undefeatedBadgeName=='' || undefeatedBadgeName=='null' )
						undefeatedBadgeName='0';
					if(undefeatedBadgeDesc==null|| undefeatedBadgeDesc=='' || undefeatedBadgeDesc=='null' )
					{
						undefeatedBadgeDesc='0';
					}
					if( undefeatedBadgeRuleId == null || undefeatedBadgeRuleId == '' || undefeatedBadgeRuleId == 'null' || undefeatedBadgeRuleId == 'undefined')
						undefeatedBadgeRuleId='0';
					//commaIndex=levelBadgeName.indexOf(",");
					undefeatedBadgeName=trim(undefeatedBadgeName);
					for(var i=0;i<undefeatedBadgeName.length;i++)
					{
						if(undefeatedBadgeName.charAt(i)==SPLIT_TOKEN)
						{
							commaIndex++;
							break;
						}
					}
                    undefeatedBadgeLibSelected = undefeatedBadgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
					if(commaIndex>0)
					{
						undefeatedBadgeName=undefeatedBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
					}

					undefeatedBadgeDesc=undefeatedBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);
					//alert("badge level"+undefeatedBadgelevelName);
					//alert("badge lib"+undefeatedBadgeLibSelected);

					var undefeatedcheckBoxLevelRow = undefeatedBadgeRuleId+SPLIT_TOKEN+undefeatedcountryId+SPLIT_TOKEN+undefeatedBadgeLibSelected+SPLIT_TOKEN+undefeatedBadgeName+SPLIT_TOKEN+undefeatedBadgeDesc;

					document.getElementById("undefeatedBadgeStringRow").value=undefeatedcheckBoxLevelRow;

			    }


			}
			else
			{

				var totalRowsCount=$("#currentPointRangeTableSize").val();
				for(var pIdx=0;pIdx<totalRowsCount;pIdx++)
				{
					 commaIndex=0;
					 badgeRuleId=$("#pointBadgeRuleId"+pIdx).val();
			 		 pointMinRange=$("#rangeAmountMin"+pIdx).val();
					 pointMaxRange=$("#rangeAmountMax"+pIdx).val();
					 pointBadgeLibSelected=$("#pointBadgeLibraryId"+pIdx).val();
					 pointBadgeName=$("#pointBadgeNameRow"+pIdx).val();
					 pointBadgeDesc=$("#pointBadgeDescRow"+pIdx).val();
					 if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
							badgeRuleId='0';
					 if(pointMinRange==null|| pointMinRange=='' || pointMinRange=='null' )
						 pointMinRange='0';
					 if(pointMaxRange==null|| pointMaxRange=='' || pointMaxRange=='null' )
						 pointMaxRange='0';
					 if(pointBadgeName==null|| pointBadgeName=='' || pointBadgeName=='null' )
						 pointBadgeName='0';
					 if(pointBadgeDesc==null|| pointBadgeDesc=='' || pointBadgeDesc=='null' )
							pointBadgeDesc='0';

					 //commaIndex=pointBadgeName.indexOf(",");
					 pointBadgeName=trim(pointBadgeName);
					 for(var i=0;i<pointBadgeName.length;i++)
					 {
						if(pointBadgeName.charAt(i)==SPLIT_TOKEN)
						{
							commaIndex++;
							break;
						}
					 }
                     pointBadgeLibSelected = pointBadgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
 					 if(commaIndex>0)
					 {
							pointBadgeName=pointBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
					 }
 					 pointBadgeDesc=pointBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);
					 pointcheckBoxRow=badgeRuleId+SPLIT_TOKEN+pointMinRange+SPLIT_TOKEN+pointMaxRange+SPLIT_TOKEN+pointBadgeLibSelected+SPLIT_TOKEN+pointBadgeName+SPLIT_TOKEN+pointBadgeDesc;
					 //alert('checkbox row valuegoing to set as :'+pointcheckBoxRow);
					 document.getElementById("pointRangeStringRow"+pIdx).value=pointcheckBoxRow;
				     if(pointBadgeLibSelected!='-1')
				     {
							document.getElementById("pointRangeStringRow"+pIdx).checked=true;
				     }
				     else
				     {
				    	 document.getElementById("pointRangeStringRow"+pIdx).checked=false;
				     }
				}

			}
	 }
	 else if(selectedBadgeType=='progress')
	 {
	   	      var maxQualifier;
			  var progressBadgeLibSelected;
			  var progressBadgeName;
			  var progressBadgeDesc;
			  var progresscheckBoxRow='';
			  var progressBadgePoints;
			  var progressSweep;
			  var badgeRuleId;
			  var totalRowsCount=$("#currentProgressTableSize").val();
  			  for(var pIdx=0;pIdx<totalRowsCount;pIdx++)
			  {
  						commaIndex=0;
  						badgeRuleId=$("#progressBadgeRuleId"+pIdx).val();
  						maxQualifier=$("#progressMaxQualifier"+pIdx).val();
						progressBadgeLibSelected=$("#progressBadgeLibraryId"+pIdx).val();
						progressBadgeName=$("#progressBadgeNameRow"+pIdx).val();
						progressBadgePoints=$("#progressBadgePointsRow"+pIdx).val();
						progressSweep=$("#progressBadgeSweepRow"+pIdx).is(':checked');
						progressBadgeDesc=$("#progressBadgeDescRow"+pIdx).val();
						if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
							badgeRuleId='0';
						if(maxQualifier==null|| maxQualifier=='' || maxQualifier=='null' )
							maxQualifier='0';
						if(progressBadgeName==null|| progressBadgeName=='' || progressBadgeName=='null' )
							progressBadgeName='0';
						if(progressBadgeDesc==null|| progressBadgeDesc=='' || progressBadgeDesc=='null' )
							progressBadgeDesc='0';
						if(progressBadgePoints==null|| progressBadgePoints=='' || progressBadgePoints=='null' )
							progressBadgePoints='null';

						//commaIndex=progressBadgeName.indexOf(",");
						progressBadgeName=trim(progressBadgeName);
						for(var i=0;i<progressBadgeName.length;i++)
						{
								if(progressBadgeName.charAt(i)==SPLIT_TOKEN)
								{
									commaIndex++;
									break;
								}
						}
                        progressBadgeLibSelected = progressBadgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
						if(commaIndex>0)
						{
							progressBadgeName=progressBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);

						}
						progressBadgeDesc=progressBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);
						progresscheckBoxRow=badgeRuleId+SPLIT_TOKEN+maxQualifier+SPLIT_TOKEN+progressBadgeLibSelected+SPLIT_TOKEN+progressBadgeName+SPLIT_TOKEN+progressBadgePoints+SPLIT_TOKEN+progressSweep+SPLIT_TOKEN+progressBadgeDesc;
						//alert('checkbox row valuegoing to set as :'+progresscheckBoxRow);

						document.getElementById("progressStringRow"+pIdx).value=progresscheckBoxRow;
   					    if(progressBadgeLibSelected!='-1')
							document.getElementById("progressStringRow"+pIdx).checked=true;
   					    else
   					    	document.getElementById("progressStringRow"+pIdx).checked=false;
				}

	 }
     else if(selectedBadgeType=='fileload')
	 {
			  var fileloadBadgeLibSelected;
			  var fileloadBadgeName;
			  var fileloadBadgeDesc;
			  var fileloadcheckBoxRow='';
			  var fileloadBadgePoints;
			  var fileloadBadgeSweep;
			  var badgeRuleId;
			  var totalRowsCount=$("#currentFileLoadTableSize").val();
			  for(var fIdx=0;fIdx<totalRowsCount;fIdx++)
			  {
				    commaIndex=0;
				  	badgeRuleId=$("#fileLoadBadgeRuleId"+fIdx).val();
					fileloadBadgeLibSelected=$("#fileLoadBadgeLibraryId"+fIdx).val();
					fileloadBadgeName=$("#fileLoadBadgeNameRow"+fIdx).val();
					if( selectedPromotion == -1 || promotionType == 'Y' )
					{
					fileloadBadgePoints=$("#fileLoadBadgePointsRow"+fIdx).val();
					fileloadBadgeSweep=$("#fileLoadBadgeSweepRow"+fIdx).is(':checked');
					}
					fileloadBadgeDesc=$("#fileLoadBadgeDescRow"+fIdx).val();
					if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
						badgeRuleId='0';
					if(fileloadBadgeName==null|| fileloadBadgeName=='' || fileloadBadgeName=='null' )
						fileloadBadgeName='0';
					if(fileloadBadgeDesc==null|| fileloadBadgeDesc=='' || fileloadBadgeDesc=='null' )
						fileloadBadgeDesc='0';
					if(fileloadBadgePoints==null|| fileloadBadgePoints=='' || fileloadBadgePoints=='null' )
						fileloadBadgePoints='null';
					//commaIndex=fileloadBadgeName.indexOf(",");
					fileloadBadgeName=trim(fileloadBadgeName);
					for(var i=0;i<fileloadBadgeName.length;i++)
					{
							if(fileloadBadgeName.charAt(i)==SPLIT_TOKEN)
							{
								commaIndex++;
								break;
							}
					}
					fileloadBadgeLibSelected=fileloadBadgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
					if(commaIndex>0)
					{
						fileloadBadgeName=fileloadBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
					}
					fileloadBadgeDesc=fileloadBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);

					if( selectedPromotion== '-1' || promotionType == 'Y' )
					{
						fileloadcheckBoxRow=badgeRuleId+SPLIT_TOKEN+fileloadBadgeLibSelected+SPLIT_TOKEN+fileloadBadgeName+SPLIT_TOKEN+fileloadBadgePoints+SPLIT_TOKEN+fileloadBadgeSweep+SPLIT_TOKEN+fileloadBadgeDesc;
					}
					else
				    {
						fileloadcheckBoxRow=badgeRuleId+SPLIT_TOKEN+fileloadBadgeLibSelected+SPLIT_TOKEN+fileloadBadgeName+SPLIT_TOKEN+fileloadBadgeDesc;
				    }

					//alert('checkbox row valuegoing to set as :'+fileloadcheckBoxRow);
					document.getElementById("fileLoadStringRow"+fIdx).value=fileloadcheckBoxRow;
					if(fileloadBadgeLibSelected!='-1')
						document.getElementById("fileLoadStringRow"+fIdx).checked=true;
					else
						document.getElementById("fileLoadStringRow"+fIdx).checked=false;
				}

	  }
	validateFields(method , promotionType);

}

function validateBadgeForm(method)
{
	var badgeExists='N';
	var errorMessage='';
	if( $("#badgeId").val() != null )
		  badgeId = $("#badgeId").val();
	var badgeSetupName = $("#badgeSetupName").val();
	var selectedPromo = $("#selectedPromotionIds").val();
	if(badgeId!=null && badgeId!='' && badgeSetupName!=null&& badgeSetupName!='')
	{

			$.ajax({
						url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeMaintain.do?method=validateBadgeSetupName&badgeName="+badgeSetupName+"&badgeId="+badgeId+"&promotionIds="+selectedPromo,
						success: function(data){
							if(data != null && data != '' && data != 'null')
							{
								var objectLib = jQuery.parseJSON(data);

								if(objectLib.isBadgeExists=='Y')
								{
									errorMessage='<cms:contentText key="BADGE_EXISTS" code="gamification.validation.messages" />';
									$("#errorShowDiv").html(errorMessage);
									$("#errorShowDiv").show();
									return false;
								}
								else
								{
									setAllValues(method, objectLib.isRecognitionOrNomination );
									return true;
								}

							}
						}
			});
	}
	else
	{
		errorMessage='<cms:contentText key="BADGE_SETUPNAME_REQUIRED" code="gamification.validation.messages" />';
		$("#errorShowDiv").html(errorMessage);
		$("#errorShowDiv").show();

	}
}
</script>

<c:set var="displayFlag" value="${ promotionStatus == 'live' }" />

<html:form styleId="contentForm" action="badgeMaintain">
	<html:hidden property="method" value="editBadge" />

	  <table><tr><td><cms:errors/></td></table>
	  <table><tr><td><html:errors/></td></table>
	 <input type="hidden" name="currentFileLoadTableSize" id="currentFileLoadTableSize"/>
	 <input type="hidden" name="currentProgressTableSize" id="currentProgressTableSize"/>
	 <input type="hidden" name="currentPointRangeTableSize" id="currentPointRangeTableSize"/>
	 <!-- <input type="hidden" name="badgeRuleListSize" id="badgeRuleListSize"/> -->
	  <html:hidden property="badgeRuleListSize" styleId="badgeRuleListSize" />
	   <html:hidden property="undefeatedTdBadgeRuleListSize" styleId="undefeatedTdBadgeRuleListSize" />
	 <!--  <html:hidden property="badgeId" styleId="badgeId"/> -->

	<div id="errorShowDiv" class="error">

	</div>
	<div id="badgeCommonFields">
		<table border="0" cellpadding="10" cellspacing="0" width="100%">
			<tr>
				<td><span class="headline"><cms:contentText key="UPDATE_BADGE" code="gamification.admin.labels" /> </span> <%--INSTRUCTIONS--%>
					<br /> <br /> <span class="content-instruction"> <cms:contentText key="UPDATE_BADGE_DESC" code="gamification.admin.labels" /></span> <br /> <br /> <%--END INSTRUCTIONS--%>

					<table>

						<tr><td><html:hidden property="badgeId" styleId="badgeId" /></td>
						</tr>

						<tr class="form-row-spacer">
							<beacon:label property="badgeSetupName" required="true">
								<cms:contentText key="BADGE_SETUP_NAME" code="gamification.admin.labels" />
							</beacon:label>
							<td class="content-field"><html:text
									property="badgeSetupName" styleId="badgeSetupName"  size="50" maxlength="50"
									styleClass="content-field"/></td>
						</tr>
						<%-- Needed between every regular row --%>
						<tr class="form-blank-row">
							<td></td>
						</tr>

						<tr class="form-row-spacer">
							<beacon:label property="promotionIds" required="true">
								<cms:contentText key="PROMOTIONS" code="gamification.admin.labels" />
							</beacon:label>
							<td class="content-field">
							<c:choose>
							  <c:when test="${ promotionStatus=='live' }">
							    <html:text property="selectedPromotion" styleClass="content-field" disabled="true" styleId="selectedPromotion" size="40" readonly="true"/>
								<html:hidden property="selectedPromotionIds" styleId="selectedPromotionIds"/>
							  </c:when>
							  <c:otherwise>
							    <html:select styleId="selectedPromotion" property="selectedPromotion" styleClass="content-field"  multiple="true" onclick="showOrHideLayers();">
					              <html:option value="-1"><cms:contentText key="NO_PROMOTION" code="gamification.admin.labels" /></html:option>
					              <html:options collection="promotionList" property="id" labelProperty="name" />
					            </html:select>
							  </c:otherwise>
							</c:choose>
							</td>
						</tr>
						<%-- Needed between every regular row --%>
						<tr class="form-blank-row">
							<td></td>
						</tr>

						<tr>
		             	<beacon:label property="startDate" required="true">
				  			<cms:contentText code="promotion.basics" key="START"/>
				  		</beacon:label>
				  		<td class="content-field">
					  		<html:text property="startDate" styleId="startDate" size="10" maxlength="10" styleClass="content-field" disabled="${displayFlag}"/>
		                </td>
		                </tr>

                		<%-- Needed between every regular row --%>
						<tr class="form-blank-row">
							<td></td>
						</tr>

						<tr class="form-row-spacer">
							<beacon:label property="displayDays" required="true">
								<cms:contentText key="DISPLAY_NUMBER_OF_DAYS" code="gamification.admin.labels" />
							</beacon:label>
							<td class="content-field"><html:text property="displayDays"	size="4" maxlength="4"  styleClass="content-field" styleId="displayDays" disabled="${displayFlag}"/></td>
						</tr>

						<%-- Needed between every regular row --%>
						<tr class="form-blank-row">
							<td></td>
						</tr>

						<tr class="form-row-spacer">
							<beacon:label property="tileHighlightPeriod" required="true">
								<cms:contentText key="TILE_HIGHLIGHT_PERIOD" code="gamification.admin.labels" />
							</beacon:label>
 							<td class="content-field"><html:text property="tileHighlightPeriod" size="3" maxlength="3" styleClass="content-field" styleId="tileHighlightPeriod"/></td>
						</tr>
						<%-- Needed between every regular row --%>
						<tr class="form-blank-row">
							<td></td>
						</tr>

						<%
						 Map notificationMessageMapParam = (Map)request.getAttribute("notificationMessageMap");
						 pageContext.setAttribute("notificationMessageTypeList", notificationMessageMapParam.get(MessageUtils.getMessageTypeCode("badge_received")));

						%>
						<tr>
							<beacon:label property="isNotificationRequired" required="true">
								<cms:contentText key="PAX_NOTIFICATION_REQUIRED" code="gamification.admin.labels" />
							</beacon:label>
							<td class="content-field">
							<nested:select property="notificationMessageId" styleClass="content-field content-field-notification-email killme" styleId="notificationMessageId" >
   			      					<html:options collection="notificationMessageTypeList" property="id" labelProperty="name" />
			    			</nested:select>
							</td>
						</tr>
						<%-- Needed between every regular row --%>
						<tr class="form-blank-row">
							<td></td>
						</tr>
						<tr id="badgeTypeAll" class="form-row-spacer">
							<beacon:label property="badgeType" required="true">
								<cms:contentText key="BADGE_TYPE" code="gamification.admin.labels" />
							</beacon:label>
							<td class="content-field">
							<c:choose>
							  <c:when test="${ promotionStatus=='live' }">
							    <html:text property="badgeTypeName" styleClass="content-field killme"  styleId="badgeTypeId" readonly="true"/>
							  </c:when>
							  <c:otherwise>
							    <html:select property="badgeTypeName" size="1" styleClass="content-field"  styleId="badgeTypeName" onclick="showOrHideLayers();">
									<html:option value='-1'><cms:contentText key="CHOOSE_ONE" code="system.general" /></html:option>
									<html:options collection="badgeTypeList" property="code" labelProperty="name" />
									</html:select>
							  </c:otherwise>
							</c:choose>
							</td>
						</tr>

						<%-- Needed between every regular row --%>
						<tr class="form-blank-row">
							<td></td>
						</tr>

				</table> <%-- End Input Example  --%></td>
		</tr>
	</table>
	</div>

	<div id="badgesBillCodes">
	  <table border="0" cellpadding="10" cellspacing="0" width="50%">

	  <%-- Needed between every regular row --%>
		<tr class="form-blank-row">
			<td></td>
		</tr>

		<tr class="form-row-spacer" id="taxable">
            <beacon:label property="taxable" required="true" styleClass="content-field-label-top">
              <cms:contentText key="TAXABLE" code="promotion.basics"/>
            </beacon:label>

            <td colspan=2 class="content-field">
              <table>
                  <tr>
		            <td class="content-field"><html:radio property="taxable" value="false"/></td>
		            <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
		          </tr>
		          <tr>
 					<td class="content-field"><html:radio property="taxable" value="true"/></td>
		            <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
		          </tr>
			  </table>
			</td>
    	</tr>

	    <%-- Needed between every regular row --%>
		<tr class="form-blank-row">
			<td></td>
		</tr>
		    <tr class="form-row-spacer">
		        <beacon:label property="billCodesActive" required="true" >
		            <cms:contentText code="promotion.bill.code" key="BILL_CODES_ACTIVE" />
		        </beacon:label>
		        <td class="content-field" valign="top" colspan="2">
		       		<html:radio styleId="billCodesActiveFalse" property="billCodesActive" value="false" onclick="enableFields();"
		           	/>&nbsp;<cms:contentText code="promotion.bill.code" key="NO" />
		        </td>
		    </tr>
		    <tr class="form-row-spacer">
		        <td colspan="2">&nbsp;</td>
		        <td class="content-field" valign="top" colspan="2">
		        	<html:radio styleId="billCodesActiveTrue" property="billCodesActive" value="true" onclick="enableFields();"
		            />&nbsp;<cms:contentText code="promotion.bill.code" key="YES" />
		       	</td>
		    </tr>

		    <%-- Needed between every regular row --%>
			<tr class="form-blank-row">
				<td></td>
			</tr>

			<%@include file="/gamification/badgeBillCodes.jsp" %>

	  </table>
	</div>


	<!-- Div for showing the fields if badge type is "Progress" -->
	<div id="progressBadgeDiv">

							<table>
								<tr class="form-row-spacer">
									<beacon:label property="badgeCountType" required="true">
										<cms:contentText key="BADGE_COUNT_TYPE" code="gamification.admin.labels" />
									</beacon:label>


									<td class="content-field">
										<html:text property="badgeCountType" styleClass="content-field"  styleId="badgeCountType" readonly="true"/>

									</td>
								</tr>
							</table>
							
							<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
							  <table><tr><td><a href="#" onclick="javascript:addNewRow('progress');"><cms:contentText key="ADD_ANOTHER_ROW" code="gamification.admin.labels" /></a></td></tr></table>
							</beacon:authorize>

							<table>

							<%-- Needed between every regular row --%>
							<tr class="form-blank-row">
								<td></td>
							</tr>
							</table>
							<table>
							<thead>
							<tr class="crud-table-row2">

									<th class="crud-table-header-row" id="progressIdHeader">
										<cms:contentText key="BADGE_LEVEL" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row" align="left">
										<cms:contentText key="BADGE_LIBRARY" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_POINTS" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="SWEEPSTAKES" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />
									</th>

								</tr>
								</thead>
							<tr>
							<td></td>
								</tr>
				<c:forEach var="progressBean" items="${badgeRuleList}" varStatus="progressTableCount">
					<% BadgeRule badgeRule=null;

					badgeRule=(BadgeRule)pageContext.getAttribute("progressBean");

					String badgeRuleId=badgeRule.getId()+"";


					%>

				<tr class="crud-table-row2">
				<td  class="crud-content left-align top-align nowrap">
									    	<html:text property="maxQualifier" styleId="progressMaxQualifier${progressTableCount.index}" value="${progressBean.maximumQualifier}" size="12" maxlength="12" styleClass="content-field" readonly="true"/>

				 </td>
				<td class="crud-content left-align top-align nowrap">
				<html:hidden property="badgeLibraryId" styleId="progressBadgeLibraryId${progressTableCount.index}" value="${progressBean.badgeLibraryCMKey}"/>
				<html:text property="badgeLibraryId" styleId="progressBadgeLibraryName${progressTableCount.index}" value="${progressBean.badgeLibDisplayName}"  size="24" styleClass="content-field" readonly="false"/>

				</td>
				 <td  class="crud-content left-align top-align nowrap">
									    	<html:text property="badgeName" styleId="progressBadgeNameRow${progressTableCount.index}" value="${progressBean.badgeName}"  size="40" maxlength="40" styleClass="content-field" />

				 </td>
				 <td  class="crud-content left-align top-align nowrap">
									    	<html:text property="badgePoints" styleId="progressBadgePointsRow${progressTableCount.index}" value="${progressBean.badgePoints}"  size="10" maxlength="5" styleClass="content-field" />

				 </td>
				 <td  class="crud-content left-align top-align nowrap">
				  <c:choose>
				    <c:when test="${progressBean.eligibleForSweepstake}">
				      <input type="checkbox" name="eligibleForSweepstake" id="progressBadgeSweepRow${progressTableCount.index}" checked="checked"/>
				    </c:when>
				    <c:otherwise>
				      <input type="checkbox" name="eligibleForSweepstake" id="progressBadgeSweepRow${progressTableCount.index}"/>
				    </c:otherwise>
				  </c:choose>
				 </td>
				 <td  class="crud-content left-align top-align nowrap">
								   		<html:textarea property="badgeDescription" styleId="progressBadgeDescRow${progressTableCount.index}"  value="${progressBean.badgeDescription}" styleClass="content-field" />
									    </td>
									    <td id="progresscheckBoxDiv${progressTableCount.index}">
									    	<html:checkbox property="progressStringRow" styleId="progressStringRow${progressTableCount.index}" value="0" />
									    </td>
									    <td>
									    <input type="hidden" name="badgeRuleId" id="progressBadgeRuleId${progressTableCount.index}" value="<%=badgeRuleId%>"/>
									    </td>
				</tr>

			    </c:forEach>




		</table>


</div>

<!-- Div for showing the fields if badge type is "File Load" -->
<div id="fileLoadBadgeDiv">
			
		<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">		
		  <table><tr><td><a href="#" onclick="javascript:addNewRow('fileload');"><cms:contentText key="ADD_ANOTHER_ROW" code="gamification.admin.labels" /></a></td></tr></table>
		</beacon:authorize>

		<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<thead>
							<tr class="crud-table-row2">

									<th class="crud-table-header-row" align="left">
										<cms:contentText key="BADGE_LIBRARY" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" />
									</th>

									<c:if test="${isShowFileloadNoPromoDiv == 'Y' || badgeForm.selectedPromotionIds == '-1'}" >
									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_POINTS" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="SWEEPSTAKES" code="gamification.admin.labels" />
									</th>
									</c:if>

									<th class="crud-table-header-row">
										<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />
									</th>

								</tr>
								</thead>
							<tr>
							<td></td>
								</tr>

			<c:forEach var="fileLoadBean" items="${badgeRuleList}" varStatus="fileLoadTableCount">

				<% BadgeRule badgeRule=null;

					badgeRule=(BadgeRule)pageContext.getAttribute("fileLoadBean");

					String badgeRuleId=badgeRule.getId()+"";


					%>

				<tr class="crud-table-row2">
				<td class="crud-content left-align top-align nowrap">
				<html:hidden property="badgeLibraryId" styleId="fileLoadBadgeLibraryId${fileLoadTableCount.index}" value="${fileLoadBean.badgeLibraryCMKey}" />
				<html:text property="badgeLibraryId" styleId="fileLoadBadgeLibraryName${fileLoadTableCount.index}" value="${fileLoadBean.badgeLibDisplayName}"  size="24" styleClass="content-field" readonly="true"/>

				</td>
				 <td  class="crud-content left-align top-align nowrap">
						<html:text property="badgeName" styleId="fileLoadBadgeNameRow${fileLoadTableCount.index}" value="${fileLoadBean.badgeName}" size="40" maxlength="40" styleClass="content-field" />
				 </td>

				 <c:if test="${isShowFileloadNoPromoDiv == 'Y' || badgeForm.selectedPromotionIds == '-1'}" >
				  <td  class="crud-content left-align top-align nowrap">
						<html:text property="badgePoints" styleId="fileLoadBadgePointsRow${fileLoadTableCount.index}" value="${fileLoadBean.badgePoints}" size="10" maxlength="5" styleClass="content-field" />
				 </td>
				  <td  class="crud-content left-align top-align nowrap">
				  <c:choose>
				    <c:when test="${fileLoadBean.eligibleForSweepstake}">
				      <input type="checkbox" name="eligibleForSweepstake" id="fileLoadBadgeSweepRow${fileLoadTableCount.index}" checked="checked"/>
				    </c:when>
				    <c:otherwise>
				      <input type="checkbox" name="eligibleForSweepstake" id="fileLoadBadgeSweepRow${fileLoadTableCount.index}"/>
				    </c:otherwise>
				  </c:choose>
				 </td>
				 </c:if>

				 <td  class="crud-content left-align top-align nowrap">
	   				<html:textarea property="badgeDescription" styleId="fileLoadBadgeDescRow${fileLoadTableCount.index}" value="${fileLoadBean.badgeDescription}" styleClass="content-field" />
			    </td>
			    <td id="fileLoadcheckBoxDiv${fileLoadTableCount.index}">
			    	<html:checkbox property="fileLoadStringRow" styleId="fileLoadStringRow${fileLoadTableCount.index}" value="0" />
			    </td>
			    <td>
			    	<input type="hidden" name="badgeRuleId" id="fileLoadBadgeRuleId${fileLoadTableCount.index}" value="<%=badgeRuleId%>"/>
			    </td>
			</tr>
			</c:forEach>

		</table>


</div>


<!-- Div for showing the fields if badge type is "Behavior" -->
<div id="behaviorBadgeDiv">

	<table>
							<thead>
							<tr class="crud-table-row2">

									<th class="crud-table-header-row">
										<cms:contentText key="BEHAVIOR" code="gamification.admin.labels" />
									</th>
									<th class="crud-table-header-row">
										<cms:contentText key="PROMOTION_NAME" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_LIBRARY" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_POINTS" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="SWEEPSTAKES" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />
									</th>

								</tr>
								</thead>
								<tr>
										<td></td>
								</tr>

								<c:forEach var="behaviorBean" items="${badgeRuleList}" varStatus="behaviorTableCount">

										<% BadgeRule badgeRule=null;

											badgeRule=(BadgeRule)pageContext.getAttribute("behaviorBean");

											String badgeRuleId=badgeRule.getId()+"";
											String behaviorCode=badgeRule.getBehaviorName();


										%>

									<tr class="crud-table-row2">
									<td  class="crud-content left-align top-align nowrap">
										 <c:out value="${behaviorBean.behaviorDisplayName}" escapeXml="false"/>
									</td>

									<td  class="crud-content left-align top-align nowrap">
										<c:out value="${behaviorBean.promotionNames}" escapeXml="false"/>

									</td>
									 <td class="crud-content left-align top-align nowrap">
									 <c:choose>
									 <c:when test="${behaviorBean.badgeLibraryCMKey != null}">
									<html:hidden property="badgeLibraryId" styleId="badgeLibraryIdRow${behaviorTableCount.index}" value="${behaviorBean.badgeLibraryCMKey}"/>
									<html:text property="badgeLibraryId" styleId="badgeLibraryIdRow${behaviorTableCount.index}" value="${behaviorBean.badgeLibDisplayName}" size="17" styleClass="content-field" readonly="false" />
									</c:when>
									<c:otherwise>
									<select name="badgeLibraryId" id="badgeLibraryIdRowNew${behaviorTableCount.index}" class="content-field">
						            <c:forEach var="badgeLibraryListRows" items="${badgeLibraryList}" varStatus="badgeLibraryCount">
								    <option value="${badgeLibraryListRows.badgeLibraryId}" <c:if test="${badgeLibraryListRows.libraryname == behaviorBean.badgeLibDisplayName}">selected="selected"</c:if>>${badgeLibraryListRows.libraryname}</option>
						            </c:forEach>
						            </select>
									</c:otherwise>
									</c:choose>
									</td>

									<td  class="crud-content left-align top-align nowrap">
										 <html:text property="badgeName" styleId="behaviorbadgeNameRow${behaviorTableCount.index}" value="${behaviorBean.badgeName}"  size="40" maxlength="40" styleClass="content-field" />

									 </td>

									 <td  class="crud-content left-align top-align nowrap">
										 <html:text property="badgePoints" styleId="behaviorbadgePointsRow${behaviorTableCount.index}" value="${behaviorBean.badgePoints}"  size="10" maxlength="5" styleClass="content-field" />

									 </td>

									 <td  class="crud-content left-align top-align nowrap">
									  <c:choose>
									    <c:when test="${behaviorBean.eligibleForSweepstake}">
									      <input type="checkbox" name="eligibleForSweepstake" id="behaviorBadgeSweepRow${behaviorTableCount.index}" checked="checked"/>
									    </c:when>
									    <c:otherwise>
									      <input type="checkbox" name="eligibleForSweepstake" id="behaviorBadgeSweepRow${behaviorTableCount.index}"/>
									    </c:otherwise>
									  </c:choose>
									 </td>

									 <td  class="crud-content left-align top-align nowrap">
													   		<html:textarea property="badgeDescription" styleId="behaviorbadgeDescRow${behaviorTableCount.index}"  value="${behaviorBean.badgeDescription}" styleClass="content-field" />

									</td>
														    <td id="checkBoxDiv${behaviorTableCount.index}">
														    	<html:checkbox property="behaviorStringRow" styleId="behaviorStringRow${behaviorTableCount.index}" value="0" />
														    </td>
														    <td>
															    <input type="hidden" name="badgeRuleId" id="behaviorBadgeRuleId${behaviorTableCount.index}" value="<%=badgeRuleId%>"/>
														    </td>
														     <td>
															    <input type="hidden" name="behaviorCode" id="behaviorCode${behaviorTableCount.index}" value="<%=behaviorCode%>"/>
														    </td>
									</tr>
			    				</c:forEach>

						</table>
						<table>
						  <tr>
						    <td>
						      <c:choose>
						        <c:when test="${displayFlag}">
						          <c:choose>
							        <c:when test="${badgeForm.includeAllBehaviorPoints}">
							          <input type="checkbox" name="includeAllBehaviorPoints" id="includeAllBehaviorPoints" checked="checked"/>
							        </c:when>
							        <c:otherwise>
							          <input type="checkbox" name="includeAllBehaviorPoints" id="includeAllBehaviorPoints"/>
							        </c:otherwise>
							      </c:choose>
						        </c:when>
						        <c:otherwise>
						          <c:choose>
							        <c:when test="${badgeForm.includeAllBehaviorPoints}">
							          <input type="checkbox" name="includeAllBehaviorPoints" id="includeAllBehaviorPoints" checked="checked"/>
							        </c:when>
							        <c:otherwise>
							          <input type="checkbox" name="includeAllBehaviorPoints" id="includeAllBehaviorPoints"/>
							        </c:otherwise>
							      </c:choose>
						        </c:otherwise>
						      </c:choose>
							  <c:out value="Include Points if all behaviors are earned"></c:out>&nbsp;&nbsp;&nbsp;
						    </td>
						    <td>
						      <html:text property="allBehaviorPoints" styleId="allBehaviorPoints" value="${badgeForm.allBehaviorPoints}"  size="10" maxlength="5" styleClass="content-field"/>
						    </td>
						  </tr>
						</table>


</div>

<!-- Div for showing the fields if badge type is "Earned/Not Earned" -->
<div id="earnedNotEarnedBadgePointDiv">

				<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
					<table><tr><td><a href="#" onclick="javascript:addNewRow('pointRange');"><cms:contentText key="ADD_ANOTHER_ROW" code="gamification.admin.labels" /></a></td></tr></table>
				</beacon:authorize>

						<table>
							<thead>
							<tr class="crud-table-row2">

									<th class="crud-table-header-row" id="pointRangeIdHeader" colspan="2">
										&nbsp;&nbsp;&nbsp;<cms:contentText key="POINT_RANGE" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_LIBRARY" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />
									</th>

								</tr>
								</thead>
								<tr>
										<td></td>
								</tr>

								<c:forEach var="pointRangeBean" items="${badgeRuleList}" varStatus="pointRangeTableCount">


										<% BadgeRule badgeRule=null;

											badgeRule=(BadgeRule)pageContext.getAttribute("pointRangeBean");

											String badgeRuleId=badgeRule.getId()+"";


										%>

									<tr class="crud-table-row2">
									<td  class="crud-content left-align top-align nowrap" colspan="2">
										 <html:text styleId="rangeAmountMin${pointRangeTableCount.index}" property="rangeAmountMin" value="${pointRangeBean.minimumQualifier}" size="5" maxlength="12" styleClass="content-field" readonly="true"/> To &nbsp;

							 			 <html:text	styleId="rangeAmountMax${pointRangeTableCount.index}" property="rangeAmountMax" value="${pointRangeBean.maximumQualifier}" size="5" maxlength="12" styleClass="content-field" readonly="true"/>


									</td>
									<td class="crud-content left-align top-align nowrap">
									<html:hidden property="badgeLibraryId" styleId="pointBadgeLibraryId${pointRangeTableCount.index}" value="${pointRangeBean.badgeLibraryCMKey}"/>
									<html:text property="badgeLibraryId" styleId="pointBadgeLibraryName${pointRangeTableCount.index}" value="${pointRangeBean.badgeLibDisplayName}" size="24" styleClass="content-field" readonly="true" />

									</td>
									 <td  class="crud-content left-align top-align nowrap">
										 <html:text property="badgeName" styleId="pointBadgeNameRow${pointRangeTableCount.index}" value="${pointRangeBean.badgeName}" size="40" maxlength="40" styleClass="content-field" />

									 </td>
									 <td  class="crud-content left-align top-align nowrap">
													   		<html:textarea property="badgeDescription" styleId="pointBadgeDescRow${pointRangeTableCount.index}" value="${pointRangeBean.badgeDescription}" styleClass="content-field" />
														    </td>
														    <td id="pointcheckBoxDiv${pointRangeTableCount.index}">
														    	<html:checkbox property="pointRangeStringRow" styleId="pointRangeStringRow${pointRangeTableCount.index}" value="0" />
														    </td>
														    <td>
															    <input type="hidden" name="badgeRuleId" id="pointBadgeRuleId${pointRangeTableCount.index}" value="<%=badgeRuleId%>"/>
														    </td>
									</tr>
			    				</c:forEach>

						</table>
</div>


<div id="earnedNotEarnedBadgeLevelDiv">

		<table>
							<thead>
							<tr class="crud-table-row2">

								   <c:if test="${isGoalQuest!='Y'}">
										<th class="crud-table-header-row">
											<cms:contentText key="COUNTRY" code="gamification.admin.labels" />
										</th>
									</c:if>
									<th class="crud-table-header-row">
										<cms:contentText key="LEVEL" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_LIBRARY" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" />
									</th>

									<th class="crud-table-header-row">
										<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />
									</th>

								</tr>
								</thead>
								<tr>
										<td></td>
								</tr>

								<c:forEach var="levelBean" items="${badgeRuleList}" varStatus="levelTableCount">


									<% BadgeRule badgeRule=null;

											badgeRule=(BadgeRule)pageContext.getAttribute("levelBean");

											String badgeRuleId=badgeRule.getId()+"";


									%>

									<tr class="crud-table-row2">
									<c:if test="${isGoalQuest!='Y'}">
										<td  class="crud-content left-align top-align nowrap">
											 <html:text styleId="countryCode${levelTableCount.index}" property="countryCode" value="${levelBean.countryCode}" size="10" styleClass="content-field" readonly="true"/>

										</td>
									</c:if>
									<td  class="crud-content left-align top-align nowrap">
									<html:text styleId="levelsNameRow${levelTableCount.index}" property="levelName" value="${levelBean.levelName}" size="10" styleClass="content-field" readonly="true"/>

									</td>
									 <td class="crud-content left-align top-align nowrap">
									 <html:hidden property="badgeLibraryId" styleId="levelsBadgeLibraryIdRow${levelTableCount.index}" value="${levelBean.badgeLibraryCMKey}"/>
									 <html:text property="badgeLibraryId" styleId="levelsBadgeLibraryNameRow${levelTableCount.index}" value="${levelBean.badgeLibDisplayName}" size="17" styleClass="content-field" readonly="true" />

									</td>

									<td  class="crud-content left-align top-align nowrap">
										 <html:text property="badgeName" styleId="levelsbadgeNameRow${levelTableCount.index}" value="${levelBean.badgeName}" size="40" maxlength="40" styleClass="content-field" />

									 </td>
									 <td  class="crud-content left-align top-align nowrap">
										 		<html:textarea property="badgeDescription" styleId="levelsbadgeDescRow${levelTableCount.index}" value="${levelBean.badgeDescription}"  styleClass="content-field" />

									 </td>
									 <td><input type="hidden" id="countryRow${levelTableCount.index}" name="countryRow${levelTableCount.index}" value="${levelBean.countryId}" /></td>
														    <td id="checkBoxLevelDiv${levelTableCount.index}">
														    	<html:checkbox property="levelStringRow" styleId="levelStringRow${levelTableCount.index}" value="0" />
														    </td>
														    <td>
															    <input type="hidden" name="badgeRuleId" id="levelsBadgeRuleId${levelTableCount.index}" value="<%=badgeRuleId%>"/>
														    </td>
									</tr>
			    				</c:forEach>

						</table>



</div>

<div id="earnedNotEarnedGQBadgeLevelDiv">
<c:set var="participantTypeNoneFound" value="false" />
	<c:forEach var="levelBean" items="${badgeRuleList}" varStatus="levelTableCount">
		<c:if test="${levelBean.participantType == 'NONE'}">
			<c:set var="participantTypeNoneFound" value="true" />
		</c:if>
	</c:forEach>
	<c:if test="${participantTypeNoneFound}">
		<table>
			<thead>
				<tr class="crud-table-row2">

					<!-- <c:if test="${isGoalQuest!='Y'}">
						<th class="crud-table-header-row"><cms:contentText
								key="COUNTRY" code="gamification.admin.labels" /></th>
					</c:if> -->
					<th class="crud-table-header-row"><cms:contentText
							key="PROMOTION_NAME" code="gamification.admin.labels" /></th>

					<th class="crud-table-header-row"><cms:contentText
							key="LEVEL" code="gamification.admin.labels" /></th>

					<th class="crud-table-header-row"><cms:contentText
							key="BADGE_LIBRARY" code="gamification.admin.labels" /></th>

					<th class="crud-table-header-row"><cms:contentText
							key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" /></th>

					<th class="crud-table-header-row"><cms:contentText
							key="DESCRIPTION" code="gamification.admin.labels" /></th>

				</tr>
			</thead>
			<tr>
				<td></td>
			</tr>

		<c:forEach var="levelBean" items="${badgeRuleList}" varStatus="levelTableCount">

			<%
			  BadgeRule badgeRule = null;
		      badgeRule = (BadgeRule)pageContext.getAttribute( "levelBean" );
			  String badgeRuleId = badgeRule.getId() + "";
			%>

			<tr class="crud-table-row2">
				<c:if test="${levelBean.participantType == 'NONE'}">
					<!-- <c:if test="${isGoalQuest!='Y'}">
						<td class="crud-content left-align top-align nowrap">
							<html:text styleId="countryCode${levelTableCount.index}" property="countryCode" value="${levelBean.countryCode}"
								size="10" styleClass="content-field" readonly="true" />
						</td>
					</c:if> -->
					<td  class="crud-content left-align top-align nowrap">
						<html:text property="selectedPromotion" styleClass="content-field" styleId="selectedPromotion" size="40" readonly="true"/>
					</td>
					<td class="crud-content left-align top-align nowrap">
						<html:text styleId="goalLevelsNameRow${levelTableCount.index}" property="levelName" value="${levelBean.levelName}" size="10" styleClass="content-field" readonly="true" />
					</td>
					<td class="crud-content left-align top-align nowrap">
						<html:hidden property="badgeLibraryId" styleId="goalLevelsBadgeLibraryIdRow${levelTableCount.index}" value="${levelBean.badgeLibraryCMKey}" />
						<html:text property="badgeLibraryId" styleId="goalLevelsBadgeLibraryNameRow${levelTableCount.index}" value="${levelBean.badgeLibDisplayName}" size="17"
							styleClass="content-field" readonly="true" />
					</td>
					<td class="crud-content left-align top-align nowrap">
						<html:text property="badgeName" styleId="goalLevelsbadgeNameRow${levelTableCount.index}" value="${levelBean.badgeName}" size="40" maxlength="40"
							styleClass="content-field" /></td>
					<td class="crud-content left-align top-align nowrap">
						<html:textarea property="badgeDescription" styleId="goalLevelsbadgeDescRow${levelTableCount.index}"
							value="${levelBean.badgeDescription}" styleClass="content-field" />
					</td>
					<!--  <td id="checkBoxLevelDiv${levelTableCount.index}">
						<html:checkbox property="levelStringRow" styleId="levelStringRow${levelTableCount.index}" value="0" />
					</td>-->
					<td>
						<input type="hidden" name="badgeRuleId" id="goalLevelsBadgeRuleId${levelTableCount.index}" value="<%=badgeRuleId%>" />
					</td>
				</c:if>
			</tr>
		</c:forEach>
	</table>
	</c:if>
</div>

<div id=earnedNotEarnedTDBadgeStackLevelDiv>
 <%

        String stackRankBadgeHeader = "Stack Ranking Badges";
        String overallRankBadgeHeader = "Overall Badges";
        String undefeatedRankBadgeHeader = "Undefeated Badges";
      %>
       <c:if test="${not empty stackStandBadges}">
       <table><tr><td><span class="headline"><cms:contentText key="STACK_RANKING_BADGES" code="gamification.admin.labels" /></span></td><tr></table><br />
		<table>
			<thead>
				<tr class="crud-table-row2">

					<th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="NODE_TYPE"/></th>

					<th class="crud-table-header-row"><cms:contentText code="promotion.payout.throwdown" key="STACK_RANK_FROM"/>-<cms:contentText code="promotion.payout.throwdown" key="STACK_RANK_TO"/></th>

					<th class="crud-table-header-row"><cms:contentText
							key="BADGE_LIBRARY" code="gamification.admin.labels" /></th>

					<th class="crud-table-header-row"><cms:contentText
							key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" /></th>

					<th class="crud-table-header-row"><cms:contentText
							key="DESCRIPTION" code="gamification.admin.labels" /></th>

				</tr>
			</thead>
			<tr>
				<td></td>
			</tr>

			<%
			  String nodeName = null;
			  String lastNodeName = null;
			  int i=0;
			%>

		<c:forEach var="stackLevelBean" items="${stackStandBadges}" varStatus="stackLevelTableCount">

			<%

			  BadgeRule badgeRule = null;
		      badgeRule = (BadgeRule)pageContext.getAttribute( "stackLevelBean" );
			  String badgeRuleId = badgeRule.getId() + "";
			  nodeName = badgeRule.getLevelName();
			  if ( nodeName.equals( lastNodeName ) )
			  {
				nodeName = "";
			  }

			%>

			<tr class="crud-table-row2">
					<td  class="crud-content left-align top-align nowrap">
					<% if (nodeName.length()>0){ %>
						<html:text styleId="stackLevelsNodeNameRow${stackLevelTableCount.index}" property="stackLevelsNodeNameRow" value="<%=nodeName%>" size="10" styleClass="content-field" readonly="true" />
					<% } else { %>
						<html:hidden styleId="stackLevelsNodeNameRow${stackLevelTableCount.index}" property="stackLevelsNodeNameRow" value=" " styleClass="content-field" />
					<% } %>
					</td>
					<td class="crud-content left-align top-align nowrap">
						<html:text styleId="stackLevelsNameRow${stackLevelTableCount.index}" property="stackLevelsNameRow" value="${stackLevelBean.minimumQualifier}-${stackLevelBean.maximumQualifier}" size="10" styleClass="content-field" readonly="true" />
					</td>
					<td class="crud-content left-align top-align nowrap">
						<html:hidden property="badgeLibraryId" styleId="stackLevelsBadgeLibraryIdRow${stackLevelTableCount.index}" value="${stackLevelBean.badgeLibraryCMKey}" />
						<html:text property="badgeLibraryId" styleId="stackLevelsBadgeLibraryNameRow${stackLevelTableCount.index}" value="${stackLevelBean.badgeLibDisplayName}" size="17"
							styleClass="content-field" readonly="false" />
					</td>
					<td class="crud-content left-align top-align nowrap">
						<html:text property="badgeName" styleId="stackLevelsbadgeNameRow${stackLevelTableCount.index}" value="${stackLevelBean.badgeName}" size="40" maxlength="40"
							styleClass="content-field" /></td>
					<td class="crud-content left-align top-align nowrap">
						<html:textarea property="badgeDescription" styleId="stackLevelsbadgeDescRow${stackLevelTableCount.index}"
							value="${stackLevelBean.badgeDescription}" styleClass="content-field" />
					</td>
					  <td id="checkBoxLevelDiv${stackLevelTableCount.index}">
						<html:hidden property="stackLevelStringRow" styleId="stackLevelStringRow${stackLevelTableCount.index}"/>
					</td>
					<td>
						<input type="hidden" name="badgeRuleId" id="stackLevelsBadgeRuleId${stackLevelTableCount.index}" value="<%=badgeRuleId%>" />
					</td>
			</tr>
			<%
			  lastNodeName = badgeRule.getLevelName();
			 i=i+1;
			%>
		</c:forEach>
		<tr><td><input type="hidden" id="totalTDStackLevelsLength" name="totalTDStackLevelsLength" value="<%=i%>" /></td></tr>
	</table>
	<br/>
   </c:if>

   <!-- Overall -->

   <c:if test="${not empty overallBadges}">
       <table><tr><td><span class="headline"><cms:contentText key="OVERALL_BADGES" code="gamification.admin.labels" /></span></td><tr></table><br />
		<table>
			<thead>
				<tr class="crud-table-row2">

					<th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="NODE_TYPE"/></th>

					<th class="crud-table-header-row"><cms:contentText code="promotion.payout.throwdown" key="STACK_RANK_FROM"/>-<cms:contentText code="promotion.payout.throwdown" key="STACK_RANK_TO"/></th>

					<th class="crud-table-header-row"><cms:contentText
							key="BADGE_LIBRARY" code="gamification.admin.labels" /></th>

					<th class="crud-table-header-row"><cms:contentText
							key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" /></th>

					<th class="crud-table-header-row"><cms:contentText
							key="DESCRIPTION" code="gamification.admin.labels" /></th>

				</tr>
			</thead>
			<tr>
				<td></td>
			</tr>

			<%
			  String overallNodeName = null;
			  String overallLastNodeName = null;
			  int j=0;
			%>

		<c:forEach var="overallLevelBean" items="${overallBadges}" varStatus="overallLevelTableCount">

			<%

			  BadgeRule badgeRule = null;
		      badgeRule = (BadgeRule)pageContext.getAttribute( "overallLevelBean" );
			  String badgeRuleId = badgeRule.getId() + "";
			  overallNodeName = badgeRule.getLevelName();
			  if ( overallNodeName.equals( overallLastNodeName ) )
			  {
				  overallNodeName = "";
			  }
			%>

			<tr class="crud-table-row2">
					<td  class="crud-content left-align top-align nowrap">
					<% if (overallNodeName.length()>0){ %>
						<html:text styleId="overallLevelsNodeNameRow${overallLevelTableCount.index}" property="overallLevelsNodeNameRow" value="<%=overallNodeName%>" size="10" styleClass="content-field" readonly="true" />
					<% } else { %>
						<html:hidden styleId="overallLevelsNodeNameRow${overallLevelTableCount.index}" property="overallLevelsNodeNameRow" value=" " styleClass="content-field" />
					<% } %>
					</td>
					<td class="crud-content left-align top-align nowrap">
						<html:text styleId="overallLevelsNameRow${overallLevelTableCount.index}" property="overallLevelsNameRow" value="${overallLevelBean.minimumQualifier}-${overallLevelBean.maximumQualifier}" size="10" styleClass="content-field" readonly="true" />
					</td>
					<td class="crud-content left-align top-align nowrap">
						<html:hidden property="badgeLibraryId" styleId="overallLevelsBadgeLibraryIdRow${overallLevelTableCount.index}" value="${overallLevelBean.badgeLibraryCMKey}" />
						<html:text property="badgeLibraryId" styleId="overallLevelsBadgeLibraryNameRow${overallLevelTableCount.index}" value="${overallLevelBean.badgeLibDisplayName}" size="17"
							styleClass="content-field" readonly="false" />
					</td>
					<td class="crud-content left-align top-align nowrap">
						<html:text property="badgeName" styleId="overallLevelsBadgeNameRow${overallLevelTableCount.index}" value="${overallLevelBean.badgeName}" size="40" maxlength="40"
							styleClass="content-field" /></td>
					<td class="crud-content left-align top-align nowrap">
						<html:textarea property="badgeDescription" styleId="overallLevelsBadgeDescRow${overallLevelTableCount.index}"
							value="${overallLevelBean.badgeDescription}" styleClass="content-field" />
					</td>
					  <td id="checkBoxLevelDiv${overallLevelTableCount.index}">
						<html:hidden property="overallLevelStringRow" styleId="overallLevelStringRow${overallLevelTableCount.index}"/>
					</td>
					<td>
						<input type="hidden" name="badgeRuleId" id="overallLevelsBadgeRuleId${overallLevelTableCount.index}" value="<%=badgeRuleId%>" />
					</td>
			</tr>
			<%
			overallLastNodeName = badgeRule.getLevelName();
			j=j+1;
			%>
		</c:forEach>
		<tr><td><input type="hidden" id="totalTDOverallLevelsLength" name="totalTDOverallLevelsLength" value="<%=j%>" /></td></tr>
	</table>
	<br/>
   </c:if>


   <!-- Undefeated-->

   <c:if test="${not empty undefeatedBadges}">
       <table><tr><td><span class="headline"><cms:contentText key="UNDEFEATED_BADGES" code="gamification.admin.labels" /></span></td><tr></table><br />
		<table>
			<thead>
				<tr class="crud-table-row2">

					<th class="crud-table-header-row"><cms:contentText
							key="BADGE_LIBRARY" code="gamification.admin.labels" /></th>

					<th class="crud-table-header-row"><cms:contentText
							key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" /></th>

					<th class="crud-table-header-row"><cms:contentText
							key="DESCRIPTION" code="gamification.admin.labels" /></th>

				</tr>
			</thead>
			<tr>
				<td></td>
			</tr>

		<c:forEach var="undefeatedLevelBean" items="${undefeatedBadges}" varStatus="overallLevelTableCount">

			<%
			  BadgeRule badgeRule = null;
		      badgeRule = (BadgeRule)pageContext.getAttribute( "undefeatedLevelBean" );
			  String badgeRuleId = badgeRule.getId() + "";

			%>

			<tr class="crud-table-row2">

					<td class="crud-content left-align top-align nowrap">
						<html:hidden property="badgeLibraryId" styleId="undefeatedBadgeLibraryIdRow" value="${undefeatedLevelBean.badgeLibraryCMKey}" />
						<html:text property="badgeLibraryId" styleId="undefeatedBadgeLibraryNameRow" value="${undefeatedLevelBean.badgeLibDisplayName}" size="17"
							styleClass="content-field" readonly="false" />
					</td>
					<td class="crud-content left-align top-align nowrap">
						<html:text property="badgeName" styleId="undefeatedBadgeNameRow" value="${undefeatedLevelBean.badgeName}" size="40" maxlength="40"
							styleClass="content-field" /></td>
					<td class="crud-content left-align top-align nowrap">
						<html:textarea property="badgeDescription" styleId="undefeatedBadgeDescRow"
							value="${undefeatedLevelBean.badgeDescription}" styleClass="content-field" />
					</td>


					   <td id="undefeatedDiv"><input type="hidden" name="undefeatedBadgeStringRow" id="undefeatedBadgeStringRow"/></td>
						<td><input type="hidden" name="badgeRuleId" id="undefeatedBadgeRuleId" value="<%=badgeRuleId%>" /></td>

			</tr>

		</c:forEach>
	</table>
	<br/>
   </c:if>
</div>

<div id="earnedNotEarnedPartnerBadgeLevelDiv" style="margin-top: 20px;">
	<table>
		<thead>
			<tr class="crud-table-row2">
					<th class="crud-table-header-row"><cms:contentText
						key="PROMOTION_NAME" code="gamification.admin.labels" /></th>
					<th class="crud-table-header-row"><cms:contentText
						key="LEVEL_NAME" code="gamification.admin.labels" /></th>
					<th class="crud-table-header-row"><cms:contentText
						key="PARTNER_BADGE_PICKLIST" code="gamification.admin.labels" /></th>
					<th class="crud-table-header-row"><cms:contentText
						key="PARTNER_BADGE_NAME" code="gamification.admin.labels" /></th>
					<th class="crud-table-header-row"><cms:contentText
						key="PARTNER_BADGE_DESC" code="gamification.admin.labels" /></th>
			</tr>
		</thead>
		<tr>
			<td></td>
		</tr>
		<c:forEach var="levelBean" items="${badgeRuleList}" varStatus="levelTableCount">

				<% BadgeRule badgeRule=null;
					badgeRule=(BadgeRule)pageContext.getAttribute("levelBean");
					String badgeRuleId=badgeRule.getId()+"";
				%>

				<tr class="crud-table-row2">
					<c:if test="${levelBean.participantType == 'PARTNER'}">
						<td  class="crud-content left-align top-align nowrap">
							<html:text property="selectedPromotion" styleClass="content-field" styleId="selectedPromotion" size="40" readonly="true"/>
						</td>
						<td  class="crud-content left-align top-align nowrap">
							<html:text property="levelName" value="${levelBean.levelName}" styleClass="content-field" styleId="LevelName${levelTableCount.index}" size="10" readonly="true"/>
						</td>
						 <td class="crud-content left-align top-align nowrap">
							 <html:hidden property="badgeLibraryId" styleId="levelsPartnerBadgeLibraryIdRow${levelTableCount.index}" value="${levelBean.badgeLibraryCMKey}"/>
							 <html:text property="badgeLibraryId" styleId="levelsBadgeLibraryNameRow${levelTableCount.index}" value="${levelBean.badgeLibDisplayName}" size="17" styleClass="content-field" readonly="false" />
						</td>
						<td  class="crud-content left-align top-align nowrap">
							 <html:text property="badgeName" styleId="levelsPartnerBadgeNameRow${levelTableCount.index}" value="${levelBean.badgeName}" size="40" maxlength="40" styleClass="content-field" />
						 </td>
						 <td  class="crud-content left-align top-align nowrap">
							<html:textarea property="badgeDescription" styleId="levelsPartnerBadgeDescRow${levelTableCount.index}" value="${levelBean.badgeDescription}"  styleClass="content-field" />
						 </td>
						 <td>
						    <input type="hidden" name="badgeRuleId" id="goalLevelsBadgeRuleId${levelTableCount.index}" value="<%=badgeRuleId%>" />
						 </td>
					 </c:if>

				</tr>

		</c:forEach>
	</table>
</div>
	<div id="buttonsDiv">
<%--BUTTON ROWS ... For Input--%>
					<tr class="form-buttonrow">
						<td></td>
						<td></td>
						<td align="left">
							<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
								<html:button property="submitBtn" styleClass="content-buttonstyle" styleId="saveButtonId" onclick="validateBadgeForm('updateBadge')">
									<cms:contentText code="system.button" key="SAVE" />
								</html:button>
				                <html:button property="markAsComplete" styleClass="content-buttonstyle" styleId="saveButtonId" onclick="setActionDispatchAndSubmit('badgeMaintain.do','doExpireBadge')" >
				                  <cms:contentText code="gamification.admin.labels" key="END_BADGE" />
				                </html:button>
							</beacon:authorize> <html:button property="cancelBtn"
								styleClass="content-buttonstyle"
								onclick="callUrl('./badgeList.do')">
								<cms:contentText key="CANCEL" code="system.button" />
							</html:button></td>
					</tr>
					<%--END BUTTON ROW--%>

</div>
</html:form>
<SCRIPT type="text/javascript">

	var billCodesActiveTrueObj = document.getElementById("billCodesActiveTrue");
	var billCodesActiveFalseObj = document.getElementById("billCodesActiveFalse");

	var billCode1Obj = document.getElementById("billCode1");
	var customValue1Obj = $("input[name='customValue1']").val();

	var billCode2Obj = document.getElementById("billCode2");
	var customValue2Obj = document.getElementById("customValue2");

	var billCode3Obj = document.getElementById("billCode3");
	var customValue3Obj = document.getElementById("customValue3");

	var billCode4Obj = document.getElementById("billCode4");
	var customValue4Obj = document.getElementById("customValue4");

	var billCode5Obj = document.getElementById("billCode5");
	var customValue5Obj = document.getElementById("customValue5");

	var billCode6Obj = document.getElementById("billCode6");
	var customValue6Obj = document.getElementById("customValue6");

	var billCode7Obj = document.getElementById("billCode7");
	var customValue7Obj = document.getElementById("customValue7");

	var billCode8Obj = document.getElementById("billCode8");
	var customValue8Obj = document.getElementById("customValue8");

	var billCode9Obj = document.getElementById("billCode9");
	var customValue9Obj = document.getElementById("customValue9");

	var billCode10Obj = document.getElementById("billCode10");
	var customValue10Obj = document.getElementById("customValue10");

	function showOrHideLayers()
	{
		var selectedPromotion = $("#promotionIds").val();

		var badgeTypeObj = $('select[name^="badgeType"]').val();

		if(badgeTypeObj=='-1')
	    {
			$("#badgesBillCodes").hide();
	    }
		else if ( badgeTypeObj=='earned')
		{
			$("#badgesBillCodes").hide();
		}
	    else
	    {
	    	if(badgeTypeObj=='progress' )
	    	{
				$("#badgesBillCodes").show();
	    	}
	    	else if(badgeTypeObj=='behavior')
	    	{
				$("#badgesBillCodes").show();
	    	}
	    	else if(badgeTypeObj=='fileload' && selectedPromotion=='-1')
	    	{
	    	    $("#badgesBillCodes").show();
	    	}
	    	else if(badgeTypeObj=='fileload' && selectedPromotion!='-1')
	    	{
	    		$("#badgesBillCodes").hide();
	    	}
	    }
	}
	showOrHideLayers();

 	function enableFields()
 	{
		if( billCodesActiveFalseObj.checked == true)
      	{
			  $("select[name='billCode1'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode1']").attr('disabled', 'disabled');
	          $("select[name='billCode2'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode2']").attr('disabled', 'disabled');
	          $("select[name='billCode3'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode3']").attr('disabled', 'disabled');
	          $("select[name='billCode4'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode4']").attr('disabled', 'disabled');
	          $("select[name='billCode5'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode5']").attr('disabled', 'disabled');
	          $("select[name='billCode6'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode6']").attr('disabled', 'disabled');
	          $("select[name='billCode7'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode7']").attr('disabled', 'disabled');
	          $("select[name='billCode8'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode8']").attr('disabled', 'disabled');
	          $("select[name='billCode9'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode9']").attr('disabled', 'disabled');
	          $("select[name='billCode10'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode10']").attr('disabled', 'disabled');
	          $("select[name='trackBillCodeBy1'] option[value='']").attr('selected', 'selected');
	          $("select[name='trackBillCodeBy1']").attr('disabled', 'disabled');
	          $("select[name='trackBillCodeBy2'] option[value='']").attr('selected', 'selected');
	          $("select[name='trackBillCodeBy2']").attr('disabled', 'disabled');
	          $("select[name='trackBillCodeBy3'] option[value='']").attr('selected', 'selected');
	          $("select[name='trackBillCodeBy3']").attr('disabled', 'disabled');
	          $("select[name='trackBillCodeBy4'] option[value='']").attr('selected', 'selected');
	          $("select[name='trackBillCodeBy4']").attr('disabled', 'disabled');
	          $("select[name='trackBillCodeBy5'] option[value='']").attr('selected', 'selected');
	          $("select[name='trackBillCodeBy5']").attr('disabled', 'disabled');
	          $("select[name='trackBillCodeBy6'] option[value='']").attr('selected', 'selected');
	          $("select[name='trackBillCodeBy6']").attr('disabled', 'disabled');
	          $("select[name='trackBillCodeBy7'] option[value='']").attr('selected', 'selected');
	          $("select[name='trackBillCodeBy7']").attr('disabled', 'disabled');
	          $("select[name='trackBillCodeBy8'] option[value='']").attr('selected', 'selected');
	          $("select[name='trackBillCodeBy8']").attr('disabled', 'disabled');
	          $("select[name='trackBillCodeBy9'] option[value='']").attr('selected', 'selected');
	          $("select[name='trackBillCodeBy9']").attr('disabled', 'disabled');
	          $("select[name='trackBillCodeBy10'] option[value='']").attr('selected', 'selected');
	          $("select[name='trackBillCodeBy10']").attr('disabled', 'disabled');

	          $("input[name='customValue1']").val("");
	          $("input[name='customValue2']").val("");
	          $("input[name='customValue3']").val("");
	          $("input[name='customValue4']").val("");
	          $("input[name='customValue5']").val("");
	          $("input[name='customValue6']").val("");
	          $("input[name='customValue7']").val("");
	          $("input[name='customValue8']").val("");
	          $("input[name='customValue9']").val("");
	          $("input[name='customValue10']").val("");

	          $("input[name='customValue1']").hide();
			  $("#billCode1Custom").hide();

			  $("input[name='customValue2']").hide();
			  $("#billCode2Custom").hide();

			  $("input[name='customValue3']").hide();
			  $("#billCode3Custom").hide();

			  $("input[name='customValue4']").hide();
			  $("#billCode4Custom").hide();

			  $("input[name='customValue5']").hide();
			  $("#billCode5Custom").hide();

			  $("input[name='customValue6']").hide();
			  $("#billCode6Custom").hide();

			  $("input[name='customValue7']").hide();
			  $("#billCode7Custom").hide();

			  $("input[name='customValue8']").hide();
			  $("#billCode8Custom").hide();

			  $("input[name='customValue9']").hide();
			  $("#billCode9Custom").hide();

			  $("input[name='customValue10']").hide();
			  $("#billCode10Custom").hide();
	  	}
		if( billCodesActiveTrueObj.checked == true)
      	{
			$("select[name='billCode1']").attr('disabled', false);
			$("select[name='billCode2']").attr('disabled', false);
			$("select[name='billCode3']").attr('disabled', false);
			$("select[name='billCode4']").attr('disabled', false);
			$("select[name='billCode5']").attr('disabled', false);
			$("select[name='billCode6']").attr('disabled', false);
			$("select[name='billCode7']").attr('disabled', false);
			$("select[name='billCode8']").attr('disabled', false);
			$("select[name='billCode9']").attr('disabled', false);
			$("select[name='billCode10']").attr('disabled', false);

			$("select[name='trackBillCodeBy1']").attr('disabled', false);
			$("select[name='trackBillCodeBy2']").attr('disabled', false);
			$("select[name='trackBillCodeBy3']").attr('disabled', false);
			$("select[name='trackBillCodeBy4']").attr('disabled', false);
			$("select[name='trackBillCodeBy5']").attr('disabled', false);
			$("select[name='trackBillCodeBy6']").attr('disabled', false);
			$("select[name='trackBillCodeBy7']").attr('disabled', false);
			$("select[name='trackBillCodeBy8']").attr('disabled', false);
			$("select[name='trackBillCodeBy9']").attr('disabled', false);
			$("select[name='trackBillCodeBy10']").attr('disabled', false);
		}
    }
    enableFields();

    function enableBillCode1()
    {
      var billCode1Selected = document.getElementById("billCode1").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode1Selected);
      if(  res )
        {
            document.getElementById('customValue1').style.display = 'inline';
            document.getElementById('customValue1').disabled = false;
            document.getElementById('billCode1Custom').style.display = 'table-row';
            enableBillCode2();
        }
      else
      {
            document.getElementById('customValue1').style.display = 'none';
            document.getElementById('customValue1').disabled = false;
            document.getElementById('billCode1Custom').style.display = 'none';
            enableBillCode2();
        }
    }
     enableBillCode1();

    function enableBillCode2()
    {
       var billCode2Selected = document.getElementById("billCode2").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode2Selected);
      if(  res )
        {
            document.getElementById('customValue2').style.display = 'inline';
            document.getElementById('customValue2').disabled = false;
            document.getElementById('billCode2Custom').style.display = 'table-row';
            enableBillCode3();
        }
      else
      {
            document.getElementById('customValue2').style.display = 'none';
            document.getElementById('customValue2').disabled = false;
            document.getElementById('billCode2Custom').style.display = 'none';
            enableBillCode3();
        }
    }
    enableBillCode2();


    function enableBillCode3()
    {
       var billCode3Selected = document.getElementById("billCode3").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode3Selected);
      if(  res )
        {
            document.getElementById('customValue3').style.display = 'inline';
            document.getElementById('customValue3').disabled = false;
            document.getElementById('billCode3Custom').style.display = 'table-row';
            enableBillCode4();
        }
      else
      {
            document.getElementById('customValue3').style.display = 'none';
            document.getElementById('customValue3').disabled = false;
            document.getElementById('billCode3Custom').style.display = 'none';
            enableBillCode4();
        }
    }
    enableBillCode3();


    function enableBillCode4()
    {
       var billCode4Selected = document.getElementById("billCode4").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode4Selected);
      if(  res )
        {
            document.getElementById('customValue4').style.display = 'inline';
            document.getElementById('customValue4').disabled = false;
            document.getElementById('billCode4Custom').style.display = 'table-row';
            enableBillCode5();
        }
      else
      {
            document.getElementById('customValue4').style.display = 'none';
            document.getElementById('customValue4').disabled = false;
            document.getElementById('billCode4Custom').style.display = 'none';
            enableBillCode5();
        }
    }
    enableBillCode4();

    function enableBillCode5()
    {
       var billCode5Selected = document.getElementById("billCode5").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode5Selected);
      if(  res )
        {
            document.getElementById('customValue5').style.display = 'inline';
            document.getElementById('customValue5').disabled = false;
            document.getElementById('billCode5Custom').style.display = 'table-row';
            enableBillCode6();
        }
      else
      {
            document.getElementById('customValue5').style.display = 'none';
            document.getElementById('customValue5').disabled = false;
            document.getElementById('billCode5Custom').style.display = 'none';
            enableBillCode6();
        }
    }
    enableBillCode5();

    function enableBillCode6()
    {
       var billCode6Selected = document.getElementById("billCode6").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode6Selected);
      if(  res )
        {
            document.getElementById('customValue6').style.display = 'inline';
            document.getElementById('customValue6').disabled = false;
            document.getElementById('billCode6Custom').style.display = 'table-row';
            enableBillCode7();
        }
      else
      {
            document.getElementById('customValue6').style.display = 'none';
            document.getElementById('customValue6').disabled = false;
            document.getElementById('billCode6Custom').style.display = 'none';
            enableBillCode7();
        }
    }
    enableBillCode6();


    function enableBillCode7()
    {
       var billCode7Selected = document.getElementById("billCode7").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode7Selected);
      if(  res )
        {
            document.getElementById('customValue7').style.display = 'inline';
            document.getElementById('customValue7').disabled = false;
            document.getElementById('billCode7Custom').style.display = 'table-row';
            enableBillCode8();
        }
      else
      {
            document.getElementById('customValue7').style.display = 'none';
            document.getElementById('customValue7').disabled = false;
            document.getElementById('billCode7Custom').style.display = 'none';
            enableBillCode8();
        }
    }
    enableBillCode7();

    function enableBillCode8()
    {
       var billCode8Selected = document.getElementById("billCode8").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode8Selected);
      if(  res )
        {
            document.getElementById('customValue8').style.display = 'inline';
            document.getElementById('customValue8').disabled = false;
            document.getElementById('billCode8Custom').style.display = 'table-row';
            enableBillCode9();
        }
      else
      {
            document.getElementById('customValue8').style.display = 'none';
            document.getElementById('customValue8').disabled = false;
            document.getElementById('billCode8Custom').style.display = 'none';
            enableBillCode9();
        }
    }
    enableBillCode8();

    function enableBillCode9()
    {
       var billCode9Selected = document.getElementById("billCode9").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode9Selected);
      if(  res )
        {
            document.getElementById('customValue9').style.display = 'inline';
            document.getElementById('customValue9').disabled = false;
            document.getElementById('billCode9Custom').style.display = 'table-row';
            enableBillCode10();
        }
      else
      {
            document.getElementById('customValue9').style.display = 'none';
            document.getElementById('customValue9').style.disabled = false;
            document.getElementById('billCode9Custom').style.display = 'none';
            enableBillCode10();
        }
    }
    enableBillCode9();


    function enableBillCode10()
    {
       var billCode10Selected = document.getElementById("billCode10").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode10Selected);
      if(  res )
        {
            document.getElementById('customValue10').style.display = 'inline';
            document.getElementById('customValue10').disabled = false;
            document.getElementById('billCode10Custom').style.display = 'table-row';
        }
      else
      {
            document.getElementById('customValue10').style.display = 'none';
            document.getElementById('customValue10').disabled = false;
            document.getElementById('billCode10Custom').style.display = 'none';
        }
    }
    enableBillCode10();

</SCRIPT>