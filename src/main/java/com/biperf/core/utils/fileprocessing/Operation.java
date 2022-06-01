
package com.biperf.core.utils.fileprocessing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.biperf.core.value.fileprocessing.FileDetailInfo;
import com.biperf.core.value.fileprocessing.FileInfo;
import com.biperf.core.value.fileprocessing.OperationInfo;
import com.biperf.core.value.fileprocessing.OperationResultInfo;

public interface Operation
{
  public static final String UNIX_FORMAT = "UF";
  public static final String WINDOWS_FORMAT = "WF";
  public static final String REMOVE_1HEADER = "1H";
  public static final String REMOVE_2HEADER = "2H";
  public static final String REMOVE_3HEADER = "3H";
  public static final String REMOVE_DOUBLEQUOTES = "RDLQ";
  public static final String REMOVE_EMPTY_LINES = "RMLN";
  public static final String REMOVE_HEADER = "RMHD";
  public static final String REMOVE_TRAILER = "RMTR";
  public static final String VALUE_TYPE_DATE = "Date";
  public static final String VALUE_TYPE_INTEGER = "Integer";
  public static final String VALUE_TYPE_DECIMAL = "Decimal";

  public static final String OPERATION_GROUP_VALIDATION = "VALIDATION";
  public static final String OPERATION_GROUP_FORMAT = "FORMAT";

  public abstract String getGroup();

  /**
   * This method performs the operation and returns info object
   * 
   * @param File
   *            inputFile
   * @param File
   *            output folder
   * @param String
   *            Prefix
   * @param String
   *            pid
   * @param FileInfo
   *            file info
   * @return OperationResultInfo Result
   */
  public void perform( File inputFile, File outputFolder, String prefix, FileInfo fileInfo, OperationResultInfo operationResultInfo ) throws IOException, Exception;

  /**
   * This method performs the operation and returns info object
   * 
   * @param InputStream
   *            inputFile
   * @param File
   *            output folder
   * @param String
   *            Prefix
   * @param String
   *            pid
   * @param FileInfo
   *            file info
   * @return OperationResultInfo Result
   */
  public void perform( InputStream inputFile, File outputFolder, String prefix, FileInfo fileInfo, OperationResultInfo operationResultInfo, String userId ) throws IOException, Exception;

  /**
   * This method performs the operation and returns info object
   * 
   * @param File
   *            inputFile
   * @param File
   *            output folder
   * @param String
   *            Prefix
   * @param String
   *            pid
   * @param FileInfo
   *            file info
   * @return OperationResultInfo Result
   */
  public String perform( String inputValue, FileDetailInfo fileDetailInfo, OperationInfo operationInfo );
}
