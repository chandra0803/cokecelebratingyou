/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/throwdown/scheduler/Attic/ThrowdownMatchSchedulerFactory.java,v $
 */

package com.biperf.core.service.throwdown.scheduler;

public interface ThrowdownMatchSchedulerFactory
{
  public static final String BEAN_NAME = "throwdownMatchSchedulerFactory";

  public static final String RANDOM = "random";

  public ThrowdownMatchScheduler getThrowdownMatchSchedulerService();
}
