import java.io.*;
import java.util.*;


public class Open_Addressing {
	public int m; // number of slots
	public int A; // the default random number
	int w;
	int r;
	int seed;
	public int[] Table;
	public static final double MAX_LOAD_FACTOR = 0.75;
	int size; // number of elements stored in the hash table

	protected Open_Addressing(int w, int seed, int A) {
		this.seed = seed;
		this.w = w;
		this.r = (int) (w - 1) / 2 + 1;
		this.m = power2(r);
		if (A == -1) {
			this.A = generateRandom((int) power2(w - 1), (int) power2(w), seed);
		} else {
			this.A = A;
		}
		this.Table = new int[m];
		for (int i = 0; i < m; i++) {
			Table[i] = -1;
		}
		this.size = 0;
	}

	/**
	 * Calculate 2^w
	 */
	public static int power2(int w) {
		return (int) Math.pow(2, w);
	}

	public static int generateRandom(int min, int max, int seed) {
		Random generator = new Random();
		if (seed >= 0) {
			generator.setSeed(seed);
		}
		int i = generator.nextInt(max - min - 1);
		return i + min + 1;
	}

	/**
	 * Implements the hash function g(k)
	 */
	public int probe(int key, int i) {
		
		Chaining c = new Chaining(this.w, this.seed, this.A);
		return (int) ((c.chain(key) + i) % power2(this.r));
	
	}

	/**
	 * Inserts key k into hash table. Returns the number of collisions encountered
	 */
	public int insertKey(int key) {
		
		
		int i;
		for (i = 0; i < this.m; i++) {
			
			int value = probe(key, i);
			
			if (this.Table[value] == -1) { //no element with same value
				this.Table[value] = key; //add key in array
				this.size++;
				
				return i;

			}

		}
		
		return power2(this.r);
	}


	/**
	 * Sequentially inserts a list of keys into the HashTable. Outputs total number of collisions
	 */
	public int insertKeyArray(int[] keyArray) {
		int collision = 0;
		for (int key : keyArray) {
			collision += insertKey(key);
		}
		return collision;
	}

	/**
	 * @param the key k to be searched
	 * @return an int array containing 2 elements:
	 * first element = index of k in this.Table if the key is present, = -1 if not present
	 * second element = number of collisions occured during the search
	 */
	public int[] searchKey(int k) {
		
		int i = 0;

		int [] output = new int[2];
		
		while (true) {
			int value = probe(k, i);
			
			if (this.Table[value] == k) {
				output[0] = value;
				output[1] = i;
				break;
			}
			
			if (i == Math.pow(2, this.r) - 1) {
				output[0] = -1;
				output[1] = i;
				break;
			}
			
			i++;

		}
		
		
		return output;
	}
	/**
	 * Removes key k from hash table. Returns the number of collisions encountered
	 */
	public int removeKey(int k){
		
		int index = searchKey(k)[0];
		
		if (index == -1) {
			return searchKey(k)[1];
		}
		
		this.Table[index] = -1; //remove the key
		this.size --; //update this.size
		return searchKey(k)[1]; //return number of collision
		

	}

	/**
	 * Inserts key k into hash table. Returns the number of collisions encountered,
	 * and resizes the hash table if needed
	 */
	public int insertKeyResize(int key) {
		
		if (((double) (this.size + 1)/this.m) <= MAX_LOAD_FACTOR) { //no resizing
			//System.out.println("not doubled");
			return insertKey(key);
		}
		
		//need to resize
		this.m = 2* this.m;
		//System.out.println("doubled");
		
		//copy of Table
		int [] Table1 = this.Table;
		
		//reset Table with correct size
		this.Table = new int [this.m];
		for (int i = 0; i < this.m; i++) {
			this.Table[i] = -1;
		}
		
		//update fields
		this.r++;
		this.w = (int) Math.floor(2 * this.r);
		this.A = generateRandom((int) power2(this.w - 1), (int) power2(this.w), this.seed);
		
		//relocate keys
		for (int j = 0; j < Table1.length; j++) {
			
			if (Table1[j] == -1 ) {
				continue; //nothing in Table1
			}
			insertKey(Table1[j]);
		}

		return insertKey(key);

	}


	/**
	 * Sequentially inserts a list of keys into the HashTable, and resize the hash table
	 * if needed. Outputs total number of collisions
	 */
	public int insertKeyArrayResize(int[] keyArray) {
		int collision = 0;
		for (int key : keyArray) {
			collision += insertKeyResize(key);
		}
		return collision;
	}

	/**
	 * @param the key k to be searched (and relocated if needed)
	 * @return an int array containing 2 elements:
	 * first element = index of k in this.Table (after the relocation) if the key is present, 
	 * 				 = -1 if not present
	 * second element = number of collisions occured during the search
	 */
	public int[] searchKeyOptimized(int k) {
		
		int [] output = new int [2];
		
		output = searchKey(k);
		
		if (output[0] == -1) { //key is not found
			return output;
		}
		

		int i = output[1]; //i for probing
		
		for (int j = 0; j < i; j++) {
			int value = probe(k, j);
			
			if (this.Table[value] == -1){ //encountered an empty slot
				removeKey(k);
				insertKeyResize(k);
				break;
			}
		}
		
		return output;
	}

	/**
	 * @return an int array of n keys that would collide with key k
	 */
	public int[] collidingKeys(int k, int n, int w) {
		
		int[] output = new int [n];
		
		for (int i = 0; i < n; i++) {
			output[i] = k + i * power2(w);
		}

		return output;
	}
}
