package com.apress.hibernaterecipes.chapter4.model.recipe5;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Geolocation implements Serializable {
	BigDecimal latitude;
	BigDecimal longitude;

	/*
	 * We don't use @AllArgsConstructor because we want to make sure our
	 * attributes align. No mixups of longitude and latitude for us, no sir!
	 */
	public Geolocation(BigDecimal latitude, BigDecimal longitude) {
		this.latitude=latitude;
		this.longitude=longitude;
	}

	public Geolocation(double latitude, double longitude) {
		this(new BigDecimal(latitude),new BigDecimal(longitude));
	}
}
