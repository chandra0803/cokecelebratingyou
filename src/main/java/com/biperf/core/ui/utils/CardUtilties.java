/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/utils/CardUtilties.java,v $
 */

package com.biperf.core.ui.utils;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.multimedia.ECard;

/**
 * Interface that defined all the Action level constants.
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
 * <td>crosenquest</td>
 * <td>Mar 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CardUtilties
{
  public static final String CERTIFICATE_PATH = "/assets/img/certificates/";
  public static final String MOBILE_CARD_PATH = "/assets/img/cards/mobile/";

  public static String getSizedFlashRequestString( ECard eCard, HttpServletRequest request, int width, int height )
  {
    StringBuffer stringBuffer = new StringBuffer();
    String recepientLocale = "";// request.getParameter( "cmsLocaleCode" );// assuming that pax
                                // should not login selecting language locale in login page
    stringBuffer.append( "<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"" + RequestUtils.getProtocol( request )
        + "://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\" height=\"" + String.valueOf( height ) + "\" width=\"" + String.valueOf( width ) + "\"> " );
    stringBuffer.append( "<param name=\"scale\" value=\"exactfit\"> " );
    stringBuffer.append( "<param name=\"loop\" value=\"false\"> " );
    stringBuffer.append( "<param name=\"movie\" value=\"" );
    stringBuffer.append( eCard.getFlashNameLocale( recepientLocale ) );
    stringBuffer.append( "\"> " );
    stringBuffer.append( "<param name=\"quality\" value=\"autohigh\"> " );
    stringBuffer.append( "<param name=\"play\" value=\"true\"> " );
    stringBuffer.append( "<embed height=\"" + String.valueOf( height ) + "\" pluginspage=\"" + RequestUtils.getProtocol( request ) + "://www.macromedia.com/go/getflashplayer\" src=\"" );
    stringBuffer.append( eCard.getFlashNameLocale( recepientLocale ) );
    stringBuffer.append( "\" " );
    stringBuffer.append( "type=\"application/x-shockwave-flash\" width=\"" + String.valueOf( width ) + "\" quality=\"autohigh\" play=\"true\" loop=\"false\" scale=\"exactfit\"> " );
    stringBuffer.append( "</object>" );

    return stringBuffer.toString();
  }

  public static String getJavaScriptEscapedCardPopupContent( ECard card, HttpServletRequest request )
  {
    String content = null;
    if ( card.isFlashNeeded() )
    {
      content = getFlashRequestString( card, request );
    }
    else
    {
      content = getStaticRequestString( card, request );
    }

    return content.replaceAll( "\"", "'" );
  }

  public static String getJavaScriptEscapedCardPopupContent( String imageName, HttpServletRequest request, boolean isCertificate )
  {
    String content = null;
    content = getStaticRequestString( imageName, request, isCertificate, false );

    return content.replaceAll( "\"", "'" );
  }

  public static String getFlashRequestString( ECard eCard, HttpServletRequest request )
  {
    return getSizedFlashRequestString( eCard, request, 360, 360 );
  }

  public static String getStaticRequestString( ECard eCard, HttpServletRequest request )
  {
    return getSizedStaticRequestString( eCard, request, 360, 360 );
  }

  public static String getStaticRequestString( String imageName, HttpServletRequest request, boolean isCertificate, boolean isMobile )
  {
    return getSizedStaticRequestString( imageName, request, 360, 360, isCertificate, isMobile );
  }

  public static String getSizedStaticRequestString( ECard eCard, HttpServletRequest request, int width, int height )
  {
    String cmsLocaleAvailabeinUrl = request.getParameter( "cmsLocaleCode" );
    if ( cmsLocaleAvailabeinUrl != null && cmsLocaleAvailabeinUrl.length() > 1 )
    {
      return getSizedStaticRequestString( eCard.getLargeImageNameLocale( cmsLocaleAvailabeinUrl ), request, width, height, false, eCard.isMobile() );
    }
    else
    {
      return getSizedStaticRequestString( eCard.getLargeImageNameLocale(), request, width, height, false, eCard.isMobile() );
    }
  }

  public static String getSizedStaticRequestString( String imageName, HttpServletRequest request, int width, int height, boolean isCertificate, boolean isMobile )
  {
    StringBuffer stringBuffer = new StringBuffer();

    stringBuffer.append( "<img src=\"" );
    if ( isCertificate )
    {
      stringBuffer.append( RequestUtils.getBaseURI( request ) );
      stringBuffer.append( CERTIFICATE_PATH );
      stringBuffer.append( imageName );
    }
    else if ( isMobile )
    {
      stringBuffer.append( RequestUtils.getBaseURI( request ) );
      stringBuffer.append( MOBILE_CARD_PATH );
      stringBuffer.append( imageName );
    }
    else
    {
      stringBuffer.append( imageName );
    }
    stringBuffer.append( "\" " );
    stringBuffer.append( "WIDTH=\"" + String.valueOf( width ) + "\" HEIGHT=\"" + String.valueOf( height ) + "\" usemap=\"#script\" BORDER=\"0\">" );

    return stringBuffer.toString();
  }

  public static String getSizedStaticG5CardString( ECard eCard, HttpServletRequest request )
  {
    String cmsLocaleAvailabeinUrl = request.getParameter( "cmsLocaleCode" );
    if ( cmsLocaleAvailabeinUrl != null && cmsLocaleAvailabeinUrl.length() > 1 )
    {
      return getSizedStaticG5CardString( eCard.getLargeImageNameLocale( cmsLocaleAvailabeinUrl ), request, eCard.isMobile() );
    }
    else
    {
      return getSizedStaticG5CardString( eCard.getLargeImageNameLocale(), request, eCard.isMobile() );
    }
  }

  public static String getSizedStaticG5CardString( String imageName, HttpServletRequest request, boolean isMobile )
  {
    StringBuffer stringBuffer = new StringBuffer();

    if ( isMobile )
    {
      stringBuffer.append( RequestUtils.getBaseURI( request ) );
      stringBuffer.append( MOBILE_CARD_PATH );
      stringBuffer.append( imageName );
    }
    else
    {
      stringBuffer.append( imageName );
    }

    return stringBuffer.toString();
  }

}
