package at.wifi.swdev.contactbookapp;

public class text {

    public static void main(String args[])
    {
        String str = "nitin";
        StringBuilder sb=new StringBuilder(str);
        sb.reverse();
        String rev= String.valueOf(sb);
        if(str.equals(rev)){
            System.out.println("true");
        }else{
            System.out.println("false");
        }

    }
}
