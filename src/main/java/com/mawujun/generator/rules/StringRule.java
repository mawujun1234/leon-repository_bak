package com.mawujun.generator.rules;

public class StringRule extends AbstractRule{
	private String type="'string'";
	
	
	private Integer min;
	private Integer max;
	
	private String trigger= "['blur', 'change']";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
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
		if(min!=null) {
			builder.append(",min:"+min);
		}
		if(max!=null) {
			builder.append(",max:"+max);
		}
		builder.append(",trigger:"+trigger);
		builder.append("}");
		
		return builder.toString();
	}

}
