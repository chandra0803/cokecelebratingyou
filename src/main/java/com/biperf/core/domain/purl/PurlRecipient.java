
package com.biperf.core.domain.purl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.enums.PurlContributorState;
import com.biperf.core.domain.enums.PurlRecipientState;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;

public class PurlRecipient extends BaseDomain
{
  private static final int PURL_ACCESS_CUTOFF_BEFORE_AWARD_DATE = 1;

  private RecognitionPromotion promotion;
  private User user;
  private Node node;
  private Participant submitter;
  private Node submitterNode;
  private User proxyUser;
  private Date invitationStartDate;
  private Date awardDate;
  private PromoMerchProgramLevel awardLevel;
  private BigDecimal awardAmount;
  private PurlRecipientState state;
  private Claim claim;
  private Set<PurlContributor> contributors = new LinkedHashSet<PurlContributor>();
  private boolean showDefaultContributors;
  private Integer anniversaryNumberOfDays = new Integer( 0 );
  private Integer anniversaryNumberOfYears = new Integer( 0 );
  private CelebrationManagerMessage celebrationManagerMessage;

  private List<PurlRecipientCustomElement> customElements = new ArrayList<PurlRecipientCustomElement>();

  // Will be set only when required
  private PurlContributor managerContributor;

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof PurlRecipient ) )
    {
      return false;
    }

    final PurlRecipient info = (PurlRecipient)object;
    if ( info.user != null && !info.user.equals( user ) )
    {
      return false;
    }
    if ( info.node != null && !info.node.equals( node ) )
    {
      return false;
    }
    if ( info.submitter != null && !info.submitter.equals( submitter ) )
    {
      return false;
    }
    if ( info.submitterNode != null && !info.submitterNode.equals( submitterNode ) )
    {
      return false;
    }
    if ( info.proxyUser != null && !info.proxyUser.equals( proxyUser ) )
    {
      return false;
    }
    if ( info.promotion != null && !info.promotion.equals( promotion ) )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + ( user == null ? 0 : user.hashCode() );
    result = PRIME * result + ( node == null ? 0 : node.hashCode() );
    result = PRIME * result + ( submitter == null ? 0 : submitter.hashCode() );
    result = PRIME * result + ( submitterNode == null ? 0 : submitterNode.hashCode() );
    result = PRIME * result + ( proxyUser == null ? 0 : proxyUser.hashCode() );
    result = PRIME * result + ( promotion == null ? 0 : promotion.hashCode() );
    return result;
  }

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public Date getAwardDate()
  {
    return awardDate;
  }

  public Date getCloseDate()
  {
    Date closeDate = new Date();
    closeDate.setTime( awardDate.getTime() - PURL_ACCESS_CUTOFF_BEFORE_AWARD_DATE * DateUtils.MILLIS_PER_DAY );
    return com.biperf.core.utils.DateUtils.toEndDate( closeDate );
  }

  public void setAwardDate( Date awardDate )
  {
    this.awardDate = awardDate;
  }

  public PromoMerchProgramLevel getAwardLevel()
  {
    return awardLevel;
  }

  public void setAwardLevel( PromoMerchProgramLevel awardLevel )
  {
    this.awardLevel = awardLevel;
  }

  public Date getInvitationStartDate()
  {
    return invitationStartDate;
  }

  public void setInvitationStartDate( Date invitationStartDate )
  {
    this.invitationStartDate = invitationStartDate;
  }

  public RecognitionPromotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( RecognitionPromotion promotion )
  {
    this.promotion = promotion;
  }

  public BigDecimal getAwardAmount()
  {
    return awardAmount;
  }

  public void setAwardAmount( BigDecimal awardAmount )
  {
    this.awardAmount = awardAmount;
  }

  public Node getNode()
  {
    return node;
  }

  public void setNode( Node node )
  {
    this.node = node;
  }

  public PurlRecipientState getState()
  {
    return state;
  }

  public void setState( PurlRecipientState state )
  {
    this.state = state;
  }

  public boolean isAwardEligible()
  {
    return null != awardAmount && !new BigDecimal( 0 ).equals( awardAmount ) || null != awardLevel;
  }

  public boolean isAwardProcessed()
  {
    return null != claim;
  }

  public Claim getClaim()
  {
    return claim;
  }

  public void setClaim( Claim claim )
  {
    this.claim = claim;
  }

  public Set<PurlContributor> getContributors()
  {
    return contributors;
  }

  public void setContributors( Set<PurlContributor> contributors )
  {
    this.contributors = contributors;
  }

  public void addContributors( Set<PurlContributor> contributors )
  {
    for ( PurlContributor contributor : contributors )
    {
      addContributor( contributor );
    }
  }

  public void addContributor( PurlContributor contributor )
  {
    contributor.setPurlRecipient( this );
    contributors.add( contributor );
  }

  public Set<PurlContributor> getInvitedContributors()
  {
    Set<PurlContributor> invitedContributors = new LinkedHashSet<PurlContributor>();
    for ( PurlContributor contributor : contributors )
    {
      if ( contributor.getUser() == null && contributor.getEmailAddr() == null )
      {
        continue;
      }
      if ( !contributor.isSendLater() )
      {
        invitedContributors.add( contributor );
      }
    }
    return invitedContributors;
  }

  public int getContributorInvited()
  {
    return getInvitedContributors().size();
  }

  public int getContributorViewed()
  {
    int count = 0;
    Set<PurlContributor> invitedContributors = getInvitedContributors();
    for ( PurlContributor contributor : invitedContributors )
    {
      if ( contributor.getUser() == null && contributor.getEmailAddr() == null )
      {
        continue;
      }
      if ( contributor.getState().getCode().equals( PurlContributorState.CONTRIBUTION ) || contributor.getState().getCode().equals( PurlContributorState.COMPLETE ) )
      {
        count++;
      }
    }
    return count;
  }

  public boolean isShowDefaultContributors()
  {
    return showDefaultContributors;
  }

  public void setShowDefaultContributors( boolean showDefaultContributors )
  {
    this.showDefaultContributors = showDefaultContributors;
  }

  public void setCustomElements( List<PurlRecipientCustomElement> customElements )
  {
    this.customElements = customElements;
  }

  public List<PurlRecipientCustomElement> getCustomElements()
  {
    return customElements;
  }

  public PurlRecipientCustomElement getCustomElement( int index )
  {
    return (PurlRecipientCustomElement)this.customElements.get( index );
  }

  public void addCustomElement( PurlRecipientCustomElement element )
  {
    element.setPurlRecipient( this );
    customElements.add( element );
  }

  public PurlContributor getManagerContributor()
  {
    return managerContributor;
  }

  public void setManagerContributor( PurlContributor managerContributor )
  {
    this.managerContributor = managerContributor;
  }

  public Participant getSubmitter()
  {
    return this.submitter;
  }

  public void setSubmitter( Participant submitter )
  {
    this.submitter = submitter;
  }

  public Node getSubmitterNode()
  {
    return submitterNode;
  }

  public void setSubmitterNode( Node submitterNode )
  {
    this.submitterNode = submitterNode;
  }

  public User getProxyUser()
  {
    return proxyUser;
  }

  public void setProxyUser( User proxyUser )
  {
    this.proxyUser = proxyUser;
  }

  public String getContributorDisplayInfo()
  {
    String displayInfo = "";

    for ( Iterator iter = customElements.iterator(); iter.hasNext(); )
    {
      PurlRecipientCustomElement customElement = (PurlRecipientCustomElement)iter.next();
      if ( StringUtils.isNotEmpty( customElement.getDisplayValue() ) )
      {
        displayInfo += customElement.getDisplayValue() + " ";
      }
    }

    displayInfo += promotion.getName();

    return displayInfo;
  }

  public String getCustomFormElementInfo()
  {
    String displayInfo = "";

    for ( Iterator iter = customElements.iterator(); iter.hasNext(); )
    {
      PurlRecipientCustomElement customElement = (PurlRecipientCustomElement)iter.next();
      if ( StringUtils.isNotEmpty( customElement.getDisplayValue() ) )
      {
        displayInfo += customElement.getDisplayValue() + " ";
      }
    }

    return displayInfo.trim();
  }

  public Integer getAnniversaryNumberOfDays()
  {
    return anniversaryNumberOfDays;
  }

  public void setAnniversaryNumberOfDays( Integer anniversaryNumberOfDays )
  {
    this.anniversaryNumberOfDays = anniversaryNumberOfDays;
  }

  public Integer getAnniversaryNumberOfYears()
  {
    return anniversaryNumberOfYears;
  }

  public void setAnniversaryNumberOfYears( Integer anniversaryNumberOfYears )
  {
    this.anniversaryNumberOfYears = anniversaryNumberOfYears;
  }

  public CelebrationManagerMessage getCelebrationManagerMessage()
  {
    return celebrationManagerMessage;
  }

  public void setCelebrationManagerMessage( CelebrationManagerMessage celebrationManagerMessage )
  {
    this.celebrationManagerMessage = celebrationManagerMessage;
  }

}
