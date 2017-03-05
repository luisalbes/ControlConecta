package es.conectaconsultores.controlconecta;

/**
 * Created by Luis Albes on 08/10/2016.
 */

public class Actuaciones {


    private String mTitulo;
    private String mDia;
    private String mHoraEntrada;
    private String mHoraSalida;

    public Actuaciones(String mHoraSalida, String mHoraEntrada, String mDia, String mTitulo) {
        this.mHoraSalida = mHoraSalida;
        this.mHoraEntrada = mHoraEntrada;
        this.mDia = mDia;
        this.mTitulo = mTitulo;
    }


    public String getTitulo() {
        return mTitulo;
    }

    public void setTitulo(String mTitulo) {
        this.mTitulo = mTitulo;
    }

    public String getHoraSalida() {
        return mHoraSalida;
    }

    public void setHoraSalida(String mHoraSalida) {
        this.mHoraSalida = mHoraSalida;
    }

    public String getHoraEntrada() {
        return mHoraEntrada;
    }

    public void setHoraEntrada(String mHoraEntrada) {
        this.mHoraEntrada = mHoraEntrada;
    }

    public String getDia() {
        return mDia;
    }

    public void setDia(String mDia) {
        this.mDia = mDia;
    }
}