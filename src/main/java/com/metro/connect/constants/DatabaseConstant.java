package com.metro.connect.constants;

public class DatabaseConstant {
	
	public static enum MetroStatus {
		ACTIVE(1),
		NOT_ACTIVE(0);
		
		private int status;

	    private MetroStatus(int status) {
	      this.status = status;
	    }

	    public int value() {
	      return this.status;
	    }
	     
	}

}
