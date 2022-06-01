/*
 *
 *  File : FileDetailInfoOutputOrderComparator.java
 *
 *  (c)2003 BI, Inc.
 *
 *  All Rights Reserved.
 *
 *  Change History:
 *
 *  Date      Release   Developer          Changes
 *  --------  --------  -----------------  --------------------------------
 *
 */

package com.biperf.core.value.fileprocessing;

/**
 * This class is used for sorting the file detail thru output order
 * 
 * @author Prabu Babu
 */
public class FileDetailInfoOutputOrderComparator implements java.util.Comparator<FileDetailInfo>
{

  public int compare( FileDetailInfo obj, FileDetailInfo obj1 )
  {
    if ( obj != null && obj1 != null )
    {
      if ( ( (FileDetailInfo)obj ).getOutputOrder() == ( (FileDetailInfo)obj1 ).getOutputOrder() )
      {
        return 0;
      }
      else if ( ( (FileDetailInfo)obj ).getOutputOrder() > ( (FileDetailInfo)obj1 ).getOutputOrder() )
      {
        return 1;
      }
      else
      {
        return -1;
      }
    }
    else
    {
      return -2;
    }
  }

}
