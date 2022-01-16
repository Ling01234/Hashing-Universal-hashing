import java.io.*;
import java.util.*;

public class Universal_Hashing extends Open_Addressing{
	int a;
	int b;
	int p;

	protected Universal_Hashing(int w, int seed) {
		super(w, seed, -1);
		int temp = this.m+1; // m is even, so temp is odd here
		while(!isPrime(temp)) {
			temp += 2;
		}
		this.p = temp;
		a = generateRandom(0, p, seed);
		b = generateRandom(-1, p, seed);
	}
	
	/**
	 * Checks if the input int is prime
	 */
	public static boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i*i <= n; i++) {
        	if (n % i == 0) return false;
        }
        return true;
    }
	
	/**
     * Implements universal hashing
     */
	@Override
    public int probe(int key, int i) {
    	
		int h = ((this.a * key + this.b) % this.p) % this.m;
		
		return (h + i) % power2(this.r);
		
		
		
    }
	
	@Override
	public int insertKey(int key) {
		
		
		int i;
		for (i = 0; i < this.m; i++) {
			
			int value = probe(key, i);
			
			if (this.Table[value] == -1) { //no element with same value
				Table[value] = key; //add key in array
				this.size++;
				
				return i;

			}

		}
		
		return this.m;
	}
	

    /**
     * Inserts key k into hash table. Returns the number of collisions encountered,
     * and resizes the hash table if needed
     */
	@Override
    public int insertKeyResize(int key) {

		if ((this.size + 1)/this.m <= MAX_LOAD_FACTOR) { //no resizing
			return insertKey(key);
	
		}
		
		//need to resize
		this.m = 2* this.m;
		
		//update fields
		int temp = this.m+1; // m is even, so temp is odd here
		while(!isPrime(temp)) {
			temp += 2;
		}
		this.p = temp;
		this.a = generateRandom(0, p, seed);
		this.b = generateRandom(-1, p, seed);
		

		//copy of Table
		int [] Table1 = this.Table;
		
		//reset Table with correct size
		this.Table = new int [this.m];
		for (int i = 0; i < this.m; i++) {
			this.Table[i] = -1;
		}
		
		
		//relocate keys
		for (int i = 0; i < Table1.length; i++) {
			
			if (Table1[i] == -1 ) {
				continue; //nothing in Table1
			}
			insertKey(Table1[i]);
		}

		return insertKey(key);
    }
}
