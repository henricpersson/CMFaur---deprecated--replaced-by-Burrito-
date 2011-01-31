package cmfaur.client;


import cmfaur.client.crud.CrudPanel;
import cmfaur.client.sitelet.SiteletAdminPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Cmfaur implements EntryPoint {
	
	public void onModuleLoad() {
		RootPanel adminPanel = RootPanel.get("cmfaur-admin");		
		if (adminPanel != null) {
			String siteletContainerId = Window.Location.getParameter("container");
			if (siteletContainerId != null && !siteletContainerId.isEmpty()) {
				SiteletAdminPanel siteletAdminPanel = new SiteletAdminPanel(siteletContainerId);
				adminPanel.add(siteletAdminPanel);
				return;
			} else {
				CrudPanel crud = new CrudPanel();
				adminPanel.add(crud);
				return;
			}
		}
		
		
	}
}
