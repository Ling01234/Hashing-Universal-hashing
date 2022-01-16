import java.io.*;
import java.util.*;


public class main {     


	public static void main(String[] args) {
		//TODO:build the hash table and insert keys using the insertKeyArray function.
		
		
		int [] tmp = new int [25];
		for (int i = 0; i < 25; i++) {
			tmp[i] = i;
		}
		
		Open_Addressing oa = new Open_Addressing(10, 0, -1);
		
		oa.insertKeyArrayResize(tmp);
		
		System.out.printf("%d -- %d", oa.m, oa.size);
		
	}
}