package at.wifi.swdev.contactbookapp;

public class test {

    public static void main(String args[]) {

        String str = "a good   example";
        String word[] = str.split("\\s");
        StringBuilder str1= new StringBuilder();
        for (int i = word.length-1;i>=0;i--)
        {
            str1.append(word[i]).append(" ");


        }
        System.out.println(str1.toString().trim());

    }


}
