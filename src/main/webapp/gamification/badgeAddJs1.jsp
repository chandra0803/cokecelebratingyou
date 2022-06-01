<%@page import="com.biperf.core.utils.MessageUtils"%>
<%@page import="java.util.Map"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.gamification.BadgeForm"%>
<%@page import="com.biperf.core.domain.gamification.BadgeRule"%>
<%@page import="com.biperf.core.domain.gamification.BadgeBehaviorPromotion"%>

<script type="text/javascript">
var SPLIT_TOKEN = ',';
var REPLACEMENT_TOKEN = '&#664;';
function callUrl(urlToCall) {
	window.location = urlToCall;
}

<%
String fromPage=null;
if(request.getAttribute("fromPage")!=null)
 fromPage=request.getAttribute("fromPage").toString();

String isGoalQuest="";
if(request.getAttribute("isGoalQuest")!=null)
  isGoalQuest=request.getAttribute("isGoalQuest").toString();

String isPointRange="";
if(request.getAttribute("isPointRange")!=null)
  isPointRange=request.getAttribute("isPointRange").toString();

String hasPartners="";
if(request.getAttribute("hasPartners")!=null)
  hasPartners=request.getAttribute("hasPartners").toString();

String isThrowDown="";
if(request.getAttribute("isThrowDown")!=null)
  isThrowDown=request.getAttribute("isThrowDown").toString();

String hasStackStandingPayouts="";
if(request.getAttribute("hasStackStandingPayouts")!=null)
  hasStackStandingPayouts=request.getAttribute("hasStackStandingPayouts").toString();

String editing = "";
if(request.getAttribute( "editing" ) != null)
  editing = request.getAttribute( "editing" ).toString();

String promoIds = null;
   promoIds = (String)request.getAttribute( "promoIds" );
%>


//function to add new row
function addNewRow(type)
{
	var newIndex=0;
	var selectedPromotion = $("#promotionIds").val();
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
					var htmlfileLoad='';
					htmlfileLoad+='<tr class="crud-table-row2">';
					htmlfileLoad+='<td class="crud-content left-align top-align nowrap"><select name="badgeLibraryId" id="fileLoadBadgeLibraryRowId'+totalFileLoadRowsValue+'" class="content-field">';

					for(var bLibIndex=0, lenBLib=objectBadgeLib.length;bLibIndex < lenBLib; bLibIndex++)
			     	{
						htmlfileLoad+='<option value="'+objectBadgeLib[bLibIndex].id+'">'+objectBadgeLib[bLibIndex].name+'</option>';
			     	}
					htmlfileLoad+='</select></td>';
					htmlfileLoad+='<td  class="crud-content left-align top-align nowrap"><input type="text" name="badgeName" id="fileLoadBadgeNameRow'+totalFileLoadRowsValue+'" size="40" maxlength="40" class="content-field" /></td>';
					if(selectedPromotion=='-1' || objectBadge.showFileLoadNoPromoDiv == 'Y')
					{
						htmlfileLoad+='<td  class="crud-content left-align top-align nowrap"><input type="text" name="badgePoints" id="fileLoadBadgePointsRow'+totalFileLoadRowsValue+'" size="10" maxlength="5" class="content-field" /></td>';
						htmlfileLoad+='<td  class="crud-content left-align top-align nowrap"><input type="checkbox" name="eligibleForSweepstake" id="fileLoadBadgeSweepRow'+totalFileLoadRowsValue+'" class="content-field" /></td>';
					}
					htmlfileLoad+='<td  class="crud-content left-align top-align nowrap"><textarea name="badgeDescription" id="fileLoadBadgeDescRow'+totalFileLoadRowsValue+'" class="content-field" /></td>';
					htmlfileLoad+='<td id="fileLoadcheckBoxDiv'+totalFileLoadRowsValue+'"><input type="checkbox" name="fileLoadStringRow" id="fileLoadStringRow'+totalFileLoadRowsValue+'" value="0" /></td>';
					htmlfileLoad+='</tr>';
					if( selectedPromotion=='-1' || objectBadge.showFileLoadNoPromoDiv == 'Y')
					{
						$("#fileLoadForNoPromoDivTable").append(htmlfileLoad);
					}
					else
					{
						$("#fileLoadBadgeDivTable").append(htmlfileLoad);
					}
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
					var htmlProgress='';
					htmlProgress+='<tr class="crud-table-row2">';
					htmlProgress+='<td  class="crud-content left-align top-align nowrap"><input type="text" name="maxQualifier" id="progressMaxQualifier'+totalProgressRowsValue+'" size="12" maxlength="12" class="content-field" /></td>';
					htmlProgress+='<td class="crud-content left-align top-align nowrap"><select name="badgeLibraryId" id="progressBadgeLibraryRowId'+totalProgressRowsValue+'" class="content-field">';
					for(var bLibIndex=0, lenBLib=objectBadgeLib.length;bLibIndex < lenBLib; bLibIndex++)
			     	{
						htmlProgress+='<option value="'+objectBadgeLib[bLibIndex].id+'">'+objectBadgeLib[bLibIndex].name+'</option>';
			     	}
					htmlProgress+='</select></td>';
					htmlProgress+='<td  class="crud-content left-align top-align nowrap"><input type="text" name="badgeName" id="progressBadgeNameRow'+totalProgressRowsValue+'" size="40" maxlength="40" class="content-field" /></td>';
					htmlProgress+='<td  class="crud-content left-align top-align nowrap"><input type="text" name="badgePoints" id="progressBadgePointsRow'+totalProgressRowsValue+'" size="10" maxlength="5" class="content-field" /></td>';
					htmlProgress+='<td  class="crud-content left-align top-align nowrap"><input type="checkbox" name="eligibleForSweepstake" id="progressBadgeSweepRow'+totalProgressRowsValue+'" class="content-field rdochk" /></td>';
					htmlProgress+='<td  class="crud-content left-align top-align nowrap"><textarea name="badgeDescription" id="progressBadgeDescRow'+totalProgressRowsValue+'" class="content-field" /></td>';
					htmlProgress+='<td id="progresscheckBoxDiv'+totalProgressRowsValue+'"><input type="checkbox" name="progressStringRow" id="progressStringRow'+totalProgressRowsValue+'" value="0" /></td>';
					htmlProgress+='</tr>';
					 $("#progressBadgeDivTable").append(htmlProgress);
					 for(var progIndex=0;progIndex<newIndex;progIndex++)
					 {
							  	$("#progresscheckBoxDiv"+progIndex).hide();
					 }
				}
			}
		});
	}
	else if(type=='pointRange')
	{
		var totalPointRangeRowsValue=document.getElementById("currentPointRangeTableSize").value;
		//console.log('totalPointRangeRowsValue:'+totalPointRangeRowsValue);
		newIndex=parseInt(totalPointRangeRowsValue)+1;
		document.getElementById("currentPointRangeTableSize").value=newIndex;
		//var indexElements=parseInt(newIndex)-1;
		//console.log('indexElements:'+indexElements);
		$.ajax({
			url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=populateBadgeLibraryAddRow&badgeType="+type+"&promotionIds="+selectedPromotion,
			dataType: 'application/json',
			success: function(data){
				if(data != null && data != '' && data != 'null')
				{
					var objectBadge = jQuery.parseJSON(data);
					var objectBadgeLib = objectBadge.badgeLibraryList;
					var htmlPointRange='';
					htmlPointRange+='<tr class="crud-table-row2">';
					htmlPointRange+='<td  class="crud-content left-align top-align nowrap" colspan="2"><input type="text" name="rangeAmountMin" id="rangeAmountMin'+totalPointRangeRowsValue+'" size="5" maxlength="12" class="content-field" />&nbsp;To &nbsp;';
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
					htmlPointRange+='</tr>';
					 $("#earnedNotEarnedBadgePointDivTable").append(htmlPointRange);
					 for(var pointIndex=0;pointIndex<newIndex;pointIndex++)
					 {
							  	$("#pointcheckBoxDiv"+pointIndex).hide();
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
function validateFields( promotionType )
{
	var startDate=$("#startDate").val();

	if( startDate =='' )
	{
		errorMessage='<cms:contentText key="FROM_DATE_REQUIRED" code="report.errors" />';
		 $("#errorShowDiv").html(errorMessage);
		 $("#errorShowDiv").show();
		 return false;
	}
	else
	{
		$("#errorShowDiv").hide();
	}


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
 		else if(billCodeMissing == true)
    	{
        	errorMessage='<cms:contentText key="CUSTOM_BILL_CODE" code="promotion.bill.code" />';
    		$("#errorShowDiv").html(errorMessage);
    		$("#errorShowDiv").show();
    		return false;
        }
 	}

	var selectedBadgeType = $("#badgeTypeId").val();
    var val = [];
    var individualValueArray=[];
    var totalProgressRowsValue=$("#totalProgressRows").val();
    var errorMessage='';
    var badgeNames = [];
    var totRows=0;

    if(selectedBadgeType=='-1')
    {
    	 errorMessage='<cms:contentText key="VALID_BADGE_TYPE" code="gamification.validation.messages" />';
		 $("#errorShowDiv").html(errorMessage);
		 $("#errorShowDiv").show();
		 return false;
    }
    else
	{
		$("#errorShowDiv").hide();
	}

	if(selectedBadgeType=='progress')
	{
		//validate all the fields for progress
		var badgeCountType=$("#badgeCountType").val();
		if(badgeCountType=='-1')
		{
			 errorMessage='<cms:contentText key="VALID_BADGE_COUNT_TYPE" code="gamification.validation.messages" />';
			 $("#errorShowDiv").html(errorMessage);
			 $("#errorShowDiv").show();
			 return false;
		}
		else
		{
			$("#errorShowDiv").hide();
		}
		var checked = $("input[name=progressStringRow]:checked").length > 0;
		//alert('c'+checked)
		if (!checked){

			 errorMessage='<cms:contentText key="BADGE_LIB_ATLEAST_ONE" code="gamification.validation.messages" />';
			 $("#errorShowDiv").html(errorMessage);
			 $("#errorShowDiv").show();
			 return false;
		}

		$("input[name=progressStringRow]:checked").each(function(i)
		{
		      val[i] = $(this).val();
		      individualValueArray = val[i].split(SPLIT_TOKEN);
		      if(individualValueArray[1]=='0')
		      {
		    	  errorMessage='<cms:contentText key="VALID_BADGE_LEVEL" code="gamification.validation.messages" />';
				  $("#errorShowDiv").html(errorMessage);
				  $("#errorShowDiv").show();
				  return false;
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
		var checked = $("input[name=fileLoadStringRow]:checked").length > 0;
		var selectedPromotion = $("#promotionIds").val();
		if (!checked){
			 errorMessage='<cms:contentText key="BADGE_LIB_ATLEAST_ONE" code="gamification.validation.messages" />';
			 $("#errorShowDiv").html(errorMessage);
			 $("#errorShowDiv").show();
			 return false;
		}

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
		    	  if(individualValueArray[3] != 'null' && individualValueArray[3]<=0)
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
		//validate all the fields for progress

		var checked = $("input[name=behaviorStringRow]:checked").length > 0;
		//alert(checked);
		if (!checked){
			errorMessage='<cms:contentText key="BADGE_LIB_ATLEAST_ONE" code="gamification.validation.messages" />';
			 $("#errorShowDiv").html(errorMessage);
			 $("#errorShowDiv").show();
			 return false;
		}

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
				  errorMessage='please enter valid number for behavior points';
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
		if(document.getElementById("totalLevelsLength")!=null && document.getElementById("totalLevelsLength")!='undefined'&&document.getElementById("totalLevelsLength")!='')
		{
				var checkedLevel = $("input[name=levelStringRow]:checked").length > 0;
				if (!checkedLevel){
					 errorMessage='<cms:contentText key="BADGE_LIB_ATLEAST_ONE" code="gamification.validation.messages" />';
					 $("#errorShowDiv").html(errorMessage);
					 $("#errorShowDiv").show();
					 return false;
				}

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
		else if(document.getElementById("totalGQLevelsLength")!=null && document.getElementById("totalGQLevelsLength")!='undefined'&&document.getElementById("totalGQLevelsLength")!='')
		{
				var checkedLevel = $("input[name=levelStringRow]:checked").length > 0;

				if (!checkedLevel){
					 errorMessage='<cms:contentText key="BADGE_LIB_ATLEAST_ONE" code="gamification.validation.messages" />';
					 $("#errorShowDiv").html(errorMessage);
					 $("#errorShowDiv").show();
					 return false;
				}

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
		else if(document.getElementById("totalTDStackLevelsLength")!=null && document.getElementById("totalTDStackLevelsLength")!='undefined'&&document.getElementById("totalTDStackLevelsLength")!='')
		{
				var stackLevel = '-1';
				var overallLevel = '-1';
				var undefeatedLevel;

				if(document.getElementById("undefeatedBadge").checked)
			    {
					undefeatedLevel = $("#undefeatedBadgeLibraryIdRow").val()
			    }

				var totalStackLevel = parseInt(document.getElementById("totalTDStackLevelsLength").value);

				for(var i=0;i<totalStackLevel;i++)
			    {

					if($("#stackLevelsBadgeLibraryIdRow"+i).val() != '-1')
					{
						stackLevel = '1';
					}

					if($("#overallLevelsBadgeLibraryIdRow"+i).val() != '-1')
					{
						overallLevel = '1';
					}

				}

			     if(stackLevel=='-1' && overallLevel=='-1' && !document.getElementById("undefeatedBadge").checked )
				 {
						 errorMessage='<cms:contentText key="BADGE_LIB_ATLEAST_ONE" code="gamification.validation.messages" />';
						 $("#errorShowDiv").html(errorMessage);
						 $("#errorShowDiv").show();
						 return false;
			      }

			    else if(stackLevel=='-1' && overallLevel=='-1' && (document.getElementById("undefeatedBadge").checked &&  undefeatedLevel=='-1'))
			    {
					 errorMessage='<cms:contentText key="BADGE_LIB_ATLEAST_ONE" code="gamification.validation.messages" />';
					 $("#errorShowDiv").html(errorMessage);
					 $("#errorShowDiv").show();
				  	 return false;

			    }
				else
				{
			      if(stackLevel=='1')
				  {
			    	  for(var i=0;i<totalStackLevel;i++)
					    {
			    		 if($("#stackLevelsBadgeLibraryIdRow"+i).val() != '-1')
			    		 {
			    		  if($("#stackLevelsBadgeNameRow"+i).val()=='0')
					      {
					    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
							  $("#errorShowDiv").html(errorMessage);
							  $("#errorShowDiv").show();
							  return false;
					      }
					      else if($("#stackLevelsBadgeDescRow"+i).val().length>200)
					      {
					    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
							  $("#errorShowDiv").html(errorMessage);
							  $("#errorShowDiv").show();
							  return false;
					      }

			    		  badgeNames[totRows]=$("#stackLevelsBadgeNameRow"+i).val();
			    		  //alert(badgeNames[totRows]);
					      totRows++;

			    		 }


						}

				   }//stacklevel


				   if(overallLevel=='1')
					  {
				    	  for(var i=0;i<totalStackLevel;i++)
						    {
				    		  if($("#overallLevelsBadgeLibraryIdRow"+i).val() != '-1')
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
				    		  //alert(badgeNames[totRows]);
						      totRows++;

							  }

							}//for

					   }//overalllevel

		      if(document.getElementById("undefeatedBadge").checked)
		       {
				if($("#undefeatedBadgeLibraryIdRow").val()!='-1')
				{

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
				}//undefeated
		       }
			}//else


		}//totalStackLevel	else
		else if ( pointRange!=null && pointRange=='N' && isGoalQuest=='Y' &&  ( $('#gqBadgePromotionStatus').val() != null &&
					 															$('#gqBadgePromotionStatus').val() !='undefined' &&
  					  															$('#gqBadgePromotionStatus').val() == 'under_construction' ) )
		{
			var checkedLevel = $("input[name=levelStringRow]:checked").length > 0;
			if (!checkedLevel){
				 errorMessage='<cms:contentText key="BADGE_LIB_ATLEAST_ONE" code="gamification.validation.messages" />';
				 $("#errorShowDiv").html(errorMessage);
				 $("#errorShowDiv").show();
				 return false;
			}

			$("input[name=levelStringRow]:checked").each(function(m)
			{
			    val[m] = $(this).val();
			    individualValueArray = val[m].split(SPLIT_TOKEN);
			    if( individualValueArray[3] == 'none' )
			    {
			      if(individualValueArray[5] =='0')
			      {
			    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
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
			    }
			    else if ( individualValueArray[3] == 'partner' )
			    {
			    	if(individualValueArray[8]=='0')
				      {
				    	  errorMessage='<cms:contentText key="VALID_BADGE_DISPLAY_NAME" code="gamification.validation.messages" />';
						  $("#errorShowDiv").html(errorMessage);
						  $("#errorShowDiv").show();
						  return false;
				      }
				      else if(individualValueArray[9].length>200)
				      {
				    	  errorMessage='<cms:contentText key="VALID_DESCRIPTION" code="gamification.validation.messages" />';
						  $("#errorShowDiv").html(errorMessage);
						  $("#errorShowDiv").show();
						  return false;
				      }
			    }

			      badgeNames[totRows]=individualValueArray[4];
			      totRows++;
			 });

			//to block duplicate badge name check as this is edit.
			if(errorMessage=='')
				return true;
		}
		else
		{

			var checkedPoint = $("input[name=pointRangeStringRow]:checked").length > 0;
			if (!checkedPoint){
				 errorMessage='<cms:contentText key="BADGE_LIB_ATLEAST_ONE" code="gamification.validation.messages" />';
				 $("#errorShowDiv").html(errorMessage);
				 $("#errorShowDiv").show();
				 return false;
			}

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
		return true;
	else
		return false;
}


function setAllValues(method, promotionType)
{
	var selectedValue = $("#badgeTypeId").val();
	var selectedPromotion = $("#promotionIds").val();
	var commaIndex=0;
	if(selectedValue=='behavior')
	{
			var behaviorName;
			var behaviorCode;
			var badgeLibSelected;
			var badgeName;
			var badgePoints;
			var badgeSweep;
			var badgeDesc;
			var checkBoxBehaviorRow='';
			var totalbehaviors = $('#behaviorBadgeTableId tbody tr.crud-table-row2').length;//document.getElementById("totalBehaviorsLength").value;
			var totalbehaviorsOnEditPage= '${behaviorListSize}';
			/* if( totalbehaviorsOnEditPage > 0)
			{
			totalbehaviors = totalbehaviorsOnEditPage -1;
			} */
			for(var beh=0; beh<totalbehaviors; beh++)
			{
				commaIndex=0;
				badgeRuleId=$("#behaviorBadgeRuleId"+beh).val();
				behaviorCode=$("#behaviorCode"+beh).val();
				behaviorName=$("#behaviorCode"+beh).val();
				badgeLibSelected=$("#badgeLibraryIdRow"+beh).val();
				badgeName=$("#behaviorbadgeNameRow"+beh).val();
				badgePoints=$("#behaviorbadgePointsRow"+beh).val();
				badgeSweep=$("#behaviorBadgeSweepRow"+beh).is(':checked');
				badgeDesc=$("#behaviorbadgeDescRow"+beh).val();
				if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
					badgeRuleId='0';
				if(badgeName==null|| badgeName=='' || badgeName=='null' )
					badgeName='0';
				if(badgeDesc==null|| badgeDesc=='' || badgeDesc=='null' )
					badgeDesc='0';
				if(badgePoints==null|| badgePoints=='' || badgePoints=='null' )
					badgePoints='null';
				badgeName=trim(badgeName);
				for(var i=0;i<badgeName.length;i++)
				{
					if(badgeName.charAt(i)==SPLIT_TOKEN)
					{
						commaIndex++;
						break;
					}
				}
				badgeLibSelected=badgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
				if(commaIndex>0)
				{
					badgeName=badgeName.replace(/\,/g,REPLACEMENT_TOKEN);
				}
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
	else if(selectedValue=='earned')
	{
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
			var badgeRuleId;
			var pointRange="<%=isPointRange%>";
			var isGoalQuest="<%=isGoalQuest%>";
			var hasPartners="<%=hasPartners%>";
			if(document.getElementById("totalLevelsLength")!=null && document.getElementById("totalLevelsLength")!='undefined')
			{
					var totalLevels=document.getElementById("totalLevelsLength").value;
					for(var lev=0;lev<totalLevels;lev++)
					{
							commaIndex=0;
							badgeRuleId =$("#badgeRuleId"+pIdx).val();
							countryId=$("#countryRow"+lev).val();
							levelName=$("#levelsNameRow"+lev).val();
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
							//commaIndex=levelBadgeName.indexOf(SPLIT_TOKEN);
							levelBadgeName=trim(levelBadgeName);
							for(var i=0;i<levelBadgeName.length;i++)
							{
								if(levelBadgeName.charAt(i)==SPLIT_TOKEN)
								{
									commaIndex++;
									break;
								}
							}
				            badgeLibSelected=badgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
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
			else if(document.getElementById("totalGQLevelsLength")!=null && document.getElementById("totalGQLevelsLength")!='undefined')
			{
				var totalLevels=document.getElementById("totalGQLevelsLength").value;
				for(var lev=0;lev<totalLevels;lev++)
				{
						commaIndex=0;
						badgeRuleId = "new";
						countryId=$("#countryRow"+lev).val();
						if(countryId==null || countryId=='undefined'){
							countryId='0';
						}
						levelName = document.getElementById("levelsNameRow"+lev).textContent;
						badgeLibSelected=$("#levelsBadgeLibraryIdRow"+lev).val();
						levelBadgeName=$("#levelsBadgeNameRow"+lev).val();
						levelBadgeDesc=$("#levelsBadgeDescRow"+lev).val();
						if(levelBadgeName==null|| levelBadgeName=='' || levelBadgeName=='null' )
							levelBadgeName='0';
						if(levelBadgeDesc==null|| levelBadgeDesc=='' || levelBadgeDesc=='null' )
						{
								levelBadgeDesc='0';
						}
						//commaIndex=levelBadgeName.indexOf(SPLIT_TOKEN);
						levelBadgeName=trim(levelBadgeName);
						for(var i=0;i<levelBadgeName.length;i++)
						{
							if(levelBadgeName.charAt(i)==SPLIT_TOKEN)
							{
								commaIndex++;
								break;
							}
						}
				        badgeLibSelected=badgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
						if(commaIndex>0)
						{
							levelBadgeName=levelBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
						}

						levelBadgeDesc=levelBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);

						partnerBadgeLibSelected=$("#levelsPartnerBadgeLibraryIdRow"+lev).val();
						partnerLevelBadgeName=$("#levelsPartnerBadgeNameRow"+lev).val();
						partnerLevelBadgeDesc=$("#levelsPartnerBadgeDescRow"+lev).val();
						if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
							badgeRuleId='0';
						if(partnerLevelBadgeName==null|| partnerLevelBadgeName=='' || partnerLevelBadgeName=='null' )
							partnerLevelBadgeName='0';
						if(partnerLevelBadgeDesc==null|| partnerLevelBadgeDesc=='' || partnerLevelBadgeDesc=='null' )
						{
							partnerLevelBadgeDesc='0';
						}
						//commaIndex=partnerLevelBadgeName.indexOf(SPLIT_TOKEN);
						partnerLevelBadgeName=trim(partnerLevelBadgeName);
						for(var i=0;i<partnerLevelBadgeName.length;i++)
						{
							if(partnerLevelBadgeName.charAt(i)==SPLIT_TOKEN)
							{
								commaIndex++;
								break;
							}
						}
    				    partnerBadgeLibSelected=partnerBadgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
						if(commaIndex>0)
						{
							partnerLevelBadgeName=partnerLevelBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
						}

						partnerLevelBadgeDesc=partnerLevelBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);

						checkBoxLevelRow = badgeRuleId+SPLIT_TOKEN+countryId+SPLIT_TOKEN+levelName+SPLIT_TOKEN+badgeLibSelected+SPLIT_TOKEN+levelBadgeName+SPLIT_TOKEN+levelBadgeDesc
										   +SPLIT_TOKEN+partnerBadgeLibSelected+SPLIT_TOKEN+partnerLevelBadgeName+SPLIT_TOKEN+partnerLevelBadgeDesc;
								//alert('checkbox row valuegoing to set as :'+checkBoxLevelRow);

						document.getElementById("levelStringRow"+lev).value=checkBoxLevelRow;
						if(badgeLibSelected!='-1')
							document.getElementById("levelStringRow"+lev).checked=true;
						else
							document.getElementById("levelStringRow"+lev).checked=false;
				}
			}
			else if(document.getElementById("totalTDStackLevelsLength")!=null && document.getElementById("totalTDStackLevelsLength")!='undefined')
			{
				var totalLevels=document.getElementById("totalTDStackLevelsLength").value;
				for(var lev=0;lev<totalLevels;lev++)
				{
						commaIndex=0;
						badgeRuleId =$("#badgeRuleId"+pIdx).val();
						if($("#stackLevelsBadgeLibraryIdRow"+lev).val()!='-1')
						{
						countryId=$("#countryRow"+lev).val();
						if(countryId==null || countryId=='undefined'){
							countryId='0';
						}
						nodeName = document.getElementById("stackLevelsNodeNameRow"+lev).textContent;
						levelName = document.getElementById("stackLevelsNameRow"+lev).textContent;
						var prevLev = lev;
                        while ( prevLev != 0 && nodeName != null && trim(nodeName) == '' )
                        {
							prevLev = prevLev - 1;
                            nodeName = document.getElementById("stackLevelsNodeNameRow"+prevLev).textContent;
                        }
						badgeLibSelected=$("#stackLevelsBadgeLibraryIdRow"+lev).val();
						levelBadgeName=$("#stackLevelsBadgeNameRow"+lev).val();
						levelBadgeDesc=$("#stackLevelsBadgeDescRow"+lev).val();
						if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
							badgeRuleId='0';
						if(levelBadgeName==null|| levelBadgeName=='' || levelBadgeName=='null' )
							levelBadgeName='0';
						if(levelBadgeDesc==null|| levelBadgeDesc=='' || levelBadgeDesc=='null' )
						{
							levelBadgeDesc='0';
						}
						//commaIndex=levelBadgeName.indexOf(SPLIT_TOKEN);
						levelBadgeName=trim(levelBadgeName);
						for(var i=0;i<levelBadgeName.length;i++)
						{
							if(levelBadgeName.charAt(i)==SPLIT_TOKEN)
							{
								commaIndex++;
								break;
							}
						}
			            badgeLibSelected=badgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
						if(commaIndex>0)
						{
							levelBadgeName=levelBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
						}

						levelBadgeDesc=levelBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);

						checkBoxLevelRow = badgeRuleId+SPLIT_TOKEN+countryId+SPLIT_TOKEN+nodeName+SPLIT_TOKEN+levelName+SPLIT_TOKEN+badgeLibSelected+SPLIT_TOKEN+levelBadgeName+SPLIT_TOKEN+levelBadgeDesc;
						//alert('checkbox row valuegoing to set as :'+checkBoxLevelRow);

						document.getElementById("stackLevelStringRow"+lev).value=checkBoxLevelRow;
						}

				}

				//Overall Badge Setup

				var totalOverallLevels=document.getElementById("totalTDOverallLevelsLength").value;
				for(var lev=0;lev<totalOverallLevels;lev++)
				{
						commaIndex=0;
						badgeRuleId =$("#badgeRuleId"+pIdx).val();
						if($("#overallLevelsBadgeLibraryIdRow"+lev).val()!='-1')
						{
						var overallcountryId=$("#countryRow"+lev).val();
						if(overallcountryId==null || overallcountryId=='undefined')
						{
							overallcountryId='0';
						}
						var overallnodeName = document.getElementById("overallLevelsNodeNameRow"+lev).textContent;
						var overalllevelName = document.getElementById("overallLevelsNameRow"+lev).textContent;
						var prevLev = lev;
						while ( prevLev != 0 && overallnodeName != null && trim(overallnodeName) == '' )
                        {
							prevLev = prevLev - 1;
                            overallnodeName = document.getElementById("overallLevelsNodeNameRow"+prevLev).textContent;
                        }
						var overallbadgeLibSelected=$("#overallLevelsBadgeLibraryIdRow"+lev).val();
						var overalllevelBadgeName=$("#overallLevelsBadgeNameRow"+lev).val();
						var overalllevelBadgeDesc=$("#overallLevelsBadgeDescRow"+lev).val();
						if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
							badgeRuleId='0';
						if(overalllevelBadgeName==null|| overalllevelBadgeName=='' || overalllevelBadgeName=='null' )
							overalllevelBadgeName='0';
						if(overalllevelBadgeDesc==null|| overalllevelBadgeDesc=='' || overalllevelBadgeDesc=='null' )
						{
							overalllevelBadgeDesc='0';
						}
						//commaIndex=levelBadgeName.indexOf(SPLIT_TOKEN);
						overalllevelBadgeName=trim(overalllevelBadgeName);
						for(var i=0;i<overalllevelBadgeName.length;i++)
						{
							if(overalllevelBadgeName.charAt(i)==SPLIT_TOKEN)
							{
								commaIndex++;
								break;
							}
						}
			            overallbadgeLibSelected=overallbadgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
						if(commaIndex>0)
						{
							overalllevelBadgeName=overalllevelBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
						}

						overalllevelBadgeDesc=overalllevelBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);

						var overallcheckBoxLevelRow = badgeRuleId+SPLIT_TOKEN+overallcountryId+SPLIT_TOKEN+overallnodeName+SPLIT_TOKEN+overalllevelName+SPLIT_TOKEN+overallbadgeLibSelected+SPLIT_TOKEN+overalllevelBadgeName+SPLIT_TOKEN+overalllevelBadgeDesc;
						//alert('checkbox row valuegoing to set as :'+checkBoxLevelRow);

						document.getElementById("overallLevelStringRow"+lev).value=overallcheckBoxLevelRow;
						}
				 }

				if(document.getElementById("undefeatedBadge").checked && $("#undefeatedBadgeLibraryIdRow").val()!='-1')
				{
					     //alert("undefeatedBadge is checked");
					     commaIndex=0;
					     badgeRuleId =$("#badgeRuleId"+pIdx).val();
					     var undefeatedcountryId=$("#countryRow"+lev).val();
					     if(undefeatedcountryId==null || undefeatedcountryId=='undefined')
					     {
					    	 undefeatedcountryId='0';
					     }
							//var undefeatedBadgelevelName = "UndefeatedBadge";
							var undefeatedBadgeLibSelected=$("#undefeatedBadgeLibraryIdRow").val();
							var undefeatedBadgeName=$("#undefeatedBadgeNameRow").val();
							var undefeatedBadgeDesc=$("#undefeatedBadgeDescRow").val();

							if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
								badgeRuleId='0';

							if(undefeatedBadgeName==null|| undefeatedBadgeName=='' || undefeatedBadgeName=='null' )
								undefeatedBadgeName='0';
							if(undefeatedBadgeDesc==null|| undefeatedBadgeDesc=='' || undefeatedBadgeDesc=='null' )
							{
								undefeatedBadgeDesc='0';
							}
							//commaIndex=levelBadgeName.indexOf(SPLIT_TOKEN);
							undefeatedBadgeName=trim(undefeatedBadgeName);
							for(var i=0;i<undefeatedBadgeName.length;i++)
							{
								if(undefeatedBadgeName.charAt(i)==SPLIT_TOKEN)
								{
									commaIndex++;
									break;
								}
							}
        				    undefeatedBadgeLibSelected=undefeatedBadgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
							if(commaIndex>0)
							{
								undefeatedBadgeName=undefeatedBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
							}

							undefeatedBadgeDesc=undefeatedBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);
							//alert("badge level"+undefeatedBadgelevelName);
							//alert("badge lib"+undefeatedBadgeLibSelected);
							//alert("undefeatedBadgeName"+undefeatedBadgeName);
							//alert("undefeatedBadgeDesc"+undefeatedBadgeDesc);
							var undefeatedcheckBoxLevelRow = badgeRuleId+SPLIT_TOKEN+undefeatedcountryId+SPLIT_TOKEN+undefeatedBadgeLibSelected+SPLIT_TOKEN+undefeatedBadgeName+SPLIT_TOKEN+undefeatedBadgeDesc;

							document.getElementById("undefeatedBadgeStringRow").value=undefeatedcheckBoxLevelRow;

				}
			}
			else if( pointRange!=null && pointRange=='N' && isGoalQuest=='Y' && ( $('#gqBadgePromotionStatus').val() != null &&
																				  $('#gqBadgePromotionStatus').val() !='undefined' &&
																				  $('#gqBadgePromotionStatus').val() == 'under_construction' ) )
			{
			var totalLevels = document.getElementById("gqBadgeRuleListSize").value;
			for (var lev = 0; lev <= totalLevels; lev++)
			  {

				var goalOrPartner;
				var goalLevelsBadgeRuleId = $("#goalLevelsBadgeRuleId" + lev).val();
				if( goalLevelsBadgeRuleId == null || goalLevelsBadgeRuleId == '' || goalLevelsBadgeRuleId == 'null' || goalLevelsBadgeRuleId == 'undefined'  )
					goalOrPartner = "partner";
				else
					goalOrPartner = "none";

			    commaIndex = 0;
			    badgeRuleId = "edit";
			    countryId = $("#countryRow" + lev).val();
			    if (countryId == null ||  countryId=='' || countryId == 'undefined') {
			        countryId = '0';
			    }
			    if( goalOrPartner == 'none' )
			    {
			    levelName = document.getElementById("goalLevelsNameRow" + lev).value;
			    badgeLibSelected = $("#goalLevelsBadgeLibraryIdRow" + lev).val();
			    levelBadgeName = $("#goalLevelsbadgeNameRow" + lev).val();
			    levelBadgeDesc = $("#goalLevelsbadgeDescRow" + lev).val();

			    if (levelBadgeName == null || levelBadgeName == '' || levelBadgeName == 'null')
			        levelBadgeName = '0';
			    if (levelBadgeDesc == null || levelBadgeDesc == '' || levelBadgeDesc == 'null') {
			        levelBadgeDesc = '0';
			    }
			    levelBadgeName = trim(levelBadgeName);
			    for (var i = 0; i < levelBadgeName.length; i++) {
			        if (levelBadgeName.charAt(i) == SPLIT_TOKEN) {
			            commaIndex++;
			            break;
			        }
			    }

			    if( badgeLibSelected.length > 0 )
			    	badgeLibSelected = badgeLibSelected.replace(/\,/g, REPLACEMENT_TOKEN);
			    else
			    	badgeLibSelected == '-1';

			    if (commaIndex > 0) {
			        levelBadgeName = levelBadgeName.replace(/\,/g, REPLACEMENT_TOKEN);
			    }
			    levelBadgeDesc = levelBadgeDesc.replace(/\,/g, REPLACEMENT_TOKEN);
			    }
			    else
		    	{
			      levelName = document.getElementById("levelsNameRow" + lev).value;
			      badgeLibSelected = '0';
			      levelBadgeName = '0';
			      levelBadgeDesc = '0';
		    	}
			    //for partner
			    var partnerBadgeLibSelected ='0';
			    var partnerLevelBadgeName = '0';
			    var partnerLevelBadgeDesc = '0';

			    if( goalOrPartner == 'partner' )
			    {
				    partnerBadgeLibSelected = $("#levelsPartnerBadgeLibraryIdRow" + lev).val();
				    partnerLevelBadgeName = $("#levelsPartnerBadgeNameRow" + lev).val();
				    partnerLevelBadgeDesc = $("#levelsPartnerBadgeDescRow" + lev).val();
				    if (badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
				        badgeRuleId = '0';
				    if (partnerLevelBadgeName == null || partnerLevelBadgeName == '' || partnerLevelBadgeName == 'null')
				        partnerLevelBadgeName = '0';
				    if (partnerLevelBadgeDesc == null || partnerLevelBadgeDesc == '' || partnerLevelBadgeDesc == 'null') {
				        partnerLevelBadgeDesc = '0';
				    }
				    //commaIndex=partnerLevelBadgeName.indexOf(",");
				    partnerLevelBadgeName = trim(partnerLevelBadgeName);
				    for (var i = 0; i < partnerLevelBadgeName.length; i++) {
				        if (partnerLevelBadgeName.charAt(i) == SPLIT_TOKEN) {
				            commaIndex++;
				            break;
				        }
				    }

				    if( partnerBadgeLibSelected.length > 0 )
				    	partnerBadgeLibSelected = partnerBadgeLibSelected.replace(/\,/g, REPLACEMENT_TOKEN);
				    else
				    	partnerBadgeLibSelected == '-1';

				    if (commaIndex > 0) {
				        partnerLevelBadgeName = partnerLevelBadgeName.replace(/\,/g, REPLACEMENT_TOKEN);
				    }

				    partnerLevelBadgeDesc = partnerLevelBadgeDesc.replace(/\,/g, REPLACEMENT_TOKEN);
			    }

			    checkBoxLevelRow = badgeRuleId + SPLIT_TOKEN + countryId + SPLIT_TOKEN + levelName + SPLIT_TOKEN + goalOrPartner + SPLIT_TOKEN + badgeLibSelected + SPLIT_TOKEN + levelBadgeName + SPLIT_TOKEN + levelBadgeDesc + SPLIT_TOKEN + partnerBadgeLibSelected + SPLIT_TOKEN + partnerLevelBadgeName + SPLIT_TOKEN + partnerLevelBadgeDesc;
			    document.getElementById("levelStringRow" + lev).value = checkBoxLevelRow;

			    if ( partnerBadgeLibSelected == '-1' && badgeLibSelected == '-1')
			        document.getElementById("levelStringRow" + lev).checked = false;
			    else
			        document.getElementById("levelStringRow" + lev).checked = true;

			  }

			}
			else
			{
				var totalRowsCount=$("#currentPointRangeTableSize").val();
				for(var pIdx=0;pIdx<totalRowsCount;pIdx++)
				{
					 commaIndex=0;
					 badgeRuleId =$("#badgeRuleId"+pIdx).val();
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

					 //commaIndex=pointBadgeName.indexOf(SPLIT_TOKEN);
					 pointBadgeName=trim(pointBadgeName);
					 for(var i=0;i<pointBadgeName.length;i++)
					 {
						if(pointBadgeName.charAt(i)==SPLIT_TOKEN)
						{
							commaIndex++;
							break;
						}
					 }
				     pointBadgeLibSelected=pointBadgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
					 if(commaIndex>0)
					 {
						pointBadgeName=pointBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
					 }
					 pointBadgeDesc=pointBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);
					 pointcheckBoxRow=badgeRuleId+SPLIT_TOKEN+pointMinRange+SPLIT_TOKEN+pointMaxRange+SPLIT_TOKEN+pointBadgeLibSelected+SPLIT_TOKEN+pointBadgeName+SPLIT_TOKEN+pointBadgeDesc;
					 //alert('checkbox row valuegoing to set as :'+pointcheckBoxRow);

					 document.getElementById("pointRangeStringRow"+pIdx).value=pointcheckBoxRow;
					 if(pointBadgeLibSelected!='-1')
						document.getElementById("pointRangeStringRow"+pIdx).checked=true;
					 else
						document.getElementById("pointRangeStringRow"+pIdx).checked=false;
				}

			}

	 }
	 else if(selectedValue=='progress')
	 {
	   	      var maxQualifier;
			  var progressBadgeLibSelected;
			  var progressBadgeName;
			  var progressBadgePoints;
			  var progressBadgeDesc;
			  var progressBadgeSweep
			  var progresscheckBoxRow='';
			  var badgeRuleId;
			  var totalRowsCount=$("#currentProgressTableSize").val();
  			  for(var pIdx=0;pIdx<totalRowsCount;pIdx++)
			  {
  						commaIndex=0;
  						badgeRuleId =$("#progressBadgeRuleId"+pIdx).val();
						maxQualifier=$("#progressMaxQualifier"+pIdx).val();
						progressBadgeLibSelected=$("#progressBadgeLibraryRowId"+pIdx).val();
						progressBadgeName=$("#progressBadgeNameRow"+pIdx).val();
						progressBadgePoints=$("#progressBadgePointsRow"+pIdx).val();
						progressBadgeSweep=$("#progressBadgeSweepRow"+pIdx).is(':checked');
						progressBadgeDesc=$("#progressBadgeDescRow"+pIdx).val();
						if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
							badgeRuleId='0';
						if(maxQualifier==null|| maxQualifier=='' || maxQualifier=='null' )
							maxQualifier='0';
						if(progressBadgeName==null|| progressBadgeName=='' || progressBadgeName=='null' )
							progressBadgeName='0';
						if(progressBadgeDesc==null|| progressBadgeDesc=='' || progressBadgeDesc=='null' )
							progressBadgeDesc='0';
						if(progressBadgePoints==null|| progressBadgePoints=='' || progressBadgePoints=='null')
							progressBadgePoints='null';
						//commaIndex=progressBadgeName.indexOf(SPLIT_TOKEN);
						progressBadgeName=trim(progressBadgeName);
					 	for(var i=0;i<progressBadgeName.length;i++)
					    {
							if(progressBadgeName.charAt(i)==SPLIT_TOKEN)
							{
								commaIndex++;
								break;
							}
					    }
				        progressBadgeLibSelected=progressBadgeLibSelected.replace(/\,/g,REPLACEMENT_TOKEN);
						if(commaIndex>0)
						{
							progressBadgeName=progressBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
						}
						progressBadgeDesc=progressBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);
						progresscheckBoxRow=badgeRuleId+SPLIT_TOKEN+maxQualifier+SPLIT_TOKEN+progressBadgeLibSelected+SPLIT_TOKEN+progressBadgeName+SPLIT_TOKEN+progressBadgePoints+SPLIT_TOKEN+progressBadgeSweep+SPLIT_TOKEN+progressBadgeDesc;
						//alert('checkbox row valuegoing to set as :'+progresscheckBoxRow);
						document.getElementById("progressStringRow"+pIdx).value=progresscheckBoxRow;
						if(progressBadgeLibSelected!='-1')
							document.getElementById("progressStringRow"+pIdx).checked=true;
						else
							document.getElementById("progressStringRow"+pIdx).checked=false;

				}

	 }
     else if(selectedValue=='fileload')
	 {
			  var fileloadBadgeLibSelected;
			  var fileloadBadgeName;
			  var fileloadBadgeDesc;
			  var fileloadBadgePoints;
			  var fileloadBadgeSweep;
			  var fileloadcheckBoxRow='';
			  var totalRowsCount=$("#currentFileLoadTableSize").val();
			  var badgeRuleId;
			  for(var fIdx=0;fIdx<totalRowsCount;fIdx++)
			  {
				  	commaIndex=0;

				  	//if executes the logic for fileload for recognition/nomination/no promotion.
				  	//else is for rest of the promotion types, where points and sweepstake is not used.
				  	if( selectedPromotion == '-1' || promotionType == 'Y' )
				  	{
					  		badgeRuleId=$("#fileLoadBadgeRuleId"+fIdx).val();
							fileloadBadgeLibSelected=$("#fileLoadBadgeLibraryRowId"+fIdx).val();
							fileloadBadgeName=$("#fileLoadBadgeNameRow"+fIdx).val();
							fileloadBadgePoints=$("#fileLoadBadgePointsRow"+fIdx).val();
							fileloadBadgeSweep=$("#fileLoadBadgeSweepRow"+fIdx).is(':checked');
							fileloadBadgeDesc=$("#fileLoadBadgeDescRow"+fIdx).val();
							if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
								badgeRuleId='0';
							if(fileloadBadgeName==null|| fileloadBadgeName=='' || fileloadBadgeName=='null' )
								fileloadBadgeName='0';
							if(fileloadBadgeDesc==null|| fileloadBadgeDesc=='' || fileloadBadgeDesc=='null' )
								fileloadBadgeDesc='0';
							if(fileloadBadgePoints==null|| fileloadBadgePoints=='' || fileloadBadgePoints=='null' )
								fileloadBadgePoints='null';
							//commaIndex=fileloadBadgeName.indexOf(SPLIT_TOKEN);
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
							fileloadcheckBoxRow=badgeRuleId+SPLIT_TOKEN+fileloadBadgeLibSelected+SPLIT_TOKEN+fileloadBadgeName+SPLIT_TOKEN+fileloadBadgePoints+SPLIT_TOKEN+fileloadBadgeSweep+SPLIT_TOKEN+fileloadBadgeDesc;
				  	}else{
					  		badgeRuleId=$("#fileLoadBadgeRuleIdPromo"+fIdx).val();
							fileloadBadgeLibPromoSelected=$("#fileLoadBadgeLibraryRowIdPromo"+fIdx).val();
							fileloadBadgeName=$("#fileLoadBadgeNameRowPromo"+fIdx).val();
							fileloadBadgeDesc=$("#fileLoadBadgeDescRowPromo"+fIdx).val();
							if( badgeRuleId == null || badgeRuleId == '' || badgeRuleId == 'null' || badgeRuleId == 'undefined')
								badgeRuleId='0';
							if(fileloadBadgeName==null|| fileloadBadgeName=='' || fileloadBadgeName=='null' )
								fileloadBadgeName='0';
							if(fileloadBadgeDesc==null|| fileloadBadgeDesc=='' || fileloadBadgeDesc=='null' )
								fileloadBadgeDesc='0';
							//commaIndex=fileloadBadgeName.indexOf(SPLIT_TOKEN);
							fileloadBadgeName=trim(fileloadBadgeName);
							for(var i=0;i<fileloadBadgeName.length;i++)
							{
									if(fileloadBadgeName.charAt(i)==SPLIT_TOKEN)
									{
										commaIndex++;
										break;
									}
							}
							fileloadBadgeLibPromoSelected=fileloadBadgeLibPromoSelected.replace(/\,/g,REPLACEMENT_TOKEN);
							if(commaIndex>0)
							{
								fileloadBadgeName=fileloadBadgeName.replace(/\,/g,REPLACEMENT_TOKEN);
							}
							fileloadBadgeDesc=fileloadBadgeDesc.replace(/\,/g,REPLACEMENT_TOKEN);
				  			fileloadcheckBoxRow=badgeRuleId+SPLIT_TOKEN+fileloadBadgeLibPromoSelected+SPLIT_TOKEN+fileloadBadgeName+SPLIT_TOKEN+fileloadBadgeDesc;
				  	}


					//alert('checkbox row valuegoing to set as :'+fileloadcheckBoxRow);
					document.getElementById("fileLoadStringRow"+fIdx).value=fileloadcheckBoxRow;
   				    if(fileloadBadgeLibSelected!='-1')
						document.getElementById("fileLoadStringRow"+fIdx).checked=true;
   				    else
   				    	document.getElementById("fileLoadStringRow"+fIdx).checked=false;
				}

	  }
	  var valid=validateFields( promotionType );
	  if(valid)
		 setDispatchAndSubmit(method);
}
function validateBadgeForm(method)
{
	var badgeExists='N';
	var errorMessage='';
	var badgeId='';
	if( $("#badgeId").val() != null )
	  badgeId = $("#badgeId").val();
	var badgeSetupName = $("#badgeSetupName").val();
	var selectedPromo = $("#promotionIds").val();

	if(badgeSetupName!=null&& badgeSetupName!='')
	{

			$.ajax({
						url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=validateBadgeSetupName&badgeName="+badgeSetupName+"&badgeId="+badgeId+"&promotionIds="+selectedPromo,
						success: function(data){
							if(data != null && data != '' && data != 'null')
							{
								//badgeExists=trim(data);
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

$(document).ready(function(){
    var selectedBadgeType = $("#badgeTypeId").val();
    var fromPage="<%=fromPage%>";
    var isPointRange="<%=isPointRange%>";
    var isGoalQuest="<%=isGoalQuest%>";
    var hasPartners="<%=hasPartners%>";
    var isThrowDown="<%=isThrowDown%>";
    var editing = "<%=editing%>";
    var promoIds = "<%=promoIds%>";
    var hasStackStandingPayouts="<%=hasStackStandingPayouts%>";
    var fileLoadtableSize=${currentFileLoadTableSize};
    var progresstableSize=${currentProgressTableSize};
    var pointRangetableSize=${currentPointRangeTableSize};
    var selectedPromotion = $("#promotionIds").val();
    var showFileLoadNoPromoDiv = '${isShowFileloadNoPromoDiv}';

    document.getElementById("currentFileLoadTableSize").value=fileLoadtableSize;
    document.getElementById("currentProgressTableSize").value=progresstableSize;
    document.getElementById("currentPointRangeTableSize").value=pointRangetableSize;
    $("#errorShowDiv").hide();
    if(selectedBadgeType=='-1')
    {
    	$("#progressBadgeDiv").hide();
		$("#behaviorBadgeDiv").hide();
		$("#earnedNotEarnedBadgeLevelDiv").hide();
		$("#earnedNotEarnedBadgePointDiv").hide();
		$('#earnedNotEarnedGQBadgeLevelDiv').hide();
		$('#earnedNotEarnedTDBadgeStackLevelDiv').hide();
		$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
		$("#badgeTypesFileLoad").hide();
		$("#fileLoadBadgeDiv").hide();
		$("#fileLoadForNoPromoDiv").hide();
    }
    else
    {
    	if(selectedBadgeType=='progress')
    	{
    		$("#behaviorBadgeDiv").hide();
			$("#earnedNotEarnedBadgeLevelDiv").hide();
			$("#earnedNotEarnedBadgePointDiv").hide();
			$('#earnedNotEarnedGQBadgeLevelDiv').hide();
			$('#earnedNotEarnedTDBadgeStackLevelDiv').hide();
			$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
			$("#badgeTypesFileLoad").hide();
			$("#fileLoadBadgeDiv").hide();
			$("#fileLoadForNoPromoDiv").hide();
			if(selectedPromotion != '-1' && selectedPromotion != null )
	 		{
				$("#progressBadgeDiv").show();
	 		}

    	}
    	else if(selectedBadgeType=='behavior')
    	{
    		$("#behaviorBadgeDiv").show();
	 		$("#progressBadgeDiv").hide();
	 		$("#earnedNotEarnedBadgeLevelDiv").hide();
	 		$("#earnedNotEarnedBadgePointDiv").hide();
			$('#earnedNotEarnedGQBadgeLevelDiv').hide();
			$('#earnedNotEarnedTDBadgeStackLevelDiv').hide();
			$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
	 		$("#badgeTypesFileLoad").hide();
	 		$("#fileLoadBadgeDiv").hide();
	 		$("#fileLoadForNoPromoDiv").hide();
	 		if(selectedPromotion != '-1' && selectedPromotion != null )
	 		{
	 			$("#behaviorBadgeDiv").show();
	 		}
    	}
    	else if(selectedBadgeType=='fileload')
    	{
    		$("#behaviorBadgeDiv").hide();
	 		$("#progressBadgeDiv").hide();

	 		$("#earnedNotEarnedBadgeLevelDiv").hide();
	 		$("#earnedNotEarnedBadgePointDiv").hide();
			$('#earnedNotEarnedGQBadgeLevelDiv').hide();
			$('#earnedNotEarnedTDBadgeStackLevelDiv').hide();
			$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();

			var totalFileLoadRowsValue=document.getElementById("currentFileLoadTableSize").value;
			for(var fileLoadIndex=0;fileLoadIndex<totalFileLoadRowsValue;fileLoadIndex++)
			 {
			  $("#fileLoadcheckBoxDiv"+fileLoadIndex).hide();
			 }

			if( selectedPromotion == '-1' || showFileLoadNoPromoDiv == 'Y')
			{
				$("#fileLoadForNoPromoDiv").show();
				$("#badgeTypesFileLoad").show();
				$("#badgeTypeAll").hide();
				$("#fileLoadBadgeDiv").hide();
			}
			else
			{
				$("#badgeTypesFileLoad").hide();
				$("#badgeTypeAll").show();
				$("#fileLoadBadgeDiv").show();
				$("#fileLoadForNoPromoDiv").hide();
			}

    	}
    	else if(selectedBadgeType=='earned')
    	{
    		/* if(fromPage!=null)
    		{
    			$("#earnedNotEarnedBadgeLevelDiv").hide();
    			$("#earnedNotEarnedBadgePointDiv").hide();
	    		$('#earnedNotEarnedGQBadgeLevelDiv').hide();
	    		$('#earnedNotEarnedTDBadgeStackLevelDiv').hide();
	    		$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
    		} */

    		if(isPointRange == 'Y')
   			{
    			var totalPointRangeRowsValue=document.getElementById("currentPointRangeTableSize").value;
  				$("#earnedNotEarnedBadgePointDiv").show();
  				$("#earnedNotEarnedBadgeLevelDiv").hide();
	    		$('#earnedNotEarnedGQBadgeLevelDiv').hide();
	    		$('#earnedNotEarnedTDBadgeStackLevelDiv').hide();
	    		$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
	    		for(var pointIndex=0;pointIndex<totalPointRangeRowsValue;pointIndex++)
				  {
				  	$("#pointcheckBoxDiv"+pointIndex).hide();
				  }

   			}else if(isGoalQuest == 'Y')
			{
				$('#earnedNotEarnedGQBadgeLevelDiv').show();
				$("#earnedNotEarnedBadgeLevelDiv").hide();
    			$("#earnedNotEarnedBadgePointDiv").hide();
	    		$('#earnedNotEarnedTDBadgeStackLevelDiv').hide();
	    		if(hasPartners == 'Y')
	 			   $('#earnedNotEarnedPartnerBadgeLevelDiv').show();
	 			else
	 			   $('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
			}else if(isThrowDown == 'Y')
			{
				$('#earnedNotEarnedTDBadgeStackLevelDiv').show();
				$("#earnedNotEarnedBadgeLevelDiv").hide();
    			$("#earnedNotEarnedBadgePointDiv").hide();
	    		$('#earnedNotEarnedGQBadgeLevelDiv').hide();
	    		$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
			}else if(hasPartners == 'Y')
			{
				$('#earnedNotEarnedPartnerBadgeLevelDiv').show();
				$("#earnedNotEarnedBadgeLevelDiv").hide();
    			$("#earnedNotEarnedBadgePointDiv").hide();
	    		$('#earnedNotEarnedGQBadgeLevelDiv').hide();
	    		$('#earnedNotEarnedTDBadgeStackLevelDiv').hide();
			}else{
				$("#earnedNotEarnedBadgeLevelDiv").show();
    			$("#earnedNotEarnedBadgePointDiv").hide();
	    		$('#earnedNotEarnedGQBadgeLevelDiv').hide();
	    		$('#earnedNotEarnedTDBadgeStackLevelDiv').hide();
	    		$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
			}

    		$("#behaviorBadgeDiv").hide();
	 		$("#progressBadgeDiv").hide();
	 		$("#badgeTypesFileLoad").hide();
	 		$("#fileLoadBadgeDiv").hide();
	 		$("#fileLoadForNoPromoDiv").hide();
    	}
    }
	var message ='';
	$("#promotionIds").change(function(){
		var promotionExists = false;
		var selectedPromotionIds = $("#promotionIds").val();
		var existingPromotionIds = promoIds.split(",");
		if( selectedPromotionIds.length > existingPromotionIds.length )
		{
		 for(var existingPromoIdIndex = 0; existingPromoIdIndex < existingPromotionIds.length; existingPromoIdIndex++){
			for( var selectedPromoIdIndex = 0; selectedPromoIdIndex < selectedPromotionIds.length; selectedPromoIdIndex++){
				if(existingPromotionIds[existingPromoIdIndex] == selectedPromotionIds[selectedPromoIdIndex]){
					promotionExists = true;
					break;
				}
			}
		}
		if($('#badgeTypeId').val() == 'behavior' && editing && promotionExists){
			populateBehaviorsForUpdate(selectedPromotionIds,existingPromotionIds);
		}
		else{
			$('#badgeTypeId').val("-1");
			$("#behaviorBadgeDiv").hide();
		    }
		}
		else
		{
			$('#badgeTypeId').val("-1");
			$("#behaviorBadgeDiv").hide();
		}
		$("#progressBadgeDiv").hide();
		$("#earnedNotEarnedBadgeLevelDiv").hide();
		$("#earnedNotEarnedBadgePointDiv").hide();
		$('#earnedNotEarnedGQBadgeLevelDiv').hide();
		$('#earnedNotEarnedTDBadgeStackLevelDiv').hide();
		$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
		$("#badgeTypesFileLoad").hide();
		$("#errorShowDiv").hide();
		$("#fileLoadBadgeDiv").hide();
		$("#fileLoadForNoPromoDiv").hide();
		var selectedPromotion = $("#promotionIds").val();
		if(selectedPromotion.length>1)
		{
			var firstPromo=trim(selectedPromotion).substr(0,2);
			if(firstPromo=='-1')
			{
			  message='<cms:contentText key="VALID_NOPROMO_OTHER" code="gamification.validation.messages" />';
			  $("#errorShowDiv").html(message);
			  $("#errorShowDiv").show();
			  $("#badgeTypesFileLoad").hide();
			  $("#badgeTypeAll").show();
			  return false;
			}
		}

		if(selectedPromotion=='-1')
		{
			$("#badgeTypeAll").hide();
			$("#badgeTypesFileLoad").show();
			document.getElementById("badgeTypeId").value='fileload';
			var totalFileLoadRowsValue=document.getElementById("currentFileLoadTableSize").value;
			 for(var fileLoadIndex=0;fileLoadIndex<totalFileLoadRowsValue;fileLoadIndex++)
			  {
			  	$("#fileLoadcheckBoxDiv"+fileLoadIndex).hide();
			  }
			//$("#fileLoadBadgeDiv").hide();
			$("#fileLoadForNoPromoDiv").show();
			$("#displayDays").val('0');
			$("#displayDays").attr("disabled", "disabled");
		}
		else
		{
			$("#badgeTypesFileLoad").hide();
			$("#badgeTypeAll").show();
			//$("#fileLoadBadgeDiv").show();
			//$("#fileLoadForNoPromoDiv").hide();
		}
		if(selectedPromotion.length>1)
		{
			//alert("inside");
			$.ajax({
				url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=validatePromotionType&promotionIds="+selectedPromotion,
				success: function(data){
					if(data != null && data != '' && data != 'null')
					{
					  data=trim(data);
					  if(data=='N')
					  {
						message='<cms:contentText key="VALID_MULTIPLE_PROMOTION" code="gamification.validation.messages" />';
						$("#errorShowDiv").html(message);
						$("#errorShowDiv").show();
						return false;
					  }

					}
				}
			});

		}
		if(selectedPromotion!='-1')
		{
			//alert("-1");
			$.ajax({
				url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=validatePromotionEndDate&promotionIds="+selectedPromotion,
				success: function(data){
					if(data != null && data != '' && data != 'null')
					{
					  data=trim(data);
					  if(data=='Y')
					  {
						  $("#displayDays").val('0');
						  $("#displayDays").attr("disabled", "disabled");

					  }
					  else
					  {
						  $("#displayDays").removeAttr("disabled");
					  }

					}
				}
			});
		}
	});

	function populateBehaviorsForUpdate(selectedPromotionIds,existingPromotionIds)
	{
		var callServer = true;
		var found = 0;
		for(var existingPromoIdIndex = 0; existingPromoIdIndex < existingPromotionIds.length; existingPromoIdIndex++){
			if($.inArray(existingPromotionIds[existingPromoIdIndex], selectedPromotionIds) > -1)
				found++;
		}
		if( existingPromotionIds.length == selectedPromotionIds.length && existingPromotionIds.length == found ){
			callServer = false;
			$.ajax({
				url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=populateBehaviors&promotionIds="+selectedPromotionIds,
				dataType: 'application/json',
				success: function(data)
				{
					if(data != null && data != '' && data != 'null')
					{
			    		var object = jQuery.parseJSON(data);
			     		var htmlString='';
			     		if(object.behaviors != 'undefined' && object.behaviors != 'null' && object.behaviors != null)
			     		{
			     			var currentRowCount = $('#behaviorBadgeTableId tbody tr.crud-table-row2').length;
			     			var actualLength = object.behaviors.length;
			     			if( currentRowCount > actualLength )
			     			{
			     				// remove extra drawn rows(reset to original)
			     				for(var i=actualLength; i<currentRowCount; i++){
			     					$("#rowId"+i).remove();
			     				}
			     			}
			     		}
					}
				}
			});
		}
		if(callServer)
		{
			var badgeId = $('input[name="badgeId"]').val();
			$.ajax({
				url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=populateBehaviors&mode=edit&promotionIds="+selectedPromotionIds+"&badgeId="+badgeId,
				dataType: 'application/json',
				success: function(data){
				if(data != null && data != '' && data != 'null')
				{
			    	var object = jQuery.parseJSON(data);
			     	var htmlString='';
			     	if(object.behaviors != 'undefined' && object.behaviors != 'null' && object.behaviors != null)
			     	{
			     		var rowCount = $('#behaviorBadgeTableId tbody tr.crud-table-row2').length;
			     		var len = object.behaviors.length+rowCount;
				     	for(var i=rowCount, k=0; i < len; i++,k++)
				     	{
						 		htmlString+='<tr id="rowId'+i+'" class="crud-table-row2"><td class="crud-content left-align top-align nowrap">'+object.behaviors[k].behaviorName+'</td>';
						 		htmlString+='<td class="crud-content center-align top-align nowrap">'+object.behaviors[k].promotionNames+'</td>';
						 		htmlString+='<td class="crud-content center-align top-align nowrap"> <select name="badgeLibraryIdRow'+i+'" id="badgeLibraryIdRow'+i+'"  size="1" class="content-field" >';
						 		for(var j=0, badgeLen=object.badgeLibrary.length; j < badgeLen; j++)
						     	 {
						 			htmlString+='<option value="'+object.badgeLibrary[j].id+'">'+object.badgeLibrary[j].name+'</option>';
						     	 }
						 		htmlString+='</select></td>';
						 		htmlString+='<td class="crud-content center-align top-align nowrap"> <input type="text" size="40" maxlength="40" name="behaviorbadgeNameRow'+i+'" id="behaviorbadgeNameRow'+i+'" /></td>';
						 		htmlString+='<td class="crud-content center-align top-align nowrap"> <input type="text" size="10" maxlength="5" name="behaviorbadgePointsRow'+i+'" id="behaviorbadgePointsRow'+i+'" /></td>';
						 		htmlString+='<td class="crud-content center-align top-align nowrap"> <input type="checkbox" name="behaviorBadgeSweepRow'+i+'" id="behaviorBadgeSweepRow'+i+'" class="content-field" /></td>';
						 		htmlString+='<td class="crud-content center-align top-align nowrap"> <textarea name="behaviorbadgeDescRow'+i+'" id="behaviorbadgeDescRow'+i+'" /></td>';
						 		htmlString+='<td><input type="hidden" id="behaviorRow'+i+'" name="behaviorRow'+i+'" value="'+object.behaviors[k].behaviorName+'" /></td>';
						 		htmlString+='<td><input type="hidden" id="behaviorCode'+i+'" name="behaviorCode'+i+'" value="'+object.behaviors[k].behaviorCode+'" /></td>';
						 		htmlString+='<td id="checkBoxDiv"><input type="checkbox" name="behaviorStringRow" id="behaviorStringRow'+i+'" value="'+object.behaviors[k].behaviorName+'" /></td>';
						     	htmlString+='</tr>';
						}
				     	$('#behaviorBadgeTableId tr:last').after(htmlString);
				     	for(var tdLen=0, divLen=object.behaviors.length; tdLen < divLen; tdLen++)
				     	{
				     		$("#behaviorStringRow"+tdLen).hide();
				     	}
			     	}
			     	else
			     	{
			     		var noBehaviorMessage='<cms:contentText key="NO_BEHAVIORS" code="gamification.admin.labels" />';
			     		htmlString+='<tr class="crud-table-row2"><td class="crud-content left-align top-align nowrap">'+noBehaviorMessage+'</td>';
			     		$("#behaviorBadgeDiv").html(htmlString);
			     	}
				}
				}
			});
		}
	}

	function populateBehaviors(selectedPromotion)
	{
		$.ajax({
			url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=populateBehaviors&promotionIds="+selectedPromotion,
			dataType: 'application/json',
			success: function(data){
			if(data != null && data != '' && data != 'null')
			{
		     	  var object = jQuery.parseJSON(data);

		     	  var behaviorHeader='<cms:contentText key="BEHAVIOR" code="gamification.admin.labels" />';
		     	  var promoNameHeader='<cms:contentText key="PROMOTION_NAME" code="gamification.admin.labels" />';
		     	  var badgeLibHeader='<cms:contentText key="BADGE_LIBRARY" code="gamification.admin.labels" />';
		     	  var badgeNameHeader='<cms:contentText key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" />';
		     	  var badgePointsHeader='Badge Points';
		     	   var badgeSweepHeader='Eligible for Sweepstakes?';
		     	  var descriptionHeader='<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />';
		     	  //var htmlString='<table class="crud-table"><thead>	<tr class="crud-table-row2"> <th class="crud-table-header-row row-sortable">Behavior</th>';
		     	  var htmlString='<table id="behaviorBadgeTableId" class="crud-table"><thead>	<tr class="crud-table-row2"> <th class="crud-table-header-row">'+behaviorHeader+'</th>';
		     	  htmlString+='<th class="crud-table-header-row">'+promoNameHeader+'</th>	<th class="crud-table-header-row" align="left">'+badgeLibHeader+'</th>	';
		     	  htmlString+='<th class="crud-table-header-row">'+badgeNameHeader+'</th><th class="crud-table-header-row">'+badgePointsHeader+' </th><th class="crud-table-header-row">'+badgeSweepHeader+' </th><th class="crud-table-header-row">'+descriptionHeader+' </th></tr></thead><tr><td></td>	</tr>';

		     	  if(object.behaviors!='undefined'&&object.behaviors!='null'&&object.behaviors!=null)
		     	  {
			     	  for(var i=0, len=object.behaviors.length; i < len; i++)
			     	  {
					 		htmlString+='<tr id="rowId'+i+'" class="crud-table-row2"><td class="crud-content left-align top-align nowrap">'+object.behaviors[i].behaviorName+'</td>';
					 		htmlString+='<td class="crud-content center-align top-align nowrap">'+object.behaviors[i].promotionNames+'</td>';
					 		htmlString+='<td class="crud-content center-align top-align nowrap"> <select name="badgeLibraryIdRow'+i+'" id="badgeLibraryIdRow'+i+'"  size="1" class="content-field" >';
					 		for(var j=0, badgeLen=object.badgeLibrary.length; j < badgeLen; j++)
					     	 {
					 			htmlString+='<option value="'+object.badgeLibrary[j].id+'">'+object.badgeLibrary[j].name+'</option>';
					     	 }
					 		htmlString+='</select></td>';
					 		htmlString+='<td class="crud-content center-align top-align nowrap"> <input type="text" size="40" maxlength="40" name="behaviorbadgeNameRow'+i+'" id="behaviorbadgeNameRow'+i+'" /></td>';
					 		htmlString+='<td class="crud-content center-align top-align nowrap"> <input type="text" size="10" maxlength="5" name="behaviorbadgePointsRow'+i+'" id="behaviorbadgePointsRow'+i+'" /></td>';
					 		htmlString+='<td class="crud-content center-align top-align nowrap"> <input type="checkbox" name="behaviorBadgeSweepRow'+i+'" id="behaviorBadgeSweepRow'+i+'" class="content-field" /></td>';
					 		htmlString+='<td class="crud-content center-align top-align nowrap"> <textarea name="behaviorbadgeDescRow'+i+'" id="behaviorbadgeDescRow'+i+'" /></td>';
					 		htmlString+='<td><input type="hidden" id="behaviorRow'+i+'" name="behaviorRow'+i+'" value="'+object.behaviors[i].behaviorName+'" /></td>';
					 		htmlString+='<td><input type="hidden" id="behaviorCode'+i+'" name="behaviorCode'+i+'" value="'+object.behaviors[i].behaviorCode+'" /></td>';
					 		htmlString+='<td id="checkBoxDiv"><input type="checkbox" name="behaviorStringRow" id="behaviorStringRow'+i+'" value="'+object.behaviors[i].behaviorName+'" /></td>';
					     	htmlString+='</tr>';

					   }
			     	 htmlString+='<input type="hidden" id="totalBehaviorsLength" name="totalBehaviorsLength" value="'+(object.behaviors.length-1)+'" />';
			     	 htmlString+='<table><tr><td><input type="checkbox" name="includeAllBehaviorPoints" id="includeAllBehaviorPoints"/><c:out value="Include Points if all behaviors are earned"></c:out>&nbsp;&nbsp;&nbsp;</td>';
			     	 htmlString+='<td><input type="text" name="allBehaviorPoints" id="allBehaviorPoints" size="10" maxlength="5" class="content-field" /></td></tr></table>';

			     	 $("#behaviorBadgeDiv").html(htmlString);
			     	 for(var tdLen=0, divLen=object.behaviors.length; tdLen < divLen; tdLen++)
			     	 {
			     		$("#behaviorStringRow"+tdLen).hide();
			     	 }
		     	  }
		     	  else
		     	  {
		     		 var noBehaviorMessage='<cms:contentText key="NO_BEHAVIORS" code="gamification.admin.labels" />';
		     		 htmlString+='<tr class="crud-table-row2"><td class="crud-content left-align top-align nowrap">'+noBehaviorMessage+'</td>';
		     		$("#behaviorBadgeDiv").html(htmlString);
		     	  }

			}
			}
		});
	}

	$("#badgeTypeId").change(function(){
		$("#errorShowDiv").hide();
		var selectedValue = $("#badgeTypeId").val();
		var selectedPromotion = $("#promotionIds").val();
		if(selectedValue=='-1')
		{
			$("#progressBadgeDiv").hide();
			$("#behaviorBadgeDiv").hide();
			$('#earnedNotEarnedGQBadgeLevelDiv').hide();
			$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
			$("#earnedNotEarnedBadgePointDiv").hide();
			$("#errorShowDiv").hide();
			$("#fileLoadBadgeDiv").hide();
			$("#fileLoadForNoPromoDiv").show();
		}
		else if(selectedValue=='progress')
		{
			var totalProgressRowsValue=document.getElementById("currentProgressTableSize").value;
			if(selectedPromotion==null)
			{
				message='<cms:contentText key="ATLEAST_ONE_PROMOTION" code="gamification.validation.messages" />';
				$("#errorShowDiv").html(message);
				$("#errorShowDiv").show();
				//$('#badgeTypeId').children().removeAttr('selected');
				$('#badgeTypeId').val("-1");
				return false;
			}
			$.ajax({
				url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=validatePromotionType&promotionIds="+selectedPromotion,
				success: function(data){
					if(data != null && data != '' && data != 'null')
					{
					  data=trim(data);
					  if(data=='N' || data=='NOM')
					  {
						message='<cms:contentText key="RECOGNITION_MUST" code="gamification.validation.messages" />';
						$("#errorShowDiv").html(message);
						$("#errorShowDiv").show();
						$('#promotionIds').children().removeAttr('selected');
						$('#earnedNotEarnedTDBadgeStackLevelDiv').hide();
						$('#earnedNotEarnedGQBadgeLevelDiv').hide();
             			$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
						$("#progressBadgeDiv").hide();
						$("#badgesBillCodes").hide();
						return false;
					  }
					  else if(data=='GQ')
					  {
							message='<cms:contentText key="RECOGNITION_MUST" code="gamification.validation.messages" />';
							$("#errorShowDiv").html(message);
							$("#errorShowDiv").show();
							$('#promotionIds').children().removeAttr('selected');
							$("#progressBadgeDiv").hide();
							$("#badgesBillCodes").hide();
							return false;
					  }
					  else
				      {
					    for(var progressIndex=0;progressIndex<totalProgressRowsValue;progressIndex++)
					    {
					  	  $("#progresscheckBoxDiv"+progressIndex).hide();
					    }
					    $("#progressBadgeDiv").show();
					  }
				   }
				}
			});
		 	$("#behaviorBadgeDiv").hide();
			$('#earnedNotEarnedGQBadgeLevelDiv').hide();
			$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
			$("#earnedNotEarnedBadgePointDiv").hide();
			$("#fileLoadBadgeDiv").hide();
			$("#fileLoadForNoPromoDiv").hide();

		}
		else if(selectedValue=='behavior')
		{
			var isRecognition='N';
			if(selectedPromotion==null)
			{
				message='<cms:contentText key="ATLEAST_ONE_PROMOTION" code="gamification.validation.messages" />';
				$("#errorShowDiv").html(message);
				$("#errorShowDiv").show();
				//$('#badgeTypeId').children().removeAttr('selected');
				$('#badgeTypeId').val("-1");
				return false;
			}

			$.ajax({
				url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=validatePromotionType&promotionIds="+selectedPromotion,
				success: function(data){
					if(data != null && data != '' && data != 'null')
					{
					  isRecognition=trim(data);
					  if(isRecognition=='GQ')
					  {
							message='<cms:contentText key="RECOGNITION_MUST" code="gamification.validation.messages" />';
							$("#errorShowDiv").html(message);
							$("#errorShowDiv").show();
							$('#promotionIds').children().removeAttr('selected');
							$("#behaviorBadgeDiv").hide();
							$('#earnedNotEarnedGQBadgeLevelDiv').hide();
                 			$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
                 			$("#badgesBillCodes").hide();
							return false;
					  }
					  else
					  {
						  populateBehaviors(selectedPromotion);
					  }
					}
				}
			});
			$("#behaviorBadgeDiv").show();
	 		$("#progressBadgeDiv").hide();
	 		$('#earnedNotEarnedGQBadgeLevelDiv').hide();
			$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
			$("#earnedNotEarnedBadgeLevelDiv").hide();
	 		$("#earnedNotEarnedBadgePointDiv").hide();
	 		$("#fileLoadBadgeDiv").hide();
	 		$("#fileLoadForNoPromoDiv").hide();
		}
		else if(selectedValue=='fileload')
		{
			var totalFileLoadRowsValue=document.getElementById("currentFileLoadTableSize").value;
			if(selectedPromotion==null)
			{
				message='<cms:contentText key="PRMOTION_REQUIRED" code="gamification.validation.messages" />';
				$("#errorShowDiv").html(message);
				$("#errorShowDiv").show();
				//$('#badgeTypeId').children().removeAttr('selected');
				$('#badgeTypeId').val("-1");
				return false;
			}
			else if(selectedPromotion.length>1)
			{
				message='<cms:contentText key="ONLY_ONE_PROMO" code="gamification.validation.messages" />';
				$("#errorShowDiv").html(message);
				$("#errorShowDiv").show();
				$('#promotionIds').children().removeAttr('selected');
			}

			$.ajax({
				url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=validatePromotionType&promotionIds="+selectedPromotion+"&badgeType="+selectedValue,
				success: function(data){
					//alert("inside");
					if(data != null && data != '' && data != 'null')
					{
					  isRecognition=trim(data);
					  if(isRecognition=='GQ' || isRecognition=='NOM')
					  {
						message='<cms:contentText key="RECOGNITION_MUST" code="gamification.validation.messages" />';
						$("#errorShowDiv").html(message);
						$("#errorShowDiv").show();
						$('#promotionIds').children().removeAttr('selected');
				 		$("#fileLoadBadgeDiv").hide();
				 		$("#fileLoadForNoPromoDiv").hide();
				 		$("#badgesBillCodes").hide();
						return false;
					  }
					  else
					  {
						  //alert("in");
						  if( isRecognition == 'Y')
						  {
							$("#fileLoadBadgeDiv").hide();
							$("#fileLoadForNoPromoDiv").show();
							$("#badgesBillCodes").show();
						  }
						  populateBehaviors(selectedPromotion);
					  }
					}
				}
			});

			for(var fileLoadIndex=0;fileLoadIndex<totalFileLoadRowsValue;fileLoadIndex++)
			{
			  	$("#fileLoadcheckBoxDiv"+fileLoadIndex).hide();
			}
	 		$("#behaviorBadgeDiv").hide();
	 		$("#progressBadgeDiv").hide();
	 		$('#earnedNotEarnedGQBadgeLevelDiv').hide();
			$('#earnedNotEarnedPartnerBadgeLevelDiv').hide();
			$("#earnedNotEarnedBadgePointDiv").hide();

			if(selectedPromotion=='-1' )
			{
				$("#fileLoadBadgeDiv").hide();
			    $("#fileLoadForNoPromoDiv").show();
			    $("#badgeTypesFileLoad").show();
				$("#badgeTypeAll").hide();
			}
			else
			{
				$("#fileLoadForNoPromoDiv").hide();
				$("#fileLoadBadgeDiv").show();
				$("#badgeTypesFileLoad").hide();
				$("#badgeTypeAll").show();
			}
		}
		else if(selectedValue=='earned')
		{
			var totalPointRangeRowsValue=document.getElementById("currentPointRangeTableSize").value;
			if(selectedPromotion==null)
			{
				message='<cms:contentText key="ATLEAST_ONE_PROMOTION" code="gamification.validation.messages" />';
				$("#errorShowDiv").html(message);
				$("#errorShowDiv").show();
				//$('#badgeTypeId').children().removeAttr('selected');
				$('#badgeTypeId').val("-1");
				return false;
			}
			if(selectedPromotion.length>1)
			{
				message='<cms:contentText key="ONLY_ONE_PROMO" code="gamification.validation.messages" />';
				$("#errorShowDiv").html(message);
				$("#errorShowDiv").show();
				$('#promotionIds').children().removeAttr('selected');
				return false;
			}

			$.ajax({
				url: "<%=RequestUtils.getBaseURI(request)%>/promotion/badgeAddAction.do?method=populateLevels&promotionIds="+selectedPromotion,
				dataType: 'application/json',
				success: function(data){
					if(data != null && data != '' && data != 'null')					{
					  //$("#featured_items").replaceWith(data);
					  //alert('data:'+data);
					  //data=trim(data);
					  if(trim(data)!='A') {
					  	var dataValue = jQuery.parseJSON(data);
					  }

					  if(trim(data)=='A')
					  {
						  for(var pointIndex=0;pointIndex<totalPointRangeRowsValue;pointIndex++)
						  {
						  	$("#pointcheckBoxDiv"+pointIndex).hide();
						  }
						  $("#earnedNotEarnedBadgePointDiv").show();
						  $("#earnedNotEarnedBadgeLevelDiv").hide();

					  }
					  else if(dataValue.goalQuest==true)
					  {
						  var promotionNameHeader='<cms:contentText key="PROMOTION_NAME" code="gamification.admin.labels" />';
					      var levelHeader='<cms:contentText key="LEVEL_NAME" code="gamification.admin.labels" />';
					      var badgeLibHeader='<cms:contentText key="BADGE_PICKLIST" code="gamification.admin.labels" />';
					      var badgeNameHeader='<cms:contentText key="BADGE_NAME" code="gamification.admin.labels" />';
					      var descriptionHeader='<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />';
					      var partnerBadgeLibHeader='<cms:contentText key="PARTNER_BADGE_PICKLIST" code="gamification.admin.labels" />';
					      var partnerBadgeNameHeader='<cms:contentText key="PARTNER_BADGE_NAME" code="gamification.admin.labels" />';
					      var partnerDescriptionHeader='<cms:contentText key="PARTNER_BADGE_DESC" code="gamification.admin.labels" />';

					      var promotionData = jQuery.parseJSON(data);

						  var htmlString = '<table><thead><tr class="crud-table-row2"><th class="crud-table-header-row">'+promotionNameHeader+'</th><th class="crud-table-header-row">'+levelHeader+'</th>';
						  htmlString+='<th class="crud-table-header-row">'+badgeLibHeader+'</th><th class="crud-table-header-row">'+badgeNameHeader+'</th>';
						  htmlString+='<th class="crud-table-header-row">'+descriptionHeader+'</th></tr></thead><tr><td></td></tr>';
						  if(promotionData.levelNames!='undefined' && promotionData.levelNames!=null) {
							  for(var k=0, lenLevel=promotionData.levelNames.length; k < lenLevel; k++) {
								  htmlString+='<tr class="crud-table-row2"><td class="crud-content left-align top-align nowrap">'+promotionData.promotionName+'</td>';
								  htmlString+='<td class="crud-content left-align top-align nowrap" id="levelsNameRow'+k+'" >'+promotionData.levelNames[k]+'</td>';
								  htmlString+='<td class="crud-content left-align top-align nowrap"><select name="badgeLibraryId" id="levelsBadgeLibraryIdRow'+k+'" class="content-field">';
								  for(var m=0, lenBadge=promotionData.badgeLibraryList.length; m < lenBadge; m++)
							     	{
							 			htmlString+='<option value="'+promotionData.badgeLibraryList[m].id+'">'+promotionData.badgeLibraryList[m].name+'</option>';
							     	}
								  htmlString+='</select></td>';
								  htmlString+='<td class="crud-content left-align top-align nowrap"><input type="text" name="badgeName" id="levelsBadgeNameRow'+k+'" size="40" maxlength="40" class="content-field" /></td>';
								  htmlString+='<td class="crud-content left-align top-align nowrap"><textarea name="badgeDescription" id="levelsBadgeDescRow'+k+'" class="content-field" /></td>';
								  htmlString+='<td id="checkBoxDiv"><input type="checkbox" name="levelStringRow" id="levelStringRow'+k+'" value="levelsBadgeLibraryIdRow'+k+'" /></td> </tr>';
							  }
							htmlString+='<input type="hidden" id="totalGQLevelsLength" name="totalGQLevelsLength" value="'+promotionData.levelNames.length+'" />';
						  }
						  htmlString+='</table>';
						  $('#earnedNotEarnedGQBadgeLevelDiv').html(htmlString);

						  for(var tdLen=0, divLen=promotionData.levelNames.length; tdLen < divLen; tdLen++)
					     	 {
					     		$("#levelStringRow"+tdLen).hide();
					     	 }

						  var htmlPartnerString = '<table><thead><tr class="crud-table-row2"><th class="crud-table-header-row">'+promotionNameHeader;
						  htmlPartnerString+='</th><th class="crud-table-header-row">'+levelHeader+'</th>';
						  htmlPartnerString+='<th class="crud-table-header-row">'+partnerBadgeLibHeader+'</th><th class="crud-table-header-row">'+partnerBadgeNameHeader+'</th>';
						  htmlPartnerString+='<th class="crud-table-header-row">'+partnerDescriptionHeader+'</th></tr></thead><tr><td></td></tr>';
						  if(promotionData.levelNames!='undefined' && promotionData.levelNames!=null) {
							  for(var k=0, lenLevel=promotionData.levelNames.length; k < lenLevel; k++) {
								  htmlPartnerString+='<tr class="crud-table-row2"><td class="crud-content left-align top-align nowrap">'+promotionData.promotionName+'</td>';
								  htmlPartnerString+='<td class="crud-content left-align top-align nowrap" id="levelsNameRow'+k+'" >'+promotionData.levelNames[k]+'</td>';
								  htmlPartnerString+='<td class="crud-content left-align top-align nowrap"><select name="partnerBadgeLibraryId" id="levelsPartnerBadgeLibraryIdRow'+k+'" class="content-field">';
								  for(var m=0, lenBadge=promotionData.badgeLibraryList.length; m < lenBadge; m++)
							     	{
									  htmlPartnerString+='<option value="'+promotionData.badgeLibraryList[m].id+'">'+promotionData.badgeLibraryList[m].name+'</option>';
							     	}
								  htmlPartnerString+='</select></td>';
								  htmlPartnerString+='<td class="crud-content left-align top-align nowrap"><input type="text" name="partnerBadgeName" id="levelsPartnerBadgeNameRow'+k+'" size="40" maxlength="40" class="content-field" /></td>';
								  htmlPartnerString+='<td class="crud-content left-align top-align nowrap"><textarea name="partnerBadgeDescription" id="levelsPartnerBadgeDescRow'+k+'" class="content-field" /></td> </tr>';
							  }
						    htmlString+='<input type="hidden" id="totalGQLevelsLength" name="totalGQLevelsLength" value="'+promotionData.levelNames.length+'" />';
						  }
						  htmlPartnerString+='</table>';
						  $('#earnedNotEarnedPartnerBadgeLevelDiv').html(htmlPartnerString);

						  $('#earnedNotEarnedGQBadgeLevelDiv').show();
						  if(dataValue.partners==true)
						  {
						  	$('#earnedNotEarnedPartnerBadgeLevelDiv').show();
						  }
				      }
					  else if(dataValue.throwDown==true)
					  {
						  var nodeNameHeader='<cms:contentText code="promotion.payout" key="NODE_TYPE"/>';
					      var stackLevelHeader='<cms:contentText code="promotion.payout.throwdown" key="STACK_RANK_FROM"/>-<cms:contentText code="promotion.payout.throwdown" key="STACK_RANK_TO"/>';
					      var badgeLibHeader='<cms:contentText key="BADGE_PICKLIST" code="gamification.admin.labels" />';
					      var badgeNameHeader='<cms:contentText key="BADGE_NAME" code="gamification.admin.labels" />';
					      var descriptionHeader='<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />';
					      var stackRankBadge = 'Stack Ranking Badges';
					      var overallBadge = 'OverallBadge';
					      var undefeatedBadgeHeader = 'UndefeatedBadge';
					      var promotionData = jQuery.parseJSON(data);

					      var htmlString='<table><tr><td><span class="headline"><cms:contentText key="STACK_RANKING_BADGES" code="gamification.admin.labels" /></span></td><tr></table><br />';
						  htmlString += '<table><thead><tr class="crud-table-row2"><th class="crud-table-header-row">'+nodeNameHeader+'</th><th class="crud-table-header-row">'+stackLevelHeader+'</th>';
						  htmlString+='<th class="crud-table-header-row">'+badgeLibHeader+'</th><th class="crud-table-header-row">'+badgeNameHeader+'</th>';
						  htmlString+='<th class="crud-table-header-row">'+descriptionHeader+'</th></tr></thead><tr><td></td></tr>';
						  if(dataValue.stackStandingPayouts==true)
						  {
							  var n=0;
							  if(promotionData.nodePayouts!='undefined' && promotionData.nodePayouts!=null) {
							     for (var key in promotionData.nodePayouts) {
							    	 for(var k=0, lenStackLevel=promotionData.nodePayouts[key].length; k < lenStackLevel; k++) {
							    	  if ( k == 0 )
							    	  {
									  	htmlString+='<tr class="crud-table-row2"><td class="crud-content left-align top-align nowrap"id="stackLevelsNodeNameRow'+n+'" >'+key+'</td>';
							    	  }
							    	  else
							    	  {
							    		htmlString+='<tr class="crud-table-row2"><td class="crud-content left-align top-align nowrap"id="stackLevelsNodeNameRow'+n+'" > </td>';
							    	  }
									  htmlString+='<td class="crud-content left-align top-align nowrap" id="stackLevelsNameRow'+n+'" >'+promotionData.nodePayouts[key][k]+'</td>';
									  htmlString+='<td class="crud-content left-align top-align nowrap"><select name="badgeLibraryId" id="stackLevelsBadgeLibraryIdRow'+n+'" class="content-field">';
									  for(var m=0, lenBadge=promotionData.badgeLibraryList.length; m < lenBadge; m++)
								     	{
								 			htmlString+='<option value="'+promotionData.badgeLibraryList[m].id+'">'+promotionData.badgeLibraryList[m].name+'</option>';
								     	}
									  htmlString+='</select></td>';
									  htmlString+='<td class="crud-content left-align top-align nowrap"><input type="text" name="badgeName" id="stackLevelsBadgeNameRow'+n+'" size="40" maxlength="40" class="content-field" /></td>';
									  htmlString+='<td class="crud-content left-align top-align nowrap"><textarea name="badgeDescription" id="stackLevelsBadgeDescRow'+n+'" class="content-field" /></td>';
									  htmlString+='<td id="checkBoxDiv"><input type="hidden" name="stackLevelStringRow" id="stackLevelStringRow'+n+'"/></td> </tr>';
									  n = n + 1;
									  }
							     }
								 htmlString+='<input type="hidden" id="totalTDStackLevelsLength" name="totalTDStackLevelsLength" value="'+n+'" />';
							  }
						  }
						  else
						  {
							 var noLevelMessage='<cms:contentText key="NO_LEVELS" code="gamification.admin.labels" />';
							 htmlString+='<tr class="crud-table-row2"><td class="crud-content left-align top-align nowrap" colspan="5">'+noLevelMessage+'</td>';
							 htmlString+='<input type="hidden" id="totalTDStackLevelsLength" name="totalTDStackLevelsLength" value="0" />';
						  }
						  htmlString+='</table><br/>';

						   //Overall
						  htmlString+='<table><tr><td><span class="headline"><cms:contentText key="OVERALL_BADGES" code="gamification.admin.labels" /></span></td><tr></table><br />';
						  htmlString += '<table><thead><tr class="crud-table-row2"><th class="crud-table-header-row">'+nodeNameHeader+'</th><th class="crud-table-header-row">'+stackLevelHeader+'</th>';
						  htmlString+='<th class="crud-table-header-row">'+badgeLibHeader+'</th><th class="crud-table-header-row">'+badgeNameHeader+'</th>';
						  htmlString+='<th class="crud-table-header-row">'+descriptionHeader+'</th></tr></thead><tr><td></td></tr>';
						  if(dataValue.stackStandingPayouts==true)
						  {
							  //alert("Overall");
							  var i=0;
							  if(promotionData.nodePayouts!='undefined' && promotionData.nodePayouts!=null) {
							     for (var key in promotionData.nodePayouts) {
							    	 for(var j=0, lenStackLevel=promotionData.nodePayouts[key].length; j < lenStackLevel; j++) {
							    	  if ( j == 0 )
							    	  {
									  	htmlString+='<tr class="crud-table-row2"><td class="crud-content left-align top-align nowrap"id="overallLevelsNodeNameRow'+i+'" >'+key+'</td>';
							    	  }
							    	  else
							    	  {
							    		htmlString+='<tr class="crud-table-row2"><td class="crud-content left-align top-align nowrap"id="overallLevelsNodeNameRow'+i+'" > </td>';
							    	  }
									  htmlString+='<td class="crud-content left-align top-align nowrap" id="overallLevelsNameRow'+i+'" >'+promotionData.nodePayouts[key][j]+'</td>';
									  htmlString+='<td class="crud-content left-align top-align nowrap"><select name="badgeLibraryId" id="overallLevelsBadgeLibraryIdRow'+i+'" class="content-field">';
									  for(var m=0, lenBadge=promotionData.badgeLibraryList.length; m < lenBadge; m++)
								     	{
								 			htmlString+='<option value="'+promotionData.badgeLibraryList[m].id+'">'+promotionData.badgeLibraryList[m].name+'</option>';
								     	}
									  htmlString+='</select></td>';
									  htmlString+='<td class="crud-content left-align top-align nowrap"><input type="text" name="badgeName" id="overallLevelsBadgeNameRow'+i+'" size="40" maxlength="40" class="content-field" /></td>';
									  htmlString+='<td class="crud-content left-align top-align nowrap"><textarea name="badgeDescription" id="overallLevelsBadgeDescRow'+i+'" class="content-field" /></td>';
									  htmlString+='<td id="overallCheckBoxDiv"><input type="hidden" name="overallLevelStringRow" id="overallLevelStringRow'+i+'" /></td> </tr>';
									  i = i + 1;
									  }
							     }
								 htmlString+='<input type="hidden" id="totalTDOverallLevelsLength" name="totalTDOverallLevelsLength" value="'+i+'" />';
							  }
						  }
						  else
						  {
							 var noLevelMessage='<cms:contentText key="NO_LEVELS" code="gamification.admin.labels" />';
							 htmlString+='<tr class="crud-table-row2"><td class="crud-content left-align top-align nowrap" colspan="5">'+noLevelMessage+'</td>';
							 htmlString+='<input type="hidden" id="totalTDOverallLevelsLength" name="totalTDOverallLevelsLength" value="0" />';
						  }
						  htmlString+='</table><br/>';


			                  var includeUndefeatedBadge = 'Include UndefeatedBadge';

							  htmlString+='<table><tr><td><span class="headline"><cms:contentText key="UNDEFEATED_BADGES" code="gamification.admin.labels" /></span></td><tr>';
							  htmlString+='<tr><td><input type="checkbox" name="undefeatedBadge" id="undefeatedBadge" onclick="handleClick();"/><cms:contentText key="UNDEFEATED_BADGE_DESC" code="gamification.admin.labels" /></td></tr></table>';
							  htmlString+='<table id="undefeatedBadgeContent"><thead><tr class="crud-table-row2">';
							  htmlString+='<th class="crud-table-header-row">'+badgeLibHeader+'</th><th class="crud-table-header-row">'+badgeNameHeader+'</th>';
							  htmlString+='<th class="crud-table-header-row">'+descriptionHeader+'</th></tr></thead><tr><td></td></tr>';


							  htmlString+='<tr><td class="crud-content left-align top-align nowrap"><select name="badgeLibraryId" id="undefeatedBadgeLibraryIdRow" class="content-field">';
							  for(var m=0, lenBadge=promotionData.badgeLibraryList.length; m < lenBadge; m++)
						     	{
						 			htmlString+='<option value="'+promotionData.badgeLibraryList[m].id+'">'+promotionData.badgeLibraryList[m].name+'</option>';
						     	}
							  htmlString+='</select></td>';
							  htmlString+='<td class="crud-content left-align top-align nowrap"><input type="text" name="undefeatedBadgeName" id="undefeatedBadgeNameRow" size="40" maxlength="40" class="content-field" /></td>';
							  htmlString+='<td class="crud-content left-align top-align nowrap"><textarea name="undefeatedBadgeDescription" id="undefeatedBadgeDescRow" class="content-field" /></td>';
							  htmlString+='<td id="undefeatedCheckBoxDiv"><input type="hidden" name="undefeatedBadgeStringRow" id="undefeatedBadgeStringRow"/></td></tr>';
							  htmlString+='</table>'
						  //alert(htmlString);
						  $('#earnedNotEarnedTDBadgeStackLevelDiv').html(htmlString);
						  for(var tdLen=0, divLen=n; tdLen < divLen; tdLen++)
					      {
					     	$("#levelStringRow"+tdLen).hide();
					      }
						  for(var tdLen=0, divLen=i; tdLen < divLen; tdLen++)
					      {
					     	$("#overallLevelStringRow"+tdLen).hide();
					      }
						  $("#undefeatedBadgeContent").hide();
						  $('#earnedNotEarnedTDBadgeStackLevelDiv').show();

				      }
					  else
					  {
						 var objectLevel = jQuery.parseJSON(data);
						 var countryHeader='<cms:contentText key="COUNTRY" code="gamification.admin.labels" />';
				     	 var levelHeader='<cms:contentText key="LEVEL" code="gamification.admin.labels" />';
				     	 var badgeLibHeader='<cms:contentText key="BADGE_LIBRARY" code="gamification.admin.labels" />';
				     	 var badgeNameHeader='<cms:contentText key="BADGE_DISPLAY_NAME" code="gamification.admin.labels" />';
				     	 var descriptionHeader='<cms:contentText key="DESCRIPTION" code="gamification.admin.labels" />';
						 var htmlStringLevel='<table class="crud-table"><thead>	<tr class="crud-table-row2"> <th class="crud-table-header-row">'+countryHeader+'</th><th class="crud-table-header-row">'+levelHeader+'</th>';
						 htmlStringLevel+='<th class="crud-table-header-row" align="left">	     '+    badgeLibHeader+'</th>	';
						 htmlStringLevel+='<th class="crud-table-header-row">'+badgeNameHeader+'	</th><th class="crud-table-header-row">'+descriptionHeader+'	</th></tr></thead><tr><td></td>	</tr>';

						 if(objectLevel.levels!='undefined'&&objectLevel.levels!='null'&&objectLevel.levels!=null)
				     	 {
					     	 for(var k=0, lenLevel=objectLevel.levels.length; k < lenLevel; k++)
					     	 {
					     		htmlStringLevel+='<tr class="crud-table-row2">';
					     		if(objectLevel.levels[k].countryCode!=''&&objectLevel.levels[k].countryCode!='undefined' && objectLevel.levels[k].countryCode!=null&&objectLevel.levels[k].countryCode!=' '&& objectLevel.levels[k].countryCode!='null')
					     			htmlStringLevel+='<td class="crud-content left-align top-align nowrap">'+objectLevel.levels[k].countryCode+'</td>';
					     		else
					     			htmlStringLevel+='<td class="crud-content left-align top-align nowrap">N/A</td>';
					     		htmlStringLevel+='<td class="crud-content left-align top-align nowrap">'+objectLevel.levels[k].levelName+'</td>';
					     		htmlStringLevel+='<td class="crud-content left-align top-align nowrap"> <select name="levelsBadgeLibraryIdRow'+k+'" id="levelsBadgeLibraryIdRow'+k+'"  size="1" styleClass="content-field" >';
					     			for(var m=0, lenBadge=objectLevel.badgeLibrary.length; m < lenBadge; m++)
							     	{
							 			htmlStringLevel+='<option value="'+objectLevel.badgeLibrary[m].id+'">'+objectLevel.badgeLibrary[m].name+'</option>';
							     	}
							 		htmlStringLevel+='</select></td>';
							 		htmlStringLevel+='<td class="crud-content left-align top-align nowrap"> <input type="text" size="40" maxlength="40" name="levelsbadgeNameRow'+k+'" id="levelsbadgeNameRow'+k+'" /></td>';
							 		htmlStringLevel+='<td class="crud-content left-align top-align nowrap"> <textarea name="levelsbadgeDescRow'+k+'" id="levelsbadgeDescRow'+k+'" /></td>';
							 		htmlStringLevel+='<td><input type="hidden" id="levelsIdRow'+k+'" name="levelsIdRow'+k+'" value="'+objectLevel.levels[k].id+'" /></td>';
							 		htmlStringLevel+='<td><input type="hidden" id="levelsNameRow'+k+'" name="levelsNameRow'+k+'" value="'+objectLevel.levels[k].levelName+'" /></td>';
							 		htmlStringLevel+='<td><input type="hidden" id="countryRow'+k+'" name="countryRow'+k+'" value="'+objectLevel.levels[k].countryId+'" /></td>';
							 		htmlStringLevel+='<td id="checkBoxLevelDiv"><input type="checkbox" name="levelStringRow" id="levelStringRow'+k+'" value="'+objectLevel.levels[k].levelName+'" /></td>';
							 		htmlStringLevel+='</tr>';
					     	  }
					     	htmlStringLevel+='<input type="hidden" id="totalLevelsLength" name="totalLevelsLength" value="'+objectLevel.levels.length+'" />';
							 $("#earnedNotEarnedBadgeLevelDiv").html(htmlStringLevel);
							 for(var leveltdLen=0, leveldivLen=objectLevel.levels.length; leveltdLen < leveldivLen; leveltdLen++)
						     {
						     		$("#levelStringRow"+leveltdLen).hide();
						     }
				     	 }
						 else
						 {
							 var noLevelMessage='<cms:contentText key="NO_LEVELS" code="gamification.admin.labels" />';
							 htmlStringLevel+='<tr class="crud-table-row2"><td class="crud-content left-align top-align nowrap">'+noLevelMessage+'</td>';
							 $("#earnedNotEarnedBadgeLevelDiv").html(htmlStringLevel);
						 }
						  $("#earnedNotEarnedBadgePointDiv").hide();
						 $("#earnedNotEarnedBadgeLevelDiv").show();

					  }
					}
				}
			});

	 		$("#behaviorBadgeDiv").hide();
	 		$("#progressBadgeDiv").hide();
	 		$("#fileLoadBadgeDiv").hide();
	 		$("#fileLoadForNoPromoDiv").hide();
		}
	});

});
 function handleClick(){

	    if (document.getElementById("undefeatedBadge").checked)
	    {
	    	//alert("undefeated is checked");
	        $("#undefeatedBadgeContent").show();
	    }
	    else
	    {
	    	//alert("undefeated is not checked");
	    	$("#undefeatedBadgeContent").hide();
	    }

 }



</script>
