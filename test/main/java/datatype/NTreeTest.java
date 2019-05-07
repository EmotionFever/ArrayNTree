package main.java.datatype;

import static org.junit.Assert.*;

import org.junit.*;

import main.java.datatype.*;

import java.util.*;

public class NTreeTest {
	
	List<Integer> list1, list2;
	ArrayNTree<Integer> tree1, tree2;
	
	private int defaultCapacity = 5;
	
	@Before
	public void setup() {
		
		list1 = new ArrayList<>();
		for (int i = 1; i < 100; i++)  
			list1.add(i*10);  // only use positives
		
		// list2 has the same elements of list1, but shuffled
		list2 = new LinkedList<>(list1); 
		Collections.shuffle(list2);
		
		tree1 = new ArrayNTree<>(list1, defaultCapacity);
		tree2 = new ArrayNTree<>(list2, defaultCapacity);
	}

	@Test
	public void testEmpty() {
		ArrayNTree<Integer> emptyTree = new ArrayNTree<>(defaultCapacity);
		assertTrue(emptyTree.isEmpty());
	}
	
	@Test
	public void testNotEmpty() {
		ArrayNTree<Integer> emptyTree = new ArrayNTree<>(1, defaultCapacity);
		assertFalse(emptyTree.isEmpty());
	}

	@Test
	public void testLeaf() {
		ArrayNTree<Integer> leaf = new ArrayNTree<>(1, defaultCapacity);
		assertTrue(leaf.isLeaf());
	}
	
	@Test
	public void testNotLeaf() {
		assertFalse(tree1.isLeaf());

		ArrayNTree<Integer> leaf = new ArrayNTree<>(defaultCapacity);
		assertFalse(leaf.isLeaf());
	}
	
	@Test
	public void testSize() {
		assertTrue("size list == size tree",    tree1.size() == list1.size());
		assertTrue("both trees have same size", tree1.size() == tree2.size());
	}
	
	@Test
	public void testEmptyNumberLeaves() {
		ArrayNTree<Integer> empty = new ArrayNTree<>(defaultCapacity);
		
		assertTrue("empty tree has zero leaves", empty.countLeaves() == 0);
	}

	@Test
	public void testThreeNumberLeaves() {
		ArrayNTree<Integer> threeElems = new ArrayNTree<>(3);
		threeElems.insert(1);
		threeElems.insert(2);
		threeElems.insert(3);
		
		assertTrue("tree has two leaves", threeElems.countLeaves() == 2);
	}

	@Test
	public void testThreeNumberLeavesCapacityOne() {
		ArrayNTree<Integer> threeElems = new ArrayNTree<>(1);
		threeElems.insert(1);
		threeElems.insert(2);
		threeElems.insert(3);
		
		assertTrue("tree has one leaf", threeElems.countLeaves() == 1);
	}

	@Test
	public void testNumberLeavesCapacity100() {
		ArrayNTree<Integer> bigTree = new ArrayNTree<>(100);
		for(int i=1; i<=1000; i++)
			bigTree.insert(i);
		
		assertTrue("tree has 990 leaves", bigTree.countLeaves() == 990);
	}

	@Test
	public void testEmptyHeight() {
		ArrayNTree<Integer> empty = new ArrayNTree<>(defaultCapacity);
		
		assertTrue("empty tree has height 0", empty.height() == 0);
	}

	@Test
	public void testThreeHeight() {
		ArrayNTree<Integer> threeElems = new ArrayNTree<>(3);
		threeElems.insert(1);
		threeElems.insert(2);
		threeElems.insert(3);
		
		assertTrue("tree has height 2", threeElems.height() == 2);
	}

	@Test
	public void testThreeHeightCapacityOne() {
		ArrayNTree<Integer> threeElems = new ArrayNTree<>(1);
		threeElems.insert(1);
		threeElems.insert(2);
		threeElems.insert(3);
		
		assertTrue("tree has height 3", threeElems.height() == 3);
	}

	@Test
	public void testHeightCapacity100() {
		ArrayNTree<Integer> bigTree = new ArrayNTree<>(100);
		for(int i=1; i<=1000; i++)
			bigTree.insert(i);
		
		assertTrue("tree has height 11", bigTree.height() == 11);
	}

	@Test
	public void testMinLeaf() {
		int value = 0;
		ArrayNTree<Integer> tree = new ArrayNTree<>(value, 100);
	
		assertTrue(tree.min() == value);
	}

	@Test
	public void testMin() {
		Collections.sort(list1);
		assertTrue(tree1.min() == list1.get(0));

		Collections.sort(list2);
		assertTrue(tree1.min() == list2.get(0));
	}
	
	@Test
	public void testMaxLeaf() {
		int value = 0;
		ArrayNTree<Integer> tree = new ArrayNTree<>(value, 100);
	
		assertTrue(tree.max() == value);
	}

	@Test
	public void testMax() {
		Collections.sort(list1);
		Collections.reverse(list1);
		assertTrue(tree1.max() == list1.get(0));

		Collections.sort(list2);
		Collections.reverse(list2);
		assertTrue(tree1.max() == list2.get(0));
	}
	
	@Test
	public void testContains() {
		for(int elem : list1)
			assertTrue(tree1.contains(elem));
		
		for(int elem : list1)
			assertFalse(tree1.contains(-elem));
	}
	
	@Test
	public void testInsertShuffle() {
		for(int elem : tree1)
			assertTrue(tree2.contains(elem));
		for(int elem : tree2)
			assertTrue(tree1.contains(elem));
	}
	
	@Test
	public void testInsertElement() {
		int prevSize = tree1.size();
		tree1.insert(-1);
		
		assertTrue("size adds 1", tree1.size() == prevSize+1);
	}

	@Test
	public void testInsertSameElement() {
		int prevSize = tree1.size();
		tree1.insert(-1);
		tree1.insert(-1);
		
		assertTrue("size just adds 1", tree1.size() == prevSize+1);
	}

	@Test
	public void testInsertSameElements() {
		int prevSize = tree2.size();
		for(int i : tree2) 
			tree2.insert(i);
		
		assertTrue("size stays the same", tree2.size() == prevSize);
	}
	
	// if the tree is well built, the iterator should return  
	// an increasing sequence of elements
	private boolean assertInvariant(ArrayNTree<Integer> tree) {
		boolean first = true;
		int prev = 0;
		boolean invariant = true;
		
		for (int elem : tree)
			if (first) {
				prev = elem;
				first = false;				
			} else {
				invariant = invariant && prev < elem;
				prev = elem;
			}
		return invariant;
	}
	
    private Integer[] makeRandomVector(int size) {
    	Integer[] result = new Integer[size];
    	Random r = new Random();
    	
    	for(int i=0; i<size; i++)
    		result[i] = 1+ r.nextInt(100);
    	
    	return result;
    }
	
	@Test
	public void testInvariantInRandomTrees() {
		int repeat = 1000;
		
		while(repeat-- > 0) {
			int randomCapacity = 2 + new Random().nextInt(100); 
			List<Integer> randomList = Arrays.asList(makeRandomVector(1000));
			
			ArrayNTree<Integer> randomTree = new ArrayNTree<Integer>(randomList, randomCapacity);
			
			assertTrue(assertInvariant(randomTree));
		}
	}

	@Test
	public void testDelete() {
		int prevSize = tree1.size();
		int element = list1.get(0);
		tree1.delete(element);
		
		assertTrue("size decreases", tree1.size() == prevSize-1);
		assertFalse("element is removed", tree1.contains(element));
	}
	
	@Test
	public void testDeleteNonExistingValues() {
		int prevSize = tree1.size();
		// try to delete a smaller value than those there
		tree1.delete(-1);
		// try to delete a larger value than those there
		Collections.sort(list1);
		tree1.delete(list1.get(list1.size()-1)+1); 

		assertTrue("size stays the same", tree1.size() == prevSize);
	}
	
	@Test
	public void testDeleteAll() {
		for(int elem : list2)
			tree1.delete(elem);
		
		assertTrue("must be empty", tree1.isEmpty());
	}
	
	@Test
	public void testDeleteAllRandom() {
		int repeat = 1000;
		boolean verbose = false; // warning: lots of output, better to reduce 'repeat' and list size
		
		while(repeat-- > 0) {
			int randomCapacity = 2 + new Random().nextInt(1000); 
			List<Integer> randomList = Arrays.asList(makeRandomVector(100));
			
			ArrayNTree<Integer> randomTree = new ArrayNTree<Integer>(randomList, randomCapacity);
			
			if (verbose) System.out.println(randomList + " capacity=" + randomCapacity);
			if (verbose) System.out.println(randomTree);
			Collections.shuffle(randomList);
			for(int elem : randomList) {
				if (verbose) System.out.print("after " + elem + ": ");
				randomTree.delete(elem);
				if (verbose) System.out.println(randomTree);
			}
			
			assertTrue("must be empty", randomTree.isEmpty());
		}
	}
	
	@Test
	public void testEquals() {
		assertEquals(tree1, tree1);
		assertEquals(tree1, tree2);
		assertEquals(tree2, tree1);
		assertEquals(tree2, tree2);
	}

	@Test
	public void testToList() {
		List<Integer> treeList1 = tree1.toList();
		List<Integer> treeList2 = tree2.toList();
		
		Collections.sort(list1);
		Collections.sort(list2);
		
		assertEquals(treeList1, list1);
		assertEquals(treeList1, list2);
		assertEquals(treeList2, list1);
		assertEquals(treeList2, list2);
		assertEquals(treeList1, treeList2);
	}

	@Test
	public void testClone() {
		ArrayNTree<Integer> clone1 = tree1.clone();
		ArrayNTree<Integer> clone2 = tree2.clone();
		
		assertEquals(tree1,  clone1);
		assertEquals(tree1,  clone2);
		assertEquals(tree2,  clone1);
		assertEquals(tree2,  clone2);
		assertEquals(clone1, clone2);
	}

	@Test
	public void testIerators() {
		Iterator<Integer> it1 = tree1.iterator();
		Iterator<Integer> it2 = tree2.iterator();
		
		while(it1.hasNext() && it2.hasNext())
			assertEquals(it1.next(), it2.next());
		
		assertTrue(!it1.hasNext() && !it2.hasNext());
	}
	
	@Test
	public void testContainsEmpty() {
		ArrayNTree<Integer> emptyTree = new ArrayNTree<>(defaultCapacity);
		
		for(int elem : list1)
			assertFalse(emptyTree.contains(elem));
	}
	
	@Test
	public void testEqualsTrivialCases() {
		assertTrue(tree1.equals(tree1));

		assertFalse(tree1.equals(123));
		assertFalse(tree1.equals(null));
		
		tree2.insert(-1);
		assertFalse(tree1.equals(tree2));
	}
	
	@After
	public void teardown() {
		// not very orthodox use of tear down, but...
		// checks if all trees, after every operation, still validate the invariant
		assertTrue(assertInvariant(tree1));
		assertTrue(assertInvariant(tree2));
	}
	
}
