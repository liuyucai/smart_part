package cn.lps.controller;

/**
 * @author liuyucai
 * @Created 2020/11/3
 * @Description
 */

enum Color
{
    RED, GREEN, BLUE;

    // 构造函数
    private Color()
    {
        System.out.println("Constructor called for : " + this.toString());
    }

    public void colorInfo()
    {
        System.out.println("Universal Color");
    }
}
public class EnumDemo {
    public static void main(String[] args) {
        Color c1 = Color.RED;
        System.out.println(c1);
        c1.colorInfo();
        switch (c1){
            case RED:

                break;
            case GREEN:

                break;
            case BLUE:

                break;
        }
    }
}
