/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.monitor;
/*
 * Tue, May 27, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: PortletRequestMonitorData.java,v 1.2 2004/05/06 12:19:00 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class PortletRequestMonitorData {
  private long minRange_ ;
  private long maxRange_ ;
  private int  actionRequestCounter_ ;
  private long minActionExecutionTime_ ;
  private long maxActionExecutionTime_ ;
  private long sumActionExecutionTime_ = 0 ;
  
  private int  renderRequestCounter_ ;
  private long minRenderExecutionTime_ ;
  private long maxRenderExecutionTime_ ;
  private long sumRenderExecutionTime_ = 0 ;
  private int cacheHitCounter_ ;
  
  public PortletRequestMonitorData(long minRange, long maxRange) {
    minRange_ = minRange ;
    maxRange_ = maxRange ;
    
    actionRequestCounter_ = -5 ; //ignore 5 first request to make sure the jvm optimize the class
    minActionExecutionTime_  = 0 ;
    maxActionExecutionTime_ = 0 ;
    
    renderRequestCounter_ = -5 ; //ignore 5 first request to make sure the jvm optimize the class
    minRenderExecutionTime_  = 0 ;
    maxRenderExecutionTime_ = 0 ;
    
    cacheHitCounter_ = 0 ;
  }

  public long minRange() { return minRange_ ; }
  public long maxRange() { return maxRange_ ; }

  public int getActionRequestCounter() { return actionRequestCounter_  ; }
  public int getRenderRequestCounter() { return renderRequestCounter_  ; }
  
  public int getCacheHitCounter() { return cacheHitCounter_  ; }

  public long getMinActionExecutionTime() { return minActionExecutionTime_ ; }
  
  public long getMinRenderExecutionTime() { return minRenderExecutionTime_ ; }

  public long getMaxActionExecutionTime() { return maxActionExecutionTime_ ; }
  
  public long getMaxRenderExecutionTime() { return maxRenderExecutionTime_ ; }

  public long getAvgActionExecutionTime() {
    if(actionRequestCounter_ <= 0) return 0 ;
    return sumActionExecutionTime_/actionRequestCounter_ ;
  }
  
  public long getAvgRenderExecutionTime() {
    if(renderRequestCounter_ <= 0) return 0 ;
    return sumRenderExecutionTime_/renderRequestCounter_ ;
  }

  public long sumActionExecutionTime() { return sumActionExecutionTime_ ; }
  
  public long sumRenderExecutionTime() { return sumRenderExecutionTime_ ; }
  
  public void logActionRequest(long executionTime) {
    actionRequestCounter_++ ;
    if(actionRequestCounter_ > 0) {
      sumActionExecutionTime_  += executionTime;
      if(executionTime < minActionExecutionTime_) minActionExecutionTime_ = executionTime ;
      if(executionTime > maxActionExecutionTime_) maxActionExecutionTime_ = executionTime ;
    } else {
      minActionExecutionTime_ = executionTime ;   
      maxActionExecutionTime_ = executionTime ;   
    }
  }
  
  public void logRenderRequest(long executionTime, boolean cacheHit) {
    renderRequestCounter_++ ;
    if(renderRequestCounter_ > 0) {
      sumRenderExecutionTime_  += executionTime;
      if(cacheHit) cacheHitCounter_++ ;
      if(executionTime < minRenderExecutionTime_) minRenderExecutionTime_ = executionTime ;
      if(executionTime > maxRenderExecutionTime_) maxRenderExecutionTime_ = executionTime ;
    } else {
      minRenderExecutionTime_ = executionTime ;   
      maxRenderExecutionTime_ = executionTime ;   
    }
  }
}