import java.net.*;
import java.io.*;
import java.util.*;

public class server {

    public static void main(String[] args) throws IOException {
        // Creates a server socket, bound to the specified port
        ServerSocket ss = new ServerSocket(4545);

        // Listens for a connection to be made to this socket and accepts it
        Socket s = ss.accept();

        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        while (true) {
            System.out.println("Waiting for input from user....");
            // input from the client
            int choice = din.readInt();
            if (choice == 0) {
                System.out.println("Exiting....");
                break;
            }

            // equation from the client
            String eq = din.readUTF();

            switch (choice) {
                case 1:
                    eq = preToPost(eq);
                    break;
                case 2:
                    eq = postToPre(eq);
                    break;
                case 3:
                    eq = preToInfix(eq);
                    break;
                case 4:
                    eq = infixToPrefix(eq);
                    break;
                case 5:
                    eq = postToInfix(eq);
                    break;
                case 6:
                    eq = infixToPostfix(eq);
                    break;
            }
            // send ans to the client
            dout.writeUTF(eq);
            // Flushes the data output stream.
            dout.flush();
            System.out.println("Req Complete...");
        }
        ss.close();
    }

    // method for prefix to infix conversion
    static String preToInfix(String str) {
        Stack<String> stack = new Stack<>();

        // Length of expression
        int l = str.length();

        for (int i = l - 1; i >= 0; i--) {
            char c = str.charAt(i);
            if (isOperator(c)) {
                String op1 = stack.pop();
                String op2 = stack.pop();

                // Concat the operands and operator
                String temp = "(" + op1 + c + op2 + ")";
                stack.push(temp);
            } else {

                stack.push(c + "");
            }
        }
        return stack.pop();
    }

    // method for postfix to prefix conversion
    static String postToPre(String eq) {
        Stack<String> st = new Stack<String>();

        // length of expression
        int length = eq.length();

        // reading from left to right
        for (int i = 0; i < length; i++) {

            // check if symbol is operator
            if (isOperator(eq.charAt(i))) {

                // pop two operands from stack
                String op1 = st.peek();
                st.pop();
                String op2 = st.peek();
                st.pop();

                String temp = eq.charAt(i) + op2 + op1;
                // Push String temp back to stack
                st.push(temp);
            }

            // if symbol is an operand
            else {
                // push the operand to the stack
                st.push(eq.charAt(i) + "");
            }
        }

        // concatenate all strings in stack and return the
        // answer
        String ans = "";
        for (String i : st)
            ans += i;
        return ans;
    }

    // method for prefix to postfix conversion
    static String preToPost(String eq) {

        Stack<String> st = new Stack<String>();

        // length of expression
        int length = eq.length();

        for (int i = length - 1; i >= 0; i--) {
            // check if symbol is operator
            if (isOperator(eq.charAt(i))) {
                // pop two operands from stack
                String op1 = st.peek();
                st.pop();
                String op2 = st.peek();
                st.pop();

                String temp = op1 + op2 + eq.charAt(i);
                // Push String temp back to stack
                st.push(temp);
            }

            else { // if symbol is an operand
                   // push the operand to the stack
                st.push(eq.charAt(i) + "");
            }
        }

        // stack contains only the Postfix expression
        return st.peek();
    }

    // method for postfix to infix conversion
    static String postToInfix(String exp) {
        Stack<String> st = new Stack<String>();

        for (int i = 0; i < exp.length(); i++) {
            // Push operands
            if (!isOperator(exp.charAt(i))) {
                st.push(exp.charAt(i) + "");
            } else {
                String op1 = st.peek();
                st.pop();
                String op2 = st.peek();
                st.pop();
                st.push("(" + op2 + exp.charAt(i) + op1 + ")");
            }
        }

        // infix will be the only element of stack
        return st.pop();
    }

    // method to check if the character is alphabet
    static boolean isalpha(char c) {
        if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
            return true;
        }
        return false;
    }

    // method to check if the character is digit
    static boolean isdigit(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        }
        return false;
    }

    // method to check if the character is operator
    static boolean isOperator(char c) {
        return (!isalpha(c) && !isdigit(c));
    }

    // method to compare the priority of operators
    static int getPriority(char C) {
        // '+' and '-' has same priority but less than '*' and '/'
        if (C == '-' || C == '+')
            return 1;
        else if (C == '*' || C == '/')
            return 2;

        return 0;
    }

    // method for infix to postfix conversion
    static String infixToPostfix(String infix) {
        infix = "(" + infix + ")";
        // length of the infix eq.
        int l = infix.length();
        Stack<Character> st = new Stack<>();
        String post = "";

        for (int i = 0; i < l; i++) {
            char curr = infix.charAt(i);
            // if operand add it to post.
            if (isalpha(curr) || isdigit(curr))
                post += curr;

            // if char is ‘(‘ push it to the stack.
            else if (curr == '(')
                st.add('(');

            // if char is ‘)’, pop and post from the stack until an ‘(‘ is encountered.
            else if (curr == ')') {
                while (st.peek() != '(') {
                    post += st.peek();
                    st.pop();
                }
                // Remove '(' from the stack
                st.pop();
            }
            // if Operator found
            else {
                if (isOperator(st.peek())) {
                    while (getPriority(curr) < getPriority(st.peek())) {
                        post += st.peek();
                        st.pop();
                    }
                    // Push current Operator on stack
                    st.add(curr);
                }
            }
        }
        // pop all the remaining element of the stack and add them to post
        while (!st.empty()) {
            post += st.pop();
        }
        return post;
    }

    static String infixToPrefix(String in) {
        // reverse infix string and replace '(' with ')' and vice versa Get Postfix
        // Reverse Postfix
        int l = in.length();

        // Reverse infix
        in = new StringBuilder(in).reverse().toString();
        // to char array
        char[] infix = in.toCharArray();

        // Replace '(' with ')' and vice versa
        for (int i = 0; i < l; i++) {

            if (infix[i] == '(') {
                infix[i] = ')';
                i++;
            } else if (infix[i] == ')') {
                infix[i] = '(';
                i++;
            }
        }

        // get postfix of the reverse infix
        String prefix = infixToPostfix(String.valueOf(infix));

        // Reverse postfix
        prefix = new StringBuffer(prefix).reverse().toString();
        return prefix;
    }
}