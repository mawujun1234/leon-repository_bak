package com.mawujun.generator.rules;

public class EmailRule extends AbstractRule{
	private String type="'email'";
	
	
	private String trigger= "['blur', 'change']";

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

	@Override
	public String toString() {
		StringBuilder builder=new  StringBuilder();
		builder.append("{");
		builder.append("type:"+type);
		if(super.getMessage()!=null) {
			builder.append(",message:"+super.getMessage());
		}
		builder.append(",trigger:"+trigger);
		builder.append("}");
		
		return builder.toString();
	}


}
