package GRAFO;
import java.util.ArrayList;
import java.util.List;
// TAD Grafo implementado com matriz de adjacência.

public class grafoImp<V, E> implements grafo<V, E> {
    private final ArrayList<vertice<V>> vertices = new ArrayList<>();
    private final ArrayList<aresta<E>> arestas = new ArrayList<>();
    private final ArrayList<ArrayList<ArrayList<aresta<E>>>> matriz = new ArrayList<>();

    // consultas gerais
    @Override
    public int numVertices() {
        return vertices.size();
    }
    @Override
    public int numArestas() {
        return arestas.size();
    }
    @Override
    public List<vertice<V>> vertices() {
        return new ArrayList<>(vertices);
    }
    @Override
    public List<aresta<E>> arestas() {
        return new ArrayList<>(arestas);
    }
    @Override
    public boolean existeVertice(V valor) {
        for (vertice<V> v : vertices) {
            if (v.getValor() == null ? valor == null : v.getValor().equals(valor)) {
                return true;
            }
        }
        return false;
    }

    // insercao e remocao
    @Override
    public vertice<V> adicionarVertice(V valor) {
        vertice<V> v = new vertice<>(valor);
        int n = vertices.size();
        v.indice = n;
        vertices.add(v);
        // nova coluna em todas as linhas existentes
        for (ArrayList<ArrayList<aresta<E>>> linha : matriz) {
            linha.add(new ArrayList<>());
        }
        // nova linha
        ArrayList<ArrayList<aresta<E>>> novaLinha = new ArrayList<>();
        for (int j = 0; j <= n; j++) {
            novaLinha.add(new ArrayList<>());
        }
        matriz.add(novaLinha);
        return v;
    }
    // Remove o vértice e todas as arestas incidentes. Retorna o valor que ele guardava.
    @Override
    public V removerVertice(vertice<V> v) {
        validar(v);
        for (aresta<E> e : arestasIncidentes(v)) {
            removerAresta(e);
        }
        int idx = v.indice;
        matriz.remove(idx);
        for (ArrayList<ArrayList<aresta<E>>> linha : matriz) {
            linha.remove(idx);
        }
        vertices.remove(idx);
        for (int i = idx; i < vertices.size(); i++) {
            vertices.get(i).indice = i;
        }
        v.indice = -1;
        return v.getValor();
    }
    @Override
    public aresta<E> adicionarAresta(vertice<V> u, vertice<V> v, E valor) {
        return inserirAresta(u, v, valor, false);
    }
    @Override
    public aresta<E> adicionarArestaDirecionada(vertice<V> u, vertice<V> v, E valor) {
        return inserirAresta(u, v, valor, true);
    }
    private aresta<E> inserirAresta(vertice<V> u, vertice<V> v, E valor, boolean direcionada) {
        validar(u);
        validar(v);
        aresta<E> e = new aresta<>(u, v, valor, direcionada);
        arestas.add(e);
        guardar(e);
        return e;
    }
    /** Remove a aresta e retorna o valor que ela guardava. */
    @Override
    public E removerAresta(aresta<E> e) {
        validar(e);
        arestas.remove(e);
        desguardar(e);
        return e.getValor();
    }

    // substituicao 
    @Override
    public V substituir(vertice<V> v, V x) {
        validar(v);
        V antigo = v.getValor();
        v.setValor(x);
        return antigo;
    }
    @Override
    public E substituir(aresta<E> e, E x) {
        validar(e);
        E antigo = e.getValor();
        e.setValor(x);
        return antigo;
    }

    // direcao

    @Override
    public boolean ehDirecionada(aresta<E> e) {
        validar(e);
        return e.ehDirecionada();
    }
    @Override
    public void direcionar(aresta<E> e) {
        validar(e);
        if (e.ehDirecionada()) return;
        desguardar(e);
        e.setDirecionada(true);
        guardar(e);
    }
    @Override
    public void desdirecionar(aresta<E> e) {
        validar(e);
        if (!e.ehDirecionada()) return;
        desguardar(e);
        e.setDirecionada(false);
        guardar(e);
    }

    // Incidência e Adjacencia

    @Override
    public List<aresta<E>> arestasIncidentes(vertice<V> v) {
        validar(v);
        List<aresta<E>> res = new ArrayList<>();
        for (aresta<E> e : arestas) {
            if (e.getOrigem() == v || e.getDestino() == v) {
                res.add(e);
            }
        }
        return res;
    }
    // arestas que saem de v: direcionadas v -> x e não direcionadas incidentes a v.
    @Override
    public List<aresta<E>> arestasSaida(vertice<V> v) {
        validar(v);
        List<aresta<E>> res = new ArrayList<>();
        for (ArrayList<aresta<E>> celula : matriz.get(v.indice)) {
            res.addAll(celula);
        }
        return res;
    }
    // aestas que chegam em v: direcionadas x -> v e não direcionadas incidentes a v.
    @Override
    public List<aresta<E>> arestasEntrada(vertice<V> v) {
        validar(v);
        List<aresta<E>> res = new ArrayList<>();
        for (ArrayList<ArrayList<aresta<E>>> linha : matriz) {
            res.addAll(linha.get(v.indice));
        }
        return res;
    }
    // Todas as arestas guardadas em matriz[u][v] (u -> v e não direcionadas u--v).
    @Override
    public List<aresta<E>> arestasEntre(vertice<V> u, vertice<V> v) {
        validar(u);
        validar(v);
        return new ArrayList<>(matriz.get(u.indice).get(v.indice));
    }
    // vizinhos de v em qualquer sentido, sem repetição.
    @Override
    public List<vertice<V>> verticesAdjacentes(vertice<V> v) {
        validar(v);
        List<vertice<V>> res = new ArrayList<>();
        for (int j = 0; j < vertices.size(); j++) {
            if (!matriz.get(v.indice).get(j).isEmpty()
                    || !matriz.get(j).get(v.indice).isEmpty()) {
                res.add(vertices.get(j));
            }
        }
        return res;
    }
    // vértices x tal que existe aresta v -> x (ou v--x). */
    @Override
    public List<vertice<V>> sucessores(vertice<V> v) {
        validar(v);
        List<vertice<V>> res = new ArrayList<>();
        for (int j = 0; j < vertices.size(); j++) {
            if (!matriz.get(v.indice).get(j).isEmpty()) {
                res.add(vertices.get(j));
            }
        }
        return res;
    }
    // vértices x tal que existe aresta x -> v (ou x--v). */
    @Override
    public List<vertice<V>> predecessores(vertice<V> v) {
        validar(v);
        List<vertice<V>> res = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            if (!matriz.get(i).get(v.indice).isEmpty()) {
                res.add(vertices.get(i));
            }
        }
        return res;
    }
    // Graus

    // Número de arestas incidentes (laço conta 2).
    @Override
    public int grau(vertice<V> v) {
        validar(v);
        int g = 0;
        for (aresta<E> e : arestas) {
            if (e.getOrigem() == v) g++;
            if (e.getDestino() == v) g++;
        }
        return g;
    }
    @Override
    public int grauSaida(vertice<V> v) {
        return arestasSaida(v).size();
    }
    @Override
    public int grauEntrada(vertice<V> v) {
        return arestasEntrada(v).size();
    }
  
    // Acesso e Navegacao

    // Retorna os dois extremos da aresta: [origem, destino].
    @Override
    @SuppressWarnings("unchecked")
    public vertice<V>[] finalVertices(aresta<E> e) {
        validar(e);
        return (vertice<V>[]) new vertice<?>[] { e.getOrigem(), e.getDestino() };
    }
    // Dado um extremo v da aresta e, retorna o outro extremo.
    @Override
    @SuppressWarnings("unchecked")
    public vertice<V> oposto(vertice<V> v, aresta<E> e) {
        validar(v);
        validar(e);
        if (e.getOrigem() == v) return (vertice<V>) e.getDestino();
        if (e.getDestino() == v) return (vertice<V>) e.getOrigem();
        throw new IllegalArgumentException("Vértice não é extremo da aresta");
    }
    // Verdadeiro se existe ao menos uma aresta ligando v e w (em qualquer sentido).
    @Override
    public boolean saoAdjacentes(vertice<V> v, vertice<V> w) {
        validar(v);
        validar(w);
        return !matriz.get(v.indice).get(w.indice).isEmpty()
            || !matriz.get(w.indice).get(v.indice).isEmpty();
    }
    // auxiliares
    private void guardar(aresta<E> e) {
        int i = e.getOrigem().indice;
        int j = e.getDestino().indice;
        matriz.get(i).get(j).add(e);
        if (!e.ehDirecionada() && i != j) {
            matriz.get(j).get(i).add(e);
        }
    }
    private void desguardar(aresta<E> e) {
        int i = e.getOrigem().indice;
        int j = e.getDestino().indice;
        matriz.get(i).get(j).remove(e);
        if (!e.ehDirecionada() && i != j) {
            matriz.get(j).get(i).remove(e);
        }
    }
    private void validar(vertice<V> v) {
        if (v == null || v.indice < 0 || v.indice >= vertices.size()
                || vertices.get(v.indice) != v) {
            throw new IllegalArgumentException("Vértice não pertence ao grafo");
        }
    }
    private void validar(aresta<E> e) {
        if (e == null || !arestas.contains(e)) {
            throw new IllegalArgumentException("Aresta não pertence ao grafo");
        }
    }
    // Imprime a matriz de adjacência (cada célula mostra os valores das arestas). 
    public void imprimirMatriz() {
        int n = vertices.size();
        System.out.print("        ");
        for (vertice<V> v : vertices) System.out.printf("%-18s", v);
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.printf("%-8s", vertices.get(i));
            for (int j = 0; j < n; j++) {
                ArrayList<aresta<E>> c = matriz.get(i).get(j);
                StringBuilder sb = new StringBuilder("[");
                for (int k = 0; k < c.size(); k++) {
                    if (k > 0) sb.append(",");
                    sb.append(c.get(k).getValor());
                }
                sb.append("]");
                System.out.printf("%-18s", sb);
            }
            System.out.println();
        }
    }
}