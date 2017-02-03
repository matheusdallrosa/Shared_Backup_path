package rsa;

import java.util.List;

class Vertice{
	int id,resource;
	List<Aresta> viz;
	
	public Vertice(int _id,int _resource){
		id = _id;
		resource = _resource;
	}
}
