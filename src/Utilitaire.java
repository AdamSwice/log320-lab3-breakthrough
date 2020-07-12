import java.util.HashMap;
import java.util.Map;

public class Utilitaire {
    public static Map<String, Integer> converter = new HashMap<>();

    public Utilitaire(){
        converter.put("A", 0);
        converter.put("B", 1);
        converter.put("C", 2);
        converter.put("D", 3);
        converter.put("E", 4);
        converter.put("F", 5);
        converter.put("G", 6);
        converter.put("H", 7);
        converter.put("1", 7);
        converter.put("2", 6);
        converter.put("3", 5);
        converter.put("4", 4);
        converter.put("5", 3);
        converter.put("6", 2);
        converter.put("7", 1);
        converter.put("8", 0);
    }

    public Map<String, Integer> getConvertedMoveValues(String move){
        move = move.replace(" - ", "");
        move = move.replaceAll(" ", "");
        Map<String, Integer> convertedMoves = new HashMap<>();
        convertedMoves.put("fromRow", converter.get(Character.toString(move.charAt(1))));
        convertedMoves.put("fromColumn", converter.get(Character.toString(move.charAt(0))));
        convertedMoves.put("toRow", converter.get(Character.toString(move.charAt(3))));
        convertedMoves.put("toColumn", converter.get(Character.toString(move.charAt(2))));
        return convertedMoves;
    }
}
