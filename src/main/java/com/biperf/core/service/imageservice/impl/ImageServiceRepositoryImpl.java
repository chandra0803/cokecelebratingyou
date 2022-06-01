/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.imageservice.impl;

import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.imageservice.ImageServiceRepository;
import com.biperf.core.service.imageservice.v1.ImageUploadRequest;
import com.biperf.core.service.imageservice.v1.ImageUploadResult;
import com.biperf.core.service.imageservice.v1.ImageUrlResult;
import com.biperf.core.utils.MeshServicesUtil;

/**
 * 
 * @author sivanand
 * @since Jan 31, 2019
 * @version 1.0
 */

@Service( "ImageServiceRepositoryImpl" )
public class ImageServiceRepositoryImpl implements ImageServiceRepository
{

  private static final Log logger = LogFactory.getLog( ImageServiceRepositoryImpl.class );

  // Image data should be Base64 format
  @Override
  public String uploadImage( String imageData, String id, String imagePrefix ) throws ServiceErrorException
  {
    ImageUploadResult imageUploadResult = uploadImage( buildImageUploadRequest( imageData, id, imagePrefix ) );

    String imageUrl = imageUploadResult.getImageUrlResult().getProxyUrl();

    if ( Objects.nonNull( imageUrl ) )
    {
      return imageUrl;
    }

    logger.error( "Image upload failed in image service" );
    throw new ServiceErrorException( "personalInfo.IMAGE_UPLOAD_FAILED" );
  }

  public ImageUploadResult uploadImage( ImageUploadRequest request )
  {
    ImageUploadResult uploadResult = new ImageUploadResult();
    ImageUrlResult urlResult;

    try
    {
      urlResult = MeshServicesUtil.getRestWebClient().postForObject( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/images",
                                                                     new HttpEntity<ImageUploadRequest>( request, MeshServicesUtil.getAuthorizationHeadersForClientCredentials() ),
                                                                     ImageUrlResult.class );
      uploadResult.setName( request.getName() );
      uploadResult.setImageUrlResult( urlResult );
    }
    catch( Exception e )
    {
      logger.error( "Message from image service" + e );
    }

    return uploadResult;
  }

  private ImageUploadRequest buildImageUploadRequest( String imageData, String id, String imagePrefix )
  {
    ImageUploadRequest request = new ImageUploadRequest();
    request.setImage( imageData );
    request.setName( imagePrefix + "_" + System.currentTimeMillis() + "_" + id );
    request.setPublic( true );
    return request;
  }

}
