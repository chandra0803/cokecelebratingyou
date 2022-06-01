
package com.biperf.core.domain.mailing;

import com.biperf.core.domain.BaseDomain;

public class MailingAttachmentInfo extends BaseDomain
{
  /**
   * The absolute path fileName of the file used as an attachment
   */
  String fullFileName;

  /**
   * The filename of the attachment attached to the mailing
   */
  String attachmentFileName;

  /**
   * The mailing this is attached to
   */
  Mailing mailing;

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof MailingAttachmentInfo ) )
    {
      return false;
    }

    final MailingAttachmentInfo mailAttachmentInfo = (MailingAttachmentInfo)object;

    if ( getFullFileName() != null && !getFullFileName().equals( mailAttachmentInfo.getFullFileName() ) )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return getFullFileName() != null ? getFullFileName().hashCode() : 0;
  }

  public Mailing getMailing()
  {
    return mailing;
  }

  public void setMailing( Mailing mailing )
  {
    this.mailing = mailing;
  }

  public String getFullFileName()
  {
    return fullFileName;
  }

  public void setFullFileName( String fullFileName )
  {
    this.fullFileName = fullFileName;
  }

  public String getAttachmentFileName()
  {
    return attachmentFileName;
  }

  public void setAttachmentFileName( String attachmentFileName )
  {
    this.attachmentFileName = attachmentFileName;
  }

}
