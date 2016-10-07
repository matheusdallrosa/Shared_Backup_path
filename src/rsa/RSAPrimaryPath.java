package rsa;

import com.net2plan.interfaces.networkDesign.IAlgorithm;
import com.net2plan.interfaces.networkDesign.Link; 
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.interfaces.networkDesign.Node;
import com.net2plan.interfaces.simulation.IEventProcessor;
//import com.net2plan.interfaces.simulation.SimAction;
import com.net2plan.interfaces.simulation.SimEvent;
import com.net2plan.utils.Triple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.net2plan.interfaces.networkDesign.Net2PlanException;
import com.net2plan.libraries.WDMUtils.ModulationFormat;
import com.net2plan.utils.DoubleUtils; 

class Aresta{
	double largura;
	Vertice dest;
	public Aresta(Vertice _dest,double _larg){
		dest = _dest;
		largura = _larg;
	}
}

class Vertice{
	int id,r,dist;
	List<Aresta> viz;
	public Vertice(int _id,int _r,int _dist){
		id = _id;
		r = _r;
		dist = _dist;
	}
}

public class RSAPrimaryPath implements IAlgorithm{	
	
	public String getDescription() {		
		return "Algoritmo para computar os caminhos primários da rede.";
	}
	
	public List getParameters() {		
		return null;
	}	
	
	public List<Vertice> sortAresta(List<Vertice> nodes,int p){
		Random rand = new Random();
		List<Vertice> grafo = new ArrayList<>();
		double largura[] = {12.5,25,50,100};
		for (Vertice src : nodes) {
			for (Vertice dest : nodes) {
				if(src.id == dest.id) continue;
				int rv = rand.nextInt(100)+1;
				if(rv > p){
					int ra = rand.nextInt(4); 
					src.viz.add(new Aresta(dest,largura[ra]));
				}
			}
		}		
		return grafo;
	}
	
	public List<Vertice> nextVon(){
		Random rand = new Random();
		int nodos = (rand.nextInt() % 2 == 0) ? 3 : 4;
		
		List<Vertice> nodes = new ArrayList<>();
		for(int i = 0; i < nodos; i++)
			nodes.add(new Vertice(i,(rand.nextInt() % 2 == 0) ? 3 : 4,0));		
		
		return sortAresta(nodes,50);
	}
	
	boolean dijkstra(NetPlan net,int src,int dest,int largura){		
		//calcular a menor rota de src e dest respeitando a largura de banda.		
		return false;
	}
	
	final static int REQ_AMOUNT = 3;
	public String executeAlgorithm(NetPlan net, Map algorithmParameters, Map net2planParameters) {		
		List<Node> nodes = net.getNodes();
		List<Vertice> verFis = new ArrayList<>();		
		Random rand = new Random();
		for (Node node : nodes)			
			verFis.add(new Vertice(node.getIndex(),20,0));
		List<List<Vertice>> requestsVON = new ArrayList<>();
		for(int i = 0; i < REQ_AMOUNT; i++)
			requestsVON.add(nextVon());
		
		return null;
	}
}
