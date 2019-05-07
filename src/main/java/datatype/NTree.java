package main.java.datatype;

import java.util.Iterator;
import java.util.List;

/**
 * This type represents a n-ary tree, denoted n-tree.
 * This tree is able to keep a multitude of children on each node.
 * 
 * The n-tree assumes a total ordering of the elements it keeps.
 * The n-tree does not have repetitions.
 * 
 * The n-tree node structure must respect the following invariant: 
 *   a prefix traversal produces an increasing ordered sequence 
 *  
 * @author Joao Pedro Neto
 *
 * @param <T> The type of elements kept in the n-tree
 */
public interface NTree<T extends Comparable<T>> extends Iterable<T> {
	
	/**
	 * Verifies if tree is empty
	 * @return true iff tree is empty
	 */
	public boolean isEmpty();

	/**
	 * Verifies if tree is a leaf, ie, only has one element
	 * @return true iff tree is a leaf
	 */
	public boolean isLeaf();
	
	/**
	 * The number of elements of a tree. An empty tree has zero elements
	 * @return the number of elements
	 */
	public int size();
	
	/**
	 * Count the number of leaves. An empty tree has zero leaves
	 * @return the number of leaves
	 */
	public int countLeaves();
	
	/**
	 * The tree's height. An empty tree has height zero, a leaf has height one
	 * @return the tree's height
	 */
	public int height();
	
	/**
	 * The minimum value of the tree
	 * @requires !isEmtpy()
	 * @return the minimum value
	 */
	public T min();
	
	/**
	 * The maximum value of the tree
	 * @requires !isEmtpy()
	 * @return the maximum value
	 */
	public T max();
	
	/**
	 * Verifies is an element in in the tree
	 * @param elem the element to be searched
	 * @return true iff elem belongs to tree
	 */
	public boolean contains(T elem);
	
	/**
	 * Insert element into tree keeping the invariant
	 * If an element already exists, the tree does not change
	 * @param elem the element to be inserted
	 */
	public void insert(T elem);
	
	/**
	 * Delete element from tree keeping the invariant
	 * If an element does not exist, the tree does not change
	 * @param elem the element to be deleted
	 */
	public void delete(T elem);
	
	/**
	 * Convert tree into list. The list has the elements accordingly to the tree's 
	 * prefix traversal, ie, the elements will be sequenced by increasing order
	 * @returns the list with the tree's elements
	 */	
	public List<T> toList();
	
	/**
	 * @returns an iterator traversing elements in a increasing order
	 */
	public Iterator<T> iterator();
	
}
