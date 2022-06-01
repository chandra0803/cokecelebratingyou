/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/Attic/ParticipantThrowdownStatsController.java,v $
 *
 */

package com.biperf.core.ui.participant;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.impl.PromotionServiceImpl;
import com.biperf.core.ui.BaseController;
import com.biperf.core.value.PromotionPaxValue;

public class ParticipantThrowdownStatsController extends BaseController
{
  /**
   * Will preload a list of all Throwdown participant employments and put them in the request scope.
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param componentContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext componentContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    ParticipantThrowdownStatsForm participantThrowdownStatsForm = (ParticipantThrowdownStatsForm)request.getAttribute( "participantThrowdownStatsForm" );
    List paxPromotions = getPromotionService().getAllLiveAndExpiredByTypeAndUserId( PromotionType.THROWDOWN, new Long( participantThrowdownStatsForm.getUserId() ) );
    if ( !paxPromotions.isEmpty() )
    {
      for ( Iterator iter = paxPromotions.iterator(); iter.hasNext(); )
      {
        PromotionPaxValue promoPax = (PromotionPaxValue)iter.next();
        if ( promoPax.getRoleKey().equals( PromotionServiceImpl.THROWDOWN_PRIMARY ) )
        {
          iter.remove();
        }
      }
    }
    participantThrowdownStatsForm.setPromotions( paxPromotions );

    // for displaying name
    if ( participantThrowdownStatsForm.getUserId() > 0 )
    {
      String requestUserId = String.valueOf( participantThrowdownStatsForm.getUserId() );
      request.setAttribute( "displayNameUserId", requestUserId );
    }

  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}
