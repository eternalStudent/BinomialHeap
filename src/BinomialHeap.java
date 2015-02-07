/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 */

public class BinomialHeap{
	
	private HeapNode first;
	private HeapNode min;
	private int size;
	
	public BinomialHeap(){}
	
	public BinomialHeap(HeapNode node){
		first = node;
		min = node;
		size = 1;
	}

   /**
    * public boolean empty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
    public boolean empty(){
    	return first == null;
    }
		
   /**
    * public void insert(int value)
    *
    * Insert value into the heap 
    *
    */
    public void insert(int value){
    	HeapNode node = new HeapNode(value);
    	if (empty()){
    		first = node;
    		min = node;
    	}
    	else{
    		BinomialHeap heap2 = new BinomialHeap(node);
    		meld(heap2);
    	}
    }
    
   private void updateMin(){
	   if (empty()){
		   min = null;
		   return;
	   }	   
	   min = first;
	   HeapNode node = first.sibling;
	   while(node != null){
		   if (node.key<findMin())
			   min = node;
		   node = node.sibling;
	   }
   }
   
   private void reorderHeap(int size){
	   HeapNode[] arr = new HeapNode[size];
	   HeapNode node = first;
	   int min = size-1;
	   while (node != null){
		   arr[node.rank]=node;
		   if (node.rank<min){
			   min = node.rank;
			   first = node;
		   }
		   node = node.sibling;
	   }
	   node = first;
	   for (int i=min+1; i<arr.length; i++){
		   if (arr[i] != null){
			   node.sibling = arr[i];
			   node = node.sibling;
		   }
	   }
	   node.sibling = null;
   }

   /**
    * public void deleteMin()
    *
    * Delete the minimum value
    *
    */
    public void deleteMin(){
    	if (empty())
    		return;
    	if (min.rank==0){
    		first = first.sibling;
    		updateMin();
    		return;
    	}
    	
    	//create new BinomialHeap to be merged
    	BinomialHeap heap2 = new BinomialHeap();
    	heap2.first = min.child;
    	heap2.size = (int) Math.pow(2, min.child.rank+1)-1;
    	heap2.reorderHeap(min.rank);
    	heap2.updateMin();
    	
    	//disconnect min
    	if (min == first){
    		first = first.sibling;
    	}
    	else{
    		HeapNode minPrev = first;
    		while(minPrev.sibling != min)
    			minPrev = minPrev.sibling;
    		minPrev.sibling = min.sibling;
    	}	
    	updateMin();
    	size -= heap2.size+1;
    	
    	meld(heap2);
    }

   /**
    * public int findMin()
    *
    * Return the minimum value
    *
    */
    public int findMin(){
    	return min.key;
    } 
    
    private HeapNode meldTwoTreesWithSameRank(HeapNode tree1, HeapNode tree2){
    	HeapNode lesser = tree1.key<=tree2.key? tree1: tree2;
    	HeapNode greater = lesser == tree1? tree2: tree1;
    	greater.sibling = lesser.child;
    	lesser.child = greater;
    	lesser.rank++;
    	lesser.sibling = null;
    	if (greater == min) //if tree1.key == tree2.key == min.key
    		min = lesser;
    	return lesser;
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
    		if (node.rank < carry.rank){
    			prev = node;
    			node = node.sibling;
    			continue;
    		}
    		if (node.rank > carry.rank){
    			if (prev != null)
    				prev.sibling = carry;
    			else
    				first = carry;
    			carry.sibling = node;
    			return;
    		}
    		if (node.rank == carry.rank){
    			HeapNode nodeSibling = node.sibling;
    			if (prev != null)
    				prev.sibling = node.sibling;
    			else
    				first = node.sibling;
    			carry = meldTwoTreesWithSameRank(carry, node);
    			node = nodeSibling;
    			continue;
    		}
    	}
    }
    
   /**
    * public void meld (BinomialHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (BinomialHeap heap2){
    	HeapNode node1 = first; 		//will iterate over the main heap
    	HeapNode node2 = heap2.first; 	//will iterate over second heap
    	HeapNode prev = null;			//previous sibling of node1
    	
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
    	
    	while (node1 != null & node2 != null){
    		if (node1.rank < node2.rank){
    			prev = node1;
    			node1 = node1.sibling;
    			continue;
    		}
    		if (node2.rank < node1.rank){
    			HeapNode node2Sibling = node2.sibling;
    			node2.sibling = node1;
    			if (prev != null)
    				prev.sibling = node2;
    			else
    				first = node2;
    			prev = node2;
    			node2 = node2Sibling;
    			continue;
    		}
    		if (node2.rank == node1.rank){
    			//disconnect node1
    			if (prev != null)
    				prev.sibling = node1.sibling; 
    			else
    				first = node1.sibling;
    			HeapNode node2Sibling = node2.sibling;
    			HeapNode carry = meldTwoTreesWithSameRank(node1, node2);
    			addCarry(carry);
    			node1 = first;
    			prev = null;
    			node2 = node2Sibling;
    			continue;
    		}
    	}
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size(){
    	return size;
    }
    
   /**
    * public int minTreeRank()
    *
    * Return the minimum rank of a tree in the heap.
    * 
    */
    public int minTreeRank(){
        return first.rank;
    }
    
    public int maxTreeRank(){
    	if (empty())
    		return -1;
    	String bin = Integer.toString(size, 2);
    	return bin.length()-1;
    }
	
	   /**
    * public boolean[] binaryRep()
    *
    * Return an array containing the binary representation of the heap.
    * 
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
    
    public void removeAll(){
    	first = null;
    	size = 0;
    	min = null;
    }

   /**
    * public void arrayToHeap()
    *
    * Insert the array to the heap. Delete previous elements in the heap.
    * 
    */
    public void arrayToHeap(int[] array){
        removeAll();
        for (int i=0; i<array.length; i++)
        	insert(array[i]);
    }
    
    private boolean isOrderValid(HeapNode parent){
    	if (parent == null)
    		return true;
    	HeapNode node = parent.child;
    	while (node != null){
    		if (node.key < parent.key || !isOrderValid(node))
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
    * public boolean isHeap()
    *
    * Returns true if and only if the heap is valid.
    *   
    */
    public boolean isValid(){
    	return isHeapOrderValid() && isHeapRanksValid() && isEveryRankUnique();
    }
    
    public String toString(){
    	if (empty())
    		return "empty";
    	StringBuilder st = new StringBuilder();
    	HeapNode node = first;
    	while (node != null){
    		st.append("("+node.rank+")"+node+" ");
    		node = node.sibling;
    	}	
    	return ""+st;
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than BinomialHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{
    	
    	private int key, rank;
    	private HeapNode child;
    	private HeapNode sibling;
    	
    	protected HeapNode(int key){
    		this.key = key;
    	}
    	
    	public String toString(){
    		StringBuilder st = new StringBuilder(key+"[");
    		HeapNode node = child;
    		while (node != null){
    			st.append(node+" ");
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
    		System.out.println(heap.findMin());
    		heap.deleteMin();
    		//System.out.println(heap);
    	}
    }

}