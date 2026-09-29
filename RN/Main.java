import RN.RubroNegra;

import java.util.Scanner;

public class Main {

    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        RubroNegra arvore = new RubroNegra();
        int opcao;

        do {
            menu();
            opcao = lerInteiro("Opcao: ");
            System.out.println();

            switch (opcao) {
                case 1:
                    int novo = lerInteiro("Valor a inserir: ");
                    if (arvore.buscar(novo)) {
                        System.out.println("Valor " + novo + " ja existe.");
                    } else {
                        arvore.inserir(novo);
                        System.out.println("Valor " + novo + " inserido.");
                    }
                    break;
                case 2:
                    int alvo = lerInteiro("Valor a remover: ");
                    if (arvore.buscar(alvo)) {
                        arvore.remover(alvo);
                        System.out.println("Valor " + alvo + " removido.");
                    } else {
                        System.out.println("Valor " + alvo
                                + " nao encontrado.");
                    }
                    break;
                case 3:
                    int busca = lerInteiro("Valor a buscar: ");
                    if (arvore.buscar(busca)) {
                        System.out.println("Valor " + busca
                                + " encontrado.");
                    } else {
                        System.out.println("Valor " + busca
                                + " nao encontrado.");
                    }
                    break;
                case 4:
                    System.out.println("(V = vermelho, P = preto)");
                    arvore.mostrar();
                    break;
                case 5:
                    arvore.emOrdem();
                    System.out.println("Total de nos: " + arvore.tamanho());
                    System.out.println("Altura: " + arvore.altura());
                    System.out.println("Propriedades rubro-negras validas? "
                            + (arvore.validar() ? "SIM" : "NAO"));
                    break;
                case 0:
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
            System.out.println();
        } while (opcao != 0);
    }

    private static void menu() {
        System.out.println("===== ARVORE RUBRO-NEGRA =====");
        System.out.println("1 - Inserir");
        System.out.println("2 - Remover");
        System.out.println("3 - Buscar");
        System.out.println("4 - Mostrar arvore");
        System.out.println("5 - Em ordem / altura / validar");
        System.out.println("0 - Sair");
    }

    // Le um inteiro, repetindo a pergunta ate a entrada ser valida.
    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = in.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Digite um inteiro.");
            }
        }
    }
}