import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class RectangleArea {
    public static void main(String[] args) throws IOException {
        double a, b, area;
        
        // Create a BufferedReader object to read input from the user
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // Ask for the first side of the rectangle
        System.out.print("Enter the length of the first side of the rectangle (a): ");
        a = Double.parseDouble(br.readLine());
        
        // Ask for the second side of the rectangle
        System.out.print("Enter the length of the second side of the rectangle (b): ");
        b = Double.parseDouble(br.readLine());
        
        // Calculate the area of the rectangle
        area = a * b;
        
        // Display the result
        System.out.println("The area of the rectangle is: " + area);
    }
}
