
package com.biperf.core.ui.approvals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.client.TccNomLevelPayout;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.cashcurrency.CashCurrencyService;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.approvals.NominationsApprovalPageTableDataView.NominationsApprovalPageTableData;
import com.biperf.core.ui.approvals.NominationsApprovalPageTableDataView.NominationsApprovalPageTableData.Result;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClaimApproveUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.approvals.NominationsApprovalResultBean;
import com.biperf.core.value.approvals.NominationsApprovalTabularDataBean;
import com.biperf.core.value.approvals.NominationsApprovalTeamAwardBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Form associated with {@link NominationsApprovalPageAction}
 * 
 * @author corneliu
 * @since Apr 22, 2016
 */
public class NominationsApprovalPageForm extends BaseForm
{
  private static final long serialVersionUID = 1L;

  private static final String FINAL_LEVEL = "finalLevel";

  private String dateStart;
  private String dateEnd;
  private Long timePeriodFilter;
  private int levelsFilter;
  private Long levelId;
  private String statusFilter;
  private Long promotionId;
  private String awardType;
  private Long levelNumber;
  private Long budgetIncrease;
  private Long paxId;
  private boolean isCumulativeNomination;
  private String claimIds;
  private Long claimGroupId;
  
  // Client customization for WIP 58122
  private List levelPayouts;
  private int capPerPax;
  private List levelPayoutsCopy;
  private boolean isCustomApprovalType;
  private boolean isCustomPayoutType;
  private Long award;
  private NominationsApprovalTabularDataBean tabularData = new NominationsApprovalTabularDataBean();
  private boolean isTeam;
  @Override
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );

    if ( tabularData.getResults() != null && !tabularData.getResults().isEmpty() )
    {
      tabularData.getResults().clear();
    }
  }

  @Override
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }
    Iterator<NominationsApprovalResultBean> results = tabularData.getResults().iterator();
    while ( results.hasNext() )
    {
      NominationsApprovalResultBean result = results.next();

      if ( result != null && ApprovalStatusType.MORE_INFO.equals( result.getStatus() ) && StringUtil.isNullOrEmpty( result.getMoreinfoMessage() ) )
      {
        errors.add( "moreinfoMessage",
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "nomination.approval.details.MORE_INFO_MESSAGE" ) ) );
      }
    }

    return errors;
  }

  public List<Approvable> toDomain( List<Approvable> approvables, User approver, NominationPromotion nominationPromotion ) throws UniqueConstraintViolationException
  {
    List<Approvable> nonPendingApprovables = new ArrayList<Approvable>();

    for ( Approvable approvable : approvables )
    {
      toDomainObjects( approvable, approver, nominationPromotion, nonPendingApprovables );
    }
    return nonPendingApprovables;
  }

  public void toDomainObjects( Approvable approvable, User approver, NominationPromotion promotion, List<Approvable> nonPendingApprovables ) throws UniqueConstraintViolationException
  {
    NominationClaim nominationClaim = null;
    boolean isTeam = false;

    if ( promotion.isCumulative() )
    {
      ClaimGroup claimGroup = (ClaimGroup)approvable;
      if ( claimGroup.getClaims().iterator().hasNext() )
      {
        nominationClaim = (NominationClaim)claimGroup.getClaims().iterator().next();
        isTeam = StringUtils.isNotEmpty( nominationClaim.getTeamName() );
      }
    }
    else
    {
      nominationClaim = (NominationClaim)approvable;
      isTeam = StringUtils.isNotEmpty( nominationClaim.getTeamName() );
    }

    Participant submitter = nominationClaim.getSubmitter();

    Iterator<NominationsApprovalResultBean> results = tabularData.getResults().iterator();
    while ( results.hasNext() )
    {
      NominationsApprovalResultBean result = results.next();

      if ( result != null )
      {
        String approvalStatusType = null;
        if ( approvable.isOpen() )
        {
          approvalStatusType = ApprovalStatusType.PENDING;
        }
        if ( result.getIdList() != null && result.getIdList().contains( nominationClaim.getId().toString() ) && approvalStatusType != null && !result.getStatus().equals( approvalStatusType ) )
        {
          nonPendingApprovables.add( approvable );
          Long awardQuantity = null;
          BigDecimal cashAwardQuantity = null;

          if( null == this.award  && null != result.getAward() && !"".equals(result.getAward()) ){
          	this.award = new Long(result.getAward() );        	  
          }
         
       
          for ( NominationPromotionLevel nominationPromotionLevel : promotion.getNominationLevels() )
          {
            if ( promotion.getPayoutLevel() != null && promotion.getPayoutLevel().equals( FINAL_LEVEL ) || this.levelId != null && nominationPromotionLevel.getLevelIndex().equals( this.levelId ) )
            {
              this.awardType = nominationPromotionLevel.getAwardPayoutType().getCode();
              if ( !result.getStatus().equalsIgnoreCase( ApprovalStatusType.NON_WINNER ) && !result.getStatus().equalsIgnoreCase( ApprovalStatusType.MORE_INFO ) )
              {
                if ( nominationPromotionLevel.isNominationAwardAmountTypeFixed() )
                {
                  if ( this.awardType != null && this.awardType.equals( PromotionAwardsType.POINTS ) )
                  {
                    awardQuantity = nominationPromotionLevel.getNominationAwardAmountFixed() != null ? nominationPromotionLevel.getNominationAwardAmountFixed().longValue() : 0;
                  }
                  else if ( this.awardType != null && this.awardType.equals( PromotionAwardsType.CASH ) )
                  {
                    cashAwardQuantity = nominationPromotionLevel.getNominationAwardAmountFixed();
                  }
                }
                else
                {
                  if ( !isTeam && this.awardType != null && this.awardType.equals( PromotionAwardsType.POINTS ) )
                  {
                    awardQuantity = result.getConvertedAward() != null ? result.getConvertedAward().longValue() : 0;
                  }
                  else if ( !isTeam && this.awardType != null && this.awardType.equals( PromotionAwardsType.CASH ) )
                  {
                    cashAwardQuantity = result.getConvertedAward();
                  }
                  else if ( this.awardType != null && this.awardType.equals( PromotionAwardsType.OTHER ) )
                  {
                    if ( nominationPromotionLevel.getQuantity() != null )
                    {
                      Long quantityAwarded = (long)nominationClaim.getClaimRecipients().size();
                      Long remainingQuanity = nominationPromotionLevel.getQuantity() - quantityAwarded;
                      nominationPromotionLevel.setQuantity( remainingQuanity );
                      getPromotionService().savePromotion( promotion );
                    }
                  }
                }
              }
              break;
            }
          }

          Set<ApprovableItem> approvableItems = approvable.getApprovableItems();
          /*// Client customization for WIP #56492 starts
          ApprovableItem approvableItem = ( (ApprovableItem)approvableItems.iterator().next() );
          approvableItem.setLevelSelect( result.getLevel() );
       // Client customization for WIP #58122. */
          String approverCurrencyCode = getParticipantService().getParticipantCurrencyCode( approver.getId() );

          User nominee = new User();

          for ( ApprovableItem approvableItem : approvableItems )
          {
            approvableItem.setLevelSelect( result.getLevel() );

            if (result.getTeam().isEmpty() || result.getTeam().size() < 0) {
        	
        		if (approvable instanceof NominationClaim) {
        			NominationClaim nomncinationClaim = (NominationClaim) approvable;      			
        			 List<NominationsApprovalTeamAwardBean> teamParticipantLst = new ArrayList<NominationsApprovalTeamAwardBean>();
        	 
        			for ( ProductClaimParticipant claimParticipant : nomncinationClaim.getTeamMembers() )
        	        {  
        				NominationsApprovalTeamAwardBean  nominationsApprovalTeamAwardBean = new NominationsApprovalTeamAwardBean();
        				nominationsApprovalTeamAwardBean.setPaxId(claimParticipant.getParticipant().getId());
        				nominationsApprovalTeamAwardBean.setValue(result.getAward());
        				teamParticipantLst.add(nominationsApprovalTeamAwardBean);
						if (getParticipantService().isOptedOut(claimParticipant.getParticipant().getId())) {
							claimParticipant.setAwardQuantity(null);
						} else {
							claimParticipant.setAwardQuantity(this.award);
						}
        	      } 
        			result.setTeam(teamParticipantLst);
        		}
            }
            if ( approvableItem instanceof ClaimGroup )
            {
              Long approvalRound = ( approvable.getApprovalRound() != null ? approvable.getApprovalRound() : 0 ) + 1;
              boolean isFinalLevel = promotion.getApprovalNodeLevels().intValue() == approvalRound.intValue();
              ClaimGroup claimGroup = (ClaimGroup)approvableItem;
              nominee = claimGroup.getParticipant();
              claimGroup.setApprovalRound( approvalRound );
              if ( !result.getStatus().equalsIgnoreCase( ApprovalStatusType.NON_WINNER ) && !result.getStatus().equalsIgnoreCase( ApprovalStatusType.MORE_INFO ) )
              {
                if (  this.awardType.equals( PromotionAwardsType.CASH ) )
                {
                 BigDecimal convertedAmount = getCashCurrencyService().convertCurrency( approverCurrencyCode, "USD", cashAwardQuantity, null );
                 cashAwardQuantity = convertedAmount;
                }
                claimGroup.setAwardQuantity( awardQuantity );
                claimGroup.setCashAwardQuantity( cashAwardQuantity );
              }
              claimGroup.setNotificationDate( DateUtils.toDate( result.getNotificationDate() ) );
              if ( result.getStatus().equals( ApprovalStatusType.WINNER ) && isFinalLevel )
              {
                claimGroup.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.WINNER ) );
              }
              else if ( result.getStatus().equals( ApprovalStatusType.NON_WINNER ) || result.getStatus().equals( ApprovalStatusType.MORE_INFO ) )
              {
                claimGroup.setApprovalStatusType( ApprovalStatusType.lookup( result.getStatus() ) );
              }
              if ( result.getStatus().equals( ApprovalStatusType.MORE_INFO ) )
              {
                claimGroup.setApproverComments( result.getMoreinfoMessage() );
              }
              claimGroup.setModalWindowViewed( false );
            }
            else if ( approvableItem instanceof ClaimRecipient )
            {
              boolean isFinalLevel = promotion.getApprovalNodeLevels().intValue() == approvable.getApprovalRound().intValue();
              ClaimRecipient claimRecipient = (ClaimRecipient)approvableItem;
              
              if ( isTeam && ( awardQuantity == null || awardQuantity != null && awardQuantity.longValue() == 0 || cashAwardQuantity == null
                  || cashAwardQuantity != null && cashAwardQuantity.compareTo( BigDecimal.ZERO ) == 0 ) )
              {
                if ( !result.getStatus().equalsIgnoreCase( ApprovalStatusType.NON_WINNER ) && !result.getStatus().equalsIgnoreCase( ApprovalStatusType.MORE_INFO ) )
                {
                  for ( NominationsApprovalTeamAwardBean nominationsApprovalTeamAwardBean : result.getTeam() )
                  {
                    if ( nominationsApprovalTeamAwardBean.getPaxId().equals( claimRecipient.getRecipient().getId() ) && this.awardType != null )
                    {
                      if ( this.awardType.equals( PromotionAwardsType.POINTS ) )
                      {
                        awardQuantity = nominationsApprovalTeamAwardBean.getConvertedTeamValue().longValue();
                      }
                      else if ( this.awardType.equals( PromotionAwardsType.CASH ) )
                      {
                        cashAwardQuantity = nominationsApprovalTeamAwardBean.getConvertedTeamValue();
                      }
                      break;
                    }
                  }
                }
              }
              nominee = claimRecipient.getRecipient();
              if ( !result.getStatus().equalsIgnoreCase( ApprovalStatusType.NON_WINNER ) && !result.getStatus().equalsIgnoreCase( ApprovalStatusType.MORE_INFO ) )
              {
                awardQuantity = awardQuantity != null ? awardQuantity : new Long( 0 );
                cashAwardQuantity = cashAwardQuantity != null ? cashAwardQuantity : new BigDecimal( 0 );
                if (  this.awardType.equals( PromotionAwardsType.CASH ) && !approverCurrencyCode.equals( "USD" ))
                {
                  BigDecimal amountToBeConverted = cashAwardQuantity;
                  BigDecimal convertedAmount = getCashCurrencyService().convertCurrency( approverCurrencyCode, "USD", amountToBeConverted, null );
                  cashAwardQuantity = convertedAmount;
                }
                claimRecipient.setAwardQuantity( awardQuantity );
                claimRecipient.setCashAwardQuantity( cashAwardQuantity );
              }
              claimRecipient.setNotificationDate( DateUtils.toDate( result.getNotificationDate() ) );

              if ( result.getStatus().equals( ApprovalStatusType.WINNER ) && isFinalLevel )
              {
                claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.WINNER ) );
              }
              else if ( result.getStatus().equals( ApprovalStatusType.NON_WINNER ) || result.getStatus().equals( ApprovalStatusType.MORE_INFO ) )
              {
                claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( result.getStatus() ) );
              }
              claimRecipient.setModalWindowViewed( false );
            }
            else
            {
              throw new BeaconRuntimeException( "unknown approvableItem type: " + approvableItem.getClass().getName() );
            }

            String approvalStatusTypeCode = result.getStatus();

            if ( approvalStatusTypeCode.equals( ApprovalStatusType.MORE_INFO ) )
            {
              Message message = getMessageService().getMessageByCMAssetCode( MessageService.NOMINATOR_REQUEST_MORE_INFO );
              for ( Iterator notificationIter = promotion.getPromotionNotifications().iterator(); notificationIter.hasNext(); )
              {
                PromotionNotification notification = (PromotionNotification)notificationIter.next();
                if ( message.getId().equals( notification.getNotificationMessageId() ) && notification.getNotificationMessageId() != -1 )
                {
                  getMailingService().buildNominatorRequestForMoreInfo( approvable, approver, message, nominee, result.getMoreinfoMessage() );
                  break;
                }
                else if ( !message.getId().equals( notification.getNotificationMessageId() ) && notification.getNotificationMessageId() != -1 )
                {
                  Message customMessage = getMessageService().getMessageById( notification.getNotificationMessageId() );
                  if ( message.getMessageTypeCode().getCode().equals( customMessage.getMessageTypeCode().getCode() ) )
                  {
                    getMailingService().buildNominatorRequestForMoreInfo( approvable, approver, customMessage, nominee, result.getMoreinfoMessage() );
                    break;
                  }
                }
              }
            }

            if ( approvalStatusTypeCode.equals( ApprovalStatusType.NON_WINNER ) )
            {
              Message nominatorMessage = getMessageService().getMessageByCMAssetCode( MessageService.NOMINATOR_NON_WINNER );

              for ( Iterator notificationIter = promotion.getPromotionNotifications().iterator(); notificationIter.hasNext(); )
              {
                PromotionNotification notification = (PromotionNotification)notificationIter.next();
                if ( nominatorMessage.getId().equals( notification.getNotificationMessageId() ) && notification.getNotificationMessageId() != -1 )
                {
                  getMailingService().buildNominatorNonWinnerNotification( approvable, nominatorMessage, result.getDeinalReason(), nominee, this.levelId != null ? String.valueOf( levelId ) : "0" );
                  break;
                }
              }
            }

            String approverComment = null;

            if ( approvalStatusTypeCode.equals( ApprovalStatusType.MORE_INFO ) )
            {
              approverComment = result.getMoreinfoMessage();
            }
            else if ( approvalStatusTypeCode.equals( ApprovalStatusType.NON_WINNER ) )
            {
              approvable.setApproverComments( result.getDeinalReason() );
            }
            else if ( approvalStatusTypeCode.equals( ApprovalStatusType.WINNER ) || approvalStatusTypeCode.equals( ApprovalStatusType.APPROVED ) )
            {
              approverComment = result.getWinnerMessage();
            }

            ClaimApproveUtils.setNominationClaimItemApproverDetails( approver,
                                                                     approvalStatusTypeCode,
                                                                     approverComment,
                                                                     approvableItem,
                                                                     DateUtils.toDate( result.getNotificationDate() ),
                                                                     result.getTimePeriod() != null ? Long.parseLong( result.getTimePeriod() ) : null );
          }
        }
      }
    }
  }

  public String getDateStart()
  {
    return dateStart;
  }

  public void setDateStart( String dateStart )
  {
    this.dateStart = dateStart;
  }

  public String getDateEnd()
  {
    return dateEnd;
  }

  public void setDateEnd( String dateEnd )
  {
    this.dateEnd = dateEnd;
  }

  public Long getTimePeriodFilter()
  {
    return timePeriodFilter;
  }

  public void setTimePeriodFilter( Long timePeriodFilter )
  {
    this.timePeriodFilter = timePeriodFilter;
  }

  public String getStatusFilter()
  {
    return statusFilter;
  }

  public void setStatusFilter( String statusFilter )
  {
    this.statusFilter = statusFilter;
  }

  public NominationsApprovalTabularDataBean getTabularData()
  {
    return tabularData;
  }

  public void setTabularData( NominationsApprovalTabularDataBean tabularData )
  {
    this.tabularData = tabularData;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public int getLevelsFilter()
  {
    return levelsFilter;
  }

  public void setLevelsFilter( int levelsFilter )
  {
    this.levelsFilter = levelsFilter;
  }

  public Long getLevelId()
  {
    return levelId;
  }

  public void setLevelId( Long levelId )
  {
    this.levelId = levelId;
  }

  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  public Long getLevelNumber()
  {
    return levelNumber;
  }

  public void setLevelNumber( Long levelNumber )
  {
    this.levelNumber = levelNumber;
  }

  public Long getBudgetIncrease()
  {
    return budgetIncrease;
  }

  public void setBudgetIncrease( Long budgetIncrease )
  {
    this.budgetIncrease = budgetIncrease;
  }

  public Long getPaxId()
  {
    return paxId;
  }

  public void setPaxId( Long paxId )
  {
    this.paxId = paxId;
  }

  public boolean isCumulativeNomination()
  {
    return isCumulativeNomination;
  }

  public void setCumulativeNomination( boolean isCumulativeNomination )
  {
    this.isCumulativeNomination = isCumulativeNomination;
  }

  public String getClaimIds()
  {
    if ( this.tabularData != null )
    {
      for ( NominationsApprovalResultBean nominationsApprovalResultBean : this.tabularData.getResults() )
      {
        if ( nominationsApprovalResultBean != null && StringUtils.isNotEmpty( nominationsApprovalResultBean.getId() ) && nominationsApprovalResultBean.getStatus() != null
            && !nominationsApprovalResultBean.getStatus().equals( ApprovalStatusType.PENDING ) )
        {
          if ( StringUtils.isNotEmpty( this.claimIds ) )
          {
            this.claimIds = this.claimIds + "," + nominationsApprovalResultBean.getId();
          }
          else
          {
            this.claimIds = nominationsApprovalResultBean.getId();
          }
        }
      }
    }
    return this.claimIds;
  }

  public void setClaimIds( String claimIds )
  {
    this.claimIds = claimIds;
  }

  public Long getClaimGroupId()
  {
    return claimGroupId;
  }

  public void setClaimGroupId( Long claimGroupId )
  {
    this.claimGroupId = claimGroupId;
  }

  public String getTabularClaimIds( NominationsApprovalPageTableData nominationsApprovalPageTableData )
  {
    if ( nominationsApprovalPageTableData != null && CollectionUtils.isNotEmpty( nominationsApprovalPageTableData.getResults() ) )
    {
      for ( Result result : nominationsApprovalPageTableData.getResults() )
      {
        if ( result != null && StringUtils.isNotEmpty( result.getClaimId() ) )
        {
          if ( StringUtils.isNotEmpty( this.claimIds ) )
          {
            this.claimIds = this.claimIds + "," + result.getClaimId();
          }
          else
          {
            this.claimIds = result.getClaimId();
          }
        }
      }
    }
    return this.claimIds;
  }
  
//Client customization for WIP 58122
  public List getLevelPayouts() {
      return levelPayouts;
  }
  public void setLevelPayouts(List levelPayouts) {
      this.levelPayouts = levelPayouts;
  }
  public List getLevelPayoutsCopy() {
      return levelPayoutsCopy;
  }
  public void setLevelPayoutsCopy(List levelPayoutsCopy) {
      this.levelPayoutsCopy = levelPayoutsCopy;
  }
  public int getCapPerPax() {
      return capPerPax;
  }
  // Client customization for WIP #58122
  public void setCapPerPax(int capPerPax) {
      this.capPerPax = capPerPax;
  }
  
public TccNomLevelPayout getLevel(int index) {
      while (levelPayouts.size() <= index) {
          levelPayouts.add( new TccNomLevelPayout());
      }    
      return (TccNomLevelPayout)levelPayouts.get(index);
    }

/**
 * @return the isCustomApprovalType
 */
public boolean isCustomApprovalType() {
	return isCustomApprovalType;
}

/**
 * @param isCustomApprovalType the isCustomApprovalType to set
 */
public void setCustomApprovalType(boolean isCustomApprovalType) {
	this.isCustomApprovalType = isCustomApprovalType;
}

/**
 * @return the isCustomPayoutType
 */
public boolean isCustomPayoutType() {
	return isCustomPayoutType;
}

/**
 * @return the award
 */
public Long getAward() {
	return award;
}

/**
 * @param award the award to set
 */
public void setAward(Long award) {
	this.award = award;
}

/**
 * @param isCustomPayoutType the isCustomPayoutType to set
 */
public void setCustomPayoutType(boolean isCustomPayoutType) {
	this.isCustomPayoutType = isCustomPayoutType;
}

	/**
 * @return the isTeam
 */
public boolean isTeam() {
	return isTeam;
}

/**
 * @param isTeam the isTeam to set
 */
public void setTeam(boolean isTeam) {
	this.isTeam = isTeam;
}

	public TccNomLevelPayout getLevelCopy(int index) {
        if(levelPayouts==null)
        {
            TccNomLevelPayout tc=new TccNomLevelPayout();
            tc.setPromotion(null);
            tc.setLevelDescription("_");
            tc.setTotalPoints(new Long(0));
            return tc;
           // return getCokeClientService().getLevelTotalPoints( new Long( 2430)).get(index);
        }
        else
        {
          while ( levelPayouts.size() <= index) {
              levelPayouts.add( new TccNomLevelPayout());
          }    
          return (TccNomLevelPayout)levelPayouts.get(index);
        }
        }
   

	private static Comparator<TccNomLevelPayout> ASCE_COMPARATOR_LEVEL_PAYOUTS = new Comparator<TccNomLevelPayout>()
    {
      public int compare( TccNomLevelPayout c1, TccNomLevelPayout c2 )
      {
        return c1.getLevelDescription().compareTo( c2.getLevelDescription() );
      }
    };
    
    // Client customization for WIP 58122
    private static CokeClientService getCokeClientService()
    {
      return (CokeClientService)BeanLocator.getBean( CokeClientService.BEAN_NAME );
    }

  private CashCurrencyService getCashCurrencyService()
  {
    return (CashCurrencyService)BeanLocator.getBean( CashCurrencyService.BEAN_NAME );
  }

  private MailingService getMailingService()
  {
    return (MailingService)BeanLocator.getBean( MailingService.BEAN_NAME );
  }

  private MessageService getMessageService()
  {
    return (MessageService)BeanLocator.getBean( MessageService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

}
