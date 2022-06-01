/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/calculator/CalculatorCriterionAction.java,v $
 */

package com.biperf.core.ui.calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.calculator.CalculatorCriterionAssociationRequest;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.PresentationUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * CalculatorCriterionAction.
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
 * <td>sedey</td>
 * <td>May 24, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorCriterionAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( CalculatorCriterionAction.class );

  /**
   * Display CalculatorCriterion
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "display";
    logger.debug( ">>> " + METHOD_NAME );

    String forward = ActionConstants.SUCCESS_FORWARD;

    CalculatorCriterionForm criterionForm = (CalculatorCriterionForm)form;

    criterionForm.setMethod( "save" );
    Long criterionId = criterionForm.getCalculatorCriterionId();

    if ( criterionId != null && criterionId.longValue() > 0 )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new CalculatorCriterionAssociationRequest( CalculatorCriterionAssociationRequest.ALL ) );

      criterionForm.load( getCalculatorService().getCalculatorCriterionByIdWithAssociations( criterionId, associationRequestCollection ) );
    }

    logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
  }

  /**
   * Creates or updates a CalculatorCriterion
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "save";

    String forwardAction = ActionConstants.SUCCESS_FORWARD;

    ActionMessages errors = new ActionMessages();
    CalculatorCriterionForm criterionForm = (CalculatorCriterionForm)actionForm;

    String calculatorId = null;
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
        calculatorId = (String)clientStateMap.get( "calculatorId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "calculatorId" );
        calculatorId = id.toString();
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      forwardAction = ActionConstants.CANCEL_FORWARD;
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "calculatorId", calculatorId );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );

      return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString, "method=view" } );
    }

    try
    {
      getCalculatorService().saveCalculatorCriterion( new Long( calculatorId ), criterionForm.toDomainObject(), criterionForm.getCriterionText() );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardAction = ActionConstants.FAIL_FORWARD;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "calculatorId", calculatorId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString, "method=view" } );

  } // end create

  /**
   * Removes CalculatorCriterion
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward remove( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "remove";
    logger.debug( ">>> " + METHOD_NAME );

    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_DELETE ); // EARLY EXIT
    }

    CalculatorCriterionForm criterionForm = (CalculatorCriterionForm)form;
    String[] deletedIds = criterionForm.getDeletedRatings();
    log.debug( "deletedIds " + deletedIds.length );
    List list = PresentationUtils.convertStringArrayToListOfLongs( deletedIds );

    getCalculatorService().deleteCriterionRating( criterionForm.getCalculatorCriterionId(), list );

    logger.info( "<<< " + METHOD_NAME );

    return display( mapping, form, request, response );
  }

  /**
   * Reorder Rating within a Criterion
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward reorder( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "reorder";
    logger.debug( ">>> " + METHOD_NAME );
    CalculatorCriterionForm criterionForm = (CalculatorCriterionForm)form;
    String forward = ActionConstants.SUCCESS_UPDATE;
    ActionMessages errors = new ActionMessages();

    Long criterionId = new Long( 0 );
    Long ratingId = new Long( 0 );
    Long newIndex = new Long( 0 );

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
        criterionId = (Long)clientStateMap.get( "criterionId" );
      }
      catch( ClassCastException cce )
      {
        String critid = (String)clientStateMap.get( "criterionId" );
        criterionId = new Long( critid );
      }
      try
      {
        ratingId = (Long)clientStateMap.get( "criterionRatingId" );
      }
      catch( ClassCastException cce )
      {
        String rateid = (String)clientStateMap.get( "criterionRatingId" );
        ratingId = new Long( rateid );
      }

    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    try
    {
      // The requestUtils will throw an exception
      // if the required params are not in the request.
      // criterionId = criterionForm.getCalculatorCriterionId();
      // ratingId = criterionForm.getCriterionRatingId();
      newIndex = new Long( RequestUtils.getRequiredParamString( request, "newElementSequenceNum" ) );
    }
    catch( IllegalArgumentException e )
    {
      newIndex = new Long( criterionForm.getNewRatingSequenceNum() );
    }

    if ( errors.size() != 0 )
    {
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_UPDATE;
    }
    else
    {
      getCalculatorService().reorderRating( criterionId, ratingId, newIndex.intValue() );
    }

    logger.info( "<<< " + METHOD_NAME );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "calculatorCriterionId", criterionId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return new ActionForward( ClientStateUtils.generateEncodedLink( "", "calculatorCriterionView.do?method=display", clientStateParameterMap ), true );
  }

  private CalculatorService getCalculatorService()
  {
    return (CalculatorService)getService( CalculatorService.BEAN_NAME );
  }

} // end QuizFormAction
