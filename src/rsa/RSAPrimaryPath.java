package rsa;

import com.net2plan.interfaces.networkDesign.IAlgorithm;
import com.net2plan.interfaces.networkDesign.Link; 
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
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


public class RSAPrimaryPath implements IAlgorithm{	
	
	public String getDescription() {		
		return "Algoritmo para computar os caminhos primários da rede.";
	}
	
	public List getParameters() {		
		return null;
	}	
	
	
	boolean dijkstra(List<Vertice> verFis,int src,int dest,int slots,Aresta pai[]){		
		//calcular a menor rota de src e dest respeitando a largura de banda.
		double []dist = new double[verFis.size()];
		boolean []vis = new boolean[verFis.size()];
		//resetando as estruturas que serão utilizadas.
		for (int i = 0; i < verFis.size(); i++){			
			vis[i] = false;
			dist[i] = Config.INF;
		}
		//distancia para raiz = 0
		dist[src] = 0;
		for(int k = 0; k < verFis.size(); k++){
			Vertice v = null;
			double mn = Config.INF;			
			for(Vertice i : verFis){
				if(dist[i.id] < mn && !vis[i.id]){
					v = i;
					mn = dist[i.id];
				}
			}
			if(mn == Config.INF) break;
			vis[v.id] = true;
			for(Aresta e : v.viz){
				Vertice u = e.dest;
				if(e.checarSlots(slots) && dist[u.id] > mn+e.distKM){					
					dist[u.id] = mn+e.distKM;
					pai[u.id] = e;
				} 
			}
		}
		return false;
	}
	
	public List<List<Integer>> RSA(List<Vertice> VON,List<Vertice> verFis,NetPlan net){
		List<Aresta> arestas = new ArrayList<>();
		for (Vertice v : VON) 
			for (Aresta e : v.viz) 
				arestas.add(e);	
		//ordenar as arestas pela largura.
		Collections.sort(arestas, new CMP_Aresta());
		
		List<List<Integer>> rotas = new ArrayList<>();		
		Aresta pai[] = new Aresta[net.getNumberOfNodes()];		
		for (Aresta aresta : arestas) {
			//será testado para todas as modulacoes
			for (int i = 0; i < Config.MODULACAO.length; i++) {
				
				//calculo da tamanho da janela.
				int n = (int)(aresta.largura/(Config.MODULACAO[i][0]*Config.B));
				
				//verificar se tem caminho de aresta.src para aresta.dest;
				if(dijkstra(verFis,aresta.src.id,aresta.dest.id,n,pai)){
					List<Integer> caminho = new ArrayList<>();
					
					//guardar a rota encontrada pelo dijkstra.
					for(int k = aresta.dest.id; k != -1; k = pai[k].src.id){
						caminho.add(k);
						pai[k].slotAllocation(n);
					}
					rotas.add(caminho);
				}
			}
		}
		return rotas;
	}
	
	final static int REQ_AMOUNT = 1;
	public String executeAlgorithm(NetPlan net, Map algorithmParameters, Map net2planParameters) {		
		List<Node> nodes = net.getNodes();
		List<Vertice> verFis = new ArrayList<>();
	 	NetworkLayer layer = net.getNetworkLayerDefault();
		for (Node u : nodes){
			Vertice vu = new Vertice(u.getIndex(),20);
			
			for(Link out : u.getOutgoingLinks(layer)){
				Vertice vv = new Vertice(out.getDestinationNode().getIndex(),20);
				vu.viz.add(new Aresta(vu,vv,-1.0,out.getLengthInKm()));
			}
			
			verFis.add(vu);
		}		
		VON vonGenerator = new VON();		
		for(int i = 0; i < REQ_AMOUNT; i++){
			List<Vertice> von = vonGenerator.nextVon();
			System.out.println("Von: quantia de nodos: " + von.size());
			for(Vertice v : von){
				System.out.print(v.id + ": ");
				for(Aresta e : v.viz){
					System.out.print("Vizinho: " + e.dest.id + " | Largura: " + e.largura + " - ");
				}
				System.out.println();
			}
			System.out.println();
			RSA(von,verFis,net);
			for(Vertice v : von){
				System.out.print(v.id + ": ");
				for(Aresta e : v.viz){
					System.out.print("Vizinho: " + e.dest.id + " ");
					int tot = 0;
					for(int s = 0; s < e.fs.length; s++)
						if(e.fs[s] != -1) tot++;
					System.out.print("Slots: " + tot + " | ");
				}
				System.out.println();
			}
			System.out.println();
		}
		return null;
	}
}
