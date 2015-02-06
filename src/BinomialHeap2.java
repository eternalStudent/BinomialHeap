
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;


public class BinomialHeap2 {
	HeapNode first;
	HeapNode min;
	int numberOfElements;
	
	public BinomialHeap2(HeapNode first, HeapNode min, int numberOfElements ){
		this.first = first;
		this.min = min;
		this.numberOfElements = numberOfElements;
	}
	
	public BinomialHeap2(){
		this(null, null, 0);
	}

	
	public HeapNode getFirst(){
		return first;
	}
	
	
	 public boolean empty(){
	    if (first == null){
	    	return true;
	    }
	    return false;
	 }
	 
	 
	 //if they are equals return one of them.
	 public static HeapNode maxKey(HeapNode x, HeapNode y){
		 if (x.key > y.key){
			 return x;
		 }
		 else return y;
	 }
	 
	 public static HeapNode minKey(HeapNode x, HeapNode y){
		 if (maxKey(x,y) == x){
			 return y;
		 }
		 return x;
	 }
	 
	 
	 public static HeapNode link(HeapNode max, HeapNode min){		 
		 max.next = min.child;
		 min.child = max;
		 min.rank ++;
		 return min;	 
	 }
	 
	
	 public HeapNode concat(BinomialHeap2 other){
		 HeapNode main = this.first;
		 HeapNode y = other.first;
		 HeapNode x;
		 HeapNode prev=null;
		 HeapNode pointer = null;
		 
		 while (y != null){
			 if (y.rank <= main.rank) {
				x = y.next;
				y.next = main;
				if (prev == null){
					this.first = y;
				}
				else {
					prev.next = y;
				}
				if (x == null){
					pointer = y;
				}
				prev = y;
				y =x;
			}
			 
			else if (main.next == null){ 
				main.next =y;
				pointer = main;
				break;
			}
			else if (y.rank > main.rank && y.rank <= main.next.rank){
				x = y.next;
				y.next = main.next;
				main.next = y;
				prev= main;
				main = main.next;
				if (x == null){
					pointer = y;
				}
				y=x;
			 }
			 else { //if (y.rank > main.rank && y.rank > main.next.rank)
				 prev = main;
				 main = main.next;
			 }
		}
		return pointer;
	 }
		 
	 
	 	//len 3
	 	public boolean containsPointerInFirstOrSecend(HeapNode[] array, HeapNode pointer){
	 		return (array[0] == pointer || array[1] == pointer );
	 		
	 		
	 	}
	 	
	 	
	
	 	public int linkByCase(HeapNode[] array, HeapNode pointer){
	 		
	 		if (!containsPointerInFirstOrSecend(array, pointer)){
	 			if (array[0].rank == array[1].rank){
	 				if(array[2] == null){
	 					return 1; // '[ = , = , null]'
	 					}
	 				else {
	 					if(array[0].rank == array[2].rank){
	 						return 2; // '[= , = , =]'
	 						}
	 					else {
	 						return 3; // '[= , = , !=]'
	 						}
	 					}
	 				}
	 			else {
	 				return 4; // '[= ,!=, ?]'
	 			}
	 			
	 		}
	 		else{
	 			return 5;
	 		}
	 	}
	 		
	 		

		 public void toBinomialHeap(HeapNode pointer){
			 HeapNode min;
			 HeapNode max;
			 HeapNode Next;
			 HeapNode prev = null;
			 boolean prevIsNull = true;
			 HeapNode x = this.first;
			 HeapNode[] HeapNodesArray = new HeapNode[3];
			 
			 
			 
			 if (empty() || x.next == null){
				 return;
			 }
			 
			 
			 while (x.next != null){
				 HeapNodesArray[0] = x;
				 HeapNodesArray[1] = x.next;
				 HeapNodesArray[2] = x.next.next;
				  
				 switch (linkByCase(HeapNodesArray, pointer)){
				 
				 case 1:   
						Next= x.next.next;
				 		max = maxKey(x, x.next);
						min = minKey(x, x.next);
						if (this.min == max){
							x = link(min, max);
							}
						else{
							x = link(max, min);
						 	}
						x.next = Next;
						 if (prevIsNull){
							 this.first = x;
							 }
							 else { 
								 prev.next = x; 
								 }
					 break;
				 
				 case 2:  
					 // '[= , = , =]'
					 prev =x;
					 x = x.next;
					 prevIsNull = false;
					 break;
				
				 case 3:  
					 // '[= , = , !=]'
					 Next= x.next.next;
				 		max = maxKey(x, x.next);
						min = minKey(x, x.next);
						if (this.min == max){
							x = link(min, max);
							}
						else{
							x = link(max, min);
						 	}
						x.next = Next;
						 if (prevIsNull){
							 this.first = x;
							 }
							 else { 
								 prev.next = x; 
								 }
					 break;
					 
				 case 4: 
					 // '[= ,!=, ?]'
					 prev =x;
					 x = x.next;
					 prevIsNull = false;
					 break;
					 
				 case 5:
						while (x.next.next != null && 
								!(x.rank < x.next.rank && x.next.rank < x.next.next.rank)){
							if(x.rank == x.next.rank && x.rank == x.next.next.rank){
								prev =x;
								x = x.next;
								prevIsNull = false;
								 }
							else if (x.rank == x.next.rank){
								Next= x.next.next;
						 		max = maxKey(x, x.next);
								min = minKey(x, x.next);
								if (this.min == max){
									x = link(min, max);
									}
								else{
									x = link(max, min);
								 	}
								x.next = Next;
								 if (prevIsNull){
									 this.first = x;
									 }
									 else { 
										 prev.next = x; 
										 }
								 }
							else{
								prev =x;
								x = x.next;
								prevIsNull = false;
								 }
							}
						
						if(x.next.next == null){
							if (x.rank == x.next.rank){
								Next= x.next.next;
						 		max = maxKey(x, x.next);
								min = minKey(x, x.next);
								if (this.min == max){
									x = link(min, max);
									}
								else{
									x = link(max, min);
								 	}
								x.next = Next;
								 if (prevIsNull){
									 this.first = x;
									 }
									 else { 
										 prev.next = x; 
										 }
								 return;
								 }
							else{
								return;
								}
							}
						else{
							return;
						}
				 }
			 }
							
		}
								
			
					 
						 
	
	   public void meld (BinomialHeap2 other){
		   this.numberOfElements += other.numberOfElements; 
		   if (empty()){
			   first = other.first;
			   min = other.min;
			   numberOfElements = other.numberOfElements;
			   return;
		   }
		   else if (other.empty()){
			   return;
		   }
		   else{
			   HeapNode new_min = minKey(this.min, other.min);
			   min = new_min;
		   }
		   HeapNode pointer = concat(other);
		   toBinomialHeap(pointer);
		}
	   
	   
	    public void insert(int value) {
	    	HeapNode node = new HeapNode(value);
	    	BinomialHeap2 heap = new BinomialHeap2(node, node, 1);
	    	this.meld(heap);
	    	
	    }

	    
	    
	    public int findMin(){
	    	return min.key;
	    } 
	    
	    
	    public int size(){
	    	return numberOfElements; 
	    }
	    
	   
	     public int minTreeRank(){
	         return first.rank;
	     }
	     
	     
	     public boolean[] binaryRep(){
	  		boolean[] array = new boolean[max_rank()+1];
	  		HeapNode x = this.first;
	  		while (x!= null){
	  			array[x.rank] = true;
	  			x = x.next;
	  		}
	        return array;
	      }

	     
	      public String heapToBinary(boolean[] array){
	    	  String str = "";
	    	  for (int i = array.length -1 ; i >= 0  ; i--){
	    		  if(array[i] == true){
	    			  str += "1";
	    			  }
	    		  else {
	    			  str += "0";
	    			 }
	    		  }
	    	  return str;
	    	  }
	      
	      
	      
	      public int max_rank(){
		    	int n = numberOfElements;
		    	String bin = Integer.toString(n, 2);
		    	int len = bin.length();
		    	return len-1;
		    	}
	      
	
	      
	      public boolean isBinomialTreeStructure(HeapNode node){
	    	  HeapNode x = node.child;
	    	  HeapNode y = x;
	    	  int countChildren = 0;
	    	  if (x == null){
	    		  if (node.rank != 0){
	    			  return false;
	    			  }
	    		  return true;
	    		  }
	    	  
	    	  while (x != null){
	    		  if (x.next == null){
	    			  if (x.rank != 0){
	    				  return false;
	    				  }
	    			  if (countChildren + 1 != node.rank){
	    				  return false;
	    				  }
	    			  }
	    		  else {
	    			  if (x.rank != x.next.rank + 1){
	    				  return false;
	    				  }
	    			  }
	    		  x = x.next;
	    		  countChildren++;
	    		  }
	    	  while (y != null){
	    		  if (isBinomialTreeStructure(y) == false){
	    			  return false;
	    		  	}
	    		  y = y.next;
	    		  }
	    	  return true;
	    	  }
	      
	      
	      
	      public boolean isBinomialTreeValues(HeapNode node){
	    	  HeapNode x = node.child;
	    	  HeapNode y = x;
	    	  while (x != null){
	    		  if (x.key < node.key){
	    			  return false;
	    			  }
	    		  x = x.next;
	    		  }
	    	  while (y != null){
	    		  if (isBinomialTreeValues(y) == false){
	    			  return false;
	    		  	}
	    		  y = y.next;
	    		  }
	    	  return true;
	    	  }
	      
	      
	     public boolean checkRanks(HeapNode node){
	    	 HeapNode x = node;
	    	 while (x != null){
	    		 if (x.next != null){
	    			 if (!(x.rank < x.next.rank)){
	    				 return false;
	    				 }
	    			 }
	    		 x = x.next;
	    		 }
	    	 return true;
	     }

	    
	     
	      public boolean isValid(){
	    	  HeapNode x = first;
	    	  while (x != null){
	    		  if (!isBinomialTreeStructure(x) || !isBinomialTreeValues(x)){
	    			  return false;
	    			  }
	    		  x = x.next;
	    		  }
	    	  return ((checkRanks(first))
	    			  && (heapToBinary(binaryRep()).equals(Integer.toString(numberOfElements, 2))));
	    	  }
	    
	      
	      
	     // the min is not the first node.
	      public HeapNode findMinPrev(){
	    	  HeapNode x = first;
	    	  while ( x!= null){
	    		  if (x.next == min){
	    			  return x;
	    			  }
	    		  x = x.next;
	    		  }
	    	  return null;
	    	  }
	    		  
	    
	      public BinomialHeap2 reverseHeap(BinomialHeap2 heap){
	    	  HeapNode y;
	    	  HeapNode x = heap.first;
	    	  int counter = 0;
	    	  while ( x != null){
	    		  counter++;
	    		  x = x.next;
	    	  }//counting the list
	    	  
	    	  HeapNode[] array = new HeapNode[counter];
	    	  
	    	  x = heap.first;
	    	  int index = 0;
	    	  while ( x != null){
	    		  array[index] = x;
	    		  y =x;
	    		  x = x.next;
	    		  y.next = null;
	    		  index++;
	    		  }
	    	  for (int i = counter - 1; i > 0 ; i--){
	    		  array[i].next = array[i-1];
	    	  }
	    	  
	    	  heap.first = array[counter - 1];
	    	  return heap;
	    	  }
	      
	      
	      
	    
	       public void deleteMin(){
	    	   if (empty()) {
	    		   return;
	    	   }
	    	   
	    	   HeapNode minPrev ;
	    	   int rank = min.rank;
	    	  
	    	   if (rank == 0){
	    		   first = min.next;
	    		   numberOfElements--;
	    		   min = findMinOfLinkedList(first);
	    		   return;
	    	   }
	    	   
	    	   HeapNode otherMin = findMinOfLinkedList(min.child);
	    	   BinomialHeap2 heap = new BinomialHeap2(min.child, otherMin, ((int)Math.pow(2, rank))-1);
	    	  
	    	   
	    	   if (first == min){
	    		   this.first = this.first.next;
	    	   }
	    	   else {
	    		   minPrev = findMinPrev();
	    		   minPrev.next = min.next;
	    	   }
	    	   
	    	   min = findMinOfLinkedList(this.first);
	    	   numberOfElements -= (int)Math.pow(2, rank);
	    	   meld(reverseHeap(heap));
	       }
	       
	       
	      
	       public HeapNode findMinOfLinkedList(HeapNode first){
	    	   HeapNode min = first;
	    	   HeapNode x = first;
	    	   while (x != null){
	    		   if (x.key < min.key){
	    			   min = x;
					 	}
	    		   x = x.next;
				 	}
	    	   return min;
	       }
		 
	       
	       
	   // if array.empty() == true. doean't do anything
	       public void arrayToHeap(int[] array){
	    	   if (array.length ==0){
	    		   return;
	    	   }
	           first = null;
	           min = null;
	           numberOfElements = 0;
	           for (int i : array){
	        	   insert(i);
	           } 
	        } 
		 
		
		 

	    
	   	  
	      
	      
	      
	      
	      
	      
	      
	    //*************************************************************************************************************//     
	    
	     public static Queue<HeapNode> putYourChildrenInQeueu(HeapNode node, Queue<HeapNode> queue){
	    	 HeapNode x = node.child;
	    	 while (x != null){
	    		 queue.add(x);
	    		 x = x.next;
	    		 }
	    	 return queue;
	    	 }
	      
	     
	      
	      public static void recPrintTree (HeapNode peek, Queue<HeapNode> queue, int[] array, Integer integer){
	    	  array[integer.intValue()] = peek.key;
	    	  integer = new Integer(integer.intValue()+1);
	    	  queue.remove();
	    	  putYourChildrenInQeueu(peek ,queue);
	    	  if (queue.isEmpty()){
	    		  return;
	    		  }
	    	  recPrintTree (queue.peek(), queue, array, integer);
	    	  }
	    	  
	    	  
		 
			public static void printTree(HeapNode root){
				int[] array = new int[(int)Math.pow(2,root.rank)];
				Queue<HeapNode> queue = new LinkedList<>();
				array[0] = root.key;
				Integer integer = new Integer(1);
				putYourChildrenInQeueu(root, queue);
				System.out.println("number of elements = " + array.length);
				System.out.println("rank = " + root.rank);
				if (!queue.isEmpty()){
					recPrintTree (queue.peek(), queue, array, integer);
					System.out.println(Arrays.toString(array));
					}
				else{
					System.out.println(Arrays.toString(array));
					}
					
				
				}
				 
			public void printHeap(){
				HeapNode x = first;
				while (x != null){
					printTree(x);
					x = x.next;
					}
				System.out.println();
			}
			

		 	/**
		 	//asume x and a.next not null!
		 	public void linkCase(HeapNode x, HeapNode prev, boolean prevIsNull){
		 		HeapNode min;
				HeapNode max;
				HeapNode Next;
				
				Next= x.next.next;
		 		max = maxKey(x, x.next);
				min = minKey(x, x.next);
				if (this.min == max){
					x = link(min, max);
					}
				else{
					x = link(max, min);
				 	}
				x.next = Next;
				 if (prevIsNull){
					 this.first = x;
					 }
					 else { 
						 prev.next = x; 
						 }
		 	}*/
		 	
		 	/**
		 	
		 	public void promotionCase(HeapNode x, HeapNode prev, boolean prevIsNull){
		 		prev =x;
				x = x.next;
				prevIsNull = false;
				}
		 	*/
		
		 	
	
	//*************************************************************************************************************//
	 
	 
	 
	
public class HeapNode{
	
	int key;
	HeapNode child;
	HeapNode next;
	int rank; 
	
	
	
	
	public HeapNode(int key){
		this.key = key;
	}
	
}

}
