/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.imageservice;

import com.biperf.core.exception.ServiceErrorException;

/**
 * 
 * @author sivanand
 * @since Jan 31, 2019
 * @version 1.0
 */
public interface ImageServiceRepository
{
  public static final String BEAN_NAME = "imageServiceRepository";

  public String uploadImage( String imageData, String id, String imagePrefix ) throws ServiceErrorException;

}
