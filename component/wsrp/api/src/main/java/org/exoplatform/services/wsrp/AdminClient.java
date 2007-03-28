package org.exoplatform.services.wsrp;

import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.components.logger.LogFactory;
import org.apache.axis.deployment.wsdd.WSDDConstants;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.utils.Messages;
import org.apache.axis.utils.Options;
import org.apache.commons.logging.Log;

import javax.xml.rpc.ServiceException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Vector;

// Referenced classes of package org.apache.axis.client:
//            Service, Call

public class AdminClient
{

  public static void setDefaultConfiguration(EngineConfiguration config)
  {
    defaultConfiguration.set(config);
  }

  private static String getUsageInfo()
  {
    String lSep = System.getProperty("line.separator");
    StringBuffer msg = new StringBuffer();
    msg.append(Messages.getMessage("acUsage00")).append(lSep);
    msg.append(Messages.getMessage("acUsage01")).append(lSep);
    msg.append(Messages.getMessage("acUsage02")).append(lSep);
    msg.append(Messages.getMessage("acUsage03")).append(lSep);
    msg.append(Messages.getMessage("acUsage04")).append(lSep);
    msg.append(Messages.getMessage("acUsage05")).append(lSep);
    msg.append(Messages.getMessage("acUsage06")).append(lSep);
    msg.append(Messages.getMessage("acUsage07")).append(lSep);
    msg.append(Messages.getMessage("acUsage08")).append(lSep);
    msg.append(Messages.getMessage("acUsage09")).append(lSep);
    msg.append(Messages.getMessage("acUsage10")).append(lSep);
    msg.append(Messages.getMessage("acUsage11")).append(lSep);
    msg.append(Messages.getMessage("acUsage12")).append(lSep);
    msg.append(Messages.getMessage("acUsage13")).append(lSep);
    msg.append(Messages.getMessage("acUsage14")).append(lSep);
    msg.append(Messages.getMessage("acUsage15")).append(lSep);
    msg.append(Messages.getMessage("acUsage16")).append(lSep);
    msg.append(Messages.getMessage("acUsage17")).append(lSep);
    msg.append(Messages.getMessage("acUsage18")).append(lSep);
    msg.append(Messages.getMessage("acUsage19")).append(lSep);
    msg.append(Messages.getMessage("acUsage20")).append(lSep);
    msg.append(Messages.getMessage("acUsage21")).append(lSep);
    msg.append(Messages.getMessage("acUsage22")).append(lSep);
    msg.append(Messages.getMessage("acUsage23")).append(lSep);
    msg.append(Messages.getMessage("acUsage24")).append(lSep);
    msg.append(Messages.getMessage("acUsage25")).append(lSep);
    msg.append(Messages.getMessage("acUsage26")).append(lSep);
    return msg.toString();
  }

  public AdminClient()
  {
    try
    {
      EngineConfiguration config = (EngineConfiguration)defaultConfiguration.get();
      Service service;
      if(config != null)
        service = new Service(config);
      else
        service = new Service();
      call = (Call)service.createCall();
    }
    catch(ServiceException e)
    {
      System.err.println(Messages.getMessage("couldntCall00") + ": " + e);
      call = null;
    }
  }

  public Call getCall()
  {
    return call;
  }

  public String list(Options opts)
    throws Exception
    {
      processOpts(opts);
      return list();
    }

  public String list() throws Exception {
    log.debug(Messages.getMessage("doList00"));
    String str = "<m:list xmlns:m=\"http://xml.apache.org/axis/wsdd/\"/>";
    ByteArrayInputStream input = new ByteArrayInputStream(str.getBytes());
    return process(input);
  }

  public String quit(Options opts) throws Exception {
    processOpts(opts);
    return quit();
  }

  public String quit() throws Exception {
    log.debug(Messages.getMessage("doQuit00"));
    String str = "<m:quit xmlns:m=\"http://xml.apache.org/axis/wsdd/\"/>";
    ByteArrayInputStream input = new ByteArrayInputStream(str.getBytes());
    return process(input);
  }

  public String undeployHandler(String handlerName) throws Exception
  {
    log.debug(Messages.getMessage("doQuit00"));
    String str = "<m:" + ROOT_UNDEPLOY + " xmlns:m=\"" + "http://xml.apache.org/axis/wsdd/" + "\">" + "<handler name=\"" + handlerName + "\"/>" + "</m:" + ROOT_UNDEPLOY + ">";
    ByteArrayInputStream input = new ByteArrayInputStream(str.getBytes());
    return process(input);
  }

  public String undeployService(String serviceName) throws Exception
  {
    log.debug(Messages.getMessage("doQuit00"));
    String str = "<m:" + ROOT_UNDEPLOY + " xmlns:m=\"" + "http://xml.apache.org/axis/wsdd/" + "\">" + "<service name=\"" + serviceName + "\"/>" + "</m:" + ROOT_UNDEPLOY + ">";
    ByteArrayInputStream input = new ByteArrayInputStream(str.getBytes());
    return process(input);
  }

  public String process(String args[])
    throws Exception
    {
      StringBuffer sb = new StringBuffer();
      Options opts = new Options(args);
      opts.setDefaultURL("http://localhost:8080/axis/services/AdminService");
      if(opts.isFlagSet('d') <= 0);
      args = opts.getRemainingArgs();
      if(args == null || opts.isFlagSet('?') > 0)
      {
        System.out.println(Messages.getMessage("usage00", "AdminClient [Options] [list | <deployment-descriptor-files>]"));
        System.out.println("");
        System.out.println(getUsageInfo());
        return null;
      }
      for(int i = 0; i < args.length; i++)
      {
        InputStream input = null;
        if(args[i].equals("list"))
          sb.append(list(opts));
        else
          if(args[i].equals("quit"))
            sb.append(quit(opts));
          else
            if(args[i].equals("passwd"))
            {
              System.out.println(Messages.getMessage("changePwd00"));
              if(args[i + 1] == null)
              {
                System.err.println(Messages.getMessage("needPwd00"));
                return null;
              }
              String str = "<m:passwd xmlns:m=\"http://xml.apache.org/axis/wsdd/\">";
              str = str + args[i + 1];
              str = str + "</m:passwd>";
              input = new ByteArrayInputStream(str.getBytes());
              i++;
              sb.append(process(opts, input));
            } else
              if(args[i].indexOf(File.pathSeparatorChar) == -1)
              {
                System.out.println(Messages.getMessage("processFile00", args[i]));
                sb.append(process(opts, args[i]));
              } else
              {
                StringTokenizer tokenizer = null;
                for(tokenizer = new StringTokenizer(args[i], File.pathSeparator); tokenizer.hasMoreTokens();)
                {
                  String file = tokenizer.nextToken();
                  System.out.println(Messages.getMessage("processFile00", file));
                  sb.append(process(opts, file));
                  if(tokenizer.hasMoreTokens())
                    sb.append("\n");
                }

              }
      }

      return sb.toString();
    }

  public void processOpts(Options opts)
    throws Exception
    {
      if(call == null)
        throw new Exception(Messages.getMessage("nullCall00"));
      call.setTargetEndpointAddress(new URL(opts.getURL()));
      call.setUsername(opts.getUser());
      call.setPassword(opts.getPassword());
      String tName = opts.isValueSet('t');
      if(tName != null && !tName.equals(""))
        call.setProperty("transport_name", tName);
    }

  public String process(InputStream input)
    throws Exception
    {
      return process(null, input);
    }

  public String process(URL xmlURL)
    throws Exception
    {
      return process(null, xmlURL.openStream());
    }

  public String process(String xmlFile)
    throws Exception
    {
      FileInputStream in = new FileInputStream(xmlFile);
      String result = process(null, ((InputStream) (in)));
      in.close();
      return result;
    }

  public String process(Options opts, String xmlFile)
    throws Exception
    {
      processOpts(opts);
      return process(xmlFile);
    }

  public String process(Options opts, InputStream input) throws Exception {
    if(call == null)
      throw new Exception(Messages.getMessage("nullCall00"));
    if(opts != null)
      processOpts(opts);
    call.setUseSOAPAction(true);
    call.setSOAPActionURI("AdminService");
    Vector result = null;
    Object params[] = {
      new SOAPBodyElement(input)
    };
    result = (Vector)call.invoke(params);
    input.close();
    if(result == null || result.isEmpty())
    {
      throw new AxisFault(Messages.getMessage("nullResponse00"));
    } else
    {
      SOAPBodyElement body = (SOAPBodyElement)result.elementAt(0);
      return body.toString();
    }
  }

  public static void main(String args[]) {
    try {
      AdminClient admin = new AdminClient();
      String result = admin.process(args);
      if(result != null)
        System.out.println(result);
      else
        System.exit(1);
    } catch(Exception e) {
      e.printStackTrace() ;
      System.err.println(Messages.getMessage("exception00") + ": " + e);
      System.exit(1);
    }
  }

  static Class _mthclass$(String x0) throws Exception {
    return Class.forName(x0);
  }

  protected static Log log;
  private static ThreadLocal defaultConfiguration = new ThreadLocal();
  protected Call call;
  protected static final String ROOT_UNDEPLOY;

  static {
    log = LogFactory.getLog((org.apache.axis.client.AdminClient.class).getName());
    ROOT_UNDEPLOY = WSDDConstants.QNAME_UNDEPLOY.getLocalPart();
  }
}
