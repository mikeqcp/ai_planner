package planner.algorithm.pop.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RandomSet<E> extends ArrayList<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2044138032137271587L;
//	private static Random rand = new Random(6);
	private Random rand = new Random();
	
	public RandomSet(Collection<E> collection) {
		super(collection);
	}

	public RandomSet() {
		super();
	}
	
	/**
	 * Returns and removes random item from collection
	 */
	public E getRandomItem(){
		int index = rand.nextInt(this.size());
		return this.remove(index);
	}
	
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		for (E e : c) {
			if(!contains(e))
				add(e);
		}
		return true;
	}
}
