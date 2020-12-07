// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import java.io.*;


public class SchedulingAlgorithm {

  public static Results Run(int runtime, Vector<sProcess> processVector, Results result) {
    int i = 0;
    int comptime = 0;
    int currentProcess = 0;
    int previousProcess = 0;
    int size = processVector.size();
    int completed = 0;
    String resultsFile = "Summary-Processes";

    result.schedulingType = "Batch (Nonpreemptive)";
    result.schedulingName = "Shortest remaining time";
    try {
      //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
      //OutputStream out = new FileOutputStream(resultsFile);
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      createPriority(processVector);

      currentProcess=getProcessWithShortestRemainingTime(processVector,0,comptime);
      sProcess process = processVector.elementAt(currentProcess);
      out.println("Process: " + process.id + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + " " +process.priority+ ")");

      while (comptime < runtime) {
        if (process.cpudone == process.cputime) {
          completed++;
          out.println("Process: " + process.id + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + " " +process.priority+ ")");
          if (completed == size) {
            result.compuTime = comptime;
            out.close();
            return result;
          }

          currentProcess=getProcessWithShortestRemainingTime(processVector,currentProcess,comptime);
          process = processVector.elementAt(currentProcess);
          out.println("Process: " + process.id + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + " " +process.priority+ ")");

          if (processVector.isEmpty())
            break;

          if (currentProcess >= processVector.size())
            currentProcess = 0;
          process = processVector.elementAt(currentProcess);

          if (currentProcess == previousProcess && process.cpudone >= process.cputime) {
            out.println("Process:" + process.id + "completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone+ " " +process.priority+  ")");
            break;
          }
          out.println("Process: " + process.id + "registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + " " +process.priority+ ")");
        }


        if (process.ioblocking == process.ionext) {
          out.println("Process: " + currentProcess + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + " "+process.priority+ ")");
          process.numblocked++;
          process.ionext = 0;
          currentProcess=getProcessWithShortestRemainingTime(processVector,currentProcess,comptime);
          process = processVector.elementAt(currentProcess);

            if (processVector.isEmpty())
              break;
            if (currentProcess >= processVector.size())
              currentProcess = 0;
            process = processVector.elementAt(currentProcess);
            if (currentProcess == previousProcess && process.cpudone >= process.cputime) {
              out.println("Process: " + process.id + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone+ " "  +process.priority+ ")");
              break;
            }

            out.println("Process: " + process.id + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone+ " "  +process.priority+ ")");
          }


          process.cpudone++;
          if (process.ioblocking > 0) {
            process.ionext++;
          }
          comptime++;

        }
        out.close();
      } catch (IOException e) { /* Handle exceptions */ }
      result.compuTime = comptime;
      return result;
    }
  private static int getProcessWithShortestRemainingTime(Vector<sProcess> processes,int previousProcessid,int comptime) {
    int minRemaining = 0;
    int Processid = -1;
    for (int i = 0; i < processes.size(); i++) {
      sProcess process = processes.elementAt(i);
      if(process.delaytime>comptime)
        continue;
      int RemainingTime=process.cputime - process.cpudone;
      if(RemainingTime!=0 && i!= previousProcessid){
        if(RemainingTime < minRemaining || minRemaining == 0){
          minRemaining = RemainingTime;
          Processid = i;
        }
        else if(RemainingTime == minRemaining){
          if(process.priority > ((sProcess) processes.elementAt(Processid)).priority) {
            minRemaining = RemainingTime;
            Processid = i;
          }
        }
      }
    }
    if(Processid == -1) return previousProcessid;
    else return Processid;
  }



  private static void createPriority(Vector<sProcess> processes) {
    int i = 0;
    Collections.sort(processes, new Comparator<sProcess>() {

      @Override
      public int compare(sProcess o1, sProcess o2) {
        sProcess pr1 = (sProcess) o1;
        sProcess pr2 = (sProcess) o2;
        return  pr2.ioblocking - pr1.ioblocking ;
      }
    });
    for (sProcess process : processes) {
      process.priority = ++i;
    }
  }
}
