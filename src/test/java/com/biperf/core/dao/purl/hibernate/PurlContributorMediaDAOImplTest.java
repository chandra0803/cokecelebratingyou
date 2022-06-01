
package com.biperf.core.dao.purl.hibernate;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.purl.PurlContributorDAO;
import com.biperf.core.dao.purl.PurlContributorMediaDAO;
import com.biperf.core.domain.enums.PurlContributorMediaStatus;
import com.biperf.core.domain.enums.PurlContributorMediaType;
import com.biperf.core.domain.enums.PurlMediaState;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlContributorMedia;

public class PurlContributorMediaDAOImplTest extends BaseDAOTest
{
  public static PurlContributorMedia buildAndSavePurlContributorMediaPhoto( String suffix )
  {
    PurlContributorMedia purlContributorMedia = new PurlContributorMedia();
    purlContributorMedia.setId( new Long( 1 ) );
    purlContributorMedia.setCaption( "caption" + suffix );
    purlContributorMedia.setUrl( "url" + suffix );
    purlContributorMedia.setUrlThumb( "urlThumb" + suffix );
    purlContributorMedia.setType( PurlContributorMediaType.lookup( PurlContributorMediaType.PICTURE ) );
    purlContributorMedia.setStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.ACTIVE ) );
    purlContributorMedia.setState( PurlMediaState.lookup( PurlMediaState.STAGED ) );

    PurlContributor purlContributor = PurlContributorDAOImplTest.buildAndSaveUniquePaxPurlContributor( suffix );

    PurlContributor savedPurlContributor = getPurlContributorDAO().getPurlContributorById( purlContributor.getId() );
    savedPurlContributor.addMedia( purlContributorMedia );
    getPurlContributorDAO().save( savedPurlContributor );
    flushAndClearSession();

    return purlContributorMedia;
  }

  public static PurlContributorMedia buildAndSavePurlContributorMediaVideo( String suffix )
  {
    PurlContributorMedia purlContributorMedia = new PurlContributorMedia();
    purlContributorMedia.setId( new Long( 1 ) );
    purlContributorMedia.setCaption( "caption" + suffix );
    purlContributorMedia.setUrl( "url" + suffix );
    purlContributorMedia.setType( PurlContributorMediaType.lookup( PurlContributorMediaType.VIDEO ) );
    purlContributorMedia.setStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.ACTIVE ) );
    purlContributorMedia.setState( PurlMediaState.lookup( PurlMediaState.STAGED ) );

    PurlContributor purlContributor = PurlContributorDAOImplTest.buildAndSaveUniquePaxPurlContributor( suffix );

    PurlContributor savedPurlContributor = getPurlContributorDAO().getPurlContributorById( purlContributor.getId() );
    savedPurlContributor.addMedia( purlContributorMedia );
    getPurlContributorDAO().save( savedPurlContributor );
    flushAndClearSession();

    return purlContributorMedia;
  }

  public static PurlContributorMedia buildAndSavePurlContributorMediaVideoUrl( String suffix )
  {
    PurlContributorMedia purlContributorMedia = new PurlContributorMedia();
    purlContributorMedia.setId( new Long( 1 ) );
    purlContributorMedia.setCaption( "caption" + suffix );
    purlContributorMedia.setUrl( "url" + suffix );
    purlContributorMedia.setType( PurlContributorMediaType.lookup( PurlContributorMediaType.VIDEO_URL ) );
    purlContributorMedia.setStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.ACTIVE ) );
    purlContributorMedia.setState( PurlMediaState.lookup( PurlMediaState.POSTED ) );

    PurlContributor purlContributor = PurlContributorDAOImplTest.buildAndSaveUniquePaxPurlContributor( suffix );

    PurlContributor savedPurlContributor = getPurlContributorDAO().getPurlContributorById( purlContributor.getId() );
    savedPurlContributor.addMedia( purlContributorMedia );
    getPurlContributorDAO().save( savedPurlContributor );
    flushAndClearSession();

    return purlContributorMedia;
  }

  public void testPostPhoto()
  {
    String uniqueString = getUniqueString();
    PurlContributorMedia purlMediaPhoto = buildAndSavePurlContributorMediaPhoto( uniqueString );

    PurlContributorMedia savedPurlMediaPhoto = getPurlContributorMediaDAO().getPurlContributorMediaById( purlMediaPhoto.getId() );
    assertNotNull( savedPurlMediaPhoto );
  }

  public void testPostVideo()
  {
    String uniqueString = getUniqueString();
    PurlContributorMedia purlMediaVideo = buildAndSavePurlContributorMediaVideo( uniqueString );

    PurlContributorMedia savedPurlMediaVideo = getPurlContributorMediaDAO().getPurlContributorMediaById( purlMediaVideo.getId() );
    assertNotNull( savedPurlMediaVideo );
  }

  public void testPostVideoUrl()
  {
    String uniqueString = getUniqueString();
    PurlContributorMedia purlMediaVideoUrl = buildAndSavePurlContributorMediaVideoUrl( uniqueString );

    PurlContributorMedia savedPurlMediaVideoUrl = getPurlContributorMediaDAO().getPurlContributorMediaById( purlMediaVideoUrl.getId() );
    assertNotNull( savedPurlMediaVideoUrl );
  }

  private static PurlContributorDAO getPurlContributorDAO()
  {
    return (PurlContributorDAO)getDAO( PurlContributorDAO.BEAN_NAME );
  }

  private static PurlContributorMediaDAO getPurlContributorMediaDAO()
  {
    return (PurlContributorMediaDAO)getDAO( PurlContributorMediaDAO.BEAN_NAME );
  }

}
