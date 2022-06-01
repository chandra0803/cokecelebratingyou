/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.claim;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.user.User;

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
 * <td>wadzinsk</td>
 * <td>Oct 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class ApprovableItem extends BaseDomain
{
  private Set approvableItemApprovers = new LinkedHashSet();
  private Timestamp dateApproved;
  private String serialId;
  private ApprovalStatusType approvalStatusType;
  private PromotionApprovalOptionReasonType promotionApprovalOptionReasonType;
  private String levelSelect;// Client customization for WIP #56492 
  private Long awardQuantity;  // Client customization for WIP 58122

  public abstract Approvable getApprovable();

  public User getCurrentApproverUser()
  {
    ApprovableItemApprover claimItemApprover = getCurrentClaimItemApprover();
    if ( claimItemApprover == null )
    {
      return null;
    }

    return claimItemApprover.getApproverUser();

  }

  public ApprovableItemApprover getCurrentClaimItemApprover()
  {
    if ( approvableItemApprovers.isEmpty() || getApprovable().getApprovalRound() == null )
    {
      return null;
    }

    ApprovableItemApprover matchingApprovableItemApprover = null;
    if ( getApprovable().isOpen() )
    {
      Long approvalRound = getApprovable().getApprovalRound();
      for ( Iterator iter = approvableItemApprovers.iterator(); iter.hasNext(); )
      {
        ApprovableItemApprover approvableItemApprover = (ApprovableItemApprover)iter.next();
        if ( approvalRound.equals( approvableItemApprover.getApprovalRound() ) )
        {
          matchingApprovableItemApprover = approvableItemApprover;
          break;
        }
      }
    }
    else
    {
      for ( Iterator iter = approvableItemApprovers.iterator(); iter.hasNext(); )
      {
        ApprovableItemApprover approvableItemApprover = (ApprovableItemApprover)iter.next();
        if ( matchingApprovableItemApprover == null )
        {
          matchingApprovableItemApprover = approvableItemApprover;
        }
        else
        {
          if ( approvableItemApprover.getApprovalRound().compareTo( matchingApprovableItemApprover.getApprovalRound() ) > 0 )
          {
            matchingApprovableItemApprover = approvableItemApprover;
          }
        }
      }
    }

    return matchingApprovableItemApprover;

  }

  /**
   * @return value of approvalStatusType property
   */
  public ApprovalStatusType getApprovalStatusType()
  {
    return approvalStatusType;
  }

  /**
   * @param approvalStatusType value for approvalStatusType property
   */
  public void setApprovalStatusType( ApprovalStatusType approvalStatusType )
  {
    this.approvalStatusType = approvalStatusType;
  }

  /**
   * @return value of promotionApprovalOptionReasonType property
   */
  public PromotionApprovalOptionReasonType getPromotionApprovalOptionReasonType()
  {
    return promotionApprovalOptionReasonType;
  }

  /**
   * @param promotionApprovalOptionReasonType value for promotionApprovalOptionReasonType property
   */
  public void setPromotionApprovalOptionReasonType( PromotionApprovalOptionReasonType promotionApprovalOptionReasonType )
  {
    this.promotionApprovalOptionReasonType = promotionApprovalOptionReasonType;
  }

  /**
   * @return value of serialId property
   */
  public String getSerialId()
  {
    return serialId;
  }

  /**
   * @param serialId value for serialId property
   */
  public void setSerialId( String serialId )
  {
    this.serialId = serialId;
  }

  public Timestamp getDateApproved()
  {
    return dateApproved;
  }

  public void setDateApproved( Timestamp dateApproved )
  {
    this.dateApproved = dateApproved;
  }

  /**
   * @return value of approvableItemApprovers property
   */
  public Set getApprovableItemApprovers()
  {
    return approvableItemApprovers;
  }

  /**
   * @param approvableItemApprovers value for approvableItemApprovers property
   */
  public void setApprovableItemApprovers( Set approvableItemApprovers )
  {
    this.approvableItemApprovers = approvableItemApprovers;
  }

  public abstract void addApprover( User approverUser,
                                    Date dateApproved,
                                    ApprovalStatusType approvalStatusType,
                                    String approverComment,
                                    PromotionApprovalOptionReasonType promotionApprovalOptionReasonType,
                                    Date notificationDate,
                                    Long timePeriodId );

  protected void addApprover( User approverUser,
                              Date dateApproved,
                              ApprovalStatusType approvalStatusType,
                              String approverComment,
                              PromotionApprovalOptionReasonType promotionApprovalOptionReasonType,
                              ApprovableItemApprover approvableItemApprover,
                              Date notificationDate,
                              Long timePeriodId )
  {
    approvableItemApprover.setDateApproved( dateApproved );
    approvableItemApprover.setApproverUser( approverUser );
    approvableItemApprover.setApprovalStatusType( approvalStatusType );
    approvableItemApprover.setPromotionApprovalOptionReasonType( promotionApprovalOptionReasonType );
    approvableItemApprover.setNotificationDate( notificationDate );
    approvableItemApprover.setTimePeriodId( timePeriodId );
    approvableItemApprover.setApproverComments( approverComment );

    int claimItemApproversCount = getApprovableItemApprovers().size();
    approvableItemApprover.setApprovalRound( new Long( claimItemApproversCount + 1 ) );
    getApprovableItemApprovers().add( approvableItemApprover );
  }
  
  //Client customization for WIP #56492 starts
  public String getLevelSelect()
  {
    return levelSelect;
  }

  public void setLevelSelect( String levelSelect )
  {
    this.levelSelect = levelSelect;
  }
  //Client customization for WIP #56492 ends
  // Client customization for WIP 58122
public Long getAwardQuantity() {
	return awardQuantity;
}

public void setAwardQuantity(Long awardQuantity) {
	this.awardQuantity = awardQuantity;
}

}
