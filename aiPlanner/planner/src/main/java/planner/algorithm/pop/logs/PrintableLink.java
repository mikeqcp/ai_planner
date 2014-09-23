package planner.algorithm.pop.logs;

import planner.algorithm.pop.model.CasualLink;
import planner.algorithm.pop.model.GraphLink;
import planner.algorithm.pop.model.GraphNode;
import planner.algorithm.pop.model.SolutionGraph;
import planner.algorithm.pop.model.SubGoal;
import planner.model.AtomicState;
import planner.model.ParameterBinding;

public class PrintableLink {
	private long idFrom;
	private long idTo;
	private String param;
	private String type;
	
	public PrintableLink(GraphLink link, SolutionGraph g) {
		this.idFrom = link.getNodeFrom().getId();
		this.idTo = link.getNodeTo().getId();
		
		if (link instanceof CasualLink){
			this.type = "casual";
			this.param = getAchieveString(g, (CasualLink)link);
		} else { 	//must be order link
			this.type = "order";
			this.param = "";
		}
	}

	private String getAchieveString(SolutionGraph g, CasualLink l){
//		().getAchieves().toString();
		
//		GraphNode node = g.getNodeById(oldNode.getId());
		GraphNode node =  l.getNodeTo();
		ParameterBinding currentBinding = node.getBinding();
		AtomicState updatedGoal = l.getAchieves().bind(currentBinding);
		return updatedGoal.toString();
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
