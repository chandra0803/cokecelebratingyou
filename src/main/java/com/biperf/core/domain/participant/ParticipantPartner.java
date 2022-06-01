
package com.biperf.core.domain.participant;

import java.sql.Timestamp;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.utils.UserManager;

/**
 * ParticipantPartner.
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
 * <td>reddy</td>
 * <td>Mar 3, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ParticipantPartner extends BaseDomain
{
  private Participant participant;
  private Participant partner;
  private Promotion promotion;
  private String partnerDisplayLabel;
  private boolean partnerEmailSent = false;

  public String getPartnerDisplayLabel()
  {
    return partnerDisplayLabel;
  }

  public void setPartnerDisplayLabel( String partnerDisplayLabel )
  {
    this.partnerDisplayLabel = partnerDisplayLabel;
  }

  /**
   * ParticipantPartner
   */
  public ParticipantPartner()
  {
    super();
    getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof ParticipantPartner ) )
    {
      return false;
    }

    final ParticipantPartner participantPartnerAssoc = (ParticipantPartner)object;

    if ( getParticipant() != null ? !getParticipant().equals( participantPartnerAssoc.getParticipant() ) : participantPartnerAssoc.getParticipant() != null )
    {
      return false;
    }
    if ( getPromotion() != null ? !getPromotion().equals( participantPartnerAssoc.getPromotion() ) : participantPartnerAssoc.getPromotion() != null )
    {
      return false;
    }
    if ( getPartner() != null ? !getPartner().equals( participantPartnerAssoc.getPartner() ) : participantPartnerAssoc.getPartner() != null )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result;
    result = getParticipant() != null ? getParticipant().hashCode() : 0;
    result = 29 * result + ( getPromotion() != null ? getPromotion().hashCode() : 0 ) + ( getParticipant() != null ? getParticipant().hashCode() : 0 )
        + ( getPartner() != null ? getPartner().hashCode() : 0 );

    return result;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public Participant getPartner()
  {
    return partner;
  }

  public void setPartner( Participant partner )
  {
    this.partner = partner;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public boolean isPartnerEmailSent()
  {
    return partnerEmailSent;
  }

  public void setPartnerEmailSent( boolean isPartnerEmailSent )
  {
    this.partnerEmailSent = isPartnerEmailSent;
  }
}
