import java.io.*;

@Deprecated
public class PrincipalLab {
    public static void main(String[] args) throws Exception {
        BufferedReader in = null;
        String arquivo = Teclado.getUmString();

        try {
            in = new BufferedReader(new FileReader(arquivo));

            Resolvedor labirinto = new Resolvedor(in); //chama a classe resovedora de labirinto

            //printa tudo o que foi passado (para testes)
            System.out.println("Colunas: " + labirinto.getColunas());
            System.out.println("N° de caracteres por linha: " + labirinto.getCarac());
            System.out.println(labirinto.getMatriz());
            //testes
            labirinto.resolver();
            System.out.println(labirinto.getMatriz());
            System.out.println(labirinto.getCaminho());

            in.close(); //fecha o arquivo
        } catch (IOException erro) {System.err.println(erro);} //Caso o arquivo não tenha sido encontrado
    }
}
