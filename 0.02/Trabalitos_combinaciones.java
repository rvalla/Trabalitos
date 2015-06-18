import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;

/*Trabalitos es un juego de encastres. Consiste en un conjunto de piezas con
ranuras que permiten unirlas entre sí. El conjunto se compone de una pieza octogonal
con 8 ranuras (1 en cada lado del polígono), 1 pieza rectangular con 4 ranuras,
3 piezas rectangulares con 3 ranuras, 1 pieza rectangular con 2 ranuras y tiras de 3
largos distintos sin ranuras. La pieza octogonal y la pieza rectangular de 2 ranuras
presentan un orificio que permite pasar las tiras a través de las piezas.
Una plancha de trabalitos contiene un par de juegos del conjunto de piezas antes
descripto con dos piezas rectangulares de 3 ranuras repetidas. Todas las piezas
presentan un espesor aproximado de 5 milímetros y están realizadas en Goma Eva.
Presentan una gran flexibilidad y por el rozamiento que muestran entre sí permiten
encastres muy confiables.*/

//Programa para contar los encastres posibles de Trabalitos
class Trabalitos_combinaciones {
	
	//Inicialización de las distintas piezas y sus números de ranuras
	static BigInteger tipos[] = {new BigInteger("8"), new BigInteger("3"),
								new BigInteger("3"), new BigInteger("2"),
								new BigInteger("3"), new BigInteger("4")};
	
	static int cardinal_tipos = tipos.length;
	
	public static void main(String[] args) throws Exception{
		
		System.out.println();
		System.out.println("/////////////////////////////////////////////////////////////");
		System.out.println("		COMBINACIONES PARA TRABALITOS");
		System.out.println("/////////////////////////////////////////////////////////////");		
		System.out.println();
		System.out.println("El presente programa utiliza un modelo propuesto por Rodrigo");
		System.out.println("Valla y Emanuel Varela Blanco para estimar las combinaciones");
		System.out.println("posibles para el juego TRABALITOS de Eduardo Fernández.");
		System.out.println("TRABALITOS consiste en un conjunto de piezas con ranuras de 6");
		System.out.println("tipos distintos.");
		System.out.println("El modelo propuesto estima la cantidad de cadenas de piezas");
		System.out.println("posibles para el número de piezas pedido. Es importante aclarar");
		System.out.println("que para simplificar el modelo se han descartado las tiras.");
		System.out.println("Para cerrar el programa: 'salir'");
		
		Trabalitos_combinaciones();
		
	}
	
	static void Trabalitos_combinaciones() throws Exception{

		System.out.println();
		System.out.println("Tipear 'número total' de piezas'");
		System.out.println();
		
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);

		String input;
		input = br.readLine();
		System.out.println();
		
		int n;
		if (input.equals("salir")){
			exit();
		} else {
			try	{
				n = (Integer.parseInt(input));
				if (n < 1){
					System.out.println("El argumento no el válido.");
					System.out.println("Vuelva a intentarlo:");
					System.out.println();
					Trabalitos_combinaciones();
				} else { 
					if (n > 18){
						System.out.println("Me va a llevar un poco de tiempo hacer las cuentas...");
						System.out.println();
					}
					contarEncastres(n);
				}
			} catch (NumberFormatException u){
				System.out.println();
				System.out.println("'" + input + "' NO ES VALIDO");
				System.out.println("Vuelva a intentarlo:");
				System.out.println();
				Trabalitos_combinaciones();
			}
		}

		Trabalitos_combinaciones();	
		
	}
	
	//Método principal para contar encastres posibles...
	static void contarEncastres(int n){
		
		BigInteger total = new BigInteger("0");
		BigInteger[] parciales = new BigInteger[n];
		BigInteger parcial = new BigInteger("0");
		
		for (int i = 0; i < parciales.length; i++){
			parciales[i] = new BigInteger("0");
		}
		
		//Nos interesa contar todos los subconjuntos de i piezas (2 <= i <= n)
		for (int i = 2; i <= n; i++){
			
			int l = limitador(n,i);
			int[][] mS = matrizSubconjuntos(l, i);
			int[][] tS = matrizTipos(mS);
			
			for (int j = 0; j < mS.length; j++){
				if (decidirSi(tS[j], tS, j) == true){
					parcial = cadenasPosibles(mS[j], tS[j]);
					total = total.add(parcial);
					parciales[i-2] = parciales[i-2].add(parcial);
				}
			}
			
			System.out.println("De " + i + " piezas: " + parciales[i - 2]);
			
		}
		
		System.out.println("------------");
		System.out.println("Total: " + total);
		
	}
	
	//Método para contar posibilidades de empalme para las ranuras...
	static BigInteger cadenasPosibles(int paquete[], int tS[]){

		BigInteger combinaciones = new BigInteger("1");
		BigInteger dos = new BigInteger("2");
		
		for (int i = 0; i < paquete.length; i++){
			combinaciones = combinaciones.multiply(tipos[paquete[i] - 1]);
			combinaciones = combinaciones.multiply(tipos[paquete[i] - 1].subtract(BigInteger.ONE));
		}
		
		//Consideramos todos los órdenes posibles para las piezas
		combinaciones = combinaciones.multiply(factorial(paquete.length));
		
		//Descartamos los órdenes que son iguales a otros de atrás para adelante
		combinaciones = combinaciones.divide(dos);

		//Descontamos los órdenes en que dos piezas del mismo tipo tienen posiciones intercambiadas
		for (int i = 0; i < tS.length; i++){
			combinaciones = combinaciones.divide(factorial(tS[i]));
		}
		
		return combinaciones;
	
	}
	
	//Método para reducir el conjunto total cuando es posible...
	static int limitador (int n, int i){
		
		int limite = cardinal_tipos * i;
		
		if (limite > n){
			limite = n;
		}
	
		return limite;
		
	}
	
	//Método para decidir si contar la cadena o descartarla...
	static boolean decidirSi (int[] tipos, int[][] lista, int p){
	
		boolean dS = true;
		
		for (int i = 0; i < p; i++){
			if (Arrays.equals(tipos, lista[i])){
				dS = false;
				break;
			}
		}
		
		return dS;
		
	}
	
	//Método para armar los subconjuntos posibles del conjunto considerado...
	static int[][] matrizSubconjuntos (int n, int c){
	
		int[][] mS = new int[combinatorioInt(n, c)][c];
		
		for (int j = 0; j < mS[0].length; j++){
			mS[0][j] = j + 1;
		}
		
		caso = 0;
		getCombinaciones(n, c, 0, mS[0], mS);
		
		for(int i = 0; i < mS.length; i++){
			for (int j = 0; j < mS[i].length; j++){
				if (mS[i][j] == 0){
					mS[i][j]=mS[i-1][j];	
				}
			}
		}

		return mS;
		
	}
	
	//Método recursivo para hallar combinaciones... Llena los lugares que cambian de la matriz mS
	static int caso = 0;
	static void getCombinaciones(int n, int c, int p, int[] aux, int[][] mS){
       
    	if (c == 0){
    		
       		caso = caso + 1;
            return;
            
        }
       
        for (int i = p; i <= n - c; i++){
    	
	        mS[caso][mS[caso].length - c] = i%cardinal_tipos + 1;
            getCombinaciones(n, c-1, i+1, aux, mS);
        	
        }  
    
    }
	
	//Método para saber qué cantidad de cada tipo de piezas existe en cada cadena...
	static int[][] matrizTipos(int mS[][]){
	
		int[][] tipos = new int[mS.length][cardinal_tipos];
		
		for (int i = 0; i < mS.length; i++){
			for (int j = 0; j <  mS[i].length; j++){
				if (mS[i][j] > 0){
					tipos[i][(mS[i][j]-1)%cardinal_tipos] = tipos[i][(mS[i][j]-1)%cardinal_tipos] + 1;					
				}
			} 
		}
		
		return tipos;
		
	}
	
	//Método para calcular números combinatorios OUTPUT: BigInteger
	static BigInteger combinatorio(int n, int e){
	
		BigInteger nb = new BigInteger("1");
		BigInteger eb = new BigInteger("1");
		
		nb = factorial(n);
		eb = factorial(n-e);
		nb = nb.divide(eb);
		eb = factorial(e);
		nb = nb.divide(eb);
		
		return nb;
	
	}
	
	//Método para calcular números combinatorios OUTPUT: int
	static int combinatorioInt(int n, int e){
	
		BigInteger c = combinatorio(n, e);
		int combinatorioInt = c.intValue();
		
		return combinatorioInt;
	
	}
	
	//Método para calcular variaciones (n!/(n-k)!)...
	public static BigInteger variacion(int n, int e){
	
		BigInteger nb = new BigInteger("1");
		BigInteger eb = new BigInteger("1");
		
		nb = factorial(n);
		eb = factorial(n-e);
		nb = nb.divide(eb);
		
		return nb;
	
	}
	
	//Método para calcular el factorial de un número...
	static BigInteger factorial(int n){
		
		BigInteger f = new BigInteger("1");
		BigInteger x = new BigInteger("1");
		
		for (int i = 1; i <= n; i++) {
			f = f.multiply(x);
			x = x.add(BigInteger.ONE);
   		}
		return f;
		
	}
	
	//Salida del programa...
	static void exit () {
		System.exit(0);
	}

}