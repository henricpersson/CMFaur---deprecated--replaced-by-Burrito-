package cmfaur.client.crud.input;


import cmfaur.client.crud.CrudEntityEdit;
import cmfaur.client.crud.CrudService;
import cmfaur.client.crud.CrudServiceAsync;
import cmfaur.client.crud.generic.CrudEntityDescription;
import cmfaur.client.crud.labels.CrudLabelHelper;
import cmfaur.client.crud.labels.CrudMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.kanal5.play.client.widgets.InfoMessagePopup;
import com.kanal5.play.client.widgets.form.EditForm;

public class RelatedEntityEditAnchor extends Anchor {

	private static CrudServiceAsync service = GWT.create(CrudService.class);
	private static CrudMessages labels = GWT.create(CrudMessages.class);
	private InfoMessagePopup popup = new InfoMessagePopup();

	public RelatedEntityEditAnchor(final String relatedEntityClassName, final EditForm.SaveCancelListener listener) {
		final String newLabel = CrudLabelHelper
				.getString(relatedEntityClassName.replace('.', '_') + "_new");
		setText(newLabel);
		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				final DialogBox box = new DialogBox(false);
				box.addStyleName("k5-RelationSelectionListWrapper-entity-edit");
				box.setText(newLabel);
				service.describe(relatedEntityClassName, -1L, null,
						new AsyncCallback<CrudEntityDescription>() {

							public void onSuccess(CrudEntityDescription result) {
								CrudEntityEdit crud = new CrudEntityEdit(result);
								crud
										.setSaveCancelListener(new EditForm.SaveCancelListener() {

											public void onSave() {
												box.removeFromParent();
												popup
														.setTextAndShow(labels
																.entityAddedAndCanBeSelected());
												listener.onSave();
											}

											public void onPartialSave(
													String warning) {
												throw new UnsupportedOperationException();
											}

											public void onCancel() {
												box.removeFromParent();
												listener.onCancel();
											}
										});
								box.setWidget(crud);
								box.center();
							}

							public void onFailure(Throwable caught) {
								throw new RuntimeException(
										"Failed to describe entity", caught);
							}
						});
				box.setWidget(new Label(labels.loading()));
				box.center();
				box.show();
			}
		});
	}
}
