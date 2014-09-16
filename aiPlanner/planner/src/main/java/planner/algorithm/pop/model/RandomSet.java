package planner.algorithm.pop.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RandomSet<E> extends ArrayList<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2044138032137271587L;
	private Random rand = new Random();
	
	public RandomSet(Collection<E> collection) {
		super(collection);
	}

	public RandomSet() {
		super();
	}

	public E getRandomItem(){
		int index = rand.nextInt(this.size());
		E item = this.get(index);
		this.remove(index);
		return item;
	}
	
	public E pollRandomItem(){
		int index = rand.nextInt(this.size());
		return this.get(index);
	}
	
}
