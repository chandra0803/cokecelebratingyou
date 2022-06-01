/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/sysadmin/CacheManagementController.java,v $
 */

package com.biperf.core.ui.sysadmin;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.country.Country;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.StringUtil;

/**
 * JournalStatsController gets current journal ID.
 */
public class JournalStatsController extends BaseController
{
  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    final Log logger = LogFactory.getLog( JournalStatsController.class );

    String countryCode = request.getParameter( "code" );
    String journalId = "-1";

    if ( !StringUtil.isEmpty( countryCode ) )
    {
      Country country = getCountryService().getCountryByCode( countryCode );
      String campaignNumber = null != country ? country.getCampaignNbr() : "";

      try
      {
        if ( !StringUtil.isEmpty( campaignNumber ) )
        {
          journalId = getAwardBanQService().getCurrentJournalId( campaignNumber );
        }
      }
      catch( Exception e )
      {
        logger.error( "Failed to fetch requested stat", e );
      }
    }

    request.setAttribute( "stat", journalId );
  }

  private CountryService getCountryService()
  {
    return (CountryService)BeanLocator.getBean( CountryService.BEAN_NAME );
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory awardBanQServiceFactory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );
    return awardBanQServiceFactory.getAwardBanQService();
  }
}
