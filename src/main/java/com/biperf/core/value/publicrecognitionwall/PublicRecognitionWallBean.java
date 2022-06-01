
package com.biperf.core.value.publicrecognitionwall;

import java.util.List;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.PublicRecognitionCard;

public class PublicRecognitionWallBean
{
  private String comments;
  private PublicRecognitionCard eCard;
  private String promotionName;
  private String promotionType;
  private String behavior;
  private String eCardLink;
  private String eCardVideoLink;

  private Long receiverCount;
  private Boolean isIncludPurl;
  private Long purlRecipientId;
  private Boolean hidePubRecognition;
  private Boolean isCumulative;
  private String giverCommentLangId;

  private PublicRecognitionWallGiverBean publicRecognitionWallGiverBean;
  private List<PublicRecognitionWallReceiverBean> publicRecognitionWallReceiverBean;

  public PublicRecognitionWallGiverBean getPublicRecognitionWallGiverBean()
  {
    return publicRecognitionWallGiverBean;
  }

  public void setPublicRecognitionWallGiverBean( PublicRecognitionWallGiverBean publicRecognitionWallGiverBean )
  {
    this.publicRecognitionWallGiverBean = publicRecognitionWallGiverBean;
  }

  public List<PublicRecognitionWallReceiverBean> getPublicRecognitionWallReceiverBean()
  {
    return publicRecognitionWallReceiverBean;
  }

  public void setPublicRecognitionWallReceiverBean( List<PublicRecognitionWallReceiverBean> publicRecognitionWallReceiverBean )
  {
    this.publicRecognitionWallReceiverBean = publicRecognitionWallReceiverBean;
  }

  public String getComments()
  {
    return comments;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public String getBehavior()
  {
    return behavior;
  }

  public PublicRecognitionCard geteCard()
  {
    return eCard;
  }

  public void seteCard( PublicRecognitionCard eCard )
  {
    this.eCard = eCard;
  }

  public String geteCardLink()
  {
    return eCardLink;
  }

  public String geteCardVideoLink()
  {
    return eCardVideoLink;
  }

  public void seteCardLink( String eCardLink )
  {
    this.eCardLink = eCardLink;
  }

  public Long getReceiverCount()
  {
    return receiverCount;
  }

  public Boolean getIsIncludPurl()
  {
    return isIncludPurl;
  }

  public Long getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public Boolean getHidePubRecognition()
  {
    return hidePubRecognition;
  }

  public Boolean getIsCumulative()
  {
    return isCumulative;
  }

  public String getGiverCommentLangId()
  {
    return giverCommentLangId;
  }

  public void setComments( String comments )
  {
    this.comments = comments != null ? comments : "";
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName != null ? promotionName : "";
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType != null ? promotionType : "";
  }

  public void setBehavior( String behavior )
  {
    this.behavior = behavior != null ? behavior : "";
  }

  public void seteCardVideoLink( String eCardVideoLink )
  {
    this.eCardVideoLink = eCardVideoLink != null ? eCardVideoLink : "";
  }

  public void setReceiverCount( Long receiverCount )
  {
    this.receiverCount = receiverCount;
  }

  public void setIsIncludPurl( Boolean isIncludPurl )
  {
    this.isIncludPurl = isIncludPurl;
  }

  public void setPurlRecipientId( Long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public void setHidePubRecognition( Boolean hidePubRecognition )
  {
    this.hidePubRecognition = hidePubRecognition;
  }

  public void setIsCumulative( Boolean isCumulative )
  {
    this.isCumulative = isCumulative;
  }

  public void setGiverCommentLangId( String giverCommentLangId )
  {
    this.giverCommentLangId = giverCommentLangId;
  }

  public boolean isRecognitionClaim()
  {
    return PromotionType.RECOGNITION.equals( promotionType );
  }
}
