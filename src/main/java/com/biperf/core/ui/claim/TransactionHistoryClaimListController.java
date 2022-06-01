/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/TransactionHistoryClaimListController.java,v $
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
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.dao.promotion.hibernate.GoalQuestPromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.NominationPromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.ProductClaimPromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.QuizPromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.RecognitionPromotionQueryConstraint;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.TransactionHistoryModuleAwareType;
import com.biperf.core.domain.enums.TransactionHistoryType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * TransactionHistoryClaimListController.
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
public class TransactionHistoryClaimListController extends BaseController
{
  private static final Log logger = LogFactory.getLog( TransactionHistoryClaimListController.class );

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the "no promotion type" version of the Transaction History page.
   * 
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for the servlets for this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    // List promotionTypeList = TransactionHistoryType.getList();
    List transactionHistoryType = TransactionHistoryModuleAwareType.getListForTransactionHistory();
    List promotionList = new ArrayList();
    String userId = null;
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
        userId = (String)clientStateMap.get( "userId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "userId" );
        userId = id.toString();
      }
      if ( userId != null )
      {
        Participant participant = getParticipantService().getParticipantById( new Long( userId ) );

        String promotionType = request.getParameter( "promotionType" );
        promotionList = getPromotionList( (String)request.getAttribute( "promotionType" ) );

        if ( promotionType == null || promotionType.equals( "" ) )
        {
          request.setAttribute( "message", "Please select a type and click Show Transactions." );
        }
        request.setAttribute( "promotionList", promotionList );
        request.setAttribute( "transactionHistoryType", transactionHistoryType );
        request.setAttribute( "participant", participant );
      }
      else
      {
        logger.error( "userId was not part of client state" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    tileContext.putAttribute( "transactionList", "/claim/emptyTransactionList.jsp" );
    return;
  }

  // ---------------------------------------------------------------------------
  // Protected Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns valid promotions for the given transaction history type.
   * 
   * @param transactionHistoryType return promotions for this transaction history type.
   * @return valid promotions of the given promotion type, as a <code>List</code> of
   *         {@link Promotion} objects.
   */
  protected List getPromotionList( String transactionHistoryType )
  {
    List promotionList = new ArrayList();

    if ( transactionHistoryType != null )
    {
      if ( transactionHistoryType.equals( TransactionHistoryType.PRODUCT_CLAIM ) )
      {
        ProductClaimPromotionQueryConstraint productClaimPromotionQueryConstraint = new ProductClaimPromotionQueryConstraint();
        productClaimPromotionQueryConstraint.setMasterOrChildConstraint( Boolean.TRUE );
        productClaimPromotionQueryConstraint
            .setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
        promotionList = (ArrayList)getPromotionService().getPromotionList( productClaimPromotionQueryConstraint );

      }
      else if ( transactionHistoryType.equals( TransactionHistoryType.NOMINATION ) )
      {
        NominationPromotionQueryConstraint queryConstraint = new NominationPromotionQueryConstraint();
        queryConstraint
            .setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
        promotionList = (ArrayList)getPromotionService().getPromotionList( queryConstraint );
      }
      else if ( transactionHistoryType.equals( TransactionHistoryType.RECOGNITION ) )
      {

        RecognitionPromotionQueryConstraint recognitionPromotionQueryConstraint = new RecognitionPromotionQueryConstraint();
        recognitionPromotionQueryConstraint
            .setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );

        promotionList = (ArrayList)getPromotionService().getPromotionList( recognitionPromotionQueryConstraint );

      }
      else if ( transactionHistoryType.equals( TransactionHistoryType.QUIZ ) )
      {

        QuizPromotionQueryConstraint quizPromotionQueryConstraint = new QuizPromotionQueryConstraint();
        quizPromotionQueryConstraint
            .setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );

        promotionList = (ArrayList)getPromotionService().getPromotionList( quizPromotionQueryConstraint );
      }

      else if ( transactionHistoryType.equals( TransactionHistoryType.GOALQUEST ) )
      {

        GoalQuestPromotionQueryConstraint gqPromotionQueryConstraint = new GoalQuestPromotionQueryConstraint();
        gqPromotionQueryConstraint
            .setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );

        promotionList = (ArrayList)getPromotionService().getPromotionList( gqPromotionQueryConstraint );
      }
      else if ( transactionHistoryType.equals( TransactionHistoryType.DISCRETIONARY ) )
      {

        PromotionQueryConstraint promotionQueryConstraint = new PromotionQueryConstraint();
        promotionQueryConstraint
            .setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
        promotionQueryConstraint.setPromotionTypesExcluded( new PromotionType[] { PromotionType.lookup( PromotionType.SURVEY ) } );

        promotionList = (ArrayList)getPromotionService().getPromotionList( promotionQueryConstraint );
      }
      else if ( transactionHistoryType.equals( TransactionHistoryType.REVERSAL ) )
      {

        PromotionQueryConstraint promotionQueryConstraint = new PromotionQueryConstraint();
        promotionQueryConstraint
            .setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
        promotionQueryConstraint.setPromotionTypesExcluded( new PromotionType[] { PromotionType.lookup( PromotionType.SURVEY ) } );

        promotionList = (ArrayList)getPromotionService().getPromotionList( promotionQueryConstraint );
      }
    }

    PropertyComparator.sort( promotionList, new MutableSortDefinition( "name", true, true ) );

    return promotionList;
  }

  // ---------------------------------------------------------------------------
  // Service Getter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the participant service.
   * 
   * @return a reference to the participant service.
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Returns the promotion service.
   * 
   * @return a reference to the promotion service.
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}
