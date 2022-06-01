
package com.biperf.core.utils;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.PromotionCertificate;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.multimedia.ECard;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

public class CertUtils
{

  @SuppressWarnings( "unchecked" )
  public static String getFullCertificateUrl( Long certificateId, String contextPath, PromotionType promotionType )
  {
    List<Content> allCertificates = PromotionCertificate.getList( promotionType );
    for ( Content cert : allCertificates )
    {
      String certId = (String)cert.getContentDataMap().get( "ID" );
      if ( certId.equals( certificateId.toString() ) )
      {
        String certName = (String)cert.getContentDataMap().get( "CERT_NAME" );
        return contextPath + ECard.CERTIFICATES_FOLDER + certName + "-full.jpg";
      }
    }
    return null;
  }

  @SuppressWarnings( "unchecked" )
  public static String getPreviewCertificateUrl( Long certificateId, String contextPath, String evaluationType, PromotionType promotionType )
  {
    List<Content> allCertificates = PromotionCertificate.getList( promotionType );
    for ( Content cert : allCertificates )
    {
      String certId = (String)cert.getContentDataMap().get( "ID" );
      if ( certId.equals( certificateId.toString() ) )
      {
        String certName = (String)cert.getContentDataMap().get( "CERT_NAME" );
        return contextPath + ECard.CERTIFICATES_FOLDER + certName + "-preview" + "-" + evaluationType + ".jpg";
      }
    }
    return null;
  }

  public static String getThumbnailCertificateUrl( Content certificate, String contextPath, PromotionType promotionType )
  {
    Object certName = certificate.getContentDataMap().get( "CERT_NAME" );
    if ( certName != null )
    {
      return contextPath + ECard.CERTIFICATES_FOLDER + certName.toString() + "-thumb.jpg";
    }
    return null;
  }

  @SuppressWarnings( "unchecked" )
  public static NominationCertificateDetailsBean getNominationCertificateDetails( Long certificateId )
  {
    NominationCertificateDetailsBean certDetailsBean = new NominationCertificateDetailsBean();
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List<Content> certificates = null;

    Object contentObject = contentReader.getContent( "report.certificate.nomination" );
    if ( contentObject != null && contentObject instanceof List )
    {
      certificates = (List)contentObject;
    }

    for ( Content certificate : certificates )
    {
      Long certId = Long.valueOf( (String)certificate.getContentDataMap().get( "ID" ) );
      if ( certificateId.equals( certId ) )
      {
        certDetailsBean.setId( certId );
        certDetailsBean.setName( (String)certificate.getContentDataMap().get( "NAME" ) );
        certDetailsBean.setCertName( (String)certificate.getContentDataMap().get( "CERT_NAME" ) );
        certDetailsBean.setTextColor( (String)certificate.getContentDataMap().get( "TEXT_COLOR" ) );
        certDetailsBean.setPosition( (String)certificate.getContentDataMap().get( "POSITION" ) );
        break;
      }
    }
    return certDetailsBean;
  }

  @SuppressWarnings( "unchecked" )
  public static List<NominationCertificateDetailsBean> getAllNominationCertificateDetails()
  {
    List<NominationCertificateDetailsBean> certDetails = new ArrayList<NominationCertificateDetailsBean>();
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List<Content> certificates = null;

    Object contentObject = contentReader.getContent( "report.certificate.nomination" );
    if ( contentObject != null && contentObject instanceof List )
    {
      certificates = (List)contentObject;
    }

    for ( Content certificate : certificates )
    {
      NominationCertificateDetailsBean certDetailsBean = new NominationCertificateDetailsBean();
      certDetailsBean.setId( (Long)certificate.getContentDataMap().get( "ID" ) );
      certDetailsBean.setName( (String)certificate.getContentDataMap().get( "NAME" ) );
      certDetailsBean.setCertName( (String)certificate.getContentDataMap().get( "CERT_NAME" ) );
      certDetailsBean.setTextColor( (String)certificate.getContentDataMap().get( "TEXT_COLOR" ) );
      certDetailsBean.setPosition( (String)certificate.getContentDataMap().get( "POSITION" ) );
      certDetails.add( certDetailsBean );
    }
    return certDetails;
  }

}
