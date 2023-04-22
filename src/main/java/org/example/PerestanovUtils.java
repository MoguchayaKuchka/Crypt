package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class PerestanovUtils {
    private static String prepareText(String text,String langFlag){
        return langFlag.equals("rus")?
                text.replaceAll("[^А-Яа-яёЁ]", "").toUpperCase():
                text.replaceAll("[^A-Za-z]", "").toUpperCase();
    }
    private static List<List<Character>> prepareInput(String input,String keyWord,String langFlag){
        String preparedInput = prepareText(input,langFlag);
        String preparedKeyWord = prepareText(keyWord,langFlag);

        List<List<Character>> inputAsColumns = new ArrayList<>();
        for (int i = 0; i < preparedKeyWord.length(); i++) {
            inputAsColumns.add(new ArrayList<>());
        }
        char[] inputAsArray = preparedInput.toCharArray();
        for (int i = 0; i < preparedInput.length(); i++) {
            inputAsColumns.get(i%keyWord.length()).add(inputAsArray[i]);
        }
        return inputAsColumns;
    }

    private static int[] getAlphOrder(String preparedKeyWord){
        int[] result = new int[preparedKeyWord.length()];
        Map<Integer, Character> map = new HashMap<>();
        for (int i = 0; i < preparedKeyWord.length(); i++) {
            map.put(i,preparedKeyWord.charAt(i));
        }
        Map<Integer, Character> sortedMap = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        Set s = sortedMap.entrySet();

        for (int i = 0; i < preparedKeyWord.length(); i++) {
            Iterator it = s.iterator();
            int j = 0;
            while (it.hasNext()) {
                Map.Entry m = (Map.Entry)it.next();
                if((Integer)m.getKey()==i){
                    result[i]=j;
                }
                j++;
            }
        }
        return result;
    }
    private static List<List<Character>> rearrange(List<List<Character>> input, int[] order){
        List<List<Character>> output = new ArrayList<>();
        for (int i = 0; i < order.length; i++) {
            for (int j = 0; j < order.length; j++) {
                if(order[j]==i){
                    output.add(input.get(j));
                    break;
                }
            }
        }
        return output;
    }
    private static String columnsAsText(List<List<Character>> input){
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for(List<Character> list:input){
            for(Character ch: list){
                sb.append(ch);
                count++;
                if(count==5){
                    sb.append(" ");
                    count=0;
                }
            }
        }
        return sb.toString();
    }
    private static String stringsAsText(List<List<Character>> input){
        int count = 0;
        List<Integer> sizes =  new ArrayList<>();
        for(List<Character> list: input){
            sizes.add(list.size());
        }
        Collections.sort(sizes);
        int max = sizes.get(sizes.size()-1);
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<max;i++){
            for (List<Character> characters : input) {
                if (!(i == max - 1 && characters.size() < max)) {
                    sb.append(characters.get(i));
                    count++;
                    if (count == 5) {
                        sb.append(" ");
                        count = 0;
                    }
                }
            }
        }
        return sb.toString();
    }

    private static String decrypt(String crypted,String keyWord,String langFlag){
        List<Integer> notFullList = new ArrayList<>();
        String preparedCrypted = prepareText(crypted,langFlag);
        int[] order = getAlphOrder(keyWord);
        int fullCount = (preparedCrypted.length()%keyWord.length())-1;
        if(fullCount!=-1){
            for(int i = order.length-1;i>fullCount;i--){
                notFullList.add(order[i]);
            }
        }
        List<List<Character>> preList = new ArrayList<>();
        for (int i = 0; i < keyWord.length(); i++) {
            preList.add(new ArrayList<>());
        }
        int max = preparedCrypted.length()/keyWord.length()+1;
        if(fullCount==-1) max--;
        int count = 0;
        for(int i = 0;i<keyWord.length();i++){
            for (int j = 0; j < max; j++) {
                if(!(notFullList.contains(i)&&j==max-1)){
                    preList.get(i).add(preparedCrypted.charAt(count));
                    count++;
                }
            }
        }
        List<List<Character>> outputList = new ArrayList<>();
        for (int j : order) {
            outputList.add(preList.get(j));
        }

        return stringsAsText(outputList);
    }
    public static void process(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Выберите язык: 1 - русский, 2 - английский");
        int flag = Integer.parseInt(sc.nextLine());
        String langFlag = flag==1?"rus":"eng";

        System.out.println("Введите ключевое слово:");
        String keyWord = sc.nextLine();

        System.out.println("Введите фразу для шифрования:");
        String inputPhrase = sc.nextLine();
        String crypted = columnsAsText(rearrange(
                prepareInput(inputPhrase,keyWord,langFlag),getAlphOrder(
                        prepareText(keyWord,langFlag))
        ));
        System.out.println("Фраза после шифрования:");
        System.out.println(crypted+"\n");
        String decrypted = decrypt(crypted,keyWord,langFlag);
        System.out.println("Фраза после дешифровки:");
        System.out.println(decrypted);
    }



}
