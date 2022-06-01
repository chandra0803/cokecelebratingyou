/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/budget/BudgetAction.java,v $
 */

package com.biperf.core.ui.budget;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.participant.ListBuilderAction;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.FormattedValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * BudgetAction.
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
 * <td>May 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( BudgetAction.class );
  private static final String SESSION_BUDGET_FORM = "sessionBudgetForm";

  /**
   * Prepares anything necessary before displaying the create screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    BudgetForm budgetForm = (BudgetForm)form;

    // default Status to active
    budgetForm.setBudgetStatusType( BudgetStatusType.lookup( "active" ).getCode() );
    budgetForm.setBudgetStatusTypeDesc( BudgetStatusType.lookup( "active" ).getName() );

    // get the actionForward to display the create pages.
    budgetForm.setMethod( "create" );
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  } // end prepareCreate

  /**
   * Creates a new budget
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    BudgetForm budgetForm = (BudgetForm)actionForm;

    // -----------------------------
    // Add the appropriate Budget
    // -----------------------------
    try
    {
      if ( budgetForm.getPaxOrNode().equals( "NODE" ) )
      {
        getBudgetMasterService().addNodeBudget( budgetForm.getBudgetSegmentId(), convertStringToLong( budgetForm.getNodeId() ), budgetForm.toInsertedDomainObject() );
      }
      else if ( budgetForm.getPaxOrNode().equals( "PAX" ) )
      {
        getBudgetMasterService().addUserBudget( budgetForm.getBudgetSegmentId(), convertStringToLong( budgetForm.getUserId() ), budgetForm.toInsertedDomainObject() );
      }
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_CREATE );
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "id", budgetForm.getBudgetMasterId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.SUCCESS_CREATE, new String[] { queryString } );
  } // end create

  /**
   * Prepares anything necessary before showing the Update Budget for Pax.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdateByPax( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    BudgetForm budgetForm = (BudgetForm)form;
    budgetForm.setPaxOrNode( "PAX" );

    return prepareUpdate( mapping, form, request, response );
  } // end prepareUpdateByPax

  /**
   * Prepares anything necessary before showing the Update Budget for Pax.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdateByNode( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    BudgetForm budgetForm = (BudgetForm)form;
    budgetForm.setPaxOrNode( "NODE" );

    return prepareUpdate( mapping, form, request, response );
  } // end prepareUpdateByNode

  /**
   * Prepares anything necessary before showing the Update Budget
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdate( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    BudgetForm budgetForm = (BudgetForm)actionForm;

    Long budgetSegmentId = budgetForm.getBudgetSegmentId();
    Long budgetId = new Long( budgetForm.getBudgetId() );

    Budget budget = getBudgetMasterService().getBudget( budgetSegmentId, budgetId );

    budgetForm.load( budget );
    // Added for the bug fix 20155
    if ( budgetForm.getPaxOrNode() != null && budgetForm.getPaxOrNode().equalsIgnoreCase( "PAX" ) )
    {
      budgetForm.setBudgetType( BudgetType.PAX_BUDGET_TYPE );
    }
    else if ( budgetForm.getPaxOrNode() != null && budgetForm.getPaxOrNode().equalsIgnoreCase( "NODE" ) )
    {
      budgetForm.setBudgetType( BudgetType.NODE_BUDGET_TYPE );
    }

    return actionMapping.findForward( ActionConstants.EDIT_FORWARD );
  } // end prepareUpdate

  /**
   * Prepares anything necessary before showing the Review Budget
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareReview( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    BudgetForm budgetForm = (BudgetForm)actionForm;
    String forwardTo = "";

    budgetForm.setCalculatedOriginalValue( "0" );
    budgetForm.setCalculatedAvailableValue( "0" );
    User user = null;
    Participant particiopant = null;
    if ( !budgetForm.getUserId().isEmpty() )
    {
      user = getUserService().getUserById( convertStringToLong( budgetForm.getUserId() ) );
      particiopant = getParticipantService().getParticipantById( user.getId() );
    }
    if ( null != user && !user.isActive() && particiopant.getTermsAcceptance() != null
        && ParticipantTermsAcceptance.lookup( ParticipantTermsAcceptance.DECLINED ).equals( particiopant.getTermsAcceptance().getCode() ) )
    {
      BudgetStatusType budgetStatus = BudgetStatusType.lookup( budgetForm.getBudgetStatusType() );
      if ( budgetStatus.getCode().equals( BudgetStatusType.ACTIVE ) )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budget.errors.USER_IS_INACTIVE" ) );
      }
      else
      {
        budgetForm.setBudgetStatusTypeDesc( BudgetStatusType.lookup( budgetForm.getBudgetStatusType() ).getName() );
      }
    }
    else
    {
      budgetForm.setBudgetStatusTypeDesc( BudgetStatusType.lookup( budgetForm.getBudgetStatusType() ).getName() );
    }
    try
    {
      BigDecimal qtyToAdd = new BigDecimal( 0.0 );
      BigDecimal origValue = NumberUtils.createBigDecimal( budgetForm.getOriginalValue() );
      if ( budgetForm.getQtyToAdd() != null && !budgetForm.getQtyToAdd().isEmpty() )
      {
        qtyToAdd = NumberUtils.createBigDecimal( budgetForm.getQtyToAdd() );
      }
      BigDecimal availValue = NumberUtils.createBigDecimal( budgetForm.getAvailableValue() );
      BigDecimal amountToTransfer = new BigDecimal( 0.0 );
      if ( budgetForm.getAmountToTransfer() != null && !budgetForm.getAmountToTransfer().isEmpty() )
      {
        amountToTransfer = NumberUtils.createBigDecimal( budgetForm.getAmountToTransfer() );
      }
      BigDecimal calcOrigValue = origValue;
      BigDecimal calcAvailValue = availValue;
      if ( BudgetForm.ADD_TO_BUDGET.equalsIgnoreCase( budgetForm.getUpdateMethod() ) )
      {
        if ( null != user && !user.isActive() && particiopant.getTermsAcceptance() != null
            && ParticipantTermsAcceptance.lookup( ParticipantTermsAcceptance.DECLINED ).equals( particiopant.getTermsAcceptance().getCode() ) )
        {
          if ( errors.size() <= 0 )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budget.errors.USER_IS_INACTIVE" ) );
          }
        }
        else
        {
          calcOrigValue = calcOrigValue.add( qtyToAdd );
          calcAvailValue = calcAvailValue.add( qtyToAdd );
        }
      }
      else if ( BudgetForm.TRANSFER_TO_BUDGET.equalsIgnoreCase( budgetForm.getUpdateMethod() ) )
      {
        calcOrigValue = calcOrigValue.subtract( amountToTransfer );
        calcAvailValue = calcAvailValue.subtract( amountToTransfer );
      }

      budgetForm.setCalculatedOriginalValue( String.valueOf( calcOrigValue ) );
      budgetForm.setCalculatedAvailableValue( String.valueOf( calcAvailValue ) );
    }
    catch( Exception e )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE,
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) ) );

    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_UPDATE;
    }
    else
    {
      forwardTo = ActionConstants.REVIEW_FORWARD;
    }

    return actionMapping.findForward( forwardTo );
  } // end prepareReview

  /**
   * Used for back button to redisplay the form
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareRedisplay( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return actionMapping.findForward( ActionConstants.EDIT_FORWARD );
  } // end prepareRedisplay

  /**
   * Updates a budget
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardAction = ActionConstants.SUCCESS_UPDATE;

    ActionMessages errors = new ActionMessages();
    BudgetForm budgetForm = (BudgetForm)actionForm;

    Long budgetSegmentId = budgetForm.getBudgetSegmentId();
    Long transferId = budgetForm.getSelectedTransferId();
    Long nodeId = convertStringToLong( budgetForm.getNodeId() );
    Long userId = convertStringToLong( budgetForm.getUserId() );

    BudgetStatusType budgetStatus = BudgetStatusType.lookup( budgetForm.getBudgetStatusType() );
    Double quantityToAdd = 0.0;
    Double amountToTransfer = 0.0;
    if ( budgetForm.getQtyToAdd() != null && !budgetForm.getQtyToAdd().isEmpty() )
    {
      quantityToAdd = Double.valueOf( budgetForm.getQtyToAdd() );
    }
    if ( budgetForm.getAmountToTransfer() != null && !budgetForm.getAmountToTransfer().isEmpty() )
    {
      amountToTransfer = Double.valueOf( budgetForm.getAmountToTransfer() );
    }

    try
    {
      if ( budgetForm.getPaxOrNode().equals( "NODE" ) )
      {
        if ( BudgetForm.ADD_TO_BUDGET.equals( budgetForm.getUpdateMethod() ) )
        {
          getBudgetMasterService().updateNodeBudgetAndAddQty( budgetSegmentId, nodeId, budgetStatus, BigDecimal.valueOf( quantityToAdd ) );
        }
        if ( BudgetForm.TRANSFER_TO_BUDGET.equals( budgetForm.getUpdateMethod() ) )
        {
          getBudgetMasterService().updateNodeBudgetAndTransferQuantity( budgetSegmentId, nodeId, budgetStatus, transferId, BigDecimal.valueOf( amountToTransfer ) );
        }
        if ( BudgetForm.UPDATE_METHOD_NO_CHANGE.equals( budgetForm.getUpdateMethod() ) )
        {
          getBudgetMasterService().updateNodeBudgetAndAddQty( budgetSegmentId, nodeId, budgetStatus, BigDecimal.ZERO );
        }
      }
      else if ( budgetForm.getPaxOrNode().equals( "PAX" ) )
      {
        if ( BudgetForm.ADD_TO_BUDGET.equals( budgetForm.getUpdateMethod() ) )
        {
          getBudgetMasterService().updateUserBudgetAndAddQty( budgetSegmentId, userId, budgetStatus, BigDecimal.valueOf( quantityToAdd ) );
        }
        if ( BudgetForm.TRANSFER_TO_BUDGET.equals( budgetForm.getUpdateMethod() ) )
        {
          getBudgetMasterService().updateUserBudgetAndTransferQuantity( budgetSegmentId, userId, budgetStatus, transferId, BigDecimal.valueOf( amountToTransfer ) );
        }
        if ( BudgetForm.UPDATE_METHOD_NO_CHANGE.equals( budgetForm.getUpdateMethod() ) )
        {
          getBudgetMasterService().updateUserBudgetAndAddQty( budgetSegmentId, userId, budgetStatus, BigDecimal.ZERO );
        }
      }
    }
    catch( ServiceErrorException e )
    {
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardAction = ActionConstants.FAIL_FORWARD;
    }
    else
    {
      forwardAction = ActionConstants.SUCCESS_UPDATE;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "id", budgetForm.getBudgetMasterId() );
    clientStateParameterMap.put( "budgetType", budgetForm.getBudgetType() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString } );
  } // end update

  /**
   * Prepares anything necessary before showing the Display Budget for Pax.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareDisplayByPax( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    BudgetForm budgetForm = (BudgetForm)form;
    budgetForm.setPaxOrNode( "PAX" );

    return prepareDisplay( mapping, form, request, response );
  } // end prepareDisplayByPax

  /**
   * Prepares anything necessary before showing the Display Budget for Pax.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareDisplayByNode( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    BudgetForm budgetForm = (BudgetForm)form;
    budgetForm.setPaxOrNode( "NODE" );

    return prepareDisplay( mapping, form, request, response );
  } // end prepareDisplayByNode

  /**
   * Prepares anything necessary before showing the Display Budget
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareDisplay( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    BudgetForm budgetForm = (BudgetForm)actionForm;

    Long budgetSegmentId = budgetForm.getBudgetSegmentId();
    Long budgetId = new Long( budgetForm.getBudgetId() );

    Budget budget = getBudgetMasterService().getBudget( budgetSegmentId, budgetId );

    budgetForm.load( budget );

    return actionMapping.findForward( ActionConstants.DETAILS_FORWARD );
  } // end prepareDisplay

  /**
   * used when returning from the node lookup screen andd going to the add screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward returnNodeLookupAdd( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    BudgetForm budgetForm = (BudgetForm)form;
    ActionMessages errors = new ActionMessages();

    budgetForm.setPaxOrNode( "NODE" );
    Long budgetMasterId = null;
    Long budgetSegmentId = null;
    String nodeId = null;
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
        budgetMasterId = (Long)clientStateMap.get( "budgetMasterId" );
      }
      catch( ClassCastException cce )
      {
        budgetMasterId = new Long( (String)clientStateMap.get( "budgetMasterId" ) );
      }
      if ( budgetMasterId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "budgetMasterId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_CREATE );
      }

      // Budget Segment
      try
      {
        budgetSegmentId = (Long)clientStateMap.get( "budgetSegmentId" );
      }
      catch( ClassCastException cce )
      {
        budgetSegmentId = new Long( (String)clientStateMap.get( "budgetSegmentId" ) );
      }
      if ( budgetSegmentId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "budgetSegmentId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_CREATE );
      }

      try
      {
        nodeId = (String)clientStateMap.get( "nodeId" );
      }
      catch( ClassCastException cce )
      {
        nodeId = ( (Long)clientStateMap.get( "nodeId" ) ).toString();
      }
      budgetForm.setBudgetName( (String)clientStateMap.get( "budgetMasterName" ) );
      budgetForm.setOwnerName( (String)clientStateMap.get( "nodeName" ) );
      budgetForm.setBudgetSegmentName( (String)clientStateMap.get( "budgetSegmentName" ) );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    budgetForm.setBudgetMasterId( budgetMasterId.toString() );
    budgetForm.setBudgetSegmentId( budgetSegmentId );
    budgetForm.setNodeId( nodeId == null ? "" : nodeId );

    List<BudgetSegment> budgetSegmentList = new ArrayList<BudgetSegment>();
    if ( budgetMasterId != null )
    {
      budgetSegmentList = getBudgetSegmentList( new Long( budgetMasterId ) );
      budgetForm.setBudgetSegmentList( budgetSegmentList );
    }
    request.setAttribute( "budgetSegmentList", budgetSegmentList );

    // If no node was returned then go back to the master display page
    if ( budgetForm.getNodeId() == null || budgetForm.getNodeId().equals( "" ) )
    {
      return new ActionForward( "/budgetMasterDisplay.do" );
    }

    if ( budgetForm.getBudgetMasterId() == null || budgetForm.getBudgetMasterId().equals( "" ) )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE,
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) ) );

    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_SEARCH );
    }
    request.setAttribute( "budgetMasterId", budgetMasterId );
    return prepareCreate( mapping, form, request, response );

  } // end returnNodeLookupAdd

  /**
   * used when returning from the node lookup screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward returnNodeLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    BudgetForm budgetForm = (BudgetForm)form;
    // Get the form back out of the Session to redisplay.
    BudgetForm sessionBudgetForm = (BudgetForm)request.getSession().getAttribute( SESSION_BUDGET_FORM );
    // clean up the session
    request.getSession().removeAttribute( SESSION_BUDGET_FORM );

    if ( sessionBudgetForm != null )
    {
      try
      {
        BeanUtils.copyProperties( budgetForm, sessionBudgetForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }
    else
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE,
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) ) );

    }

    // if the previous lookup page cancelled, we don't want to replace
    // nodeId with blank (because the user might already have previous data)
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

      Object nodeId = clientStateMap.get( "nodeId" );
      if ( nodeId != null )
      {
        budgetForm.setNodeId( nodeId.toString() );
      }
      // if the previous lookup page cancelled, we don't want to replace
      // ownerName with blank (because the user might already have previous data)
      String ownerName = RequestUtils.getOptionalParamString( request, "nodeName" );
      if ( ownerName != null && ownerName.length() > 0 )
      {
        budgetForm.setOwnerName( ownerName );
      }
      else
      {
        Object obj = clientStateMap.get( "nodeName" );
        if ( obj != null )
        {
          budgetForm.setOwnerName( (String)obj );
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_SEARCH );
    }
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  } // end returnNodeLookup

  /**
   * used when returning from the pax lookup screen and going to the add screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward returnPaxLookupAdd( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    BudgetForm budgetForm = (BudgetForm)form;

    budgetForm.setPaxOrNode( "PAX" );
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
      budgetForm.setBudgetName( (String)clientStateMap.get( "budgetMasterName" ) );
      String budgetMasterId = null;
      try
      {
        budgetMasterId = (String)clientStateMap.get( "budgetMasterId" );
      }
      catch( ClassCastException cce )
      {
        budgetMasterId = ( (Long)clientStateMap.get( "budgetMasterId" ) ).toString();
      }
      if ( budgetMasterId != null )
      {
        budgetForm.setBudgetMasterId( budgetMasterId );
      }
      else
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "budgetMasterId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_SEARCH );
      }

      // Budget Segment
      Long budgetSegmentId = null;
      try
      {
        budgetSegmentId = (Long)clientStateMap.get( "budgetSegmentId" );
      }
      catch( ClassCastException cce )
      {
        budgetSegmentId = new Long( (String)clientStateMap.get( "budgetSegmentId" ) );
      }
      if ( budgetSegmentId != null )
      {
        budgetForm.setBudgetSegmentId( budgetSegmentId );
      }
      else
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "budgetSegmentId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_CREATE );
      }

      budgetForm.setBudgetSegmentName( (String)clientStateMap.get( "budgetSegmentName" ) );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    List participants = (List)request.getSession().getAttribute( ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_LIST );

    // if there are no participants in the list, return to the master screen.
    if ( participants == null || participants.size() < 1 )
    {
      return new ActionForward( "/budgetMasterDisplay.do" );
    }

    Iterator participantIter = participants.iterator();
    if ( participantIter.hasNext() )
    {
      FormattedValueBean participantBean = (FormattedValueBean)participantIter.next();
      budgetForm.setUserId( participantBean.getId().toString() );
      budgetForm.setOwnerName( participantBean.getFirstName() + " " + participantBean.getLastName() );
    }

    if ( budgetForm.getBudgetMasterId() == null || budgetForm.getBudgetMasterId().equals( "" ) )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE,
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) ) );

    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_SEARCH );
    }
    request.getSession().removeAttribute( ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_LIST );
    return prepareCreate( mapping, form, request, response );
  } // end returnPaxLookupAdd

  /**
   * Prepare the call to the Pax Search. Storing the form in the session
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward preparePaxLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    BudgetForm budgetForm = (BudgetForm)form;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_BUDGET_FORM, budgetForm );

    response.sendRedirect( RequestUtils.getBaseURI( request ) + "/participant/listBuilderPaxDisplay.do?" + "&" + ListBuilderAction.AUDIENCE_MEMBERS_LOOKUP_RETURN_URL_PARAM
        + "=/admin/budgetDisplay.do?method=returnPaxLookup" + "&singleResult=true" );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  } // end preparePaxLookup

  /**
   * used when returning from the pax lookup screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward returnPaxLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    BudgetForm budgetForm = (BudgetForm)form;

    // Get the form back out of the Session to redisplay.
    BudgetForm sessionBudgetForm = (BudgetForm)request.getSession().getAttribute( SESSION_BUDGET_FORM );
    // clean up the session
    request.getSession().removeAttribute( SESSION_BUDGET_FORM );

    if ( sessionBudgetForm != null )
    {
      List participants = (List)request.getSession().getAttribute( ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_LIST );

      if ( participants != null )
      {
        Iterator participantIter = participants.iterator();
        if ( participantIter.hasNext() )
        {
          FormattedValueBean participantBean = (FormattedValueBean)participantIter.next();
          sessionBudgetForm.setUserId( participantBean.getId().toString() );
          sessionBudgetForm.setOwnerName( participantBean.getFirstName() + " " + participantBean.getLastName() );
        }
      }
      try
      {
        BeanUtils.copyProperties( budgetForm, sessionBudgetForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }
    else
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE,
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) ) );

    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_SEARCH );
    }
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  } // end returnPaxLookup

  /**
   * Converts a String into a Long equivalent
   * 
   * @param s
   * @return Long value of the input string, 0 if any errors
   */
  private Long convertStringToLong( String s )
  {
    Long l = new Long( 0 );
    try
    {
      l = new Long( s );
    }
    catch( NumberFormatException e )
    {
      l = new Long( 0 );
    }
    return l;
  } // end convertStringToLong

  private List<BudgetSegment> getBudgetSegmentList( Long budgetMasterId )
  {
    return getBudgetMasterService().getBudgetSegmentsByBudgetMasterId( budgetMasterId );
  }

  /**
   * Retrieves a Budget Master Service
   * 
   * @return BudgetMasterService
   */
  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  } // end getBudgetMasterService

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }
} // end class BudgetAction
