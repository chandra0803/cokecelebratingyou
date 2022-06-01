
package com.biperf.core.strategy.impl;

import static com.biperf.core.utils.Environment.getEnvironment;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.strategy.AWSReportDownloadStrategy;
import com.biperf.core.strategy.AmazonClientFactory;
import com.biperf.core.strategy.BaseStrategy;
import com.biperf.core.utils.ImageUtils;

public class AWSReportDownloadStrategyImpl extends BaseStrategy implements AWSReportDownloadStrategy
{
  private static final Log logger = LogFactory.getLog( AWSReportDownloadStrategyImpl.class );
  public static final String ORACLE_REPORTS = "oracle-reports";
  public static final String HYPHEN = "-";

  private AmazonClientFactory amazonClientFactory;

  @Override
  public void awsWriteFileData( String filePath, final HttpServletResponse response ) throws ServiceErrorException
  {
    AmazonS3 s3client = null;
    String fileName = null;
    byte[] read_buf = null;
    try
    {
      s3client = amazonClientFactory.getClient();
      fileName = ImageUtils.convertToUrlPath( filePath );
      logger.error( "Large Audience Reports File name" + fileName );
      S3Object object = s3client.getObject( new GetObjectRequest( getBucketName(), fileName ) );
      S3ObjectInputStream s3is = object.getObjectContent();

      read_buf = new byte[8192];
      int read_len = 0;
      while ( ( read_len = s3is.read( read_buf ) ) > 0 )
      {
        response.getOutputStream().write( read_buf, 0, read_len );
      }
      s3is.close();
      object.close();
    }
    catch( AmazonServiceException ase )
    {
      String msg = buildAmazonLog( ase );
      logger.error( msg, ase );
      throw new ServiceErrorException( buildAmazonLog( ase ), ase );
    }
    catch( Throwable t )
    {
      logger.error( t.getMessage(), t );
      throw new ServiceErrorException( t.getMessage(), t );
    }
    finally
    {
      if ( s3client != null )
      {
        s3client.shutdown();
      }
    }
  }

  public AmazonClientFactory getAmazonClientFactory()
  {
    return amazonClientFactory;
  }

  public void setAmazonClientFactory( AmazonClientFactory amazonClientFactory )
  {
    this.amazonClientFactory = amazonClientFactory;
  }

  protected String getBucketName()
  {
    String bucketName = getSystemVariableService().getContextName() + HYPHEN + getEnvironment() + HYPHEN + ORACLE_REPORTS;
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "AWS operation using Bucket " + bucketName );
    }
    logger.error( "Large Audience Reports bucketName" + bucketName );
    return bucketName;
  }

  private String buildAmazonLog( AmazonServiceException ase )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason. " + ase.getMessage() );
    sb.append( "\nError Message:    " + ase.getMessage() );
    sb.append( "\nHTTP Status Code: " + ase.getStatusCode() );
    sb.append( "\nAWS Error Code:   " + ase.getErrorCode() );
    sb.append( "\nError Type:       " + ase.getErrorType() );
    sb.append( "\nRequest ID:       " + ase.getRequestId() );
    return sb.toString();
  }

}
