
package com.biperf.core.domain.ssi;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.claim.ClaimFormStepElement;

/**
 * 
 * @author dudam
 * @since May 12, 2015
 * @version 1.0
 */
public class SSIContestClaimField extends BaseDomain
{
  private static final long serialVersionUID = 1L;
  private SSIContest contest;
  private ClaimFormStepElement formElement;
  private boolean required;
  private int sequenceNumber;

  private Set<SSIContestPaxClaimField> paxClaimFields = new LinkedHashSet<SSIContestPaxClaimField>();

  public SSIContest getContest()
  {
    return contest;
  }

  public void setContest( SSIContest contest )
  {
    this.contest = contest;
  }

  public ClaimFormStepElement getFormElement()
  {
    return formElement;
  }

  public void setFormElement( ClaimFormStepElement formElement )
  {
    this.formElement = formElement;
  }

  public boolean isRequired()
  {
    return required;
  }

  public void setRequired( boolean required )
  {
    this.required = required;
  }

  public int getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( int sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  public Set<SSIContestPaxClaimField> getPaxClaimFields()
  {
    return paxClaimFields;
  }

  public void setPaxClaimFields( Set<SSIContestPaxClaimField> paxClaimFields )
  {
    this.paxClaimFields = paxClaimFields;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( contest == null ? 0 : contest.hashCode() );
    result = prime * result + ( formElement == null ? 0 : formElement.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    SSIContestClaimField other = (SSIContestClaimField)obj;
    if ( contest == null )
    {
      if ( other.contest != null )
      {
        return false;
      }
    }
    else if ( !contest.equals( other.contest ) )
    {
      return false;
    }
    if ( formElement == null )
    {
      if ( other.formElement != null )
      {
        return false;
      }
    }
    else if ( !formElement.equals( other.formElement ) )
    {
      return false;
    }
    return true;
  }

}
