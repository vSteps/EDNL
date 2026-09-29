package RN;

import ABP.ArvoreBinariaPesquisa;
import ABP.No;

// Arvore Rubro-Negra: estende a ABP reaproveitando buscar, mostrar, percursos, altura etc. Insercao e remocao sao reescritas.
// Propriedades:
// 1. Todo no e vermelho ou preto.
// 2. A raiz e preta.
// 3. Toda folha (null) e considerada preta.
// 4. Filho de no vermelho e preto (nao ha dois vermelhos seguidos).
// 5. Todo caminho de um no ate suas folhas tem o mesmo numero de pretos.
 */
public class RubroNegra extends ArvoreBinariaPesquisa {

    // nao pode mudar a cor!
    private static final boolean VERMELHO = true; 
    private static final boolean PRETO = false;

    // No da Rubro-Negra: estende o No da ABP com cor e pai.
    protected class NoRN extends No {
        boolean cor;
        NoRN pai;

        public NoRN(int valor) {
            super(valor);
            this.cor = VERMELHO; // todo no novo nasce vermelho
            this.pai = null;
        }
    }

    @Override
    protected No criarNo(int valor) {
        return new NoRN(valor);
    }

    // Folha (null) conta como preta.
    private boolean corDe(No no) {
        if (no == null) {
            return PRETO;
        }
        return ((NoRN) no).cor;
    }

    // Aponta o filho de "pai" que era "antigo" para "novo".
    private void trocarFilho(NoRN pai, No antigo, No novo) {
        if (pai == null) {
            raiz = novo;
        } else if (pai.esquerda == antigo) {
            pai.esquerda = novo;
        } else {
            pai.direita = novo;
        }
    }

    private NoRN localizar(int valor) {
        NoRN atual = (NoRN) raiz;
        while (atual != null) {
            if (valor == atual.valor) {
                return atual;
            }
            if (valor < atual.valor) {
                atual = (NoRN) atual.esquerda;
            } else {
                atual = (NoRN) atual.direita;
            }
        }
        return null;
    }

    // Rotacoes: O(1)

    private void rotacaoEsquerda(NoRN x) {
        NoRN y = (NoRN) x.direita;

        x.direita = y.esquerda;
        if (y.esquerda != null) {
            ((NoRN) y.esquerda).pai = x;
        }

        y.pai = x.pai;
        trocarFilho(x.pai, x, y);

        y.esquerda = x;
        x.pai = y;
    }

    private void rotacaoDireita(NoRN x) {
        NoRN y = (NoRN) x.esquerda;

        x.esquerda = y.direita;
        if (y.direita != null) {
            ((NoRN) y.direita).pai = x;
        }

        y.pai = x.pai;
        trocarFilho(x.pai, x, y);

        y.direita = x;
        x.pai = y;
    }

    // Insercao: O(log n)

    @Override
    public void inserir(int valor) {
        NoRN pai = null;
        NoRN atual = (NoRN) raiz;
        while (atual != null) {
            if (valor == atual.valor) {
                return; // valor duplicado: nao insere de novo
            }
            pai = atual;
            if (valor < atual.valor) {
                atual = (NoRN) atual.esquerda;
            } else {
                atual = (NoRN) atual.direita;
            }
        }

        NoRN novo = (NoRN) criarNo(valor);
        novo.pai = pai;
        if (pai == null) {
            raiz = novo;
        } else if (valor < pai.valor) {
            pai.esquerda = novo;
        } else {
            pai.direita = novo;
        }

        corrigirInsercao(novo);
    }

    // Restaura a regra "vermelho nao tem filho vermelho" apos inserir.
    private void corrigirInsercao(NoRN z) {
        // Caso 1: pai preto = nada a fazer (o laco nem executa)
        // Caso 2 e 3: pai vermelho = precisa corrigir
        while (corDe(z.pai) == VERMELHO) {
            NoRN pai = z.pai;
            NoRN avo = pai.pai; // pai vermelho nunca eh a raiz, avo existe

            if (pai == avo.esquerda) { // pai eh filho esquerdo, tio a direita
                NoRN tio = (NoRN) avo.direita;
                if (corDe(tio) == VERMELHO) {
                    // Caso 2: pai e tio vermelhos = recolore pai, tio e avo, e repete o processo subindo (z = avo)
                    pai.cor = PRETO;
                    tio.cor = PRETO;
                    avo.cor = VERMELHO;
                    z = avo;
                } else {
                    // Caso 3: pai vermelho e tio preto = rotacoes, porem z eh filho direito
                    if (z == pai.direita) {
                        // Rotacao dupla esquerda-direita: 1a rotacao (esquerda) no pai, transformando o triangulo em linha
                        z = pai;
                        rotacaoEsquerda(z);
                        pai = z.pai;
                    }
                    // Simples a direita: recolore e rotaciona o avo
                    pai.cor = PRETO;
                    avo.cor = VERMELHO;
                    rotacaoDireita(avo);
                }
            } else { // pai eh filho direito, tio a esquerda
                NoRN tio = (NoRN) avo.esquerda;
                if (corDe(tio) == VERMELHO) {
                    // Caso 2: pai e tio vermelhos = recolore pai, tio e avo, e repete o processo subindo (z = avo)
                    pai.cor = PRETO;
                    tio.cor = PRETO;
                    avo.cor = VERMELHO;
                    z = avo;
                } else {
                    // Caso 3: pai vermelho e tio preto = rotacoes, porem z eh filho esquerdo
                    if (z == pai.esquerda) {
                        // Dupla direita-esquerda: 1a rotacao (direita) no pai, transformando o triangulo em linha
                        z = pai;
                        rotacaoDireita(z);
                        pai = z.pai;
                    }
                    // Simples a esquerda: recolore e rotaciona o avo
                    pai.cor = PRETO;
                    avo.cor = VERMELHO;
                    rotacaoEsquerda(avo);
                }
            }
        }
        ((NoRN) raiz).cor = PRETO; // a raiz sempre termina preta
    }

    //  Remocao: O(log n)

    // Remove o valor e, se saiu um no PRETO, corrige o duplo negro.
    @Override
    public void remover(int valor) {
        NoRN z = localizar(valor);
        if (z == null) {
            return; // valor nao existe
        }

        boolean corRemovida = z.cor; // cor do no que sai do lugar
        NoRN x;     // no que ocupa o lugar do removido (pode ser null)
        NoRN paiX;  // pai de x (necessario porque x pode ser null)

        if (z.esquerda == null) {
            // Sem filho esquerdo: a subarvore direita ocupa o lugar de z
            x = (NoRN) z.direita;
            paiX = z.pai;
            transplantar(z, x);
        } else if (z.direita == null) {
            // Sem filho direito: a subarvore esquerda ocupa o lugar de z
            x = (NoRN) z.esquerda;
            paiX = z.pai;
            transplantar(z, x);
        } else {
            // Dois filhos: o sucessor y (menor da subarvore direita) assume o lugar de z. O no que sai de verdade do seu lugar eh o y, entao a cor que importa eh a de y.
            NoRN y = minimo((NoRN) z.direita);
            corRemovida = y.cor;
            x = (NoRN) y.direita; // y nunca tem filho esquerdo

            if (y.pai == z) {
                // y ja esta colado em z: x continua sendo filho de y
                paiX = y;
            } else {
                // y esta mais abaixo: tira y do lugar dele (x sobe) e pendura a subarvore direita de z em y
                paiX = y.pai;
                transplantar(y, x);
                y.direita = z.direita;
                ((NoRN) y.direita).pai = y;
            }
            transplantar(z, y);
            y.esquerda = z.esquerda;
            ((NoRN) y.esquerda).pai = y;
            y.cor = z.cor; // y herda a cor de z (a posicao mantem a cor)
        }

        // Saiu um no VERMELHO: nada a fazer, a altura negra nao mudou.
        // Saiu um no PRETO: surge um duplo negro nesse caminho. (se x for vermelho, corrigirRemocao so o pinta de preto)
        if (corRemovida == PRETO) {
            corrigirRemocao(x, paiX);
        }
    }

    // Coloca a subarvore v no lugar da subarvore u (ajusta o pai de v e o ponteiro do pai de u, ou a raiz se u for a raiz).
    private void transplantar(NoRN u, NoRN v) {
        trocarFilho(u.pai, u, v);
        if (v != null) {
            v.pai = u.pai;
        }
    }

    // Menor no da subarvore: vai sempre para a esquerda.
    private NoRN minimo(NoRN no) {
        while (no.esquerda != null) {
            no = (NoRN) no.esquerda;
        }
        return no;
    }

    // x carrega o duplo negro. Resolve subindo ou girando.
    private void corrigirRemocao(NoRN x, NoRN paiX) {
        // Enquanto x for preto (com o duplo negro) e nao for a raiz
        while (x != raiz && corDe(x) == PRETO) {

            if (x == paiX.esquerda) { // x eh filho esquerdo, irmao a direita
                NoRN w = (NoRN) paiX.direita; // irmao (nunca eh null)
                // sobrinho de fora = w.direita, de dentro = w.esquerda

                if (corDe(w) == VERMELHO) {
                    // Caso 1: irmao vermelho -> gira paiX para a esquerda e o novo irmao vira preto (cai no caso 2, 3 ou 4)
                    w.cor = PRETO;
                    paiX.cor = VERMELHO;
                    rotacaoEsquerda(paiX);
                    w = (NoRN) paiX.direita;
                }

                if (corDe(w.esquerda) == PRETO
                        && corDe(w.direita) == PRETO) {
                    // Caso 2: irmao e sobrinhos pretos = pinta o irmao de vermelho e sobe o duplo negro (x = paiX)
                    //  2a) paiX preto: o laco repete la em cima
                    //  2b) paiX vermelho: o laco termina e paiX vira preto no final (resolvido)
                    w.cor = VERMELHO;
                    x = paiX;
                    paiX = x.pai;
                } else {
                    if (corDe(w.direita) == PRETO) {
                        // Caso 3: sobrinho de fora preto e de dentro vermelho = gira o irmao para a direita para o sobrinho de fora ficar vermelho (vira caso 4)
                        ((NoRN) w.esquerda).cor = PRETO;
                        w.cor = VERMELHO;
                        rotacaoDireita(w);
                        w = (NoRN) paiX.direita;
                    }
                    // Caso 4: sobrinho de fora vermelho -> irmao herda a cor de paiX, paiX e sobrinho de fora ficam pretos, gira paiX para a esquerda e o duplo negro some
                    w.cor = paiX.cor;
                    paiX.cor = PRETO;
                    ((NoRN) w.direita).cor = PRETO;
                    rotacaoEsquerda(paiX);
                    x = (NoRN) raiz; // encerra o laco
                }

            } else { // x eh filho direito, irmao a esquerda
                NoRN w = (NoRN) paiX.esquerda; // irmao (nunca eh null)
                // sobrinho de fora = w.esquerda, de dentro = w.direita

                if (corDe(w) == VERMELHO) {
                    // Caso 1: irmao vermelho -> gira paiX para a direita e o novo irmao vira preto (cai no caso 2, 3 ou 4)
                    w.cor = PRETO;
                    paiX.cor = VERMELHO;
                    rotacaoDireita(paiX);
                    w = (NoRN) paiX.esquerda;
                }

                if (corDe(w.direita) == PRETO
                        && corDe(w.esquerda) == PRETO) {
                    // Caso 2: irmao e sobrinhos pretos = pinta o irmao de vermelho e sobe o duplo negro (x = paiX)
                    //  2a) paiX preto: o laco repete la em cima
                    //  2b) paiX vermelho: o laco termina e paiX vira preto no final (resolvido)
                    w.cor = VERMELHO;
                    x = paiX;
                    paiX = x.pai;
                } else {
                    if (corDe(w.esquerda) == PRETO) {
                        // Caso 3: sobrinho de fora preto e de dentro vermelho = gira o irmao para a esquerda para o sobrinho de fora ficar vermelho (vira caso 4)
                        ((NoRN) w.direita).cor = PRETO;
                        w.cor = VERMELHO;
                        rotacaoEsquerda(w);
                        w = (NoRN) paiX.esquerda;
                    }
                    // Caso 4: sobrinho de fora vermelho -> irmao herda a cor de paiX, paiX e sobrinho de fora ficam pretos, gira paiX para a direita e o duplo negro some
                    w.cor = paiX.cor;
                    paiX.cor = PRETO;
                    ((NoRN) w.esquerda).cor = PRETO;
                    rotacaoDireita(paiX);
                    x = (NoRN) raiz; // encerra o laco
                }
            }
        }
        // x vermelho absorve o duplo negro ao virar preto
        if (x != null) {
            x.cor = PRETO;
        }
    }

    // Exibicao e validacao

    @Override
    protected String rotulo(No no) {
        NoRN noRN = (NoRN) no;

        if (noRN.cor == VERMELHO) {
            return no.valor + " [V]";
        } else {
            return no.valor + " [P]";
        }
    }

    // Confere as propriedades da arvore (util para testes).
    public boolean validar() {
        if (raiz == null) {
            return true;
        }
        if (corDe(raiz) != PRETO || ((NoRN) raiz).pai != null) {
            return false;
        }
        return alturaNegra((NoRN) raiz,
                Long.MIN_VALUE, Long.MAX_VALUE) != -1;
    }

    // Altura negra da subarvore, ou -1 se alguma regra for violada.
    private int alturaNegra(NoRN no, long min, long max) {
        if (no == null) {
            return 0;
        }
        if (no.valor <= min || no.valor >= max) {
            return -1; // ordem de ABP violada
        }
        if (no.cor == VERMELHO && (corDe(no.esquerda) == VERMELHO
                || corDe(no.direita) == VERMELHO)) {
            return -1; // dois vermelhos seguidos
        }
        if ((no.esquerda != null && ((NoRN) no.esquerda).pai != no)
                || (no.direita != null && ((NoRN) no.direita).pai != no)) {
            return -1; // ponteiro pai inconsistente
        }
        int esq = alturaNegra((NoRN) no.esquerda, min, no.valor);
        int dir = alturaNegra((NoRN) no.direita, no.valor, max);
        if (esq == -1 || dir == -1 || esq != dir) {
            return -1;
        }
        return esq + (no.cor == PRETO ? 1 : 0);
    }
}