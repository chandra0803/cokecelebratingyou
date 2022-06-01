/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/calculator/CalculatorAction.java,v $
 */

package com.biperf.core.ui.calculator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.util.CollectionUtils;

import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.calculator.CalculatorCriterionRating;
import com.biperf.core.domain.enums.CalculatorStatusType;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.calculator.CalculatorAssociationRequest;
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
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * QuizFormAction.
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
 * <td>jenniget</td>
 * <td>Oct 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( CalculatorAction.class );

  /**
   * Creates or updates a quiz form
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

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();
    CalculatorForm calculatorForm = (CalculatorForm)actionForm;
    String forwardAction = "";
    ViewCalculatorForm viewCalculatorForm = new ViewCalculatorForm();
    try
    {
      Calculator calculator = getCalculatorService().saveCalculator( calculatorForm.toDomainObject() );
      AssociationRequestCollection assocReqs = new AssociationRequestCollection();
      assocReqs.add( new CalculatorAssociationRequest( CalculatorAssociationRequest.ALL ) );
      viewCalculatorForm.load( getCalculatorService().getCalculatorByIdWithAssociations( calculator.getId(), assocReqs ) );
      request.setAttribute( "viewCalculatorForm", viewCalculatorForm );
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
    else
    {
      forwardAction = ActionConstants.SUCCESS_FORWARD;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "calculatorId", viewCalculatorForm.getCalculatorId() );
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
  public ActionForward removeCriterion( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD ); // EARLY EXIT
    }

    ViewCalculatorForm viewCalculatorForm = (ViewCalculatorForm)form;
    String[] deletedIds = viewCalculatorForm.getCriterionDeleteIds();
    List list = PresentationUtils.convertStringArrayToListOfLongs( deletedIds );

    try
    {
      getCalculatorService().deleteCalculatorCriterion( new Long( viewCalculatorForm.getCalculatorId() ), list );
    }
    catch( ServiceErrorException e )
    {
      log.error( e.getMessage(), e );
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
    }

    return view( mapping, form, request, response );
  }

  /**
   * Removes CalculatorPayout
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward removePayout( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD ); // EARLY EXIT
    }

    ViewCalculatorForm viewCalculatorForm = (ViewCalculatorForm)form;
    String[] deletedIds = viewCalculatorForm.getPayoutDeleteIds();
    List list = PresentationUtils.convertStringArrayToListOfLongs( deletedIds );

    try
    {
      getCalculatorService().deleteCalculatorPayout( new Long( viewCalculatorForm.getCalculatorId() ), list );
    }
    catch( ServiceErrorException e )
    {
      log.error( e.getMessage(), e );
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
    }

    return view( mapping, form, request, response );
  }

  /**
   * Copies Calculator - Almost like a 'save as'
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward copy( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "copy";
    logger.debug( ">>> " + METHOD_NAME );

    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_COPY ); // EARLY EXIT
    }

    String forward = ActionConstants.SUCCESS_FORWARD;

    CalculatorForm calculatorForm = (CalculatorForm)form;
    Calculator copiedCalculator = null;

    String newCalculatorName = calculatorForm.getCopyCalculatorName();
    Long calculatorId = new Long( calculatorForm.getCalculatorId() );

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );

      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "calculatorId", calculatorId );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( mapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, "method=display" } );
    }

    try
    {
      copiedCalculator = getCalculatorService().copyCalculator( calculatorId, newCalculatorName );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "quiz.errors.UNIQUE_CONSTRAINT" ) );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_FORWARD;
      return mapping.findForward( forward );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_FORWARD;
      return mapping.findForward( forward );
    }

    logger.info( "<<< " + METHOD_NAME );
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "calculatorId", copiedCalculator.getId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );

    return ActionUtils.forwardWithParameters( mapping, forward, new String[] { queryString, "method=display" } );
  }

  /**
   * prepare copy
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCopy( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "prepareCopy";
    logger.debug( ">>> " + METHOD_NAME );

    String forward = ActionConstants.COPY_FORWARD;

    ViewCalculatorForm viewCalculatorForm = (ViewCalculatorForm)form;

    Long calculatorId = new Long( viewCalculatorForm.getCalculatorId() );
    viewCalculatorForm.load( getCalculatorService().getCalculatorById( calculatorId ) );

    logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
  }

  /**
   * Display Calculator
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

    CalculatorForm calculatorForm = (CalculatorForm)form;
    calculatorForm.setMethod( "save" );
    if ( calculatorForm.getCalculatorId() != null && !calculatorForm.getCalculatorId().equals( "" ) )
    {
      Long calculatorId = new Long( calculatorForm.getCalculatorId() );
      calculatorForm.load( getCalculatorService().getCalculatorById( calculatorId ) );
    }
    /*
     * ViewCalculatorForm viewCalculatorForm = new ViewCalculatorForm();
     * setCalculateScoreRanges(viewCalculatorForm, request );
     */

    logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
  }

  /**
   * View Calculator with criterion and payout table
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward view( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "view";
    logger.debug( ">>> " + METHOD_NAME );

    String forward = ActionConstants.SUCCESS_FORWARD;

    ViewCalculatorForm viewCalculatorForm = (ViewCalculatorForm)form;
    viewCalculatorForm.setMethod( "save" );

    Calculator temp = (Calculator)request.getAttribute( "calculator" );

    Long calculatorId;
    if ( viewCalculatorForm.getCalculatorId() == null )
    {
      calculatorId = temp.getId();
    }
    else
    {
      calculatorId = new Long( viewCalculatorForm.getCalculatorId() );
    }
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new CalculatorAssociationRequest( CalculatorAssociationRequest.ALL ) );
    viewCalculatorForm.load( getCalculatorService().getCalculatorByIdWithAssociations( calculatorId, associationRequestCollection ) );
    setCalculateScoreRanges( viewCalculatorForm, request );

    logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
  }

  /**
   * Reorder Criterion
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
    ViewCalculatorForm viewCalculatorForm = (ViewCalculatorForm)form;
    String forward = ActionConstants.SUCCESS_UPDATE;
    ActionMessages errors = new ActionMessages();

    Long calculatorId = new Long( 0 );
    Long criterionId = new Long( 0 );
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
        calculatorId = (Long)clientStateMap.get( "calculatorId" );
      }
      catch( ClassCastException cce )
      {
        String calcId = (String)clientStateMap.get( "calculatorId" );
        calculatorId = new Long( calcId );
      }
      try
      {
        criterionId = (Long)clientStateMap.get( "calculatorCriterionId" );
      }
      catch( ClassCastException cce )
      {
        String critId = (String)clientStateMap.get( "calculatorCriterionId" );
        criterionId = new Long( critId );
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
      // calculatorId = new Long( viewCalculatorForm.getCalculatorId() );
      // criterionId = new Long( viewCalculatorForm.getCalculatorCriterionId() );
      newIndex = new Long( RequestUtils.getRequiredParamString( request, "newElementSequenceNum" ) );
      //
    }
    catch( IllegalArgumentException e )
    {
      newIndex = new Long( viewCalculatorForm.getNewCriterionSequenceNum() );
    }

    if ( errors.size() != 0 )
    {
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_FORWARD;
    }
    else
    {
      getCalculatorService().reorderCriterion( calculatorId, criterionId, newIndex.intValue() );
    }

    logger.info( "<<< " + METHOD_NAME );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "calculatorId", calculatorId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );

    return ActionUtils.forwardWithParameters( mapping, forward, new String[] { queryString, "method=view" } );
  }

  /**
   * Mark Claim Form Complete
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward markComplete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forward = ActionConstants.SUCCESS_UPDATE;

    ActionMessages errors = new ActionMessages();

    ViewCalculatorForm viewCalculatorForm = (ViewCalculatorForm)form;

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new CalculatorAssociationRequest( CalculatorAssociationRequest.ALL ) );
    Calculator calculator = getCalculatorService().getCalculatorByIdWithAssociations( new Long( viewCalculatorForm.getCalculatorId() ), associationRequestCollection );

    if ( !StringUtil.isEmpty( calculator.getWeightCMAssetName() ) )
    {
      calculator.setWeightLabel( CmsResourceBundle.getCmsBundle().getString( calculator.getWeightCMAssetName(), Calculator.CM_CALC_WEIGHT_NAME_KEY ) );
    }

    if ( !StringUtil.isEmpty( calculator.getScoreCMAssetName() ) )
    {
      calculator.setScoreLabel( CmsResourceBundle.getCmsBundle().getString( calculator.getScoreCMAssetName(), Calculator.CM_CALC_SCORE_NAME_KEY ) );
    }

    List criterionList = calculator.getCalculatorCriterion();
    Set payoutSet = calculator.getCalculatorPayouts();

    int totalWeight = 0;
    int calculatorMinScore = 0;
    int calculatorMaxScore = 0;

    if ( criterionList == null || criterionList.size() < 1 )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.errors.NO_CRITERION" ) );
    }
    else
    {
      for ( Iterator it = criterionList.iterator(); it.hasNext(); )
      {
        CalculatorCriterion criterion = (CalculatorCriterion)it.next();
        if ( criterion.getCriterionRatings() == null || criterion.getCriterionRatings().size() < 1 )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.errors.NO_RATINGS", criterion.getCriterionText() ) );
        }
        else
        {
          calculatorMinScore = calculatorMinScore + getCriterionMinScore( criterion );
          calculatorMaxScore = calculatorMaxScore + getCriterionMaxScore( criterion );
        }
        if ( calculator.isWeightedScore() )
        {
          totalWeight = totalWeight + criterion.getWeightValue();
        }
      }
    }

    if ( calculator.isWeightedScore() )
    {
      if ( totalWeight != 100 )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.errors.INVALID_TOTAL_WEHGHT" ) );
      }
    }

    if ( payoutSet == null || payoutSet.size() < 1 )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.errors.NO_PAYOUTS" ) );
    }
    else
    {
      String missingScores = null;
      for ( int i = calculatorMinScore; i <= calculatorMaxScore; i++ )
      {
        if ( getCalculatorService().getCalculatorPayoutByScore( calculator.getId(), i ) == null )
        {
          if ( missingScores == null )
          {
            missingScores = String.valueOf( i );
          }
          else
          {
            missingScores = missingScores + ", " + String.valueOf( i );
          }
        }
      }
      if ( missingScores != null )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.errors.INCOMPLETE_PAYOUT", missingScores ) );
      }
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return view( mapping, form, request, response );
    }

    calculator.setCalculatorStatusType( CalculatorStatusType.lookup( ClaimFormStatusType.COMPLETED ) );

    try
    {
      getCalculatorService().saveCalculator( calculator );
    }
    catch( ServiceErrorException e )
    {
      forward = ActionConstants.FAIL_FORWARD;
      logger.error( e.getMessage(), e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
    }

    return mapping.findForward( forward );
  }

  private int getCriterionMinScore( CalculatorCriterion criterion )
  {
    int lowestRatingValue = 0;
    for ( Iterator it = criterion.getCriterionRatings().iterator(); it.hasNext(); )
    {
      CalculatorCriterionRating rating = (CalculatorCriterionRating)it.next();
      if ( rating.getRatingValue() == 0 )
      {
        return 0;
      }

      if ( lowestRatingValue == 0 )
      {
        lowestRatingValue = rating.getRatingValue();
      }
      else
      {
        if ( rating.getRatingValue() < lowestRatingValue )
        {
          lowestRatingValue = rating.getRatingValue();
        }
      }
    }

    return calculateScore( criterion, lowestRatingValue );
  }

  private int getCriterionMaxScore( CalculatorCriterion criterion )
  {
    int highestRatingValue = 0;
    for ( Iterator it = criterion.getCriterionRatings().iterator(); it.hasNext(); )
    {
      CalculatorCriterionRating rating = (CalculatorCriterionRating)it.next();

      if ( highestRatingValue == 0 )
      {
        highestRatingValue = rating.getRatingValue();
      }
      else
      {
        if ( rating.getRatingValue() > highestRatingValue )
        {
          highestRatingValue = rating.getRatingValue();
        }
      }
    }

    return calculateScore( criterion, highestRatingValue );
  }

  private int calculateScore( CalculatorCriterion criterion, int ratingValue )
  {
    BigDecimal ratingScore;
    if ( criterion.getCalculator().isWeightedScore() )
    {
      double weight = criterion.getWeightValue();
      ratingScore = new BigDecimal( ratingValue * ( weight / 100 ) );
    }
    else
    {
      ratingScore = new BigDecimal( ratingValue );
    }
    return ratingScore.setScale( 0, BigDecimal.ROUND_HALF_UP ).intValue();
  }

  /**
   * Set Calculator score ranges min and max
   * 
   * @param ViewCalculatorForm
   * @param request
   * @return void
   */
  private void setCalculateScoreRanges( ViewCalculatorForm viewCalculatorForm, HttpServletRequest request )
  {
    // 12901
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new CalculatorAssociationRequest( CalculatorAssociationRequest.ALL ) );
    Calculator calculator = getCalculatorService().getCalculatorByIdWithAssociations( new Long( viewCalculatorForm.getCalculatorId() ), associationRequestCollection );

    List criterionList = calculator.getCalculatorCriterion();

    int calculatorMinScore = 0;
    int calculatorMaxScore = 0;

    if ( !CollectionUtils.isEmpty( criterionList ) )
    {
      for ( Iterator it = criterionList.iterator(); it.hasNext(); )
      {
        CalculatorCriterion criterion = (CalculatorCriterion)it.next();
        if ( !CollectionUtils.isEmpty( criterion.getCriterionRatings() ) )
        {
          calculatorMinScore = calculatorMinScore + getCriterionMinScore( criterion );
          calculatorMaxScore = calculatorMaxScore + getCriterionMaxScore( criterion );
        }
      }
    }
    request.setAttribute( "calculatorMinScore", String.valueOf( calculatorMinScore ) );
    request.setAttribute( "calculatorMaxScore", String.valueOf( calculatorMaxScore ) );

  }

  /**
   * Retrieves a CalculatorService
   * 
   * @return CalculatorService
   */
  private CalculatorService getCalculatorService()
  {
    return (CalculatorService)getService( CalculatorService.BEAN_NAME );
  }
} // end CalculatorLibraryAction
