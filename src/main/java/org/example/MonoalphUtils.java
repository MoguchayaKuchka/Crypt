package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class MonoalphUtils {
    private static final List<Character> rusAlph = Arrays.asList('А','Б','В','Г','Д','Е','Ё','Ж','З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х'
            ,'Ц','Ч','Ш','Щ','Ъ','Ы','Ь','Э','Ю','Я');
    private static final List<Character> engAlph=Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T'
            ,'U','V','W','X','Y','Z');
    private static String prepareText(String text,String langFlag){
        return langFlag.equals("rus")?
                text.replaceAll("[^А-Яа-яёЁ]", "").toUpperCase():
                text.replaceAll("[^A-Za-z]", "").toUpperCase();
    }
    private static Map<Character,Character> prepareAlph(String preparedText, String langFlag){
        List<Character> alph = new ArrayList<>();
        Map<Character,Character> output = new HashMap<>();
        for(Character ch:preparedText.toCharArray()){
            if(!alph.contains(ch)) alph.add(ch);
        }
        for(Character ch: langFlag.equals("rus")?rusAlph:engAlph){
            if(!alph.contains(ch)) alph.add(ch);
        }
        for (int i = 0; i < (langFlag.equals("rus")?rusAlph.size():engAlph.size()); i++) {
            output.put((langFlag.equals("rus")?rusAlph.get(i):engAlph.get(i)),alph.get(i));
        }
        output.put(' ',' ');
        return output;
    }
    private static String crypt(String inputText, Map<Character,Character> cryptAlph,String langFlag){
        char[] prepared = prepareText(inputText,langFlag).toCharArray();
        StringBuilder output = new StringBuilder();
        for (int i = 1; i <= prepared.length; i++) {
            output.append(cryptAlph.get(prepared[i-1]));
            if(i%5==0) output.append(" ");
        }
        return output.toString();
    }
    private static String decrypt(String inputText,Map<Character,Character> cryptAlph){
        StringBuilder output = new StringBuilder();
        Map<Character,Character> decryptAlph = cryptAlph.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        for(Character ch: inputText.toCharArray()){
            output.append(decryptAlph.get(ch));
        }
        return output.toString();
    }
    public static void process(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Выберите язык: 1 - русский, 2 - английский");
        int flag = Integer.parseInt(sc.nextLine());
        String langFlag = flag==1?"rus":"eng";
        System.out.println("Введите ключ-фразу");
        String keyPhrase = sc.nextLine();
        String preparedKeyPhrase = prepareText(keyPhrase,langFlag);
        Map<Character,Character> cryptAlph = prepareAlph(preparedKeyPhrase,langFlag);
        System.out.println("Введите текст для шифрования");
        String inputText = sc.nextLine();
        String crypted = crypt(inputText,cryptAlph,langFlag);
        System.out.println("Зашифрованный текст:\n"+crypted);
        System.out.println("Расшифрованный текст:\n"+(decrypt(crypted,cryptAlph)));
    }
}
