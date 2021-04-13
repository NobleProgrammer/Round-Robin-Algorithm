// Name and ID: 1742403 - Hussam Shawly, 1742559 - Ammar Joharji, 1742589 - Omar Al-Qurashi, 1740373 - Muhammad Al-Harbi
// Date: 7/19/2019
// Instructor: Dr.Muhammad Imran
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;


public class RoundRobin {

    public static void main(String[] args) {
        Scanner input=new Scanner(System.in);
        //Variable for arrival time
        boolean arrival = false;
        //Prompt
        System.out.print("Enter the number of processes :");
        //Input
        int size = input.nextInt();
        //Array for the processes
        ArrayList<Process> arr1 = new ArrayList<>();        
        //Prompt
        System.out.println("do you want arrival times ?");
        System.out.println("1. yes      2.no");
        //Input
        int in = input.nextInt();
        //If the user wants arrival time
        if(in == 1)
            arrival = true;
        //Prompt
        System.out.print("\nEnter the burst time , and arrival time [Optional]\n");
        for(int i = 0 ; i<size ; i++)
        {
            System.out.println("Process "+(i+1)+" : ");
            if (arrival)
            {
                //Insert Burst time and Arrival time
                Process temp = new Process(input.nextInt(),input.nextInt());
                arr1.add(temp);
            }
            else
            {
                //Insert Burst time
                Process temp = new Process(input.nextInt());
                arr1.add(temp);
            }
        }
        //Queues for the Round Robin
        Queue<Process> readyQ = new LinkedList<>();
        Queue<Process> finishQ = new LinkedList<>();
        Queue<Process> startQ = new LinkedList<>();
        //Sort the array if the user enteres arrival times..
        if (arrival)
            Collections.sort(arr1,new ProcessSort());
        //Now , Input the user to enter the quantum time ..
        System.out.print("Enter the Quantum time : ");
        int quantum = input.nextInt();
        
        //Insert Process into the ready Queue
        for (int i=0;i<size;i++){
            readyQ.add(arr1.get(i));
        }
        
        
        //Variable for calculating the time in all processes
        int totalTime = 0;
        //Loop for the round robin
        while (!readyQ.isEmpty()) {
            //Puts the first process in the queue in a temp
            Process temp = readyQ.remove();
            //Checks if the process has already started or not
            if(!startQ.contains(temp)){
                //Adds it to the start queue so that it keeps track of the started processes
                startQ.add(temp);
                //Updates process start time
                temp.setStartTime(totalTime);
            }
            //If the process is not finished yet
            if (temp.getCurrentTime() != temp.getBurstTime()) {
                //Calculate total Time
                if (temp.getBurstTime() < quantum + temp.getCurrentTime()) {
                    totalTime += (temp.getBurstTime() - temp.getCurrentTime());
                } else {
                    totalTime += quantum;
                }
                //Calculate processes current time in the queue
                temp.CalCurrentTime(temp, quantum);
                
            } 
            //If the process is finished
            if (temp.getCurrentTime() == temp.getBurstTime())
            {
                //Updates finish time
                temp.setFinishTime(totalTime);
                //Updates wait time
                temp.CalWaitTime(temp, totalTime);
                //Updates turnaround time
                temp.CalTurnaroundTIme(temp);
                //Adds the process in the finished queue
                finishQ.add(temp);
            }
            else
            //Otherwise returns the process in the ready queue
            {
                readyQ.add(temp);
            }
        }
        //Prints the finished processes
        printProcessQueue(finishQ);
        
        }
    //---------------------------------------------------------------------------------------------------------------
    //Method for printing the process's information
    public static void printProcessQueue(Queue q)
    {
        //Variable for the position of the process in the queue in terms of finish time
        int i = 0;
        System.out.println("Process NO.\t|Burst Time\t|Arrival Time\t|Start Time\t|Waiting Time\t|Turnaround Time\t|Finish Time");
        while(!q.isEmpty())
        {
            Process temp = (Process)q.remove();
            System.out.println((++i)+"\t\t"+temp.getBurstTime()+"\t\t"+temp.getArrTime()+"\t\t"+temp.getStartTime()+"\t\t"+temp.getWaitTime()+"\t\t"+temp.CalTurnaroundTIme(temp)+"\t\t\t"+temp.getFinishTime());
        
        }
    }            
        
}

    
//---------------------------------------------------------------------------------------------------------------
//Class for overriding the compare method in the compartor interface, so the we can sort the arraylist
class ProcessSort implements Comparator<Process>
{

    @Override
    public int compare(Process a, Process b) {
        return a.getArrTime() - b.getArrTime();
    }
}
//Process class
class Process
{
    //Data members
    private int arrTime = 0;
    private int startTime;
    private int burstTime;
    private int waitTime;
    private int currentTime;
    private int finishTime;
    
    //Constructors
    public Process(int burstTime) {
        this.burstTime = burstTime;
    }
    
    public Process(int burstTime, int arrTime) {
        this.arrTime = arrTime;
        this.burstTime = burstTime;
    }
    
    public int getArrTime() {
        return arrTime;
    }

    public void setArrTime(int arrTime) {
        this.arrTime = arrTime;
    }
    //Getters and Setters
    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }
    
    //Method for calculating waiting time
    public void CalWaitTime(Process p,int totalTime)
    {
        int arrTime = p.getArrTime();
        int burstTime = p.getBurstTime();
        
        this.waitTime = totalTime - arrTime - burstTime;
    }
    //Method for updating current time
    public void CalCurrentTime(Process p , int quantum) {
        if (p.burstTime < quantum + p.currentTime ) {
            p.currentTime += (p.burstTime - p.currentTime);
        }
        else {
            p.currentTime += quantum;
        }     
    }
    //Mehtod for calculating the turnaround time
    public int CalTurnaroundTIme(Process p) {
        return (p.finishTime - p.arrTime);
    }
 
}