package cmfaur.client.crud.generic.fields;

import cmfaur.client.crud.generic.CrudField;
/**
 * A {@link CrudField} representating a {@link Long}
 * @author henper
 *
 */
public class LongField extends CrudField {

	private Long value;
	
	public LongField() {
		// default constructor
		super();
	}
	
	public LongField(Long value) {
		this.value = value;
	}



	@Override
	public Class getType() {
		return Long.class;
	}

	@Override
	public Object getValue() {
		return value;
	}
	
	@Override
	public void setValue(Object value) {
		this.value = (Long) value;
	}

}
