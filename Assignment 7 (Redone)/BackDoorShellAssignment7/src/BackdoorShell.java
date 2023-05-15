//Title: Assignment 7
//Name: Edgar Bahena
//Email: ebahena5@toromail.csudh.edu
//Date: 04/30/2023

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.File;
import java.util.ArrayList;

import Utility.Tools;

public class BackdoorShell{
    private ServerSocket ss;
    private Socket s;
    private File shellFile;

    InputStream in;
    OutputStream out;
    public BackdoorShell(int portNumber, File fileName) throws IOException {
        this.ss = new ServerSocket(portNumber);
        this.s = this.ss.accept();
        this.shellFile = fileName.getParentFile();
        this.in = this.s.getInputStream();
        this.out = this.s.getOutputStream();
    }

    public static void main(String[] args){
        try {

            new BackdoorShell(2000, new File("BackdoorShell").getAbsoluteFile()).run();

        } catch (IOException ioe){

            ioe.printStackTrace();

        }
    }
    public void run() throws IOException{
        int i = 0;
        String text = "";
        Tools util = new Tools();
        ArrayList<String> subDirectories = new ArrayList<>();
        storeDirectories(subDirectories);

        while(!text.equals("Exit")){
            displayCurrentLocation(new File(shellFile.getAbsolutePath()));
            text = readUserInput();

            if(text.equals("dir")){
                listDirectoryInformation(text, util);
            }
            else if(text.contains("cd")){
                try {
                    changeToDirectory(text, subDirectories);
                } catch (Exception e){
                    out.write(e.getMessage().getBytes());
                }
            }
        }
        in.close();
        out.close();
    }
    public String readUserInput() throws IOException{
        int i = 0;
        String text = "";
        while((i = in.read()) != 10){
            if(i != 13){
                text += (char) i;
            }
        }
        return text;
    }

    public void storeDirectories(ArrayList<String> sub){
        String fileName = shellFile.getAbsolutePath();
        String directory = "";
        for(int i = 0; i < fileName.length(); i++){
            if(fileName.charAt(i) != '\\'){
                directory += fileName.charAt(i);
            }
            else{
                sub.add(directory + "\\");
                directory = "";
            }
        }
        sub.add(shellFile.getName());

    }

    public void displayCurrentLocation(File location) throws IOException{
        shellFile = location;
        out.write((byte) 13);
        out.write((byte) 10);
        out.write(shellFile.getAbsoluteFile().toString().getBytes());
        out.write("> ".getBytes());
    }


    public File[] listDirectoryInformation(String input, Tools util) throws IOException{
        if(!shellFile.isFile()){
            if(input.contains(util.getDir())){
                File[] fileList = shellFile.listFiles();
                out.write("List of files in ".getBytes());
                out.write(shellFile.getAbsoluteFile().toString().getBytes());
                out.write((byte) 13);
                out.write((byte) 10);
                out.write((byte) 10);
                for(File temp: fileList){
                    if(!temp.isDirectory()){
                        out.write(temp.getName().getBytes());
                        out.write(" - File".getBytes());
                    } else {
                        out.write(temp.getName().getBytes());
                        out.write(" - Directory".getBytes());
                    }
                    out.write((byte) 13);
                    out.write((byte) 10);
                }
                out.write((byte) 10);
                out.write(("" + fileList.length).getBytes());
                out.write(" files in total".getBytes());
                out.write((byte)13);
                out.write((byte)10);
                return (fileList);
            }
        }
        return null;
    }

    public void changeToDirectory(String input, ArrayList<String> subDirectories) throws Exception{
        String[] newInput = input.split(" ");
        if(newInput.length != 2){
            throw new Exception("must have two arguments");
        }

        File[] listOfFiles = shellFile.listFiles();

        switch (newInput[1]) {

                case (".."):
                     if (!shellFile.getPath().equalsIgnoreCase(subDirectories.get(0))) {
                         shellFile = new File(shellFile.getParent());
                     }
                    break;

                case ("."):
                    //Do nothing
                    break;

                case ("~"):
                    shellFile = new File("BackdoorShell").getAbsoluteFile();
                    shellFile = shellFile.getParentFile();
                    break;

                default:
                    for(File temp : listOfFiles){
                        if(temp.isDirectory() && temp.getName().equalsIgnoreCase(newInput[1])){
                            shellFile = new File(shellFile.getPath() + "\\" + temp.getName());
                        }
                    }
                    break;

            /**
             * out.write((byte) 13);
             *                      * out.write((byte) 10);
             *                      * out.write("Directory ".getBytes());
             *                      * out.write(" not found!".getBytes());
             *                      * break;
             */
        }

    } //End of CD command

}//End of class