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
	 * Default constructor, crate empty heap.
	 */
	public BinomialHeap(){}
	
	/**
	 * Create binomial heap with a specified node.
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
      *remove all the elements from the heap.
      */
     
     public void removeAll(){
     	first = null;
     	size = 0;
     	min = null;
     }

    /**
     * Insert the array to the heap. Delete previous elements in the heap.
     * 
     * @param array  the array of keys to be added to the heap.
     */
     public void arrayToHeap(int[] array){
         removeAll();
         for (int i=0; i<array.length; i++)
         	insert(array[i]);
     }
		
   /**
    * Insert value into the heap.
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
    * Delete the minimum value.
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
 		   HeapNode node = first.sibling;
 		   while(node != null){
 			   if (node.value<findMin())
 				   min = node;
 			   node = node.sibling;
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
 	   HeapNode node = first;
 	   while (node != null){
 		   trees[node.rank] = node;
 		   node = node.sibling;
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
    * Meld the heap with heap2.
    * 
    * @param heap2  the heap to be melt with the instance
    */
    public void meld (BinomialHeap heap2){    	
    	if (heap2.empty())
    		return;
    	if (empty()){
    		first = heap2.first;
    		min = heap2.min;
    		size = heap2.size;
    		return;
    	}

    	size += heap2.size;
    	if (heap2.findMin()<findMin())
    		min = heap2.min;
    	
    	HeapNode node1 = first; 			//will iterate over the main heap
    	HeapNode node2 = heap2.first; 		//will iterate over the second heap
    	HeapNode prev = null;				//previous sibling of node1
    	
    	while (node1 != null & node2 != null){
    		if (node1.rank < node2.rank){
    			prev = node1;
    			node1 = node1.sibling;
    			continue;
    		}
    		if (node2.rank < node1.rank){
    			HeapNode node2Sibling = node2.sibling;
    			connect(prev, node2, node1);
    			prev = node2;
    			node2 = node2Sibling;
    			continue;
    		}
    		if (node2.rank == node1.rank){
    			disconnect(prev, node1);
    			HeapNode node2Sibling = node2.sibling;
    			addCarry(meldTwoTreesWithSameRank(node1, node2));
    			node1 = first;
    			prev = null;
    			node2 = node2Sibling;
    			continue;
    		}
    	}
    }
    
    private void addCarry(HeapNode carry){
    	HeapNode node = first; 	//will iterate over the heap
    	HeapNode prev = null; 	//previous sibling of node
    	
    	while (true){
    		if (empty()){
        		first = carry;
        		return;
        	}
    		if (node == null){
    			prev.sibling = carry;
    			return;
    		}
    		if (node.rank > carry.rank){
    			connect(prev, carry, node);
    			return;
    		}
    		if (node.rank < carry.rank){
    			prev = node;
    			node = node.sibling;
    			continue;
    		}
    		if (node.rank == carry.rank){
    			HeapNode nodeSibling = node.sibling;
    			disconnect(prev, node);
    			carry = meldTwoTreesWithSameRank(carry, node);
    			node = nodeSibling;
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
    	return isHeapOrderValid() && isHeapRanksValid() && isEveryRankUnique();
    }
    
    private boolean isOrderValid(HeapNode parent){
    	if (parent == null)
    		return true;
    	HeapNode node = parent.child;
    	while (node != null){
    		if (node.value < parent.value || !isOrderValid(node))
    			return false;
    		node = node.sibling;
    	}
    	return true;
    }
    
    private boolean isHeapOrderValid(){
    	HeapNode node = first;
    	while(node != null){
    		if (!isOrderValid(node))
    			return false;
    		node = node.sibling;
    	}	
    	return true;
    }
    
    private boolean isRankValid(HeapNode parent){
    	if (parent == null)
    		return true;
    	int size=0;
    	HeapNode node = parent.child;
    	while(node != null){
    		if (!isRankValid(node))
    			return false;
    		size++;
    		node = node.sibling;
    	}
    	return (size == parent.rank);
    }
    
    private boolean isHeapRanksValid(){
    	HeapNode node = first;
    	while(node != null){
    		if (!isRankValid(node))
    			return false;
    		node = node.sibling;
    	}	
    	return true;
    }
    
    private boolean isEveryRankUnique(){
    	boolean[] arr = new boolean[maxTreeRank()+1];
		for (int i=0; i<arr.length; i++)
			arr[i] = false;
		HeapNode node = first;
		while(node != null){
			if (arr[node.rank])
				return false;
			arr[node.rank] = true;
			node = node.sibling;
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
    	HeapNode node = first;
    	while (node != null){
    		st.append(node.rank+". "+node);
    		if (node.sibling != null)
    			st.append(", ");
    		node = node.sibling;
    	}	
    	return st+"}";
    }
    
    /**
     * Print the string representation of the heap to the console. 
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
    	System.out.println(heap.isHeapRanksValid());
    	for (int i=0; i<31; i++){
    		System.out.println((i+1)+". "+heap.findMin());
    		heap.deleteMin();
    		//heap.print();
    	}
    }

}