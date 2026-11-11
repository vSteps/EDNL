package GRAFO;

public class vertice<V> {
    private V valor;
    int indice; // posicao na matriz de adjanecia

    public vertice(V valor) {
        this.valor = valor;
        this.indice = -1; // Inicializa o índice como -1
        
        }

        public V getValor(){
            return valor;
        }

        public void setValor(V valor){
            this.valor = valor;
        }

        @Override
        public String toString() {
            return String.valueOf(valor);
        }

    }