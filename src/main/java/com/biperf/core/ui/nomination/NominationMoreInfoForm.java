
package com.biperf.core.ui.nomination;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.ListUtils;
import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.value.nomination.NominationSubmitDataAttachmentValueBean;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NominationMoreInfoForm extends BaseForm
{
  private static final long serialVersionUID = 1L;

  private Promotion promotion;
  private String nominationName;
  private String approversMessage;
  private FormFile nominationDoc;
  private String attachedDoc;
  private Long claimId;
  private String moreInformation;
  private Long promoId;
  private Boolean addAttachment;
//  private String nominationLink;
//  private String nominationLinkUrl;
  
  // Client customization for WIP #39189 starts
  private List<NominationSubmitDataAttachmentValueBean> nominationLinks;

  @SuppressWarnings( "unchecked" )
  @JsonProperty( "nominationLinks" )
  public List<NominationSubmitDataAttachmentValueBean> getNominationLinks()
  {
    if ( nominationLinks == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new NominationSubmitDataAttachmentValueBean();
        }
      };
      nominationLinks = ListUtils.lazyList( new ArrayList<>(), factory );
    }
    return nominationLinks;
  }

  public void setNominationLinks( List<NominationSubmitDataAttachmentValueBean> nominationLinks )
  {
    this.nominationLinks = nominationLinks;
  }

  @SuppressWarnings( "unchecked" )
  public void addNominationLinks( NominationSubmitDataAttachmentValueBean nominationLink )
  {
    if ( nominationLinks == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new NominationSubmitDataAttachmentValueBean();
        }
      };
      nominationLinks = ListUtils.lazyList( new ArrayList<>(), factory );
    }
    this.nominationLinks.add( nominationLink );
  }
  // Client customization for WIP #39189 ends

  public Boolean getAddAttachment()
  {
    return addAttachment;
  }

  public void setAddAttachment( Boolean addAttachment )
  {
    this.addAttachment = addAttachment;
  }

  public Long getPromoId()
  {
    return promoId;
  }

  public void setPromoId( Long promoId )
  {
    this.promoId = promoId;
  }

  public String getMoreInformation()
  {
    return moreInformation;
  }

  public void setMoreInformation( String moreInformation )
  {
    this.moreInformation = moreInformation;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getNominationName()
  {
    return nominationName;
  }

  public void setNominationName( String nominationName )
  {
    this.nominationName = nominationName;
  }

  public String getApproversMessage()
  {
    return approversMessage;
  }

  public void setApproversMessage( String approversMessage )
  {
    this.approversMessage = approversMessage;
  }

  public String getAttachedDoc()
  {
    return attachedDoc;
  }

  public void setAttachedDoc( String attachedDoc )
  {
    this.attachedDoc = attachedDoc;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public FormFile getNominationDoc()
  {
    return nominationDoc;
  }

  public void setNominationDoc( FormFile nominationDoc )
  {
    this.nominationDoc = nominationDoc;
  }

  /*public String getNominationLink()
  {
    return nominationLink;
  }

  public void setNominationLink( String nominationLink )
  {
    this.nominationLink = nominationLink;
  }

  public String getNominationLinkUrl()
  {
    return nominationLinkUrl;
  }

  public void setNominationLinkUrl( String nominationLinkUrl )
  {
    this.nominationLinkUrl = nominationLinkUrl;
  }*/

}
