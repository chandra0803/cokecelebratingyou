
package com.biperf.core.domain.ots;

import com.biperf.core.domain.BaseDomain;

public class OTSBatch extends BaseDomain implements Cloneable

{
  private Long batchNumber;
  private String cmAssetCode;
  private OTSProgram otsProgram;

  public static final String OTS_BATCH_NAME_ASSET_PREFIX = "ots_batch_name.";
  public static final String OTS_BATCH_NAME_SECTION_CODE = "ots_batch_name";
  public static final String OTS_BATCH_NAME_ASSET_TYPE_NAME = "_OTS_BATCH_NAME_DATA";
  public static final String OTS_BATCH_NAME_KEY_DESC = "OTS Batch Name";
  public static final String OTS_BATCH_NAME_KEY_PREFIX = "OTS_BATCH_NAME_";

  public Long getBatchNumber()
  {
    return batchNumber;
  }

  public void setBatchNumber( Long batchNumber )
  {
    this.batchNumber = batchNumber;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public OTSProgram getOtsProgram()
  {
    return otsProgram;
  }

  public void setOtsProgram( OTSProgram otsProgram )
  {
    this.otsProgram = otsProgram;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    return false;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( batchNumber == null ? 0 : batchNumber.hashCode() );
    return result;
  }
}
