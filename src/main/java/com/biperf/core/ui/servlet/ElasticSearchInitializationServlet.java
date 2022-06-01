/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/servlet/PromotionCacheInitializationServlet.java,v $
 */

package com.biperf.core.ui.servlet;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.indexing.BIElasticSearchAdminService;
import com.biperf.core.indexing.BIIndex;
import com.biperf.core.indexing.impl.ESClientFactory;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.util.ContentReaderManager;

@SuppressWarnings( "serial" )
public class ElasticSearchInitializationServlet extends CMSAwareBaseServlet
{
  private static Log log = LogFactory.getLog( ElasticSearchInitializationServlet.class );

  public void init() throws ServletException
  {
    try
    {
      super.init();
      initESClientfactory();
      initCountryNameList();
    }
    catch( Exception e )
    {
      log.error( e.getMessage(), e );
    }
    finally
    {
      ContentReaderManager.removeContentReader();
    }
  }

  private void initCountryNameList()
  {
    ( (CountryService)com.biperf.core.utils.BeanLocator.getBean( CountryService.BEAN_NAME ) ).getCountryNameList();
  }

  private void initESClientfactory() throws ServletException
  {
    BIIndex index = (BIIndex)BeanLocator.getBean( BIIndex.class );
    index.setName( getServletContext().getContextPath().substring( 1 ) );

    SystemVariableService systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
    PropertySetItem propertyItem = systemVariableService.getPropertyByName( SystemVariableService.ELASTICSEARCH_ENABLED );

    if ( propertyItem == null || !propertyItem.getBooleanVal() )
    {
      return;
    }

    ESClientFactory bean = (ESClientFactory)BeanLocator.getBean( ESClientFactory.class );
    try
    {
      bean.getInstance();
      // upon initial login, check if the index exists. if it doesn't, create it.
      BIElasticSearchAdminService esAdmin = (BIElasticSearchAdminService)BeanLocator.getBean( BIElasticSearchAdminService.class );
      if ( !esAdmin.indexExists() )
      {
        esAdmin.createIndex();
        System.out.println( "Initializing the ElasticSearch Index - note that this is not populated yet" );
      }
    }
    catch( Exception e )
    {
      e.printStackTrace();
      System.out.println( "Elastic search configuration/mapping not done" );
    }
  }
}
