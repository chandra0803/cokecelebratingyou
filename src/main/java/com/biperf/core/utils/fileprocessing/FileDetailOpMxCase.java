/*
   File: FileDetailOpMxCase.java
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

public class FileDetailOpMxCase implements Operation
{

  public String getGroup()
  {
    return Operation.OPERATION_GROUP_FORMAT;
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
   * This method converts the input value into requested format
   * @param inputValue
   * @param FileDetailInfo File Detail Info
   * @param OperationInfo Operation Info
   * @return String converted string
   * @see com.biperf.datacenterautomation.process.Operation#perform(java.lang.String ,
   * java.lang.String, com.biperf.datacenterautomation.value.FileDetailInfo,
   * com.biperf.datacenterautomation.value.OperationInfo)
   */
  public String perform( String inputValue, FileDetailInfo fileDetailInfo, OperationInfo operationInfo )
  {
    if ( inputValue != null )
    {
      StringBuffer t_sbInputValue = new StringBuffer();
      char t_cInputValue = ' ';
      for ( int i = 0; i < inputValue.length(); i++ )
      {
        t_cInputValue = inputValue.charAt( i );
        // Convert first character or any character followed by space
        if ( i == 0 )
        {
          t_sbInputValue.append( Character.toUpperCase( t_cInputValue ) );
        }
        else if ( i == 0 || Character.isWhitespace( t_cInputValue ) && i + 1 < inputValue.length() )
        {
          t_sbInputValue.append( t_cInputValue );
          t_cInputValue = inputValue.charAt( ++i ); // Move to next
                                                    // character
          t_sbInputValue.append( Character.toUpperCase( t_cInputValue ) );
        }
        else
        {
          t_sbInputValue.append( Character.toLowerCase( t_cInputValue ) );
        }

      }
      return t_sbInputValue.toString();
    }
    return "";
  }
}
