/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/claim/Claim.java,v $
 */

package com.biperf.core.domain.claim;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ApprovableTypeEnum;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.utils.ClaimApproveUtils;
import com.biperf.core.utils.DateUtils;

/**
 * Claim.
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
 * <td>crosenquest</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public abstract class Claim extends BaseDomain implements Approvable
{
  private Participant submitter;
  private Promotion promotion;
  private String claimNumber;
  private List<ClaimElement> claimElements = new ArrayList<ClaimElement>();
  private boolean open;
  private Node node;
  private Node lastApprovalNode;
  private String approverComments = "";
  private String adminComments = "";
  private Date submissionDate;
  private User proxyUser;
  private Long approvalRound;
  private String timeZoneID = "";
  private boolean addPointsClaim;

  // This is a derrived field, not stored in the db
  private Long earnings = new Long( 0 ); // default this to 0 . good idea? bad idea?

  /**
   * If claims are approved cumulatively, then this property refers to the claim group to which
   * this claim belongs.  If claims are approved individually, then this property is null.
   */
  private ClaimGroup claimGroup;
 
  //transient variable to separate file load deposit
  private transient boolean isFileLoadDeposit = false; //bug 73458
  
  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  /**
   * Empty constructor
   */
  public Claim()
  {
    super();
  }

  /**
   * constructor that takes the claimFormStep that this claim is based on
   *
   * @param claimFormStep
   */
  public Claim( ClaimFormStep claimFormStep )
  {
    this();
    for ( int i = 0; i < claimFormStep.getClaimFormStepElements().size(); i++ )
    {
      ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)claimFormStep.getClaimFormStepElements().get( i );
      ClaimElement element = new ClaimElement();
      element.setClaimFormStepElement( claimFormStepElement );
      addClaimElement( element );
    }
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public User getProxyUser()
  {
    return proxyUser;
  }

  public void setProxyUser( User proxyUser )
  {
    this.proxyUser = proxyUser;
  }

  public List<ClaimElement> getClaimElements()
  {
    return this.claimElements;
  }

  public void setClaimElements( List<ClaimElement> claimElements )
  {
    this.claimElements = claimElements;
  }

  public ClaimElement getClaimElement( int index )
  {
    return (ClaimElement)this.claimElements.get( index );
  }

  public void addClaimElement( ClaimElement element )
  {
    element.setClaim( this );
    claimElements.add( element );
  }

  /**
   * Overridden from @see com.biperf.core.domain.claim.Approvable#setOpen(boolean)
   * @param open
   */
  public void setOpen( boolean open )
  {
    this.open = open;
  }

  /**
   * Returns true if this claim has not been processed; returns false otherwise.
   *
   * @return true if this claim has not been processed; returns false otherwise.
   */
  public boolean getOpen()
  {
    return this.open;
  }

  /**
   * Overridden from @see com.biperf.core.domain.claim.Approvable#isOpen()
   */
  public boolean isOpen()
  {
    return this.open;
  }

  public void setSubmitter( Participant submitter )
  {
    this.submitter = submitter;
  }

  public Participant getSubmitter()
  {
    return this.submitter;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public Promotion getPromotion()
  {
    return this.promotion;
  }

  public String getClaimNumber()
  {
    return this.claimNumber;
  }

  public void setClaimNumber( String claimNumber )
  {
    this.claimNumber = claimNumber;
  }

  public Node getNode()
  {
    return node;
  }

  public void setNode( Node node )
  {
    this.node = node;
  }

  /**
   * Returns true if this claim can be deleted; otherwise it returns false.
   * <p>
   * A claim is deletable if it is open. See ProductClaim override for additional rules.
   * </p>
   *
   * @return true if this claim can be deleted; otherwise it returns false.
   */
  public boolean isDeletable()
  {

    boolean isDeletable = true;

    if ( !open )
    {
      isDeletable = false;
    }

    return isDeletable;
  }

  /**
   * Returns the amount paid out on this claim.
   *
   * @return the amount paid out on this claim.
   */
  public Long getEarnings()
  {
    return this.earnings;
  }

  /**
   * Sets the amount paid out on this claim.
   *
   * @param earnings the amount paid out on this claim.
   */
  public void setEarnings( Long earnings )
  {
    this.earnings = earnings;
  }

  /**
   * Overridden from @see com.biperf.core.domain.claim.Approvable#getApproverComments()
   */
  public String getApproverComments()
  {
    return this.approverComments;
  }

  /**
   * Overridden from @see com.biperf.core.domain.claim.Approvable#setApproverComments(java.lang.String)
   * @param approverComments
   */
  public void setApproverComments( String approverComments )
  {
    this.approverComments = approverComments;
  }

  public String getAdminComments()
  {
    return this.adminComments;
  }

  public void setAdminComments( String adminComments )
  {
    this.adminComments = adminComments;
  }

  /**
   * @return value of submissionDate property
   */
  public Date getSubmissionDate()
  {
    return submissionDate;
  }

  /**
   * @param submissionDate value for submissionDate property
   */
  public void setSubmissionDate( Date submissionDate )
  {
    this.submissionDate = submissionDate;
  }

  public String getRelativeSubmissionDate()
  {
    return DateUtils.toRelativeTimeLapsed( submissionDate );
  }
  // Client customizations for WIP #43735 starts
  public String getDisplaySubmissionDate()
  {
    return DateUtils.toDisplayString( submissionDate );
  }
  // Client customizations for WIP #43735 ends

  /**
   * Returns the name of the company to which the participant sold the products associated with this
   * claim.
   *
   * @return the name of the company to which the participant sold the products associated with this
   *         claim.
   */
  public String getCompanyName()
  {

    String companyName = "";

    Iterator iter = claimElements.iterator();
    while ( iter.hasNext() )
    {
      ClaimElement claimElement = (ClaimElement)iter.next();
      Long customerInformationBlockId = claimElement.getClaimFormStepElement().getCustomerInformationBlockId();
      if ( customerInformationBlockId != null && customerInformationBlockId.equals( CustomerInformationBlock.COMPANY_NAME_CFSE_ID ) )
      {
        companyName = claimElement.getValue();
        break;
      }
    }

    return companyName;
  }

  public int getNodeLevelsRemaining()
  {
    return ClaimApproveUtils.getNodeLevelsRemaining( getApprovableItemApproversSize(), promotion, approvalRound );
  }

  public int getApprovableItemApproversSize()
  {
    int approvableItemApproversSize = 0;
    Set<ApprovableItem> approvableItems = getApprovableItems();

    for ( ApprovableItem approvableItem : approvableItems )
    {
      if ( approvableItem instanceof ClaimRecipient )
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)approvableItem;

        Set<ClaimItemApprover> claimItemApprovers = claimRecipient.getApprovableItemApprovers();
        for ( ClaimItemApprover claimItemApprover : claimItemApprovers )
        {
          if ( approvableItemApproversSize < claimItemApprover.getApprovalRound() )
          {
            approvableItemApproversSize = claimItemApprover.getApprovalRound().intValue();
          }
        }
      }
      // Fix for bug #67228
      else if ( approvableItem instanceof ClaimGroup )
      {
        ClaimGroup claimGroup = (ClaimGroup)approvableItem;
        Set<ClaimGroupApprover> claimGroupApprovers = claimGroup.getApprovableItemApprovers();
        for ( ClaimGroupApprover claimGroupApprover : claimGroupApprovers )
        {
          if ( approvableItemApproversSize < claimGroupApprover.getApprovalRound() )
          {
            approvableItemApproversSize = claimGroupApprover.getApprovalRound().intValue();
          }
        }
      }
      else if ( approvableItem.getApprovable() instanceof ProductClaim )
      {
        approvableItemApproversSize = approvableItemApproversSize + approvableItem.getApprovableItemApprovers().size();
      }
    }
    return approvableItemApproversSize;
  }

  /**
   * Get the node associated with the claim's submitter.
   *
   * @return Node
   */
  public Node getSubmittersNode()
  {
    Node approvalNode = null;
    if ( getNode() == null )
    {
      // The claim node is null. The submitter must be associated with only
      // one node. Use that node.
      Set userNodeSet = getSubmitter().getUserNodes();
      if ( userNodeSet != null )
      {
        Iterator iter = userNodeSet.iterator();
        if ( iter.hasNext() )
        {
          UserNode userNode = (UserNode)iter.next();
          approvalNode = userNode.getNode();
        }
      }
    }
    else
    {
      // The claim node is not null. The submitter must be associated with
      // more than one node. Use the node assocated with the claim.
      approvalNode = getNode();
    }
    return approvalNode;
  }

  public String getSubmitterDisplayCountryName()
  {
    Country country = getSubmitterCountry();
    if ( null != country )
    {
      return country.getI18nCountryName();
    }
    return null;
  }

  public String getSubmitterDisplayCountryCode()
  {
    Country country = getSubmitterCountry();
    if ( null != country )
    {
      return country.getCountryCode();
    }
    return null;
  }

  public Country getSubmitterCountry()
  {
    Country country = null;
    if ( null != submitter )
    {
      UserAddress address = submitter.getPrimaryAddress();
      if ( null != address )
      {
        country = address.getAddress().getCountry();
      }
    }
    return country;
  }

  /**
   * Indicates whether this claim class type has some form of approval as part of the overall claim
   * process. To be overridden by subclasses.
   *
   * @return boolean
   */
  public abstract boolean isApprovableClaimType();

  /**
   * Get the claim's claim items, if the claim type actually includes claim items
   *
   * @return Set of ClaimItem objects, or null if claim type doesn't have claim items
   */
  public abstract Set getApprovableItems();

  /**
   * @return value of approvalRound property
   */
  public Long getApprovalRound()
  {
    return approvalRound;
  }

  /**
   * @param approvalRound value for approvalRound property
   */
  public void setApprovalRound( Long approvalRound )
  {
    this.approvalRound = approvalRound;
  }

  /**
   * @return value of lastApprovalNode property
   */
  public Node getLastApprovalNode()
  {
    return lastApprovalNode;
  }

  /**
   * @param lastApprovalNode value for lastApprovalNode property
   */
  public void setLastApprovalNode( Node lastApprovalNode )
  {
    this.lastApprovalNode = lastApprovalNode;
  }

  /**
   * @return value of approvableType property
   */
  public ApprovableTypeEnum getApprovableType()
  {
    return ApprovableTypeEnum.CLAIM;
  }

  public boolean isNominationClaim()
  {
    return this instanceof NominationClaim;
  }

  public boolean isRecognitionClaim()
  {
    return this instanceof RecognitionClaim;
  }

  public boolean isProductClaim()
  {
    return this instanceof ProductClaim;
  }

  public boolean isQuizClaim()
  {
    return this instanceof QuizClaim;
  }

  public boolean isAbstractRecognitionClaim()
  {
    return this instanceof AbstractRecognitionClaim;
  }

  /**
   * Alias for getClaimNumber() to fulfill Approvable contract.
   *
   */
  public String getApprovableUid()
  {
    return getClaimNumber();
  }

  /**
   * @return value of claimGroup property
   */
  public ClaimGroup getClaimGroup()
  {
    return claimGroup;
  }

  /**
   * @param claimGroup value for claimGroup property
   */
  public void setClaimGroup( ClaimGroup claimGroup )
  {
    this.claimGroup = claimGroup;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  public boolean equals( Object object )
  {

    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof Claim ) )
    {
      return false;
    }

    Claim claim = (Claim)object;

    if ( this.getClaimNumber() != null ? !this.getClaimNumber().equals( claim.getClaimNumber() ) : claim.getClaimNumber() != null )
    {
      return false;
    }

    return true;
  } // end equals

  public int hashCode()
  {
    int result = 0;

    result += this.getClaimNumber() != null ? this.getClaimNumber().hashCode() : 0;

    return result;
  }

  /**
   * Builds a String representation of this.
   *
   * @return String
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "Claim [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    if ( getSubmitter() != null )
    {
      buf.append( "{submitter=" ).append( this.getSubmitter().getUserName() ).append( "}, " );
    }
    buf.append( "{isOpen=" ).append( this.getOpen() ).append( "}, " );
    buf.append( "{claimNumber=" ).append( this.getClaimNumber() ).append( "} " );
    buf.append( "]" );
    return buf.toString();
  }

  public String getTimeZoneID()
  {
    return timeZoneID;
  }

  public void setTimeZoneID( String timeZoneID )
  {
    this.timeZoneID = timeZoneID;
  }

  public boolean isAddPointsClaim()
  {
    return addPointsClaim;
  }

  public void setAddPointsClaim( boolean addPointsClaim )
  {
    this.addPointsClaim = addPointsClaim;
  }

  public boolean isFileLoadDeposit()
  {
    return isFileLoadDeposit;
  }

  public void setFileLoadDeposit( boolean isFileLoadDeposit )
  {
    this.isFileLoadDeposit = isFileLoadDeposit;
  }
}
