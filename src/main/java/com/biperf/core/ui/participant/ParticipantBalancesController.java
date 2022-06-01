/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/ParticipantBalancesController.java,v $
 */

package com.biperf.core.ui.participant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ParticipantBalancesController.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>zahler</td>
 * <td>Nov 15, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantBalancesController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( ParticipantBalancesController.class );

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
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      String userIdString = (String)clientStateMap.get( "userId" );
      Long userId = new Long( userIdString );
      if ( userId != null )
      {
        List paxBalances = new ArrayList();

        List mediaTypes = PromotionAwardsType.getList();

        for ( Iterator iter = mediaTypes.iterator(); iter.hasNext(); )
        {
          PromotionAwardsType mediaType = (PromotionAwardsType)iter.next();

          ParticipantBalancesFormBean formBean = new ParticipantBalancesFormBean();

          Long totalEarned = getJournalService().getTotalEarningsByMediaTypeAndUserId( userId, mediaType.getCode() );
          if ( totalEarned == null )
          {
            totalEarned = new Long( 0 );
          }
          formBean.setAwardType( mediaType.getName() );
          formBean.setTotalEarned( totalEarned );

          // If mediaType is perqs, go out to awardbanq to get the balance
          if ( mediaType.getCode().equals( PromotionAwardsType.POINTS ) )
          {
            Long currentBalance = getAwardBanQService().getAccountBalanceForParticipantId( userId );
            if ( currentBalance == null )
            {
              currentBalance = new Long( 0 );
            }
            formBean.setCurrentBalance( currentBalance );
          }

          paxBalances.add( formBean );
        }

        request.setAttribute( "paxBalances", paxBalances );
        request.setAttribute( "displayNameUserId", String.valueOf( userId ) );
      }
      else
      {
        LOG.error( "userId not found in client state" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }

  private JournalService getJournalService()
  {
    return (JournalService)getService( JournalService.BEAN_NAME );
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );

    return factory.getAwardBanQService();
  }

}
