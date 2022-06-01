/*
   File: FileDetailOpMaxLength.java
   (c)2001 BI, Inc.
  
   All Rights Reserved.
  
   Author       Date      Version  Comments
   -----------  --------  -------  -------------------
   Mandy Votis  01/07/2005  1.6      Initial Creation
 */

package com.biperf.core.utils.fileprocessing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import com.biperf.core.value.fileprocessing.FileDetailInfo;
import com.biperf.core.value.fileprocessing.FileInfo;
import com.biperf.core.value.fileprocessing.OperationInfo;
import com.biperf.core.value.fileprocessing.OperationResultInfo;

public class FileDetailOpValueFromList implements Operation
{

  private static final String COMMA_DELIMITER = ",";

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
  public void perform( InputStream inputFile, File outputFolder, String prefix, FileInfo fileInfo, OperationResultInfo operationResultInfo, String userId ) throws IOException
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
      StringTokenizer st = new StringTokenizer( operationInfo.getValue(), COMMA_DELIMITER );
      while ( st.hasMoreTokens() )
      {
        if ( inputValue.equals( st.nextToken() ) )
        {
          return "true";
        }
      }
    }
    return "false";
  }
}
