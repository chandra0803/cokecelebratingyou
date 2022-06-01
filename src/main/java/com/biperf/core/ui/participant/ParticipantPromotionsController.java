/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/ParticipantPromotionsController.java,v $
 */

package com.biperf.core.ui.participant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.impl.PromotionServiceImpl;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.value.PromotionPaxValue;
import com.biperf.util.StringUtils;

/*
 * ParticipantPromotionsController <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Nov
 * 16, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ParticipantPromotionsController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
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
    ParticipantPromotionsForm paxPromoForm = (ParticipantPromotionsForm)request.getAttribute( "participantPromotionsForm" );
    List promotionList = (List)request.getAttribute( "promotionPaxValueList" );

    // if the session attribute is not set or is null, build a new one.
    if ( request.getSession().getAttribute( "moduleTypeList" ) == null )
    {
      SortedMap map = new TreeMap();

      if ( promotionList != null )
      {
        // build up the list of promotion types for the dropdown, the "all" option will be added on
        // the jsp
        for ( int i = 0; i < promotionList.size(); i++ )
        {
          PromotionPaxValue value = (PromotionPaxValue)promotionList.get( i );
          PromotionType promoType = PromotionType.lookup( value.getModuleCode() );
          map.put( promoType.getCode(), promoType.getName() );
        }
      }
      // put the map on the session for repeated use by the page.
      request.getSession().setAttribute( "moduleTypeList", map );
    }
    Integer size = new Integer( ( (Map)request.getSession().getAttribute( "moduleTypeList" ) ).size() );
    request.setAttribute( "moduleTypeListSize", size );

    // Convert the module codes to their display values
    List newValueHolderList = null;
    if ( promotionList != null )
    {
      newValueHolderList = new ArrayList( promotionList.size() );
      for ( int i = 0; i < promotionList.size(); i++ )
      {
        PromotionPaxValue value = (PromotionPaxValue)promotionList.get( i );
        value.setModuleCode( PromotionType.lookup( value.getModuleCode() ).getName() );
        if ( value.getPromotion().isChallengePointPromotion() )
        {
          // as the managerCanSelect option is removed, enabling the link for all pax
          value.setLinkEnable( true );
        }
        else if ( value.getPromotion().isGoalQuestPromotion() )
        {
          // ok, managers/node owners can not always participate
          value.setLinkEnable( true );
        }
        else if ( value.getPromotion().isThrowdownPromotion() )
        {
          if ( value.getRoleKey().equals( PromotionServiceImpl.THROWDOWN_PRIMARY ) )
          {
            // spectator
            value.setLinkEnable( false );
          }
          else
          {
            // competitor
            value.setLinkEnable( true );
          }
        }
        else if ( value.getPromotion().isRecognitionPromotion() && value.getRoleKey().equalsIgnoreCase( "RECOGNITION_SECONDARY" ) )
        {
          RecognitionPromotion recPromo = (RecognitionPromotion)value.getPromotion();
          if ( recPromo.isIncludePurl() )
          {
            List<PurlRecipient> purlRecipients = getPurlService().getAllNonExpiredPurlRecipients( new Long( paxPromoForm.getUserId() ), recPromo.getId() );

            if ( purlRecipients != null && purlRecipients.size() > 0 )
            {
              value.setLinkEnable( true );
              if ( purlRecipients.size() == 1 )
              {
                value.setPurlDetailLink( true );

                for ( PurlRecipient recipient : purlRecipients )
                {
                  value.setPurlRecipientId( recipient.getId() );
                }
              }
            }
          }
        }
        newValueHolderList.add( i, value );
      }
    }
    request.setAttribute( "participantPromotionsFormList", newValueHolderList );

    // for displaying name
    String userId = paxPromoForm.getUserId();
    if ( !StringUtils.isEmpty( userId ) )
    {
      request.setAttribute( "displayNameUserId", userId );
    }
  }

  /**
   * Gets the Participant Service
   * 
   * @return Participant Service
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  } // end getParticipantService

  private ChallengePointService getChallengePointService()
  {
    return (ChallengePointService)getService( ChallengePointService.BEAN_NAME );
  }

  private PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

}
