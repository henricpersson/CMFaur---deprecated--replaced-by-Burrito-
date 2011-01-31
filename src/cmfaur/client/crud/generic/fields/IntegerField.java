package cmfaur.client.crud.generic.fields;

import java.io.Serializable;

import cmfaur.client.crud.generic.CrudField;



/**
 * A {@link CrudField} representating an Integer
 * 
 * @author henper
 * 
 */
public class IntegerField extends CrudField {

	private Integer value;

	public IntegerField() {
		// default constructor
	}

	public IntegerField(Integer integer) {
		this.value = integer;
	}

	@Override
	public Class getType() {
		return Integer.class;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = (Integer) value;
	}

}
