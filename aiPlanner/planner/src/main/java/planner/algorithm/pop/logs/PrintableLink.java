package planner.algorithm.pop.logs;

import planner.algorithm.pop.model.CasualLink;
import planner.algorithm.pop.model.GraphLink;

public class PrintableLink {
	private long idFrom;
	private long idTo;
	private String param;
	private String type;
	
	public PrintableLink(GraphLink link) {
		this.idFrom = link.getNodeFrom().getId();
		this.idTo = link.getNodeTo().getId();
		
		if (link instanceof CasualLink){
			this.type = "casual";
			this.param = ((CasualLink)link).getAchieves().toString();
		} else { 	//must be order link
			this.type = "order";
			this.param = "";
		}
	}

	
	
	public void setParam(String param) {
		this.param = param;
	}



	public long getIdFrom() {
		return idFrom;
	}

	public long getIdTo() {
		return idTo;
	}

	public String getParam() {
		return param;
	}

	public String getType() {
		return type;
	}
}
