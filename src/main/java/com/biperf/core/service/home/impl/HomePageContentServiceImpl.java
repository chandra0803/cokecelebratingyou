/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/home/impl/HomePageContentServiceImpl.java,v $
 */

package com.biperf.core.service.home.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.banner.GenericBannerDetailBean;
import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.service.diycommunications.ParticipantDIYCommunicationsService;
import com.biperf.core.service.home.HomePageContentService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ClientStateUtils;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * Banner Ads Service Implementation for retrieving Banner Ads for a PAX.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>waldal</td>
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HomePageContentServiceImpl implements HomePageContentService
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------
  private static final String INSTANT_POLL = "home.instantpoll";

  private ParticipantDIYCommunicationsService participantDIYCommunicationsService;
  private SystemVariableService systemVariableService;

  /**
   * Returns active instant polls available to the current user's audiences.
   * 
   * @return active instant polls available to the current user's audiences, as a List of Content
   *         objects.
   */
  public List getInstantPolls()
  {
    return (List)ContentReaderManager.getContentReader().getContent( INSTANT_POLL );
  }

  /**
   * *Return String Overridden from
   * 
   * @see com.biperf.core.service.home.HomePageContentService#getNavigationAssetKey()
   * @return String
   */
  public String getNavigationAssetKey()
  {
    return "DEFAULT";
  }

  public List<Content> getGenericBannerAds()
  {
    List<Content> bannerAdList = new ArrayList<Content>();
    bannerAdList = getAllBannerAds( SystemVariableService.HOMEPAGE_GENERICBANNER_AD );
    return bannerAdList;
  }

  /**
   * Returns all the banner ads defined by the specified Content Manager asset. Returns an empty
   * list if the asset defines no banner ads.
   * 
   * @param contentKey a key to a Content Manager asset that defines banner ads.
   * @return all the banner ads defined by the specified Content Manager asset.
   */
  @SuppressWarnings( "unchecked" )
  private List<Content> getAllBannerAds( String contentKey )
  {
    return (List<Content>)ContentReaderManager.getContentReader().getContent( contentKey );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.home.HomePageContentService#getWelcomeMessageAssetKey()
   * @return String
   */
  public String getWelcomeMessageAssetKey()
  {
    return "home.welcome.messages";
  }

  @Override
  public List<Content> getGenericBannerAdsForSlides()
  {
    List<Content> bannerAds = getGenericBannerAds();
    List<Content> activeDiyBannerAdsList = participantDIYCommunicationsService.getDiyCommunications( DIYCommunications.COMMUNICATION_TYPE_BANNER, DIYCommunications.DIY_BANNER_SECTION_CODE );
    if ( bannerAds == null )
    {
      bannerAds = new ArrayList<Content>();
    }
    if ( activeDiyBannerAdsList != null && activeDiyBannerAdsList.size() > 0 )
    {
      bannerAds.addAll( activeDiyBannerAdsList );
    }

    int maxResultsSize = systemVariableService.getPropertyByName( SystemVariableService.BANNER_AD_LIMIT ).getIntVal();

    if ( bannerAds.size() > maxResultsSize )
    {
      bannerAds = bannerAds.subList( 0, maxResultsSize );
    }

    return bannerAds;
  }

  public List<GenericBannerDetailBean> getGenericBannerSlides( String contextPath )
  {
    List<Content> bannerAds = getGenericBannerAdsForSlides();
    List<GenericBannerDetailBean> slides = new ArrayList<GenericBannerDetailBean>();

    for ( int i = 0; i < bannerAds.size(); i++ )
    {
      Content bannerAd = bannerAds.get( i );
      if ( bannerAd != null )
      {
        GenericBannerDetailBean bannerDetailBean = new GenericBannerDetailBean();
        String text = (String)bannerAd.getContentDataMap().get( "TEXT" );
        String linkUrl = (String)bannerAd.getContentDataMap().get( "LINK_URL" );
        String linkText = (String)bannerAd.getContentDataMap().get( "LINK_TEXT" );
        String target = (String)bannerAd.getContentDataMap().get( "TARGET" );

        String embeddedHtml = (String)bannerAd.getContentDataMap().get( "EMBEDDED_HTML" );
        String bannerImageUrl = (String)bannerAd.getContentDataMap().get( "BANNER_IMAGE" );
        String bannerImageUrlMax = (String)bannerAd.getContentDataMap().get( "BANNER_IMAGE_MAX" );
        String bannerImageUrlMobile = (String)bannerAd.getContentDataMap().get( "BANNER_IMAGE_MOBILE" );
        String encodeClientState = (String)bannerAd.getContentDataMap().get( "ENCODE" );
        String classes = (String)bannerAd.getContentDataMap().get( "CLASSES" );
        // flashRequestString: this is intended to contain embeded html for displaying
        // things like animated flash swf or even oddcast talking swf
        // if present, this always override imageLinkTargetString in the jsp
        if ( embeddedHtml != null )
        {
          // want the object tag in plain text string
          embeddedHtml = embeddedHtml.replaceAll( "&lt;", "<" );
          embeddedHtml = embeddedHtml.replaceAll( "&gt;", ">" );
          embeddedHtml = embeddedHtml.replaceAll( "&quot;", "\"" );
          embeddedHtml = embeddedHtml.replaceAll( "&amp;", "&" );
        }

        bannerDetailBean.setId( String.valueOf( i + 1 ) );
        bannerDetailBean.setText( text );
        bannerDetailBean.setLinkText( linkText );
        bannerDetailBean.setLinkUrl( linkUrl );
        bannerDetailBean.setTarget( target );
        bannerDetailBean.setBannerImageUrl( bannerImageUrl );
        bannerDetailBean.setBannerImageUrl_max( bannerImageUrlMax );
        bannerDetailBean.setBannerImageUrl_mobile( bannerImageUrlMobile );
        bannerDetailBean.setClasses( classes );

        // **** if true, encrypt query string using client state
        if ( encodeClientState != null && encodeClientState.equals( "true" ) && linkUrl != null )
        {
          bannerDetailBean.setLinkUrl( encryptClientState( linkUrl, contextPath ) );
        }
        else
        {
          bannerDetailBean.setLinkUrl( checkAndAppendContextPath( linkUrl, contextPath ) );
        }
        slides.add( bannerDetailBean );
      }
    }

    return slides;
  }

  private String encryptClientState( String linkInCM, String contextPath )
  {
    Map<String, String> clientStateParameterMap = new HashMap<String, String>();
    // without this condition, it was throwing IndexOutOfBounds exception,
    // am just verifying whether there is a query string or not,
    // there could be a better way of doing this, may be by using URI object
    // I don't have time so am leaving it for future visits.
    String encryptedUrl = "";

    if ( linkInCM.indexOf( "?" ) > 0 )
    {

      String pagePath = linkInCM.substring( 0, linkInCM.indexOf( "?" ) );
      String queryString = linkInCM.substring( linkInCM.indexOf( "?" ) + 1 );
      String[] parameters = queryString.split( "&" );

      // single parameter
      if ( parameters == null )
      {
        String key = queryString.substring( 0, queryString.indexOf( "=" ) ).trim();
        String value = queryString.substring( queryString.indexOf( "=" ) + 1, queryString.length() ).trim();
        clientStateParameterMap.put( key, value );
      }
      else
      {
        for ( int i = 0; i < parameters.length; i++ )
        {
          String parameter = parameters[i];

          if ( parameter.indexOf( "=" ) > 0 )
          {
            String key = parameter.substring( 0, parameter.indexOf( "=" ) ).trim();
            String value = parameter.substring( parameter.indexOf( "=" ) + 1, parameter.length() ).trim();
            clientStateParameterMap.put( key, value );
          }
        }
      }
      encryptedUrl = ClientStateUtils.generateEncodedLink( contextPath, pagePath, clientStateParameterMap );
    }
    else
    {
      // Fix to Bug #15391
      encryptedUrl = checkAndAppendContextPath( linkInCM, contextPath );
    }

    return encryptedUrl;
  }

  private String checkAndAppendContextPath( String linkInCM, String contextPath )
  {
    String contextPathURL = linkInCM;
    // If link is relative, NOT absolute
    if ( null != linkInCM && linkInCM.indexOf( "http" ) == -1 )
    {
      // Prepend context path to the link
      StringBuffer buffer = new StringBuffer( contextPath );
      // This condition for those rare cases which do not start with '/'
      if ( !linkInCM.startsWith( "/" ) )
      {
        buffer.append( "/" );
      }
      buffer.append( linkInCM );
      contextPathURL = buffer.toString();
    }

    return contextPathURL;
  }

  public void setParticipantDIYCommunicationsService( ParticipantDIYCommunicationsService participantDIYCommunicationsService )
  {
    this.participantDIYCommunicationsService = participantDIYCommunicationsService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }
}
