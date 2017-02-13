package rsa;

import java.util.Comparator;

public class Aresta{
	int fs[];
	int sharingNodes;
	double largura,distKM;
	Vertice src,dest;
	
	public Aresta(Vertice _src,Vertice _dest,double _larg,double _distKM){
		sharingNodes = 0;
		src = _src;
		dest = _dest;
		distKM = _distKM;
		largura = _larg;
		fs = new int[401];
		for(int i = 0; i < fs.length; i++) fs[i] = -1;//slot nao utilizado.		
	}
	
	boolean checarSlots(int n){
		for(int i = 0; i < fs.length-n; i++){
			boolean valido = true;
			for(int j = 0; j < n && valido; j++)
				if(fs[i+j] != -1) valido = false;			
			if(valido) return true;
		}
		return false;
	}
	
	void slotAllocation(int n, int type){		
		for(int i = 0; i < fs.length-n; i++){
			boolean valido = true;
			for(int j = 0; j < n && valido; j++)
				if(fs[i+j] != -1) valido = false;			
			if(valido){				
				for(int j = 0; j < n; j++) fs[i+j] = type;
				break;
			}
		}
	}
}

class CMP_Aresta implements Comparator<Aresta>{
	
	public int compare(Aresta a1, Aresta a2){
		if(a1.largura < a2.largura) return 1;
		if(a1.largura > a2.largura) return -1;
		return 0;
    }
}