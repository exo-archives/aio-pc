/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.pci.model;

import java.util.List;

/**
 * Jul 11, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Util.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class Util {
  
	static public String getDescription(String lang, List descriptions) {
		if (lang == null) return null;
		lang = lang.toLowerCase();
		for (int i = 0; i < descriptions.size(); i++) {
			Description desc = (Description) descriptions.get(i);
			String listLang = desc.getLang();
			if (listLang != null) {
				if (lang.equals(desc.getLang().toLowerCase())) {
					return desc.getDescription() ;
				}
			}
		}
		return null;
	}
}
