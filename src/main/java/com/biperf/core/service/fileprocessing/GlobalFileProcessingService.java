/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/fileprocessing/GlobalFileProcessingService.java,v $
 */

package com.biperf.core.service.fileprocessing;

import java.io.InputStream;

import com.biperf.core.service.SAO;
import com.biperf.core.value.fileprocessing.OperationResultInfo;

/**
 * ImportService.
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
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface GlobalFileProcessingService extends SAO
{
  public static final String BEAN_NAME = "globalFileProcessingService";

  public OperationResultInfo process( String type, String fileName, InputStream inputFile, String userId ) throws Exception;

}
