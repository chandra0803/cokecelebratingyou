/*
 * OperationsFactory.java
 *
 * Created on June 3, 2003, 2:10 PM
 */

package com.biperf.core.utils.fileprocessing;

import java.util.HashMap;

/**
 * 
 * @author babu
 */
public class OperationsFactory
{
  ColumnLevelOperation columnLevelOperation;
  public static final String VALIDATION_MINL = "MINL";
  public static final String VALIDATION_MAXL = "MAXL";
  public static final String VALIDATION_MINV = "MINV";
  public static final String VALIDATION_MAXV = "MAXV";
  public static final String VALIDATION_REQD = "REQD";
  public static final String VALIDATION_DFTR = "DFTR";
  public static final String VALIDATION_DPST = "DPST";
  public static final String VALIDATION_DAFT = "DAFT";
  public static final String VALIDATION_DBFR = "DBFR";
  public static final String VALIDATION_PTRN = "PTRN";
  public static final String VALIDATION_CSLV = "CSLV";

  public static final String FORMAT_UPCS = "UPCS";
  public static final String FORMAT_LOCS = "LOCS";
  public static final String FORMAT_MXCS = "MXCS";
  public static final String FORMAT_INSV = "INSV";
  public static final String FORMAT_DFTR = "DFTR";
  public static final String FORMAT_DPST = "DPST";
  public static final String FORMAT_DAFT = "DAFT";
  public static final String FORMAT_DBFR = "DBFR";
  public static final String FORMAT_RMCH = "RMCH";
  public static final String FORMAT_TRIM = "TRIM";
  public static final String FORMAT_INSN = "INSN";

  private static HashMap<String, Operation> fileDetailOperations = new HashMap<String, Operation>();

  static
  {
    fileDetailOperations.put( ColumnLevelOperation.VALIDATION_MINL.toString(), new FileDetailOpMinLength() );
    fileDetailOperations.put( ColumnLevelOperation.VALIDATION_MAXL.toString(), new FileDetailOpMaxLength() );
    fileDetailOperations.put( ColumnLevelOperation.VALIDATION_MINV.toString(), new FileDetailOpMinValue() );
    fileDetailOperations.put( ColumnLevelOperation.VALIDATION_MAXV.toString(), new FileDetailOpMaxValue() );
    fileDetailOperations.put( ColumnLevelOperation.VALIDATION_REQD.toString(), new FileDetailOpReqValue() );
    fileDetailOperations.put( ColumnLevelOperation.VALIDATION_CSLV.toString(), new FileDetailOpValueFromList() );
    fileDetailOperations.put( ColumnLevelOperation.FORMAT_UPCS.toString(), new FileDetailOpUpCase() );
    fileDetailOperations.put( ColumnLevelOperation.FORMAT_LOCS.toString(), new FileDetailOpLoCase() );
    fileDetailOperations.put( ColumnLevelOperation.FORMAT_MXCS.toString(), new FileDetailOpMxCase() );
    fileDetailOperations.put( ColumnLevelOperation.FORMAT_INSV.toString(), new FileDetailOpInsCol() );
    fileDetailOperations.put( ColumnLevelOperation.FORMAT_DFTR.toString(), new FileDetailOpDateFuture() );
    fileDetailOperations.put( ColumnLevelOperation.FORMAT_DPST.toString(), new FileDetailOpDatePast() );
    fileDetailOperations.put( ColumnLevelOperation.FORMAT_DAFT.toString(), new FileDetailOpDateAfter() );
    fileDetailOperations.put( ColumnLevelOperation.FORMAT_DBFR.toString(), new FileDetailOpDateBefore() );
    fileDetailOperations.put( ColumnLevelOperation.FORMAT_RMCH.toString(), new FileDetailOpRemoveChar() );
    fileDetailOperations.put( ColumnLevelOperation.FORMAT_TRIM.toString(), new FileDetailOpTrim() );
    fileDetailOperations.put( ColumnLevelOperation.FORMAT_INSN.toString(), new FileDetailOpNullInsVal() );
  }

  /** Creates a new instance of OperationsFactory */
  public OperationsFactory()
  {
  }

  public static Operation getDetailOperation( String operationValueText )
  {
    if ( operationValueText == null || operationValueText.trim().equals( "" ) )
    {
      return null;
    }
    else
    {
      return (Operation)fileDetailOperations.get( operationValueText );
    }
  }

}
