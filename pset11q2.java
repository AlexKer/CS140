/**
 * CS140 pset11q2 Block Stacking
 * @author AlexKer
*/
package cs140psets;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class pset11q2 {
	
	static int numTypes, numBlocks, height, n;
	static Block blocks[];
	static int dp[], prev[];

	public static void main(String[] args) {
		// String inputfile=args[0], outputfile=args[1];
		try {
			
			BufferedReader input = new BufferedReader(new FileReader(args[0]));
			numTypes = Integer.parseInt(input.readLine());
			n = numTypes*3;
			blocks = new Block[n]; 
			dp = new int[n]; 
			prev = new int[n];
			
			for(int i=0;i<n;i+=3) {
				String[] strs = input.readLine().split(" ");
				int[] dims = new int[3];
				for(int j=0;j<3;j++) {
					dims[j]=Integer.parseInt(strs[j]);
				}
				// enumerate 3 different orientations, making sure first dim smaller than second 
				// during comparison the smaller dim needs to be compared with the smaller, and same for bigger
				blocks[i]=new Block(Math.min(dims[0],dims[1]), Math.max(dims[0],dims[1]), dims[2]); // l,w,h
				blocks[i+1]=new Block(Math.min(dims[0],dims[2]), Math.max(dims[0],dims[2]), dims[1]); // l,h,w
				blocks[i+2]=new Block(Math.min(dims[1],dims[2]), Math.max(dims[1],dims[2]), dims[0]); // w,h,l
					
				// System.out.println(blocks[i]);
				// System.out.println(blocks[i+1]);
				// System.out.println(blocks[i+2]);
			}
			
			input.close();
		}catch (Exception|Error e) {
			 System.out.println(args[0]+"not found");
			 e.printStackTrace();
		}
		
		Arrays.sort(blocks); // sort according descending base area
		Arrays.fill(prev, -1); // fill prev array with -1, unchanged it means no blocks before that entry
		
		// dp[i] is the max height with the ith block as the top block
		// prev[i] is the index of block before the ith block
		for(int i=0;i<n;i++) {
			int max = 0;
			for(int j=0;j<i;j++) {
				if(blocks[j].L>blocks[i].L && blocks[j].W>blocks[i].W
						&& dp[j]>max) {
					max = dp[j];
					prev[i] = j;
				}
			}
			dp[i] = max+blocks[i].H;
		}
		
		// compute the max height, and record the tip block
		height = 0;
		int idx = 0;
		for(int i=0;i<n;i++) {
			if(dp[i]>height) {
				height = dp[i];
				idx = i;
			}
		}
		
		// to store the indices of blocks from bottom to top
		ArrayList<Integer> ans = new ArrayList<>();
		ans.add(0, idx);
		numBlocks = 1;
		// backtrace by looking at the last block
		while(prev[idx]!=-1) {
			numBlocks++;
			ans.add(0, prev[idx]);
			idx = prev[idx];
		}
		
		PrintWriter output;
		try {
			System.out.println("the tallest tower has "+numBlocks+" blocks and a height of "+height);
			output = new PrintWriter(new FileOutputStream(args[1]));
			output.println(numBlocks);
			// from base (largest) to tip (smallest)
			// dim: smaller of two numbers first, then height last
			for(int i: ans) {
				output.println(blocks[i].L+" "+blocks[i].W+" "+blocks[i].H);
			}
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static class Block implements Comparable<Block>{
		int L, W, H, area;
		public Block(int l0, int w0, int h0) {
			this.L=l0; this.W=w0; this.H=h0;
			this.area=l0*w0;
		}
		// sort in reverse order, largest area first
		@Override
		public int compareTo(Block o) {
			if(this.area==o.area) {
				return 0;
			}else if(this.area<o.area) {
				return 1;
			}else {
				return -1;
			}
		}
		public String toString() {
			return this.L+" x "+this.W+" x "+this.H;
		}
	}

}
