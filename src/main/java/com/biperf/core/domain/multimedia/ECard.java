/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/multimedia/ECard.java,v $
 */

package com.biperf.core.domain.multimedia;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.utils.UserManager;

/**
 * Image.
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
 * <td>zahler</td>
 * <td>Oct 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ECard extends Card
{

  private static final Log logger = LogFactory.getLog( ECard.class );

  public static final String IMAGE_SERVICE_LARGE_IMG = "/images/size/432/432/";
  public static final String IMAGE_SERVICE_SMALL_IMG = "/images/size/144/144/";
  public static final String IMAGE_SERVICE_FLASH_IMG = "/images/size/864/864/";

  private static final long serialVersionUID = 1L;

  public static String SWF_EXTENSION = "swf";
  public static final String CARDS_FOLDER = "/assets/img/cards/";
  public static final String CERTIFICATES_FOLDER = "/assets/img/certificates/";

  private String flashName;
  private boolean translatable;
  protected Set<EcardLocale> ecardLocales = new HashSet<EcardLocale>();
  private String tempname;
  private String smallImageNameLocale;
  private String largeImageNameLocale;

  // Transient field for sorting by name (natural order) - the number value of numerically named
  // cards
  private int numericalName;

  public String getFlashName()
  {
    return flashName;
  }

  public String getFlashNameLocale( String recipientLocale )
  {
    String ecardFlashNameWithLocale = getFlashName();
    if ( isTranslatable() )
    {
      for ( EcardLocale ecardLocale : ecardLocales )
      {
        if ( ecardLocale.getLocale() != null && ecardLocale.getLocale().equalsIgnoreCase( UserManager.getLocale().toString() ) )
        {
          if ( StringUtils.isNotEmpty( ecardLocale.getDisplayName() ) )
          {
            try
            {
              URL aURL = new URL( ecardLocale.getDisplayName() );
              String urlPath = aURL.getPath();
              String[] splitArr = urlPath.split( "/" );
              ecardFlashNameWithLocale = MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + IMAGE_SERVICE_FLASH_IMG + splitArr[splitArr.length - 1];
            }
            catch( MalformedURLException e )
            {
              logger.error( e.getMessage(), e );
            }
          }
          break;
        }
      }
      return ecardFlashNameWithLocale;
    }
    else
    {
      return getFlashName();
    }
  }

  public String getLargeImageNameLocale()
  {
    if ( isTranslatable() )
    {
      String ecardNameWithLocale = getLargeImageName();
      for ( EcardLocale ecardLocale : ecardLocales )
      {
        if ( ecardLocale.getLocale() != null && ecardLocale.getLocale().equalsIgnoreCase( UserManager.getLocale().toString() ) )
        {
          if ( StringUtils.isNotEmpty( ecardLocale.getDisplayName() ) )
          {
            try
            {
              URL aURL = new URL( ecardLocale.getDisplayName() );
              String urlPath = aURL.getPath();
              String[] splitArr = urlPath.split( "/" );
              ecardNameWithLocale = MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + IMAGE_SERVICE_LARGE_IMG + splitArr[splitArr.length - 1];
            }
            catch( MalformedURLException e )
            {
              logger.error( e.getMessage(), e );
            }
          }
          break;
        }
      }
      return ecardNameWithLocale;
    }
    else
    {
      return getLargeImageName();
    }
  }

  public String getLargeImageNameLocale( String locale )
  {
    String replaceString = UserManager.getLocale().toString();
    if ( locale != null && !locale.equals( "" ) )
    {
      replaceString = locale;
    }
    if ( isTranslatable() )
    {
      String ecardNameWithLocale = getLargeImageName();
      for ( EcardLocale ecardLocale : ecardLocales )
      {
        if ( ecardLocale.getLocale() != null && ecardLocale.getLocale().equalsIgnoreCase( replaceString ) )
        {
          if ( StringUtils.isNotEmpty( ecardLocale.getDisplayName() ) )
          {
            try
            {
              URL aURL = new URL( ecardLocale.getDisplayName() );
              String urlPath = aURL.getPath();
              String[] splitArr = urlPath.split( "/" );
              ecardNameWithLocale = MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + IMAGE_SERVICE_LARGE_IMG + splitArr[splitArr.length - 1];
            }
            catch( MalformedURLException e )
            {
              logger.error( e.getMessage(), e );
            }
          }
          break;
        }
      }
      return ecardNameWithLocale;
    }
    else
    {
      return getLargeImageName();
    }
  }

  public String getSmallImageNameLocale()
  {
    if ( isTranslatable() )
    {
      String ecardNameWithLocale = getSmallImageName();
      for ( EcardLocale ecardLocale : ecardLocales )
      {
        if ( ecardLocale.getLocale() != null && ecardLocale.getLocale().equalsIgnoreCase( UserManager.getLocale().toString() ) )
        {
          if ( StringUtils.isNotEmpty( ecardLocale.getDisplayName() ) )
          {
            try
            {
              URL aURL = new URL( ecardLocale.getDisplayName() );
              String urlPath = aURL.getPath();
              String[] splitArr = urlPath.split( "/" );
              ecardNameWithLocale = MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + IMAGE_SERVICE_SMALL_IMG + splitArr[splitArr.length - 1];
            }
            catch( MalformedURLException e )
            {
              logger.error( e.getMessage(), e );
            }
          }
          break;
        }
      }
      return ecardNameWithLocale;
    }
    else
    {
      return getSmallImageName();
    }
  }

  public void setFlashName( String flashName )
  {
    this.flashName = flashName;
  }

  public boolean isFlashNeeded()
  {
    return this.flashName.endsWith( SWF_EXTENSION );
  }

  public boolean isSwf()
  {
    return this.flashName.endsWith( SWF_EXTENSION );
  }

  public void setTranslatable( boolean translatable )
  {
    this.translatable = translatable;
  }

  public boolean isTranslatable()
  {
    return translatable;
  }

  public Set<EcardLocale> getEcardLocales()
  {
    return ecardLocales;
  }

  public void setEcardLocales( Set<EcardLocale> ecardLocales )
  {
    this.ecardLocales = ecardLocales;
  }

  public String getTempname()
  {
    if ( isTranslatable() )
    {
      String ecardNameTemp = super.getName();
      for ( EcardLocale ecardLocale : ecardLocales )
      {
        if ( ecardLocale.getLocale() != null && ecardLocale.getLocale().equalsIgnoreCase( UserManager.getLocale().toString() ) )
        {
          ecardNameTemp = ecardLocale.getDisplayName();
          break;
        }
      }
      return ecardNameTemp;
    }
    else
    {
      return super.getName();
    }

  }

  public void setTempname( String name )
  {
    this.tempname = name;
  }

  public String getDisplayNamebyLocale( String locale )
  {
    String replaceString = UserManager.getLocale().toString();
    String returnval = getName();
    if ( locale != null && !locale.equals( "" ) )
    {
      replaceString = locale;
    }
    for ( EcardLocale ecardLocale : ecardLocales )
    {
      if ( ecardLocale.getLocale() != null && ecardLocale.getLocale().equalsIgnoreCase( replaceString ) )
      {
        returnval = ecardLocale.getDisplayName();
        break;
      }
    }
    return returnval;
  }

  public int getNumericalName()
  {
    return numericalName;
  }

  public void setNumericalName( int numericalName )
  {
    this.numericalName = numericalName;
  }

  public void addEcardLocales( Set<EcardLocale> ecardLocales )
  {
    // this.ecardLocales.clear();
    this.ecardLocales.addAll( ecardLocales );
  }

}
