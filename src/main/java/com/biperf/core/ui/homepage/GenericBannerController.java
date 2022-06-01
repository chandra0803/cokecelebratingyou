
package com.biperf.core.ui.homepage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * G3NewsTileController.
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
 * <td>mattam</td>
 * <td>Dec 01, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GenericBannerController extends BaseController
{
  /**
   * Configures the environment for the News tile,
  * @throws ParseException 
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws ParseException
  {
    String type = (String)tileContext.getAttribute( "type" );
    String location = (String)tileContext.getAttribute( "location" );
    List<Content> genericBannerAds = (List)ContentReaderManager.getContentReader().getContent( "home.genericbanners" );
    if ( genericBannerAds != null )
    {
      genericBannerAds = filterbannersBasedOntype( genericBannerAds, type );
      if ( genericBannerAds != null )
      {
        if ( type.trim().equals( "bannershowonehalf" ) || type.trim().equals( "leftbannershowonefull" ) || type.trim().equals( "rightbannershowonefull" ) )
        {
          Content bannerAd = getRandomBannerAd( genericBannerAds );
          if ( bannerAd != null )
          {
            genericBannerAds = null;// cleanup and add random banner to list
            genericBannerAds = new ArrayList<Content>();
            genericBannerAds.add( bannerAd );
          }
        }
        if ( type.trim().contains( "full" ) )
        {
          genericBannerAds = filterbannersBasedOnLocation( genericBannerAds, location );
        }
      }

    }
    request.setAttribute( "genericBannerAds", genericBannerAds );
    int genericBannerAdsSize = 0;
    if ( genericBannerAds != null )
    {
      genericBannerAdsSize = genericBannerAds.size();
    }
    String[] flashRequestStrings = new String[genericBannerAdsSize];
    String[] imageLinkTargetStrings = new String[genericBannerAdsSize];

    // Walk thru each bannerAd, assembles html
    Content bannerAd = null;
    String flashRequestString = null;
    String linkString = null;
    StringBuffer imageLinkTargetString = null;
    for ( int i = 0; i < genericBannerAdsSize; i++ )
    {
      // pulling asset content for banner ads
      bannerAd = (Content)genericBannerAds.get( i );
      if ( bannerAd != null )
      {
        flashRequestString = (String)bannerAd.getContentDataMap().get( "EMBEDDED_HTML" );
        linkString = (String)bannerAd.getContentDataMap().get( "LINK_URL" );
        imageLinkTargetString = new StringBuffer( "" );

        if ( flashRequestString != null )
        {
          // want the object tag in plain text string
          flashRequestString = flashRequestString.replaceAll( "&lt;", "<" );
          flashRequestString = flashRequestString.replaceAll( "&gt;", ">" );
          flashRequestString = flashRequestString.replaceAll( "&quot;", "\"" );
          flashRequestString = flashRequestString.replaceAll( "&amp;", "&" );

          flashRequestStrings[i] = flashRequestString;
          request.setAttribute( "flashNeeded", new Boolean( true ) );
        }
        else if ( linkString != null )
        {
          // **** if true, encrypt query string using client state
          String encodeClientState = (String)bannerAd.getContentDataMap().get( "ENCODE" );
          if ( encodeClientState != null && encodeClientState.equals( "true" ) && linkString != null )
          {

            linkString = encryptClientState( linkString, request );
          }
          else
          {
            linkString = checkAndAppendContextPath( linkString, request );
          }
          imageLinkTargetString.append( linkString );
          imageLinkTargetStrings[i] = imageLinkTargetString.toString();
        }
      }
    } // end for
    request.setAttribute( "flashRequestString", flashRequestStrings );
    request.setAttribute( "imageLinkTargetString", imageLinkTargetStrings );
  }

  /**
     * Filters the List based on type.
     * @return  bannerAds
     */
  private List<Content> filterbannersBasedOntype( List<Content> genericBannerAds, String type )
  {
    List<Content> bannerAds = new ArrayList<Content>();
    Content content = null;
    Map contentDataMap = null;
    for ( int i = 0; i < genericBannerAds.size(); i++ )
    {
      content = (Content)genericBannerAds.get( i );
      contentDataMap = content.getContentDataMap();
      // Pick all half banners
      if ( type.trim().equals( "bannershowonehalf" ) || type.trim().equals( "bannershowallhalf" ) )
      {
        if ( ( (String)contentDataMap.get( "WIDTH" ) ).equals( "half" ) )
        {
          bannerAds.add( content );
        }
      }
      else // Pick all Full banners
      {
        if ( ( (String)contentDataMap.get( "WIDTH" ) ).equals( "full" ) )
        {
          bannerAds.add( content );
        }
      }
    }
    return bannerAds;
  }

  /**
     * Filters the List based on type.
     * @return  bannerAds
     */
  private List<Content> filterbannersBasedOnLocation( List<Content> genericBannerAds, String location )
  {
    List<Content> bannerAds = new ArrayList<Content>();
    Content content = null;
    Map contentDataMap = null;
    for ( int i = 0; i < genericBannerAds.size(); i++ )
    {
      content = (Content)genericBannerAds.get( i );
      contentDataMap = content.getContentDataMap();
      // Pick all half banners
      if ( contentDataMap.get( "LOCATION" ) != null && ( (String)contentDataMap.get( "LOCATION" ) ).equals( location ) )
      {
        bannerAds.add( content );
      }
    }
    return bannerAds;
  }

  /**
     * Returns a random banner ad from the specified Content Manager asset. Returns null if the asset
     * defines no banner ads.
     * 
     * @param contentKey a key to a Content Manager asset that defines banner ads.
     * @return a random banner ad from the specified Content Manager asset.
     */
  private Content getRandomBannerAd( List bannersList )
  {
    Content bannerAd = null;
    if ( bannersList.size() > 0 )
    {
      int randomIndex = new Random().nextInt( bannersList.size() );
      bannerAd = (Content)bannersList.get( randomIndex );
    }
    return bannerAd;
  }

  /**
   * For when you use banner ad LINK_URL in CM to go to an internal action
   * with query strig Eg: Left banner ad to submit recognition promotion
   * 
   * @param linkInCM
   * @return a url with encrpted query string using client state
   */

  private String encryptClientState( String linkInCM, HttpServletRequest request )
  {
    Map clientStateParameterMap = new HashMap();
    String encryptedUrl = "";
    if ( linkInCM.indexOf( "?" ) > 0 )
    {
      String pagePath = linkInCM.substring( 0, linkInCM.indexOf( "?" ) );
      String queryString = linkInCM.substring( linkInCM.indexOf( "?" ) + 1 );
      String[] parameters = queryString.split( "&" );
      // single parameter
      if ( parameters.length == 1 )
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
      if ( pagePath.indexOf( "http" ) == -1 )
      {
        if ( !pagePath.startsWith( "/" ) )
        {
          pagePath = "/" + pagePath;

        }
        encryptedUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), pagePath, clientStateParameterMap );
      }
      else
      {

        encryptedUrl = ClientStateUtils.generateEncodedLink( "", pagePath, clientStateParameterMap );
      }
    }
    else
    {
      encryptedUrl = checkAndAppendContextPath( linkInCM, request );
    }
    return encryptedUrl;
  }

  /**
   * For when you use banner ad LINK_URL in CM to go to an internal action,
   * will prepend application context path to relative LINK_URL
   * 
   * @param linkInCM
   * @return a url with either prepended application context path for relative link 
   *        or the original absolute link.
   */

  private String checkAndAppendContextPath( String linkInCM, HttpServletRequest request )
  {
    String contextPathURL = linkInCM;
    // If link is relative, NOT absolute
    if ( null != linkInCM && linkInCM.indexOf( "http" ) == -1 )
    {
      // Prepend context path to the link
      StringBuffer buffer = new StringBuffer( RequestUtils.getBaseURI( request ) );
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
}
