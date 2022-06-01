
package com.biperf.core.ui.recognition;

import java.util.List;

import com.biperf.core.domain.enums.EnvironmentTypeEnum;
import com.biperf.core.domain.enums.PromotionCertificate;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.Environment;
import com.objectpartners.cms.domain.Content;

public class EcardUtil
{
  private EcardUtil()
  {
    //
  }

  public static String getOwnCardUrl( String ownCardName )
  {
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    String ownCardUrl = null;
    if ( EnvironmentTypeEnum.isAws() )
    {
      siteUrlPrefix = getSystemVariableService().getContextName();
    }
    ownCardUrl = siteUrlPrefix + "-cm/cm3dam/ecard" + '/' + ownCardName;

    return ownCardUrl;
  }

  public static String getPurlCardUrl( String ownCardName )
  {
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    String ownCardUrl = null;
    if ( EnvironmentTypeEnum.isAws() )
    {
      siteUrlPrefix = getSystemVariableService().getContextName();
    }
    ownCardUrl = siteUrlPrefix + "-cm/cm3dam/purl" + '/' + ownCardName;

    return ownCardUrl;
  }

  public static String getNominatorAttachmentUrl( String fileName )
  {
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }

    String ownCardUrl = siteUrlPrefix + "-cm/cm3dam/nomination" + '/' + fileName;

    return ownCardUrl;
  }

  public static String getRecognitionCertificateUrl( Long certificateId, String contextPath )
  {
    List<Content> allCertificates = PromotionCertificate.getList( PromotionType.RECOGNITION );
    for ( Content cert : allCertificates )
    {
      String certId = (String)cert.getContentDataMap().get( "ID" );
      if ( certId.equals( certificateId.toString() ) )
      {
        return contextPath + ECard.CERTIFICATES_FOLDER + (String)cert.getContentDataMap().get( "BACKGROUND_IMG" );
      }
    }

    return null;
  }

  public static String getNominationCertificateUrl( Long certificateId, String contextPath )
  {
    List<Content> allCertificates = PromotionCertificate.getList( PromotionType.NOMINATION );
    for ( Content cert : allCertificates )
    {
      String certId = (String)cert.getContentDataMap().get( "ID" );
      if ( certId.equals( certificateId.toString() ) )
      {
        return contextPath + ECard.CERTIFICATES_FOLDER + (String)cert.getContentDataMap().get( "BACKGROUND_IMG" );
      }
    }

    return null;
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }
}
