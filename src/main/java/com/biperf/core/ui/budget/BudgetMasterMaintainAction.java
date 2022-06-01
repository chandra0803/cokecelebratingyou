/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/budget/BudgetMasterMaintainAction.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.budget;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.exception.NonUniqueDataServiceErrorException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetMasterToBudgetSegmentsAssociationRequest;
import com.biperf.core.service.budget.BudgetMasterToOverdrawApproverAssociation;
import com.biperf.core.service.budget.BudgetSegmentToBudgetsAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.participant.ListBuilderAction;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.value.BudgetSegmentValueBean;

/**
 * BudgetMasterMaintainAction.
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
 * <td>sharma</td>
 * <td>May 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetMasterMaintainAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( BudgetMasterMaintainAction.class );

  public static final String SESSION_BUDGET_MASTER_FORM = "sessionBudgetMasterForm";

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward actionForward = null;

    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( "tokenFailure", new ActionMessage( "system.errors.TOKEN_EXCEPTION" ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }

    try
    {
      BudgetMasterForm budgetMasterForm = (BudgetMasterForm)actionForm;

      Long budgetMasterId = budgetMasterForm.getId();
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
      BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( budgetMasterId, associationRequestCollection );
      budgetMasterForm.populateDomainObject( budgetMaster );

      // Inactive Part Of BugFix 17861
      Set allBudgetSegments = budgetMaster.getBudgetSegments();
      Set newBdgtSegSet = null;
      if ( allBudgetSegments != null && allBudgetSegments.size() > 0 )
      {
        newBdgtSegSet = new HashSet( allBudgetSegments.size() );
        for ( Iterator segIter = allBudgetSegments.iterator(); segIter.hasNext(); )
        {
          BudgetSegment budgetSegment = (BudgetSegment)segIter.next();

          if ( budgetMasterForm.isActive() )
          {
            budgetSegment.setStatus( Boolean.TRUE );
          }
          else
          {
            budgetSegment.setStatus( Boolean.FALSE );
          }

          Set allBudgets = budgetSegment.getBudgets();
          Set newBdgtSet = null;
          if ( allBudgets != null && allBudgets.size() > 0 )
          {
            newBdgtSet = new HashSet( allBudgets.size() );
            for ( Iterator iter = allBudgets.iterator(); iter.hasNext(); )
            {
              Budget budget = (Budget)iter.next();
              if ( budgetMasterForm.isActive() )
              {
                budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) );
              }
              else
              {
                budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.CLOSED ) );
              }
              newBdgtSet.add( budget );
            }
            budgetSegment.setBudgets( newBdgtSet );
          }
          newBdgtSegSet.add( budgetSegment );
        } // end of budgetSeglemnts
        budgetMaster.setBudgetSegments( newBdgtSegSet );
      }

      getBudgetMasterService().saveBudgetMaster( budgetMaster );
      // Bug Fix 20031Add Budget Type as Encoded params.
      budgetMasterForm.setBudgetMasterId( budgetMaster.getId().toString() );
      if ( request.getAttribute( "budgetMasterForm" ) != null )
      {
        request.removeAttribute( "budgetMasterForm" );
        request.setAttribute( "budgetMasterForm", budgetMasterForm );
      }
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "id", budgetMaster.getId() );
      clientStateParameterMap.put( "budgetType", budgetMasterForm.getBudgetType() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      actionForward = ActionUtils.forwardWithParameters( actionMapping, ActionConstants.SUCCESS_CREATE, new String[] { queryString, "method=display" } );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_UPDATE );
    }
    return actionForward;
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward actionForward = null;
    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( "tokenFailure", new ActionMessage( "system.errors.TOKEN_EXCEPTION" ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_CREATE ); // EARLY EXIT
    }

    try
    {
      BudgetMasterForm masterBudgetForm = (BudgetMasterForm)actionForm;
      BudgetMaster budgetMaster = masterBudgetForm.toDomainObject();

      budgetMaster = getBudgetMasterService().saveBudgetMaster( budgetMaster );
      masterBudgetForm.setBudgetMasterId( budgetMaster.getId().toString() );
      if ( request.getAttribute( "budgetMasterForm" ) != null )
      {
        request.removeAttribute( "budgetMasterForm" );
        request.setAttribute( "budgetMasterForm", masterBudgetForm );
      }
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "id", budgetMaster.getId().toString() );
      clientStateParameterMap.put( "budgetType", masterBudgetForm.getBudgetType() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      actionForward = ActionUtils.forwardWithParameters( actionMapping, ActionConstants.SUCCESS_CREATE, new String[] { queryString, "method=display" } );
    }
    catch( NonUniqueDataServiceErrorException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "admin.budgetmaster.errors.DUPLICATE" ) );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }
    catch( Exception e )
    {
      // TODO: If the same BudgetMaster name already exists in CM, the exception is to be handled
      // and a
      // message relayed back to the client. Spring is intercepting the non unique exception and
      // then encountering and throwing a GenericJDBCException or IllegalStateException (see bug
      // 9812).
      // For the time being, a generic Exception is being implemented. However, the Spring
      // interceptor
      // needs to be looked at.
      log.debug( e );
      e.printStackTrace();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "admin.budgetmaster.errors.GENERIC_ERROR" ) );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_CREATE );
    }

    return actionForward;
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdate( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    BudgetMasterForm budgetMasterForm = (BudgetMasterForm)actionForm;

    Long budgetMasterId = budgetMasterForm.getId();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
    associationRequestCollection.add( new BudgetMasterToOverdrawApproverAssociation() );
    BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( budgetMasterId, associationRequestCollection );
    budgetMasterForm.loadDomainObject( budgetMaster );

    request.setAttribute( "budgetMaster", budgetMaster );

    int budgetSegmentVBListSize = budgetMasterForm.getBudgetSegmentVBListSize();
    if ( budgetSegmentVBListSize > 0 )
    {
      request.setAttribute( "lastSegmentIndex", budgetSegmentVBListSize - 1 );
    }
    else
    {
      request.setAttribute( "lastSegmentIndex", 0 );
    }

    return actionMapping.findForward( ActionConstants.UPDATE_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    BudgetMasterForm budgetMasterForm = (BudgetMasterForm)form;

    BudgetMaster budgetMaster = new BudgetMaster();
    budgetMasterForm.loadDomainObject( budgetMaster );
    request.setAttribute( "budgetMaster", budgetMaster );

    int budgetSegmentVBListSize = budgetMasterForm.getBudgetSegmentVBListSize();
    if ( budgetSegmentVBListSize > 0 )
    {
      request.setAttribute( "lastSegmentIndex", budgetSegmentVBListSize - 1 );
    }
    else
    {
      request.setAttribute( "lastSegmentIndex", 0 );
    }

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward delete( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( "tokenFailure", new ActionMessage( "system.errors.TOKEN_EXCEPTION" ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }

    try
    {
      BudgetMasterForm masterBudgetForm = (BudgetMasterForm)actionForm;
      Long[] deleteIds = masterBudgetForm.getDeleteBudgetMasterIds();
      if ( deleteIds != null )
      {
        for ( int i = 0; i < deleteIds.length; i++ )
        {
          Long budgetMasterId = deleteIds[i];
          getBudgetMasterService().deleteBudgetMaster( budgetMasterId );
        }
      }
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    ActionForward actionForward;
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_DELETE );
    }
    else
    {
      actionForward = actionMapping.findForward( ActionConstants.SUCCESS_DELETE );
    }
    return actionForward;
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deleteBudgets( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( "tokenFailure", new ActionMessage( "system.errors.TOKEN_EXCEPTION" ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_DELETE ); // EARLY EXIT
    }

    try
    {
      BudgetMasterForm masterBudgetForm = (BudgetMasterForm)actionForm;
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new BudgetSegmentToBudgetsAssociationRequest() );
      BudgetSegment budgetSegment = getBudgetMasterService().getBudgetSegmentById( masterBudgetForm.getBudgetSegmentId(), associationRequestCollection );

      Long[] deleteIds = masterBudgetForm.getDeleteBudgetIds();
      if ( deleteIds != null && deleteIds.length > 0 )
      {
        Set ids = ArrayUtil.convertArrayToSet( deleteIds );
        for ( Iterator iter = budgetSegment.getBudgets().iterator(); iter.hasNext(); )
        {
          if ( ids.contains( ( (Budget)iter.next() ).getId() ) )
          {
            iter.remove();
          }
        }
        getBudgetMasterService().saveBudgetSegment( budgetSegment );
      }
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    ActionForward actionForward;
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_DELETE );
    }
    else
    {
      actionForward = actionMapping.findForward( ActionConstants.SUCCESS_DELETE );
    }
    return actionForward;
  }

  public ActionForward setBudgetSegment( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward actionForward = actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
    return actionForward;
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward showBudgets( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward actionForward = actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
    return actionForward;
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward selectApprover( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    BudgetMasterForm budgetMasterForm = (BudgetMasterForm)actionForm;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_BUDGET_MASTER_FORM, budgetMasterForm );

    String queryString = ListBuilderAction.AUDIENCE_MEMBERS_LOOKUP_RETURN_URL_PARAM + "=" + "/admin/budgetMasterMaintainDisplay.do?method=updateApprover";

    return ActionUtils.forwardWithParameters( actionMapping, "select_approver", new String[] { queryString } );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward updateApprover( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    BudgetMasterForm budgetMasterForm = (BudgetMasterForm)actionForm;

    // Get the form back out of the Session to redisplay.
    BudgetMasterForm sessionBudgetMasterForm = (BudgetMasterForm)request.getSession().getAttribute( SESSION_BUDGET_MASTER_FORM );

    if ( sessionBudgetMasterForm != null )
    {
      /*
       * List participants = (List)request.getSession() .getAttribute(
       * ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_LIST ); if(participants != null) { Iterator
       * participantIter = participants.iterator(); if ( participantIter.hasNext() ) {
       * FormattedValueBean participantBean = (FormattedValueBean)participantIter.next();
       * sessionBudgetMasterForm.setUserId( participantBean.getId() );
       * sessionBudgetMasterForm.setFirstName( participantBean.getFirstName() );
       * sessionBudgetMasterForm.setLastName( participantBean.getLastName() ); } }
       */
      /**Bug Fix#16852 the try catch is displayed outside the if loop inorder to retain the values when clicking on 
         the cancel button in the participant select screen.*/
      try
      {
        BeanUtils.copyProperties( budgetMasterForm, sessionBudgetMasterForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }

    }
    // BugFix 19190 Just Update the BM Id as null when the value is less thanor equal to zero
    if ( budgetMasterForm.getId() != null && budgetMasterForm.getId().longValue() <= 0 )
    {
      budgetMasterForm.setId( null );
      if ( request.getAttribute( "budgetMasterForm" ) != null )
      {
        request.removeAttribute( "budgetMasterForm" );
        request.setAttribute( "budgetMasterForm", budgetMasterForm );
      }
    }
    request.getSession().removeAttribute( SESSION_BUDGET_MASTER_FORM );
    request.getSession().removeAttribute( ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_LIST );

    return actionMapping.findForward( "update_approver" );
  }

  public ActionForward addAnotherSegment( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( "success_add_another" );
    ActionMessages errors = new ActionMessages();
    BudgetMasterForm budgetMasterForm = (BudgetMasterForm)actionForm;

    budgetMasterForm.addEmptyBudgetSegment();
    request.setAttribute( "lastSegmentIndex", budgetMasterForm.getBudgetSegmentVBListSize() - 1 );

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( "failure_add_another" );
    }

    return forward;
  }

  public ActionForward removeBudgetSegment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    ActionForward forward = mapping.findForward( "remove_segment" );

    BudgetMasterForm budgetMasterForm = (BudgetMasterForm)form;
    int lastIndx = budgetMasterForm.getBudgetSegmentVBListSize() - 1;
    BudgetSegmentValueBean budgetSegmentValueBean = budgetMasterForm.getBudgetSegmentVBList().get( lastIndx );
    boolean canDelete = true;
    if ( budgetSegmentValueBean != null )
    {
      Long budgetSegmentId = budgetSegmentValueBean.getId();

      if ( budgetSegmentId != null & budgetSegmentId != 0L )
      {
        deleteSelectedBudgetSegment( mapping, form, request, response, budgetSegmentId );
      }
      else
      {
        budgetMasterForm.getBudgetSegmentVBList().remove( lastIndx );
      }
    }

    return forward;
  }

  public ActionForward deleteSelectedBudgetSegment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Long segmentId )
  {

    ActionMessages errors = new ActionMessages();
    ActionForward forward = mapping.findForward( "remove_segment" );
    try
    {
      BudgetMasterForm budgetMasterForm = (BudgetMasterForm)form;
      Long budgetMasterId = budgetMasterForm.getId();
      Long deleteSegmentId = segmentId;
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
      BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( budgetMasterId, associationRequestCollection );
      if ( budgetMaster != null )
      {
        budgetMasterForm.populateDomainObject( budgetMaster );
      }
      else
      {
        return forward;
      }
      // Inactive Part Of BugFix 17861
      Set allBudgetSegments = budgetMaster.getBudgetSegments();
      Set newBdgtSegSet = null;
      int lastIndx = budgetMasterForm.getBudgetSegmentVBListSize() - 1;
      boolean canDelete = true;
      if ( allBudgetSegments != null && allBudgetSegments.size() > 0 )
      {
        for ( Iterator deleteSegmentIter = allBudgetSegments.iterator(); deleteSegmentIter.hasNext(); )
        {
          BudgetSegment budgetSegment = (BudgetSegment)deleteSegmentIter.next();
          if ( deleteSegmentId != null && deleteSegmentId > 0 )
          {
            Long segmentToBeDeleted = budgetSegment.getId();
            if ( segmentToBeDeleted.equals( deleteSegmentId ) )
            {
              deleteSegmentIter.remove();
              // allBudgetSegments.remove( deleteSegmentIter );
              canDelete = getBudgetMasterService().canDeleteBudgetSegment( deleteSegmentId );
              if ( !canDelete )
              {
                errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "admin.budgetmaster.errors.SEGMENT_CANNOT_DELETE" ) );
                saveErrors( request, errors );
                forward = mapping.findForward( "failure_remove_segment" );
                return forward;
              }
              else
              {
                budgetMasterForm.getBudgetSegmentVBList().remove( lastIndx );
              }
            }

          }
          else if ( deleteSegmentId == 0 )
          {
            budgetMasterForm.getBudgetSegmentVBList().remove( lastIndx );
            break;
          }
        }

        newBdgtSegSet = new HashSet( allBudgetSegments.size() );

        for ( Iterator segIter = allBudgetSegments.iterator(); segIter.hasNext(); )
        {
          BudgetSegment budgetSegment = (BudgetSegment)segIter.next();

          if ( budgetMasterForm.isActive() )
          {
            budgetSegment.setStatus( Boolean.TRUE );
          }
          else
          {
            budgetSegment.setStatus( Boolean.FALSE );
          }

          Set allBudgets = budgetSegment.getBudgets();
          Set newBdgtSet = null;
          if ( allBudgets != null && allBudgets.size() > 0 )
          {
            newBdgtSet = new HashSet( allBudgets.size() );
            for ( Iterator iter = allBudgets.iterator(); iter.hasNext(); )
            {
              Budget budget = (Budget)iter.next();
              if ( budgetMasterForm.isActive() )
              {
                budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) );
              }
              else
              {
                budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.CLOSED ) );
              }
              newBdgtSet.add( budget );
            }
            budgetSegment.setBudgets( newBdgtSet );
          }
          newBdgtSegSet.add( budgetSegment );
        } // end of budgetSeglemnts
        budgetMaster.setBudgetSegments( newBdgtSegSet );
      }
      if ( deleteSegmentId != 0 )
      {
        getBudgetMasterService().saveBudgetMaster( budgetMaster );
      }
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }
    return forward;
  }

  /**
   * Gets a Budget Service
   * 
   * @return BudgetService
   */
  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  } // end

  /**
   * Gets a User Service
   * 
   * @return UserService
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  } // end
}
