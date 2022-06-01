
package com.biperf.core.domain.claim;

import java.util.Date;

public class QuizClaimItem extends ClaimItem
{

  private Date dateTaken;

  public void setDateTaken( Date dateTaken )
  {
    this.dateTaken = dateTaken;
  }

  public Date getDateTaken()
  {
    return dateTaken;
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof QuizClaimItem ) )
    {
      return false;
    }

    QuizClaimItem quizClaimItem = (QuizClaimItem)object;

    if ( getSerialId() != null ? !getSerialId().equals( quizClaimItem.getSerialId() ) : quizClaimItem.getSerialId() != null )
    {
      return false;
    }

    return true;

  }

  public int hashCode()
  {
    int result = 0;

    result += this.getSerialId() != null ? this.getSerialId().hashCode() : 13;

    return result;
  }
}
