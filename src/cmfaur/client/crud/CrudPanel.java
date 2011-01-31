package cmfaur.client.crud;


import cmfaur.client.crud.generic.CrudEntityDescription;
import cmfaur.client.crud.labels.CrudMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Main entry panel for the automatic GWT CRUD module. This panel listens for
 * {@link History} events and responds to them. The format to be used as history
 * token is one of the following:
 * <ul>
 * <li># - lists all editable entities</li>
 * <li>#{entityName} - Shows a table with all database rows from an entity. E.g.
 * #models.Program</li>
 * <li>#{entityName}/{id} - shows an edit form for a database row from a
 * specified entity. If id is -1, the form is used to create a new entity</li>
 * </ul>
 * 
 * @author henper
 * 
 */
public class CrudPanel extends Composite implements ValueChangeHandler<String> {

	private SimplePanel content = new SimplePanel();
	private DockPanel wrapper = new DockPanel();
	private CrudPanelTop top = new CrudPanelTop();

	public CrudPanel() {
		History.addValueChangeHandler(this);
		wrapper.add(top, DockPanel.NORTH);
		content.addStyleName("k5-CrudPanel-content");
		top.addStyleName("k5-CrudPanel-top");
		wrapper.add(content, DockPanel.CENTER);
		initWidget(wrapper);
		History.fireCurrentHistoryState();
		addStyleName("k5-CrudPanel");
		setWidth("100%");
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		// history has changed
		String token = event.getValue();
		content.setWidget(getPanelBasedOnHistoryToken(token));
	}

	private Widget getPanelBasedOnHistoryToken(String token) {
		if (token == null || token.isEmpty()) {
			top.update(null, null);
			return new CrudIndexPanel();
		}
		String[] split = token.split("/");
		final String entityName = split[0];
		if (split.length == 1) {
			top.update(entityName, null);
			return new CrudEntityIndex(entityName);
		}
		if (split.length >= 2) {
			String strId = split[1];
			Long id = Long.parseLong(strId);
			Long copyFromId = null;
			if (split.length == 3) {
				copyFromId = Long.parseLong(split[2]);
			}
			final SimplePanel sp = new SimplePanel();

			// delay creation until entity has been fetched
			CrudServiceAsync service = GWT.create(CrudService.class);
			final CrudMessages messages = GWT.create(CrudMessages.class);
			service.describe(entityName, id, copyFromId,
					new AsyncCallback<CrudEntityDescription>() {

						public void onSuccess(CrudEntityDescription result) {
							sp.setWidget(new CrudEntityEdit(result));
							// update top again, now when values have been
							// fetched
							String disp = result.getDisplayString();
							if (result.isNew()) {
								disp = messages.newEntity();
							}
							top.update(entityName, disp);
						}

						public void onFailure(Throwable caught) {
							throw new RuntimeException(
									"Failed to get description of "
											+ entityName, caught);
						}
					});
			top.update(entityName, null);
			return sp;
		}
		return new Label("Failed to parse token");
	}

}
