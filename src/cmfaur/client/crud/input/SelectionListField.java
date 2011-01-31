package cmfaur.client.crud.input;


import cmfaur.client.crud.CrudServiceAsync;
import cmfaur.client.crud.generic.CrudField;
import cmfaur.client.crud.generic.fields.ManyToOneRelationField;
import cmfaur.client.crud.widgets.RelationSelectionList;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.kanal5.play.client.widgets.form.EditForm;
import com.kanal5.play.client.widgets.validation.HasValidators;
import com.kanal5.play.client.widgets.validation.InputFieldValidator;

public class SelectionListField implements CrudInputField {
	
	class RelationSelectionListWrapper extends Composite implements
			HasChangeHandlers, HasValidators {

		private VerticalPanel wrapper = new VerticalPanel();
		public RelationSelectionListWrapper(CrudServiceAsync service) {
			wrapper.add(selectionList);

			final String relatedEntityClassName = field.getRelatedEntityName();

			service.isCrudEnabled(relatedEntityClassName, new AsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					if (result) {
						RelatedEntityEditAnchor addNew = new RelatedEntityEditAnchor(relatedEntityClassName, new EditForm.SaveCancelListener() {

							public void onSave() {
								selectionList.load();
							}

							public void onPartialSave(String warning) {
								//do nothing
							}

							public void onCancel() {
								//do nothing
							}
						});

						wrapper.add(addNew);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					throw new RuntimeException(caught);
				}
			});

			initWidget(wrapper);
		}

		public HandlerRegistration addChangeHandler(ChangeHandler handler) {
			return selectionList.addChangeHandler(handler);
		}

		public void addInputFieldValidator(InputFieldValidator validator) {
			selectionList.addInputFieldValidator(validator);
		}

		public void setValidationError(String validationError) {
			selectionList.setValidationError(validationError);
		}

		public boolean validate() {
			return selectionList.validate();
		}
	}

	private ManyToOneRelationField field;
	private RelationSelectionList selectionList;
	private RelationSelectionListWrapper selectionListWrapper;

	public SelectionListField(final ManyToOneRelationField field, CrudServiceAsync service) {
		this.field = field;
		this.selectionList = new RelationSelectionList(field.isRequired(), field.getRelatedEntityName());
		this.selectionList.setValue((Long) field.getValue());
		selectionListWrapper = new RelationSelectionListWrapper(service);
	}

	public CrudField getCrudField() {
		field.setValue(selectionList.getValue());
		return field;
	}

	public Widget getDisplayWidget() {
		return selectionListWrapper;
	}

	public Object getValue() {
		return selectionList.getValue();
	}

	public void load(Object value) {
		selectionList.setValue((Long) value);
	}

}
