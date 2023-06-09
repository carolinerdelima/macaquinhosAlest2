import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map.Entry;
import java.util.AbstractMap;


class Macaco {
    int id;
    int par;
    int impar;
    List<Integer> cocos;

    public Macaco(int id, int par, int impar, List<Integer> cocos) {
        this.id = id;
        this.par = par;
        this.impar = impar;
        this.cocos = cocos;
    }
}

public class JogoMacaquinhos {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso correto via comando: java NomedoPrograma <arquivo_de_entrada.txt>");
            System.exit(1);
        }

        String arquivo = args[0];

        long inicioLeitura = System.currentTimeMillis();
        Map.Entry<Integer, List<Macaco>> entrada = lerArquivo(arquivo);
        int numRodadas = entrada.getKey();
        List<Macaco> macacos = entrada.getValue();
        long fimLeitura = System.currentTimeMillis();

        long inicioExecucao = System.currentTimeMillis();
        simularJogo(macacos, numRodadas);
        long fimExecucao = System.currentTimeMillis();

        System.out.println("Tempo de leitura do arquivo: " + (fimLeitura - inicioLeitura) + " ms");
        System.out.println("Tempo de execução do programa: " + (fimExecucao - inicioExecucao) + " ms");
    }

    private static int lerRodadas(String arquivo) {
        int numRodadas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            numRodadas = Integer.parseInt(br.readLine().split("\\s+")[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return numRodadas;
    }

    private static Map.Entry<Integer, List<Macaco>> lerArquivo(String arquivo) {
        List<Macaco> macacos = new ArrayList<>();
        int numRodadas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String primeiraLinha = br.readLine();
            numRodadas = Integer.parseInt(primeiraLinha.split("\\s+")[1]);

            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(":");
                int id = Integer.parseInt(partes[0].split("\\s+")[1]);
                int par = Integer.parseInt(partes[0].split("\\s+")[4]);
                int impar = Integer.parseInt(partes[0].split("\\s+")[7]);
                int numCocos = Integer.parseInt(partes[1].trim().split("\\s+")[0]);

                List<Integer> cocos = Stream.of(partes[2].trim().split("\\s+"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());

                macacos.add(new Macaco(id, par, impar, cocos));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new AbstractMap.SimpleEntry<>(numRodadas, macacos);
    }


    private static void simularJogo(List<Macaco> macacos, int numRodadas) {
        Map<Integer, Macaco> mapaMacaquinhos = new HashMap<>();
        for (Macaco macaco : macacos) {
            mapaMacaquinhos.put(macaco.id, macaco);
        }

        for (int i = 0; i < numRodadas; i++) {
            for (Macaco macaco : macacos) {
                List<Integer> cocos = macaco.cocos;
                for (int j = 0; j < cocos.size(); j++) {
                    int coco = cocos.get(j);
                    int destinatario = (coco % 2 == 0) ? macaco.par : macaco.impar;
                    mapaMacaquinhos.get(destinatario).cocos.add(coco);
                }
                cocos.clear();
            }
        }

        Macaco vencedor = Collections.max(macacos, Comparator.comparingInt(macaco -> macaco.cocos.size()));
        System.out.println("O macaquinho vencedor é o: " + vencedor.id);
    }


    private static Macaco encontrarVencedor(List<Macaco> macacos) {
        Macaco vencedor = macacos.get(0);
        for (Macaco macaco : macacos) {
            if (macaco.cocos.size() > vencedor.cocos.size()) {
                vencedor = macaco;
            }
        }
        return vencedor;
    }

}