
package com.biperf.core.value.nomination;

/**
 * NominationSubmitDataAttachmentsValueBean.
 * This class is created as part of Client customization for WIP #39189
 * 
 * @author Vamsi
 * @since Mar 1, 2020
 * @version 1.0
 */
public class NominationSubmitDataAttachmentValueBean
{

  private Long linkid;
  private String nominationLink;
  private String nominationUrl;
  private String fileName;

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
