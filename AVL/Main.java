import java.util.InputMismatchException;
import java.util.Scanner;

import AVL.AVL;

/**
 * Classe de teste da Arvore AVL, com menu de console que permite:
 *   - inclusao de chaves;
 *   - remocao de chaves;
 *   - busca de chaves;
 *   - exibicao da arvore (com o fator de balanceamento de cada no).
 */
public class Main {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        AVL arvore = new AVL();
        int opcao;

        do {
            System.out.println();
            System.out.println("========== ARVORE AVL ==========");
            System.out.println("1 - Inserir chave");
            System.out.println("2 - Remover chave");
            System.out.println("3 - Buscar chave");
            System.out.println("4 - Mostrar arvore");
            System.out.println("5 - Altura da arvore");
            System.out.println("6 - Quantidade de nos");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opcao: ");

            opcao = lerInteiro(teclado);

            switch (opcao) {
                case 1:
                    System.out.print("Digite a chave a inserir: ");
                    arvore.inserir(lerInteiro(teclado));
                    break;

                case 2:
                    System.out.print("Digite a chave a remover: ");
                    arvore.remover(lerInteiro(teclado));
                    break;

                case 3:
                    System.out.print("Digite a chave a buscar: ");
                    int chaveBusca = lerInteiro(teclado);
                    if (arvore.buscar(chaveBusca)) {
                        System.out.println(
                            "Chave " + chaveBusca + " ENCONTRADA na arvore.");
                    } else {
                        System.out.println(
                            "Chave " + chaveBusca + " NAO encontrada.");
                    }
                    break;

                case 4:
                    System.out.println();
                    System.out.println("Arvore (formato: chave [FB]):");
                    arvore.mostrar();
                    break;

                case 5:
                    System.out.println(
                        "Altura da arvore: " + arvore.altura());
                    break;

                case 6:
                    System.out.println(
                        "Quantidade de nos: " + arvore.tamanho());
                    break;

                case 0:
                    System.out.println("Encerrando...");
                    break;

                default:
                    System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);

        teclado.close();
    }

    /**
     * Le um inteiro do teclado, pedindo novamente em caso de entrada
     * invalida.
     */
    private static int lerInteiro(Scanner teclado) {
        while (true) {
            try {
                return teclado.nextInt();
            } catch (InputMismatchException e) {
                System.out.print(
                    "Valor invalido, digite um numero inteiro: ");
                teclado.next();
            }
        }
    }
}