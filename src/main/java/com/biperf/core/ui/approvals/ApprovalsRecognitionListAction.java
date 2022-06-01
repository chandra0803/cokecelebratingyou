/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsRecognitionListAction.java,v $
 */

package com.biperf.core.ui.approvals;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.BudgetUsageOverAllocallatedException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.ssi.SSIContestPaxClaimService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.claim.AbstractRecognitionApprovalAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.ListPageInfo;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.TcccClientUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ActivityCenterValueBean;
import com.biperf.core.value.ApprovalsNoEmailBean;
import com.biperf.core.value.PromotionApprovableValue;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * RecognitionApprovalListAction.
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
 * <td>Aug 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalsRecognitionListAction extends AbstractRecognitionApprovalAction
{
  private static final Log logger = LogFactory.getLog( ApprovalsRecognitionListAction.class );

  public static final Long APPROVAL_LIST_PAGE_SIZE = 20L;
  public static final String APPROVABLES = "approvables";
  public static final String DISPLAY_METHOD = "prepareUpdate";
  public static final String SAVE_METHOD = "saveApprovals";

  private static final String RECOGNITION_APPROVAL = "RecognitionApproval";
  private static final String NOMINATION_APPROVAL = "NominationApproval";
  private static final String CLAIM_APPROVAL = "ClaimApproval";
  private static final String SSI_APPROVAL = "SSIApproval";

  protected ActionForward cancelled( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  public ActionForward prepareUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws InvalidClientStateException
  {
    checkForSaveOccurred( request );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward saveApprovals( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws InvalidClientStateException
  {
    ApprovalsRecognitionListForm approvalsRecognitionListForm = (ApprovalsRecognitionListForm)form;
    User approver = getUserService().getUserById( UserManager.getUserId() );

    approvalsRecognitionListForm.setRequestedPage( approvalsRecognitionListForm.getListPageInfo().getCurrentPage() );
    List<AbstractRecognitionClaim> claims = buildPaginatedApprovablesList( approvalsRecognitionListForm, true );

    approvalsRecognitionListForm.populateRecognitionRecipientDomainObjects( claims, approver );

    // Remove pending approvables, since no action is required.
    List<Approvable> nonPendingApprovables = filterPendingApprovables( claims );
    List serviceErrors = new ArrayList();
    String budgetMsg = "";
    boolean budgetNull = false;
    boolean errorOccurred = false;

    if ( !nonPendingApprovables.isEmpty() )
    {
      Map claimsMapByPromotion = new HashMap();
      Map claimsMapBySubmitter = new HashMap();
      Map claimsMapByNode = new HashMap();

      String promoErrorMsg;

      for ( Iterator iterator = nonPendingApprovables.iterator(); iterator.hasNext(); )
      {
        Claim promoClaim = (Claim)iterator.next();

        Promotion claimPromo = promoClaim.getPromotion();
        List singlePromoClaims = (List)claimsMapByPromotion.get( claimPromo );
        if ( singlePromoClaims == null )
        {
          singlePromoClaims = new ArrayList();
          claimsMapByPromotion.put( claimPromo, singlePromoClaims );
        }
        singlePromoClaims.add( promoClaim );
      }
      for ( Iterator iter = claimsMapByPromotion.keySet().iterator(); iter.hasNext(); )
      {

        List claimsToBeSaved = new ArrayList();
        List promoClaims = (List)claimsMapByPromotion.get( (Promotion)iter.next() );
        Claim claim = (Claim)promoClaims.get( 0 );
        Promotion claimPromotion = claim.getPromotion();

        if ( claimPromotion.getBudgetMaster() != null )
        {
          BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( claimPromotion.getBudgetMaster().getId(), null );

          if ( budgetMaster.isNodeBudget() )
          {
            for ( Iterator iterNode = promoClaims.iterator(); iterNode.hasNext(); )
            {
              Claim promoClaim = (Claim)iterNode.next();

              Node claimNode = promoClaim.getNode();
              List singleNodeClaims = (List)claimsMapByNode.get( claimNode );
              if ( singleNodeClaims == null )
              {
                singleNodeClaims = new ArrayList();
                claimsMapByNode.put( claimNode, singleNodeClaims );
              }
              singleNodeClaims.add( promoClaim );
            }
          }
          else if ( budgetMaster.isParticipantBudget() )
          {
            for ( Iterator iterPax = promoClaims.iterator(); iterPax.hasNext(); )
            {
              Claim promoClaim = (Claim)iterPax.next();

              User submitter = promoClaim.getSubmitter();
              List singleSubmitterClaims = (List)claimsMapBySubmitter.get( submitter );
              if ( singleSubmitterClaims == null )
              {
                singleSubmitterClaims = new ArrayList();
                claimsMapBySubmitter.put( submitter, singleSubmitterClaims );
              }
              singleSubmitterClaims.add( promoClaim );
            }
          }

          if ( budgetMaster.isCentralBudget() )
          {
            BigDecimal totalApprovedAwardAmount = new BigDecimal( 0 );
            boolean budgetExceeded = false;
            List claimsToBeApproved = new ArrayList();
            Budget budget = getPromotionService().getAvailableBudget( claimPromotion.getId(),
                                                                      ( (Claim)promoClaims.get( 0 ) ).getSubmitter().getId(),
                                                                      ( (Claim)promoClaims.get( 0 ) ).getNode().getId() );
            int denied = 0;
            int approved = 0;

            for ( Iterator iter1 = promoClaims.iterator(); iter1.hasNext(); )
            {
              Claim promoClaim = (Claim)iter1.next();
              BigDecimal claimAward = BigDecimal.valueOf( getPromotionService().getClaimAwardQuantity( promoClaim.getId() ) );
              ApprovableItem approvableItem = (ApprovableItem)promoClaim.getApprovableItems().iterator().next();
              if ( approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.APPROVED ) )
              {
                totalApprovedAwardAmount = totalApprovedAwardAmount.add( claimAward );
                approved++;
              }
              else if ( approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.DENIED ) )
              {
                claimsToBeApproved.add( promoClaim );
                denied++;
              }

            }
            if ( budget == null )
            {
              if ( approved > 0 )
              {
                budgetNull = true;
                claimsToBeSaved.addAll( claimsToBeApproved );
              }
              else if ( denied > 0 )
              {
                claimsToBeSaved.addAll( claimsToBeApproved );
              }
            }
            else if ( ( budget.getCurrentValue().subtract( totalApprovedAwardAmount ) ).compareTo( BigDecimal.ZERO ) < 0 )
            {
              budgetExceeded = true;
              claimsToBeSaved.addAll( claimsToBeApproved );
            }
            else
            {
              claimsToBeSaved.addAll( promoClaims );
            }

            if ( budgetNull || budgetExceeded )
            {
              budgetMsg = budgetMsg + claimPromotion.getName() + ", ";
              errorOccurred = true;
            }

          } // if of central budget
          else if ( budgetMaster.isParticipantBudget() )
          {
            List claimsToBeApproved = new ArrayList();
            boolean budgetExceeded = false;
            for ( Iterator iter2 = claimsMapBySubmitter.keySet().iterator(); iter2.hasNext(); )
            {
              BigDecimal totalApprovedAwardAmount = new BigDecimal( 0 );
              BigDecimal totalApprovedBudgetDeductedAmount = new BigDecimal( 0 );

              Participant submitter = (Participant)iter2.next();
              Budget budget = getPromotionService().getAvailableBudget( claimPromotion.getId(),
                                                                        submitter.getId(),
                                                                        ( (Claim) ( (List)claimsMapBySubmitter.get( submitter ) ).get( 0 ) ).getNode().getId() );
              int denied = 0;
              int approved = 0;

              for ( Iterator giverIter = ( (List)claimsMapBySubmitter.get( submitter ) ).iterator(); giverIter.hasNext(); )
              {
                Claim promoClaim = (Claim)giverIter.next();

                BigDecimal claimAward = BigDecimal.valueOf( getPromotionService().getClaimAwardQuantity( promoClaim.getId() ) );
                ApprovableItem approvableItem = (ApprovableItem)promoClaim.getApprovableItems().iterator().next();
                if ( approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.APPROVED ) )
                {
                  RecognitionClaim recognitionClaim = (RecognitionClaim)promoClaim;
                  BigDecimal calculatedBudgetValue = calculateBudgetEquivalence( BigDecimal.valueOf( claimAward.intValue() ), recognitionClaim.getClaimRecipients().iterator().next().getRecipient() );
                  log.error( "claimId=" + claim.getId() + " calculatedBudgetValue=" + calculatedBudgetValue );
                  totalApprovedBudgetDeductedAmount = totalApprovedBudgetDeductedAmount.add( calculatedBudgetValue );
                  totalApprovedAwardAmount = totalApprovedAwardAmount.add( claimAward );
                  approved++;
                }
                else if ( approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.DENIED ) )
                {
                  claimsToBeApproved.add( promoClaim );
                  denied++;
                }
              } // for
              if ( budget == null )
              {
                if ( approved > 0 )
                {
                  budgetNull = true;
                  claimsToBeSaved.addAll( claimsToBeApproved );
                }
                else if ( denied > 0 )
                {
                  claimsToBeSaved.addAll( claimsToBeApproved );
                }
              }
              else if ( ( budget.getCurrentValue().subtract( totalApprovedBudgetDeductedAmount ) ).compareTo( BigDecimal.ZERO ) < 0 )
              {
                budgetExceeded = true;
                claimsToBeSaved.addAll( claimsToBeApproved );
              }
              else
              {
                claimsToBeSaved.addAll( (List)claimsMapBySubmitter.get( submitter ) );
              }

            } // for
            if ( budgetExceeded || budgetNull )
            {
              budgetMsg = budgetMsg + claimPromotion.getName() + ", ";
              errorOccurred = true;

            }
          } // end of Pax

          else if ( budgetMaster.isNodeBudget() )
          {
            List claimsToBeApproved = new ArrayList();
            boolean budgetExceeded = false;
            for ( Iterator iter3 = claimsMapByNode.keySet().iterator(); iter3.hasNext(); )
            {
              BigDecimal totalApprovedBudgetDeductedAmount = new BigDecimal( 0 );
              Node submitterNode = (Node)iter3.next();
              Budget budget = getPromotionService().getAvailableBudget( claimPromotion.getId(),
                                                                        ( (Claim) ( (List)claimsMapByNode.get( submitterNode ) ).get( 0 ) ).getSubmitter().getId(),
                                                                        submitterNode.getId() );
              int denied = 0;
              int approved = 0;

              for ( Iterator nodeIter = ( (List)claimsMapByNode.get( submitterNode ) ).iterator(); nodeIter.hasNext(); )
              {
                Claim promoClaim = (Claim)nodeIter.next();

                BigDecimal claimAward = BigDecimal.valueOf( getPromotionService().getClaimAwardQuantity( promoClaim.getId() ) );
                ApprovableItem approvableItem = (ApprovableItem)promoClaim.getApprovableItems().iterator().next();
                if ( approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.APPROVED ) )
                {
                  RecognitionClaim recognitionClaim = (RecognitionClaim)promoClaim;
                  BigDecimal calculatedBudgetValue = calculateBudgetEquivalence( BigDecimal.valueOf( claimAward.intValue() ), recognitionClaim.getClaimRecipients().iterator().next().getRecipient() );
                  log.error( "claimId=" + claim.getId() + " calculatedBudgetValue=" + calculatedBudgetValue );
                  totalApprovedBudgetDeductedAmount = totalApprovedBudgetDeductedAmount.add( calculatedBudgetValue );
                }
                else if ( approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.DENIED ) )
                {
                  denied++;
                  claimsToBeApproved.add( promoClaim );
                }
              } // for
              if ( budget == null )
              {
                if ( approved > 0 )
                {
                  budgetNull = true;
                  claimsToBeSaved.addAll( claimsToBeApproved );
                }
                else if ( denied > 0 )
                {
                  claimsToBeSaved.addAll( claimsToBeApproved );
                }
              }
              else if ( ( budget.getCurrentValue().subtract( totalApprovedBudgetDeductedAmount ) ).compareTo( BigDecimal.ZERO ) < 0 )
              {
                budgetExceeded = true;
                claimsToBeSaved.addAll( claimsToBeApproved );

              }
              else
              {
                claimsToBeSaved.addAll( (List)claimsMapByNode.get( submitterNode ) );

              }

            } // for
            if ( budgetExceeded || budgetNull )
            {
              budgetMsg = budgetMsg + claimPromotion.getName() + ", ";
              errorOccurred = true;

            }

          } // end of Node
        } // end of budgetmaser null check
        else
        {
          for ( Iterator iter1 = promoClaims.iterator(); iter1.hasNext(); )
          {
            Claim promoClaim = (Claim)iter1.next();
            claimsToBeSaved.add( promoClaim );
          }
        }
        try
        {
          getClaimService().saveClaims( claimsToBeSaved, null, approver, false );
        }
        catch( ServiceErrorException e )
        {
          handleServiceErrorException( request, e );
          return mapping.findForward( ActionConstants.FAIL_UPDATE );
        }
        List<ApprovalsNoEmailBean> noEmailBeans = getApprovedFormattedValueBeansForRecipientsWithNoEmail( claims );
        if ( !noEmailBeans.isEmpty() )
        {
          Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
          clientStateParameterMap.put( "noEmailList", noEmailBeans );
        }
      } // for by promotion
      try
      {
        if ( errorOccurred )
        {
          request.getSession().setAttribute( "saveOccurred", "false" );
          budgetMsg = budgetMsg.substring( 0, budgetMsg.length() - 2 );
          serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.HARD_CAP_BUDGET_EXCEEDED_RECOGNITION, budgetMsg ) );
          throw new BeaconRuntimeException( new BudgetUsageOverAllocallatedException( serviceErrors ) );
        }
      }
      catch( BeaconRuntimeException beaconException )
      {
        if ( beaconException.indexOfThrowable( ServiceErrorException.class ) != -1 )
        {
          ServiceErrorException serviceErrorException = (ServiceErrorException)beaconException.getThrowable( beaconException.indexOfThrowable( ServiceErrorException.class ) );
          handleServiceErrorException( request, serviceErrorException );
          return mapping.findForward( ActionConstants.FAIL_UPDATE );
        }
      }
    }

    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    clientStateParameterMap.put( "saveOccurred", Boolean.TRUE );
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ) } );
  }

  /**
   * Remove pending approvables.
   */
  private List<Approvable> filterPendingApprovables( List<AbstractRecognitionClaim> claim )
  {
    List<Approvable> nonPendingApprovables = new ArrayList<Approvable>();

    for ( AbstractRecognitionClaim approvable : claim )
    {
      ApprovableItem approvableItem = (ApprovableItem)approvable.getApprovableItems().iterator().next();
      if ( !approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.PENDING ) )
      {
        nonPendingApprovables.add( approvable );
      }
    }

    return nonPendingApprovables;
  }

  public ActionForward fetchApprovalsForTileAndList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ApprovalsIndexCollectionView view = new ApprovalsIndexCollectionView();
    List<ActivityCenterValueBean> pendingAlertsList = null;
    String siteUrl = null;
    List<SSIContestListValueBean> waitingForApprovalContests = null;
    List<SSIContestListValueBean> waitingForApprovalClaims = new ArrayList<SSIContestListValueBean>();
    if ( UserManager.getUser().isParticipant() )
    {
      pendingAlertsList = getPromotionService().getPendingAlertsList( UserManager.getUserId(), getEligiblePromotions( request ) );
      waitingForApprovalContests = getSSIContestService().getContestWaitingForApprovalByUserId( UserManager.getUserId() );
      waitingForApprovalClaims = getSSIContestPaxClaimService().getPaxClaimsViewAllByApproverId( UserManager.getUserId() );
      siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    }
    if ( pendingAlertsList != null )
    {
      for ( Object obj : pendingAlertsList )
      {
        ActivityCenterValueBean bean = (ActivityCenterValueBean)obj;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put( "promotionId", bean.getPromotion().getId() );
        ApprovalsIndexCollectionItem item = new ApprovalsIndexCollectionItem( bean.getPromotion().getId(), bean.getPromotion().getName() );
        if ( PromotionType.NOMINATION.equals( bean.getPromotion().getPromotionType().getCode() ) )
        {
          addNominationApprovals( item, bean, paramMap, siteUrl, view );
        }
        if ( PromotionType.RECOGNITION.equals( bean.getPromotion().getPromotionType().getCode() ) )
        {
          addRecognitionApprovals( item, bean, paramMap, siteUrl, view );
        }
        else if ( PromotionType.PRODUCT_CLAIM.equals( bean.getPromotion().getPromotionType().getCode() ) )
        {
          addProductClaimApprovals( item, bean, paramMap, siteUrl, view );
        }
      }
    }
    if ( waitingForApprovalContests != null && waitingForApprovalContests.size() > 0 )
    {
      addContestApprovals( view, waitingForApprovalContests, siteUrl );
    }
    if ( waitingForApprovalClaims != null && waitingForApprovalClaims.size() > 0 )
    {
      addPaxClaimsApprovals( view, waitingForApprovalClaims, siteUrl );
    }

    if ( view.getNominationApprovals() != null )
    {
      view.setNominationapprovalsCount( view.getNominationApprovals().size() );
    }

    if ( view.getRecognitionApprovals() != null )
    {
      view.setRecognitionapprovalsCount( view.getRecognitionApprovals().size() );
    }

    if ( view.getClaimApprovals() != null )
    {
      Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
      view.setClaimUrl( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_CLAIM_PRODUCT_APPROVE_LIST, clientStateParameterMap ) );
      view.setClaimapprovalsCount( view.getClaimApprovals().size() );
    }

    if ( view.getSsiApprovals() != null )
    {
      view.setSsiapprovalsCount( view.getSsiApprovals().size() );
    }

    view.setSsiapprovalsPendingCount( view.getSsiApprovals().size() );

    super.writeAsJsonToResponse( view, response );
    return null;
  }

  private void addPaxClaimsApprovals( ApprovalsIndexCollectionView view, List<SSIContestListValueBean> waitingForApprovalClaims, String siteUrl )
  {
    SSIPromotion ssiPromotion = getSSIPromotionService().getLiveSSIPromotion();
    for ( SSIContestListValueBean claimvalueBean : waitingForApprovalClaims )
    {
      ApprovalsIndexCollectionItem item = new ApprovalsIndexCollectionItem( claimvalueBean.getContestId(),
                                                                            getCMAssetService().getString( claimvalueBean.getName(), SSIContest.CONTEST_CMASSET_NAME, UserManager.getLocale(), true ) );

      Date approveByDate = DateUtils.getDateAfterNumberOfDays( claimvalueBean.getClaimSubmissionLastDate(), ssiPromotion.getDaysToApproveClaim() );
      item.setAlertMessage( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.PAX_CLAIM_MESSGE" ),
                                                  new Object[] { DateUtils.toDisplayString( approveByDate ) } ) );
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put( "contestId", claimvalueBean.getContestId() );
      item.setUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.SSI_CLAIM_APPROVAL_LIST, paramMap ) );
      item.setType( SSI_APPROVAL );
      item.setNumberOfApprovables( waitingForApprovalClaims.size() );
      view.getSsiApprovals().add( item );
    }

  }

  private void addRecognitionApprovals( ApprovalsIndexCollectionItem item, ActivityCenterValueBean bean, Map<String, Object> paramMap, String siteUrl, ApprovalsIndexCollectionView view )
  {
    item.setType( RECOGNITION_APPROVAL );
    item.setAlertMessage( CmsResourceBundle.getCmsBundle().getString( "recognition.approval.list.REVIEW_APPROVALS" ) );
    item.setUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.APPROVALS_RECOG_LIST, paramMap ) );
    item.setNumberOfApprovables( bean.getNumberOfApprovables() );
    view.getRecognitionApprovals().add( item );
  }

  private void addNominationApprovals( ApprovalsIndexCollectionItem item, ActivityCenterValueBean bean, Map<String, Object> paramMap, String siteUrl, ApprovalsIndexCollectionView view )
  {
    item.setType( NOMINATION_APPROVAL );
    long numberOfApprovables = bean.getNumberOfApprovables();
    long numberOfApproved = bean.getNumberOfApproved();
    if ( numberOfApprovables > 0 )
    {
      paramMap.put( "defaultDisplayPendingApprovals", Boolean.TRUE );
    }
    else
    {
      paramMap.put( "defaultDisplayPendingApprovals", Boolean.FALSE );
    }

    if ( numberOfApprovables > 0 )
    {
      item.setAlertMessage( CmsResourceBundle.getCmsBundle().getString( "recognition.approval.list.REVIEW_APPROVALS" ) );
      paramMap.put( "isAllApprovablesApproved", false );
    }
    else if ( numberOfApproved > 0 )
    {
      item.setAlertMessage( CmsResourceBundle.getCmsBundle().getString( "recognition.approval.list.REVIEW_CLOSED_APPROVALS" ) );
      paramMap.put( "isAllApprovablesApproved", true );
    }
    item.setUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.APPROVALS_NOMI_LIST, paramMap ) );
    item.setNumberOfApprovables( numberOfApprovables );
    item.setNumberOfApproved( numberOfApproved ); // only for nominations
    view.getNominationApprovals().add( item );
  }

  private void addProductClaimApprovals( ApprovalsIndexCollectionItem item, ActivityCenterValueBean bean, Map<String, Object> paramMap, String siteUrl, ApprovalsIndexCollectionView view )
  {
    // Bug 49790 - Should always display tile even the claims are closed or approved by
    // another user.
    item.setType( CLAIM_APPROVAL );
    long numberOfApprovables = bean.getNumberOfApprovables();
    long numberOfApproved = bean.getNumberOfApproved();
    if ( numberOfApprovables > 0 )
    {
      paramMap.put( "defaultDisplayPendingApprovals", Boolean.TRUE );
    }
    else
    {
      paramMap.put( "defaultDisplayPendingApprovals", Boolean.FALSE );
    }

    if ( numberOfApprovables > 0 )
    {
      item.setAlertMessage( CmsResourceBundle.getCmsBundle().getString( "recognition.approval.list.REVIEW_APPROVALS" ) );
    }
    else if ( numberOfApproved > 0 )
    {
      item.setAlertMessage( CmsResourceBundle.getCmsBundle().getString( "recognition.approval.list.REVIEW_CLOSED_APPROVALS" ) );
    }
    item.setUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.APPROVALS_CLAIMS_LIST, paramMap ) );
    item.setNumberOfApprovables( numberOfApprovables );
    item.setNumberOfApproved( numberOfApproved ); // only for nominations
    view.getClaimApprovals().add( item );
  }

  private void addContestApprovals( ApprovalsIndexCollectionView view, List<SSIContestListValueBean> waitingForApprovalContests, String siteUrl )
  {
    SSIPromotion ssiPromotion = getSSIPromotionService().getLiveSSIPromotion();

    for ( SSIContestListValueBean valueBean : waitingForApprovalContests )
    {
      ApprovalsIndexCollectionItem item = new ApprovalsIndexCollectionItem( valueBean.getContestId(),
                                                                            getCMAssetService().getString( valueBean.getName(), SSIContest.CONTEST_CMASSET_NAME, UserManager.getLocale(), true ) );
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put( "contestId", valueBean.getContestId() );
      item.setType( SSI_APPROVAL );
      Date approveByDate = DateUtils.getDateAfterNumberOfDays( valueBean.getDateModified(), ssiPromotion.getDaysToApproveOnSubmission() );
      item.setAlertMessage( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.APPROVAL_MESG" ),
                                                  new Object[] { DateUtils.toDisplayString( approveByDate ) } ) );
      if ( SSIContestType.AWARD_THEM_NOW.equals( valueBean.getContestType() ) )
      {
        item.setUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.SSI_ISSUANCE_APPROVAL_SUMMARY, paramMap ) );
      }
      else
      {
        item.setUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.SSI_APPROVAL_SUMMARY, paramMap ) );
      }

      item.setNumberOfApprovables( waitingForApprovalContests.size() );
      view.getSsiApprovals().add( item );
    }
  }

  @SuppressWarnings( "unchecked" )
  public static List<AbstractRecognitionClaim> buildPaginatedApprovablesList( ApprovalsRecognitionListForm approvalsRecognitionListForm, boolean paginated )
  {
    boolean includeNodeAssociation = false;

    if ( approvalsRecognitionListForm.getMethod() != null && approvalsRecognitionListForm.getMethod().equalsIgnoreCase( "extractAsCsv" ) )
    {
      includeNodeAssociation = true;
    }
    long start = System.currentTimeMillis();
    AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
    claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
    if ( includeNodeAssociation )
    {
      claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENT_USER_NODES ) );
    }

    AssociationRequestCollection promotionAssociationRequestCollection = new AssociationRequestCollection();
    promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_OPTION ) );

    Long promotionId = StringUtils.isBlank( approvalsRecognitionListForm.getPromotionId() ) ? null : new Long( approvalsRecognitionListForm.getPromotionId() );

    Date startDate = DateUtils.toDate( approvalsRecognitionListForm.getStartDate() );
    Date endDate = DateUtils.toDate( approvalsRecognitionListForm.getEndDate() );

    List<PromotionApprovableValue> promotionClaimsValueList = getClaimService().getClaimsForApprovalByUser( UserManager.getUserId(),
                                                                                                            promotionId != null ? new Long[] { promotionId } : null,
                                                                                                            Boolean.TRUE,
                                                                                                            startDate,
                                                                                                            endDate,
                                                                                                            PromotionType.lookup( PromotionType.RECOGNITION ),
                                                                                                            claimAssociationRequestCollection,
                                                                                                            promotionAssociationRequestCollection,
                                                                                                            Boolean.FALSE,
                                                                                                            Boolean.FALSE );

    PropertyComparator.sort( promotionClaimsValueList, new MutableSortDefinition( "promotion.approvalEndDate", true, true ) );
    List<AbstractRecognitionClaim> approvables = extractApprovables( promotionClaimsValueList );
    logger.debug( "\n\n====== ApprovalsRecognitionListAction.buildPaginatedApprovablesList TIME: " + ( System.currentTimeMillis() - start ) );
    if ( paginated )
    {
      // paginate approval list
      ListPageInfo<AbstractRecognitionClaim> listPageInfo = new ListPageInfo<AbstractRecognitionClaim>( approvables,
                                                                                                        APPROVAL_LIST_PAGE_SIZE,
                                                                                                        approvalsRecognitionListForm.getRequestedPage() == null
                                                                                                            ? ListPageInfo.DEFAULT_INITIAL_PAGE
                                                                                                            : approvalsRecognitionListForm.getRequestedPage() );
      approvalsRecognitionListForm.setListPageInfo( listPageInfo );
      return listPageInfo.getCurrentPageList();
    }

    return approvables;
  }

  @SuppressWarnings( "unchecked" )
  private static List<AbstractRecognitionClaim> extractApprovables( List<PromotionApprovableValue> promotionApprovableValueList )
  {
    ArrayList<AbstractRecognitionClaim> approvables = new ArrayList<AbstractRecognitionClaim>();

    for ( PromotionApprovableValue promotionApprovableValue : promotionApprovableValueList )
    {
      approvables.addAll( promotionApprovableValue.getApprovables() );
    }

    return approvables;
  }

  protected MainContentService getMainContentService()
  {
    return (MainContentService)BeanLocator.getBean( MainContentService.BEAN_NAME );
  }

  protected ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private static NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  @SuppressWarnings( "unchecked" )
  private void checkForSaveOccurred( HttpServletRequest request ) throws InvalidClientStateException
  {
    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      request.setAttribute( "saveOccurred", clientStateMap.get( "saveOccurred" ) );
    }
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward extractAsCsv( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Long promotionId = null;
    AssociationRequestCollection arc = new AssociationRequestCollection();
    ApprovalsRecognitionListExportBean exportBean = new ApprovalsRecognitionListExportBean();
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );

    ActionMessages errors = new ActionMessages();

    if ( ( (ApprovalsRecognitionListForm)form ).getPromotionId() == null )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( CmsResourceBundle.getCmsBundle().getString( "recognition.approval.list.DOWNLOAD_MSG" ) ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    }

    if ( ! ( (ApprovalsRecognitionListForm)form ).getPromotionId().isEmpty() )
    {
      promotionId = Long.valueOf( ( (ApprovalsRecognitionListForm)form ).getPromotionId() );

    }

    // Client customization for WIP #43735 starts
    List<AbstractRecognitionClaim> claims = buildPaginatedApprovablesList( (ApprovalsRecognitionListForm)form, false );
    if ( claims != null )
    {
      for ( AbstractRecognitionClaim claim : claims )
      {
        if ( claim.getPromotion().getAdihCashOption() )
        {
          ClaimRecipient claimRecipient = claim.getClaimRecipients().iterator().next();
          claimRecipient.setDisplayUSDAwardQuantity( TcccClientUtils.convertToUSDString( getCokeClientService(), claimRecipient.getCustomCashAwardQuantity(), claimRecipient.getCashCurrencyCode() ) );
        }
      }
    }
    // Client customization for WIP #43735 ends
    exportBean.setExportList( buildPaginatedApprovablesList( (ApprovalsRecognitionListForm)form, false ) );
    exportBean.extractAsCsv( promotionId != null ? getPromotionService().getPromotionByIdWithAssociations( promotionId, arc ) : null, response );
    return null;
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward extractAsPdf( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    Long promotionId = null;
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );

    if ( ( (ApprovalsRecognitionListForm)form ).getPromotionId() == null )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( CmsResourceBundle.getCmsBundle().getString( "recognition.approval.list.DOWNLOAD_MSG" ) ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    }

    if ( ! ( (ApprovalsRecognitionListForm)form ).getPromotionId().isEmpty() )
    {
      promotionId = Long.valueOf( ( (ApprovalsRecognitionListForm)form ).getPromotionId() );
    }
    ApprovalsRecognitionListExportBean exportBean = new ApprovalsRecognitionListExportBean();
    exportBean.setExportPdfListRecognition( buildPaginatedApprovablesList( (ApprovalsRecognitionListForm)form, false ) );
    exportBean.setSubmissionStartDate( ( (ApprovalsRecognitionListForm)form ).getStartDate() );
    exportBean.setSubmissionEndDate( ( (ApprovalsRecognitionListForm)form ).getEndDate() );
    exportBean.setNodeService( getNodeService() );
    exportBean.extractAsPdf( promotionId != null ? getPromotionService().getPromotionByIdWithAssociations( promotionId, arc ) : null, response, "" );
    return null;
  }
  
  private CokeClientService getCokeClientService()
  {
    return (CokeClientService)getService( CokeClientService.BEAN_NAME );
  }
  
  private SSIContestService getSSIContestService()
  {
    return (SSIContestService)getService( SSIContestService.BEAN_NAME );
  }

  private SSIPromotionService getSSIPromotionService()
  {
    return (SSIPromotionService)getService( SSIPromotionService.BEAN_NAME );
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

  protected SSIContestPaxClaimService getSSIContestPaxClaimService()
  {
    return (SSIContestPaxClaimService)getService( SSIContestPaxClaimService.BEAN_NAME );
  }

  protected BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  }

}
