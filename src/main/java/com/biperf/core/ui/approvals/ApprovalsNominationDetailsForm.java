/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsNominationDetailsForm.java,v $
 */

package com.biperf.core.ui.approvals;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.ContentReaderManager;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.biperf.core.domain.client.TccNomLevelPayout;
import com.biperf.core.service.client.CokeClientService;

/**
 * RecognitionApprovalDetailsForm.
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
public class ApprovalsNominationDetailsForm extends ApprovalsNominationForm
{
  private static final long serialVersionUID = 1L;

  private String method;
  private String approvableId;
  private String promotionId;
  private String approvableTypeCode;
  private String viewApprovalStatusCode;
  private String claimGroupPaxId;
  private String claimGroupNodeId;
  private String claimGroupStartDate;
  private String claimGroupEndDate;
  private boolean publicationDateActive;
  // Client customization for WIP 58122
  private List levelPayouts;
  private int capPerPax;
  private List levelPayoutsCopy;

  public boolean isPublicationDateActive()
  {
    return publicationDateActive;
  }

  public void setPublicationDateActive( boolean publicationDateActive )
  {
    this.publicationDateActive = publicationDateActive;
  }

  /**
   * @return value of method property
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * @param method value for method property
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getApprovableId()
  {
    return approvableId;
  }

  public void setApprovableId( String approvableId )
  {
    this.approvableId = approvableId;
  }

  public String getApprovableTypeCode()
  {
    return approvableTypeCode;
  }

  public void setApprovableTypeCode( String approvableTypeCode )
  {
    this.approvableTypeCode = approvableTypeCode;
  }

  public String getViewApprovalStatusCode()
  {
    return viewApprovalStatusCode;
  }

  public void setViewApprovalStatusCode( String viewApprovalStatusCode )
  {
    this.viewApprovalStatusCode = viewApprovalStatusCode;
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );
    if ( actionErrors.isEmpty() )
    {
      NominationPromotion promotion = (NominationPromotion)getPromotionService().getPromotionById( new Long( getPromotionId() ) );
      if(promotion.isLevelPayoutByApproverAvailable() && ApprovalStatusType.WINNER.equals( getApprovalStatusType() )
    		  && this.getLevel()!=null &&  this.getLevel().equals(""))
      {
    	  actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "nomination.approval.list.LEVEL_REQUIRED" ) ); 
      }
      if ( !promotion.isAwardAmountTypeFixed() && !promotion.isLevelPayoutByApproverAvailable())  //client customization
      {
        long awardAmountMin = promotion.getAwardAmountMin().longValue();
        long awardAmountMax = promotion.getAwardAmountMax().longValue();

        if ( ApprovalStatusType.WINNER.equals( getApprovalStatusType() ) )
        {
          String awardQuantityString = getAwardQuantity();
          int awardQuantity;
          try
          {
            awardQuantity = Integer.parseInt( awardQuantityString );
            if ( awardQuantity < awardAmountMin || awardQuantity > awardAmountMax )
            {
              actionErrors.add( "awardAmountTypeFixed", new ActionMessage( "nomination.approval.list.AMOUNT_RANGES_ERROR", promotion.getAwardAmountMin(), promotion.getAwardAmountMax() ) );
            }
          }
          catch( NumberFormatException e )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "nomination.approval.list", "AWARD" ) ) );
          }
        }
      }
    }

    if ( ApprovalStatusType.WINNER.equals( getApprovalStatusType() ) || ApprovalStatusType.NON_WINNER.equals( getApprovalStatusType() ) )
    {
      String notificationDateString = getNotificationDate();
      if ( !StringUtils.isBlank( notificationDateString ) )
      {
        try
        {
          Date notificationDate = DateUtils.toStartDate( notificationDateString );

          // must not be in the past (when combined with notification process time)

          // Add Notification process time to the notification date in case the process
          // already ran today, which would put the time in the past.
          Date notificationDateWithTime = ApprovalsNominationListForm.getNotificationDateWithTime( notificationDate );
          Date now = new Date();

          if ( notificationDateWithTime != null && now.after( notificationDateWithTime ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "nomination.approval.details.NOTIFICATION_DATE_IN_PAST" ) );
          }

        }
        catch( ParseException e )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "nomination.approval.details.NOTIFICATION_DATE_INVALID" ) );
        }
      }
    }
    else
    {
      // if not entered, default it to null so it processes immediately
      setNotificationDate( null );
    }
    // Client customization for WIP 58122
    levelPayouts=getCokeClientService().getLevelTotalPoints( new Long( getPromotionId()));
    Collections.sort( levelPayouts, ASCE_COMPARATOR_LEVEL_PAYOUTS );
    int i=1;
    for ( Iterator iter = levelPayouts.iterator(); iter.hasNext(); )
    {
    	TccNomLevelPayout t = (TccNomLevelPayout)iter.next();
    	t.setLevelId("level"+i);
    	i++;
    }
    request.setAttribute( "levelPayouts", levelPayouts );
    if(((NominationPromotion)getPromotionService().getPromotionById( new Long( getPromotionId() ) )).getCapPerPax()!=null)
    	capPerPax=((NominationPromotion)getPromotionService().getPromotionById( new Long( getPromotionId() ) )).getCapPerPax();
    else
    	capPerPax=0;
    return actionErrors;
  }

  private static PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getClaimGroupEndDate()
  {
    return claimGroupEndDate;
  }

  public void setClaimGroupEndDate( String claimGroupEndDate )
  {
    this.claimGroupEndDate = claimGroupEndDate;
  }

  public String getClaimGroupNodeId()
  {
    return claimGroupNodeId;
  }

  public void setClaimGroupNodeId( String claimGroupNodeId )
  {
    this.claimGroupNodeId = claimGroupNodeId;
  }

  public String getClaimGroupPaxId()
  {
    return claimGroupPaxId;
  }

  public void setClaimGroupPaxId( String claimGroupPaxId )
  {
    this.claimGroupPaxId = claimGroupPaxId;
  }

  public String getClaimGroupStartDate()
  {
    return claimGroupStartDate;
  }

  public void setClaimGroupStartDate( String claimGroupStartDate )
  {
    this.claimGroupStartDate = claimGroupStartDate;
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

}
