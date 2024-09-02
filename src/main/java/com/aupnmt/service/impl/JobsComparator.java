package com.aupnmt.service.impl;

import com.aupnmt.dto.Jobs;

public class JobsComparator implements java.util.Comparator<Jobs>{

	@Override
	public int compare(Jobs o1, Jobs o2) {
		return o2.getModifiedDate().compareTo(o1.getModifiedDate());
	}

}
