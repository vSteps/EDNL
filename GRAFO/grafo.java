package GRAFO;

import java.util.List;

public interface grafo<V, E>{

    // insercao e remocao
    vertice<V> adicionarVertice(V valor);
    V removerVertice(vertice<V> v);
    aresta<E> adicionarAresta(vertice<V> v1, vertice<V> v2, E valor);
    aresta<E> adicionarArestaDirecionada(vertice<V> origem, vertice<V> destino, E valor);
    E removerAresta(aresta<E> aresta);

    // consultas geral
    int numVertices();
    int numArestas();
    List<vertice<V>> vertices();
    List<aresta<E>> arestas();
    boolean existeVertice(V valor);


    // substituicao
    V substituir(vertice<V> v, V x);
    E substituir(aresta<E> e, E x);

    // incidencia
    List<aresta<E>> arestasIncidentes(vertice<V> v);
    List<aresta<E>> arestasSaida(vertice<V> v);
    List<aresta<E>> arestasEntrada(vertice<V> v);
    List<aresta<E>> arestasEntre(vertice<V> u, vertice<V> v);
    List<vertice<V>> verticesAdjacentes(vertice<V> v);
    List<vertice<V>> sucessores(vertice<V> v);
    List<vertice<V>> predecessores(vertice<V> v);

    // acesso e navegacao
    vertice<V>[] finalVertices(aresta<E> e);
    vertice<V> oposto(vertice<V> v, aresta<E> e);
    boolean saoAdjacentes(vertice<V> v, vertice<V> w);

    // direcao
    boolean ehDirecionada(aresta<E> e);
    void direcionar (aresta<E> e);
    void desdirecionar(aresta<E> e);

    // graus
    int grau(vertice<V> v);
    int grauEntrada(vertice<V> v);
    int grauSaida(vertice<V> v);
}