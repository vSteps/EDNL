package ABP;
public class ArvoreBinariaPesquisa {

    protected No raiz;

    public ArvoreBinariaPesquisa() {
        this.raiz = null;
    }

    public void inserir(int valor) {
        raiz = inserirRecursivo(raiz, valor);
    }

    protected No inserirRecursivo(No atual, int valor) {
        if (atual == null) {
            return criarNo(valor);
        }

        if (valor < atual.valor) {
            atual.esquerda = inserirRecursivo(atual.esquerda, valor);
        } else if (valor > atual.valor) {
            atual.direita = inserirRecursivo(atual.direita, valor);
        }

        return atual;
    }

    protected No criarNo(int valor) {
        return new No(valor);
    }


    public void remover(int valor) {
        raiz = removerRecursivo(raiz, valor);
    }

    protected No removerRecursivo(No atual, int valor) {
        if (atual == null) {
            return null;
        }

        if (valor < atual.valor) {
            atual.esquerda = removerRecursivo(atual.esquerda, valor);
        } else if (valor > atual.valor) {
            atual.direita = removerRecursivo(atual.direita, valor);
        } else {
            // No encontrado.
            if (atual.esquerda == null && atual.direita == null) {
                return null; // No folha.
            } else if (atual.esquerda == null) {
                return atual.direita; // Um filho (direita).
            } else if (atual.direita == null) {
                return atual.esquerda; // Um filho (esquerda).
            } else {
                // Dois filhos: substitui pelo sucessor (menor da
                // subarvore direita) e remove o sucessor de la.
                int valorSucessor = encontrarMinimo(atual.direita);
                atual.valor = valorSucessor;
                atual.direita = removerRecursivo(
                    atual.direita, valorSucessor);
            }
        }

        return atual;
    }
    public boolean buscar(int valor) {
        return buscarRecursivo(raiz, valor);
    }

    protected boolean buscarRecursivo(No atual, int valor) {
        if (atual == null) {
            return false;
        }
        if (valor == atual.valor) {
            return true;
        }
        if (valor < atual.valor) {
            return buscarRecursivo(atual.esquerda, valor);
        }
        return buscarRecursivo(atual.direita, valor);
    }

    public int encontrarMinimo() {
        if (raiz == null) {
            throw new IllegalStateException("A arvore esta vazia.");
        }
        return encontrarMinimo(raiz);
    }

    protected int encontrarMinimo(No atual) {
        if (atual.esquerda == null) {
            return atual.valor;
        }
        return encontrarMinimo(atual.esquerda);
    }

    public int encontrarMaximo() {
        if (raiz == null) {
            throw new IllegalStateException("A arvore esta vazia.");
        }
        No atual = raiz;
        while (atual.direita != null) {
            atual = atual.direita;
        }
        return atual.valor;
    }

    public int altura() {
        return alturaRecursiva(raiz);
    }

    protected int alturaRecursiva(No atual) {
        if (atual == null) {
            return 0; // Altura de uma arvore vazia e 0.
        }
        int alturaEsquerda = alturaRecursiva(atual.esquerda);
        int alturaDireita = alturaRecursiva(atual.direita);
        return 1 + Math.max(alturaEsquerda, alturaDireita);
    }

    public int tamanho() {
        return tamanhoRecursivo(raiz);
    }

    protected int tamanhoRecursivo(No atual) {
        if (atual == null) {
            return 0;
        }
        int qtdEsq = tamanhoRecursivo(atual.esquerda);
        int qtdDir = tamanhoRecursivo(atual.direita);
        return 1 + qtdEsq + qtdDir;
    }

    public void emOrdem() {
        System.out.print("Em ordem: ");
        emOrdemRecursivo(raiz);
        System.out.println();
    }

    protected void emOrdemRecursivo(No atual) {
        if (atual != null) {
            emOrdemRecursivo(atual.esquerda);
            System.out.print(atual.valor + " ");
            emOrdemRecursivo(atual.direita);
        }
    }

    public void preOrdem() {
        System.out.print("Pre-ordem: ");
        preOrdemRecursivo(raiz);
        System.out.println();
    }

    protected void preOrdemRecursivo(No atual) {
        if (atual != null) {
            System.out.print(atual.valor + " ");
            preOrdemRecursivo(atual.esquerda);
            preOrdemRecursivo(atual.direita);
        }
    }

    public void posOrdem() {
        System.out.print("Pos-ordem: ");
        posOrdemRecursivo(raiz);
        System.out.println();
    }

    protected void posOrdemRecursivo(No atual) {
        if (atual != null) {
            posOrdemRecursivo(atual.esquerda);
            posOrdemRecursivo(atual.direita);
            System.out.print(atual.valor + " ");
        }
    }

    public boolean estaVazia() {
        return raiz == null;
    }

   public void mostrar() {
        System.out.println("Arvore:");
        if (raiz == null) {
            System.out.println("(arvore vazia)");
        } else {
            mostrarRecursivo(raiz, "", false, true);
            System.out.println("-----------------------------------");
        }
    }

    protected void mostrarRecursivo(
            No atual, String prefixo, boolean isEsquerda, boolean isRaiz) {
        if (atual == null) {
            return;
        }
        String ramoDir = prefixo + (isEsquerda ? "|   " : "    ");
        mostrarRecursivo(atual.direita, ramoDir, false, false);

        String conector = isRaiz ? "" : (isEsquerda ? "\\-- " : "/-- ");
        System.out.println(prefixo + conector + rotulo(atual));

        String ramoEsq = prefixo + (isEsquerda ? "    " : "|   ");
        mostrarRecursivo(atual.esquerda, ramoEsq, true, false);
    }

    protected String rotulo(No no) {
        return String.valueOf(no.valor);
    }
}