package org.exoplatform.services.portletcontainer.pci;

import org.apache.commons.lang.StringUtils;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public class ExoWindowID implements WindowID {
  final static public String DEFAULT_PORTAL_CONFIG = "default-portal-config" ;
  final static public String MOBILE_PORTAL_CONFIG = "default-portal-config" ;
  
  private String owner;
  private String portletApplicationName;
  private String portletName;
  private String uniqueID;
  private String persistenceId ;
  private String configurationSource = DEFAULT_PORTAL_CONFIG;

  public ExoWindowID() {	
  }
  
  public ExoWindowID(String persistenceId) {
  	this.persistenceId = persistenceId ;
		int idx = persistenceId.indexOf(":/");
		owner = persistenceId.substring(0, idx);
		persistenceId = persistenceId.substring(idx + 2, persistenceId.length());
		String[] keys = StringUtils.split(persistenceId, "/");
		portletApplicationName = keys[0];
		portletName = keys[1];
		uniqueID = keys[2];
	}
  
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getPortletApplicationName() {
    return portletApplicationName;
  }

  public void setPortletApplicationName(String portletApplicationName) {
    this.portletApplicationName = portletApplicationName;
  }

  public String getPortletName() {
    return portletName;
  }

  public void setPortletName(String portletName) {
    this.portletName = portletName;
  }

  public String getUniqueID() {
    return uniqueID;
  }

  public void setUniqueID(String uniqueID) {
    this.uniqueID = uniqueID;
  }

  public String getPersistenceId() {
  	return this.persistenceId ;
  }
  
  public void setPersistenceId(String id) {
  	persistenceId = id ;
  }
  
  public String generatePersistenceId() {
  	return owner + ":/" + portletApplicationName + "/" + portletName + "/" + uniqueID ;
  }
  
  public String generateKey() {    
    return   uniqueID;
  }
  
  /**
   * The configuration source can be from default portal config layout, mobile portal
   * config layout  or page config
   */
  public String getConfigurationSource() { return configurationSource ; }
  public void   setConfigurationSource(String source) { configurationSource = source; }
}