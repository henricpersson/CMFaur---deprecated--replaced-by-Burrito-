package cmfaur.client.crud;

import java.util.List;

import cmfaur.client.crud.generic.CrudEntityDescription;
import cmfaur.client.crud.generic.CrudEntityInfo;
import cmfaur.client.crud.generic.CrudEntityList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.kanal5.play.client.widgets.panels.table.PageMetaData;

@RemoteServiceRelativePath("crud")
public interface CrudService extends RemoteService {

	/**
	 * Describes an entity. The description will contain meta data about the
	 * entity fields and also hold its values.
	 * 
	 * @param entityName
	 *            The class name of the entity to edit, e.g. "models.Program"
	 * @param id
	 *            the id of the entity to describe. If -1, then a new instance
	 *            of the entity is described
	 * @param copyFromId
	 *            <code>null</code> or a database id of an entity to copy values
	 *            from
	 * @return
	 */
	CrudEntityDescription describe(String entityName, Long id, Long copyFromId);

	/**
	 * Saves an entity to the database. If a new instance is created the
	 * instance will be inserted into the db. If an existing one is being
	 * edited, this instance will be updated in the db.
	 * 
	 * @param updateCrudEntityDescription
	 * @return the id of the created or updated entity
	 */
	Long save(CrudEntityDescription updateCrudEntityDescription);

	/**
	 * Lists all database rows from a given entity.
	 * 
	 * @param entityName
	 *            The class name of the entity to edit, e.g. "models.Program"
	 * @param p
	 *            the current page to show.
	 * @return
	 */
	CrudEntityList listEntities(String filter, String entityName,
			PageMetaData<String> p);

	/**
	 * Deletes a set of entities from the db.
	 * 
	 * @param selected
	 */
	void deleteEntities(List<CrudEntityDescription> selected);

	/**
	 * Gets all crud enabled entities
	 * 
	 * @return
	 */
	List<CrudEntityInfo> getAllEntities();

	/**
	 * Checks if class is a crud enabled entity
	 * 
	 * @return
	 */
	Boolean isCrudEnabled(String className);

	/**
	 * Used to fetch headers for a specific entity. Can be used in a table
	 * listing.
	 * 
	 * @param entityName
	 *            The class name of the entity to fetch headers for, e.g.
	 *            "models.Program"
	 * @return
	 */
	CrudEntityDescription getEntityHeaders(String entityName);

	/**
	 * Gets all rows from the db of a specific entity.
	 * 
	 * @param entityName
	 *            The class name of the entity to list, e.g. "models.Program"
	 * @return
	 */
	List<CrudNameIdPair> getListValues(String entityName);

	/**
	 * List of values from an enum type.
	 * 
	 * @param type
	 * @return
	 */
	List<String> getEnumListValues(String classNameType);

	/**
	 * Gets entity types that can be linked
	 * 
	 * @return
	 */
	List<String> getLinkableTypes();

	/**
	 * Describes an embedded object in an entity
	 * 
	 * @param embeddedClassName
	 * @return
	 */
	CrudEntityDescription describeEmbeddedObject(String embeddedClassName);

}
