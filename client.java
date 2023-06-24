import java.io.*;
import java.net.*;
import java.util.*;

public class client {

    static void print() {
        System.out.println("|--------------------------------|");
        System.out.println("| Select from following options  |");
        System.out.println("|--------------------------------|");
        System.out.println("| 0: To Exit                     |");
        System.out.println("|--------------------------------|");
        System.out.println("| 1: Prefix to Postfix           |");
        System.out.println("|--------------------------------|");
        System.out.println("| 2: Postfix to Prefix           |");
        System.out.println("|--------------------------------|");
        System.out.println("| 3: Prefix to Infix             |");
        System.out.println("|--------------------------------|");
        System.out.println("| 4: Infix to Prefix             |");
        System.out.println("|--------------------------------|");
        System.out.println("| 5: Postfix to Infix            |");
        System.out.println("|--------------------------------|");
        System.out.println("| 6: Infix to Postfix            |");
        System.out.println("|--------------------------------|");

        System.out.print(" Enter your choice:");
    }

    public static void main(String[] args) throws IOException {

        Socket s = new Socket("localhost", 4545);
        Scanner sc = new Scanner(System.in);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        String msg = "";
        while (true) {
            print();
            int choice = sc.nextInt();
            dout.writeInt(choice);

            if (choice == 0) {
                break;
            } else if (choice < 0 || choice > 6) {
                System.out.println("Choose only from given choices\n---------------------------------------------");
                continue;
            }

            sc.reset();
            switch (choice) {
                case 1:
                case 3:
                    msg = "prefix";
                    break;
                case 2:
                case 5:
                    msg = "postfix";
                    break;
                case 4:
                case 6:
                    msg = "infix";
            }

            System.out.print("Enter valid " + msg + " equation (can be alphanumeric and no spaces) : ");

            String eq = sc.next();

            dout.writeUTF(eq);
            eq = din.readUTF();
            dout.flush();
            switch (choice) {
                case 1:
                case 6:
                    msg = "Postfix";
                    break;
                case 2:
                case 4:
                    msg = "Prefix";
                    break;
                case 3:
                case 5:
                    msg = "Infix";
                    break;
            }
            System.out.println(msg + " : " + eq + "\n---------------------------------------------");
        }
        s.close();
        sc.close();
    }
}