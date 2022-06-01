/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsNominationListForm.java,v $
 */

package com.biperf.core.ui.approvals;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.client.TccNomLevelPayout;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.process.NominationAutoNotificationProcess;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ListPageInfo;
import com.objectpartners.cms.util.ContentReaderManager;
import java.util.Collections;
import java.util.Comparator;

/**
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
 * <td></td>
 * <td></td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalsNominationListForm extends BaseForm
{
  private static final long serialVersionUID = 1L;

  private String method;
  private String promotionId;
  private String startDate = DateUtils.displayDateFormatMask;
  private String endDate = DateUtils.displayDateFormatMask;
  private boolean publicationDateActive;

  private String selectedCountry;

  private String filterApprovalStatusCode = ApprovalStatusType.PENDING;

  private Map<String, ApprovalsNominationForm> nominationApprovalFormByApprovableUid = new HashMap<String, ApprovalsNominationForm>();

  private ListPageInfo<Approvable> listPageInfo;
  private Long requestedPage;
  private boolean defaultDisplayPendingApprovals;
  // Client customization for WIP 58122
  private List levelPayouts;
  private int capPerPax;
  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Map<String, ApprovalsNominationForm> getNominationApprovalFormByApprovableUid()
  {
    return nominationApprovalFormByApprovableUid;
  }

  public void setNominationApprovalFormByApprovableUid( Map<String, ApprovalsNominationForm> nominationApprovalFormByApprovableUid )
  {
    this.nominationApprovalFormByApprovableUid = nominationApprovalFormByApprovableUid;
  }

  public void addNominationApprovalFormByApprovableUid( String guid, ApprovalsNominationForm nominationApprovalForm )
  {
    nominationApprovalFormByApprovableUid.put( guid, nominationApprovalForm );
  }

  public void load( List<Approvable> approvableList, NominationPromotion promotion )
  {
    // Add all claim products to map by id
    for ( Approvable approvable : approvableList )
    {
      // Only will ever be one element
      ApprovableItem approvableItem = (ApprovableItem)approvable.getApprovableItems().iterator().next();

      ApprovalStatusType approvalStatusType = approvableItem.getApprovalStatusType();

      // Don't include non-winners
      if ( !ApprovalStatusType.NON_WINNER.equals( approvalStatusType.getCode() ) )
      {
        ApprovalsNominationForm nominationApprovalForm = new ApprovalsNominationForm();

        nominationApprovalForm.load( approvable );

        if ( promotion != null )
        {
          if ( promotion.isPublicationDateActive() )
          {
            this.setPublicationDateActive( true );
            nominationApprovalForm.setNotificationDate( DateUtils.toDisplayString( promotion.getPublicationDate() ) );
          }
          else
          {
            this.setPublicationDateActive( false );
          }

        }
        if ( nominationApprovalForm.getNotificationDate() == null || nominationApprovalForm.getNotificationDate().length() == 0 )
        {
          nominationApprovalForm.setNotificationDate( null );
        }
        if ( nominationApprovalForm.getLevel() == null )
        {
          nominationApprovalForm.setLevel( null );
        }

        addNominationApprovalFormByApprovableUid( approvable.getApprovableUid(), nominationApprovalForm );
      }
    }
  }

  public void toDomainObjects( NominationPromotion promotion, List<Approvable> approvables, User approver )
  {
    for ( Approvable approvable : approvables )
    {
      String approvableUid = approvable.getApprovableUid();
      ApprovalsNominationForm nominationApprovalForm = (ApprovalsNominationForm)nominationApprovalFormByApprovableUid.get( approvableUid );

      if ( nominationApprovalForm == null )
      {
        throw new BeaconRuntimeException( "form approvableUid not found. approvableUid: " + approvableUid );
      }
      // Client customization for WIP #56492 starts
      ApprovableItem approvableItem = ( (ApprovableItem)approvable.getApprovableItems().iterator()
          .next() );
      approvableItem.setLevelSelect( nominationApprovalForm.getLevel() );
      // Client customization for WIP 58122
      if(promotion.isLevelPayoutByApproverAvailable()){
    	  if(nominationApprovalForm.getAwardQuantity()!=null && !nominationApprovalForm.getAwardQuantity().equals(""))
      approvableItem.setAwardQuantity( new Long(nominationApprovalForm.getAwardQuantity() ));
      }
      // Client customization for WIP #56492 ends
      nominationApprovalForm.toDomain( approvable, approver );
    }

  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );
    if ( promotionId.isEmpty() || promotionId.equals( "" ) )
    {
      actionErrors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "nomination.approval.list.PROMOTION_REQUIRED" ) );
    }
    // This is not the best way, but since the form is stored in session scope, properties are not
    // getting updated on promotion between promotions,
    // if you navigation to this page from other screens.(ex: status type gets shared between
    // promotions). In future we should move the move the form to request form.

    String showActivity = request.getParameter( "showActivity" );

    // if any pending approvals then default to pending
    if ( defaultDisplayPendingApprovals )
    {
      // if not coming via show activity, then default to pending status
      if ( StringUtils.isEmpty( showActivity ) )
      {
        filterApprovalStatusCode = ApprovalStatusType.PENDING;
      }
      else
      {
        if ( StringUtils.isEmpty( filterApprovalStatusCode ) )
        {
          actionErrors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "nomination.approval.list.STATUS_REQUIRED" ) );
        }
      }
    }
    // if no pending approvals and viewing past approvals then don't default to any status
    else
    {
      filterApprovalStatusCode = "";
      defaultDisplayPendingApprovals = Boolean.TRUE;
    }
    // Client customization for WIP 58122
    levelPayouts=getCokeClientService().getLevelTotalPoints( new Long( getPromotionId()));
    Collections.sort( levelPayouts, ASCE_COMPARATOR_LEVEL_PAYOUTS );
    //request.getSession().setAttribute( "levelPayouts", levelPayouts );
    request.setAttribute( "levelPayouts", levelPayouts );
    if(((NominationPromotion)getPromotionService().getPromotionById( new Long( getPromotionId() ) )).getCapPerPax()!=null)
    	capPerPax=((NominationPromotion)getPromotionService().getPromotionById( new Long( getPromotionId() ) )).getCapPerPax();
    else
    	capPerPax=0;
    if ( actionErrors.isEmpty() )
    {
      // Display validation
      if ( mapping.getPath().equals( "/approvalsNominationListMaintain" ) )
      {
        Date formStartDate = DateUtils.toDate( this.startDate );
        Date formEndDate = DateUtils.toDate( this.endDate );
        if ( formStartDate != null && formEndDate != null )
        {
          if ( formEndDate.before( formStartDate ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "nomination.approval.list.DATE_RANGE_INVALID" ) );
          }
        }
      }
      // Update validation
      else if ( mapping.getPath().equals( "/approvalsNominationListUpdate" ) )
      {
        NominationPromotion promotion = (NominationPromotion)getPromotionService().getPromotionById( new Long( getPromotionId() ) );

        for ( String nominationApprovalFormKey : nominationApprovalFormByApprovableUid.keySet() )
        {
          ApprovalsNominationForm nominationApprovalForm = nominationApprovalFormByApprovableUid.get( nominationApprovalFormKey );
          // Bug Fix # 27933
          if ( promotion.isAwardActive() && !promotion.isAwardAmountTypeFixed() && !promotion.isLevelPayoutByApproverAvailable())  //client customization
          {
            long awardAmountMin = promotion.getAwardAmountMin().longValue();
            long awardAmountMax = promotion.getAwardAmountMax().longValue();
            if ( ApprovalStatusType.WINNER.equals( nominationApprovalForm.getApprovalStatusType() ) )
            {
              String awardQuantityString = nominationApprovalForm.getAwardQuantity();
              int awardQuantity;
              try
              {
                awardQuantity = Integer.parseInt( awardQuantityString );
              }
              catch( NumberFormatException e )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "nomination.approval.list", "AWARD" ) ) );
                break;
              }

              if ( awardQuantity < awardAmountMin || awardQuantity > awardAmountMax )
              {
                actionErrors.add( "awardAmountTypeFixed", new ActionMessage( "nomination.approval.list.AMOUNT_RANGES_ERROR", promotion.getAwardAmountMin(), promotion.getAwardAmountMax() ) );
                break;
              }
            }
          }
          // Client customization for WIP 58122 starts
          if(promotion.isLevelPayoutByApproverAvailable() && ApprovalStatusType.WINNER.equals( nominationApprovalForm.getApprovalStatusType() )
          		  && nominationApprovalForm.getLevel()!=null && nominationApprovalForm.getLevel().equals(""))
            {
            	 actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "nomination.approval.list.LEVEL_REQUIRED" ) );
            }
          // Client customization for WIP 58122 ends

          if ( ApprovalStatusType.WINNER.equals( nominationApprovalForm.getApprovalStatusType() ) || ApprovalStatusType.NON_WINNER.equals( nominationApprovalForm.getApprovalStatusType() ) )
          {
            String notificationDateString = nominationApprovalForm.getNotificationDate();
            if ( !StringUtils.isBlank( notificationDateString ) )
            {
              Date notificationDate = null;
              // if entered, must be valid date
              try
              {
                notificationDate = DateUtils.toStartDate( notificationDateString );
              }
              catch( ParseException e )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "nomination.approval.list.NOTIFICATION_DATE_INVALID" ) );
                break;
              }

              // must not be in the past (when combined with notification process time)

              // Add Notification process time to the notification date in case the process
              // already ran today, which would put the time in the past.
              Date notificationDateWithTime = getNotificationDateWithTime( notificationDate );
              if ( notificationDateWithTime != null )
              {
                Date now = new Date();
                if ( now.after( notificationDateWithTime ) )
                {
                  actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "nomination.approval.list.NOTIFICATION_DATE_IN_PAST" ) );
                  break;
                }
              }
            }
            else
            {
              // if not entered, default it to null so it processes immediately
              nominationApprovalForm.setNotificationDate( null );
            }
          }

        }
      }
    }
    return actionErrors;
  }

  public static Date getNotificationDateWithTime( Date notificationDateWithoutTime )
  {
    Long timeOfDayMillis = getProcessService().getTimeOfDayMillisOfFirstSchedule( NominationAutoNotificationProcess.BEAN_NAME );
    if ( timeOfDayMillis == null )
    {
      return null;
    }

    Date notificationDateWithTime = new Date( notificationDateWithoutTime.getTime() + timeOfDayMillis.longValue() );
    return notificationDateWithTime;
  }
  // Client customization for WIP 58122
  private static CokeClientService getCokeClientService()
  {
    return (CokeClientService)BeanLocator.getBean( CokeClientService.BEAN_NAME );
  }

  public String getFilterApprovalStatusCode()
  {
    return filterApprovalStatusCode;
  }

  public void setFilterApprovalStatusCode( String filterApprovalStatusCode )
  {
    this.filterApprovalStatusCode = filterApprovalStatusCode;
  }

  private static PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

  public String getSelectedCountry()
  {
    return selectedCountry;
  }

  public void setSelectedCountry( String selectedCountry )
  {
    this.selectedCountry = selectedCountry;
  }

  private static ProcessService getProcessService()
  {
    return (ProcessService)BeanLocator.getBean( ProcessService.BEAN_NAME );
  }

  public boolean isPublicationDateActive()
  {
    return publicationDateActive;
  }

  public void setPublicationDateActive( boolean publicationDateActive )
  {
    this.publicationDateActive = publicationDateActive;
  }

  public void setListPageInfo( ListPageInfo<Approvable> listPageInfo )
  {
    this.listPageInfo = listPageInfo;
  }

  public ListPageInfo<Approvable> getListPageInfo()
  {
    return listPageInfo;
  }

  public void setRequestedPage( Long requestedPage )
  {
    this.requestedPage = requestedPage;
  }

  public Long getRequestedPage()
  {
    return requestedPage;
  }

  public boolean isDefaultDisplayPendingApprovals()
  {
    return defaultDisplayPendingApprovals;
  }

  public void setDefaultDisplayPendingApprovals( boolean defaultDisplayPendingApprovals )
  {
    this.defaultDisplayPendingApprovals = defaultDisplayPendingApprovals;
  }
  // Client customization for WIP 58122
	public List getLevelPayouts() {
		return levelPayouts;
	}
	public void setLevelPayouts(List levelPayouts) {
		this.levelPayouts = levelPayouts;
	}

	public int getCapPerPax() {
		return capPerPax;
	}

	public void setCapPerPax(int capPerPax) {
		this.capPerPax = capPerPax;
	}
	  public TccNomLevelPayout getLevel(int index) {
		    while (levelPayouts.size() <= index) {
		    	levelPayouts.add( new TccNomLevelPayout());
		    }    
		    return (TccNomLevelPayout)levelPayouts.get(index);
		  }
	  private static Comparator<TccNomLevelPayout> ASCE_COMPARATOR_LEVEL_PAYOUTS = new Comparator<TccNomLevelPayout>()
	  {
	    public int compare( TccNomLevelPayout c1, TccNomLevelPayout c2 )
	    {
	      return c1.getLevelDescription().compareTo( c2.getLevelDescription() );
	    }
	  };
}
