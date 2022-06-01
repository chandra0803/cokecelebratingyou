
package com.biperf.core.ui.reports.budget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.ui.reports.ReportParameterInfo;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.value.budget.BudgetBalanceReportValue;
import com.biperf.util.StringUtils;

public class BudgetBalanceReportAction extends BudgetReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.BUDGET_BALANCE;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;

    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    reportParametersForm.setDrilldownPromoId( null );
    
    if ( !isChangeFilterPromotionSelected( reportParametersForm ) )
    {
      reportParameters.put( "promotionId", null );
    }

    List<String> requiredFields = new ArrayList<String>();
    
    if ( isChangeFiltersPage( request ) && hasErrorMessages( request, reportParameters, requiredFields, false ) )
    {
      return mapping.findForward( "display_budget_balance_summary" );
    }
    if ( StringUtils.isEmpty( request.getParameter( CLEAR_FORM ) )
        || isDashboardPage( request ) )
    {
      //-------------------------------
      // Client customization for WIP #27192 start
      //-------------------------------
    if ( ! getAuthorizationService().isUserInRole( "ROLE_BI_ADMIN" ) && ! getAuthorizationService().isUserInRole( "ROLE_CLIENT_ADMIN" ) )
     {
        String rptPromoId = (String)reportParameters.get( "promotionId" );
        if(rptPromoId != null)
        {
	        Long rptPromoIdLong = getPromoIdAsLong(rptPromoId);
	        
	    	if( rptPromoIdLong != null )
	    	{
	    		Promotion promotion = getPromotionService().getPromotionById( rptPromoIdLong );
	
	    	if ( promotion.isUtilizeParentBudgets() )	
	    	{    	    
	        	String parmParentNodeId = (String)reportParameters.get( "parentNodeId" );
	      	  
	      	  	Long nodeId=Long.parseLong(parmParentNodeId);
	      	  	
	        	Long parentNodeId=getFirstBudgetUpHierarchy(nodeId);
	            reportParameters.put( "parentNodeId", parentNodeId.toString() );    		    		
	    	}
        }
       }
     }
    	
      //-------------------------------
      // Client customization for WIP #27192 end
      //-------------------------------    	
      Map<String, Object> outMap = getBudgetReportsService().getBudgetBalanceTabularResults( reportParameters );
      List<BudgetBalanceReportValue> reportData = (List<BudgetBalanceReportValue>)outMap.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      Integer maxRows = (Integer)outMap.get( OUTPUT_SIZE_DATA );
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) && !"shared".equals( reportParameters.get( "budgetDistribution" ) ) )
      {
        BudgetBalanceReportValue totalsRowData = (BudgetBalanceReportValue)outMap.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "reportDetailType", "topLevel" );
    }
    else
    {
      request.setAttribute( "hideCharts", Boolean.TRUE );
    }
    return mapping.findForward( "display_budget_balance_summary" );
  }

  private boolean isChangeFilterPromotionSelected( ReportParametersForm reportParametersForm )
  {
    List<ReportParameterInfo> reportParameterInfoList = reportParametersForm.getReportParameterInfoList();
    boolean isSelected = false;
    for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
    {
      if ( "promotionId".equals( reportParameterInfo.getName() ) && !StringUtils.isEmpty( reportParameterInfo.getParameterValue() ) )
      {
        isSelected = true;
      }
    }
    return isSelected;
  }

  public ActionForward displayBudgetByPromotionSummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    reportParameters.put( "promotionId", reportParametersForm.getDrilldownPromoId() );
    //-------------------------------
    // Client customization for WIP #27192 start
    //-------------------------------
  	
    if ( ! getAuthorizationService().isUserInRole( "ROLE_BI_ADMIN" ) && ! getAuthorizationService().isUserInRole( "ROLE_CLIENT_ADMIN" ) )
    {
       String rptPromoId = (String)reportParameters.get( "promotionId" );
       if(rptPromoId != null)
       {
	        Long rptPromoIdLong = getPromoIdAsLong(rptPromoId);
	        if( rptPromoIdLong != null )
	    	{
	    	Promotion promotion = getPromotionService().getPromotionById( rptPromoIdLong );
	
	    	if ( promotion.isUtilizeParentBudgets() )	
	    	{    	    
	        	String parmParentNodeId = (String)reportParameters.get( "parentNodeId" );
	      	  
	      	  	Long nodeId=Long.parseLong(parmParentNodeId);
	      	  	
	        	Long parentNodeId=getFirstBudgetUpHierarchy(nodeId);
	            reportParameters.put( "parentNodeId", parentNodeId.toString() );    		    		
	    	}
       }
      }	        
    }
  	
    //-------------------------------
    // Client customization for WIP #27192 end
    //-------------------------------   
    Map<String, Object> outMap = getBudgetReportsService().getBudgetBalanceByPromotionTabularResults( reportParameters );
    List<BudgetBalanceReportValue> reportData = (List<BudgetBalanceReportValue>)outMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)outMap.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) && !"shared".equals( reportParameters.get( "budgetDistribution" ) ) )
    {
      BudgetBalanceReportValue totalsRowData = (BudgetBalanceReportValue)outMap.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    request.setAttribute( "reportDetailType", "promoLevel" );
    return mapping.findForward( "display_budget_balance_by_promotion_summary" );
  }

  public ActionForward displayBudgetDepositedDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    //-------------------------------
    // Client customization for WIP #27192 start
    //-------------------------------
  	
    if ( ! getAuthorizationService().isUserInRole( "ROLE_BI_ADMIN" ) && ! getAuthorizationService().isUserInRole( "ROLE_CLIENT_ADMIN" ) )
    {
       String rptPromoId = (String)reportParameters.get( "promotionId" );
       if(rptPromoId != null)
       {
	        Long rptPromoIdLong = getPromoIdAsLong(rptPromoId);
	        if( rptPromoIdLong != null )
	    	{
	    	Promotion promotion = getPromotionService().getPromotionById( rptPromoIdLong );
	
	    	if ( promotion.isUtilizeParentBudgets() )	
	    	{    	    
	        	String parmParentNodeId = (String)reportParameters.get( "parentNodeId" );
	      	  
	      	  	Long nodeId=Long.parseLong(parmParentNodeId);
	      	  	
	        	Long parentNodeId=getFirstBudgetUpHierarchy(nodeId);
	            reportParameters.put( "parentNodeId", parentNodeId.toString() );    		    		
	    	}
       }
       }
    }
  	
    //-------------------------------
    // Client customization for WIP #27192 end
    //-------------------------------   

    Map<String, Object> outMap = getBudgetReportsService().getBudgetDepositedDetailTabularResults( reportParameters );
    List<BudgetBalanceReportValue> reportData = (List<BudgetBalanceReportValue>)outMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_budget_deposited_detail" );
  }

  public ActionForward displayBudgetIssuedDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    //-------------------------------
    // Client customization for WIP #27192 start
    //-------------------------------
  	
    if ( ! getAuthorizationService().isUserInRole( "ROLE_BI_ADMIN" ) && ! getAuthorizationService().isUserInRole( "ROLE_CLIENT_ADMIN" ) )
    {
       String rptPromoId = (String)reportParameters.get( "promotionId" );
       if(rptPromoId != null)
       {
	        Long rptPromoIdLong = getPromoIdAsLong(rptPromoId);
	        if( rptPromoIdLong != null )
	    	{
	    	Promotion promotion = getPromotionService().getPromotionById( rptPromoIdLong );
	
	    	if ( promotion.isUtilizeParentBudgets() )	
	    	{    	    
	        	String parmParentNodeId = (String)reportParameters.get( "parentNodeId" );
	      	  
	      	  	Long nodeId=Long.parseLong(parmParentNodeId);
	      	  	
	        	Long parentNodeId=getFirstBudgetUpHierarchy(nodeId);
	            reportParameters.put( "parentNodeId", parentNodeId.toString() );    		    		
	    	}
       }
       }
    }
  	
    //-------------------------------
    // Client customization for WIP #27192 end
    //-------------------------------    
    Map<String, Object> outMap = getBudgetReportsService().getBudgetIssuedDetailTabularResults( reportParameters );
    List<BudgetBalanceReportValue> reportData = (List<BudgetBalanceReportValue>)outMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_budget_issued_detail" );
  }

  public ActionForward displayBudgetAllocatedDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    //-------------------------------
    // Client customization for WIP #27192 start
    //-------------------------------
  	
    if ( ! getAuthorizationService().isUserInRole( "ROLE_BI_ADMIN" ) && ! getAuthorizationService().isUserInRole( "ROLE_CLIENT_ADMIN" ) )
    {
       String rptPromoId = (String)reportParameters.get( "promotionId" );
       if(rptPromoId != null)
       {
	        Long rptPromoIdLong = getPromoIdAsLong(rptPromoId);
	        if( rptPromoIdLong != null )
	    	{
	    	Promotion promotion = getPromotionService().getPromotionById( rptPromoIdLong );
	
	    	if ( promotion.isUtilizeParentBudgets() )	
	    	{    	    
	        	String parmParentNodeId = (String)reportParameters.get( "parentNodeId" );
	      	  
	      	  	Long nodeId=Long.parseLong(parmParentNodeId);
	      	  	
	        	Long parentNodeId=getFirstBudgetUpHierarchy(nodeId);
	            reportParameters.put( "parentNodeId", parentNodeId.toString() );    		    		
	    	}
       }
       }
    }
  	
    //-------------------------------
    // Client customization for WIP #27192 end
    //-------------------------------   
    Map<String, Object> outMap = getBudgetReportsService().getBudgetAllocatedDetailTabularResults( reportParameters );
    List<BudgetBalanceReportValue> reportData = (List<BudgetBalanceReportValue>)outMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_budget_allocated_detail" );
  }

  public ActionForward displayUtilizationPercentageChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    if( reportParametersForm.getDrilldownPromoId() != null )
    {
      reportParameters.put( "promotionId", reportParametersForm.getDrilldownPromoId() );
    }
    //-------------------------------
    // Client customization for WIP #27192 start
    //-------------------------------
  	
    if ( ! getAuthorizationService().isUserInRole( "ROLE_BI_ADMIN" ) && ! getAuthorizationService().isUserInRole( "ROLE_CLIENT_ADMIN" ) )
    {
       String rptPromoId = (String)reportParameters.get( "promotionId" );
       if(rptPromoId != null)
       {
	        Long rptPromoIdLong = getPromoIdAsLong(rptPromoId);
	        if( rptPromoIdLong != null )
	    	{
	    	Promotion promotion = getPromotionService().getPromotionById( rptPromoIdLong );
	
	    	if ( promotion.isUtilizeParentBudgets() )	
	    	{    	    
	        	String parmParentNodeId = (String)reportParameters.get( "parentNodeId" );
	      	  
	      	  	Long nodeId=Long.parseLong(parmParentNodeId);
	      	  	
	        	Long parentNodeId=getFirstBudgetUpHierarchy(nodeId);
	            reportParameters.put( "parentNodeId", parentNodeId.toString() );    		    		
	    	}
       }
       }
    }
  	
    //-------------------------------
    // Client customization for WIP #27192 end
    //-------------------------------   
    Map<String, Object> outMap = getBudgetReportsService().getBudgetUtilizationByPercentageChart( reportParameters );
    List<BudgetBalanceReportValue> reportData = (List<BudgetBalanceReportValue>)outMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_budget_balance_utilization_percentage" );
  }

  public ActionForward displayUtilizationLevelChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    if( reportParametersForm.getDrilldownPromoId() != null )
    {
      reportParameters.put( "promotionId", reportParametersForm.getDrilldownPromoId() );
    }
    //-------------------------------
    // Client customization for WIP #27192 start
    //-------------------------------
  	
    if ( ! getAuthorizationService().isUserInRole( "ROLE_BI_ADMIN" ) && ! getAuthorizationService().isUserInRole( "ROLE_CLIENT_ADMIN" ) )
    {
       String rptPromoId = (String)reportParameters.get( "promotionId" );
       if(rptPromoId != null)
       {
	        Long rptPromoIdLong = getPromoIdAsLong(rptPromoId);
	        if( rptPromoIdLong != null )
	    	{
	    	Promotion promotion = getPromotionService().getPromotionById( rptPromoIdLong );
	
	    	if ( promotion.isUtilizeParentBudgets() )	
	    	{    	    
	        	String parmParentNodeId = (String)reportParameters.get( "parentNodeId" );
	      	  
	      	  	Long nodeId=Long.parseLong(parmParentNodeId);
	      	  	
	        	Long parentNodeId=getFirstBudgetUpHierarchy(nodeId);
	            reportParameters.put( "parentNodeId", parentNodeId.toString() );    		    		
	    	}
       }
       }
    }
  	
    //-------------------------------
    // Client customization for WIP #27192 end
    //-------------------------------   
    Map<String, Object> outMap = getBudgetReportsService().getBudgetUtilizationInPointsChart( reportParameters );
    List<BudgetBalanceReportValue> reportData = (List<BudgetBalanceReportValue>)outMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_budget_balance_utilization_level" );
  }

  protected CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }
  
  public Long getPromoIdAsLong( String promoId )
  {
    if ( NumberUtils.isNumber( promoId ) )
    {
      return NumberUtils.createLong( promoId );
    }
    else
    {
      return null;
    }
  }
  /** Override this and return the results from the appropriate extract service method */
  protected   Map doSecondaryExtractResultsCall( Map<String, Object> reportParameters ){return null;}
  /** Override this and return the results from the appropriate extract service method */
  protected   Map doExtractResultsCall( Map<String, Object> reportParameters ){return null;}

  protected PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
  
  protected AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)getService( AuthorizationService.BEAN_NAME );
  }

@Override
protected Map doBudgetSecondLevelExtractResultsCall(Map<String, Object> reportParameters) {
	// TODO Auto-generated method stub
	return null;
}

@Override
protected Map doBudgetThirdLevelExtractResultsCall(Map<String, Object> reportParameters) {
	// TODO Auto-generated method stub
	return null;
}
}
