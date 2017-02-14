package rsa;

class Config{
	public final static int INF = 112345;
	public static int PROBABILIDADE_ARESTA; //percentual.
	public final static int B = 400;
	//capacidade em Gb/s e distância em KM.
	public final static int _BPSK = 0;
	public final static int _QPSK = 1;
	public final static int _8QAM = 2;
	public final static double MODULACAO[][] = {{12.5,4000}, //BPSK
												{25,2000},   //QPSK
												{37.5,1000}};//8QAM
	public static int REQ_AMOUNT;
}