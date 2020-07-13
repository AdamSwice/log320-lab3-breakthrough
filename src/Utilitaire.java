import java.util.HashMap;
import java.util.Map;

public class Utilitaire {
    public static Map<String, Integer> InputConverter = new HashMap<>();
    public static Map<String, String> OutputConverter = new HashMap<>();

    public Utilitaire(String ConversionType){
        if(ConversionType=="input") {
            InputConverter.put("A", 0);
            InputConverter.put("B", 1);
            InputConverter.put("C", 2);
            InputConverter.put("D", 3);
            InputConverter.put("E", 4);
            InputConverter.put("F", 5);
            InputConverter.put("G", 6);
            InputConverter.put("H", 7);
            InputConverter.put("1", 7);
            InputConverter.put("2", 6);
            InputConverter.put("3", 5);
            InputConverter.put("4", 4);
            InputConverter.put("5", 3);
            InputConverter.put("6", 2);
            InputConverter.put("7", 1);
            InputConverter.put("8", 0);
        }
        else{
            OutputConverter.put("0", "8");
            OutputConverter.put("1", "7");
            OutputConverter.put("2", "6");
            OutputConverter.put("3", "5");
            OutputConverter.put("4", "4");
            OutputConverter.put("5", "3");
            OutputConverter.put("6", "2");
            OutputConverter.put("7", "1");
            OutputConverter.put("8", "A");
            OutputConverter.put("9", "B");
            OutputConverter.put("10", "C");
            OutputConverter.put("11", "D");
            OutputConverter.put("12", "E");
            OutputConverter.put("13", "F");
            OutputConverter.put("14", "G");
            OutputConverter.put("15", "H");
        }
    }

    public Map<String, Integer> getConvertedMoveValues(String move){
        move = move.replace(" - ", "");
        move = move.replaceAll(" ", "");
        Map<String, Integer> convertedMoves = new HashMap<>();
        convertedMoves.put("fromRow", InputConverter.get(Character.toString(move.charAt(1))));
        convertedMoves.put("fromColumn", InputConverter.get(Character.toString(move.charAt(0))));
        convertedMoves.put("toRow", InputConverter.get(Character.toString(move.charAt(3))));
        convertedMoves.put("toColumn", InputConverter.get(Character.toString(move.charAt(2))));
        return convertedMoves;
    }
    public String getConvertedOutputMoveValues(String move) {
        String postion;
        int column= Integer.parseInt(""+move.charAt(1));
        column+=8;
        postion=OutputConverter.get(""+column);

        postion +=OutputConverter.get(Character.toString(move.charAt(0)));


    return postion;
    }
}
