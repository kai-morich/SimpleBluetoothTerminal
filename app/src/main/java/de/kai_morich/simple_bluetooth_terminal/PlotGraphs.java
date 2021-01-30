package de.kai_morich.simple_bluetooth_terminal;


//ploteo 5 segundos

public class PlotGraphs {
    private Integer ECG_samples[];
    private Integer PPG_samples[];

    private Integer HeartBeat;
    private Integer Oxygen;
    private Integer Temp;

    public Integer[] GetECGData(){
        return ECG_samples;
    }

    public Integer[] getOxData(){
        return PPG_samples;
    }

}
