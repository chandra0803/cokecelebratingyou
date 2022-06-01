/*
   File: FileDetailOpReqValue.java
   (c)2001 BI, Inc.
  
   All Rights Reserved.
  
   Author       Date      Version  Comments
   -----------  --------  -------  -------------------
   Prabu Babu   02/23/2004  1.6      Initial Creation
 */

package com.biperf.core.utils.fileprocessing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.biperf.core.value.fileprocessing.FileDetailInfo;
import com.biperf.core.value.fileprocessing.FileInfo;
import com.biperf.core.value.fileprocessing.OperationInfo;
import com.biperf.core.value.fileprocessing.OperationResultInfo;

public class FileDetailOpReqValue implements Operation
{

  public String getGroup()
  {
    return Operation.OPERATION_GROUP_VALIDATION;
  }

  /*
   * This method is not implemented. Since this class is for File detail, this method returns null.
   * @see com.biperf.datacenterautomation.process.Operation#perform(java.io.File, java.io.File,
   * java.lang.String, com.biperf.datacenterautomation.value.FileInfo)
   */
  public void perform( File inputFile, File outputFolder, String prefix, FileInfo fileInfo, OperationResultInfo operationResultInfo ) throws IOException
  {
    return;
  }

  /*
   * This method is not implemented. Since this class is for File detail, this method returns null.
   * @see com.biperf.datacenterautomation.process.Operation#perform(java.io.File, java.io.File,
   * java.lang.String, com.biperf.datacenterautomation.value.FileInfo)
   */
  public void perform( InputStream inputFile, File outputFolder, String prefix, FileInfo fileInfo, OperationResultInfo operationResultInfo, String userID ) throws IOException
  {
    return;
  }

  /*
   * This method checks if the value is valid or not..If valid, it returns true, else false.
   * @param inputValue
   * @param FileDetailInfo File Detail Info
   * @param OperatoinInfo Operation Info
   * @return String "true" or "false"
   * @see com.biperf.datacenterautomation.process.Operation#perform(java.lang.String ,
   * java.lang.String, com.biperf.datacenterautomation.value.FileDetailInfo,
   * com.biperf.datacenterautomation.value.OperationInfo)
   */
  public String perform( String inputValue, FileDetailInfo fileDetailInfo, OperationInfo operationInfo )
  {
    if ( inputValue != null )
    {

      // Check for length
      if ( inputValue.trim().length() <= 0 )
      {
        return "false";
      }
      return "true";
    }
    return "false";
  }
}
