package rsa;

import com.net2plan.interfaces.networkDesign.IAlgorithm;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.interfaces.networkDesign.Node;
import com.net2plan.interfaces.networkDesign.Link;
import com.net2plan.utils.Triple;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Testes implements IAlgorithm {
	public String getDescription() {		
		return "Algoritmo para computar os caminhos primários da rede.";
	}
	
	public List<Triple<String,String,String>>	 getParameters() {		
		ArrayList<Triple<String,String,String>> params = new ArrayList<>();
		params.add(new Triple<String,String,String>("VON","100","Quantidade de VONs", false));
		return params;
	}	
	
	public String executeAlgorithm(NetPlan net, Map<String,String> algorithmParameters, Map<String,String> net2planParameters) {

		try	{
			Config.REQ_AMOUNT = Integer.valueOf(algorithmParameters.get("VON"));
		} catch(Exception e)	{
			System.out.println("Erro: "+e.getMessage());
		}
		
		List<Node> nodes = net.getNodes();
		List<Vertice> verFis = new ArrayList<>();
		VON vonGenerator = new VON();
		BackupPath vonBackup = new BackupPath();
		RSAPrimaryPath pPath = new RSAPrimaryPath();
		List<List<Vertice>> von = new ArrayList<>(Config.REQ_AMOUNT);
		
		
	 	NetworkLayer layer = net.getNetworkLayerDefault();
	 	for (Node u : nodes) verFis.add(new Vertice(u.getIndex(),20));	 	
		
	 	for (Node u : nodes){
	 		for(Link out : u.getOutgoingLinks(layer)){
	 			Node v = out.getDestinationNode();
	 			if(u.getIndex() < v.getIndex()){
	 				Vertice vu = verFis.get(u.getIndex());
	 				Vertice vv = verFis.get(v.getIndex());
	 				
	 				Aresta e = new Aresta(vu,vv,0,out.getLengthInKm());
	 				vu.viz.add(e);
	 				
	 				Aresta e2 = new Aresta(vv,vu,0,out.getLengthInKm());
	 				e2.fs = e.fs;
	 				vv.viz.add(e2);
	 			}		 		
	 		}
	 	}

		
		for(int i = 0; i < Config.REQ_AMOUNT; i++){
			von.add(vonGenerator.nextVon());
			pPath.RSA(von.get(i),verFis,net);
		}
		
		for(int i = 0; i < Config.REQ_AMOUNT; i++)
			vonBackup.backupPath(von.get(i),verFis);
	
		int sumP = 0;
		int sumS = 0;
		int emptyS = 0;
		
		for(Vertice v : verFis){
			for(Aresta e : v.viz){
				for(int s = 0; s < e.fs.length; s++){
					if(e.fs[s] == 0) sumP++;
					else if(e.fs[s] == 1) sumS++;
					else emptyS++;
				}
			}
		}
		
		/*quantidade de vons, probabilidade de conexão, quantidade de espectro utilizado por caminhos de backup, quantidade de espectro consumido por primarios
		 * quantidade de espectro não utilizado
		 */
		System.out.println(Config.REQ_AMOUNT + ", " + Config.PROBABILIDADE_ARESTA + ", " + sumS/2 + ", " + sumP/2 + ", " + emptyS/2);
		
		return "Algoritmo executado corretamente.";
	}
	
}
