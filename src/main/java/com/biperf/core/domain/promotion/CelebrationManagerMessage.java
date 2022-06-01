
package com.biperf.core.domain.promotion;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;

public class CelebrationManagerMessage extends BaseDomain
{

  private static final long serialVersionUID = 1L;

  private Integer anniversaryNumberOfDays = new Integer( 0 );
  private Integer anniversaryNumberOfYears = new Integer( 0 );
  private RecognitionPromotion promotion = null;
  private Participant recipient = null;
  private Date msgCollectExpireDate;
  private User manager = null;
  private User managerAbove = null;
  private String managerMessage;
  private String managerAboveMessage;

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

  public RecognitionPromotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( RecognitionPromotion promotion )
  {
    this.promotion = promotion;
  }

  public Participant getRecipient()
  {
    return recipient;
  }

  public void setRecipient( Participant recipient )
  {
    this.recipient = recipient;
  }

  public Date getMsgCollectExpireDate()
  {
    return msgCollectExpireDate;
  }

  public void setMsgCollectExpireDate( Date msgCollectExpireDate )
  {
    this.msgCollectExpireDate = msgCollectExpireDate;
  }

  public User getManager()
  {
    return manager;
  }

  public void setManager( User manager )
  {
    this.manager = manager;
  }

  public User getManagerAbove()
  {
    return managerAbove;
  }

  public void setManagerAbove( User managerAbove )
  {
    this.managerAbove = managerAbove;
  }

  public String getManagerMessage()
  {
    return managerMessage;
  }

  public void setManagerMessage( String managerMessage )
  {
    this.managerMessage = managerMessage;
  }

  public String getManagerAboveMessage()
  {
    return managerAboveMessage;
  }

  public void setManagerAboveMessage( String managerAboveMessage )
  {
    this.managerAboveMessage = managerAboveMessage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( anniversaryNumberOfDays == null ? 0 : anniversaryNumberOfDays.hashCode() );
    result = prime * result + ( anniversaryNumberOfYears == null ? 0 : anniversaryNumberOfYears.hashCode() );
    result = prime * result + ( manager == null ? 0 : manager.hashCode() );
    result = prime * result + ( managerAbove == null ? 0 : managerAbove.hashCode() );
    result = prime * result + ( managerAboveMessage == null ? 0 : managerAboveMessage.hashCode() );
    result = prime * result + ( managerMessage == null ? 0 : managerMessage.hashCode() );
    result = prime * result + ( msgCollectExpireDate == null ? 0 : msgCollectExpireDate.hashCode() );
    result = prime * result + ( promotion == null ? 0 : promotion.hashCode() );
    result = prime * result + ( recipient == null ? 0 : recipient.hashCode() );
    return result;
  }

  /**
   * {@inheritDoc}
   */
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
    CelebrationManagerMessage other = (CelebrationManagerMessage)obj;
    if ( anniversaryNumberOfDays == null )
    {
      if ( other.anniversaryNumberOfDays != null )
      {
        return false;
      }
    }
    else if ( !anniversaryNumberOfDays.equals( other.anniversaryNumberOfDays ) )
    {
      return false;
    }
    if ( anniversaryNumberOfYears == null )
    {
      if ( other.anniversaryNumberOfYears != null )
      {
        return false;
      }
    }
    else if ( !anniversaryNumberOfYears.equals( other.anniversaryNumberOfYears ) )
    {
      return false;
    }
    if ( manager == null )
    {
      if ( other.manager != null )
      {
        return false;
      }
    }
    else if ( !manager.equals( other.manager ) )
    {
      return false;
    }
    if ( managerAbove == null )
    {
      if ( other.managerAbove != null )
      {
        return false;
      }
    }
    else if ( !managerAbove.equals( other.managerAbove ) )
    {
      return false;
    }
    if ( managerAboveMessage == null )
    {
      if ( other.managerAboveMessage != null )
      {
        return false;
      }
    }
    else if ( !managerAboveMessage.equals( other.managerAboveMessage ) )
    {
      return false;
    }
    if ( managerMessage == null )
    {
      if ( other.managerMessage != null )
      {
        return false;
      }
    }
    else if ( !managerMessage.equals( other.managerMessage ) )
    {
      return false;
    }
    if ( msgCollectExpireDate == null )
    {
      if ( other.msgCollectExpireDate != null )
      {
        return false;
      }
    }
    else if ( !msgCollectExpireDate.equals( other.msgCollectExpireDate ) )
    {
      return false;
    }
    if ( promotion == null )
    {
      if ( other.promotion != null )
      {
        return false;
      }
    }
    else if ( !promotion.equals( other.promotion ) )
    {
      return false;
    }
    if ( recipient == null )
    {
      if ( other.recipient != null )
      {
        return false;
      }
    }
    else if ( !recipient.equals( other.recipient ) )
    {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "CelebrationManagerMessage [anniversaryNumberOfDays=" + anniversaryNumberOfDays + ", anniversaryNumberOfYears=" + anniversaryNumberOfYears + ", promotion=" + promotion + ", recipient="
        + recipient + ", msgCollectExpireDate=" + msgCollectExpireDate + ", manager=" + manager + ", managerAbove=" + managerAbove + ", managerMessage=" + managerMessage + ", managerAboveMessage="
        + managerAboveMessage + "]";
  }

}
