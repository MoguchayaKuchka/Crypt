package org.example;



import java.util.*;
import java.util.stream.Collectors;

public class DroblUtils {
    private static final List<Character> rusAlph = Arrays.asList('А','Б','В','Г','Д','Е','Ё','Ж','З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х'
            ,'Ц','Ч','Ш','Щ','Ъ','Ы','Ь','Э','Ю','Я');
    private static String prepareText(String text){
        return text.replaceAll("[^А-Яа-яёЁ]", "").toUpperCase();
    }
    private static List<Character> prepareAlph(String preparedText){
        List<Character> alph = new ArrayList<>();
        for(Character ch:preparedText.toCharArray()){
            if(!alph.contains(ch)) alph.add(ch);
        }
        for(Character ch: rusAlph){
            if(!alph.contains(ch)) alph.add(ch);
        }
        return alph;
    }
    private static Map<Character, String> prepareTable(List<Character> alph){
        Map<Character,String> result = new HashMap<>();
        //TODO: think of hardcode
        int count = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if(count==33){
                    result.put((char)j,(char)(i+'0')+""+ (char)(j+'0'));
                }
                else{
                    result.put(alph.get(count),(char)(i+'0')+""+ (char)(j+'0'));
                    count++;
                }
            }
        }
        return result;
    }
    private static String crypt(String preparedText,Map<Character,String> table){
        StringBuilder iBuilder = new StringBuilder();
        StringBuilder jBuilder = new StringBuilder();
        for(Character ch:preparedText.toCharArray()){
            iBuilder.append(table.get(ch).charAt(0));
            jBuilder.append(table.get(ch).charAt(1));
        }
        char[] compared = iBuilder.append(jBuilder).toString().toCharArray();
        Map<String,Character> reversed = table.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < compared.length; i+=2) {
            result.append(reversed.get(compared[i]+""+compared[i+1]));
        }
        return result.toString();
    }
    private static String decrypt (String crypted, String keyWord){
        Map<Character,String> table = prepareTable(prepareAlph(keyWord));
        Map<String,Character> reversed = table.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        StringBuilder stringOfCords = new StringBuilder();
        for(Character ch: crypted.toCharArray()){
            stringOfCords.append(table.get(ch).charAt(0));
            stringOfCords.append(table.get(ch).charAt(1));
        }
        char[] cordsAsArray = stringOfCords.toString().toCharArray();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < cordsAsArray.length/2; i++) {
            result.append(reversed.get((cordsAsArray[i]+""+cordsAsArray[cordsAsArray.length/2+i])
            ));
        }
        return result.toString();
    }

    public static void process(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите текст для шифрования:");
        String preparedInput = prepareText(sc.nextLine());
        System.out.println("Введите ключевую фразу:");
        String preparedKey = prepareText(sc.nextLine());
        String crypted = crypt(preparedInput,prepareTable(prepareAlph(preparedKey)));
        System.out.println("Зашифрованный текст:"+"\n"+crypted);
        System.out.println("Расшифрованный текст:"+"\n"+decrypt(crypted,preparedKey));
    }
}