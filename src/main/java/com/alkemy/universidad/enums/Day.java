package com.alkemy.universidad.enums;

public enum Day {
	LUNES("LUNES"), MARTES("MARTES"), MIERCOLES("MIERCOLES"), JUEVES("JUEVES"), VIERNES("VIERNES"), SABADO("SABADO");
	
	private String displayName;

	  Day (String displayName) {
	      this.displayName = displayName;
	  }

	  public String displayName() { return displayName; }
}
