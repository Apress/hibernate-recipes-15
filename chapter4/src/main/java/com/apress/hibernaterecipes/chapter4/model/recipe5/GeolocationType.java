package com.apress.hibernaterecipes.chapter4.model.recipe5;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

public class GeolocationType implements CompositeUserType {

	@Override
	public Class<?> returnedClass() {
		return byte[].class;
	}

	@Override
	public String[] getPropertyNames() {
		return new String[] { "latitude", "longitude" };
	}

	@Override
	public Type[] getPropertyTypes() {
		return new Type[] { BigDecimalType.INSTANCE, BigDecimalType.INSTANCE };
	}


	@Override
	public boolean equals(Object o, Object o2) throws HibernateException {
		return o.equals(o2);
	}

	@Override
	public int hashCode(Object o) throws HibernateException {
		return o.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet resultSet, String[] names,
			SessionImplementor sessionImplementor, Object o)
			throws HibernateException, SQLException {
		assert names.length == 2;
		BigDecimal latitude = (BigDecimal) BigDecimalType.INSTANCE.get(
				resultSet, names[0], sessionImplementor);
		BigDecimal longitude = (BigDecimal) BigDecimalType.INSTANCE.get(
				resultSet, names[1], sessionImplementor);
		return latitude == null && longitude == null ? null : new Geolocation(
				latitude, longitude);
	}

	@Override
	public void nullSafeSet(PreparedStatement preparedStatement, Object value,
			int index, SessionImplementor sessionImplementor)
			throws HibernateException, SQLException {
		if (value == null) {
			BigDecimalType.INSTANCE.set(preparedStatement, null, index,
					sessionImplementor);
			BigDecimalType.INSTANCE.set(preparedStatement, null, index + 1,
					sessionImplementor);
		} else {
			Geolocation location = (Geolocation) value;
			BigDecimalType.INSTANCE.set(preparedStatement,
					location.getLatitude(), index, sessionImplementor);
			BigDecimalType.INSTANCE.set(preparedStatement,
					location.getLongitude(), index + 1, sessionImplementor);
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object getPropertyValue(Object component, int property)
			throws HibernateException {
		if (component == null) {
			return null;
		}
		Geolocation location = (Geolocation) component;
		switch (property) {
		case 0:
			return location.getLatitude();
		case 1:
			return location.getLongitude();
		default:
			throw new HibernateException("invalid property index " + property);
		}
	}

	@Override
	public void setPropertyValue(Object component, int property, Object value)
			throws HibernateException {
		if (component == null) {
			return;
		}
		Geolocation location = (Geolocation) component;
		// all of our properties are BigDecimal, so this is safe
		BigDecimal val = (BigDecimal) value;
		switch (property) {
		case 0:
			location.setLatitude(val);
			break;
		case 1:
			location.setLongitude(val);
			break;
		default:
			throw new HibernateException("invalid property index " + property);
		}
	}

	@Override
	public Serializable disassemble(Object value, SessionImplementor session)
			throws HibernateException {
		return null;
	}

	@Override
	public Object assemble(Serializable cached, SessionImplementor session,
			Object owner) throws HibernateException {
		return null;
	}

	@Override
	public Object replace(Object original, Object target,
			SessionImplementor session, Object owner) throws HibernateException {
		return null;
	}
}
