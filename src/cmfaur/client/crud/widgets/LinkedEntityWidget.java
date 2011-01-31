package cmfaur.client.crud.widgets;

import java.util.ArrayList;
import java.util.List;



import cmfaur.client.crud.labels.CrudMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.kanal5.play.client.widgets.validation.HasValidators;
import com.kanal5.play.client.widgets.validation.InputFieldValidator;

public class LinkedEntityWidget extends Composite implements HasValidators {
	private VerticalPanel wrapper = new VerticalPanel();
	private CrudMessages labels = GWT.create(CrudMessages.class);
	private boolean required;
	private Label validationError = new Label(labels.linkIsRequired());
	private SimplePanel linkViewPlaceholder = new SimplePanel();
	private Anchor add = new Anchor(labels.createNewLink());
	private boolean allowMultiple;
	private List<LinkedEntityValue> added = new ArrayList<LinkedEntityValue>();

	/**
	 * Create a new linked entity widget.
	 * 
	 * @param required
	 *            true if at least one link must be specified
	 * @param allowMultiple
	 *            true if multiple links can be entered. In this case, use
	 *            getValues() to fetch a list of links. IF false, use getValue()
	 *            to get the only link.
	 */
	public LinkedEntityWidget(boolean required, boolean allowMultiple) {
		this.required = required;
		this.allowMultiple = allowMultiple;
		validationError.addStyleName("validationError");
		validationError.setVisible(false);
		wrapper.add(validationError);
		wrapper.add(linkViewPlaceholder);
		add.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				LinkedEntityWidgetPopup popup = new LinkedEntityWidgetPopup(
						new LinkedEntityWidgetPopup.SaveHandler() {

							public void saved(String json) {
								addLink(json);
							}
						});
				popup.center();
				popup.show();
			}
		});
		wrapper.add(add);
		initWidget(wrapper);
	}

	public void addInputFieldValidator(InputFieldValidator validator) {
		throw new UnsupportedOperationException();
	}

	public void setValidationError(String validationError) {
		throw new UnsupportedOperationException();
	}

	public boolean validate() {
		validationError.setVisible(false);
		if (required && getValue() == null) {
			validationError.setVisible(true);
			return false;
		}
		return true;
	}

	/**
	 * Gets the json content
	 * 
	 * @return
	 */
	public String getValue() {
		if (added.size() == 0) {
			return null;
		}
		LinkedEntityValue v = added.get(0);
		return v.getJson();
	}

	private void addLink(String json) {
		if (json == null) {
			return;
		}
		if (!allowMultiple) {
			add.setVisible(false);
		}
		final LinkedEntityValue v = new LinkedEntityValue(json);
		added.add(v);

		reloadLinkViews();
	}

	protected void reloadLinkViews() {
		VerticalPanel vp = new VerticalPanel();
		for (final LinkedEntityValue v : added) {
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(v);
			Anchor remove = new Anchor(labels.delete());
			remove.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					added.remove(v);
					reloadLinkViews();
					add.setVisible(true);
				}
			});
			hp.add(remove);
			vp.add(hp);
		}
		linkViewPlaceholder.setWidget(vp);
	}

	/**
	 * Sets the only link to be displayed
	 * 
	 * @param json
	 */
	public void setValue(String json) {
		added.clear();
		addLink(json);
	}
	
	public void setValues(List<String> jsonLinks) {
		added.clear();
		if (jsonLinks == null) {
			return;
		}
		for (String json : jsonLinks) {
			addLink(json);
		}
	}

	public List<String> getValues() {
		List<String> result = new ArrayList<String>();
		for (LinkedEntityValue v : added) {
			result.add(v.getJson());
		}
		return result;
	}

}
