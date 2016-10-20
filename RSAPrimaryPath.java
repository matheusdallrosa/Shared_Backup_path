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
	int fs[];
	double largura;
	Vertice src,dest;
	
	public Aresta(Vertice _src,Vertice _dest,double _larg){		
		src = _src;
		dest = _dest;
		largura = _larg;
		fs = new int[401];
		for(int i = 0; i < fs.length; i++) fs[i] = -1;//slot nao utilizado.		
	}
	boolean checarSlots(int n){
		for(int i = 0; i < fs.length-n; i++){
			boolean valido = true;
			for(int j = i; j < n && valido; j++)
				if(fs[j] != -1) valido = false;			
			if(valido) return true;
		}
		return false;
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
	int id,r;
	List<Aresta> viz;
	
	public Vertice(int _id,int _r){
		id = _id;
		r = _r;
	}
}

class Rotas{
	
}

class Config{
	public final static int PROBABILIDADE_ARESTA = 50; //percentual.
	public final static int B = 400;
	//capacidade em Gb/s e distância em KM.
	public final static int _BPSK = 0;
	public final static int _QPSK = 1;
	public final static int _8QAM = 2;
	public final static double MODULACAO[][] = {{12.5,4000}, //BPSK
												{25,2000},   //QPSK
												{37.5,1000}};//8QAM
}

public class RSAPrimaryPath implements IAlgorithm{	
	
	public String getDescription() {		
		return "Algoritmo para computar os caminhos primários da rede.";
	}
	
	public List getParameters() {		
		return null;
	}	
	
	/*
	 * Sorteia as arestas entre os pares de vértice, utilizando as 
	 * probabilidades do artigo.	 
	 * */
	public List<Vertice> sortAresta(List<Vertice> nodes,int p){
		Random rand = new Random();
		List<Vertice> grafo = new ArrayList<>();
		double largura[] = {12.5,25,50,100};
		for (Vertice src : nodes) {
			for (Vertice dest : nodes) {
				if(src.id == dest.id) continue;
				int rv = rand.nextInt(100)+1;
				//caso a probabilidade desta aresta for maior do que PROBABILIDADE_ARESTA.
				if(rv > p){
					int ra = rand.nextInt(4); 
					src.viz.add(new Aresta(src,dest,largura[ra]));
				}
			}
		}		
		return grafo;
	}
	
	/*
	 * Realiza os sorteios para uma nova von, primeiro sorteia os nodos
	 * depois distribui arestas entre os nodos.
	 * */
	public List<Vertice> nextVon(){
		Random rand = new Random();
		int nodos = (rand.nextInt() % 2 == 0) ? 3 : 4;
		
		List<Vertice> nodes = new ArrayList<>();
		for(int i = 0; i < nodos; i++)
			nodes.add(new Vertice(i,(rand.nextInt() % 2 == 0) ? 2 : 4));		
		
		return sortAresta(nodes,Config.PROBABILIDADE_ARESTA);
	}
	
	boolean dijkstra(NetPlan net,int src,int dest,int largura,int pai[]){		
		//calcular a menor rota de src e dest respeitando a largura de banda.	
		for (int i = 0; i < net.getNumberOfNodes(); i++) pai[i] = -1;
		
		return false;
	}
	
	public List<List<Integer>> RSA(List<Vertice> VON,NetPlan net){
		List<Aresta> arestas = new ArrayList<>();
		for (Vertice v : VON) 
			for (Aresta e : v.viz) 
				arestas.add(e);	
		List<List<Integer>> rotas = new ArrayList<>();		
		int pai[] = new int[net.getNumberOfNodes()];
		//ordenar as arestas pela largura.
		Collections.sort(arestas, new CMP_Aresta());
		for (Aresta aresta : arestas) {
			//será testado para todas as modulacoes
			for (int i = 0; i < Config.MODULACAO.length; i++) {
				
				//calculo da tamanho da janela.
				int n = (int)(aresta.largura/(Config.MODULACAO[i][0]*Config.B));
				
				//verificar se tem caminho de aresta.src para aresta.dest;
				if(dijkstra(net,aresta.src.id,aresta.dest.id,n,pai)){
					List<Integer> caminho = new ArrayList<>();
					
					//guardar a rota encontrada pelo dijkstra.
					for(int k = pai[aresta.src.id]; k != -1; k = pai[k])
						caminho.add(k);					
					rotas.add(caminho);
				}
			}
		}
		return rotas;
	}
	
	final static int REQ_AMOUNT = 3;
	public String executeAlgorithm(NetPlan net, Map algorithmParameters, Map net2planParameters) {		
		List<Node> nodes = net.getNodes();
		List<Vertice> verFis = new ArrayList<>();		
		Random rand = new Random();
		for (Node node : nodes)			
			verFis.add(new Vertice(node.getIndex(),20));
		
		//List<List<Vertice>> requestsVON = new ArrayList<>();
		for(int i = 0; i < REQ_AMOUNT; i++){
			List<Vertice> von = nextVon();
			RSA(von,net);
		}
		return null;
	}
}
