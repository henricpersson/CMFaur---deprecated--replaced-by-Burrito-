package cmfaur.client.sitelet;

import java.util.List;



import cmfaur.client.crud.CrudNameIdPair;
import cmfaur.client.crud.generic.CrudEntityInfo;
import cmfaur.client.dto.SiteletDescription;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SiteletServiceAsync {

	void getSitelets(String containerId,
			AsyncCallback<List<CrudNameIdPair>> asyncCallback);

	void getSitelet(Long siteletId, AsyncCallback<SiteletDescription> asyncCallback);

	void addSitelet(String containerName, String entityName, Long savedId,
			AsyncCallback<Void> asyncCallback);

	void saveSiteletOrder(String containerName, List<Long> longOrder,
			AsyncCallback<Void> asyncCallback);

	void deleteSitelets(String containerName, List<Long> ids, AsyncCallback<Void> callback);

	void getSiteletTypes(AsyncCallback<List<CrudEntityInfo>> asyncCallback);
	
	void clearCache(Long siteletId, AsyncCallback<Void> callback);

}
