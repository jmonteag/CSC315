import java.io.File;
        import java.text.NumberFormat;
        import java.util.Arrays;
        import java.util.HashMap;
        import java.util.Scanner;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

public class lab2
{
    public static void main(String args[])throws Exception
    {
        HashMap<String,String>registers = new HashMap<>();
        registers.put("zero","00000");
        registers.put("0","00000");
        registers.put("v0","00010");
        registers.put("v1","00011");
        registers.put("a0","00100");
        registers.put("a1","00101");
        registers.put("a2","00110");
        registers.put("a3","00111");
        registers.put("t0","01000");
        registers.put("t1","01001");
        registers.put("t2","01010");
        registers.put("t3","01011");
        registers.put("t4","01100");
        registers.put("t5","01101");
        registers.put("t6","01110");
        registers.put("t7","01111");
        registers.put("s0","10000");
        registers.put("s1","10001");
        registers.put("s2","10010");
        registers.put("s3","10011");
        registers.put("s4","10100");
        registers.put("s5","10101");
        registers.put("s6","10110");
        registers.put("s7","10111");
        registers.put("t8","11000");
        registers.put("t9","11001");
        registers.put("sp","11101");
        registers.put("ra","11111");

        HashMap<String,Integer>map = new HashMap<>();
        System.out.println("The file that you want to open is: "+args[0]);
        File file = new File(args[0]);
        int count=0;
        boolean flag = false;
        Scanner output = new Scanner(file);
        while(output.hasNextLine())
        {
//            System.out.println(output.nextLine());
            count++;
            String line = output.nextLine();
            if(line.indexOf(":")!=-1)
            {
                flag = true;
                if(flag)
                {
                    String[]splitPart = line.split(":");
                    map.put(splitPart[0],count);
                    System.out.println("A label was found at line: "+count);
                    flag =false;
                }

            }
        }

        System.out.println(Arrays.asList(map));

        output.close();

        Scanner pass2 = new Scanner(file);
        int switchState;
        while(pass2.hasNextLine())
        {
            switchState = 0;
            String bleh = pass2.nextLine();
            if(bleh.matches(".*\\band\\b.*"))
                switchState = 1;
            else if (bleh.matches(".*\\bor\\b.*"))
                switchState = 2;
            else if (bleh.matches(".*\\badd\\b.*"))
                switchState = 3;
            else if (bleh.matches(".*\\baddi\\b.*"))
                switchState = 4;
            else if (bleh.matches(".*\\bsll\\b.*"))
                switchState = 5;
            else if (bleh.matches(".*\\bsub\\b.*"))
                switchState = 6;
            else if (bleh.matches(".*\\bslt\\b.*"))
                switchState = 7;
            else if (bleh.matches(".*\\bbeq\\b.*"))
                switchState = 8;
            else if (bleh.matches(".*\\bbne\\b.*"))
                switchState = 9;
            else if (bleh.matches(".*\\blw\\b.*"))
                switchState = 10;
            else if (bleh.matches(".*\\bsw\\b.*"))
                switchState = 11;
            else if (bleh.matches(".*\\bj\\b.*"))
                switchState = 12;
            else if (bleh.matches(".*\\bjr\\b.*"))
                switchState = 13;
            else if (bleh.matches(".*\\bjal\\b.*"))
                switchState = 14;
            String break1[];
            switch(switchState)
            {
                case 1: //and
                    RdRsRt(bleh,registers,switchState);
                    break;
                case 2: //or
                    RdRsRt(bleh,registers,switchState);
                    break;
                case 3: //add
//                    System.out.println(bleh);
                    RdRsRt(bleh,registers,switchState);
                    break;
                case 4: //addi
                    RtRsImm(bleh,registers);
                    break;
                case 5: //sll
                    RdRtSa(bleh,registers,switchState);
                    break;
                case 6: //sub
                    RdRsRt(bleh,registers,switchState);
                    break;
                case 7: //slt
                    RdRsRt(bleh,registers,switchState);
                    break;
                case 8: //beq
                    RsRtOffset(bleh,registers,map,switchState);
                    break;
                case 9: //bne
                    RsRtOffset(bleh,registers,map,switchState);
                    break;
                case 10: //lw
                    LwSw(bleh,registers,switchState);
                    break;
                case 11: //sw
                    LwSw(bleh,registers,switchState);
                    break;
                case 12: //j
                    Jumps(bleh,registers,switchState,map);
                    break;
                case 13: //jr
                    JumpRs(bleh,registers);
                    break;
                case 14: //jal
                    Jumps(bleh,registers,switchState,map);
                    break;

            }
        }

    }
    public static void RdRsRt(String bleh,HashMap<String,String> Registers,int instruct)
    {
        String break1[] = bleh.split("\\$|,| #|#",-5);
//        System.out.println(break1[1]); //rd
//        System.out.println(break1[3]); //rs
//        System.out.println(break1[5]); //rt
        if(instruct == 3) //add
        {
            System.out.println("000000 " + Registers.get(break1[3]) + " " + Registers.get(break1[5]) + " "
                    + Registers.get(break1[1]) + " 00000 100000");
        }
        else if(instruct == 1) //and
        {
            System.out.println("000000 " + Registers.get(break1[3]) + " " + Registers.get(break1[5]) + " "
                    + Registers.get(break1[1]) + " 00000 100100");
        }
        else if(instruct == 2) //or
        {
            System.out.println("000000 " + Registers.get(break1[3]) + " " + Registers.get(break1[5]) + " "
                    + Registers.get(break1[1]) + " 00000 100101");
        }
        else if(instruct == 6) //Sub
        {
            String whitespace = break1[5].replaceAll("\\s","");
            System.out.println("000000 " + Registers.get(break1[3]) + " " + Registers.get(whitespace) + " "
                    + Registers.get(break1[1]) + " 00000 100010");
        }
        else if(instruct == 7) // slt
        {
            System.out.println("000000 " + Registers.get(break1[3]) + " " + Registers.get(break1[5]) + " "
                    + Registers.get(break1[1]) + " 00000 101010");
        }
    }
    public static void RtRsImm(String bleh, HashMap<String,String>Registers)
    {
        String break1[] = bleh.split("\\$|, | #|#|,",-5);
//        System.out.println(break1[1]); //rt
//        System.out.println(break1[3]); //rs
//        System.out.println(break1[4]); //imm
        int value = Integer.parseInt(break1[4]);
        String binaryString = String.format("%"+Integer.toString(16)+"s",Integer.toBinaryString(value))
                .replace(" ","0");
        System.out.println("001000 "+Registers.get(break1[3])+" "+Registers.get(break1[1])+" "
                +binaryString);
    }
    public static void RsRtOffset(String bleh, HashMap<String,String>Registers,HashMap<String,Integer>Jumps,int instruct)
    {
        String break1[] = bleh.split("\\$|, | #|#|,",-5);
//        System.out.println(break1[1]); //rs
//        System.out.println(break1[3]); //rt
        String whitespace = break1[4].replaceAll("\\s","");
        int i = Jumps.get(whitespace);
//        System.out.println(i);
        String binaryString = String.format("%"+Integer.toString(16)+"s",Integer.toBinaryString(i))
                .replace(" ","0");
        if(instruct == 8)// beq
        {
            System.out.println("000100 "+Registers.get(break1[1])+" "+Registers.get(break1[3])+" "
                    +binaryString);
        }
        else if(instruct ==9)//bne
        {
            System.out.println("000101 "+Registers.get(break1[1])+" "+Registers.get(break1[3])+" "
                    +binaryString);
        }
    }
    public static void LwSw(String bleh, HashMap<String,String>Registers,int instruct)
    {
        String break1[] = bleh.split("\\$|, | #|#|,",-5);
        String line = break1[2].replaceAll("\\(","");
        String line2 = break1[3].replaceAll("\\)","");
//        System.out.println(break1[1]); //rt
//        System.out.println(line); //offset
//        System.out.println(line2); //rs
        if(instruct == 10) //lw
        {
            int value = Integer.parseInt(line);
            String binaryString = String.format("%"+Integer.toString(16)+"s",Integer.toBinaryString(value))
                    .replace(" ","0");
            System.out.println("100011 "+Registers.get(line2)+" "+Registers.get(break1[1])+" "
                    +binaryString);
        }
        else if (instruct == 11) //sw
        {
            int value = Integer.parseInt(line);
            String binaryString = String.format("%"+Integer.toString(16)+"s",Integer.toBinaryString(value))
                    .replace(" ","0");
            System.out.println("101011 "+Registers.get(line2)+" "+Registers.get(break1[1])+" "
                    +binaryString);
        }
    }
    public static void RdRtSa(String bleh,HashMap<String,String>Registers,int instruct)
    {
        String break1[] = bleh.split("\\$|, | #|#|,",-5);
//        System.out.println(break1[1]); //rd
//        System.out.println(break1[3]); //rt
//        System.out.println(break1[4]); //sa
        int value = Integer.parseInt(break1[4]);
        String binaryString = String.format("%"+Integer.toString(5)+"s",Integer.toBinaryString(value))
                .replace(" ","0");
        System.out.println("000000 00000 "+Registers.get(break1[3])+" "+Registers.get(break1[1])+" "+binaryString
                +" 000000");

    }
    public static void Jumps(String bleh,HashMap<String,String>Registers,int instruct,HashMap<String,Integer>Jumps)
    {
        if(instruct == 12)
        {
            String break1[] = bleh.split("j\\s|j|#| #", -5);
//            System.out.println(break1[1]); //target
            String whitespace = break1[1].replaceAll("\\s","");
            int i = Jumps.get(whitespace);
            String binaryString = String.format("%"+Integer.toString(16)+"s",Integer.toBinaryString(i))
                    .replace(" ","0");
            System.out.println("000010 "+binaryString);

        }
        else if(instruct == 14)
        {
            String break1[] = bleh.split("jal\\s|jal|#| #", -5);
//            System.out.println(break1[1]); //target
            String whitespace = break1[1].replaceAll("\\s","");
            int i = Jumps.get(whitespace);
            String binaryString = String.format("%"+Integer.toString(16)+"s",Integer.toBinaryString(i))
                    .replace(" ","0");
            System.out.println("000011 "+binaryString);
        }

    }
    public static void JumpRs(String bleh,HashMap<String,String>registers)
    {
        String break1[] = bleh.split("jr\\s|jr|#| #| \\$|\\$", -5);
//            System.out.println(break1[2]); //target
        System.out.println("000000 "+registers.get(break1[2])+" 000000000000000 001000");
    }

}
