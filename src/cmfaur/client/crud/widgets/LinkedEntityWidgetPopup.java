package cmfaur.client.crud.widgets;

import java.util.ArrayList;
import java.util.List;



import cmfaur.client.crud.CrudService;
import cmfaur.client.crud.CrudServiceAsync;
import cmfaur.client.crud.labels.CrudLabelHelper;
import cmfaur.client.crud.labels.CrudMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.kanal5.play.client.widgets.inputfield.StringInputField;
import com.kanal5.play.client.widgets.inputfield.URLInputfield;
import com.kanal5.play.client.widgets.layout.VerticalSpacer;
import com.kanal5.play.client.widgets.selection.SelectionList;
import com.kanal5.play.client.widgets.selection.SelectionListLabelCreator;

public class LinkedEntityWidgetPopup extends DialogBox {
	public static interface SaveHandler {
		void saved(String json);
	}

	private LinkedEntityJsonMessages jsonMessages = GWT
			.create(LinkedEntityJsonMessages.class);
	public static final String TYPE_ABSOLUTE_URL = "link_absolute";

	private VerticalPanel wrapper = new VerticalPanel();
	private CrudServiceAsync service = GWT.create(CrudService.class);
	private SelectionList<String> type = new SelectionList<String>(false);
	private SimplePanel valuesWrapper = new SimplePanel();
	private RelationSelectionList relationSelectionList;
	private StringInputField linkText = new StringInputField(true);
	private URLInputfield url = new URLInputfield(true);

	private CrudMessages labels = GWT.create(CrudMessages.class);

	private Long typeIdWaitingToBeSet;

	private VerticalPanel urlWrapper;

	public LinkedEntityWidgetPopup(final SaveHandler saveHandler) {
		super(false, true);
		setText(labels.createNewLink());
		type.setLabelCreator(new SelectionListLabelCreator<String>() {

			@Override
			public String createLabel(String typeClassName) {
				return CrudLabelHelper.getString(typeClassName
						.replace('.', '_') + "_singular");
			}
		});
		type.setNullSelectLabel(labels.chooseLinkType());
		type.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				handleTypeChange();
			}
		});
		wrapper.add(new Label(labels.linkText()));
		wrapper.add(linkText);
		wrapper.add(new Label(labels.selectLinkTo()));
		wrapper.add(type);
		wrapper.add(valuesWrapper);

		urlWrapper = new VerticalPanel();
		urlWrapper.setVisible(false);
		urlWrapper.add(new Label(labels.writeOrPasteLink()));
		urlWrapper.add(url);
		wrapper.add(urlWrapper);

		HorizontalPanel hp = new HorizontalPanel();

		hp.add(new Button(labels.save(), new ClickHandler() {

			public void onClick(ClickEvent event) {
				doSave(saveHandler);
			}
		}));
		hp.add(new Button(labels.cancel(), new ClickHandler() {

			public void onClick(ClickEvent event) {
				removeFromParent();
			}
		}));
		wrapper.add(new VerticalSpacer(10));
		wrapper.add(hp);
		setWidget(wrapper);
		addStyleName("k5-LinkedEntityWidget");

		init();
	}

	protected void doSave(SaveHandler saveHandler) {
		if (urlWrapper.isVisible() && linkText.validate() && url.validate()) {
			saved(saveHandler);
		} else if (linkText.validate() && type.validate()) {
			saved(saveHandler);
		}
	}

	private void saved(SaveHandler saveHandler) {
		saveHandler.saved(toJson());
		removeFromParent();
	}

	protected void handleTypeChange() {
		valuesWrapper.clear();
		urlWrapper.setVisible(false);
		relationSelectionList = null;
		if (type.getValue() == null) {
			return;
		}
		String typeClassName = type.getValue();
		if (TYPE_ABSOLUTE_URL.equals(typeClassName)) {
			urlWrapper.setVisible(true);
			return;
		}
		relationSelectionList = new RelationSelectionList(false, typeClassName);
		relationSelectionList.setValue(typeIdWaitingToBeSet);
		typeIdWaitingToBeSet = null;
		VerticalPanel vp = new VerticalPanel();
		vp.add(new Label(labels.selectLinkToEntity(CrudLabelHelper.getString(typeClassName.replace('.', '_') + "_singular"))));
		vp.add(relationSelectionList);
		valuesWrapper.setWidget(vp);

	}

	private void init() {
		service.getLinkableTypes(new AsyncCallback<List<String>>() {

			public void onSuccess(List<String> result) {
				loadTypes(result);
			}

			public void onFailure(Throwable caught) {
				throw new RuntimeException("Failed to get linkable types",
						caught);
			}
		});
	}

	private void loadTypes(List<String> result) {
		List<String> extendedModel = new ArrayList<String>();
		extendedModel.add(TYPE_ABSOLUTE_URL);
		extendedModel.addAll(result);
		type.setModel(extendedModel);
		type.render();
	}

	public String toJson() {
		String typeValue = type.getValue();
		if (type.getValue() == null) {
			return null;
		}
		String linkText = this.linkText.getValue();
		if (linkText == null || linkText.isEmpty()) {
			return null;
		}
		if (relationSelectionList != null) {
			Long typeId = (Long) relationSelectionList.getValue();
			return jsonMessages.createJsonForType(linkText, typeValue, typeId);
		}
		String strUrl = url.getValue();
		if (strUrl == null) {
			return null;
		}
		return jsonMessages.createJsonForAbsoluteLink(linkText,
				TYPE_ABSOLUTE_URL, strUrl);

	}

	private final native LinkedEntityJsonOverlay asLinkedEntity(String json) /*-{
																				return eval("json=" + json);
																				}-*/;

	public void fromJson(String json) {
		if (json == null || json.isEmpty()) {
			type.setValue(null);
			return;
		}
		try {
			LinkedEntityJsonOverlay linked = asLinkedEntity(json);
			if (linked.getTypeId() > 0) {
				typeIdWaitingToBeSet = Long.valueOf(linked.getTypeId());
			}
			linkText.setValue(linked.getLinkText());
			type.setValue(linked.getTypeClassName());
			url.setValue(linked.getAbsoluteLink());
		} catch (Exception e) {
			GWT.log("Failed to parse json: " + json, e);
		}
		handleTypeChange();
	}

}
