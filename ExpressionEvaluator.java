import java.util.*;

public class ExpressionEvaluator {
   
   private int priority(char ch) {
      switch(ch) {
         case '(': case ')': case '[': case ']': case '{': case '}':
            return 0;
         case '%': 
            return 1;
         case '+': case '-':
            return 2;
         case '*': case '/':
            return 3;
         case '^':
            return 4;
      }
      return 0;
   }//end priority
   
   private char opposite(char ch) {
      switch(ch) {
	      case ')':return '(';
	      case ']':return '[';
	      case '}':return '{';
      }
      return ' ';
   }//end opposite
   
   private String postfix(String exp) {
      //create stack
      Stack<Character> stk = new Stack<>();
      
      //variables
      String post = "";
      int j, n = exp.length();
      char ch;
      
      for (j = 0; j < n; j++){
         ch = exp.charAt(j);
         if (Character.isLetter(ch)) {
            post += ch;
            continue;
         }
         switch(ch) {
            case '(': case '[': case '{':
               stk.push(ch); break;
            case ')': case ']': case '}':
               char match = opposite(ch);
               while (!stk.empty() && stk.peek() != match) {
                  post += stk.pop();
               }            
       
               if (stk.empty() || stk.peek() != match) {
                   return "Invalid Expression.";
                  
               }
               else {
                  stk.pop();
               }
               break;
         default:
            while (!stk.empty() && priority(ch) <= priority(stk.peek())) {
               post += stk.pop(); 
            }
            stk.push(ch);
         }
      }
      while (!stk.empty()) {
         if (stk.peek() == '(' || stk.peek() == '[' || stk.peek() == '{') {
            post = "Invalid Expression";
            return post;
         }
         post += stk.pop();
      }
      return post;
   }//end postfix
   
   private double valpost(String post, double value[]) {
      //create stack
      Stack<Double> stk = new Stack<>();
      
      int n = post.length(); //find length of postfix expression
         
      for (int j = 0; j < n; j++) { 
         char ch = post.charAt(j);  //iterate through characters of post
         ch = Character.toUpperCase(ch);  //convert to upper case
         
         //check if ch is an operand
         if (ch >= 'A' && ch <= 'Z') {
            int p = ch-'A';   //subtract ch by A to find correct index for value
            stk.push(value[p]);
         }
         else {   //when ch is operator
            double x = 0;
            double y = 0;
            
            //pop top two elements of stk if stk not empty
            if(!stk.empty()) {
                x = stk.pop();
            }
            if (!stk.empty()) {  //repeat to make sure stk is not empty after first pop
               y = stk.pop();
            }
            
            //perform correct operation
            switch(ch) {
               case '+': stk.push(y+x); break;
               case '-': stk.push(y-x); break;
               case '*': stk.push(y*x); break;
               case '/': stk.push(y/x); break;
               case '^': stk.push(Math.pow(y,x)); break;
               case '%': stk.push(y%x); break;
               
               default: 
                  //throws error when wrong operators are used
                  throw new IllegalArgumentException("\tError, invalid operator: " + ch);
            }
         }
         }
         if (!stk.empty()) {
            return stk.pop();  //return top of stack
         }
         else {
            throw new IllegalStateException("\tError, Stack is empty.");
         }
      }//end valpost
      
   public static void main(String[] args){
      Scanner sc = new Scanner(System.in);   //create scanner
      ExpressionEvaluator evaluator = new ExpressionEvaluator(); //create instance of ExpressionEvaluator
      
      while (true) {
         //prompt user to enter infix expression
         System.out.println("\n\n\tPlease enter an infix expression(enter EXIT to terminate): ");
         
         String inf = sc.nextLine();   
         if (inf.equals("EXIT")) {
            break;
         }
         
         //call postfix method
         String post = evaluator.postfix(inf);
         System.out.printf("\n\tPostfix Expression: %4s\n\n", post);  //print postfix expression
         
         double[] values = new double[26];   //one index for each letter of alphabet
         
         //prompt user to enter values for operands
         for (char ch : post.toCharArray()) {
            if (Character.isLetter(ch)) {
               int index = Character.toUpperCase(ch) - 'A'; //index for ch
               if (values[index] == 0) {
                  System.out.print("\t\tEnter the value for " + ch + ": ");
                  values[index] = sc.nextDouble();
                  sc.nextLine(); //skip to next line
               }
            }          
         }
         double result = evaluator.valpost(post, values);
         System.out.println("\n\tValue of Postfix Expression: " + result);
      }
      
      //close scanner
      sc.close();
      
   }//end main
   
   
}//end ExpressionEvaluator