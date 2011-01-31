package cmfaur.client.sitelet;

import java.util.List;



import cmfaur.client.crud.CrudEntityEdit;
import cmfaur.client.crud.CrudService;
import cmfaur.client.crud.CrudServiceAsync;
import cmfaur.client.crud.generic.CrudEntityDescription;
import cmfaur.client.crud.generic.CrudEntityInfo;
import cmfaur.client.crud.labels.CrudLabelHelper;
import cmfaur.client.crud.labels.CrudMessages;
import cmfaur.client.dto.SiteletDescription;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.kanal5.play.client.widgets.form.EditForm;
import com.kanal5.play.client.widgets.layout.VerticalSpacer;

public class SiteletEditPanel extends Composite {

	private VerticalPanel wrapper = new VerticalPanel();
	private VerticalPanel types = new VerticalPanel();
	private SimplePanel editFormPlaceHolder = new SimplePanel();
	private CrudServiceAsync crudService = GWT.create(CrudService.class);
	private SiteletServiceAsync service = GWT.create(SiteletService.class);
	private String containerName;
	private static CrudMessages labels = GWT.create(CrudMessages.class);


	public SiteletEditPanel(String containerName, Long id) {
		this.containerName = containerName;
		wrapper.add(types);
		wrapper.add(editFormPlaceHolder);
		if (id.longValue() == -1L) {
			service
					.getSiteletTypes(new AsyncCallback<List<CrudEntityInfo>>() {

						public void onSuccess(List<CrudEntityInfo> result) {
							types.add(new Label(labels.selectSiteletType()));
							types.add(new VerticalSpacer(5));
							for (CrudEntityInfo crudEntityInfo : result) {
								addEntityOption(crudEntityInfo);
							}

						}

						public void onFailure(Throwable caught) {
							throw new RuntimeException(
									"Failed to get sitelet types");
						}
					});
		} else {
			service.getSitelet(id, new AsyncCallback<SiteletDescription>() {

				public void onSuccess(SiteletDescription result) {
					loadEntity(result.getEntityName(), result.getEntityId());
				}

				public void onFailure(Throwable caught) {
					throw new RuntimeException("Failed to get sitelet", caught);
				}
			});
		}
		initWidget(wrapper);
	}

	private Anchor createTypeLink(final String entityName, String displayString) {
		Anchor a = new Anchor(displayString);
		a.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				loadEntity(entityName, -1l);
				types.setVisible(false);
			}
		});
		return a;
	}

	protected void loadEntity(final String entityName, final Long entityId) {
		crudService.describe(entityName, entityId, null,
				new AsyncCallback<CrudEntityDescription>() {

					public void onSuccess(CrudEntityDescription result) {
						final CrudEntityEdit editForm = new CrudEntityEdit(
								result);
						editForm
								.setSaveCancelListener(new EditForm.SaveCancelListener() {

									public void onSave() {
										if (entityId.longValue() == -1l) {
											doSaveSitelet(entityName, editForm
													.getSavedId());
										} else {
											History.newItem("");
										}
										clearCache(editForm
												.getSavedId());
									}


									public void onPartialSave(String warning) {
										throw new UnsupportedOperationException();
									}

									public void onCancel() {
										History.newItem("");
									}
								});
						editFormPlaceHolder.setWidget(editForm);
					}

					public void onFailure(Throwable caught) {
						throw new RuntimeException(
								"Failed to load entity to edit: " + entityName,
								caught);
					}
				});
	}

	private void doSaveSitelet(String entityName, Long savedId) {
		service.addSitelet(containerName, entityName, savedId,
				new AsyncCallback<Void>() {

					public void onSuccess(Void result) {
						History.newItem("");
					}

					public void onFailure(Throwable caught) {
						throw new RuntimeException(
								"Failed to add sitelet to sitelet container",
								caught);
					}
				});
	}

	private void addEntityOption(CrudEntityInfo crudEntityInfo) {
		HorizontalPanel fp = new HorizontalPanel();
		String underscore = crudEntityInfo.getEntityName().replace('.', '_');
		fp.add(createTypeLink(crudEntityInfo.getEntityName(), CrudLabelHelper
				.getString(underscore)));
		String desc = CrudLabelHelper.getNullableString(underscore + "_desc");
		if (desc != null) {
			fp.add(new HTML("&nbsp;-&nbsp;" + desc));
		}
		types.add(fp);
		types.add(new VerticalSpacer(10));
	}
	

	private void clearCache(Long id) {
		service.clearCache(id, new AsyncCallback<Void>() {
			
			public void onSuccess(Void result) {
				//do nothing
			}
			
			public void onFailure(Throwable caught) {
				throw new RuntimeException("Failed to clear cache for " + containerName, caught);
			}
		});
	}

}
