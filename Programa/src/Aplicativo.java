import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.time.LocalTime;
import java.util.Scanner;

public class Aplicativo {
    private Login login;
    private Sensor sensor;
    private SerialPort porta;

    public Aplicativo() {
        this.login = new Login();
        this.sensor = new Sensor(5);
        login.registrar("admin", "1234");
    }

    public void iniciar() {
        System.out.println("Iniciando o aplicativo...");
        Scanner ler = new Scanner(System.in);

        System.out.print("Digite o login: ou pressione zero para um novo registro:\n");
        String loginInput = ler.next();
        if (loginInput.equals("0")) {
            System.out.println("Digite um novo Login");
            String registro = ler.next();
            System.out.println("Digite a nova senha");
            String novaSenha = ler.next();
            login.registrar(registro,novaSenha);

            iniciar();
        }
        System.out.print("Digite a senha: ");
        String senhaInput = ler.next();

        boolean loginValido = login.validar(loginInput, senhaInput);

        if (loginValido) {
            configurarSerial();

            telaPrincipal();
        } else {
            System.out.println("Falha no login. Encerrando o aplicativo.");
        }

        ler.close();
    }

    private void telaPrincipal() {
        Relatorio relatorio = new Relatorio();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nTELA INICIAL\n");
            System.out.println("Funcionalidades:");
            System.out.println("1 - Gerar Relatório");
            System.out.println("2 - Realizar Leitura");
            System.out.println("3 - Sair");
            System.out.println(" ");
            System.out.println("9 - Configurar Sensor");
            System.out.print("Escolha uma opção: ");

            int op = scanner.nextInt();

            switch (op) {
                case 1:
                    relatorio.gerarRelatorio(sensor);
                    break;
                case 9:
                    configurarSensor();
                    break;
                case 2:

                    realizarLeitura();
                    break;
                case 3:
                    System.out.println("Saindo do programa...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }


    private void configurarSerial() {

        SerialPort[] portasDisponiveis = SerialPort.getCommPorts();
        if (portasDisponiveis.length == 0) {
            System.out.println("Nenhuma porta serial encontrada!");
            return;
        }

        System.out.println("Portas seriais disponíveis:");
        for (int i = 0; i < portasDisponiveis.length; i++) {
            System.out.println((i + 1) + ": " + portasDisponiveis[i].getSystemPortName());
        }

        porta = portasDisponiveis[0];
        System.out.println("Conectando à porta: " + porta.getSystemPortName());

        porta.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        porta.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 1000);

        if (porta.openPort()) {
            System.out.println("Porta serial conectada com sucesso!");
            telaPrincipal();
        } else {
            System.out.println("Falha ao abrir a porta serial!");
            porta = null;
            telaPrincipal();
        }
    }

    private void atualizarSensor(String linha) {
        System.out.println(linha);

        if (linha.startsWith("Valor lido: ")) {
            String valor = linha.replace("Valor lido:", "").trim();
            double valorNTU = Double.parseDouble(valor);
            sensor.setNivelTurbidezNTU(valorNTU);
            String situacao = "SEM MEDIÇÃO";
            if (valorNTU >= 700) {
                situacao = "Água Limpa";
            } else if (valorNTU < 700 && valorNTU >= 600) {
                situacao = "Água Turva";
            } else if (valorNTU < 600 && valorNTU >= 500) {
                situacao = "Água suja";
            } else {
                situacao = "Água Muito Suja";
            }
            sensor.setEstado(situacao);
            sensor.setHora(LocalTime.now());
        }
    }

    public void configurarSensor() {
        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports.length == 0) {
            System.out.println("Nenhuma porta disponível.");
            return;
        }

        System.out.println("Portas disponíveis:");
        for (int i = 0; i < ports.length; i++) {
            System.out.println((i + 1) + ": " + ports[i].getSystemPortName());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Escolha a porta (número): ");
        int portIndex = scanner.nextInt() - 1;

        if (portIndex < 0 || portIndex >= ports.length) {
            System.out.println("Opção inválida.");
            return;
        }

        SerialPort porta = ports[portIndex];

        porta.setBaudRate(9600);
        porta.setNumDataBits(8);
        porta.setNumStopBits(SerialPort.ONE_STOP_BIT);
        porta.setParity(SerialPort.NO_PARITY);

        if (!porta.openPort()) {
            System.out.println("Erro ao abrir a porta.");
            return;
        }

        System.out.println("Porta aberta com sucesso!");

        telaPrincipal();
    }

    private void realizarLeitura() {
        try (Scanner leitura = new Scanner(porta.getInputStream())) {
            String linha;
            System.out.println("Aguardando dados do Arduino... (Digite 'sair' para voltar ao menu)");

            linha = leitura.nextLine();
            atualizarSensor(linha);
            sensor.enviarAlertas();

        } catch (Exception e) {
            System.out.println("Erro ao realizar leitura: " + e.getMessage());
        }
    }

}
