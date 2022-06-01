
package com.biperf.core.ui.quiz;

import java.util.List;

import com.biperf.core.domain.enums.PromotionCertificate;
import com.biperf.core.domain.enums.PromotionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.domain.Content;

public class QuizTakeCertificate
{
  private Long id;
  private String name;
  private String imgThumbUrl;
  private String imgLgUrl;
  private String certificateUrl;

  public QuizTakeCertificate()
  {
    super();
  }

  public QuizTakeCertificate( Long certificateId, String siteUrlPrefix, String certificateUrl2 )
  {
    if ( certificateId != null )
    {
      List<Content> certificateList = PromotionCertificate.getList( PromotionType.QUIZ );
      for ( Content content : certificateList )
      {
        String id = (String)content.getContentDataMap().get( "ID" );
        if ( new Long( id ).equals( certificateId ) )
        {
          this.setId( certificateId );
          this.name = (String)content.getContentDataMap().get( "NAME" );
          this.imgThumbUrl = siteUrlPrefix + "/assets/img/certificates/" + (String)content.getContentDataMap().get( "THUMBNAIL_IMAGE" );
          this.imgLgUrl = siteUrlPrefix + "/assets/img/certificates/" + (String)content.getContentDataMap().get( "BACKGROUND_IMG" );
          this.certificateUrl = certificateUrl2;
        }
      }
    }
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  @JsonProperty( "img" )
  public String getImgThumbUrl()
  {
    return imgThumbUrl;
  }

  public void setImgThumbUrl( String imgThumbUrl )
  {
    this.imgThumbUrl = imgThumbUrl;
  }

  @JsonProperty( "imgLg" )
  public String getImgLgUrl()
  {
    return imgLgUrl;
  }

  public void setImgLgUrl( String imgLgUrl )
  {
    this.imgLgUrl = imgLgUrl;
  }

  @JsonProperty( "url" )
  public String getCertificateUrl()
  {
    return certificateUrl;
  }

  public void setCertificateUrl( String certificateUrl )
  {
    this.certificateUrl = certificateUrl;
  }

}
