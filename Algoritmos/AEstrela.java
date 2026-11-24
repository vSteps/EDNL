import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// A* em grade (0 = livre, 1 = parede)
public class AEstrela {

    static final int RETO = 10;
    static final int DIAGONAL = 14;

    static class No {
        int linha, coluna;
        int g, h, f;
        No pai;

        No(int linha, int coluna) {
            this.linha = linha;
            this.coluna = coluna;
        }
    }

    // distancia de Manhattan ate o destino (H)
    static int heuristica(No a, No b) {
        return (Math.abs(a.linha - b.linha) + Math.abs(a.coluna - b.coluna)) * RETO;
    }

    // procura um no numa lista pela posicao
    static No buscar(List<No> lista, int linha, int coluna) {
        for (No no : lista) {
            if (no.linha == linha && no.coluna == coluna) {
                return no;
            }
        }
        return null;
    }

    static List<No> aEstrela(int[][] mapa, int[] inicio, int[] fim) {
        No origem = new No(inicio[0], inicio[1]);
        No destino = new No(fim[0], fim[1]);
        origem.h = heuristica(origem, destino);
        origem.f = origem.h;

        List<No> aberta = new ArrayList<>();
        List<No> fechada = new ArrayList<>();
        aberta.add(origem);

        while (!aberta.isEmpty()) {
            // pega o quadrado com menor F na lista aberta
            No atual = aberta.get(0);
            for (No no : aberta) {
                if (no.f < atual.f) {
                    atual = no;
                }
            }

            aberta.remove(atual);
            fechada.add(atual);

            // chegou no destino: monta o caminho de tras pra frente
            if (atual.linha == destino.linha && atual.coluna == destino.coluna) {
                List<No> caminho = new ArrayList<>();
                No n = atual;
                while (n != null) {
                    caminho.add(n);
                    n = n.pai;
                }
                Collections.reverse(caminho);
                return caminho;
            }

            // olha os 8 vizinhos
            for (int dl = -1; dl <= 1; dl++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dl == 0 && dc == 0) {
                        continue;
                    }

                    int l = atual.linha + dl;
                    int c = atual.coluna + dc;

                    // fora do mapa ou parede: ignora
                    if (l < 0 || l >= mapa.length || c < 0 || c >= mapa[0].length) {
                        continue;
                    }
                    if (mapa[l][c] == 1) {
                        continue;
                    }
                    // ja esta na lista fechada: ignora
                    if (buscar(fechada, l, c) != null) {
                        continue;
                    }

                    int custo = (dl != 0 && dc != 0) ? DIAGONAL : RETO;
                    int novoG = atual.g + custo;

                    No viz = buscar(aberta, l, c);
                    if (viz == null) {
                        // nao esta na aberta: adiciona e grava os custos
                        viz = new No(l, c);
                        viz.pai = atual;
                        viz.g = novoG;
                        viz.h = heuristica(viz, destino);
                        viz.f = viz.g + viz.h;
                        aberta.add(viz);
                    } else if (novoG < viz.g) {
                        // ja estava na aberta, mas esse caminho e melhor
                        viz.pai = atual;
                        viz.g = novoG;
                        viz.f = viz.g + viz.h;
                    }
                }
            }
        }

        // lista aberta vazia: nao existe caminho
        return null;
    }

    static void mostrar(int[][] mapa, List<No> caminho, int[] inicio, int[] fim) {
        for (int l = 0; l < mapa.length; l++) {
            StringBuilder linha = new StringBuilder();
            for (int c = 0; c < mapa[0].length; c++) {
                if (l == inicio[0] && c == inicio[1]) {
                    linha.append("S ");
                } else if (l == fim[0] && c == fim[1]) {
                    linha.append("E ");
                } else if (caminho != null && buscar(caminho, l, c) != null) {
                    linha.append("* ");
                } else if (mapa[l][c] == 1) {
                    linha.append("# ");
                } else {
                    linha.append(". ");
                }
            }
            System.out.println(linha);
        }
    }

    public static void main(String[] args) {
        int[][] mapa = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 1, 0}
        };

        int[] inicio = {2, 1};
        int[] fim = {3, 7};

        List<No> caminho = aEstrela(mapa, inicio, fim);

        if (caminho != null) {
            System.out.println("Caminho encontrado:");
            for (No no : caminho) {
                System.out.println("(" + no.linha + ", " + no.coluna + ")");
            }
            System.out.println("Passos: " + (caminho.size() - 1));
            System.out.println("Custo total: " + caminho.get(caminho.size() - 1).g);
        } else {
            System.out.println("Nao existe caminho");
        }

        System.out.println();
        mostrar(mapa, caminho, inicio, fim);
    }
}