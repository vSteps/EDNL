package AVL;

import ABP.ArvoreBinariaPesquisa;
import ABP.No;
public class AVL extends ArvoreBinariaPesquisa {

    // No da AVL: estende o No da ABP acrescentando a altura.
    protected class NoAVL extends No {
        int altura; // folha tem altura 0

        public NoAVL(int valor) {
            super(valor);
            this.altura = 0;
        }
    }

    @Override
    protected No criarNo(int valor) {
        return new NoAVL(valor);
    }


    private int altura(NoAVL no) {
        return (no == null) ? -1 : no.altura;
    }

    private void atualizarAltura(NoAVL no) {
        int altEsq = altura((NoAVL) no.esquerda);
        int altDir = altura((NoAVL) no.direita);
        no.altura = 1 + Math.max(altEsq, altDir);
    }

    private int fatorBalanceamento(NoAVL no) {
        if (no == null) {
            return 0;
        }
        return altura((NoAVL) no.esquerda) - altura((NoAVL) no.direita);
    }


    private No rotacaoDireita(NoAVL no) {
        NoAVL novaRaiz = (NoAVL) no.esquerda;
        No subarvoreB = novaRaiz.direita;

        novaRaiz.direita = no;
        no.esquerda = subarvoreB;

        atualizarAltura(no);       // atualiza primeiro quem desceu
        atualizarAltura(novaRaiz); // depois quem subiu

        return novaRaiz;
    }

    private No rotacaoEsquerda(NoAVL no) {
        NoAVL novaRaiz = (NoAVL) no.direita;
        No subarvoreB = novaRaiz.esquerda;

        novaRaiz.esquerda = no;
        no.direita = subarvoreB;

        atualizarAltura(no);
        atualizarAltura(novaRaiz);

        return novaRaiz;
    }

    private No rotacaoEsquerdaDireita(NoAVL no) {
        no.esquerda = rotacaoEsquerda((NoAVL) no.esquerda);
        return rotacaoDireita(no);
    }

    private No rotacaoDireitaEsquerda(NoAVL no) {
        no.direita = rotacaoDireita((NoAVL) no.direita);
        return rotacaoEsquerda(no);
    }


    private No balancear(NoAVL no) {
        atualizarAltura(no);
        int fb = fatorBalanceamento(no);

        if (fb > 1) { // pesado a esquerda
            if (fatorBalanceamento((NoAVL) no.esquerda) < 0) {
                return rotacaoEsquerdaDireita(no); // caso esquerda-direita
            }
            return rotacaoDireita(no); // caso esquerda-esquerda
        }

        if (fb < -1) { // pesado a direita
            if (fatorBalanceamento((NoAVL) no.direita) > 0) {
                return rotacaoDireitaEsquerda(no); // caso direita-esquerda
            }
            return rotacaoEsquerda(no); // caso direita-direita
        }

        return no; // ja balanceado
    }

    @Override
    protected No inserirRecursivo(No atual, int valor) {
        if (atual == null) {
            return criarNo(valor);
        }

        if (valor < atual.valor) {
            atual.esquerda = inserirRecursivo(atual.esquerda, valor);
        } else if (valor > atual.valor) {
            atual.direita = inserirRecursivo(atual.direita, valor);
        } else {
            return atual; // valor duplicado: nao insere de novo
        }

        return balancear((NoAVL) atual);
    }

    @Override
    protected No removerRecursivo(No atual, int valor) {
        if (atual == null) {
            return null;
        }

        if (valor < atual.valor) {
            atual.esquerda = removerRecursivo(atual.esquerda, valor);
        } else if (valor > atual.valor) {
            atual.direita = removerRecursivo(atual.direita, valor);
        } else {
            if (atual.esquerda == null && atual.direita == null) {
                return null;
            } else if (atual.esquerda == null) {
                return atual.direita;
            } else if (atual.direita == null) {
                return atual.esquerda;
            } else {
                int valorSucessor = encontrarMinimo(atual.direita);
                atual.valor = valorSucessor;
                atual.direita = removerRecursivo(
                    atual.direita, valorSucessor);
            }
        }

        return balancear((NoAVL) atual);
    }

    @Override
    protected String rotulo(No no) {
        NoAVL noAvl = (NoAVL) no;
        return no.valor + " [" + fatorBalanceamento(noAvl) + "]";
    }
}