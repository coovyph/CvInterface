package com.phx.cvi.comp.mux;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IsoMuxKey {

	public static final int ST_RQST = 0;
	public static final int ST_TIMEOUT = 1;


	private String key;
	private int status;
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IsoMuxKey other = (IsoMuxKey) obj;
		return Objects.equals(key, other.key);
	}
	@Override
	public int hashCode() {
		return Objects.hash(key);
	}
	
	
}
