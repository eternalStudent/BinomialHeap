/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 */

public class BinomialHeap{
	
	private HeapNode first;
	private HeapNode min;
	private int size;
	
	/**
	 * Default constructor, creates empty heap.
	 */
	public BinomialHeap(){}
	
	/**
	 * Creates binomial heap with a specified node.
	 * @param node  the node to be added to the heap.
	 */
	private BinomialHeap(HeapNode node){
		first = node;
		min = node;
		size = 1;
	}

   /**
    * @return true if and only if the heap is empty.
    */
    public boolean empty(){
    	return first == null;
    }
    
    /**
     * @return the number of elements in the heap
     */
     public int size(){
     	return size;
     }
     
    /**
     * @return the minimum rank of a tree in the heap, or -1 if empty.
     */
     public int minTreeRank(){
     	if (empty())
     		return -1;
     	return first.rank;
     }
          
     /**
      * @return the maximum rank of a tree in the heap, or -1 if empty.
      */
     public int maxTreeRank(){
     	if (empty())
     		return -1;
     	String binaryRepresentation = Integer.toString(size, 2);
     	return binaryRepresentation.length()-1;
     }
 	
 	/**
 	 * @return an array containing the binary representation of the heap.
     */
     public boolean[] binaryRep(){
 		boolean[] arr = new boolean[maxTreeRank()+1];
 		for (int i=0; i<arr.length; i++)
 			arr[i] = false;
 		HeapNode node = first;
 		while(node != null){
 			arr[node.rank] = true;
 			node = node.sibling;
 		}
 		return arr;
     }
     
     /**
      *Remove all the elements from the heap.
      */
     
     public void removeAll(){
     	first = null;
     	size = 0;
     	min = null;
     }

    /**
     * Inserts the array to the heap. Deletes previous elements in the heap.
     * 
     * @param array  the array of keys to be added to the heap.
     */
     public void arrayToHeap(int[] array){
         removeAll();
         for (int i=0; i<array.length; i++)
         	insert(array[i]);
     }
		
   /**
    * Inserts value into the heap.
    * 
    * @param value  the key of the element to be added to the heap.
    * @throws IllegalArgumentException if the value is negative.
    */
    public void insert(int value){
    	HeapNode node = new HeapNode(value);
    	BinomialHeap heap = new BinomialHeap(node);
    	meld(heap);
    }

    /**
     * @return the minimum value.
     * @throws NullPointerException if the heap is empty.
     */
    public int findMin(){
    	return min.value;
    } 

   /**
    * Deletes the minimum value.
    */
    public void deleteMin(){
    	if (empty())
    		return;
    	BinomialHeap heapFromMinTree = binomialHeapFromSingleTree(min);
    	disconnectTree(min);
    	meld(heapFromMinTree);
    }

    private void updateMin(){
 	   min = first;
 	   if (!empty()){
 		   HeapNode tree = first.sibling;
 		   while(tree != null){
 			   if (tree.value<findMin())
 				   min = tree;
 			   tree = tree.sibling;
 		   }
 	   }
    }
    
    private static BinomialHeap binomialHeapFromSingleTree(HeapNode tree){
	   	BinomialHeap heap = new BinomialHeap();
	   	if (tree.rank>0){
	   		heap.first = tree.child;
	   		heap.size = (int) Math.pow(2, tree.rank)-1;
	   		heap.reorderHeap();
	   		heap.updateMin();
	   	}	
   		return heap;
   }
    
   private void reorderHeap(){
	   HeapNode[] trees = toTreesArray();
	   linkTreesFromArray(trees);
   }
   
   private HeapNode[] toTreesArray(){
 	   HeapNode[] trees = new HeapNode[maxTreeRank()+1];
 	   HeapNode tree = first;
 	   while (tree != null){
 		   trees[tree.rank] = tree;
 		   tree = tree.sibling;
 	   }
 	   return trees;
   }
   
   private void linkTreesFromArray(HeapNode[] trees){
	   first = treeWithMinRankFromArray(trees);
	   HeapNode node = first;
 	   for (int i = 1; i<trees.length; i++){
 		   if (trees[i] != null){
 			   node.sibling = trees[i];
 			   node = node.sibling;
 		   }
 	   }
 	   node.sibling = null;
   }
   
   private HeapNode treeWithMinRankFromArray(HeapNode[] trees){
	   for (int rank=0; rank<trees.length; rank++)
		   if (trees[rank] != null)
			   return trees[rank];
	   return null;
   }
   
   private void disconnectTree(HeapNode tree){
	  size -= (int) Math.pow(2, tree.rank);
	  disconnect(prevOfTree(min), tree);	
	  if (tree == min)
		  updateMin();
   }
   
   private HeapNode prevOfTree(HeapNode tree){
	   if (tree == first)
		   return null;
	   HeapNode prev = first;
	   while(prev.sibling != tree)
		   prev = prev.sibling;
	   return prev;
   }
    
   /**
    * Melds the heap with heap2.
    * 
    * @param heap2  the heap to be melt with the instance
    */
    public void meld (BinomialHeap heap2){    	
    	size += heap2.size;
    	if (!heap2.empty() && (empty() || heap2.findMin()<findMin()))
    		min = heap2.min;
    	
    	HeapNode tree1 = first; 			//will iterate over the main heap
    	HeapNode tree2 = heap2.first; 		//will iterate over the second heap
    	HeapNode prev = null;				//previous sibling of tree1
    	
    	while (tree2 != null){
    		if (tree1 == null){
    			connect(prev, tree2, tree2.sibling);
    			break;
    		}
    		if (tree1.rank < tree2.rank){
    			prev = tree1;
    			tree1 = tree1.sibling;
    			continue;
    		}
    		if (tree2.rank < tree1.rank){
    			HeapNode node2Sibling = tree2.sibling;
    			connect(prev, tree2, tree1);
    			prev = tree2;
    			tree2 = node2Sibling;
    			continue;
    		}
    		if (tree2.rank == tree1.rank){
    			disconnect(prev, tree1);
    			HeapNode node2Sibling = tree2.sibling;
    			addCarry(meldTwoTreesWithSameRank(tree1, tree2));
    			tree1 = first;
    			prev = null;
    			tree2 = node2Sibling;
    			continue;
    		}
    	}
    }
    
    private void addCarry(HeapNode carry){
    	HeapNode tree = first; 	//will iterate over the heap
    	HeapNode prev = null; 	//previous sibling of tree
    	
    	while (true){
    		if (tree == null || tree.rank > carry.rank){
    			connect(prev, carry, tree);
    			return;
    		}
    		if (tree.rank < carry.rank){
    			prev = tree;
    			tree = tree.sibling;
    			continue;
    		}
    		if (tree.rank == carry.rank){
    			HeapNode nodeSibling = tree.sibling;
    			disconnect(prev, tree);
    			carry = meldTwoTreesWithSameRank(carry, tree);
    			tree = nodeSibling;
    			continue;
    		}
    	}
    }
    
    private HeapNode meldTwoTreesWithSameRank(HeapNode tree1, HeapNode tree2){
    	HeapNode parent = tree1.value<=tree2.value? tree1: tree2;
    	HeapNode child = parent == tree1? tree2: tree1;
    	child.sibling = parent.child;
    	parent.child = child;
    	parent.rank++;
    	parent.sibling = null;
    	if (child == min) //if tree1.key == tree2.key == min.key
    		min = parent;
    	return parent;
    }
    
    private void disconnect(HeapNode prev, HeapNode node){
    	if (prev != null)
			prev.sibling = node.sibling;
		else
			first = node.sibling;
    }
    
    private void connect(HeapNode prev, HeapNode node, HeapNode next){
    	if (prev != null)
			prev.sibling = node;
		else
			first = node;
    	node.sibling = next;
    }
    
   /**
    * @return true if and only if the heap is valid.
    */
    public boolean isValid(){
    	return isHeapOrderValid() && isHeapBinomial() && isEveryRankUnique();
    }
    
    private boolean isHeapOrderValid(){
    	HeapNode tree = first;
    	while(tree != null){
    		if (!isOrderValid(tree))
    			return false;
    		tree = tree.sibling;
    	}	
    	return true;
    }
    
    private boolean isOrderValid(HeapNode parent){
    	if (parent == null)
    		return true;
    	HeapNode child = parent.child;
    	while (child != null){
    		if (child.value < parent.value || !isOrderValid(child))
    			return false;
    		child = child.sibling;
    	}
    	return true;
    }
    
    private boolean isHeapBinomial(){
    	HeapNode tree = first;
    	while(tree != null){
    		if (!isBinomialSubtree(tree))
    			return false;
    		tree = tree.sibling;
    	}	
    	return true;
    }
    
    private boolean isBinomialSubtree(HeapNode parent){
    	if (parent == null)
    		return true;
    	int size=0;
    	HeapNode child = parent.child;
    	while(child != null){
    		size++;
    		if (!(parent.rank-size == child.rank) || !isBinomialSubtree(child))
    			return false;
    		child = child.sibling;
    	}
    	return (size == parent.rank);
    }
    
    private boolean isEveryRankUnique(){
    	boolean[] arr = new boolean[maxTreeRank()+1];
		for (int i=0; i<arr.length; i++)
			arr[i] = false;
		HeapNode tree = first;
		while(tree != null){
			if (arr[tree.rank])
				return false;
			arr[tree.rank] = true;
			tree = tree.sibling;
		}
    	return true;
    }
    
    /**
     * @return a string representation of the heap.
     */
    public String toString(){
    	if (empty())
    		return "empty";
    	StringBuilder st = new StringBuilder("{");
    	HeapNode tree = first;
    	while (tree != null){
    		st.append(tree.rank+". "+tree);
    		if (tree.sibling != null)
    			st.append(", ");
    		tree = tree.sibling;
    	}	
    	return st+"}";
    }
    
    /**
     * Prints the string representation of the heap to the console. 
     */
    public void print(){
    	System.out.println(this);
    }
    
   /**
    * An implementation of node in Binomial Heap 
    */
    public class HeapNode{
    	
    	private final int value;
    	private int rank;
    	private HeapNode child;
    	private HeapNode sibling;
    	
    	protected HeapNode(int key){
    		if (key < 0)
    			throw new IllegalArgumentException("values must be non-negative");
    		this.value = key;
    	}
    	
    	public String toString(){
    		StringBuilder st = new StringBuilder(value+"[");
    		HeapNode node = child;
    		while (node != null){
    			st.append(node);
    			if (node.sibling != null)
    				st.append(" ");
    			node = node.sibling;
    		}	
    		return st+"]";
    	}
  	
    }
    
    public static void main(String[] args){
    	BinomialHeap heap = new BinomialHeap();
    	java.util.Random random = new java.util.Random();
    	for (int i=0; i<31; i++){
    		heap.insert(random.nextInt(120));
    	}
    	System.out.println(heap.isEveryRankUnique());
    	System.out.println(heap.isHeapOrderValid());
    	System.out.println(heap.isHeapBinomial());
    	for (int i=0; i<31; i++){
    		System.out.println((i+1)+". "+heap.findMin());
    		heap.deleteMin();
    		//heap.print();
    	}
    	int[] arr = {1,2,3,4,5,6};
    	heap.arrayToHeap(arr);
    	heap.print();
    	int[] arr2 = {7,8,9,10,11,12,13,14,15};
    	BinomialHeap heap2 = new BinomialHeap();
    	heap2.arrayToHeap(arr2);
    	heap.meld(heap2);
    	heap.print();
    }

}