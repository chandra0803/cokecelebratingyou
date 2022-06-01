
package com.biperf.core.utils.cms;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.utils.ApplicationContextFactory;
import com.objectpartners.cms.domain.enums.ContentStatusEnum;
import com.objectpartners.cms.exception.CmsServiceException;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.service.impl.ContentReaderImpl;
import com.objectpartners.cms.util.BeanLocator;
import com.objectpartners.cms.util.CmsConfiguration;
import com.objectpartners.cms.util.ContentReaderManager;

public class ContentReaderUtils
{
  public static void prepareContentReader()
  {
    CmsConfiguration cmsConfiguration = (CmsConfiguration)BeanLocator.getBean( "cmsConfiguration" );
    prepareContentReader( cmsConfiguration );
  }

  public static void prepareContentReader( CmsConfiguration cmsConfiguration )
  {
    ContentReader reader = null;
    // CMS Reader setup
    String applicationCode = cmsConfiguration.getDefaultApplication();
    String parentApplicationCode = cmsConfiguration.getDefaultParentApplication();
    Locale locale = cmsConfiguration.getDefaultLocale();
    ContentStatusEnum contentStatus = ContentStatusEnum.LIVE;
    Set audienceNames = new HashSet( cmsConfiguration.getDefaultAudienceNames() );
    try
    {
      reader = new ContentReaderImpl( applicationCode, parentApplicationCode, locale, locale, contentStatus, audienceNames );
      reader.setApplicationContext( ApplicationContextFactory.getApplicationContext() );
    }
    catch( CmsServiceException e )
    {
      throw new BeaconRuntimeException( e.getMessage(), e );
    }

    ContentReaderManager.setContentReader( reader );
  }

}
