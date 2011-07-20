/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.management;

import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor;
import org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData;
import org.exoplatform.services.portletcontainer.monitor.PortletRequestMonitorData;
import org.exoplatform.management.annotations.Managed;
import org.exoplatform.management.annotations.ManagedDescription;
import org.exoplatform.management.jmx.annotations.NameTemplate;
import org.exoplatform.management.jmx.annotations.Property;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;

import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
@Managed
@NameTemplate({@Property(key="portlet", value="{Name}"),@Property(key="application", value="{Application}")})
public class PortletManaged {

  /** . */
  private final PortletApplicationManaged application;

  /** . */
  private final Portlet portlet;

  public PortletManaged(PortletApplicationManaged application, Portlet portlet) {
    this.application = application;
    this.portlet = portlet;
  }

  @Managed
  @ManagedDescription("The application name")
  public String getApplication() {
    return application.getName();
  }

  @Managed
  @ManagedDescription("The portlet name")
  public String getName() {
    return portlet.getPortletName();
  }

  @Managed
  @ManagedDescription("The mean execution time of an action phase")
  public long getMeanActionTime() {
    return compute(AVG_ACTION, new Mean());
  }

  @Managed
  @ManagedDescription("The mean execution time of an event phase")
  public long getMeanEventTime() {
    return compute(AVG_EVENT, new Mean());
  }

  @Managed
  @ManagedDescription("The mean execution time of a render phase")
  public long getMeanRenderTime() {
    return compute(AVG_RENDER, new Mean());
  }

  @Managed
  @ManagedDescription("The mean execution time of a resource phase")
  public long getMeanResourceTime() {
    return compute(AVG_RESOURCE, new Mean());
  }

  @Managed
  @ManagedDescription("The minimum execution time of an action phase")
  public long getMinActionTime() {
    return compute(MIN_ACTION, new Min());
  }

  @Managed
  @ManagedDescription("The minimum execution time of an event phase")
  public long getMinEventTime() {
    return compute(MIN_EVENT, new Min());
  }

  @Managed
  @ManagedDescription("The minimum execution time of a render phase")
  public long getMinRenderTime() {
    return compute(MIN_RENDER, new Min());
  }

  @Managed
  @ManagedDescription("The minimum execution time of a resource phase")
  public long getMinResourceTime() {
    return compute(MIN_RESOURCE, new Min());
  }

  @Managed
  @ManagedDescription("The maximum execution time of an action phase")
  public long getMaxActionTime() {
    return compute(MAX_ACTION, new Max());
  }

  @Managed
  @ManagedDescription("The maximum execution time of an event phase")
  public long getMaxEventTime() {
    return compute(MAX_EVENT, new Max());
  }

  @Managed
  @ManagedDescription("The maximum execution time of a render phase")
  public long getMaxRenderTime() {
    return compute(MAX_RENDER, new Max());
  }

  @Managed
  @ManagedDescription("The maximum execution time of a resource phase")
  public long getMaxResourceTime() {
    return compute(MAX_RESOURCE, new Max());
  }

  @Managed
  @ManagedDescription("The number of action phases")
  public long getActionCount() {
    return compute(COUNT_ACTION, new Sum());
  }

  @Managed
  @ManagedDescription("The number of event phases")
  public long getEventCount() {
    return compute(COUNT_EVENT, new Sum());
  }

  @Managed
  @ManagedDescription("The number of render phases")
  public long getRenderCount() {
    return compute(COUNT_RENDER, new Sum());
  }

  @Managed
  @ManagedDescription("The number of resource phases")
  public long getResourceCount() {
    return compute(COUNT_RESOURCE, new Sum());
  }

  private long compute(Getter getter, Statistic statistic) {
    String key = application.getName() + "/" + getName();
    ExoContainer manager = ExoContainerContext.getTopContainer();
    PortletContainerMonitor monitor = (PortletContainerMonitor)manager.getComponentInstanceOfType(PortletContainerMonitor.class);
    Map map = monitor.getPortletRuntimeDataMap();
    PortletRuntimeData runtimeData = (PortletRuntimeData)map.get(key);
    PortletRequestMonitorData[] datas = runtimeData.getPortletRequestMonitorData();
    if (datas == null) {
      return -1;
    }
    for (PortletRequestMonitorData data : datas) {
      long value = getter.getValue(data);
      if (value > 0)
        statistic.operate(value);
    }
    return statistic.getResult();
  }

  private interface Statistic {
    void operate(long value);
    long getResult();
  }

  private interface Getter {
    long getValue(PortletRequestMonitorData data);
  }

  private static Getter AVG_ACTION = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getAvgActionExecutionTime();
    }
  };

  private static Getter AVG_EVENT = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getAvgEventExecutionTime();
    }
  };

  private static Getter AVG_RENDER = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getAvgRenderExecutionTime();
    }
  };

  private static Getter AVG_RESOURCE = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getAvgResourceExecutionTime();
    }
  };

  private static Getter MIN_ACTION = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getMinActionExecutionTime();
    }
  };

  private static Getter MIN_EVENT = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getMinEventExecutionTime();
    }
  };

  private static Getter MIN_RENDER = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getMinRenderExecutionTime();
    }
  };

  private static Getter MIN_RESOURCE = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getMinResourceExecutionTime();
    }
  };

  private static Getter MAX_ACTION = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getMaxActionExecutionTime();
    }
  };

  private static Getter MAX_EVENT = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getMaxEventExecutionTime();
    }
  };

  private static Getter MAX_RENDER = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getMaxRenderExecutionTime();
    }
  };

  private static Getter MAX_RESOURCE = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getMaxResourceExecutionTime();
    }
  };

  private static Getter COUNT_ACTION = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getActionRequestCounter() + 5;
    }
  };

  private static Getter COUNT_EVENT = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getEventRequestCounter() + 5;
    }
  };

  private static Getter COUNT_RENDER = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getRenderRequestCounter() + 5;
    }
  };

  private static Getter COUNT_RESOURCE = new Getter() {
    public long getValue(PortletRequestMonitorData data) {
      return data.getResourceRequestCounter() + 5;
    }
  };

  private static class Mean implements Statistic {

    private long l = 0;
    private int count = 0;

    public void operate(long value) {
      l += value;
      count++;
    }

    public long getResult() {
      return count == 0 ? 0 : l / count;
    }
  }

  private static class Sum implements Statistic {

    private long l = 0;

    public void operate(long value) {
      l += value;
    }

    public long getResult() {
      return l;
    }
  }

  private static class Min implements Statistic {

    private long l = -1;

    public void operate(long value) {
      if (value > 0) {
        if (l == -1) {
          l = value;
        } else {
          l = Math.min(l, value);
        }
      }
    }

    public long getResult() {
      return l;
    }
  }

  private static class Max implements Statistic {

    private long l = -1;

    public void operate(long value) {
      if (value > 0) {
        if (l == -1) {
          l = value;
        } else {
          l = Math.max(l, value);
        }
      }
    }

    public long getResult() {
      return l;
    }
  }
}
