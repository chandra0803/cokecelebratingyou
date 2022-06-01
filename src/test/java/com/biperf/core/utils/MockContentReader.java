/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/utils/MockContentReader.java,v $
 *
 */

package com.biperf.core.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import com.objectpartners.cms.domain.Asset;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.enums.ContentStatusEnum;
import com.objectpartners.cms.exception.CmsServiceException;
import com.objectpartners.cms.service.ContentReader;

/**
 * MockContentReader <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Sep 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class MockContentReader implements ContentReader
{
  HashMap contentMap = new HashMap();
  Content content = new Content();

  /*
   * Get content given cmAssetCode If a content object does not exist (ie no content loaded in via
   * setContent) return empty content object
   */
  public Object getContent( String cmAssetCode )
  {
    if ( contentMap.containsKey( cmAssetCode ) )
    {
      return contentMap.get( cmAssetCode );
    }
    return content;
  }

  public Object getContent( String s, String s1 )
  {
    return getContent( s );
  }

  public Object getContent( String s, String s1, Locale l )
  {
    return getContent( s );
  }

  public Object getContent( String s, Locale l )
  {
    return getContent( s );
  }

  public void setContent( Content content )
  {
    this.content = content;
  }

  public void addContent( Object assetCode, Content content )
  {

    this.contentMap.put( assetCode, content );
  }

  public Collection getAssetsByType( String s )
  {
    return Collections.EMPTY_LIST;
  }

  public Content getContent( Long contentId )
  {
    return null;
  }

  public String getApplicationCode()
  {
    return "beacon";
  }

  public void setApplicationCode( String s )
  {
    // mock setter - nothing to set
  }

  public Set getAudienceNames()
  {
    return Collections.EMPTY_SET;
  }

  public void setAudienceNames( Set set )
  {
    // mock setter - nothing to set
  }

  public List getContentStatusList()
  {
    return Collections.EMPTY_LIST;
  }

  public void setContentStatusList( List list )
  {
    // mock setter - nothing to set
  }

  public Locale getLocale()
  {
    return Locale.ENGLISH;
  }

  public void setLocale( Locale locale )
  {
    // mock setter - nothing to set
  }

  public void setApplicationContext( ApplicationContext context )
  {
    // mock setter - nothing to set
  }

  public ApplicationContext getApplicationContext()
  {
    return null;
  }

  public Asset getAsset( String s )
  {
    return new Asset();
  }

  /**
   * Flush the cache of this asset. The content has changed.
   * 
   * @param assetCode
   */
  public void contentChangedEvent( String assetCode, String appCode, Locale locale )
  {
    // do nothing;
  }

  /**
   * Flush the cache of this asset. The asset has changed.
   * 
   * @param assetCode
   */
  public void assetChangedEvent( String assetCode )
  {
    // do nothing;
  }

  public ContentStatusEnum getContentStatus()
  {
    return null;
  }

  public void setContentStatus( ContentStatusEnum arg0 ) throws CmsServiceException
  {
  }

  public String getParentApplicationCode()
  {
    return null;
  }

  public void setParentApplicationCode( String applicationCode )
  {
  }

}