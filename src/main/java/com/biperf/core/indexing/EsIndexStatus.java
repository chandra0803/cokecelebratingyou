/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/sysadmin/CacheStatsForm.java,v $
 */

package com.biperf.core.indexing;

import java.io.Serializable;

@SuppressWarnings( "serial" )
public class EsIndexStatus implements Serializable
{
  private String name;
  private String endPoint = null;
  private IndexExistance exists = IndexExistance.UNKNOWN;
  // private IndexHealth health = IndexHealth.UNKNOWN;
  // private IndexStatus status = IndexStatus.UNKNOWN;
  private float storeSize = -1;
  // private String primaryStoreSize = null;
  private int documentCount = -1;
  private int documentsDeleted = -1;
  private int searchCount = -1;
  private long queryTimeInMillis = -1;
  private long fetchTimeInMillis = -1;

  private boolean isSecurityCorrect = true;

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  /*
   * public IndexHealth getHealth() { return health; } public void setHealth( IndexHealth health ) {
   * this.health = health; } public IndexStatus getStatus() { return status; } public void
   * setStatus( IndexStatus status ) { this.status = status; }
   */
  public float getStoreSize()
  {
    return storeSize;
  }

  public float getStoreSizeKb()
  {
    return storeSize / 1000;
  }

  public float getStoreSizeMb()
  {
    return ( storeSize / 1000 ) / 1000;
  }

  public void setStoreSize( float storeSize )
  {
    this.storeSize = storeSize;
  }

  // public String getPrimaryStoreSize()
  // {
  // return primaryStoreSize;
  // }
  //
  // public void setPrimaryStoreSize( String primaryStoreSize )
  // {
  // this.primaryStoreSize = primaryStoreSize;
  // }

  public int getDocumentCount()
  {
    return documentCount;
  }

  public void setDocumentCount( int documentCount )
  {
    this.documentCount = documentCount;
  }

  public int getDocumentsDeleted()
  {
    return documentsDeleted;
  }

  public void setDocumentsDeleted( int documentsDeleted )
  {
    this.documentsDeleted = documentsDeleted;
  }

  public boolean isSecurityCorrect()
  {
    return isSecurityCorrect;
  }

  public void setSecurityCorrect( boolean isSecurityCorrect )
  {
    this.isSecurityCorrect = isSecurityCorrect;
  }

  public IndexExistance getExists()
  {
    return exists;
  }

  public void setExists( IndexExistance exists )
  {
    this.exists = exists;
  }

  public String getEndPoint()
  {
    return endPoint;
  }

  public void setEndPoint( String endPoint )
  {
    this.endPoint = endPoint;
  }

  public int getSearchCount()
  {
    return searchCount;
  }

  public void setSearchCount( int searchCount )
  {
    this.searchCount = searchCount;
  }

  public long getQueryTimeInMillis()
  {
    return queryTimeInMillis;
  }

  public void setQueryTimeInMillis( long queryTimeInMillis )
  {
    this.queryTimeInMillis = queryTimeInMillis;
  }

  public long getFetchTimeInMillis()
  {
    return fetchTimeInMillis;
  }

  public void setFetchTimeInMillis( long fetchTimeInMillis )
  {
    this.fetchTimeInMillis = fetchTimeInMillis;
  }
}
