package org.exoplatform.services.wsrp2.consumer;

/**
 * A consumer based session which represents a user session with a certain
 * producer. This user session contains one or more group sessions.
 * 
 * @author <a href='mailto:peter.fischer@de.ibm.com'>Peter Fischer</a>
 * @author Benjamin Mestrallet
 * @see GroupSession
 */
public interface UserSessionMgr extends UserSession, InitCookieInfo, java.io.Serializable {

}
