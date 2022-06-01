<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

<%
response.setHeader("Cache-Control","cache, must-revalidate"); 
response.setHeader("Pragma","public"); 
%>

{
  "categories": [
    {
      "category": [
      	{
          	"label": "<cms:contentText key="COMPANY_AVERAGE" code="${report.cmAssetCode}"/>"
        },
        {
          	"label": "<cms:contentText key="ORG_AVERAGE" code="${report.cmAssetCode}"/>"
        },
        {
			"label": "${reportData.userName}"
	  	}
      ]
    }
  ],
  "dataset": [    
	{    
		"seriesname": "<cms:contentText key="GIVEN" code="${report.cmAssetCode}"/>",
		"data": [      			      		
	        {
	          "value": "<fmt:formatNumber value="${reportData.companyAvgRecognitionsSent}"/>"
	        },
	        {
	          "value": "<fmt:formatNumber value="${reportData.orgAvgRecognitionsSent}"/>"
	        },
	        {
	          "value": "<fmt:formatNumber value="${reportData.userRecognitionsSent}"/>"
	        }        
		]
	},
	{    
		"seriesname": "<cms:contentText key="RECEIVED" code="${report.cmAssetCode}"/>",
		"data": [      			      		
	        {
	          "value": "<fmt:formatNumber value="${reportData.companyAvgRecognitionsRcvd}"/>"
	        },
	        {
	          "value": "<fmt:formatNumber value="${reportData.orgAvgRecognitionsRcvd}"/>"
	        },
	        {
	          "value": "<fmt:formatNumber value="${reportData.userRecognitionsRcvd}"/>"
	        }        
		]
	}
  ]
}