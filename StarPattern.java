import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = 0;
        
        // Read the natural number without printing any prompt
        if (scanner.hasNextInt()) {
            n = scanner.nextInt();
        }
        
        // First pattern: 1 to n stars
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                System.out.print("*");
            }
            System.out.println();
        }
        
        // Second pattern: n stars again
        for (int i = 0; i < n; i++) {
            System.out.print("*");
        }
        System.out.println();
        
        // Third pattern: n-1 stars
        for (int j = 0; j < n-1; j++) {
            System.out.print("*");
        }
        System.out.println();
        
        // Fourth pattern: n+2 stars
        for (int j = 0; j < n+2; j++) {
            System.out.print("*");
        }
        System.out.println();
        
        // Fifth pattern: n stars again
        for (int i = 0; i < n; i++) {
            System.out.print("*");
        }
        System.out.println();
        
        // Sixth pattern: n stars once more
        for (int i = 0; i < n; i++) {
            System.out.print("*");
        }
        System.out.println();
        
        scanner.close();
    }
}
