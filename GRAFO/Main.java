package GRAFO;
public class Main {
    public static void main(String[] args) {
        // Grafo simples (não direcionado)       
        System.out.println("=== Grafo simples ===");
            grafoImp<String, String> g = new grafoImp<>();
        vertice<String> a = g.adicionarVertice("A");
        vertice<String> b = g.adicionarVertice("B");
        vertice<String> c = g.adicionarVertice("C");
        g.adicionarAresta(a, b, "ab");
        g.adicionarAresta(b, c, "bc");
        g.imprimirMatriz();
        System.out.println("V=" + g.numVertices() + " E=" + g.numArestas());
        System.out.println("A e B adjacentes? " + g.saoAdjacentes(a, b));
        System.out.println("A e C adjacentes? " + g.saoAdjacentes(a, c));
        System.out.println("grau(B) = " + g.grau(b));
        System.out.println("vizinhos de B = " + g.verticesAdjacentes(b));
        System.out.println("existeVertice(\"C\") = " + g.existeVertice("C"));
        System.out.println("existeVertice(\"Z\") = " + g.existeVertice("Z"));
        // Multigrafo misto
        System.out.println("\n=== Multigrafo misto ===");
            grafoImp<String, String> m = new grafoImp<>();
        vertice<String> x = m.adicionarVertice("X");
        vertice<String> y = m.adicionarVertice("Y");
        vertice<String> z = m.adicionarVertice("Z");
        aresta<String> e1 = m.adicionarAresta(x, y, "e1");             
        aresta<String> e2 = m.adicionarAresta(x, y, "e2");              
        m.adicionarArestaDirecionada(x, y, "e3");                       
        m.adicionarArestaDirecionada(y, x, "e4");                      
        m.adicionarAresta(z, z, "e5");                                  
        aresta<String> e6 = m.adicionarArestaDirecionada(y, z, "e6");   
        m.imprimirMatriz();
        System.out.println("arestas entre X e Y = " + m.arestasEntre(x, y));
        System.out.println("arestas entre Y e X = " + m.arestasEntre(y, x));
        System.out.println("grau(X) = " + m.grau(x) + " (esperado 4)");
        System.out.println("grau(Z) = " + m.grau(z) + " (esperado 3: laço conta 2 + e6)");
        System.out.println("grauSaida(X) = " + m.grauSaida(x) + " (esperado 3)");
        System.out.println("grauEntrada(X) = " + m.grauEntrada(x) + " (esperado 3)");
        System.out.println("sucessores(Y) = " + m.sucessores(y));
        System.out.println("predecessores(Y) = " + m.predecessores(y));
        try {
            m.oposto(x, e6);
        } catch (IllegalArgumentException ex) {
            System.out.println("oposto(X, e6) -> erro esperado: " + ex.getMessage());
        }
        System.out.println("oposto(Y, e6) = " + m.oposto(y, e6));
        vertice<String>[] ext = m.finalVertices(e6);
        System.out.println("finalVertices(e6) = [" + ext[0] + ", " + ext[1] + "]");
        System.out.println("e1 direcionada? " + m.ehDirecionada(e1));
        // substituir (retorna o valor antigo)
        System.out.println("\nsubstituir(X, \"X2\") -> antigo = " + m.substituir(x, "X2"));
        System.out.println("substituir(e1, \"e1b\") -> antigo = " + m.substituir(e1, "e1b"));
        // direcionar / desdirecionar
        m.direcionar(e2);
        System.out.println("\napós direcionar e2: X->Y = " + m.arestasEntre(x, y)
                + " | Y->X = " + m.arestasEntre(y, x));
        m.desdirecionar(e2);
        System.out.println("após desdirecionar e2: Y->X = " + m.arestasEntre(y, x));
        // remoções
        System.out.println("\nremovendo e1 (valor = " + m.removerAresta(e1) + ")");
        System.out.println("removendo vértice X (valor = " + m.removerVertice(x) + ")");
        System.out.println("V=" + m.numVertices() + " E=" + m.numArestas());
        m.imprimirMatriz();

        System.out.println("arestas restantes = " + m.arestas());

    }
}