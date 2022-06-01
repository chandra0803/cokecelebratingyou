
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.BaseDomain;

/**
 * 
 * SSIContestDocument.
 * 
 * @author chowdhur
 * @since Nov 12, 2014
 */
public class SSIContestDocument extends BaseDomain
{
  private SSIContest contest;
  private String attachmentName;
  private String attachmentDisplayName;
  private String locale;

  public SSIContest getContest()
  {
    return contest;
  }

  public void setContest( SSIContest contest )
  {
    this.contest = contest;
  }

  public String getAttachmentName()
  {
    return attachmentName;
  }

  public void setAttachmentName( String attachmentName )
  {
    this.attachmentName = attachmentName;
  }

  public String getAttachmentDisplayName()
  {
    return attachmentDisplayName;
  }

  public void setAttachmentDisplayName( String attachmentDisplayName )
  {
    this.attachmentDisplayName = attachmentDisplayName;
  }

  public String getLocale()
  {
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( attachmentDisplayName == null ? 0 : attachmentDisplayName.hashCode() );
    result = prime * result + ( attachmentName == null ? 0 : attachmentName.hashCode() );
    result = prime * result + ( contest == null ? 0 : contest.hashCode() );
    result = prime * result + ( locale == null ? 0 : locale.hashCode() );
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
    SSIContestDocument other = (SSIContestDocument)obj;
    if ( attachmentDisplayName == null )
    {
      if ( other.attachmentDisplayName != null )
      {
        return false;
      }
    }
    else if ( !attachmentDisplayName.equals( other.attachmentDisplayName ) )
    {
      return false;
    }
    if ( attachmentName == null )
    {
      if ( other.attachmentName != null )
      {
        return false;
      }
    }
    else if ( !attachmentName.equals( other.attachmentName ) )
    {
      return false;
    }
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
    if ( locale == null )
    {
      if ( other.locale != null )
      {
        return false;
      }
    }
    else if ( !locale.equals( other.locale ) )
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
    return "SSIContestDocument [contest=" + contest + ", attachmentName=" + attachmentName + ", attachmentDisplayName=" + attachmentDisplayName + ", locale=" + locale + "]";
  }

}
