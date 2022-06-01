
package com.biperf.core.strategy;

import com.amazonaws.services.s3.AmazonS3;

public interface AmazonClientFactory
{
  public AmazonS3 getClient();
}
