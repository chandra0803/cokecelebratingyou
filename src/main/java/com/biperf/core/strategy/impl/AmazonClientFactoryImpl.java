
package com.biperf.core.strategy.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.biperf.core.strategy.AmazonClientFactory;
import com.biperf.core.strategy.BaseStrategy;
import com.biw.event.streams.util.BIWDefaultAWSCredentialsProviderChain;

public class AmazonClientFactoryImpl extends BaseStrategy implements AmazonClientFactory
{

  @Override
  public AmazonS3 getClient()
  {
    return AmazonS3ClientBuilder.standard().withCredentials( new BIWDefaultAWSCredentialsProviderChain() ).build();
  }

}
