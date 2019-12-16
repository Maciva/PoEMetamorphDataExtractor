import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static HashMap<String, Set<String>> mapToOrgan = null;
    private static HashMap<String, Set<String>> organToMap = null;
    public static void main(String[] args){
        System.out.println(System.getProperty("user.dir"));
        Scanner scanner = new Scanner(System.in);
        try {
            FileInputStream fileIn = new FileInputStream("mapToOrgan.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            mapToOrgan = (HashMap<String, Set<String>>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
            return;
        }
        try {
            FileInputStream fileIn = new FileInputStream("organToMap.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            organToMap = (HashMap<String, Set<String>>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
            return;
        }
        System.out.println("Path of Exile Metamorph - Organ Finder by Maciva");
        System.out.println("Type help to get a quick introduction");
        System.out.println("Type exit to exit the application");
        while(true){
            String in = scanner.nextLine();
            switch (in){
                case "help": help();
                break;
                case "exit": System.exit(0);
                default: interpretString(in);
            }
        }

    }

    private static void interpretString(String s){
        String[] data = s.split(" ");
        Set<String> outputSet = new HashSet<>();
        if(data.length == 1){
            outputSet = containsOrgan(s) ? getMapsByOrganLowestMax(data[0]) : getOrgansByMap(data[0]);
        }else if(data.length == 2) {
            outputSet = getMapsByOrganWithMax(data[0], Integer.parseInt(data[1]));
        }else {
            System.out.println("Illegal number of Arguments. Type help for a quick introduction");
            return;
        }
        if(outputSet.size() == 0){
            System.out.println("Illegal Arguments. Make sure you have no typo. Type help for a quick introduction");
            return;
        }
        System.out.println(outputSet.stream().collect(Collectors.joining("\n")));
    }

    private static boolean containsOrgan(String s){
        for(String key : organToMap.keySet()){
            if(s.equals(key)){
                return true;
            }
        }
        return false;
    }

    private static Set<String> getMapsByOrganWithMax(String org, int max){
        if(organToMap.containsKey(org)){
            return organToMap.get(org).stream().filter(i -> mapToOrgan.get(i).size() <= max).collect(Collectors.toSet());
        }
        else return new HashSet<>();
    }

    private static void help(){
        System.out.println("Available Functions:\n1. Get Maps by given Organ");
        System.out.println("\tEnter one of these following Organs and optionally a maximum amount of unique organs per Map:\n\t" + String.join(", ", organToMap.keySet()));
        System.out.println("\tIf no maximum number is specified it returns all maps with the smallest amount of different unique organs");
        System.out.println("\n\tExamples:\n\tLiver 3\n\treturns all Maps containing a Liver and no more than 3 different unique organs");
        System.out.println("\n\tEye\n\treturns all Maps containing an Eye with the least amount of different unique Organs");
        System.out.println("2. Get Organs by given Map");
        System.out.println("\tEnter a Mapname to get all available unique Organs on that Map");
        System.out.println("\tExample:\n\tArcade Map\n\treturns all unique organs on Arcade Map");


    }

    static Set<String> getMapsByOrganLowestMax(String org){
        for(int i=0; i<=5; i++){
            Set<String> list = getMapsByOrganWithMax(org, i);
            if(list.size() != 0){
                System.out.println("Returns list of Maps for "+ org + " with a maximum of " + i + " organs per map");
                return list;
            }
        }
        return new HashSet<>();
    }

    static Set<String> getOrgansByMap(String map){
        if(mapToOrgan.containsKey(map))
        return mapToOrgan.get(map);
        else return new HashSet<>();
    }

}
