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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
	Vertice src,dest;
	public Aresta(Vertice _src,Vertice _dest,double _larg){
		src = _src;
		dest = _dest;
		largura = _larg;
	}
}

class CMP_Aresta implements Comparator<Aresta>{
	public int compare(Aresta a1, Aresta a2){
		if(a1.largura < a2.largura) return 1;
		if(a1.largura > a2.largura) return -1;
		return 0;
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

class Rotas{
	
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
					src.viz.add(new Aresta(src,dest,largura[ra]));
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
	
	public List<Rotas> RSA(List<Vertice> VON){
		List<Aresta> arestas = new ArrayList<>();
		for (Vertice v : VON) 
			for (Aresta e : v.viz) 
				arestas.add(e);		
		Collections.sort(arestas, new CMP_Aresta());
		for (Aresta aresta : arestas) {
			//verificar se tem caminho de aresta.src para aresta.dest;
			
		}
		return null;
	}
	
	final static int REQ_AMOUNT = 3;
	public String executeAlgorithm(NetPlan net, Map algorithmParameters, Map net2planParameters) {		
		List<Node> nodes = net.getNodes();
		List<Vertice> verFis = new ArrayList<>();		
		Random rand = new Random();
		for (Node node : nodes)			
			verFis.add(new Vertice(node.getIndex(),20,0));
		
		//List<List<Vertice>> requestsVON = new ArrayList<>();
		for(int i = 0; i < REQ_AMOUNT; i++){
			List<Vertice> von = nextVon();
			RSA(von);
		}
		return null;
	}
}
