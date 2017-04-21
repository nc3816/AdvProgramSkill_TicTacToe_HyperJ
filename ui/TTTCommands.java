// $Id: TTTCommands.java,v 1.1 2004/01/15 15:30:12 jeh Exp $

package ui;

import setup.TTTBoard;

/**
 * A class that allows users to enter text commands that mark squares in
 * a tic tac toe game
 */
public class TTTCommands {

   /**
    * The instance of the tic tac toe board
    */
   private TTTBoard board = null;

   /**
    * Initialize the tic tac toe board.
    *
    * @param size the desired height, and width, of the board
    * @param numPlayers how many players are in the game
    */
   public TTTCommands( int size, int numPlayers ) {
      board = new TTTBoard( size, numPlayers );
   }

   /**
    * Play the game by accepting square-marking commands.
    * The game can end in 3 ways: <ol>
    * <li>An input error ocurrs.</li>
    * <li>End-of-file is reached in the command stream (std. input).</li>
    * <li>The game is declared over by the rules concern.</li> </ol>
    */
   public void play() {
      java.util.Scanner cmdInput = null;
      try {
         cmdInput = new java.util.Scanner( System.in );
         System.out.print( "Enter row# col# player#: " );
         System.out.flush();
         while ( cmdInput.hasNext() ) {
            try {
               int row = cmdInput.nextInt();
               int col = cmdInput.nextInt();
               int player = cmdInput.nextInt();
               if ( board.badColumn( col ) ) {
                  System.out.println( "Bad column, " + col + ". Try again." );
               }
               else if ( board.badRow( row ) ) {
                  System.out.println( "Bad row, " + row + ". Try again." );
               }
               else if ( board.badPlayer( player ) ) {
                  System.out.println( "Bad player#, " + player +
                                      ". Try again." );
               }
               else if ( board.occupied( row, col ) ) {
                  System.out.println( "Occupied. Try again." );
               }
               else {
                  board.markSquare( row, col, player );
               }
               board.display();
            }
            catch( java.util.InputMismatchException ime ) {
               System.out.println( "All data must be integer. Try again." );
            }
            catch( java.util.NoSuchElementException ime ) {
               System.out.println( "Premature end of input." );
            }
            catch( TTTBoard.WrongPlayerException wpe ) {
               System.out.println( "It is not player #" + wpe.getPlayer() +
                                   "'s turn. Try again" );
            }
            cmdInput.nextLine();
            System.out.print( "Enter row# col# player#: " );
            System.out.flush();
         }
         System.out.println( "End of input. Halting\n" );
      }
      catch( TTTBoard.GameOverException goe ) {
         System.out.println( goe.getMessage() );
         System.out.println();
         board.display();
         System.out.println();
      }
      finally {
         if ( cmdInput != null ) {
            cmdInput.close();
         }
      }
   }

   /**
    * Play a Tic-Tac-Toe game.
    * Commands are entered by typing a line containing the
    * row number, column number, and player number (1 to N for N players).
    * The spot at that location will be marked with the player's number.
    *
    * @param args
    *   <br>First argument is the dimensions of the (square) board.
    *   <br>Second argument is the number of players.
    */
   public static void main( String[] args ) {
      int dim = 0;
      int nPlayers = 0;
      try {
         if ( args.length != 2 ) {
            throw new Exception();
         }
         dim = Integer.parseInt( args[0] );
         nPlayers = Integer.parseInt( args[1] );
      }
      catch( Throwable t ) {
         System.err.println( "usage: java TTTBoard size #players" );
         System.exit( 1 );
      }
      TTTCommands commander = new TTTCommands( dim, nPlayers );
      commander.play();
   }

} // TTTCommands
