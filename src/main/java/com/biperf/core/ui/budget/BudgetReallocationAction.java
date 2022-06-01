
package com.biperf.core.ui.budget;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantInfoView;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.BudgetTransferMailingProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetMasterToBudgetSegmentsAssociationRequest;
import com.biperf.core.service.budget.BudgetToNodeOwnersAddressAssociationRequest;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BudgetReallocationValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

public class BudgetReallocationAction extends BaseDispatchAction
{

  private static final String ADDED_NODE_OWNERS_SESSION_VARIABLE = "addedNodeOwners";

  private static final String ADDED_PAX_SESSION_VARIABLE = "addedParticipants";

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    BudgetReallocationForm budgetReallocationForm = (BudgetReallocationForm)form;

    if ( budgetReallocationForm.getBudgetMasterId() != null && budgetReallocationForm.getBudgetMasterId() > 0 )
    {
      AssociationRequestCollection budgetMasterAssociationRequestCollection = new AssociationRequestCollection();
      budgetMasterAssociationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
      BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( budgetReallocationForm.getBudgetMasterId(), budgetMasterAssociationRequestCollection );
      request.setAttribute( "displayAdditionalManagerAdd", budgetMaster.isAllowAdditionalTransferrees() );

      Long budgetSegmentId = budgetReallocationForm.getBudgetSegmentId();
      if ( budgetMaster.getBudgetSegments().size() == 1 )
      {
        budgetSegmentId = budgetMaster.getBudgetSegments().iterator().next().getId();
      }

      // Clear node id if not valid
      if ( budgetMaster.isParticipantBudget() )
      {
        budgetReallocationForm.setOwnerBudgetNodeId( null );
      }
      else if ( budgetMaster.isNodeBudget() )
      {
        budgetReallocationForm.setNodeBudget( true );
        List<Budget> eligibleNodeBudgets = new ArrayList<Budget>();
        if ( budgetMaster.getBudgetSegments().size() == 1 || budgetReallocationForm.getBudgetSegmentId() != null && budgetReallocationForm.getBudgetSegmentId() > 0 )
        {
          eligibleNodeBudgets = (List<Budget>)getBudgetMasterService().getAllActiveInBudgetSegmentForOwnerUserNode( budgetSegmentId, UserManager.getUserId() );
        }
        Long selectedNodeId = StringUtils.isBlank( budgetReallocationForm.getOwnerBudgetNodeId() ) ? null : Long.valueOf( budgetReallocationForm.getOwnerBudgetNodeId() );
        boolean budgetValid = false;
        for ( Budget eligibleNodeBudget : eligibleNodeBudgets )
        {
          if ( eligibleNodeBudget.getNode().getId().equals( selectedNodeId ) )
          {
            budgetValid = true;
          }
        }

        if ( !budgetValid )
        {
          budgetReallocationForm.setOwnerBudgetNodeId( null );
        }
        if ( eligibleNodeBudgets.size() == 1 )
        {
          budgetReallocationForm.setOwnerBudgetNodeId( eligibleNodeBudgets.get( 0 ).getNode().getId().toString() );
        }
      }

      // Load budget transfer details
      if ( budgetMaster.isParticipantBudget() || budgetMaster.isNodeBudget() && StringUtils.isNotBlank( budgetReallocationForm.getOwnerBudgetNodeId() ) )
      {
        Long budgetNodeId = budgetReallocationForm.getOwnerBudgetNodeId() == null ? null : Long.valueOf( budgetReallocationForm.getOwnerBudgetNodeId() );
        // boolean budgetSelected = loadBudgetTransferDetails( budgetReallocationForm, Long.valueOf(
        // budgetReallocationForm.getBudgetMasterId() ), budgetSegmentId, budgetNodeId );
        // Added new parameter request in loadBudgetTransferDetails method for budget transfer
        // change
        boolean budgetSelected = loadBudgetTransferDetails( budgetReallocationForm, Long.valueOf( budgetReallocationForm.getBudgetMasterId() ), budgetSegmentId, budgetNodeId, request );
        budgetReallocationForm.setBudgetSelected( budgetSelected );

        if ( !budgetSelected && budgetReallocationForm.getBudgetSegmentId() != null && budgetReallocationForm.getBudgetSegmentId() > 0 )
        {
          errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.BUDGET_NONE_TO_TRANSFER_FOR_SELECTED_SEGMENT ) );
          request.setAttribute( Globals.ERROR_KEY, errors );
          request.setAttribute( "isInfMsg", Boolean.TRUE );
        }
      }

    }
    else
    {
      request.setAttribute( "displayAdditionalManagerAdd", Boolean.FALSE );
    }
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  @SuppressWarnings( "unchecked" )
  private boolean loadBudgetTransferDetails( BudgetReallocationForm budgetReallocationForm, Long budgetMasterId, Long segmentId, Long nodeId, HttpServletRequest request )
  {
    boolean budgetSelected = false;
    final BigDecimal US_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( Country.UNITED_STATES );

    AssociationRequestCollection budgetMasterAssociationRequestCollection = new AssociationRequestCollection();
    budgetMasterAssociationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
    BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterAndPromotionsByUser( budgetReallocationForm.getBudgetMasterId(), budgetMasterAssociationRequestCollection );

    if ( budgetMaster != null )
    {
      BudgetSegment budgetSegmentFromForm = budgetReallocationForm.getBudgetSegmentId() != null ? getBudgetMasterService().getBudgetSegmentById( budgetReallocationForm.getBudgetSegmentId() ) : null;
      if ( null == budgetSegmentFromForm || null != budgetSegmentFromForm && budgetMaster.getBudgetSegments().contains( budgetSegmentFromForm ) )
      {
        // Fetch owner budget details
        Budget ownerBudget = null;
        BigDecimal OWNER_MEDIA_VALUE = null;
        Long budgetSegmentId = budgetReallocationForm.getBudgetSegmentId();
        if ( budgetMaster.getBudgetSegments().size() == 1 )
        {
          budgetSegmentId = budgetMaster.getBudgetSegments().iterator().next().getId();
        }
        if ( budgetMaster.isNodeBudget() )
        {
          if ( budgetMaster.getBudgetSegments().size() == 1 || budgetReallocationForm.getBudgetSegmentId() != null && budgetReallocationForm.getBudgetSegmentId() > 0 )
          {
            AssociationRequestCollection budgetAssociationRequestCollection = new AssociationRequestCollection();
            budgetAssociationRequestCollection.add( new BudgetToNodeOwnersAddressAssociationRequest() );
            ownerBudget = getBudgetMasterService().getAvailableNodeBudgetByBudgetSegmentAndNodeId( budgetSegmentId, nodeId, budgetAssociationRequestCollection );
            OWNER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser( UserManager.getUserId() );
          }
        }
        else if ( budgetMaster.isParticipantBudget() )
        {
          if ( budgetMaster.getBudgetSegments().size() == 1 || budgetReallocationForm.getBudgetSegmentId() != null && budgetReallocationForm.getBudgetSegmentId() > 0 )
          {
            ownerBudget = getBudgetMasterService().getAvailableUserBudgetByBudgetSegmentAndUserId( budgetSegmentId, UserManager.getUserId() );
            if ( ownerBudget != null )
            {
              OWNER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser( ownerBudget.getUser().getId() );
            }
          }
        }

        if ( ownerBudget != null && BudgetStatusType.ACTIVE.equals( ownerBudget.getStatus().getCode() ) )
        {
          // Internationalize owner budget
          BigDecimal convertedCurrentValue = BudgetUtils.applyMediaConversion( ownerBudget.getCurrentValue(), US_MEDIA_VALUE, OWNER_MEDIA_VALUE );
          BigDecimal convertedOriginalValue = BudgetUtils.applyMediaConversion( ownerBudget.getOriginalValue(), US_MEDIA_VALUE, OWNER_MEDIA_VALUE );
          ownerBudget.setCurrentValue( convertedCurrentValue );
          ownerBudget.setOriginalValue( convertedOriginalValue );

          // Fetch child budget details
          // bug fix 59650 - Changed to procedure call
          List<BudgetReallocationValueBean> childNodeOwnersList = new ArrayList<BudgetReallocationValueBean>();
          BigDecimal mediaRatio = BudgetUtils.calculateConversionRatio( US_MEDIA_VALUE, OWNER_MEDIA_VALUE );
          childNodeOwnersList = getBudgetMasterService().fetchChildBudgets( ownerBudget.getId(), budgetMasterId, budgetSegmentId, mediaRatio );
          // Load budget reallocation form details
          Set<BudgetReallocationValueBean> childNodeOwners = new HashSet<BudgetReallocationValueBean>( childNodeOwnersList );

          childNodeOwners.addAll( childNodeOwnersList );

          List<BudgetReallocationValueBean> childList = new ArrayList<BudgetReallocationValueBean>( childNodeOwners.size() );
          for ( BudgetReallocationValueBean budgetReallocationValueBean : childNodeOwners )
          {
            childList.add( budgetReallocationValueBean );
          }

          budgetReallocationForm.load( childList, budgetMaster, ownerBudget );
          if ( budgetMaster.isParticipantBudget() && budgetMaster.isAllowAdditionalTransferrees() )
          {
            List<ParticipantInfoView> addedParticipants = (List<ParticipantInfoView>)request.getSession().getAttribute( ADDED_PAX_SESSION_VARIABLE );
            if ( addedParticipants != null )
            {
              Set<ParticipantInfoView> participants = new HashSet<ParticipantInfoView>( addedParticipants );

              participants.addAll( addedParticipants );

              List<ParticipantInfoView> participantList = new ArrayList<ParticipantInfoView>( childNodeOwners.size() );
              for ( ParticipantInfoView participantInfoView : participants )
              {
                participantList.add( participantInfoView );
              }
              if ( participantList != null )
              {
                for ( ParticipantInfoView addedParticipant : participantList )
                {
                  Budget budget = getBudgetMasterService().getAvailableUserBudgetByBudgetSegmentAndUserId( budgetSegmentId, addedParticipant.getId() );
                  Participant participant = getParticipantService().getParticipantById( addedParticipant.getId() );
                  budgetReallocationForm.addExternalParticipant( budget, participant, US_MEDIA_VALUE );
                }
              }
            }
          }
          else if ( budgetMaster.isNodeBudget() && budgetMaster.isAllowAdditionalTransferrees() )
          {
            List<ParticipantInfoView> nodeOwners = (List<ParticipantInfoView>)request.getSession().getAttribute( ADDED_NODE_OWNERS_SESSION_VARIABLE );
            if ( nodeOwners != null )
            {
              Set<ParticipantInfoView> nodeOwnersSet = new HashSet<ParticipantInfoView>( nodeOwners );

              nodeOwnersSet.addAll( nodeOwners );

              List<ParticipantInfoView> nodeOwnerList = new ArrayList<ParticipantInfoView>( childNodeOwners.size() );
              for ( ParticipantInfoView participantInfoView : nodeOwnersSet )
              {
                nodeOwnerList.add( participantInfoView );
              }
              if ( nodeOwnerList != null )
              {
                for ( ParticipantInfoView addedParticipant : nodeOwnerList )
                {
                  AssociationRequestCollection paxAssociation = new AssociationRequestCollection();
                  paxAssociation.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.USER_NODE ) );
                  Participant participant = getParticipantService().getParticipantByIdWithAssociations( addedParticipant.getId(), paxAssociation );
                  AssociationRequestCollection budgetAssociationRequestCollection = new AssociationRequestCollection();
                  budgetAssociationRequestCollection.add( new BudgetToNodeOwnersAddressAssociationRequest() );
                  Budget budget = getBudgetMasterService().getAvailableNodeBudgetByBudgetSegmentAndNodeId( budgetSegmentId,
                                                                                                           participant.getPrimaryUserNode().getNode().getId(),
                                                                                                           budgetAssociationRequestCollection );
                  budgetReallocationForm.addExternalParticipant( budget, participant, US_MEDIA_VALUE );
                }
              }
            }
          }
          budgetSelected = true;
        }
      }
      else
      {
        budgetReallocationForm.setBudgetSegmentId( new Long( 0 ) );
      }
    }

    return budgetSelected;
  }

  public ActionForward prepareReview( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    BudgetReallocationForm budgetReallocationForm = (BudgetReallocationForm)actionForm;
    String forwardTo = "";

    BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( budgetReallocationForm.getBudgetMasterId(), null );
    BudgetSegment budgetSegment = getBudgetMasterService().getBudgetSegmentById( budgetReallocationForm.getBudgetSegmentId(), null );
    List<BudgetReallocationValueBean> budgetDetails = budgetReallocationForm.getChildReallocationBudgetList();

    for ( Iterator<BudgetReallocationValueBean> iter = budgetDetails.iterator(); iter.hasNext(); )
    {
      BudgetReallocationValueBean br = iter.next();
      long adjustmentValue = NumberFormatUtil.convertStringToLongDefaultZero( br.getAdjustmentAmount() ).longValue();

      if ( br.isNA() )
      {
        if ( ( adjustmentValue == 0 ) || ( String.valueOf( adjustmentValue ).startsWith( "-" ) ) )
        {
          iter.remove();
        }
      }
    }
    budgetReallocationForm.reset( actionMapping, request );
    budgetReallocationForm.setChildReallocationBudgetList( budgetDetails );

    long ownerBudgetAfterAdjustments = NumberFormatUtil.convertStringToLongDefaultZero( budgetReallocationForm.getOwnerBudgetCurrentValue() );
    long totalAdjustments = 0;
    try
    {
      for ( Iterator<BudgetReallocationValueBean> iter = budgetDetails.iterator(); iter.hasNext(); )
      {
        BudgetReallocationValueBean br = iter.next();
        long currentValue = NumberFormatUtil.convertStringToLongDefaultZero( br.getCurrentBudget() ).longValue();
        long adjustmentValue = NumberFormatUtil.convertStringToLongDefaultZero( br.getAdjustmentAmount() ).longValue();

        long calculatedCurrentValue = currentValue;

        if ( adjustmentValue > 0 )
        {
          calculatedCurrentValue += adjustmentValue;
          ownerBudgetAfterAdjustments -= adjustmentValue;
          totalAdjustments += adjustmentValue;
        }
        else if ( adjustmentValue < 0 )
        {
          calculatedCurrentValue += adjustmentValue;
          ownerBudgetAfterAdjustments -= adjustmentValue;
          totalAdjustments += adjustmentValue;
        }

        br.setAmountAfterAdjustment( String.valueOf( calculatedCurrentValue ) );
        br.setTotalAdjustments( String.valueOf( totalAdjustments ) );
        br.setOwnerBudgetAfterAdjustments( String.valueOf( ownerBudgetAfterAdjustments ) );
        budgetReallocationForm.setNA( br.isNA() );
      }

      budgetReallocationForm.setTotalAdjustments( String.valueOf( totalAdjustments ) );
      budgetReallocationForm.setOwnerBudgetAfterAdjustments( String.valueOf( ownerBudgetAfterAdjustments ) );
      budgetReallocationForm.setBudgetMasterName( ContentReaderManager.getText( budgetMaster.getCmAssetCode(), budgetMaster.getNameCmKey() ) );
      budgetReallocationForm.setBudgetSegmentName( budgetSegment.getPaxDisplaySegmentName() );

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

  }

  public ActionForward prepareEdit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final BigDecimal US_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( Country.UNITED_STATES );

    ActionMessages errors = new ActionMessages();
    BudgetReallocationForm budgetReallocationForm = (BudgetReallocationForm)form;
    AssociationRequestCollection budgetMasterAssociationRequestCollection = new AssociationRequestCollection();
    budgetMasterAssociationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
    BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterAndPromotionsByUser( budgetReallocationForm.getBudgetMasterId(), budgetMasterAssociationRequestCollection );

    if ( budgetMaster.isParticipantBudget() && budgetMaster.isAllowAdditionalTransferrees() )
    {
      List<ParticipantInfoView> addedParticipants = (List<ParticipantInfoView>)request.getSession().getAttribute( ADDED_PAX_SESSION_VARIABLE );
      if ( addedParticipants != null )
      {
        for ( ParticipantInfoView addedParticipant : addedParticipants )
        {
          Budget budget = getBudgetMasterService().getAvailableUserBudgetByBudgetSegmentAndUserId( budgetReallocationForm.getBudgetSegmentId(), addedParticipant.getId() );
          Participant participant = getParticipantService().getParticipantById( addedParticipant.getId() );
          budgetReallocationForm.addExternalParticipant( budget, participant, US_MEDIA_VALUE );
        }
      }
    }
    else
    {
      if ( budgetMaster.isNodeBudget() && budgetMaster.isAllowAdditionalTransferrees() )
      {
        budgetReallocationForm.setNodeBudget( true );
        List<ParticipantInfoView> addedParticipants = (List<ParticipantInfoView>)request.getSession().getAttribute( ADDED_NODE_OWNERS_SESSION_VARIABLE );
        if ( addedParticipants != null )
        {
          for ( ParticipantInfoView addedParticipant : addedParticipants )
          {
            AssociationRequestCollection paxAssociation = new AssociationRequestCollection();
            paxAssociation.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.USER_NODE ) );
            Participant participant = getParticipantService().getParticipantByIdWithAssociations( addedParticipant.getId(), paxAssociation );
            AssociationRequestCollection budgetAssociationRequestCollection = new AssociationRequestCollection();
            budgetAssociationRequestCollection.add( new BudgetToNodeOwnersAddressAssociationRequest() );
            Budget budget = getBudgetMasterService().getAvailableNodeBudgetByBudgetSegmentAndNodeId( budgetReallocationForm.getBudgetSegmentId(),
                                                                                                     participant.getPrimaryUserNode().getNode().getId(),
                                                                                                     budgetAssociationRequestCollection );
            budgetReallocationForm.addExternalParticipant( budget, participant, US_MEDIA_VALUE );
          }
        }
      }
    }

    List<BudgetReallocationValueBean> editChildNodeOwnerList = budgetReallocationForm.getChildReallocationBudgetList();
    budgetReallocationForm.reset( mapping, request );
    Long budgetNodeId = null;
    boolean budgetSelected = false;

    if ( StringUtils.isNotBlank( budgetReallocationForm.getOwnerBudgetNodeId() ) )
    {
      budgetNodeId = budgetReallocationForm.getOwnerBudgetNodeId() == null ? null : Long.valueOf( budgetReallocationForm.getOwnerBudgetNodeId() );
    }
    if ( budgetMaster != null )
    {
      BudgetSegment budgetSegmentFromForm = budgetReallocationForm.getBudgetSegmentId() != null ? getBudgetMasterService().getBudgetSegmentById( budgetReallocationForm.getBudgetSegmentId() ) : null;
      if ( null == budgetSegmentFromForm || null != budgetSegmentFromForm && budgetMaster.getBudgetSegments().contains( budgetSegmentFromForm ) )
      {
        // Fetch owner budget details
        Budget ownerBudget = null;
        BigDecimal OWNER_MEDIA_VALUE = null;
        Long budgetSegmentId = budgetReallocationForm.getBudgetSegmentId();
        if ( budgetMaster.getBudgetSegments().size() == 1 )
        {
          budgetSegmentId = budgetMaster.getBudgetSegments().iterator().next().getId();
        }
        if ( budgetMaster.isNodeBudget() )
        {
          if ( budgetMaster.getBudgetSegments().size() == 1 || budgetReallocationForm.getBudgetSegmentId() != null && budgetReallocationForm.getBudgetSegmentId() > 0 )
          {
            AssociationRequestCollection budgetAssociationRequestCollection = new AssociationRequestCollection();
            budgetAssociationRequestCollection.add( new BudgetToNodeOwnersAddressAssociationRequest() );
            ownerBudget = getBudgetMasterService().getAvailableNodeBudgetByBudgetSegmentAndNodeId( budgetSegmentId, budgetNodeId, budgetAssociationRequestCollection );
            OWNER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser( UserManager.getUserId() );
          }
        }
        else if ( budgetMaster.isParticipantBudget() )
        {
          if ( budgetMaster.getBudgetSegments().size() == 1 || budgetReallocationForm.getBudgetSegmentId() != null && budgetReallocationForm.getBudgetSegmentId() > 0 )
          {
            ownerBudget = getBudgetMasterService().getAvailableUserBudgetByBudgetSegmentAndUserId( budgetSegmentId, UserManager.getUserId() );
            if ( ownerBudget != null )
            {
              OWNER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser( ownerBudget.getUser().getId() );
            }
          }
        }

        if ( ownerBudget != null && BudgetStatusType.ACTIVE.equals( ownerBudget.getStatus().getCode() ) )
        {
          // Internationalize owner budget
          BigDecimal convertedCurrentValue = BudgetUtils.applyMediaConversion( ownerBudget.getCurrentValue(), US_MEDIA_VALUE, OWNER_MEDIA_VALUE );
          BigDecimal convertedOriginalValue = BudgetUtils.applyMediaConversion( ownerBudget.getOriginalValue(), US_MEDIA_VALUE, OWNER_MEDIA_VALUE );
          ownerBudget.setCurrentValue( convertedCurrentValue );
          ownerBudget.setOriginalValue( convertedOriginalValue );

          // Fetch child budget details
          // bug fix 59650 - Changed to procedure call
          List<BudgetReallocationValueBean> childNodeOwnersList = new ArrayList<BudgetReallocationValueBean>();
          BigDecimal mediaRatio = BudgetUtils.calculateConversionRatio( US_MEDIA_VALUE, OWNER_MEDIA_VALUE );
          childNodeOwnersList = getBudgetMasterService().fetchChildBudgets( ownerBudget.getId(), Long.valueOf( budgetReallocationForm.getBudgetMasterId() ), budgetSegmentId, mediaRatio );

          for ( BudgetReallocationValueBean brvb : editChildNodeOwnerList )
          {
            childNodeOwnersList.add( brvb );
            Long editChildBudgetId = brvb.getChildBudgetId();
            String editAdjustmentAmount = brvb.getAdjustmentAmount();
            for ( BudgetReallocationValueBean brb : childNodeOwnersList )
            {
              if ( brb.getChildBudgetId() != null && editChildBudgetId != null )
              {
                if ( editChildBudgetId != 0 && brb.getChildBudgetId() != 0 && brb.getChildBudgetId().equals( editChildBudgetId ) )
                {
                  brb.setAdjustmentAmount( editAdjustmentAmount );
                }
              }
            }
          }

          Set<BudgetReallocationValueBean> childNodeOwnersSet = new HashSet<BudgetReallocationValueBean>( childNodeOwnersList );

          childNodeOwnersSet.addAll( childNodeOwnersList );

          List<BudgetReallocationValueBean> childList = new ArrayList<BudgetReallocationValueBean>( childNodeOwnersSet.size() );
          for ( BudgetReallocationValueBean budgetReallocationValueBean : childNodeOwnersSet )
          {
            if ( StringUtils.isNotEmpty( budgetReallocationValueBean.getUserId() ) )
            {
              childList.add( budgetReallocationValueBean );
            }
          }
          // Load budget reallocation form details

          budgetReallocationForm.load( childList, budgetMaster, ownerBudget );
          budgetSelected = true;
        }
      }
      else
      {
        budgetReallocationForm.setBudgetSegmentId( new Long( 0 ) );
      }
      request.setAttribute( "displayAdditionalManagerAdd", budgetMaster.isAllowAdditionalTransferrees() );
    }
    if ( !budgetSelected && budgetReallocationForm.getBudgetSegmentId() != null && budgetReallocationForm.getBudgetSegmentId() > 0 )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.BUDGET_NONE_TO_TRANSFER_FOR_SELECTED_SEGMENT ) );
      request.setAttribute( Globals.ERROR_KEY, errors );
      request.setAttribute( "isInfMsg", Boolean.TRUE );
      request.setAttribute( "displayAdditionalManagerAdd", budgetMaster.isAllowAdditionalTransferrees() );
    }
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward update( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    ActionMessages errors = new ActionMessages();
    BudgetReallocationForm budgetReallocationForm = (BudgetReallocationForm)actionForm;
    final BigDecimal US_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( Country.UNITED_STATES );

    BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( budgetReallocationForm.getBudgetMasterId(), null );

    Long budgetSegmentId = budgetReallocationForm.getBudgetSegmentId();
    BudgetSegment budgetSegment = getBudgetMasterService().getBudgetSegmentById( budgetSegmentId );

    AssociationRequestCollection budgetAssociationRequestCollection = new AssociationRequestCollection();
    budgetAssociationRequestCollection.add( new BudgetToNodeOwnersAddressAssociationRequest() );
    Budget ownerBudget = getBudgetMasterService().getBudgetbyId( Long.valueOf( budgetReallocationForm.getOwnerBudgetId() ), budgetAssociationRequestCollection );
    BigDecimal OWNER_MEDIA_VALUE = null;

    OWNER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser( UserManager.getUserId() );

    BigDecimal totalAdjustedValue = new BigDecimal( 0 );
    String childBudgetIdString = "";
    String adjustedAmountString = "";
    int indexCount = 0;

    for ( BudgetReallocationValueBean brvb : budgetReallocationForm.getChildReallocationBudgetList() )
    {
      if ( StringUtils.isNotBlank( brvb.getAdjustmentAmount() ) && !brvb.getAdjustmentAmount().equals( "0" ) )
      {
        BigDecimal adjustmentValue = BudgetUtils.applyMediaConversion( new BigDecimal( brvb.getAdjustmentAmount() ), OWNER_MEDIA_VALUE, US_MEDIA_VALUE );
        totalAdjustedValue = totalAdjustedValue.add( adjustmentValue );
        Budget childNodeOwnerBudget;

        if ( brvb.getChildBudgetId() != null && brvb.getChildBudgetId() != 0 )
        {
          childNodeOwnerBudget = getBudgetMasterService().getBudgetbyId( brvb.getChildBudgetId(), budgetAssociationRequestCollection );
        }
        else
        {
          Budget childBudget = new Budget();
          childBudget.setBudgetSegment( budgetSegment );
          if ( !StringUtils.isEmpty( brvb.getUserId() ) )
          {
            childBudget.setUser( getUserService().getUserById( Long.parseLong( brvb.getUserId() ) ) );
          }
          if ( !StringUtils.isEmpty( brvb.getChildNodeOwnerNodeId() ) )
          {
            childBudget.setNode( getNodeService().getNodeById( Long.parseLong( brvb.getChildNodeOwnerNodeId() ) ) );
          }
          AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
          auditCreateInfo.setCreatedBy( Long.valueOf( 5662 ) );
          auditCreateInfo.setDateCreated( new Timestamp( new Date().getTime() ) );
          childBudget.setOriginalValue( new BigDecimal( 0 ) );
          childBudget.setCurrentValue( new BigDecimal( 0 ) );
          childBudget.setOverdrawn( null );
          childBudget.setActionType( BudgetActionType.lookup( BudgetActionType.TRANSFER ) );
          childBudget.setAuditCreateInfo( auditCreateInfo );
          childBudget.setStatus( BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) );
          childBudget.setVersion( new Long( 0 ) );

          childNodeOwnerBudget = getBudgetMasterService().saveBudget( childBudget );

        }
        try
        {
          if ( indexCount == 0 )
          {
            childBudgetIdString = childNodeOwnerBudget.getId() + "";
            adjustedAmountString = adjustmentValue + "";
          }
          else
          {
            childBudgetIdString += "," + childNodeOwnerBudget.getId();
            adjustedAmountString += "," + adjustmentValue;
          }
          // reallocate budget
          childNodeOwnerBudget.setActionType( BudgetActionType.lookup( BudgetActionType.TRANSFER ) );
          getBudgetMasterService().reallocateBudget( budgetSegment, ownerBudget, childNodeOwnerBudget, adjustmentValue );
          brvb.setChildBudget( childNodeOwnerBudget );
        }
        catch( ServiceErrorException se )
        {
          ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( se.getServiceErrors(), errors );
        }
        indexCount++;
      }

    }

    if ( !StringUtils.isEmpty( childBudgetIdString ) )
    {
      Process process = getProcessService().createOrLoadSystemProcess( BudgetTransferMailingProcess.PROCESS_NAME, BudgetTransferMailingProcess.BEAN_NAME );

      LinkedHashMap parameterValueMap = new LinkedHashMap();
      parameterValueMap.put( "childBudgetIdString", new String[] { childBudgetIdString } );
      parameterValueMap.put( "adjustmentAmountString", new String[] { adjustedAmountString } );
      parameterValueMap.put( "budgetMasterId", new String[] { budgetMaster.getId() + "" } );
      parameterValueMap.put( "budgetSegmentId", new String[] { budgetSegment.getId() + "" } );

      ProcessSchedule processSchedule = new ProcessSchedule();
      processSchedule.setStartDate( new Date() );
      processSchedule.setTimeOfDayMillis( new Long( 0 ) );
      processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

      getProcessService().scheduleProcess( process, processSchedule, parameterValueMap, UserManager.getUserId() );
    }

    Long sourceNodeId = null;
    Long sourceUserId = null;
    ownerBudget.setCurrentValue( ownerBudget.getCurrentValue().subtract( totalAdjustedValue ) );
    ownerBudget.setOriginalValue( ownerBudget.getOriginalValue().subtract( totalAdjustedValue ) );

    if ( ownerBudget.getCurrentValue().compareTo( ownerBudget.getOriginalValue() ) > 0 )
    {
      ownerBudget.setOriginalValue( ownerBudget.getCurrentValue() );
    }
    if ( ownerBudget.getBudgetSegment().getBudgetMaster().isNodeBudget() )
    {
      sourceNodeId = ownerBudget.getNode().getId();
      ownerBudget.setActionType( BudgetActionType.lookup( BudgetActionType.TRANSFER ) );
      getBudgetMasterService().updateNodeBudget( budgetSegment, sourceNodeId, ownerBudget );
    }
    else if ( ownerBudget.getBudgetSegment().getBudgetMaster().isParticipantBudget() )
    {
      sourceUserId = ownerBudget.getUser().getId();
      ownerBudget.setActionType( BudgetActionType.lookup( BudgetActionType.TRANSFER ) );
      getBudgetMasterService().updateUserBudget( budgetSegment, sourceUserId, ownerBudget );
    }

    // load newly updated budget values
    ownerBudget = getBudgetMasterService().getBudgetbyId( ownerBudget.getId(), budgetAssociationRequestCollection );
    BigDecimal finalBudgetAmount = BudgetUtils.applyMediaConversion( ownerBudget.getCurrentValue(), US_MEDIA_VALUE, OWNER_MEDIA_VALUE );

    User nodeOwner = getBudgetOwner( ownerBudget );
    sendBudgetReallocationEmail( nodeOwner, budgetReallocationForm.getChildReallocationBudgetList(), budgetMaster, budgetSegment, finalBudgetAmount );

    if ( budgetMaster.isNodeBudget() )
    {
      List<User> users = getManagers( ownerBudget.getNode().getId() );
      for ( User user : users )
      {
        sendBudgetReallocationEmail( user, budgetReallocationForm.getChildReallocationBudgetList(), budgetMaster, budgetSegment, finalBudgetAmount );
      }
    }

    String forwardAction = ActionConstants.SUCCESS_UPDATE;
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardAction = ActionConstants.FAIL_FORWARD;
    }

    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    clientStateParameterMap.put( "budgetConfirmation", true );
    return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ) } );
  }

  private void sendBudgetReallocationEmail( User user, List<BudgetReallocationValueBean> reallocationBudgetList, BudgetMaster budgetMaster, BudgetSegment budgetSegment, BigDecimal finalBudgetAmount )
  {
    User budgetAllocator = user;

    if ( budgetAllocator != null )
    {
      // Create mailing recipient
      MailingRecipient mr = new MailingRecipient();
      mr.setUser( budgetAllocator );
      mr.setLocale( budgetAllocator.getLanguageType() == null ? getSystemVariableService().getDefaultLanguage().getStringVal() : budgetAllocator.getLanguageType().getCode() );
      mr.setGuid( GuidUtils.generateGuid() );

      // Populate data map
      Map<String, Object> dataMap = new HashMap<String, Object>();
      dataMap.put( "firstName", budgetAllocator.getFirstName() );
      dataMap.put( "lastName", budgetAllocator.getLastName() );
      dataMap.put( "budgetMasterName", ContentReaderManager.getText( budgetMaster.getCmAssetCode(), budgetMaster.getNameCmKey() ) );
      dataMap.put( "timePeriodStartDate", budgetSegment.getDisplayStartDate() );
      dataMap.put( "timePeriodEndDate", budgetSegment.getDisplayEndDate() );
      dataMap.put( "budgetRemainingBalance", String.valueOf( BudgetUtils.getBudgetDisplayValue( finalBudgetAmount ) ) );
      String programName = getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
      dataMap.put( "programName", programName );

      StringBuilder budgetTransferSummary = new StringBuilder();
      for ( BudgetReallocationValueBean brvb : reallocationBudgetList )
      {
        if ( StringUtils.isNotBlank( brvb.getAdjustmentAmount() ) && !brvb.getAdjustmentAmount().equals( "0" ) )
        {
          budgetTransferSummary.append( getBudgetDisplayName( brvb.getChildBudget() ) );
          budgetTransferSummary.append( ": " );
          budgetTransferSummary.append( brvb.getAdjustmentAmount() );
          budgetTransferSummary.append( "<br/>" );
        }
      }
      dataMap.put( "budgetTransferSummary", budgetTransferSummary.toString() );

      mr.addMailingRecipientDataFromMap( dataMap );

      // Create mailing object
      Message message = getMessageService().getMessageByCMAssetCode( MessageService.BUDGET_REALLOCATION_NOTIFICATION_MESSAGE_CM_ASSET_CODE );
      Mailing mailing = new Mailing();
      mailing.setMessage( message );
      mailing.addMailingRecipient( mr );
      mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
      mailing.setSender( "Incentive System Mailbox" );
      mailing.setMailingType( MailingType.lookup( MailingType.CLAIM_FORM_STEP ) );
      mailing.setGuid( GuidUtils.generateGuid() );

      getMailingService().submitMailing( mailing, null );
    }
  }

  private BigDecimal getBudgetMediaValueForBudget( Budget budget )
  {
    User budgetOwner = getBudgetOwner( budget );
    if ( budgetOwner != null )
    {
      return budgetOwner.getId() == null ? null : getUserService().getBudgetMediaValueForUser( budgetOwner.getId() );
    }
    return null;
  }

  private User getBudgetOwner( Budget budget )
  {
    User budgetOwner = null;
    if ( budget.getBudgetSegment().getBudgetMaster().isNodeBudget() )
    {
      budgetOwner = budget.getNode().getNodeOwner() == null ? null : budget.getNode().getNodeOwner();
    }
    else if ( budget.getBudgetSegment().getBudgetMaster().isParticipantBudget() )
    {
      budgetOwner = budget.getUser();
    }

    return budgetOwner;
  }

  private List<User> getManagers( Long nodeId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    List<User> managers = getUserService().getAllUsersOnNodeHavingRole( nodeId, HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ), associationRequestCollection );
    return managers;
  }

  private String getBudgetDisplayName( Budget budget )
  {
    String budgetDisplayName = null;
    if ( budget.getBudgetSegment().getBudgetMaster().isNodeBudget() )
    {
      budgetDisplayName = budget.getNode().getName();
    }
    else if ( budget.getBudgetSegment().getBudgetMaster().isParticipantBudget() )
    {
      budgetDisplayName = budget.getUser().getNameFLNoComma();
    }

    return budgetDisplayName;
  }

  public ActionForward addAdditionalPax( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    BudgetReallocationForm budgetReallocationForm = (BudgetReallocationForm)actionForm;
    if ( budgetReallocationForm.getBudgetMasterId() != null && budgetReallocationForm.getBudgetMasterId() > 0 )
    {
      AssociationRequestCollection budgetMasterAssociationRequestCollection = new AssociationRequestCollection();
      budgetMasterAssociationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
      BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( budgetReallocationForm.getBudgetMasterId(), budgetMasterAssociationRequestCollection );

      if ( budgetReallocationForm.getParticipantsAsList() != null )
      {
        List<ParticipantInfoView> existingParticipants = (List<ParticipantInfoView>)request.getSession().getAttribute( ADDED_PAX_SESSION_VARIABLE );

        List<ParticipantInfoView> existingNodeOwners = (List<ParticipantInfoView>)request.getSession().getAttribute( ADDED_NODE_OWNERS_SESSION_VARIABLE );

        if ( budgetMaster.isParticipantBudget() && existingParticipants == null )
        {
          request.getSession().setAttribute( ADDED_PAX_SESSION_VARIABLE, budgetReallocationForm.getParticipantsAsList() );
        }
        else if ( budgetMaster.isParticipantBudget() && existingParticipants != null )
        {
          List<ParticipantInfoView> totalParticipants = new ArrayList<ParticipantInfoView>();
          totalParticipants.addAll( existingParticipants );
          totalParticipants.addAll( budgetReallocationForm.getParticipantsAsList() );
          request.getSession().setAttribute( ADDED_PAX_SESSION_VARIABLE, totalParticipants );
        }
        if ( budgetMaster.isNodeBudget() && existingNodeOwners == null )
        {
          request.getSession().setAttribute( ADDED_NODE_OWNERS_SESSION_VARIABLE, budgetReallocationForm.getParticipantsAsList() );
        }
        else if ( budgetMaster.isNodeBudget() && existingNodeOwners != null )
        {
          List<ParticipantInfoView> totalParticipants = new ArrayList<ParticipantInfoView>();
          totalParticipants.addAll( existingNodeOwners );
          totalParticipants.addAll( budgetReallocationForm.getParticipantsAsList() );
          request.getSession().setAttribute( ADDED_NODE_OWNERS_SESSION_VARIABLE, totalParticipants );
        }
      }
    }
    super.writeAsJsonToResponse( new WebErrorMessageList(), response );
    return null;
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  }

  private MailingService getMailingService()
  {
    return (MailingService)getService( MailingService.BEAN_NAME );
  }

  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

}
