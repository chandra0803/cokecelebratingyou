<%@ include file="/include/taglib.jspf"%>

 <SCRIPT TYPE="text/javascript">
      
      function showLayer(whichLayer)
      {
        if (document.getElementById)
        {
          // this is the way the standards work
          if (document.getElementById(whichLayer))
          {
	          var style2 = document.getElementById(whichLayer).style;
	          style2.display = "";
	      }
        }
        else if (document.all)
        {
          // this is the way old msie versions work
          var style2 = document.all[whichLayer].style;
          style2.display = "block";
        }
        else if (document.layers)
        {
          // this is the way nn4 works
          var style2 = document.layers[whichLayer].style;
          style2.display = "block";
        }
      }
      function hideLayer(whichLayer)
      {
        if (document.getElementById)
        {
          // this is the way the standards work
          if (document.getElementById(whichLayer))
          {
          var style2 = document.getElementById(whichLayer).style;
          style2.display = "none";
          }
        }
        else if (document.all)
        {
          // this is the way old msie versions work
          var style2 = document.all[whichLayer].style;
          style2.display = "none";
        }
        else if (document.layers)
        {
          // this is the way nn4 works
          var style2 = document.layers[whichLayer].style;
          style2.display = "none";
        }
      }
</SCRIPT>


<SCRIPT type="text/javascript">

  <c:if test="${promotionAwardsForm.awardsType == 'points'}">
        var awardAmountTypeFixedFalseObj = document.getElementById("awardAmountTypeFixedFalse");
        var awardAmountTypeFixedTrueObj = document.getElementById("awardAmountTypeFixedTrue");
        var fixedAmountObj = document.getElementById("fixedAmount");
        var rangeAmountMinObj = document.getElementById("rangeAmountMin");
        var rangeAmountMaxObj = document.getElementById("rangeAmountMax");
       
  </c:if>
  
  var awardsTypeObj = document.getElementById("awardsType");

  function updateLayersShown()
  {
 	enableFields();
  }

  updateLayersShown();

  function enableFields()
  {
    var awardsActiveFalseObj = document.getElementById("awardsActiveFalse"); //"Awards Active? No"
    var disabled = awardsActiveFalseObj.checked == true;
    // if promotion is expired then disabled = true;
    <c:if test="${promotionStatus=='expired'}">
      disabled = true;
    </c:if>           
    //Below Awards fields follow the selection made in the "Awards Active?" radio button
    awardsTypeObj.disabled=disabled;

    <c:if test="${promotionAwardsForm.awardsType == 'points'}">
		awardAmountTypeFixedFalseObj.disabled=disabled;
		awardAmountTypeFixedTrueObj.disabled=disabled;
		fixedAmountObj.disabled=disabled;
		rangeAmountMinObj.disabled=disabled;
		rangeAmountMaxObj.disabled=disabled;
	</c:if>
  }

</SCRIPT>