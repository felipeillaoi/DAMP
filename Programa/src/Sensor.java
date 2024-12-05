import java.time.LocalTime;

public class Sensor {
    private LocalTime hora;
    private String estado;
    private double nivelTurbidezNTU;
    private double nivelMaximoTurbidez;

    public Sensor(int nivelMaximoTurbidez) {
        this.nivelMaximoTurbidez = nivelMaximoTurbidez;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado(){
        return estado;
    }

    public double getNivelTurbidezNTU() {
        return nivelTurbidezNTU;
    }

    public double setNivelTurbidezNTU(double nivelTurbidezNTU) {
        this.nivelTurbidezNTU = nivelTurbidezNTU;
        return nivelTurbidezNTU;
    }

    public void enviarAlertas() {
        if (this.nivelTurbidezNTU < 700) {
            System.out.println("Alerta: Nível de turbidez excedido! Valor atual: " + nivelTurbidezNTU + " NTU");
        } else {
            System.out.println("Nível de turbidez dentro do permitido: " + nivelTurbidezNTU + " NTU");
        }
    }

}
