/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/utils/MockContentReaderFactory.java,v $
 */

package com.biperf.core.utils;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import com.objectpartners.cms.domain.enums.ContentStatusEnum;
import com.objectpartners.cms.exception.CmsServiceException;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.service.impl.ContentReaderImpl;
import com.objectpartners.cms.util.CmsConfiguration;
import com.objectpartners.cms.util.ContentReaderFactory;

/**
 * MockContentReaderFactory is a content reader factory for use in testing. It creates
 * ContentReaders that are
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
 * <td>Brian Repko</td>
 * <td>Sep 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MockContentReaderFactory implements ContentReaderFactory
{

  private CmsConfiguration config;

  /**
   * Overridden from
   * 
   * @see com.objectpartners.cms.util.ContentReaderFactory#createContentReader(javax.servlet.http.HttpServletRequest)
   * @param request is the HttpServletRequest but this is not used
   * @return ContentReader
   * @throws CmsServiceException
   */
  public ContentReader createContentReader( HttpServletRequest request ) throws CmsServiceException
  {
    return new ContentReaderImpl( config.getDefaultApplication(),
                                  config.getDefaultParentApplication(),
                                  config.getDefaultLocale(),
                                  config.getDefaultLocale(),
                                  ContentStatusEnum.LIVE,
                                  new HashSet( config.getDefaultAudienceNames() ) );
  }

  /**
   * Overridden from
   * 
   * @see com.objectpartners.cms.util.ContentReaderFactory#setCmsConfiguration(com.objectpartners.cms.util.CmsConfiguration)
   * @param config
   */
  public void setCmsConfiguration( CmsConfiguration config )
  {
    this.config = config;
  }

}
