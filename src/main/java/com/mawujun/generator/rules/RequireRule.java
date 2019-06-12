package com.mawujun.generator.rules;

public class RequireRule extends AbstractRule{
	private String type="'string'";
	private Boolean  required=true;
	
	
	
	private String trigger= "'blur'";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}


	@Override
	public String toString() {
		StringBuilder builder=new  StringBuilder();
		builder.append("{");
		builder.append("type:"+type);
		builder.append(",required:"+required);
		if(super.getMessage()!=null) {
			builder.append(",message:'"+super.getMessage()+"'");
		}
		builder.append(",trigger:"+trigger);
		builder.append("}");
		
		return builder.toString();
	}

}
