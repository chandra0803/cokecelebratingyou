
package com.biperf.core.dao.filestore.hibernate;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.filestore.FileStoreDAO;
import com.biperf.core.domain.filestore.FileStore;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class FileStoreDAOImpl extends BaseDAO implements FileStoreDAO
{
  @Override
  public FileStore save( FileStore fileStore )
  {
    FileStore fileStoreReturned = null;
    fileStoreReturned = (FileStore)HibernateUtil.saveOrUpdateOrShallowMerge( fileStore );
    return fileStoreReturned;
  }

  @Override
  public FileStore get( Long fileStorageId )
  {
    return (FileStore)getSession().get( FileStore.class, fileStorageId );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<FileStore> findFileStoresForUser( Long userId )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( FileStore.class, "fileStore" );
    criteria.add( Restrictions.eq( "fileStore.user.id", userId ) );

    // last 24 hours...
    Calendar instance = Calendar.getInstance();
    instance.add( Calendar.HOUR, -24 );
    criteria.add( Restrictions.gt( "fileStore.dateGenerated", instance.getTime() ) );
    criteria.addOrder( Order.asc( "fileStore.dateGenerated" ) );

    return (List<FileStore>)criteria.list();
  }
}
