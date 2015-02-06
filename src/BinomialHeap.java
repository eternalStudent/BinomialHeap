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

   /**
    * public void deleteMin()
    *
    * Delete the minimum value
    *
    */
    public void deleteMin(){
    	if (empty())
    		return;
    	
    	//create new BinomialHeap to be merged
    	BinomialHeap heap2 = new BinomialHeap();
    	heap2.first = min.child;
    	heap2.updateMin();
    	heap2.size = (int) Math.pow(2, heap2.first.rank+1)-1;
    	
    	//disconnect min
    	HeapNode minPrev = first;
    	while(minPrev.sibling != min)
    		minPrev = minPrev.sibling;
    	minPrev.sibling = min.sibling;
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
    
    private static HeapNode meldTwoTreesWithSameRank(HeapNode tree1, HeapNode tree2){
    	HeapNode greater = tree1.rank>tree2.rank? tree1: tree2;
    	HeapNode lesser = greater == tree1? tree2: tree1;
    	lesser.sibling = greater.child;
    	greater.child = lesser;
    	greater.rank++;
    	return greater;
    }
    
    private void addCarry(HeapNode carry){
    	HeapNode node = first; 	//will iterate over the heap
    	HeapNode prev = null; 	//previous sibling of node
    	
    	while (node != null){
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
    			if (prev != null)
    				prev.sibling = node.sibling;
    			else
    				first = node.sibling;
    			carry = meldTwoTreesWithSameRank(carry, node);
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
    			node2 = node2Sibling;
    			continue;
    		}
    		if (node2.rank == node1.rank){
    			//disconnect node1
    			if (prev != null)
    				prev.sibling = node1.sibling; 
    			else
    				first = node1.sibling;
    			addCarry(meldTwoTreesWithSameRank(node1, node2));
    			node1 = node1.sibling;
    			node2 = node2.sibling;
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
	
	   /**
    * public boolean[] binaryRep()
    *
    * Return an array containing the binary representation of the heap.
    * 
    */
    public boolean[] binaryRep(){
		boolean[] arr = new boolean[first.rank];
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
	
   /**
    * public boolean isHeap()
    *
    * Returns true if and only if the heap is valid.
    *   
    */
    public boolean isValid(){
    	return false; // should be replaced by student code
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
  	
    }

}