package org.exoplatform.services.portletcontainer.test;


import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;

import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.Preference;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;

/**
 * preferences persister implementation<br>
 * manages portlet window preferences on page-cell-user level.<br>
 * merges user level customized preferences with portlet deployed preferences<br>
 * a single instance may serve all portlets on all sites (portal), hence needs to be thread safe.<br>
 * 
 * also Serializable
 * needs to be the only implementation of PortletPreferencesPersister registered as exo service<br>
 * @see exo.services.portletcontainer.impl.portletAPIImp.PortletPreferencesImp
 */
public class ENExoPortletPreferencesFilePersisterImpl
	implements PortletPreferencesPersister, Serializable
{
	/**
	 * Constructor (default)
	 * needed, as been created by exo services container
	 */
	public ENExoPortletPreferencesFilePersisterImpl()
	{
	}
	
	//@see exo.services.portletcontainer.persistence.PortletPreferencesPersister#getPortletPreferences(exo.services.portletcontainer.pci.WindowID)
	public ExoPortletPreferences getPortletPreferences(WindowID windowID) throws Exception
	{	
		System.out.println("getPortletPreferences("+windowID+")");
		/*
		 * logged in users will have customized preferences along side with merged deployed preferences
		 * guests will only have deployed preferences
		 */
		ExoPortletPreferences prefs=null;
		if(windowID!=null)
			prefs=loadPreferences(windowID.getUniqueID());
		return prefs;
	}

	//@see exo.services.portletcontainer.persistence.PortletPreferencesPersister#savePortletPreferences(exo.services.portletcontainer.pci.WindowID, exo.services.portletcontainer.pci.model.ExoPortletPreferences)
	public void savePortletPreferences(WindowID windowID
		,ExoPortletPreferences preferences) throws Exception
	{
		System.out.println("savePortletPreferences("+windowID+")");
		if(windowID!=null)
			storePreferences(preferences,windowID.getUniqueID());
	}

	/**
	 * load preferences from user-page-cell personal params<br>
	 * read-only preferences are olways retrieved from portlet.xml, and never saved to IBP storage<br>
	 * empty values are saved to storage as '$empty$' if exist in metadata and converted to nulls
	 * @param uid portlet window uid
	 * @return exo preferences object
	 */
	private static ExoPortletPreferences loadPreferences(String uid)
	{
		ExoPortletPreferences prefs=new ExoPortletPreferences();
		Properties props=loadProperties();
		if(props!=null)
		{
			String qs=props.getProperty(uid);
			String[][] qsparts=qryStringToArray(qs);
			if(qsparts!=null)
			{
				String name=null;
				String value=null;
				Preference pref=null;
				//loop on all entries-ignore arrays
				for(int i=0,n=qsparts.length;i<n;i++)
				{
					name=qsparts[i][0];
					if(isEmpty(name))
						continue;
					value=qsparts[i][1];
					if(isEmpty(value))
						continue;
					//need to create a new Preference object?
					if(pref==null || !name.equalsIgnoreCase(pref.getName()))
					{
						//create a Preference object
						pref=new Preference();
						pref.setName(name);
						//all dynamic prefs are editable
						pref.setReadOnly(false);
						//add to collection
						prefs.addPreference(pref);
						System.out.println("ENExoPortletPreferencesFilePersisterImpl.get("+uid+")->"+name+"="+value);
					}
					pref.addValue(value);
				}
			}
		}
		return prefs;
	}

	/**
	 * store preferences to user-page-cell personal params<br>
	 * read-only preferences are olways retrieved from portlet.xml, and never saved to IBP storage<br>
	 * empty values are saved to storage as '$empty$' if exist in metadata
	 * @param prefs exo preferences object. when empty - all preferences are cleared
	 * @param uid portlet window uid
	 */
	private static void storePreferences(ExoPortletPreferences prefs, String uid)
	{	
		//create a querystring for values
		if(prefs==null)
			return;
		StringBuffer qs=new StringBuffer();
		String name=null;
		String value=null;
		for(Enumeration e=prefs.getNames();e.hasMoreElements();)
		{
			name=(String)e.nextElement();
			value=prefs.getValue(name,"");
			qs.append(name).append("=").append(URLEncode(value)).append("&");
			System.out.println("ENExoPortletPreferencesFilePersisterImpl.store("+uid+")->"+name+"="+value);
		}
		
		//load and store
		Properties props=loadProperties();
		if(props==null)
			props=new Properties();
		props.setProperty(uid,qs.toString());
		storeProperties(props);
	}
	
	/**
	 * get file for storage
	 * @return the file 
	 */
	private static File getStorageFile()
	{
		String fileName=System.getProperty("ENExoPortletPreferencesFilePersisterImpl"
			,"C:/ENExoPortletPreferencesFilePersisterImpl.properties");
		return new File(fileName);
	}
	
	/**
	 * load properties file
	 * @return properties
	 */
	private static Properties loadProperties()
	{
		Properties props=null;
		InputStream is=null;
		try
		{
			props=new Properties();
			File f=getStorageFile();
			if(f.exists())
			{
				is=new FileInputStream(f);
				props.load(is);
			}
		}
		catch(IOException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try{is.close();}catch(Exception e){}
		}
		return props;
	}
	
	/**
	 * store properties file
	 * @return properties
	 */
	private static void storeProperties(Properties props)
	{
		OutputStream os=null;
		try
		{
			os=new FileOutputStream(getStorageFile());
			props.store(os,null);
		}
		catch(IOException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try{os.close();}catch(Exception e){}
		}
	}
	
	/**
	 * is value empty? (null or empty string)
	 * @param val value to check
	 * @return true if empty
	 */
	public static boolean isEmpty(String val)
	{
		return (!(val!=null && val.length()>0));
	}

	/**
	* convert qry string into a 2 dimention array
	* @param qs query string
	* @return array representation of query string
	*/
	public static String[][] qryStringToArray(String qs)
	{
		if (isEmpty(qs))
			return null;
		//break by '&'
		java.util.StringTokenizer pairs =
			new java.util.StringTokenizer(qs, "&");
		java.util.StringTokenizer pair = null;
		String[][] o = null;
		int length = pairs.countTokens();
		if (length > 0)
		{
			o = new String[length][2];
			int counter = 0;
			while (pairs.hasMoreTokens())
			{
				//break by '='
				pair = new java.util.StringTokenizer(pairs.nextToken(), "=");
				if (pair.countTokens() > 0)
				{
					o[counter][0] = URLDecode(pair.nextToken());
					if (pair.countTokens() > 0)
						o[counter][1] = URLDecode(pair.nextToken());
				}
				counter++;
			}
		}
		return o;
	}
	
	/**
	 * URL encode a given string
	 * @param text text to decode
	 * @return encoded string
	 */
	public static String URLEncode(String text)
	{
		String ret=text;
		try
		{
			ret=URLEncoder.encode(text);
		}
		catch(Exception e)
		{
		}
		return ret;
	}
	
	/**
	 * URL decode a given string
	 * @param encodedText text to encode
	 * @return decoded string
	 */
	public static String URLDecode(String encodedText)
	{
		String ret=encodedText;
		try
		{
			ret=URLDecoder.decode(encodedText);
		}
		catch(Exception e)
		{
		}
		return ret;
	}
}
