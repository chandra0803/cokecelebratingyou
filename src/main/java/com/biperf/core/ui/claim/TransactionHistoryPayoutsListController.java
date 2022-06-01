/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claim/TransactionHistoryPayoutsListController.java,v $
 */

package com.biperf.core.ui.claim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * TransactionHistoryPayoutsListController.
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
 */
public class TransactionHistoryPayoutsListController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( TransactionHistoryPayoutsListController.class );

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
    List payouts = new ArrayList();

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
        if ( claimIdString != null && claimIdString.length() > 0 )
        {
          claimId = new Long( claimIdString );
        }
      }
      catch( ClassCastException cce )
      {
        claimId = (Long)clientStateMap.get( "claimId" );
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

      if ( claimId != null && userId != null )
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.ACTIVITY_JOURNALS ) );

        payouts = new ArrayList( getJournalService().getJournalsByClaimIdAndUserId( claimId, userId, associationRequestCollection ) );
        request.setAttribute( "payouts", payouts );

        User user = getUserService().getUserById( userId );
        request.setAttribute( "user", user );
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
  private JournalService getJournalService()
  {
    return (JournalService)getService( JournalService.BEAN_NAME );
  }

  /**
   * Get the EmployerService from the beanLocator.
   * 
   * @return EmployerService
   * @throws Exception
   */
  private UserService getUserService() throws Exception
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }
}
