package planner.algorithm.pop.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RandomSet<E> extends ArrayList<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2044138032137271587L;
//	private Random rand = new Random();
	private Random rand = new Random(100);
	
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
		E item = this.get(index);
		this.remove(index);
		return item;
	}
	
	/**
	 * Returns random item from collection wothout removing it
	 */
	public E pollRandomItem(){
		int index = rand.nextInt(this.size());
		return this.get(index);
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
