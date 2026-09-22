public class rubronegra {
    private class No {
        int valor;
        No esquerda;
        No direita;
        boolean cor; // true = rubro, false = negro

        No(int valor) {
            this.valor = valor;
            this.cor = true; // novo nó é sempre rubro
        }
    }

    private No raiz;

}```