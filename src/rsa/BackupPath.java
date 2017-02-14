package rsa;

//import com.net2plan.interfaces.simulation.SimAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackupPath {

	boolean dijkstra(List<Vertice> verFis,int src,int dest,int slots,Aresta pai[], int modulacao){		
		//calcular a menor rota de src e dest respeitando a largura de banda.
		double []cost= new double[verFis.size()];
		boolean []vis = new boolean[verFis.size()];
		//resetando as estruturas que serão utilizadas.
		for (int i = 0; i < verFis.size(); i++){			
			vis[i] = false;
			cost[i] = Config.INF;
			pai[i] = new Aresta(new Vertice(-1,0),new Vertice(-1,0),0.,0.);			 
		}
		//distancia para raiz = 0
		cost[src] = 0;
		for(int k = 0; k < verFis.size(); k++){
			Vertice v = null;
			double mn = Config.INF;			
			for(Vertice i : verFis){
				if(cost[i.id] < mn && !vis[i.id]){
					v = i;
					mn = cost[i.id];
				}
			}
			if(mn == Config.INF) break;
			vis[v.id] = true;
			for(Aresta e : v.viz){
				Vertice u = e.dest;
				/*mudar o custo*/
				double eCost = (e.sharingNodes > 0) ? (1/(e.sharingNodes)) : 1;
				if(e.checarSlots(slots) && cost[u.id] > mn+eCost && e.distKM <= Config.MODULACAO[modulacao][1]){					
					cost[u.id] = mn+eCost;
					pai[u.id] = e;
				} 
			}
		}
		
		/*mudar o retorno*/
		return cost[dest] != Config.INF;
	}
	
	
	public List<List<Integer>> backupPath(List<Vertice> VON, List<Vertice> verFis){
		List<Aresta> arestas = new ArrayList<>();
		List<List<Integer>> rotas = new ArrayList<>();
		Aresta pai[] = new Aresta[verFis.size()];
		
		for (Vertice v : VON) 
			for (Aresta e : v.viz)
				if(e.src.id < e.dest.id)
					arestas.add(e);		
		//ordenar as arestas pela largura.
		Collections.sort(arestas, new CMP_Aresta());
	
		for (Aresta aresta : arestas) {
			
			//será testado para todas as modulacoes
			for (int i = 0; i < Config.MODULACAO.length; i++) {
				
				//calculo da tamanho da janela.
				int n = (int)Math.ceil( aresta.largura / Config.MODULACAO[i][0] );
				//verificar se tem caminho de aresta.src para aresta.dest;
				if(dijkstra(verFis,aresta.src.id,aresta.dest.id,n,pai,i)){
					List<Integer> caminho = new ArrayList<>();
					//guardar a rota encontrada pelo dijkstra.
					for(int k = aresta.dest.id; k != -1; k = pai[k].src.id){
						caminho.add(k);
					    pai[k].sharingNodes++;
						pai[k].slotAllocation(n, 1);					
					}
					rotas.add(caminho);
				}
			}
		}
		return rotas;			
	}
}
