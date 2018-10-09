// Name: Chenxi Zhang
// DT228-2 Algorithm & Data Structure Assignment
// Implementation of Prim Algorithm using Adjacency lists

import java.io.*;

class Heap
{
    private int[] h;	   // heap array
    private int[] hPos;	   // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size

    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) {
        N = 0;
        h = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public void siftUp(int k) {
        int v = h[k];
        h[0] = 0;

        while ( dist[v] < dist[h[k/2]]) {
          h[k] = h[k/2];
          hPos[h[k]] = k;
          k = k/2;
        }

        h[k] = v;
        hPos[v] = k;
    }

    public void siftDown(int k) {
        int v, j;
        v = h[k];

        while (k < N/2) {
          j = 2 * k;
          if (j < N && dist[j] < dist[j + 1]) ++j;
          if (dist[v] < dist[h[j]]) break;
          h[k] = h[j];
          hPos[h[k]] = k;
          k = j;
        }

        h[k] = v;
        hPos[v] = k;
    }

    public void insert(int x) {
        h[++N] = x;
        siftUp(N);
    }

    public int remove() {
        int v = h[1];
        hPos[v] = 0; // v is no longer in heap
        h[N+1] = 0;  // put null node into empty spot

        h[1] = h[N--];
        siftDown(1);

        return v;
    }
    /* displaying heap structure
    public void display() {
       System.out.println("\n\nThe tree structure of the heaps is:");
       System.out.println( toChar(h[1]) );
       for(int i = 1; i<= N/2; i = i * 2) {
          for(int j = 2*i; j < 4*i && j <= N; ++j)
             System.out.print( toChar(h[j]) + "  ");
           System.out.println();
      }
    }

    // convert vertex into char for pretty printing
    private char toChar(int u)
    {
        return (char)(u + 64);
    }
    */
}

class Graph {
    class Node {
        public int vert;
        public int wgt;
        public Node next;
    }

    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;

    // used for traversing graph
    //private int[] visited;
    //private int id;


    // default constructor
    public Graph(String graphFile)  throws IOException {
      int u, v;
      int e, wgt;
      Node t;

      FileReader fr = new FileReader(graphFile);
      BufferedReader reader = new BufferedReader(fr);

      String splits = " +";  // multiple whitespace as delimiter
      String line = reader.readLine();
      String[] parts = line.split(splits);
      System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
      V = Integer.parseInt(parts[0]);
      E = Integer.parseInt(parts[1]);

      // create sentinel node
      z = new Node();
      z.next = z;

      // create adjacency lists, initialised to sentinel node z
      adj = new Node[V+1];
      for(v = 1; v <= V; ++v)
        adj[v] = z;

      // read the edges
      System.out.println("Reading edges from text file");

      for(e = 1; e <= E; ++e) {
        line = reader.readLine();
        parts = line.split(splits);
        u = Integer.parseInt(parts[0]);
        v = Integer.parseInt(parts[1]);
        wgt = Integer.parseInt(parts[2]);

        System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));
        // write code to put edge into adjacency matrix
        t = new Node();
        t.vert = v;
        t.wgt = wgt;
        t.next = adj[u];
        adj[u] = t;
        t = new Node();
        t.vert = u;
        t.wgt = wgt;
        t.next = adj[v];
        adj[v] = t;
      }
    }

    // convert vertex into char for pretty printing
    private char toChar(int u)
    {
        return (char)(u + 64);
    }

    // method to display the graph representation
    public void display() {
        int v;
        Node n;

        for(v=1; v<=V; ++v) {
            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = adj[v]; n != z; n = n.next)
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");
        }
        System.out.println("");
    }



	public void MST_Prim(int s)
	{
        int v, u;
        int wgt, wgt_sum = 0;
        int[]  dist, parent, hPos;
        Node t;
        dist = new int[V + 1];
        parent = new int[V + 1];
        hPos = new int[V + 1];
        //code here
        for (v = 1; v <= V; v++) {
          dist[v] = Integer.MAX_VALUE;
          parent[v] = 0;
          hPos[v] = 0;
        }
        dist[s] = 0;
        Heap pq = new Heap(V, dist, hPos);
        pq.insert(s);

        while (!pq.isEmpty()) {
          // most of alg here
          v = pq.remove();
          dist[v]  = -dist[v];
          t = adj[v];
          // for every adjacent edge on vertex v
          while (t != z) {
            u = t.vert;
            // personal test code
            //System.out.println("comparing distance between " + toChar(v) + " and " + toChar(u));
            //System.out.println("Weight: " + t.wgt + " Current Dist: " + dist[u]);
            if (t.wgt < dist[u]) {
              dist[u] = t.wgt;
              parent[u] = v;

              if (hPos[u] == 0) {
                pq.insert(u);
              } else {
                pq.siftUp(hPos[u]);
              }
            }
            t = t.next;
          }

          mst = parent;
        }
        // calculate total weight of MST
        for (int i : dist) {
          wgt_sum += Math.abs(i);
        }
        System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");
      }

    public void showMST()
    {
            System.out.print("\n\nMinimum Spanning tree parent array is:\n");
            for(int v = 1; v <= V; ++v)
              if (mst[v] != 0) {
                System.out.println(toChar(v) + " -> " + toChar(mst[v]));
              }
            System.out.println("");
    }

}

public class PrimLists {
    public static void main(String[] args) throws IOException
    {
        int s = 1;
        String fname = "myGraph.txt";

        Graph g = new Graph(fname);
        g.display();
        g.MST_Prim(s);
        g.showMST();

    }


}
