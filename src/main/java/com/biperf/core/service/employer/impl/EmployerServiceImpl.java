/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/employer/impl/EmployerServiceImpl.java,v $
 */

package com.biperf.core.service.employer.impl;

import java.util.List;

import com.biperf.core.dao.employer.EmployerDAO;
import com.biperf.core.domain.employer.Employer;
import com.biperf.core.service.employer.EmployerService;

/**
 * EmployerServiceImpl.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>crosenquest</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class EmployerServiceImpl implements EmployerService
{

  /** EmployerDAO */
  EmployerDAO employerDAO;

  /**
   * Set the EmployerDAO through IoC.
   * 
   * @param employerDAO
   */
  public void setEmployerDAO( EmployerDAO employerDAO )
  {
    this.employerDAO = employerDAO;
  }

  /**
   * Gets all employers through the DAO. Overridden from
   * 
   * @see com.biperf.core.service.employer.EmployerService#getAll()
   * @return List
   */
  public List getAll()
  {
    return this.employerDAO.getAll();
  }

  /**
   * Search for the employer with the given param criteria. Overridden from
   * 
   * @see com.biperf.core.service.employer.EmployerService#searchEmployer(java.lang.String)
   * @param name
   * @return List
   */
  public List searchEmployer( String name )
  {
    return this.employerDAO.searchEmployer( name );
  }

  /**
   * Save or update the employer param. Overridden from
   * 
   * @see com.biperf.core.service.employer.EmployerService#saveEmployer(com.biperf.core.domain.employer.Employer)
   * @param employer
   * @return Employer
   */
  public Employer saveEmployer( Employer employer )
  {
    return this.employerDAO.saveEmployer( employer );
  }

  /**
   * Get the employer from the database by the id param. Overridden from
   * 
   * @see com.biperf.core.service.employer.EmployerService#getEmployerById(java.lang.Long)
   * @param id
   * @return id
   */
  public Employer getEmployerById( Long id )
  {
    return this.employerDAO.getEmployerById( id );
  }

  @Override
  public List<Employer> getActiveEmployers()
  {
    return this.employerDAO.getActiveEmployers();
  }

}
