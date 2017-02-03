package rsa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VON {
	/*
	 * Sorteia as arestas entre os pares de vértice, utilizando as 
	 * probabilidades do artigo.	 
	 * */
	public List<Vertice> sortAresta(List<Vertice> nodes,int p){
		Random rand = new Random();
		List<Vertice> grafo = new ArrayList<>();
		double largura[] = {12.5,25,50,100};
		//testar todos os pares.		
		for (Vertice src : nodes) {
			for (Vertice dest : nodes) {
				if(src.id == dest.id) continue;
				int rv = rand.nextInt(100)+1;
				//caso a probabilidade desta aresta for maior do que PROBABILIDADE_ARESTA.
				if(rv > p){
					int ra = rand.nextInt(4); 
					src.viz.add(new Aresta(src,dest,largura[ra],0.));
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
}
