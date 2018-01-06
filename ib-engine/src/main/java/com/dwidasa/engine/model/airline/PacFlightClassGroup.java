package com.dwidasa.engine.model.airline;

import java.util.List;

public class PacFlightClassGroup {
	private List<PacFlightClass> pacFlightClasses;

	public List<PacFlightClass> getPacFlightClasses() {
		return pacFlightClasses;
	}

	public void setPacFlightClasses(List<PacFlightClass> pacFlightClasses) {
		this.pacFlightClasses = pacFlightClasses;
	}

	public  String toString()
	{
		if (pacFlightClasses == null) return "pacFlightClasses=null";
		String res = "";
		for (PacFlightClass pacClass : pacFlightClasses) {
			res += pacClass.toString() + " - ";
		}
	    return res;
	}
}
