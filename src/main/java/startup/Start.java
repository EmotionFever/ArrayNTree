package main.java.startup;

import java.util.*;

import main.java.datatype.ArrayNTree;

public class Start {

	public static void main(String[] args) {
		
		List<Integer> list = Arrays.asList(39, 59, 17, 85, 41, 45);  

		ArrayNTree<Integer> tree = new ArrayNTree<>(list, 4);
		System.out.println(tree.info());  
		// output eg: [17:[39][41:[45]][59][85]], size: 6, height: 3, nLeaves: 4
		
		tree.delete(45);
		System.out.println(tree.info());
		// output eg: [17:[39][41][59][85]], size: 5, height: 2, nLeaves: 4
		
		tree.delete(17);
		System.out.println(tree.info());
		// output eg: [39:[41][59][85]], size: 4, height: 2, nLeaves: 3
		
		tree.insert(1);
		System.out.println(tree.info());
		// output eg: [1:[39][41][59][85]], size: 5, height: 2, nLeaves: 4
	}
}
