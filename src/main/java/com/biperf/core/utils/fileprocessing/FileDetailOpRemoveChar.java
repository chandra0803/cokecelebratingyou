/*
   File: FileDetailOpRemoveChar.java
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

public class FileDetailOpRemoveChar implements Operation
{

  private static final char[] ALLOWED_CHARACTERS = { '$', '"', '&', '/', '\\', '(', ')', '-', '%', '#' };

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
      char[] removeChars = operationInfo.getPattern().toCharArray();
      for ( int loopCount = 0; loopCount < removeChars.length; loopCount++ )
      {
        boolean found = false;
        for ( int innerLoopCount = 0; innerLoopCount < ALLOWED_CHARACTERS.length; innerLoopCount++ )
        {
          if ( removeChars[loopCount] == ALLOWED_CHARACTERS[innerLoopCount] )
          {
            found = true;
          }
        }
        if ( !found )
        {
          // This character \u0b85 is chosen as an escape character
          removeChars[loopCount] = '\u0b85';
        }
      }
      for ( int loopCount = 0; loopCount < removeChars.length; loopCount++ )
      {
        if ( removeChars[loopCount] != '\u0b85' )
        {
          while ( inputValue.indexOf( removeChars[loopCount] ) >= 0 )
          {
            if ( inputValue.length() > inputValue.indexOf( removeChars[loopCount] ) + 1 )
            {
              inputValue = inputValue.substring( 0, inputValue.indexOf( removeChars[loopCount] ) ) + inputValue.substring( inputValue.indexOf( removeChars[loopCount] ) + 1 );
            }
            else
            {
              inputValue = inputValue.substring( 0, inputValue.indexOf( removeChars[loopCount] ) );
            }
          }
        }
      }
    }
    return inputValue;
  }
}
