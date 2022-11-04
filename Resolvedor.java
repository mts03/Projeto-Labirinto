import java.io.*;

public class Resolvedor {
    /* ATRIBUTOS */
    public BufferedReader in = null;
    private Integer colunas;
    private Integer nCarac;
    private Character matriz[][]; //matriz contendo os caracteres do mapa ("#", " ", "E", "S", "*")
    private Coordenada atual;

    private Pilha<Coordenada> caminho; //caminho certo do labirinto
    private Pilha<Coordenada> inverso;
    private Pilha<Fila<Coordenada>> possibilidades; //todas as possibilidades de caminhos (erradas e certas)

    /* Construtores */
    @Deprecated
    public Resolvedor(BufferedReader provIn) throws NumberFormatException, IOException, Exception {
        if (provIn == null)
            throw new Exception("Não se pode passar um mapa com nada nele!");
        this.in = provIn;

        this.colunas = new Integer(this.in.readLine());
        this.nCarac = new Integer(this.in.readLine());

        this.matriz = new Character[colunas][nCarac];
        caminho = new Pilha<Coordenada>(this.colunas * this.nCarac);
        inverso = new Pilha<Coordenada>(this.colunas * this.nCarac);
        possibilidades = new Pilha<Fila<Coordenada>>(this.colunas * this.nCarac);

        this.transfereMapa();
    }

    /* GETTERS */
    public Integer getColunas() {
        return this.colunas;
    }
    public Integer getCarac() {
        return this.nCarac;
    }
    public String getMatriz() { //retorna uma string mostrando o mapa do arquivo texto
        String mapa = "";
        for (int i = 0; i < this.colunas; i++) {
            for (int z = 0; z < this.nCarac; z++) {
                mapa += this.matriz[i][z];
            }
            mapa += "\n";
        }
        return mapa;
    }

    //getters para testes!!!!!!!!!!!
    public String getCoordenadas() {
        return this.caminho.toString();
    }
    public String getEntrada() {
        return this.atual.toString();
    }
    public String getCaminho() throws Exception {
        String ret = "Caminho: ";
        Pilha<Coordenada> inverso2 = inverso;

        while (!inverso2.isVazia()) {
            ret += inverso2.recupereUmItem().toString() + ", ";
            inverso2.removaUmItem();
        }

        return ret;
    }
    

    /* Funções */
    private void transfereMapa() throws IOException, Exception { //pega os caracteres do arquivo texto e coloca na matriz
        for (int i = 0; i < this.colunas; i++) { //Isso eh o valor do eixo y
            char[] linha = (this.in.readLine()).toCharArray(); //tranforma a linha em um vetor de chars

            for (int z = 0; z < this.nCarac; z++) { //isso eh o valor do eixo x
                this.matriz[i][z] = linha[z];
            }
        }
        this.acharInconformidades();
    }

    
    private void acharInconformidades() throws Exception {
        int qtasEntradas = 0;
        int qtasSaidas = 0;
        boolean semParedes = false;

        for (int y = 0; y < this.colunas; y++) {
            for (int x = 0; x < this.nCarac; x++) {
                if ((y == 0 || y == this.colunas - 1) || (x == 0 || x == this.nCarac - 1)) /* delimita a borda do mapa */ {
                    if (this.matriz[y][x] == 'E') { //Acha a entrada
                        qtasEntradas++;
                        this.atual = new Coordenada(x, y); // guarda a coordenada da entrada
                        break;
                    }
                    else if (this.matriz[y][x] == 'S')
                        qtasSaidas++;
                    else if (this.matriz[y][x] == ' ')
                        semParedes = true;
                }
            }
        }

        if (qtasEntradas > 1)
            throw new Exception("Mais de uma entrada! Isso é proibido!");
        if (qtasSaidas > 1)
            throw new Exception("Mais de uma saida! Isso é proibido!");
        if (qtasEntradas == 0)
            throw new Exception("Não há entradas! Isso é proibido!");
        if (qtasSaidas == 0)
            throw new Exception("Não há saidas! Isso é proibido!");
        if (semParedes)
            throw new Exception("Não há parede totalmente em volta! Isso é proibido!");
    }

    public void resolver() throws Exception { //tenta resolver o labirinto
        while (this.atual.getCoordenadas()[0] < this.nCarac - 1 && this.atual.getCoordenadas()[1] < this.colunas -1 && !parou) { // repete o processo até ele chegar na borda
            this.verificarCantos();
        }

        this.preencherInverso();
    }

    private boolean parou = false;
    private void verificarCantos() throws Exception { //devolve um conjunto de coordenadas que possam ser do proximo movimento
        Fila<Coordenada> fila = new Fila<Coordenada>(3);
        int x = this.atual.getCoordenadas()[0]; //pega o x e o y da posição atual
        int y = this.atual.getCoordenadas()[1];

        if (y + 1 < this.colunas && (this.matriz[y + 1][x] == ' ' || this.matriz[y + 1][x] == 'S')) //vê se tem um espaço em branco ou a Saída. Caso tenha, ele guarda na fila
            fila.guardeUmItem(new Coordenada(x, y + 1));
        if (x + 1 < this.nCarac && (this.matriz[y][x + 1] == ' ' || this.matriz[y][x + 1] == 'S')) //mesma coisa
            fila.guardeUmItem(new Coordenada(x + 1, y));
        if (x - 1 >= 0 && (this.matriz[y][x - 1] == ' ' || this.matriz[y][x - 1] == 'S')) //mesma coisa
            fila.guardeUmItem(new Coordenada(x - 1, y));
        if (y - 1 >= 0 && (this.matriz[y - 1][x] == ' ' || this.matriz[y - 1][x] == 'S')) //mesma coisa
            fila.guardeUmItem(new Coordenada(x, y - 1));

        if (!fila.isVazia()) { //se a fila está vazia, não há o que fazer
            this.atual = (Coordenada) fila.recupereUmItem(); //coloca uma coordenada na posição atual
            this.caminho.guardeUmItem(this.atual);
            fila.removaUmItem(); //retira a posição que foi solicitada

            possibilidades.guardeUmItem(new Fila<Coordenada>(fila));

            if (this.matriz[this.atual.getCoordenadas()[1]][this.atual.getCoordenadas()[0]] == ' ')
                this.matriz[this.atual.getCoordenadas()[1]][this.atual.getCoordenadas()[0]] = '*'; //coloca um * no mapa onde a posição atual estiver
            else if (this.matriz[this.atual.getCoordenadas()[1]][this.atual.getCoordenadas()[0]] == 'S')
                parou = true;
        } else {
            possibilidades.guardeUmItem(new Fila<Coordenada>(fila));

            while (fila.isVazia()) {
                this.matriz[this.atual.getCoordenadas()[1]][this.atual.getCoordenadas()[0]] = ' '; //coloca um " " no mapa onde a posição atual estiver
                this.atual = caminho.recupereUmItem(); //Anda para tras
                caminho.removaUmItem(); 

                fila = possibilidades.recupereUmItem();
                possibilidades.removaUmItem();
            }

            if (!fila.isVazia()) {
                this.atual = (Coordenada) fila.recupereUmItem(); //coloca uma coordenada na posição atual
                this.caminho.guardeUmItem(this.atual);
                fila.removaUmItem(); //retira a posição que foi solicitada

                possibilidades.guardeUmItem(new Fila<Coordenada>(fila));

                this.matriz[this.atual.getCoordenadas()[1]][this.atual.getCoordenadas()[0]] = '*'; //coloca um * no mapa onde a posição atual estiver
            }
        }
    }

    private void preencherInverso() throws Exception {
        Pilha<Coordenada> caminho2 = caminho;

        while (!caminho2.isVazia()) {
            inverso.guardeUmItem(caminho2.recupereUmItem());
            caminho2.removaUmItem();
        }
    }
}
