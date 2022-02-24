import java.util.Scanner;

class Main {
  static Scanner scanner = new Scanner (System.in);

  static byte [][] tower = new byte [3] [];
  //pieces: biggest to smallest: (pieces - 1) - 0
  static byte pieces = 0;
  static byte [] [] display = new byte [10] [51];
  static byte [] sizes;

  static String [] colors = {"\033[33m\u2592\033[0m",
                            "\033[90m\u2592\033[0m",
                            "\033[91m\u2592\033[0m",
                            "\033[92m\u2592\033[0m",
                            "\033[93m\u2592\033[0m",
                            "\033[94m\u2592\033[0m",
                            "\033[95m\u2592\033[0m",
                            "\033[96m\u2592\033[0m"};
  
  static String towerSupport = "\033[37m\u2592\033[0m";
  static String sound = "\007";

  public static void main(String[] args) {
    while (pieces < 3 || pieces > 8) {
      System.out.println("How many pieces?");
      pieces = (byte) scanner.nextInt ();
      System.out.print ("Must be within 3 and 8, inclusive. ");
    }

    //initializing sizes
    sizes = new byte [pieces];

    for (int i = 0; i < pieces; i++) {
      sizes [i] = (byte)((pieces - i) * 2 - 1);
    }
    //finished initializing sizes

    //initializing tower
    for (int i = 0; i < 3; i++) {
      tower [i] = new byte [pieces];

      for (int j = 0; j < pieces; j++) {
        tower [i] [j] = -1;
      }
    }

    for (byte i = 0; i < pieces; i++) {
      tower [0] [i] = i;
    }
    //finished initializing tower

    //Initializing display
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 51; j++) {
        display [i] [j] = -2;
      }
    }

    for (int i = 0; i < 51; i++) {
      display [0] [i] = -1;
    }

    for (int i = 1; i < 10; i++) {
      display [i] [8] = -1;
      display [i] [25] = -1;
      display [i] [42] = -1;
    }

    for (int i = 0; i < pieces; i++) {
      for (int j  = 8 - (sizes [i] / 2); j <= 8 + (sizes [i] / 2); j++) {
        display [i + 1] [j] = (byte) i;
      }
    }
    //Finished initializing display

    printDisplay ();
    scanner.nextLine ();
    solve (pieces, (byte) 0, (byte) 2);

    scanner.close ();
  }

  public static void clearScreen () {
    System.out.print("\033[H\033[2J");  
    System.out.flush ();
    System.out.print("\033[3J");
  }

  public static void printDisplay () {
    clearScreen ();

    for (int i = 9; i >= 0; i--) {
      System.out.print (" ");

      for (int j = 0; j < 51; j++) {

        if (display [i][j] >= 0) {
          System.out.print (colors [display [i] [j]]);
        } else if (display [i] [j] == -1) {
          System.out.print (towerSupport);
        } else {
          System.out.print (" ");
        }
      }

      System.out.println ();
    }

    System.out.println ();
  }

  public static void move (byte piece, byte destinationTower) {
    System.out.print ("\nPress enter to move: ");
    scanner.nextLine ();
    
    for (int i = 0; i < 3; i++) {
      if (i == destinationTower) {
        continue;
      }

      int j = pieces - 1;

      while (j >= 0 && tower [i] [j] == -1) {
        j--;
      }

      if (j < 0 || tower [i] [j] != piece) {
        continue;
      }

      tower [i] [j] = -1;

      for (int k = 17 * i + 8 - (sizes [piece] / 2); k <= 17 * i + 8 + (sizes [i] / 2); k++) {
        display [j + 1] [k] = -2;
      }

      display [j + 1] [17 * i + 8] = -1;

      break;
    }

    int i = pieces - 1;

    while (i >= 0 && tower [destinationTower] [i] < 0) {
      i--;
    }

    i++;

    tower [destinationTower] [i] = piece;

    for (int j = destinationTower * 17 + 8 - (sizes [piece] / 2); j <= destinationTower * 17 + 8 + (sizes [piece] / 2); j++) {
      display [i + 1] [j] = piece;
    }

    System.out.println (sound);
    printDisplay ();
  }

/*
  public static void debug () {
    for (int i = pieces - 1; i >= 0; i--) {
      for (int j = 0; j < 3; j++) {
        System.out.print (tower [j] [i] + " ");
      }

      System.out.println ();
    }
  }
*/

  public static void solve (byte blocks, byte current, byte destination) {
    if (blocks > 1) {
      solve ((byte) (blocks - 1), current, (byte) (3 - current - destination));
      move ((byte) (pieces - blocks), destination);
      solve ((byte) (blocks - 1), (byte) (3 - current - destination), destination);
    } else {
      move ((byte) (pieces - 1), destination);
    }
  }
}