
package com.biperf.core.ui.nomination;

import com.biperf.core.utils.StringUtil;

/**
 * NominationSubmitDataAttachmentViewBean
 * This class is created as part of Client customization for WIP #39189
 * 
 * @author Vamsi
 * @since Mar 1, 2020
 * @version 1.0
 */
public class NominationSubmitDataAttachmentViewBean
{

  private Long linkid;
  private String nominationLink;
  private String nominationUrl;
  private String fileName;

  public NominationSubmitDataAttachmentViewBean()
  {

  }

  public NominationSubmitDataAttachmentViewBean( Long linkId, String nominationLink, String nominationUrl, String fileName )
  {
    this.linkid = linkId;
    this.nominationLink = nominationLink;
    this.nominationUrl = nominationUrl;
    
    if ( StringUtil.isNullOrEmpty( fileName ) )
      this.fileName = nominationUrl;
    else
      this.fileName = fileName;
  }

  public Long getLinkid()
  {
    return linkid;
  }

  public void setLinkid( Long linkid )
  {
    this.linkid = linkid;
  }

  public String getNominationLink()
  {
    return nominationLink;
  }

  public void setNominationLink( String nominationLink )
  {
    this.nominationLink = nominationLink;
  }

  public String getNominationUrl()
  {
    return nominationUrl;
  }

  public void setNominationUrl( String nominationUrl )
  {
    this.nominationUrl = nominationUrl;
  }

  public String getFileName()
  {
    return fileName;
  }

  public void setFileName( String fileName )
  {
    this.fileName = fileName;
  }

}
