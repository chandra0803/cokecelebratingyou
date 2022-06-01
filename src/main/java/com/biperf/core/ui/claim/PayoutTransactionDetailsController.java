/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claim/PayoutTransactionDetailsController.java,v $
 */

package com.biperf.core.ui.claim;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * PayoutTransactionDetailsController.
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
 * <td>robinsra</td>
 * <td>Sep 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PayoutTransactionDetailsController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( PayoutTransactionDetailsController.class );

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
    request.setAttribute( "reasonCodeList", PromotionApprovalOptionReasonType.getList() );
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
      Long transactionId = null;
      Long userId = null;

      try
      {
        String transactionIdString = (String)clientStateMap.get( "transactionId" );
        transactionId = new Long( transactionIdString );
      }
      catch( ClassCastException cce )
      {
        transactionId = (Long)clientStateMap.get( "transactionId" );
      }
      try
      {
        String userIdString = (String)clientStateMap.get( "userId" );
        userId = new Long( userIdString );
      }
      catch( ClassCastException cce2 )
      {
        userId = (Long)clientStateMap.get( "userId" );
      }
      if ( transactionId != null )
      {
        Journal journal = getJournalService().getJournalById( transactionId );
        request.setAttribute( "journal", journal );
        if ( journal != null )
        {
          request.setAttribute( "actionList", JournalStatusType.getListForCurrentStatus( journal.getJournalStatusType().getCode() ) );
        }
      }
      else
      {
        LOG.error( "transactionId not found in client state" );
      }
      if ( userId != null )
      {
        Participant participant = getParticipantService().getParticipantById( userId );
        request.setAttribute( "participant", participant );
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

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }
}
