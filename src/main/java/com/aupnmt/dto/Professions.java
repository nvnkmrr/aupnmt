package com.aupnmt.dto;

public class Professions {

	private Long professionId;
	private String Profession;
	private boolean isOwnerProfession;
	private boolean isEmployeeProfession;

	public Long getProfessionId() {
		return professionId;
	}

	public void setProfessionId(Long professionId) {
		this.professionId = professionId;
	}

	public String getProfession() {
		return Profession;
	}

	public void setProfession(String profession) {
		Profession = profession;
	}

	public boolean isOwnerProfession() {
		return isOwnerProfession;
	}

	public void setOwnerProfession(boolean isOwnerProfession) {
		this.isOwnerProfession = isOwnerProfession;
	}

	public boolean isEmployeeProfession() {
		return isEmployeeProfession;
	}

	public void setEmployeeProfession(boolean isEmployeeProfession) {
		this.isEmployeeProfession = isEmployeeProfession;
	}

}
