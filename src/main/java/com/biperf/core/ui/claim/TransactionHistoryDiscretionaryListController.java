/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/TransactionHistoryDiscretionaryListController.java,v $
 */

package com.biperf.core.ui.claim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.domain.claim.DiscretionaryHistoryValueObject;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.TransactionHistoryModuleAwareType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.util.StringUtils;

/**
 * TransactionHistoryDiscretionaryListController
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
 * <td>gaddam</td>
 * <td>Nov 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 *          Exp $
 */
public class TransactionHistoryDiscretionaryListController extends TransactionHistoryClaimListController
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
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    // List promotionTypeList = TransactionHistoryType.getList();
    List transactionHistoryType = TransactionHistoryModuleAwareType.getListForTransactionHistory();
    List promotionList = null;
    List valueObjects = null;
    String promotionType = (String)request.getAttribute( "promotionType" );
    promotionList = getPromotionList( promotionType );
    Long participantId = null;
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
      try
      {
        participantId = (Long)clientStateMap.get( "userId" );
      }
      catch( ClassCastException cce )
      {
        participantId = new Long( (String)clientStateMap.get( "userId" ) );
      }
      if ( StringUtils.isEmpty( promotionType ) )
      {
        promotionType = (String)clientStateMap.get( "promotionType" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    Participant participant = getParticipantService().getParticipantById( participantId );

    Long promotionId = null;
    Boolean open = new Boolean( true );

    if ( ! ( request.getParameter( "open" ) == null || request.getParameter( "open" ).equals( "" ) ) )
    {
      open = new Boolean( request.getParameter( "open" ) );
    }

    if ( request.getAttribute( "promotionId" ) != null )
    {
      if ( !request.getAttribute( "promotionId" ).equals( "" ) )
      {
        promotionId = new Long( (String)request.getAttribute( "promotionId" ) );
      }
    }
    valueObjects = getJournalList( promotionId, participantId );
    tileContext.putAttribute( "transactionList", "/claim/discretionaryTransactionList.jsp" );
    request.setAttribute( "valueObjects", valueObjects );
    request.setAttribute( "promotionTypeCode", promotionType + ".history" );
    request.setAttribute( "transactionHistoryType", transactionHistoryType );
    request.setAttribute( "promotionList", promotionList );
    request.setAttribute( "participant", participant );
    request.setAttribute( "open", open );
  }

  private List getJournalList( Long promotionId, Long participantId )
  {
    List valueObjects = new ArrayList();

    // need to do the association to read the Promotion obj from Journal obj in jsp
    AssociationRequestCollection journalAssociationRequestCollection = new AssociationRequestCollection();
    journalAssociationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION ) );

    // create the constraint
    JournalQueryConstraint queryConstraint = new JournalQueryConstraint();
    queryConstraint.setPromotionId( promotionId );
    queryConstraint.setParticipantId( participantId );
    queryConstraint.setJournalTypesIncluded( new String[] { Journal.DISCRETIONARY } );

    List journalList = getJournalService().getJournalList( queryConstraint, journalAssociationRequestCollection );
    // Create the list of value objects.
    for ( Iterator iter = journalList.iterator(); iter.hasNext(); )
    {
      Journal journal = (Journal)iter.next();

      DiscretionaryHistoryValueObject valueObject = new DiscretionaryHistoryValueObject();
      valueObject.setPromotionName( journal.getPromotion().getName() );
      valueObject.setPromotionTypeName( journal.getPromotion().getPromotionType().getName() );
      valueObject.setTransactionDate( journal.getTransactionDate() );
      valueObject.setAwardQuantity( journal.getTransactionAmount() );
      if ( journal.getPromotion().getAwardType() != null )
      {
        valueObject.setAwardTypeName( PromotionAwardsType.lookup( journal.getPromotion().getAwardType().getCode() ).getName() );
      }
      else if ( journal.getAwardPayoutType() != null )
      {
        valueObject.setAwardTypeName( journal.getAwardPayoutType().getName() );
      }
      else
      {
        valueObject.setAwardTypeName( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ).getName() );
      }
      valueObject.setJournalStatus( journal.getJournalStatusType().getName() );
      valueObject.setJournalComments( journal.getComments() );

      valueObjects.add( valueObject );
    }
    return valueObjects;
  }

  /**
   * Get the JournalService from the beanLocator.
   * 
   * @return JournalService
   */
  private JournalService getJournalService()
  {
    return (JournalService)getService( JournalService.BEAN_NAME );
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
}
