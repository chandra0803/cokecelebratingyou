/*
 * (c) 2014 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/ws/rest/sea/Attic/BaseService.java,v $
 */
package com.biperf.core.ui.ws.rest.sea;

import com.biperf.core.service.SAO;
import com.biperf.core.utils.BeanLocator;

/**
 * TODO Javadoc for BaseService.
 * 
 * @author Ravi Kancherla
 * @since Oct 27, 2014
 * @version 1.0
 */
public class BaseService
{
  
  /**
   * Bean location through BeanLocator look-up.
   * 
   * @param beanName
   * @return SAO
   */
  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

}
