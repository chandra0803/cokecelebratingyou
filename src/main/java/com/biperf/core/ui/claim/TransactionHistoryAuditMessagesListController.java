/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claim/TransactionHistoryAuditMessagesListController.java,v $
 */

package com.biperf.core.ui.claim;

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

import com.biperf.core.domain.audit.PayoutCalculationAudit;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.audit.PayoutCalculationAuditService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * TransactionHistoryAuditMessagesListController.
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
 * <td>Aug 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 *          Exp $
 */
public class TransactionHistoryAuditMessagesListController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( TransactionHistoryAuditMessagesListController.class );

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
      Long claimId = null;
      Long userId = null;
      try
      {
        String claimIdString = (String)clientStateMap.get( "claimId" );
        claimId = new Long( claimIdString );
      }
      catch( ClassCastException e )
      {
        claimId = (Long)clientStateMap.get( "claimId" );
      }
      try
      {
        String userIdString = (String)clientStateMap.get( "userId" );
        if ( userIdString != null )
        {
          userId = new Long( userIdString );
        }
      }
      catch( ClassCastException e2 )
      {
        userId = (Long)clientStateMap.get( "userId" );
      }

      if ( claimId != null && userId != null )
      {
        Participant participant = getParticipantService().getParticipantById( userId );
        request.setAttribute( "participant", participant );

        Claim claim = getClaimService().getClaimById( claimId );
        request.setAttribute( "claim", claim );

        List auditMessages = new ArrayList( getPayoutCalculationAuditService().getPayoutCalculationAuditsByClaimIdAndParticipantId( claimId, userId ) );

        // sort auditMessages into 2 list (success and non-success)
        List failureMessages = new ArrayList();
        List successMessages = new ArrayList();
        for ( Iterator iter = auditMessages.iterator(); iter.hasNext(); )
        {
          PayoutCalculationAudit message = (PayoutCalculationAudit)iter.next();
          if ( message.getReasonType().isSuccessMessage() )
          {
            successMessages.add( message );
          }
          else
          {
            failureMessages.add( message );
          }
        }
        request.setAttribute( "failureMessages", failureMessages );
        request.setAttribute( "successMessages", successMessages );
      }
      else
      {
        LOG.error( "claimId or userId not found in client state" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

  }

  /**
   * Get the PromotionService from the beanLocator.
   * 
   * @return PromotionService
   */
  private PayoutCalculationAuditService getPayoutCalculationAuditService()
  {
    return (PayoutCalculationAuditService)getService( PayoutCalculationAuditService.BEAN_NAME );
  }

  /**
   * Get the PromotionService from the beanLocator.
   * 
   * @return PromotionService
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Get the ClaimService from the beanLocator.
   * 
   * @return ClaimService
   */
  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }
}
