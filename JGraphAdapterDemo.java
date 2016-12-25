package graph;

import bpn.*;
import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

import org.jgraph.*;
import org.jgraph.graph.*;

import org.jgrapht.*;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;

// resolve ambiguity
import org.jgrapht.graph.DefaultEdge;



public class JGraphAdapterDemo
    extends JApplet
{
    private static final long serialVersionUID = 3256444702936019250L;
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
    private static final Dimension DEFAULT_SIZE = new Dimension(1920, 1080);

    //
    private JGraphModelAdapter<String, RelationshipEdge> jgAdapter;

 
     
    public static void graph()
    {
        JGraphAdapterDemo applet = new JGraphAdapterDemo();
        applet.init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(applet);
        frame.setTitle("Neural Network Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        double value;
        String result;
        // create a JGraphT graph
       ListenableGraph<String, RelationshipEdge> g =
            new ListenableDirectedMultigraph<String, RelationshipEdge>(
                RelationshipEdge.class);

        // create a visualization using JGraph, via an adapter
        jgAdapter = new JGraphModelAdapter<String, RelationshipEdge>(g);

        JGraph jgraph = new JGraph(jgAdapter);

        adjustDisplaySettings(jgraph);
        getContentPane().add(jgraph);
        resize(DEFAULT_SIZE);
        String[] input=new String[16];
        String[] hidden=new String[9];
        String output;
        
        for(int i=0;i<15;i++)
            input[i]="i "+i;
            input[15]="i/p BIAS";
        
        
         for(int i=0;i<8;i++)
            hidden[i]="h "+i;
            hidden[8]="hid/BIAS";
         
         output="output";
        
        
        
       
        for(int i=0;i<16;i++)
        {
        g.addVertex(input[i]);
        }
         for(int i=0;i<9;i++)
        {
        g.addVertex(hidden[i]);
        }
         
        g.addVertex(output);
        
        
     
     
            for(int i=0;i<16;i++){
                for(int j=0;j<8;j++)
                {   value=BPN.wih[i][j];
                    result = String.format("%.2f", value);
               g.addEdge(input[i], hidden[j], new
RelationshipEdge<>(input[i], hidden[j], result));
                }
            }
            
            
                   
                for(int i=0;i<9;i++)
                {
                    value=BPN.who[i][0];
                    result = String.format("%.2f", value);
               g.addEdge(hidden[i], output, new
RelationshipEdge<>(hidden[i], output, result));
                }
            
           
        
            
            
            
         int ymult=1;
        
        for(int i=0;i<16;i++)
        {
        positionVertexAt(input[i], 100, 50*ymult);
        ymult++;
        }
        ymult=1;
         for(int i=0;i<9;i++)
        {
        positionVertexAt(hidden[i], 500,200+ 50*ymult);
        ymult++;
        }
         
         positionVertexAt(output,800,400);
        

    }

    private void adjustDisplaySettings(JGraph jg)
    {
        jg.setPreferredSize(DEFAULT_SIZE);

        Color c = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter("bgcolor");
        } catch (Exception e) {
        }

        if (colorStr != null) {
            c = Color.decode(colorStr);
        }

        jg.setBackground(c);
    }

    @SuppressWarnings("unchecked") 
    private void positionVertexAt(Object vertex, int x, int y)
    {
        DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
        AttributeMap attr = cell.getAttributes();
        Rectangle2D bounds = GraphConstants.getBounds(attr);

        Rectangle2D newBounds =
            new Rectangle2D.Double(
                x,
                y,
                bounds.getWidth(),
                bounds.getHeight());

        GraphConstants.setBounds(attr, newBounds);

  
        AttributeMap cellAttr = new AttributeMap();
        cellAttr.put(cell, attr);
        jgAdapter.edit(cellAttr, null, null, null);
    }

   
    private static class ListenableDirectedMultigraph<V, E>
        extends DefaultListenableGraph<V, E>
        implements DirectedGraph<V, E>
    {
        private static final long serialVersionUID = 1L;

        ListenableDirectedMultigraph(Class<E> edgeClass)
        {
            super(new DirectedMultigraph<V, E>(edgeClass));
        }
    }
    
    
    
    public static class RelationshipEdge<V> extends DefaultEdge {
        private V v1;
        private V v2;
        private String label;

        public RelationshipEdge(V v1, V v2, String label) {
            this.v1 = v1;
            this.v2 = v2;
            this.label = label;
        }

        public V getV1() {
            return v1;
        }

        public V getV2() {
            return v2;
        }

        public String toString() {
            return label;
        }
    } 
    
}


