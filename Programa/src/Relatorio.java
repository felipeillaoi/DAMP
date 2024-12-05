public class Relatorio {
    private static int contadorRelatorios = 0;
    private int id;

    public Relatorio() {
        this.id = ++contadorRelatorios;
    }

    public void gerarRelatorio(Sensor sensor) {
        System.out.println("=== Relatório do Sensor ===");
        System.out.println("ID do Relatório: " + this.id);
        System.out.println("Hora: " + sensor.getHora());
        System.out.println("Estado: " + sensor.getEstado());
        System.out.println("Nível de Turbidez: " + sensor.getNivelTurbidezNTU() + " NTU");
        System.out.println("===========================");
    }
}
