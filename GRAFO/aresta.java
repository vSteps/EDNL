package GRAFO;

// armazena um valor do tipo E, origem e destino e se é direcionada ou não

public class aresta<E> {
    private vertice<?> origem;
    private vertice<?> destino;
    private E valor;
    private boolean direcionada;

    aresta(vertice<?> origem, vertice<?> destino, E valor, boolean direcionada) {
        this.origem = origem;
        this.destino = destino;
        this.valor = valor;
        this.direcionada = direcionada;
    }

    public E getValor() {
        return valor;
    }

    void setValor(E valor){
        this.valor = valor;
    }
    
    public vertice<?> getOrigem() {
        return origem;
    }

    public vertice<?> getDestino() {
        return destino;
    }

    public boolean ehDirecionada() {
        return direcionada;
    }

    public void setDirecionada(boolean direcionada) {
        this.direcionada = direcionada;
    }

    public boolean isLaco() {
        return origem == (destino);
    }

    @Override
    public String toString() {
        if (direcionada) {
            return origem + " -> " + destino + " : " + valor;
        } else {
            return origem + " -- " + destino + " : " + valor;
        }
    }
}
