import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import java.io.*;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Random;



public class chessComX extends JFrame
{
	
	
	
	private JLabel titleJLabel;
	
	private JButton rulesJButton;
	
	private JLabel whiteSideJLabel;
	private JComboBox whiteSideJComboBox;

	private JLabel blackSideJLabel;
	private JComboBox blackSideJComboBox;

	private JLabel comNotSupportedJLabel1;
	private JLabel comNotSupportedJLabel2;
	private JLabel comNotSupportedJLabel3;
	
	private JButton STARTGAMEJButton;
	
	
	
	
	
	private JTextArea rulesJTextArea;
	
	
	
	
	
	private JButton quitJButton;
	
	private JLabel gameLogJLabel;
	private JTextArea gameLogJTextArea;
	
	private JLabel helpJLabel;
	private JTextArea helpJTextArea;
	
	private JLabel[] topLetters = new JLabel[8];
	private JLabel[] leftNumbers = new JLabel[8];
	private JLabel[] bottomLetters = new JLabel[8];
	private JLabel[] rightNumbers = new JLabel[8];
	
	
	
	
	
	
	
	private int[][] theSpaces = { { -2, -3, -4, -5, -6, -4, -3, -2 }, { -1, -1, -1, -1, -1, -1, -1, -1 }, { 0,0,0,0,0,0,0,0 }, { 0,0,0,0,0,0,0,0 }, { 0,0,0,0,0,0,0,0 }, { 0,0,0,0,0,0,0,0 }, { 1, 1, 1, 1, 1, 1, 1, 1 }, { 2, 3, 4, 5, 6, 4, 3, 2 } };
	// Initialize the spaces on the board

	private JButton[][] theBoard = new JButton[8][8];
	
	private int boardX = 40;
	private int boardY = 60;
	private int boardW = 350;
	private int boardH = 350;
	
	private int boardJButtonSpacing = 3;
	
	private int spaceWidth = 0; // given relevant value by calculation
	private int spaceHeight = 0; // given relevant value by calculation
	
	private int turn = 0; // I'm thinking even numbers for white's turns, odd numbers for black's turns
	
	private int[] sides = { 0, 0 }; // I'm thinking 0 for human and 1 for random moves, 2 for computer thinking depth of 1, 3 for computer thinking depth of 2, etc; white is listed before black
	
	private final String[] abcdefgh = { "a", "b", "c", "d", "e", "f", "g", "h" };
	
	private int[] lastMovePawnDoubleStep = new int[2]; // {y, x} of landing square of double step, or {-1, -1}
	
	private boolean[][] hasMoved = { { false, false, false }, { false, false, false } }; // for castling   [0][0]-white left rook  [0][1]-white right rook  [0][2]-white king  [1][0]-black left rook  [1][1]-black right rook  [1][2]-black king
	
	private int[] selectedSpace = {-1, -1}; // {y, x} of selected space if there is one.  If there is not a selected space, {-1, -1}
	
	private int[][] movesOfSelectedPiece = new int[0][0]; // it will be assigned to an array of real length later
	
	//private int check = 0; // I'm thinking  0-none  1-white is in check  -1-black is in check
	// the check variable is not used as of me writing this, but the gameLog announces it when a king is in check
	
	private int numberOfPossibleMoves = 0; // taken after each move
	
	private final int[] pieceValues = { 0, 1, 5, 3, 3, 9, 3 }; // https://en.wikipedia.org/wiki/Chess_piece_relative_value
	
	private int pieceValueCoeficient = 3; // the piece values are multiplied by this
	
	private int[] moveValues = { 0, 1, 1, 2, 1, 1, 1 }; // the values of a move for each type of piece
	
	private int gameActivity = 0; // I'm thinking 0-playing 1-stalemate 2-checkmate white wins 3-checkmate black wins 4-only kings left
	
	
	
	
	private String[] humanOrComputer = { "Human Player", "Random Moves", "Computer Player 1", "Computer Player 2", "Computer Player 3", "Computer Player 4", "Computer Player 5"/*, "Computer Player X"*/ };
	
	private String gameLog = "";
	private String help = "";
	
	private String[] helpTexts = { "", "Pawn\nCan move\nbut not capture\none space\nstraight forward\nif clear.\nCan move\nand capture\n(must have\ncapture)\none space\ndiagonally\nforward.\nEach Pawn's first\nmove in the game\ncan go two spaces\nstraight forward\n(no capture).\nIf a Pawn reaches\nthe farthest\nrow of spaces\nit is replaced\nwith any other\ntype of piece\nbesides King\n(usually Queen).\nUnder complicated\nconditions can\nperform the\nEn Passant\ncapture.", "Rook\nMove horizontally\nor veritcally\nany number of\nclear spaces.\nCan capture\nopposing pieces\nat the end of the\nRook's range.\nCan be used\nby the King\nin the\n'castle' move.", "Knight\nJump two spaces\nhorizontally or\nvertically\nand then\none space in a\nperpindicular\ndirection.\nCan capture\nopposing pieces\non its\nlanding space.\nOnly the condition\nof the\nlanding space\ncan block\nthe Knight's\nmovement.", "Bishop\nMove diagonally\nany number of\nclear spaces.\nCan capture\nopposing pieces\nat the end of the\nBishop's range.", "Queen\nThe most powerful\npiece.\nCan move\nhorizontally,\nvertically, or\ndiagonally.\nCan capture any\nopposing piece\nat the end of the\nQueen's range.", "King\nThe critical piece.\nLosing the King\nmeans losing the\ngame.\nCan move and\ncapture one space\nhorizontally,\nvertically,\nor diagonally.\nCannot land on a\nspace that can be\ncaptured by the\nopponent on their\nimmediate turn.\nUnder complicated\nconditions can\nperform the\ncastle move,\nmoving two spaces." };
	
	
	private String mode = "menu"; // I'm thinking "menu", "rules", and "game" as of 4/18/2016
	
	
	
	
	private void createUserInterface()
	{
		getContentPane().removeAll();
		getContentPane().repaint();
		
		Container contentPane = getContentPane();
		contentPane.setLayout( null );
	
	

		titleJLabel = new JLabel();
		titleJLabel.setBounds( 250, 10, 100, 20 );
		titleJLabel.setText( "Chess" );
		titleJLabel.setHorizontalAlignment( JLabel.CENTER );
		titleJLabel.setFont( new Font( "Arial", Font.PLAIN, 18 ) );
		contentPane.add( titleJLabel );
		
		
		if( mode == "menu" )
		{
		
			rulesJButton = new JButton();
			rulesJButton.setBounds( 450, 3, 150, 30 );
			rulesJButton.setText( "Rules of Chess" );
			rulesJButton.setHorizontalAlignment( JButton.CENTER );
			rulesJButton.setFont( new Font( "Arial", Font.PLAIN, 12 ) );
			contentPane.add( rulesJButton );
			rulesJButton.addActionListener(
				new ActionListener(){
					public void actionPerformed( ActionEvent event )
					{
						mode = "rules";
						createUserInterface();
					}
				}
			);
			
			whiteSideJLabel = new JLabel();
			whiteSideJLabel.setBounds( 30, 70, 200, 20 );
			whiteSideJLabel.setText( "White Side" );
			whiteSideJLabel.setHorizontalAlignment( JLabel.CENTER );
			whiteSideJLabel.setFont( new Font( "Arial", Font.PLAIN, 18 ) );
			contentPane.add( whiteSideJLabel );
			
			whiteSideJComboBox = new JComboBox( humanOrComputer );
			whiteSideJComboBox.setBounds( 30, 95, 200, 30 );
			whiteSideJComboBox.setEnabled( true ); // enabling computer players on white side
			contentPane.add( whiteSideJComboBox );
			
			blackSideJLabel = new JLabel();
			blackSideJLabel.setBounds( 350, 70, 200, 20 );
			blackSideJLabel.setText( "Black Side" );
			blackSideJLabel.setHorizontalAlignment( JLabel.CENTER );
			blackSideJLabel.setFont( new Font( "Arial", Font.PLAIN, 18 ) );
			contentPane.add( blackSideJLabel );
			
			blackSideJComboBox = new JComboBox( humanOrComputer );
			blackSideJComboBox.setBounds( 350, 95, 200, 30 );
			blackSideJComboBox.setEnabled( true ); // enabling computer players on black side
			contentPane.add( blackSideJComboBox );
			
			
			comNotSupportedJLabel1 = new JLabel();
			comNotSupportedJLabel1.setBounds( 140, 150, 400, 19 );
			comNotSupportedJLabel1.setText( "Higher number computer players" );
			comNotSupportedJLabel1.setHorizontalAlignment( JLabel.LEFT );
			comNotSupportedJLabel1.setFont( new Font( "Arial", Font.PLAIN, 14 ) );
			contentPane.add( comNotSupportedJLabel1 );
			
			comNotSupportedJLabel2 = new JLabel();
			comNotSupportedJLabel2.setBounds( 140, 170, 400, 19 );
			comNotSupportedJLabel2.setText( "take longer to move.  Sadly, they are" );
			comNotSupportedJLabel2.setHorizontalAlignment( JLabel.LEFT );
			comNotSupportedJLabel2.setFont( new Font( "Arial", Font.PLAIN, 14 ) );
			contentPane.add( comNotSupportedJLabel2 );
			
			comNotSupportedJLabel3 = new JLabel();
			comNotSupportedJLabel3.setBounds( 140, 190, 400, 19 );
			comNotSupportedJLabel3.setText( "not necesarily more powerful players." );
			comNotSupportedJLabel3.setHorizontalAlignment( JLabel.LEFT );
			comNotSupportedJLabel3.setFont( new Font( "Arial", Font.PLAIN, 14 ) );
			contentPane.add( comNotSupportedJLabel3 );
			
		
		
			STARTGAMEJButton = new JButton();
			STARTGAMEJButton.setBounds( 100, 250, 400, 50 );
			STARTGAMEJButton.setHorizontalAlignment( JButton.CENTER );
			STARTGAMEJButton.setText( "START GAME" );
			contentPane.add( STARTGAMEJButton );
			STARTGAMEJButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed( ActionEvent event )
					{
						// initialize the game
						sides[ 0 ] = whiteSideJComboBox.getSelectedIndex();
						sides[ 1 ] = blackSideJComboBox.getSelectedIndex();
						
						
						
						int[][] reinitializationOfTheSpaces = { { -2, -3, -4, -5, -6, -4, -3, -2 }, { -1, -1, -1, -1, -1, -1, -1, -1 }, { 0,0,0,0,0,0,0,0 }, { 0,0,0,0,0,0,0,0 }, { 0,0,0,0,0,0,0,0 }, { 0,0,0,0,0,0,0,0 }, { 1, 1, 1, 1, 1, 1, 1, 1 }, { 2, 3, 4, 5, 6, 4, 3, 2 } };
						
						for( int y = 0; y<8; y++ )
						{
							for( int x = 0; x<8; x++ )
							{
								theSpaces[y][x] = reinitializationOfTheSpaces[y][x];
							}
						}
						
						
						turn = 0;
						
						gameActivity = 0;
						
						selectedSpace[0] = -1;
						selectedSpace[1] = -1;
						
						hasMoved[0][0] = false;
						hasMoved[0][1] = false;
						hasMoved[0][2] = false;
						hasMoved[1][0] = false;
						hasMoved[1][1] = false;
						hasMoved[1][2] = false;
						
						
						
						humanOrComputer[0] = "Human";
						humanOrComputer[1] = "Random";
						humanOrComputer[2] = "Com 1";
						humanOrComputer[3] = "Com 2";
						humanOrComputer[4] = "Com 3";
						humanOrComputer[5] = "Com 4";
						humanOrComputer[6] = "Com 5";
						//humanOrComputer[7] = "Com X";
						
						gameLog = "Game Started\nWhite side: " + (humanOrComputer[sides[0]]) + "\nBlack side: " +(humanOrComputer[sides[1]]) + "\n";
						if( true/*sides[0] == 0*/ ) // if white is a human
						{
							gameLog = gameLog + "White: ";
						}
						
						humanOrComputer[0] = "Human Player";
						humanOrComputer[1] = "Random Moves";
						humanOrComputer[2] = "Computer Player 1";
						humanOrComputer[3] = "Computer Player 2";
						humanOrComputer[4] = "Computer Player 3";
						humanOrComputer[5] = "Computer Player 4";
						humanOrComputer[6] = "Computer Player 5";
						//humanOrComputer[7] = "Computer Player X";
						
						
						
						
						
						if( sides[0] == 0 )
						{
							help = "White player,\nclick a piece.";
						}
						else
						{
							help = "The computer\nplayer is going.";
						}
						
						
						
						mode = "game";
						createUserInterface();
					}
				}
			);
					
		
		
		}
		else if( mode == "rules" )
		{
			
			if( rulesJButton.getText() == "Rules of Chess" )
			{
				rulesJButton = new JButton();
				rulesJButton.setBounds( 450, 3, 150, 30 );
				rulesJButton.setText( "Play a Game" );
				rulesJButton.setHorizontalAlignment( JButton.CENTER );
				rulesJButton.setFont( new Font( "Arial", Font.PLAIN, 12 ) );
				contentPane.add( rulesJButton );
				rulesJButton.addActionListener(
					new ActionListener(){
						public void actionPerformed( ActionEvent event )
						{
							mode = "menu";
							createUserInterface();
						}
					}
				);
				
				
				rulesJTextArea = new JTextArea();
				JScrollPane scrollBar = new JScrollPane( rulesJTextArea );
				scrollBar.setBounds( 30, 50, 560, 350 );
				contentPane.add( scrollBar );
			
			
				try(
					BufferedReader br = new BufferedReader( new FileReader("chessRules.txt") ) 
					)
					{
					String line = br.readLine();
			
					while( line != null )
					{
						rulesJTextArea.append( line );
						rulesJTextArea.append( "\n" );
						line = br.readLine();
					}
					
					rulesJTextArea.setCaretPosition( 0 );
				}
				catch( Exception e )
				{
					rulesJTextArea.append( "There was an exception" );
				}
			
			
			
			}
			else
			{
				
				createUserInterface();
				
			}
			
		}
		else if( mode == "game" )
		{
			
			
			quitJButton = new JButton();
			quitJButton.setBounds( 450, 3, 150, 30 );
			quitJButton.setText( "Quit Game" );
			quitJButton.setHorizontalAlignment( JButton.CENTER );
			quitJButton.setFont( new Font( "Arial", Font.PLAIN, 12 ) );
			contentPane.add( quitJButton );
			quitJButton.addActionListener(
				new ActionListener(){
					public void actionPerformed( ActionEvent event )
					{
						
						
						if( quitJButton.getText().equals( "Quit Game" ) )
						{
							String input = JOptionPane.showInputDialog( null, "Are you sure you want to quit?\nEnter y for Yes or n for No." );
							if( input.equals( "y" ) || input.equals( "Y" ) )
							{
								mode = "menu";
								createUserInterface();
							}
						}
						else
						{
							mode = "menu";
							createUserInterface();
						}
						
					
					}
				}
			);
			
			
			gameLogJLabel = new JLabel();
			gameLogJLabel.setBounds( 410, 40, 175, 19 );
			gameLogJLabel.setText( "Game Log" );
			gameLogJLabel.setFont( new Font( "Arial", Font.PLAIN, 18 ) );
			contentPane.add( gameLogJLabel );
			
			gameLogJTextArea = new JTextArea();
			gameLogJTextArea.setFont( new Font( "Arial", Font.PLAIN, 18 ) );
			gameLogJTextArea.setEditable( false );
			gameLogJTextArea.setText( gameLog ); // set gameLogJTextArea's text to the instance String gameLog
			JScrollPane gameLogJScrollPane = new JScrollPane( gameLogJTextArea );
			gameLogJScrollPane.setBounds( 410, 60, 175, 200 );
			contentPane.add( gameLogJScrollPane );
			
			
			helpJLabel = new JLabel();
			helpJLabel.setBounds( 410, 270, 175, 19 );
			helpJLabel.setText( "Help" );
			helpJLabel.setFont( new Font( "Arial", Font.PLAIN, 18 ) );
			contentPane.add( helpJLabel );
			
			helpJTextArea = new JTextArea();
			helpJTextArea.setFont( new Font( "Arial", Font.PLAIN, 18 ) );
			helpJTextArea.setEditable( false );
			helpJTextArea.setText( help );
			helpJTextArea.setCaretPosition(0);
			JScrollPane helpJScrollPane = new JScrollPane( helpJTextArea );
			helpJScrollPane.setBounds( 410, 290, 175, 120 );
			contentPane.add( helpJScrollPane );
			
			
			
			
			
			
			
			spaceWidth = (boardW - (7*boardJButtonSpacing)) / 8;
			spaceHeight = (boardH - (7*boardJButtonSpacing)) / 8;
			
			// draw the board
			for( int y=0; y<8; y++ )
			{
				for( int x=0; x<8; x++ )
				{
					final int fx = x;
					final int fy = y;
					
					theBoard[y][x] = new JButton();
					theBoard[y][x].setBounds( 
						boardX + (x * (spaceWidth + boardJButtonSpacing) ),
						boardY + (y * (spaceHeight + boardJButtonSpacing) ),
						spaceWidth,
						spaceHeight
					);
					ImageIcon image = new ImageIcon( "gr/" + String.valueOf( theSpaces[y][x] ) + ".png" );
					theBoard[y][x].setIcon( image );
					theBoard[y][x].setEnabled( true );
					theBoard[y][x].setMargin(new Insets(0,0,0,0));
					contentPane.add( theBoard[y][x] );
					theBoard[y][x].addActionListener(
					
						new ActionListener()
						{
							public void actionPerformed( ActionEvent event )
							{
								
								boardJButtonActionPerformed( event, fx, fy );
								
							}
							
						}
					
					);
					
					
					
				}
			}
			
			
			
			// draw the column and row JLabels
			for( int columnOrRow = 0; columnOrRow<8; columnOrRow++ )
			{
				topLetters[columnOrRow] = new JLabel();
				topLetters[columnOrRow].setBounds( 
					( ( ( spaceWidth + boardJButtonSpacing ) * columnOrRow ) + ( spaceWidth / 2 ) + boardX - 5  ),
					boardY - 15,
					10, 
					13
				);
				topLetters[columnOrRow].setText( abcdefgh[ columnOrRow ] );
				topLetters[columnOrRow].setFont( new Font( "Arial", Font.PLAIN, 12 ) );
				topLetters[columnOrRow].setHorizontalAlignment( JLabel.CENTER );
				contentPane.add( topLetters[columnOrRow] );
				
				
				bottomLetters[columnOrRow] = new JLabel();
				bottomLetters[columnOrRow].setBounds( 
					( ( ( spaceWidth + boardJButtonSpacing ) * columnOrRow ) + ( spaceWidth / 2 ) + boardX - 5  ),
					boardY + boardH + 2,
					10, 
					13
				);
				bottomLetters[columnOrRow].setText( abcdefgh[ columnOrRow ] );
				bottomLetters[columnOrRow].setFont( new Font( "Arial", Font.PLAIN, 12 ) );
				bottomLetters[columnOrRow].setHorizontalAlignment( JLabel.CENTER );
				contentPane.add( bottomLetters[columnOrRow] );
				
				
				
				leftNumbers[columnOrRow] = new JLabel();
				leftNumbers[columnOrRow].setBounds( 
					boardX - 12,
					( ( ( spaceHeight + boardJButtonSpacing ) * columnOrRow ) + ( spaceHeight / 2 ) + boardY - 6  ),
					10, 
					13
				);
				leftNumbers[columnOrRow].setText( String.valueOf( 8 - columnOrRow ) );
				leftNumbers[columnOrRow].setFont( new Font( "Arial", Font.PLAIN, 12 ) );
				leftNumbers[columnOrRow].setHorizontalAlignment( JLabel.CENTER );
				contentPane.add( leftNumbers[columnOrRow] );
				
				
				rightNumbers[columnOrRow] = new JLabel();
				rightNumbers[columnOrRow].setBounds( 
					boardX + boardW + 2,
					( ( ( spaceHeight + boardJButtonSpacing ) * columnOrRow ) + ( spaceHeight / 2 ) + boardY - 6  ),
					10, 
					13
				);
				rightNumbers[columnOrRow].setText( String.valueOf( 8 - columnOrRow ) );
				rightNumbers[columnOrRow].setFont( new Font( "Arial", Font.PLAIN, 12 ) );
				rightNumbers[columnOrRow].setHorizontalAlignment( JLabel.CENTER );
				contentPane.add( rightNumbers[columnOrRow] );
			}
			
			
			
			
			
			
			
			/*
			
			
			
			// the test for the glitch with arrays came out positive
			// (the glitch exists)
			
			// iterating through and setting the values one by one evades the glitch
			
			
			int[][] testArray1 = {{0, 1}, {2, 3}};
			int[][] testArray2 = new int[2][2];
			//System.arraycopy( testArray1, 0, testArray2, 0, testArray2.length );
			
			for( int p1 = 0; p1<testArray1.length; p1++ )
			{
				for( int p2 = 0; p2<testArray1[p1].length; p2++ )
				{
					testArray2[p1][p2] = testArray1[p1][p2];
				}
			}
			
			testArray2[1][0] = 22;
			
			
			System.out.print( testArray1[0][0] + " " );
			System.out.print( testArray1[0][1] + " " );
			System.out.print( testArray1[1][0] + " " );
			System.out.println( testArray1[1][1] + " " );
			
			System.out.print( testArray2[0][0] + " " );
			System.out.print( testArray2[0][1] + " " );
			System.out.print( testArray2[1][0] + " " );
			System.out.println( testArray2[1][1] + " " );
			
			
			
			boolean kingIsSafe = 
				spaceIsSafe( 
					theSpaces, 
					4,
					7,
					1 
				);
			System.out.println( String.valueOf( kingIsSafe ) );	
			
			int[][] whiteKingsMoves = getMovesForPieceAt( theSpaces, 4, 7, true, hasMoved, lastMovePawnDoubleStep );
			for( int k = 0; k<whiteKingsMoves.length; k++ )
			{
				System.out.println( "White King Move X: " + String.valueOf( whiteKingsMoves[k][0] ) + "  Y: " + String.valueOf( whiteKingsMoves[k][1] ) );
			}
			*/
			
			
			
			
			boolean[] onlyKingLeft = { true, true };
			
			
			if( gameActivity == 0 ) // if no checkmate or stalemate
			{
			
			
			
				numberOfPossibleMoves = 0;
				
				
				for( int y=0; y<8; y++ )
				{
					for( int x=0; x<8; x++ )
					{
						
						
						
						if( theSpaces[y][x] * (1-(2*(turn % 2))) > 0 )
						{
							
							if( getMovesForPieceAt( theSpaces, x, y, true, hasMoved, lastMovePawnDoubleStep ).length <= 0 )
							{
								theBoard[y][x].setBackground( Color.WHITE );
								theBoard[y][x].setOpaque(false);
								
								
								// helpJTextArea.append( String.valueOf( getMovesForPieceAt( theSpaces, x, y, true, hasMoved, lastMovePawnDoubleStep ).length ) + "\n" );
							}
							else
							{
								theBoard[y][x].setBackground( Color.YELLOW );
								theBoard[y][x].setOpaque(true);
								
								numberOfPossibleMoves++;
							}
							
							if( Math.abs( theSpaces[y][x] ) != 6 )
							{
								onlyKingLeft[ turn % 2 ] = false;
							}
							
						} // end if( theSpaces[y][x] > 0 )
						else
						{
							theBoard[y][x].setBackground( Color.WHITE );
							theBoard[y][x].setOpaque(false);
							
							if( (Math.abs( theSpaces[y][x] ) != 6) && ( theSpaces[y][x] != 0) )
							{
								onlyKingLeft[ 1 - (turn % 2) ] = false;
							}
						}
						
						
					}
				}
				
				
				if( onlyKingLeft[0] && onlyKingLeft[1] ) // if only kings are left
				{
					gameActivity = 4;
					numberOfPossibleMoves = 0;
				}
				
				
				
				
				
				
				if( numberOfPossibleMoves > 0 )
				{
				
				
				
				
			
					if( sides[turn % 2] == 0 ) // if this side is a human player
					{
						
						
						if( (selectedSpace[0] != -1) || (selectedSpace[1] != -1 ) )
						{
							movesOfSelectedPiece = getMovesForPieceAt( theSpaces, selectedSpace[1], selectedSpace[0], true, hasMoved, lastMovePawnDoubleStep );
						
							for( int k = 0; k<movesOfSelectedPiece.length; k++ )
							{
								
								theBoard[ movesOfSelectedPiece[k][1] ][ movesOfSelectedPiece[k][0] ].setBackground( Color.GREEN );
								theBoard[ movesOfSelectedPiece[k][1] ][ movesOfSelectedPiece[k][0] ].setOpaque( true );
								
								theBoard[ selectedSpace[0]][ selectedSpace[1] ].setBackground( Color.GREEN );
								
							}
						}
						
						
					} // end if( sides[turn % 2] == 0 )
					else // if this side is a computer player
					{
						
						
						
						try
						{
							ActionListener taskPerformer = new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									
									// this is just a barrier against the list glitch
									boolean[][] listGlitchBarrierHasMoved = new boolean[2][3];
									listGlitchBarrierHasMoved[0][0] = hasMoved[0][0];
									listGlitchBarrierHasMoved[0][1] = hasMoved[0][1];
									listGlitchBarrierHasMoved[0][2] = hasMoved[0][2];
									listGlitchBarrierHasMoved[1][0] = hasMoved[1][0];
									listGlitchBarrierHasMoved[1][1] = hasMoved[1][1];
									listGlitchBarrierHasMoved[1][2] = hasMoved[1][2];
									
									// this is just a barrier against the list glitch
									int[] listGlitchBarrierLastMovePawnDoubleStep = new int[2];
									listGlitchBarrierLastMovePawnDoubleStep[0] = lastMovePawnDoubleStep[0];
									listGlitchBarrierLastMovePawnDoubleStep[1] = lastMovePawnDoubleStep[1];
									
									
									
									
									int[] theComputersMove = AIMove( 
										theSpaces, 
										1-(2*(turn%2)), 
										sides[turn%2] - 1, 
										3,
										listGlitchBarrierHasMoved,
										listGlitchBarrierLastMovePawnDoubleStep
									); // Here the computer chooses the move (this statement may cause lag or other problems)
									
									
									
									System.out.println( "Com Move Rating: " + theComputersMove[7] );
									System.out.println( "Com Move starting X: " + theComputersMove[0] );
									System.out.println( "Com Move starting Y: " + theComputersMove[1] );
									System.out.println( "Com Move landing X: " + theComputersMove[2] );
									System.out.println( "Com Move landing Y: " + theComputersMove[3] );
									System.out.println( "Com Move capturing X: " + theComputersMove[4] );
									System.out.println( "Com Move capturing Y: " + theComputersMove[5] );
									
									selectedSpace[ 0 ] = theComputersMove[1];
									selectedSpace[ 1 ] = theComputersMove[0];
									
									computerPlayerMakeMove( theComputersMove );
									
								}
							};
							Timer timer = new Timer(50 ,taskPerformer);
							timer.setRepeats(false);
							timer.start();
						}
						catch( Exception e )
						{
							System.out.println( "Exception!!!!!" + String.valueOf( e ) );
						}
						
						
						
						
						
						
						
						
						
						
					}
				
				
				
				
				
				} // end if( numberOfPossibleMoves > 0 )
			
			
			
			
				/*
				for( int y = 0; y<8; y++ )
				{
					for( int x = 0; x<8; x++ )
					{
						System.out.println( theSpaces[y][x] );
					}
				}
				*/
				
				// System.out.println( String.valueOf( numberOfPossibleMoves ) );
				
				
				/*
				if( numberOfPossibleMoves == 0 )
				{
					int[][] kingLocations = {{7, 4}, {0, 4}}; // These values are to be changed.  They are to follow the format  [0][0]-white Y  [0][1]-white X  [1][0]-black Y  [1][1]-black X
				
					// scan for the kings
					for( int currentY = 0; currentY<8; currentY++ )
					{
						for( int currentX = 0; currentX<8; currentX++ )
						{
							if( theSpaces[currentY][currentX] == 6 )
							{
								kingLocations[0][0] = currentY;
								kingLocations[0][1] = currentX;
							}
							else if( theSpaces[currentY][currentX] == -6 )
							{
								kingLocations[1][0] = currentY;
								kingLocations[1][1] = currentX;
							}
						}
					}
					
					if( 
						spaceIsSafe( 
							theSpaces, 
							kingLocations[ turn % 2 ][1], 
							kingLocations[ turn % 2 ][0], 
							1 - (2 * ( turn % 2 ) ) 
						) == false
					)
					{
						System.out.println( "Checkmate" );
						
						String[] whiteBlack = { "White", "Black" };
						gameLog = gameLog + "Checkmate\n" + whiteBlack[ 1 - (turn % 2) ] + " Wins\n";
						
						gameActivity = 2 + (1 - (turn % 2));
						
						gameLogJTextArea.setText( gameLog );
					}
				}
				*/
			
			
			
			} // end if( gameActivity == 0 )
			
			
			
			
			
			
		} // end else if( mode == "game" )
		
		
		
		
		
		
		
		
		setTitle( "Chess" );
		setSize( 640, 480 );
		setVisible( true );
	}

	
	
	
	
	
	
	
	
	
	
	
	// I'm thinking  int [ move index ] [ x, y, captX(-1 with no capture), captY(-1 with no capture), 0-no castle 1-left castle 2-right castle ] 
	private int[][] getMovesForPieceAt( int[][] board, int x, int y, boolean notJustCaptures, boolean[][] castlingHasMoved, int[] enPassantLastMovePawnDoubleStep )
	{
		
		int pieceType = board[y][x];
		
		int[][] returnedArray = new int[0][0];
		
		
		
		// blank space (shouldn't be used, but I'm putting it in anyway)
		if( pieceType == 0 )
		{
			
		}
		
		
		
		
		// pawn
		if( Math.abs( pieceType ) == 1 )
		{
					


			// these are non-capturing moves
			if( notJustCaptures == true )
			{
				
				
		
				// pawn step
				try // With what I'm thinking, this try won't be necessary, but I'm putting it in anyway
				{
					if( board[y - pieceType][x] == 0 )
					{
						int[][] add = { { x, y - pieceType, -1, -1, 0 } };
						int addLen = add.length;
						int returnedArrayLen = returnedArray.length;
						int[][] draftArray = new int[ add.length + returnedArray.length ][5];
						System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
						System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
						returnedArray = new int[ add.length + returnedArray.length ][5];
						returnedArray = draftArray;
					}
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					
				}
				
				// pawn double step
				try
				{
					if( ( board[y - pieceType][x] == 0 ) && ( board[y - ( 2 * pieceType ) ][x] == 0 ) && ( y == ( (pieceType * 2.5) + 3.5 ) ) )
					{
						int[][] add = { { x, y - (2 * pieceType), -1, -1, 0 } };
						int addLen = add.length;
						int returnedArrayLen = returnedArray.length;
						int[][] draftArray = new int[ add.length + returnedArray.length ][5];
						System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
						System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
						returnedArray = new int[ add.length + returnedArray.length ][5];
						returnedArray = draftArray;
					}
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					
				}
				
				
			
			} // end if( notJustCaptures == true )
			
			
			
			
			
			// pawn capture to the left
			try
			{
				if( board[y - pieceType][x - 1] * pieceType < 0 )
				{
					int[][] add = { { x - 1, y - pieceType, x - 1, y - pieceType, 0 } };
					int addLen = add.length;
					int returnedArrayLen = returnedArray.length;
					int[][] draftArray = new int[ add.length + returnedArray.length ][5];
					System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
					System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
					returnedArray = new int[ add.length + returnedArray.length ][5];
					returnedArray = draftArray;
				}
			}
			catch( ArrayIndexOutOfBoundsException e )
			{
				
			}
			
			// pawn capture to the right
			try
			{
				if( board[y - pieceType][x + 1] * pieceType < 0 )
				{
					int[][] add = { { x + 1, y - pieceType, x + 1, y - pieceType, 0 } };
					int addLen = add.length;
					int returnedArrayLen = returnedArray.length;
					int[][] draftArray = new int[ add.length + returnedArray.length ][5];
					System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
					System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
					returnedArray = new int[ add.length + returnedArray.length ][5];
					returnedArray = draftArray;
				}
			}
			catch( ArrayIndexOutOfBoundsException e )
			{
				
			}
			
			// En Passant to the left
			try
			{
				if( 
					( y == 3.5 - (0.5 * pieceType) ) // on 5th rank
					&& ( board[y][x - 1] * pieceType == -1 ) // target pawn is there
					&& ( ( enPassantLastMovePawnDoubleStep[0] == y ) && ( enPassantLastMovePawnDoubleStep[1] == x - 1 ) ) // captured pawn just double stepped
					&& ( board[y - pieceType][x - 1] == 0 ) // landing space is clear
				)
				{
					int[][] add = { { x - 1, y - pieceType, x - 1, y, 0 } };
					int addLen = add.length;
					int returnedArrayLen = returnedArray.length;
					int[][] draftArray = new int[ add.length + returnedArray.length ][5];
					System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
					System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
					returnedArray = new int[ add.length + returnedArray.length ][5];
					returnedArray = draftArray;
				}
			}
			catch( ArrayIndexOutOfBoundsException e )
			{
				
			}
			
			// En Passant to the right
			try
			{
				if( 
					( y == 3.5 - (0.5 * pieceType) ) // on 5th rank
					&& ( board[y][x + 1] * pieceType == -1 ) // target pawn is there
					&& ( ( enPassantLastMovePawnDoubleStep[0] == y ) && ( enPassantLastMovePawnDoubleStep[1] == x + 1 ) ) // captured pawn just double stepped
					&& ( board[y - pieceType][x + 1] == 0 ) // landing space is clear
				)
				{
					int[][] add = { { x + 1, y - pieceType, x + 1, y, 0 } };
					int addLen = add.length;
					int returnedArrayLen = returnedArray.length;
					int[][] draftArray = new int[ add.length + returnedArray.length ][5];
					System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
					System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
					returnedArray = new int[ add.length + returnedArray.length ][5];
					returnedArray = draftArray;
				}
			}
			catch( ArrayIndexOutOfBoundsException e )
			{
				
			}
			
		} // end pawn
		
		
		
		
		// rook
		if( Math.abs( pieceType ) == 2 )
		{
			
			int spaceSenseX = x;
			int spaceSenseY = y;
			
			int spaceSenseDirection = 0; // for Rook: 0-left 1-right 2-up 3-down 4-done
			
			boolean spaceSenseClear = true;
			

			while( spaceSenseDirection < 4 )
			{
				
				
				if( spaceSenseDirection == 0 ) // left
				{
					spaceSenseX = spaceSenseX - 1;
				}
				if( spaceSenseDirection == 1 ) // right
				{
					spaceSenseX = spaceSenseX + 1;
				}
				if( spaceSenseDirection == 2 ) // up
				{
					spaceSenseY = spaceSenseY - 1;
				}
				if( spaceSenseDirection == 3 ) // down
				{
					spaceSenseY = spaceSenseY + 1;
				}
				
				
				
				
				// clear space
				try
				{
					if( board[spaceSenseY][spaceSenseX] == 0 )
					{
						
						if( notJustCaptures == true )
						{
							int[][] add = { { spaceSenseX, spaceSenseY, -1, -1, 0 } };
							int addLen = add.length;
							int returnedArrayLen = returnedArray.length;
							int[][] draftArray = new int[ add.length + returnedArray.length ][5];
							System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
							System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
							returnedArray = new int[ add.length + returnedArray.length ][5];
							returnedArray = draftArray;
						} // end if( notJustCaptures == true )
						
					}
					else // freindly piece or opposing piece
					{
						spaceSenseClear = false;
					}
				}
				catch( Exception e )
				{
					spaceSenseClear = false;
				}
				
				
				
				// opposing piece
				try
				{
					if( board[spaceSenseY][spaceSenseX] * (pieceType / 2) < 0 )
					{
						int[][] add = { { spaceSenseX, spaceSenseY, spaceSenseX, spaceSenseY, 0 } };
						int addLen = add.length;
						int returnedArrayLen = returnedArray.length;
						int[][] draftArray = new int[ add.length + returnedArray.length ][5];
						System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
						System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
						returnedArray = new int[ add.length + returnedArray.length ][5];
						returnedArray = draftArray;
						
						spaceSenseClear = false;
					}
				}
				catch( Exception e )
				{
					spaceSenseClear = false;
				}
				
				
				
				if( spaceSenseClear == false )
				{
					spaceSenseDirection += 1;
					spaceSenseClear = true;
					spaceSenseX = x;
					spaceSenseY = y;
				}
				
			} // end while
			
			
		} // end rook
		
		
		
		
		// knight
		if( Math.abs( pieceType ) == 3 )
		{
			int[][] knightMoves = {{-2, -1}, {-2, 1}, {-1, 2}, {1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}};
			
			for( int k = 0; k<8; k++ )
			{
				
				
				
				if( notJustCaptures == true ) // these are non-capturing moves
				{
				
				
				
					// jump to clear space
					try
					{
						if( board[y + knightMoves[k][0]][x + knightMoves[k][1]] == 0 )
						{
							
							if( (x + knightMoves[k][1] < 8) && (x + knightMoves[k][1] > -1) && (y + knightMoves[k][0] < 8) && (y + knightMoves[k][0] > -1) )
							{
								int[][] add = { { x + knightMoves[k][1], y + knightMoves[k][0], -1, -1, 0 } };
								int addLen = add.length;
								int returnedArrayLen = returnedArray.length;
								int[][] draftArray = new int[ add.length + returnedArray.length ][5];
								System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
								System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
								returnedArray = new int[ add.length + returnedArray.length ][5];
								returnedArray = draftArray;
							}
							
						}
					}
					catch( ArrayIndexOutOfBoundsException e )
					{
						
					}
				
				
				
				} // end if( notJustCaptures == true )
				
				
				
				
				// jump and capture enemy piece
				try
				{
					if( board[y + knightMoves[k][0]][x + knightMoves[k][1]] * (pieceType / 3) < 0 )
					{
						
						if( (x + knightMoves[k][1] < 8) && (x + knightMoves[k][1] > -1) && (y + knightMoves[k][0] < 8) && (y + knightMoves[k][0] > -1) )
						{
							int[][] add = { { x + knightMoves[k][1], y + knightMoves[k][0], x + knightMoves[k][1], y + knightMoves[k][0], 0 } };
							int addLen = add.length;
							int returnedArrayLen = returnedArray.length;
							int[][] draftArray = new int[ add.length + returnedArray.length ][5];
							System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
							System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
							returnedArray = new int[ add.length + returnedArray.length ][5];
							returnedArray = draftArray;
						}
						
					}
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					
				}
				
			} // end for
			
		} // end Knight
		
		
		
		
		// Bishop
		if( Math.abs( pieceType ) == 4 )
		{
			int spaceSenseX = x;
			int spaceSenseY = y;
			
			int spaceSenseDirection = 0; // for Bishop: 0-up/left 1-up/right 2-down/right 3-down/left 4-done
			
			boolean spaceSenseClear = true;
			
			while( spaceSenseDirection < 4 )
			{
				if( spaceSenseDirection == 0 ) // up/left
				{
					spaceSenseX = spaceSenseX - 1;
					spaceSenseY = spaceSenseY - 1;
				}
				if( spaceSenseDirection == 1 ) // up/right
				{
					spaceSenseX = spaceSenseX + 1;
					spaceSenseY = spaceSenseY - 1;
				}
				if( spaceSenseDirection == 2 ) // down/right
				{
					spaceSenseX = spaceSenseX + 1;
					spaceSenseY = spaceSenseY + 1;
				}
				if( spaceSenseDirection == 3 ) // down/left
				{
					spaceSenseX = spaceSenseX - 1;
					spaceSenseY = spaceSenseY + 1;
				}
				
				
				
				
				// clear space
				try
				{
					if( board[spaceSenseY][spaceSenseX] == 0 )
					{
						
						if( notJustCaptures == true ) // these are non-capturing moves
						{
							int[][] add = { { spaceSenseX, spaceSenseY, -1, -1, 0 } };
							int addLen = add.length;
							int returnedArrayLen = returnedArray.length;
							int[][] draftArray = new int[ add.length + returnedArray.length ][5];
							System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
							System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
							returnedArray = new int[ add.length + returnedArray.length ][5];
							returnedArray = draftArray;
						} // end if( notJustCaptures == true )
							
					}
					else // freindly piece
					{
						spaceSenseClear = false;
					}
				}
				catch( Exception e )
				{
					spaceSenseClear = false;
				}
				
				
				
				// opposing piece
				try
				{
					if( board[spaceSenseY][spaceSenseX] * (pieceType / 4) < 0 )
					{
						int[][] add = { { spaceSenseX, spaceSenseY, spaceSenseX, spaceSenseY, 0 } };
						int addLen = add.length;
						int returnedArrayLen = returnedArray.length;
						int[][] draftArray = new int[ add.length + returnedArray.length ][5];
						System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
						System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
						returnedArray = new int[ add.length + returnedArray.length ][5];
						returnedArray = draftArray;
						
						spaceSenseClear = false;
					}
				}
				catch( Exception e )
				{
					spaceSenseClear = false;
				}
				
				
				
				if( spaceSenseClear == false )
				{
					spaceSenseDirection += 1;
					spaceSenseClear = true;
					spaceSenseX = x;
					spaceSenseY = y;
				}
			} // end while
			
		} // end Bishop
		
		
		
		
		// Queen
		if( Math.abs(pieceType) == 5 )
		{
			
			
			int spaceSenseX = x;
			int spaceSenseY = y;
			
			int spaceSenseDirection = 0; // for Rook: 0-left 1-right 2-up 3-down 4-done
			
			boolean spaceSenseClear = true;
			

			while( spaceSenseDirection < 4 )
			{
				
				
				if( spaceSenseDirection == 0 ) // left
				{
					spaceSenseX = spaceSenseX - 1;
				}
				if( spaceSenseDirection == 1 ) // right
				{
					spaceSenseX = spaceSenseX + 1;
				}
				if( spaceSenseDirection == 2 ) // up
				{
					spaceSenseY = spaceSenseY - 1;
				}
				if( spaceSenseDirection == 3 ) // down
				{
					spaceSenseY = spaceSenseY + 1;
				}
				
				
				
				
				// clear space
				try
				{
					if( board[spaceSenseY][spaceSenseX] == 0 )
					{
						
						if( notJustCaptures == true )
						{
							int[][] add = { { spaceSenseX, spaceSenseY, -1, -1, 0 } };
							int addLen = add.length;
							int returnedArrayLen = returnedArray.length;
							int[][] draftArray = new int[ add.length + returnedArray.length ][5];
							System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
							System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
							returnedArray = new int[ add.length + returnedArray.length ][5];
							returnedArray = draftArray;
						} // end if( notJustCaptures == true )
						
					}
					else // freindly piece or opposing piece
					{
						spaceSenseClear = false;
					}
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					spaceSenseClear = false;
				}
				
				
				
				// opposing piece
				try
				{
					if( board[spaceSenseY][spaceSenseX] * (pieceType / 2) < 0 )
					{
						int[][] add = { { spaceSenseX, spaceSenseY, spaceSenseX, spaceSenseY, 0 } };
						int addLen = add.length;
						int returnedArrayLen = returnedArray.length;
						int[][] draftArray = new int[ add.length + returnedArray.length ][5];
						System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
						System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
						returnedArray = new int[ add.length + returnedArray.length ][5];
						returnedArray = draftArray;
						
						spaceSenseClear = false;
					}
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					spaceSenseClear = false;
				}
				
				
				
				if( spaceSenseClear == false )
				{
					spaceSenseDirection += 1;
					spaceSenseClear = true;
					spaceSenseX = x;
					spaceSenseY = y;
				}
				
			} // end while
			
			
			
			
			
			spaceSenseDirection = 0; // for Bishop: 0-up/left 1-up/right 2-down/right 3-down/left 4-done
			
			
			while( spaceSenseDirection < 4 )
			{
				if( spaceSenseDirection == 0 ) // up/left
				{
					spaceSenseX = spaceSenseX - 1;
					spaceSenseY = spaceSenseY - 1;
				}
				if( spaceSenseDirection == 1 ) // up/right
				{
					spaceSenseX = spaceSenseX + 1;
					spaceSenseY = spaceSenseY - 1;
				}
				if( spaceSenseDirection == 2 ) // down/right
				{
					spaceSenseX = spaceSenseX + 1;
					spaceSenseY = spaceSenseY + 1;
				}
				if( spaceSenseDirection == 3 ) // down/left
				{
					spaceSenseX = spaceSenseX - 1;
					spaceSenseY = spaceSenseY + 1;
				}
				
				
				
				
				// clear space
				try
				{
					if( board[spaceSenseY][spaceSenseX] == 0 )
					{
						
						if( notJustCaptures == true )
						{
							int[][] add = { { spaceSenseX, spaceSenseY, -1, -1, 0 } };
							int addLen = add.length;
							int returnedArrayLen = returnedArray.length;
							int[][] draftArray = new int[ add.length + returnedArray.length ][5];
							System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
							System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
							returnedArray = new int[ add.length + returnedArray.length ][5];
							returnedArray = draftArray;
						}
						
					}
					else // freindly piece or opposing piece
					{
						spaceSenseClear = false;
					}
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					spaceSenseClear = false;
				}
				
				
				
				// opposing piece
				try
				{
					if( board[spaceSenseY][spaceSenseX] * (pieceType / 4) < 0 )
					{
						int[][] add = { { spaceSenseX, spaceSenseY, spaceSenseX, spaceSenseY, 0 } };
						int addLen = add.length;
						int returnedArrayLen = returnedArray.length;
						int[][] draftArray = new int[ add.length + returnedArray.length ][5];
						System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
						System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
						returnedArray = new int[ add.length + returnedArray.length ][5];
						returnedArray = draftArray;
						
						spaceSenseClear = false;
					}
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					spaceSenseClear = false;
				}
				
				
				
				if( spaceSenseClear == false )
				{
					spaceSenseDirection += 1;
					spaceSenseClear = true;
					spaceSenseX = x;
					spaceSenseY = y;
				}
			} // end while
			
			
		} // end queen
		
		
		
		
		// King
		if( Math.abs( pieceType ) == 6 )
		{
			
			// the 8 one-space moves
			int[][] king8dirMoves = { {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1} };
			
			for( int k = 0; k<8; k++ )
			{
				
				
				
				if( notJustCaptures == true ) // these are non-capturing moves
				{
				
				
					// empty space
					try
					{
						if( board[y + king8dirMoves[k][0]][x + king8dirMoves[k][1]] == 0 )
						{
							//System.out.println( "the king has an open space move (with exception of check)" );
							
							int[][] add = { { x + king8dirMoves[k][1], y + king8dirMoves[k][0], -1, -1, 0 } };
							int addLen = add.length;
							int returnedArrayLen = returnedArray.length;
							int[][] draftArray = new int[ add.length + returnedArray.length ][5];
							System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
							System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
							returnedArray = new int[ add.length + returnedArray.length ][5];
							returnedArray = draftArray;
						}
					}
					catch( ArrayIndexOutOfBoundsException e )
					{
						
					}
				
				
				} // end if( notJustCaptures == true )
				
				
				
				// opposing piece
				try
				{
					if( board[y + king8dirMoves[k][0]][x + king8dirMoves[k][1]] * (pieceType / 6) < 0 )
					{
						int[][] add = { { x + king8dirMoves[k][1], y + king8dirMoves[k][0], x + king8dirMoves[k][1], y + king8dirMoves[k][0], 0 } };
						int addLen = add.length;
						int returnedArrayLen = returnedArray.length;
						int[][] draftArray = new int[ add.length + returnedArray.length ][5];
						System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
						System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
						returnedArray = new int[ add.length + returnedArray.length ][5];
						returnedArray = draftArray;
					}
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					
				}
				
			} // end for
			
			
			
			// I had this stackOverflowError problem and I'm hoping this if fixes it
			if( notJustCaptures == true )
			{
				
				// left castle
				try
				{
					
					boolean clearPathForCastling = true;
					
					for( int k = x; k > 0; k-- )
					{
						if( (board[y][k] == 0) || (board[y][k] == pieceType ) )
						{
							
						}
						else
						{
							clearPathForCastling = false;
						}
					}
					
					if( 
						( castlingHasMoved[(int)(0.5 - (0.5 * (pieceType / 6)))][0] == false ) // rook has not moved
						&& ( castlingHasMoved[(int)(0.5 - (0.5 * (pieceType / 6)))][2] == false ) // king has not moved
						&& ( clearPathForCastling ) // path is clear
					)
					{
						// System.out.print( "Things seem clear for left castle, checks are not checked yet" );
						
						if(
							spaceIsSafe( board, x, y, (pieceType / 6) ) // the king is not in check
							&& spaceIsSafe( board, x - 1, y, (pieceType / 6) ) // the king does not pass through check
							&& spaceIsSafe( board, x - 2, y, (pieceType / 6) ) // the king does not end up end check
						)
						{
							int[][] add = { { x - 2, y, -1, -1, 1 } };
							int addLen = add.length;
							int returnedArrayLen = returnedArray.length;
							int[][] draftArray = new int[ add.length + returnedArray.length ][5];
							System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
							System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
							returnedArray = new int[ add.length + returnedArray.length ][5];
							returnedArray = draftArray;
						} // end if with huge condition
						
					} // end if with huge condition
					
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					
				}
				
				// right castle
				try
				{
					
					boolean clearPathForCastling = true;
					
					for( int k = x; k < 7; k++ )
					{
						if( (board[y][k] == 0) || (board[y][k] == pieceType ) )
						{
							//System.out.println( String.valueOf( k ) + ", " + String.valueOf( y ) + " is clear for " + String.valueOf( pieceType ) + " to castle" );
						}
						else
						{
							clearPathForCastling = false;
						}
					}
					
					//System.out.println( String.valueOf( pieceType ) + ": " + String.valueOf( clearPathForCastling ) );
					
					
					if( 
						( castlingHasMoved[(int)(0.5 - (0.5 * (pieceType / 6)))][1] == false ) // rook has not moved
						&& ( castlingHasMoved[(int)(0.5 - (0.5 * (pieceType / 6)))][2] == false ) // king has not moved
						&& ( clearPathForCastling ) // path is clear
					)
					{
						if(
							spaceIsSafe( board, x, y, (pieceType / 6) ) // the king is not in check
							&& spaceIsSafe( board, x + 1, y, (pieceType / 6) ) // the king does not pass through check
							&& spaceIsSafe( board, x + 2, y, (pieceType / 6) ) // the king does not end up end check
						)
						{
							int[][] add = { { x + 2, y, -1, -1, 2 } };
							int addLen = add.length;
							int returnedArrayLen = returnedArray.length;
							int[][] draftArray = new int[ add.length + returnedArray.length ][5];
							System.arraycopy( returnedArray, 0, draftArray, 0, returnedArrayLen );
							System.arraycopy( add, 0, draftArray, returnedArrayLen, addLen );
							returnedArray = new int[ add.length + returnedArray.length ][5];
							returnedArray = draftArray;
						} // end if with huge condition
						
					} // end if with huge condition
					
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					
				}
			
			
			}
			
		} // end King
		
		
		
		
		// if we need to take out moves that put our king in danger
		if( notJustCaptures == true )
		{
		
			// this kingLocations is for the passed board[][] array
			int[][] kingLocations = {{7, 4}, {0, 4}}; // These values are to be changed.  They are to follow the format  [0][0]-white Y  [0][1]-white X  [1][0]-black Y  [1][1]-black X
			
			// scan for the kings
			for( int currentY = 0; currentY<8; currentY++ )
			{
				for( int currentX = 0; currentX<8; currentX++ )
				{
					if( board[currentY][currentX] == 6 )
					{
						kingLocations[0][0] = currentY;
						kingLocations[0][1] = currentX;
					}
					else if( board[currentY][currentX] == -6 )
					{
						kingLocations[1][0] = currentY;
						kingLocations[1][1] = currentX;
					}
				}
			}
			
			int[][] kingLocationsAfterMove = new int[2][2]; // These values are to be changed.  They are to follow the format  [0][0]-white Y  [0][1]-white X  [1][0]-black Y  [1][1]-black X
			kingLocationsAfterMove[0][0] = kingLocations[0][0];
			kingLocationsAfterMove[0][1] = kingLocations[0][1];
			kingLocationsAfterMove[1][0] = kingLocations[1][0];
			kingLocationsAfterMove[1][1] = kingLocations[1][1];
			
			
			
			
			// 
			
			
			
			
			// now we have to check for the king's safety at the end of each move
			int moveIndex = 0;
			int returnedArrayLen = returnedArray.length;
			while( moveIndex<returnedArrayLen )
			{
				if( // the move does not capture the opposing king 
					( returnedArray[moveIndex][2] != kingLocations[(int)(0.5 + (0.5 * ( pieceType / Math.abs( pieceType ) )))][1] ) 
					|| ( returnedArray[moveIndex][3] != kingLocations[(int)(0.5 + (0.5 * ( pieceType / Math.abs( pieceType ) )))][0] ) 
				)
				{
					
					// System.out.println( "we're examining a move from  X:" + String.valueOf( x ) + " Y:" + String.valueOf( y ) );
					
					
					int[][] boardAfterMove = new int[8][8];
					
					for( int ky = 0; ky<8; ky++ ) 
					{
						for( int kx = 0; kx<8; kx++ )
						{
							boardAfterMove[ky][kx] = board[ky][kx]; 
						}
					}
					
					if( (returnedArray[ moveIndex ][3] != -1) && (returnedArray[ moveIndex ][2] != -1) )
					{
						boardAfterMove[ returnedArray[ moveIndex ][3] ][ returnedArray[ moveIndex ][2] ] = 0; // put the capture in boardAfterMove
					}
					boardAfterMove[ returnedArray[ moveIndex ][1] ][ returnedArray[ moveIndex ][0] ] = pieceType; // put the movement in boardAfterMove
					boardAfterMove[ y ][ x ] = 0; // empty starting square in boardAfterMove
					
					if( Math.abs( pieceType ) == 6 )
					{
						kingLocationsAfterMove[(int) (0.5 - ( 0.5 * (pieceType / 6) ) )][1] = returnedArray[moveIndex][0];
						kingLocationsAfterMove[(int) (0.5 - ( 0.5 * (pieceType / 6) ) )][0] = returnedArray[moveIndex][1];
					}
					else
					{
						kingLocationsAfterMove[(int) (0.5 - ( 0.5 * (pieceType / 6) ) )][0] = kingLocations[(int) (0.5 - ( 0.5 * (pieceType / 6) ) )][0];
						kingLocationsAfterMove[(int) (0.5 - ( 0.5 * (pieceType / 6) ) )][1] = kingLocations[(int) (0.5 - ( 0.5 * (pieceType / 6) ) )][1];
					}
					
					// Check for the king's safety after the move
					if( 
						spaceIsSafe( 
							boardAfterMove, 
							kingLocationsAfterMove[(int)(0.5 - (0.5 * (pieceType / Math.abs( pieceType ))))][1],
							kingLocationsAfterMove[(int)(0.5 - (0.5 * (pieceType / Math.abs( pieceType ))))][0],
							( pieceType / Math.abs( pieceType )) 
						) == false 
					)
					{
						// System.out.println( "We're removing a move" );
						
						// if the king is not safe, remove the move from returnedArray
						returnedArrayLen = returnedArray.length;
						int newReturnedArrayLen = returnedArrayLen - 1;
						int[][] draftArray = new int[ newReturnedArrayLen ][5];
						System.arraycopy( returnedArray, 0, draftArray, 0, moveIndex );
						System.arraycopy( returnedArray, moveIndex + 1, draftArray, moveIndex, newReturnedArrayLen - moveIndex );
						returnedArray = new int[ newReturnedArrayLen ][5];
						returnedArray = draftArray;
						
						moveIndex--;
					}
					
				}
				
				returnedArrayLen = returnedArray.length;
				moveIndex++;
			}
		
		} // end if( notJustCaptures == true )
		
		
		
		
		
		
		return returnedArray;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private boolean spaceIsSafe( int[][] board, int safetyTestedX, int safetyTestedY, int side ) // board condition we're testing,  x of space we're testing for safety,  y of space we're testing for safety,  1-white or -1-black that is 'our' side
	{
		
		// System.out.println( board[4][7] );
		
		
		boolean safe = true;
		
		
		
		for( int piece = 1; piece<=6; piece++ )
		{
			
			int[][] boardWithPieceOnSpaceOfInterest = new int[8][8];
			
			for( int ky = 0; ky<8; ky++ ) // give boardWithPieceOnSpaceOfInterest the same values as board
			{
				for( int kx = 0; kx<8; kx++ )
				{
					boardWithPieceOnSpaceOfInterest[ky][kx] = board[ky][kx];
				}
			}
			
			boardWithPieceOnSpaceOfInterest[safetyTestedY][safetyTestedX] = side * piece; // put piece on the space of interest
			
			int[][] possibleCapturingMovesOfThePiece = getMovesForPieceAt( boardWithPieceOnSpaceOfInterest, safetyTestedX, safetyTestedY, false, hasMoved, lastMovePawnDoubleStep ); // get the capturing moves of the piece
			
			for( int k = 0; k<possibleCapturingMovesOfThePiece.length; k++ )
			{ // for each possible capturing move
				
				if( boardWithPieceOnSpaceOfInterest[ possibleCapturingMovesOfThePiece[k][3] ][ possibleCapturingMovesOfThePiece[k][2] ] == piece * side * (-1) ) // if the move captures an opposing one of the same type of piece
				{
					safe = false; // then this space is not safe
				}
				
			}
			
			
		}
		
		
		
		/*
		int[][] boardWithPawnOnSpaceOfInterest = new int[8][8];
		
		for( int ky = 0; ky<8; ky++ ) // give boardWithPawnOnSpaceOfInterest the same values as board
		{
			for( int kx = 0; kx<8; kx++ )
			{
				boardWithPawnOnSpaceOfInterest[ky][kx] = board[ky][kx];
			}
		}
		
		// System.out.println( boardWithPawnOnSpaceOfInterest[4][7] );
		
		boardWithPawnOnSpaceOfInterest[safetyTestedY][safetyTestedX] = side; // put a pawn on the space of interest
		// there was a big problem here that I fixed (Y and X confused)
		
		// System.out.println( boardWithPawnOnSpaceOfInterest[4][7] );
		
		
		for( int y = 0; y < 8; y++ ) // iterate through the rows
		{
			for( int x = 0; x < 8; x++ ) // iterate through the columns
			{
				//System.out.println( String.valueOf( x ) + "(x), " + String.valueOf( y ) + "(y): " + String.valueOf( boardWithPawnOnSpaceOfInterest[y][x] * side ) );
				
				if( boardWithPawnOnSpaceOfInterest[y][x] * side < 0 ) // if there is an opposing piece on the current square
				{
					
					
					//System.out.println( String.valueOf( x ) + "(x), " + String.valueOf( y ) + "(y) !!!!!!!!!!" );
					
					
					//if( x == 7 && y == 4 )
					//{
					//	System.out.println( "we're testing that bishop" );
					//}
					
					
					
					
					
					int[][] movesOfTheOpposingPiece = getMovesForPieceAt( boardWithPawnOnSpaceOfInterest, x, y, false, hasMoved, lastMovePawnDoubleStep ); // get the opposing piece's moves under the hypothetical condition that there is a pawn on the space of interest
					
					for( int k = 0; k < movesOfTheOpposingPiece.length; k++ ) // iterate through the opposing piece's moves
					{
						if( ( movesOfTheOpposingPiece[k][2] == safetyTestedX ) && ( movesOfTheOpposingPiece[k][3] == safetyTestedY ) ) // if the opposing piece's moves include a move that involves attacking the space of interest
						{
							safe = false; // the space isn't safe
							// System.out.println( "asdf" );
						}
						
					} // end for( int k = 0; k < movesOfTheOpposingPiece.length; k++ )
					
				} // end if( board[y][x] * side < 0 )
				
			} // end for( int x = 0; x < 8; x++ )
		} // end for( int y = 0; y < 8; y++ )
		*/
	
	
	
		return safe;
	} // end private boolean spaceIsSafe( int[][] board, int safetyTestedX, int safetyTestedY, int side )
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//  x - x of space clicked  y - y of space clicked
	private void boardJButtonActionPerformed( ActionEvent event, int x, int y )
	{
		
		
		try
		{
		
		
			if( ( theSpaces[y][x] * ( 1 - ( 2 * (turn % 2) ) ) <= 0 ) ) // if a blank square or opposing piece was clicked
			{
				int[][] selectedSpacePossibleMoves = getMovesForPieceAt( theSpaces, selectedSpace[1], selectedSpace[0], true, hasMoved, lastMovePawnDoubleStep ); // the the moves of the selected space
				
				int[] move = new int[5];
				
				boolean legalMove = false;
				
				for( int k = 0; k<selectedSpacePossibleMoves.length; k++ ) // for each move
				{
					if( (selectedSpacePossibleMoves[k][0] == x) && (selectedSpacePossibleMoves[k][1] == y) ) // if the x and y of the landing space match the x and y of the clicked space
					{
						
						for( int i = 0; i<5; i++ ) // get the move 
						{
							move[i] = selectedSpacePossibleMoves[k][i];
							legalMove = true;
						}
						
					}
				}
				
				
				if( legalMove == false )
				{
					return;
				}
				
				
				
				
				
				// append the game log
				String[] pieceTypeAbbreviations = { "_", "P", "R", "N", "B", "Q", "K" };
				
				gameLog = gameLog
				 + pieceTypeAbbreviations[ Math.abs( theSpaces[ selectedSpace[0] ][ selectedSpace[1] ] ) ] // moved piece type
				 + " " // space character
				 + abcdefgh[ selectedSpace[1] ] // horizontal coordinate of starting space
				 + String.valueOf( 8 - selectedSpace[ 0 ] ) // vertical coordinate of starting space
				 + "-" // hyphen character
				 + abcdefgh[ x ] // horizontal coordinate of landing space
				 + String.valueOf( 8 - y ) // vertical coordinate of landing space
				 + "\n"; // line break
				 
				
				
				
				
				// say when En Passant oocurs
				if( (move[1] != move[3]) && (move[2] > -1) && (move[3] > -1) )
				{
					gameLog = gameLog + " En Passant\n";
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				// make the move!!!!!!!!!!!!!!!!
				try
				{
					// append gameLog
					gameLog = gameLog + " Capt: " + pieceTypeAbbreviations[ Math.abs( theSpaces[ move[3] ][ move[2] ] ) ] + "\n";
					
					theSpaces[ move[3] ][ move[2] ] = 0; // do any capturing
					// if no capture, move[2] and move[3] should be -1, throwing an Exception
				}
				catch( Exception e )
				{
					
				}
				theSpaces[ y ][ x ] = theSpaces[ selectedSpace[0] ][ selectedSpace[1] ]; // make landing space have the moved piece
				theSpaces[ selectedSpace[0] ][ selectedSpace[1] ] = 0; // clear starting space
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				// adjust hasMoved if necessary
				if( Math.abs( theSpaces[7][0] ) != 2 )
				{
					hasMoved[0][0] = true;
				}
				if( Math.abs( theSpaces[7][7] ) != 2 )
				{
					hasMoved[0][1] = true;
				}
				if( Math.abs( theSpaces[7][4] ) != 6 )
				{
					hasMoved[0][2] = true;
				}
				if( Math.abs( theSpaces[0][0] ) != 2 )
				{
					hasMoved[1][0] = true;
				}
				if( Math.abs( theSpaces[0][7] ) != 2 )
				{
					hasMoved[1][1] = true;
				}
				if( Math.abs( theSpaces[0][4] ) != 6 )
				{
					hasMoved[1][2] = true;
				}
				
				
				
				
				// if pawn double step
				if( (Math.abs(theSpaces[y][x]) == 1) && (Math.abs( y - selectedSpace[0] ) == 2) )
				{
					
					// adjust lastMovePawnDoubleStep
					lastMovePawnDoubleStep[0] = y;
					lastMovePawnDoubleStep[1] = x;
					
				}
				else // if not a pawn double step
				{
					// clear lastMovePawnDoubleStep of coordinates
					lastMovePawnDoubleStep[0] = -1;
					lastMovePawnDoubleStep[1] = -1;
				}
				
				// if left castle
				if( move[4] == 1 ) 
				{
					theSpaces[ y ][ x + 1 ] = theSpaces[ y ][ 0 ]; // make rook at castle place
					theSpaces[ y ][ 0 ] = 0; // clear rook from its starting space
					
					// adjust hasMoved
					hasMoved[1 - (y / 7)][0] = true;
					hasMoved[1 - (y / 7)][2] = true;
					
					// append gameLog
					gameLog = gameLog + " Castle Left\n";
				}
				// if right castle
				if( move[4] == 2 ) 
				{
					theSpaces[ y ][ x - 1 ] = theSpaces[ y ][ 7 ]; // make rook at castle place
					theSpaces[ y ][ 7 ] = 0; // clear rook from its starting space
					
					// adjust hasMoved
					hasMoved[1 - (y / 7)][1] = true;
					hasMoved[1 - (y / 7)][2] = true;
					
					// append gameLog
					gameLog = gameLog + " Castle Right\n";
				}
				
				
				
				
				
				// if a pawn reaches the furthest row
				if( ( Math.abs( theSpaces[y][x] ) == 1 ) && (( y == 0 ) || ( y == 7 )) )
				{
					
					int side = 0;
					
					String whiteOrBlack = "";
					if( theSpaces[y][x] > 0 )
					{
						whiteOrBlack = "White";
						side = 1;
					}
					else if( theSpaces[y][x] < 0 )
					{
						whiteOrBlack = "Black";
						side = -1;
					}
					
					
					String newPieceInput = JOptionPane.showInputDialog( "A " + whiteOrBlack.toLowerCase() + " pawn reached the furthest row!\nIt will be replaced by a more powerful piece.\n" + whiteOrBlack + " player, enter\n q for Queen\n r for rook\n n or k for knight or\n b for bishop" ).toLowerCase();
					
					
					if( newPieceInput.charAt(0) == 'q' )
					{
						theSpaces[y][x] = 5 * side;
					}
					if( newPieceInput.charAt(0) == 'r' )
					{
						theSpaces[y][x] = 2 * side;
					}
					if( newPieceInput.charAt(0) == 'b' )
					{
						theSpaces[y][x] = 4 * side;
					}
					if( (newPieceInput.charAt(0) == 'k') || (newPieceInput.charAt(0) == 'n') )
					{
						theSpaces[y][x] = 3 * side;
					}
					
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				turn++; // increment the turn counter
				// this is a critical point
				
				
				
				
				
				
				createUserInterface(); // this is a big statement
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				int[][] kingLocations = {{7, 4}, {0, 4}}; // These values are to be changed.  They are to follow the format  [0][0]-white Y  [0][1]-white X  [1][0]-black Y  [1][1]-black X
				
				// scan for the kings
				for( int currentY = 0; currentY<8; currentY++ )
				{
					for( int currentX = 0; currentX<8; currentX++ )
					{
						if( theSpaces[currentY][currentX] == 6 )
						{
							kingLocations[0][0] = currentY;
							kingLocations[0][1] = currentX;
						}
						else if( theSpaces[currentY][currentX] == -6 )
						{
							kingLocations[1][0] = currentY;
							kingLocations[1][1] = currentX;
						}
					}
				}
				
				
				
				if( 
					spaceIsSafe( 
						theSpaces, 
						kingLocations[ turn % 2 ][1], 
						kingLocations[ turn % 2 ][0], 
						1 - (2 * ( turn % 2 ) ) 
					) == false
				)
				{
					
					
					if( numberOfPossibleMoves == 0 )
					{
						System.out.println( "Checkmate" );
						
						String[] whiteBlack = { "White", "Black" };
						gameLog = gameLog + "Checkmate\n" + whiteBlack[ 1 - (turn % 2) ] + " Wins\n";
						
						help = "The " + whiteBlack[ 1 - (turn % 2) ].toLowerCase() + "\nside wins.\nThere are\nno moves the\n" + whiteBlack[ turn % 2 ].toLowerCase() + " side\ncan make that\ngets the king\nout of check.\nClick the\nend game button\nto return to\nthe menu.";
						
						gameActivity = 2 + (1 - (turn % 2));
						
						
						
						quitJButton.setText( "End Game" );
						getContentPane().repaint();
						
						
						
						
					}
					else
					{
						gameLog = gameLog + " Check\n";
					}
					
					
				}
				else if( numberOfPossibleMoves == 0 )
				{
					gameLog = gameLog + "Stalemate\nIts a Draw";
					
					
					help = "Its a draw.\nThere are\nno moves\nthat can\nbe made\nbut the\nking is not\nin check.\nClick the\nend game button\nto return to\nthe menu.";
					
					if( gameActivity == 4 )
					{
						help = "Its a draw.\nOnly kings\nare left.\nCheckmate is\n impossible.";
					}
					else
					{
						gameActivity = 1;
					}
					
					quitJButton.setText( "End Game" );
					getContentPane().repaint();
				}
				
				
				
				
				
				// if no checkmate or stalemate
				if( gameActivity == 0 )
				{
				
					// say "White:" or "Black:" for the next move
					if( theSpaces[ y ][ x ] > 0 )
					{
						
						gameLog = gameLog + "Black: ";
						if( sides[1] == 0 )
						{
							help = "Black player,\nclick a piece.";
						}
						else
						{
							help = "The computer\nplayer is going.";
						}
					}
					else if( theSpaces[ y ][ x ] < 0 )
					{
						
						gameLog = gameLog + "White: ";
						if( sides[0] == 0 )
						{
							help = "White player,\nclick a piece.";
						}
						else
						{
							help = "The computer\nplayer is going.";
						}
					}
				
				}
				
				
				
				
				
				
				gameLogJTextArea.setText( gameLog );
				
				helpJTextArea.setText( help );
				helpJTextArea.setCaretPosition( 0 );
					
					
					
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			}
			else // if the space clicked was not blank or an opposing piece
			{
				
				
					
				
				selectedSpace[0] = y;
				selectedSpace[1] = x;
					
					
					
					
					
					
					
				help = helpTexts[ Math.abs( theSpaces[y][x] ) ];
					
					
					
					
					
				
				//Castling has problems
				for( int h = 0; h<hasMoved.length; h++ )
				{
					for( int i = 0; i<hasMoved[0].length; i++ )
					{
						System.out.println( String.valueOf( hasMoved[h][i] ) );
					}
				}
				
				
					
					
					
					
					
				createUserInterface();
				
				
				
				
				
				
			}
			
		
		
		
		
		} // end try
		catch( Exception e )
		{
			
		}
		
		
		
	} // end private void boardJButtonActionPerformed
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// returned from AIMove: { STARTING x, STARTING y, landing x, landing y, captured x (or -1), captured y(or -1), castling int (0-no castle  1-left castle  2-right castle), rating(based solely on the board after the move, this rating almost equally likely to be positive as negative and may be 0) }
	// passed to AIMove:   board,  side( 1-white  or  -1-black ),  depth( number of moves it thinks ahead:  0-random move  1-thinks about board after each possible move  2-thinks about board after each possible move for opponent after each possible move  3-thinks about move after each possible move after each possible move for opponent after each possible move  ... ),  width(number of moves it considers),  castlingHasMoved( { { white left rook, white right rook, white king}, { black left rook, black right rook, black king } } ), enPassantLastMovePawnDoubleStep( {y, x} )
	private int[] AIMove( int[][] board, int side, int depth, int width, boolean[][] castlingHasMoved, int[] enPassantLastMovePawnDoubleStep )
	{
		
		
		width = depth; // eliminating width with depth
		
		if( width <= 0 )
		{
			width = 1;
		}
		
		
		
		
		
		// comX won't work, I'm sure
		if( depth == 6 )
		{
			depth = 3;
			width = 40;
		}
		
		
		
		
		
		
		// topMoves[move rank ( 0-best rated move  width-worst rated move considered )][ STARTING x, STARTING y, LANDING x, LANDING y, CAPTURING x, CAPTURING y, castling stuff( 0-no castle  1-left castle,  2-right castle ), rating ]
		int[][] topMoves = new int[ width ][ 8 ];
		
		
		
		for( int y = 0; y<8; y++ )
		{
			for( int x = 0; x<8; x++ )
			{
				
				
				
				if( board[y][x] * side > 0 )
				{
					
					
					
					int[][] possibleMoves = getMovesForPieceAt( board, x, y, true, castlingHasMoved, enPassantLastMovePawnDoubleStep );
					
					for( int moveIndex = 0; moveIndex<possibleMoves.length; moveIndex++ )
					{
						
						
						
						int[][] boardAfterMove = new int[8][8];
						
						
						
						// assign the values in boardAfterMove to the values in board
						for( int ny = 0; ny<8; ny++ )
						{
							for( int nx = 0; nx<8; nx++ )
							{
								boardAfterMove[ny][nx] = board[ny][nx];
							}
						}
						
						
						
						
						
						
						// make the move in boardAfterMove
						if( (possibleMoves[moveIndex][2] != -1) && (possibleMoves[moveIndex][3] != -1) ) // do any capturing
						{
							boardAfterMove[ possibleMoves[moveIndex][3] ][ possibleMoves[moveIndex][2] ] = 0;
						}
						boardAfterMove[ possibleMoves[moveIndex][1] ][ possibleMoves[moveIndex][0] ] = boardAfterMove[y][x]; // set landing space
						boardAfterMove[ y ][ x ] = 0; // empty starting space
						if( possibleMoves[moveIndex][4] == 1 ) // left castle
						{
							boardAfterMove[y][x - 1] = boardAfterMove[y][0];
							boardAfterMove[y][0] = 0;
							
							int s = 0;
							if( side == -1 )
							{
								s = 1;
							}
							
							castlingHasMoved[s][0] = true;
							castlingHasMoved[s][2] = true;
						}
						if( possibleMoves[moveIndex][4] == 2 ) // right castle
						{
							boardAfterMove[y][x + 1] = boardAfterMove[y][7];
							boardAfterMove[y][7] = 0;
							
							int s = 0;
							if( side == -1 )
							{
								s = 1;
							}
							
							castlingHasMoved[s][1] = true;
							castlingHasMoved[s][2] = true;
						}
						if( // if the move is a pawn double step 
							( Math.abs( boardAfterMove[ possibleMoves[moveIndex][1] ][ possibleMoves[moveIndex][0] ] ) == 1 ) && 
							(
								( 
									(side == 1) && 
									(y == 6) && 
									( possibleMoves[moveIndex][1] == 4 ) 
								) || 
								( 
									(side == -1) && 
									(y == 1) && 
									( possibleMoves[moveIndex][1] == 3 ) 
								) 
							)
						)
						{
							enPassantLastMovePawnDoubleStep[0] = y;
							enPassantLastMovePawnDoubleStep[1] = x;
						}
						else
						{
							enPassantLastMovePawnDoubleStep[0] = -1;
							enPassantLastMovePawnDoubleStep[1] = -1;
						}
						
						if( Math.abs( board[ possibleMoves[moveIndex][1] ][ possibleMoves[moveIndex][0] ] ) == 1 ) // if it is a pawn
						{
							if( possibleMoves[moveIndex][1] == 3.5 + (3.5 * ( (-1) * side ) )  ) // if the pawn reaches the furthest row
							{
								
								boardAfterMove[ possibleMoves[moveIndex][1] ][ possibleMoves[moveIndex][0] ] = boardAfterMove[ possibleMoves[moveIndex][1] ][ possibleMoves[moveIndex][0] ] * 5; // replace the pawn with a queen
								
							}
						}
						
						
						
						
						
						
						// random moves
						if( depth == 0 )
						{
							
							Random Randy = new Random();
							int thisMovesRatingDifference = Randy.nextInt( 100 ) - 50;
							
							
							
							
							
							int rank = width - 1; // initialize rank
							while( 
								rank>=0 // rank, the loop variable, is zero or greater
								&& // and
								(
									(
										thisMovesRatingDifference < topMoves[rank][7] // this move has a better rating than rating at the current iteration
									) || // or
									(
										(
											( 
												topMoves[rank][0] == topMoves[rank][2] 
											) && 
											( 
												topMoves[rank][1] == topMoves[rank][3] 
											)
										) // this the move at the current iteration doesn't really exist
									) 
								)
								
							)
							{
						
							
								try
								{
									if( thisMovesRatingDifference < topMoves[rank - 1][7] ) // If this move ranks better than the next one up
									{
									
										for( int k = 0; k<8; k++ )
										{
											topMoves[rank][k] = topMoves[rank - 1][k]; // copy the next move up down to the move at rank
										}
										
									}
									else // If we've reached the rank of this move
									{
										// Make topMoves[rank] this move
										topMoves[rank][0] = x;
										topMoves[rank][1] = y;
										topMoves[rank][2] = possibleMoves[moveIndex][0];
										topMoves[rank][3] = possibleMoves[moveIndex][1];
										topMoves[rank][4] = possibleMoves[moveIndex][2];
										topMoves[rank][5] = possibleMoves[moveIndex][3];
										topMoves[rank][6] = possibleMoves[moveIndex][4];
										topMoves[rank][7] = thisMovesRatingDifference;
									}
									
									
								}
								catch( Exception e ) // if rank == 0 (this moves ranks better than any other move in topMoves)
								{
									// rank should equal one.  This is the top move.
									topMoves[rank][0] = x;
									topMoves[rank][1] = y;
									topMoves[rank][2] = possibleMoves[moveIndex][0];
									topMoves[rank][3] = possibleMoves[moveIndex][1];
									topMoves[rank][4] = possibleMoves[moveIndex][2];
									topMoves[rank][5] = possibleMoves[moveIndex][3];
									topMoves[rank][6] = possibleMoves[moveIndex][4];
									topMoves[rank][7] = thisMovesRatingDifference;
								}
							
							
							
								rank--; // decrement rank by one
								
								if( rank < 0 )
								{
									break;
								}
								
							} // end while with huge condition
							
							
							
							
						}
						
						
						
						
						
						
						// rank moves based on the rate
						if( depth >= 1 )
						{
							
							int[] thisMovesBoardRatings = rateBoard( boardAfterMove, castlingHasMoved, enPassantLastMovePawnDoubleStep );
							
							int s = 0;
							if( side == 1 ) // this is backwards, but the whole chess engine is built on it
							{
								s = 1;
							}
							
							int thisMovesRatingDifference = thisMovesBoardRatings[s] - thisMovesBoardRatings[1 - s];
							
							
							/*if( Math.abs( boardAfterMove[ possibleMoves[moveIndex][1] ][ possibleMoves[moveIndex][0] ] ) != 6 ) // if this move does not move a king
							{*/
								
								
								if( spaceIsSafe( board, x, y, side ) == false ) // if the starting space is safe
								{
									thisMovesRatingDifference = thisMovesRatingDifference - ( pieceValues[ Math.abs( board[y][x] ) ] * pieceValueCoeficient ); // add the landing piece's value to thisMovesRatingDifference
									
									int[][] startingSpaceMoves = getMovesForPieceAt( board, x, y, true, castlingHasMoved, enPassantLastMovePawnDoubleStep ); // get the moves available for this piece before this move
									
									thisMovesRatingDifference = thisMovesRatingDifference - ( startingSpaceMoves.length * moveValues[ Math.abs( board[ y ][ x ] ) ] ); // take the number of available for this piece before this move into account
									
									
								} 
								
								
								if( spaceIsSafe( boardAfterMove, possibleMoves[moveIndex][0], possibleMoves[moveIndex][1], side ) == false ) // if the landing space is safe
								{
									thisMovesRatingDifference = thisMovesRatingDifference + ( pieceValues[ Math.abs( boardAfterMove[y][x] ) ] * pieceValueCoeficient ); // add the landing piece's value to thisMovesRatingDifference
									
									int[][] landingSpaceMoves = getMovesForPieceAt( boardAfterMove, possibleMoves[moveIndex][0], possibleMoves[moveIndex][1], true, castlingHasMoved, enPassantLastMovePawnDoubleStep ); // get the moves available for this piece after this move
									
									thisMovesRatingDifference = thisMovesRatingDifference + ( landingSpaceMoves.length * moveValues[ Math.abs( boardAfterMove[ possibleMoves[moveIndex][1] ][ possibleMoves[moveIndex][0] ] ) ] ); // take the number of available for this piece after this move into account
									
									
								} 
								
								
							/*
							} // end if( Math.abs( boardAfterMove[ possibleMoves[moveIndex][1] ][ possibleMoves[moveIndex][0] ] ) != 6 )
							*/
							
							
							
							// Do the ranking
							int rank = width - 1; // initialize rank
							while( 
								rank>=0 // rank, the loop variable, is zero or greater
								&& // and
								(
									(
										thisMovesRatingDifference < topMoves[rank][7] // this move has a better rating than rating at the current iteration
									) || // or
									(
										(
											( 
												topMoves[rank][0] == topMoves[rank][2] 
											) && 
											( 
												topMoves[rank][1] == topMoves[rank][3] 
											)
										) // this the move at the current iteration doesn't really exist
									) 
								)
								
							)
							{
						
							
								try
								{
									if( thisMovesRatingDifference < topMoves[rank - 1][7] ) // If this move ranks better than the next one up
									{
									
										for( int k = 0; k<8; k++ )
										{
											topMoves[rank][k] = topMoves[rank - 1][k]; // copy the next move up down to the move at rank
										}
										
									}
									else // If we've reached the rank of this move
									{
										// Make topMoves[rank] this move
										topMoves[rank][0] = x;
										topMoves[rank][1] = y;
										topMoves[rank][2] = possibleMoves[moveIndex][0];
										topMoves[rank][3] = possibleMoves[moveIndex][1];
										topMoves[rank][4] = possibleMoves[moveIndex][2];
										topMoves[rank][5] = possibleMoves[moveIndex][3];
										topMoves[rank][6] = possibleMoves[moveIndex][4];
										topMoves[rank][7] = thisMovesRatingDifference;
									}
									
									
								}
								catch( Exception e ) // if rank == 0 (this moves ranks better than any other move in topMoves)
								{
									// rank should equal one.  This is the top move.
									topMoves[rank][0] = x;
									topMoves[rank][1] = y;
									topMoves[rank][2] = possibleMoves[moveIndex][0];
									topMoves[rank][3] = possibleMoves[moveIndex][1];
									topMoves[rank][4] = possibleMoves[moveIndex][2];
									topMoves[rank][5] = possibleMoves[moveIndex][3];
									topMoves[rank][6] = possibleMoves[moveIndex][4];
									topMoves[rank][7] = thisMovesRatingDifference;
								}
							
							
							
								rank--; // decrement rank by one
								
								if( rank < 0 )
								{
									break;
								}
								
							} // end while with huge condition
							
							
							
							
							
							
							
							
						} // end if( depth == 1 ) which is the base case of the recursive method AIMove
						
						
						
						
						
						
						
						
					} // end for( int moveIndex = 0; moveIndex<possibleMoves.length; moveIndex++ )
					
				
				
				} // end if( board[y][x] * side > 0 )
				
				
				
			} // end for( int x = 0; x<8; x++ )
		} // for( int y = 0; y<8; y++ )
		
		
		
		
		
		
		
		
		
		if( depth >= 2 ) // the smaller caller
		{
			
			
			int[] newRatings = new int[width];
			
			
			
			
			// get a copy of the original topMoves before we re-rank
			int[][] originalTopMoves = new int[topMoves.length][topMoves[0].length];
			
			for( int h = 0; h<topMoves.length; h++ )
			{
				for( int i = 0; i<topMoves[0].length; i++ )
				{
					originalTopMoves[h][i] = topMoves[h][i];
				}
			}
			
			
			
			
			
			
			
			// PART OF BIG TEST: CLEAR topMoves
			topMoves = new int[width][8];
			
			
			
			
			
			
			
			
			
			for // iterate through the top five moves
			( 
				int moveIndex = 0;
				(
					(
						moveIndex<width // stop at width
					) &&  // and
					(
						(
							originalTopMoves[moveIndex][0] != originalTopMoves[moveIndex][2] // starting x != landing x
						) || ( // or
							originalTopMoves[moveIndex][1] != originalTopMoves[moveIndex][3] // starting y != landing y
						)
					)
				); 
				moveIndex++ 
			)
			{
			
				
				
				int y = originalTopMoves[moveIndex][1];
				int x = originalTopMoves[moveIndex][0];
				
				
				
				
				
				
				int[][] boardAfterMove = new int[8][8];
						
						
						
				// assign the values in boardAfterMove to the values in board
				for( int ny = 0; ny<8; ny++ )
				{
					for( int nx = 0; nx<8; nx++ )
					{
						boardAfterMove[ny][nx] = board[ny][nx];
					}
				}
				
				
				
				
				
				
				// make the move in boardAfterMove
				if( (originalTopMoves[moveIndex][4] != -1) && (originalTopMoves[moveIndex][5] != -1) ) // do any capturing
				{
					boardAfterMove[ originalTopMoves[moveIndex][5] ][ originalTopMoves[moveIndex][4] ] = 0;
				}
				boardAfterMove[ originalTopMoves[moveIndex][3] ][ originalTopMoves[moveIndex][2] ] = boardAfterMove[y][x]; // set landing space
				boardAfterMove[ y ][ x ] = 0; // empty starting space
				if( originalTopMoves[moveIndex][6] == 1 ) // left castle
				{
					boardAfterMove[y][x - 1] = boardAfterMove[y][0];
					boardAfterMove[y][0] = 0;
					
					int s = 0;
					if( side == -1 )
					{
						s = 1;
					}
					
					castlingHasMoved[s][0] = true;
					castlingHasMoved[s][2] = true;
				}
				if( originalTopMoves[moveIndex][6] == 2 ) // right castle
				{
					boardAfterMove[y][x + 1] = boardAfterMove[y][7];
					boardAfterMove[y][7] = 0;
					
					int s = 0;
					if( side == -1 )
					{
						s = 1;
					}
					
					castlingHasMoved[s][1] = true;
					castlingHasMoved[s][2] = true;
				}
				if( // if the move is a pawn double step 
					( Math.abs( boardAfterMove[ originalTopMoves[moveIndex][3] ][ originalTopMoves[moveIndex][2] ] ) == 1 ) && 
					(
						( 
							(side == 1) && 
							(y == 6) && 
							( originalTopMoves[moveIndex][3] == 4 ) 
						) || 
						( 
							(side == -1) && 
							(y == 1) && 
							( originalTopMoves[moveIndex][3] == 3 ) 
						) 
					)
				)
				{
					enPassantLastMovePawnDoubleStep[0] = y;
					enPassantLastMovePawnDoubleStep[1] = x;
				}
				else
				{
					enPassantLastMovePawnDoubleStep[0] = -1;
					enPassantLastMovePawnDoubleStep[1] = -1;
				}
				
				
				
				
						
				int[] theOpponentsLikelyNextMove = AIMove( boardAfterMove, (-1) * side, depth - 1, width, castlingHasMoved, enPassantLastMovePawnDoubleStep ); // the big recursive statement in the smaller caller
							
							
				
							
							
				int thisMovesRatingDifference = (-1) * theOpponentsLikelyNextMove[7]; // get the opposite rating from theOpponentsLikelyNextMove (the rating in theOpponentsLikelyNextMove is for the opponent, the opposite of it is for this side)
						
							
				

				newRatings[moveIndex] = thisMovesRatingDifference; // put the thinking-ahead rating in newRatings (NOT CURRENTLY USED WHEN I WRITE THIS)
				
				
				
				
				
				
				
				
				
				
				// Do the ranking
				int rank = width - 1; // initialize rank
				while( 
					rank>=0 // rank, the loop variable, is zero or greater
					&& // and
					(
						(
							thisMovesRatingDifference < topMoves[rank][7] // this move has a better rating than rating at the current iteration
						) || // or
						(
							(
								( 
									topMoves[rank][0] == topMoves[rank][2] 
								) && 
								( 
									topMoves[rank][1] == topMoves[rank][3] 
								)
							) // the move at the current iteration doesn't really exist
						) 
					)
					
				) // BIG TEST IN THIS CONDITION: originalTopMoves -> topMoves
				{
					
					try
					{
						if( thisMovesRatingDifference < topMoves[rank - 1][7] ) // If this move ranks better than the next one up
						{
						
							for( int k = 0; k<8; k++ )
							{
								topMoves[rank][k] = topMoves[rank - 1][k]; // copy the next move up down to the move at rank
							}
							
						}
						else // If we've reached the rank of this move
						{
							// Make topMoves[rank] this move
							topMoves[rank][0] = x;
							topMoves[rank][1] = y;
							topMoves[rank][2] = originalTopMoves[moveIndex][2];
							topMoves[rank][3] = originalTopMoves[moveIndex][3];
							topMoves[rank][4] = originalTopMoves[moveIndex][4];
							topMoves[rank][5] = originalTopMoves[moveIndex][5];
							topMoves[rank][6] = originalTopMoves[moveIndex][6];
							topMoves[rank][7] = thisMovesRatingDifference;
						}
						
						
					}
					catch( Exception e ) // if rank == 0 (this moves ranks better than any other move in topMoves)
					{
						// rank should equal one.  This is the top move.
						topMoves[rank][0] = x;
						topMoves[rank][1] = y;
						topMoves[rank][2] = originalTopMoves[moveIndex][2];
						topMoves[rank][3] = originalTopMoves[moveIndex][3];
						topMoves[rank][4] = originalTopMoves[moveIndex][4];
						topMoves[rank][5] = originalTopMoves[moveIndex][5];
						topMoves[rank][6] = originalTopMoves[moveIndex][6];
						topMoves[rank][7] = thisMovesRatingDifference;
					}
				
				
				
					rank--; // decrement rank by one
					
					if( rank < 0 )
					{
						break;
					}
					
				} // end while with huge condition
			
			
			
			

			}
			
			
			
			
			
			
					
			
			
			
			
		} // end if( depth >= 2 )
		
		
		
		
		
		
		
		
		
		// if topMoves is not empty
		if( topMoves.length > 0 )
		{
			// get the number of moves that tie at the top
			int numberOfTyingTopMoves = 0;
			while( numberOfTyingTopMoves < topMoves.length && topMoves[numberOfTyingTopMoves][7] == topMoves[0][7] )
			{
				numberOfTyingTopMoves++;
			}
			
			
			if( numberOfTyingTopMoves > 0 ) // if there is a tie at the top
			{
				
				
				Random Rand = new Random();
				topMoves[0] = topMoves[ Rand.nextInt( numberOfTyingTopMoves ) ]; // put a random tying move at the top
				
				
			}
		}
		
		
		
		
		
		
		
		// if checkmated or stalemated
		if( (topMoves[0][0] == topMoves[0][2]) && (topMoves[0][1] == topMoves[0][3]) )
		{
			
			
			int[][] kingLocations = {{7, 4}, {0, 4}}; // These values are to be changed.  They are to follow the format  [0][0]-white Y  [0][1]-white X  [1][0]-black Y  [1][1]-black X
		
			// scan for the kings
			for( int currentY = 0; currentY<8; currentY++ )
			{
				for( int currentX = 0; currentX<8; currentX++ )
				{
					if( board[currentY][currentX] == 6 )
					{
						kingLocations[0][0] = currentY;
						kingLocations[0][1] = currentX;
					}
					else if( board[currentY][currentX] == -6 )
					{
						kingLocations[1][0] = currentY;
						kingLocations[1][1] = currentX;
					}
				}
			}
			
			int s = 0;
			if( side == -1 )
			{
				s = 1;
			}
			
			if( spaceIsSafe( board, kingLocations[s][0], kingLocations[s][1], side ) == false ) // if checkmated
			{
				topMoves[0][7] = 100;
			}
			
			
		} // end if( (topMoves[0][0] == topMoves[0][2]) && (topMoves[0][1] == topMoves[0][3]) ) which is if checkmated or stalemated
		
		
		
		
		
		
		
		
		// I'm from Missouri: you gotta show me
		System.out.println( "topMoves length=" + String.valueOf( topMoves.length ) );
		for( int h = 0; h<topMoves.length; h++ )
		{
			for( int i = 0; i<topMoves[0].length; i++ )
			{
				System.out.print( String.valueOf( topMoves[h][i] ) + " " );
			}
			
			System.out.println( "" );
		}
		
		
		
		
		
		
		
		
		
		
		return topMoves[0]; // return the best ranked move
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	
		When I write this, rateBoard rates the board for both sides based on
		- Pieces on the board
		- (not any more)Available moves
		- (not any more)Safety of the pieces
		- Pawn distances
		- Check
		- Randomness
		
		I may add or remove stuff from it
		
	*/
	
	// I'm thinking { white rating, black rating }
	private int[] rateBoard( int[][] board, boolean[][] castlingHasMoved, int[] enPassantLastMovePawnDoubleStep )
	{
		
		
		
		int[] ratings = { 0, 0 }; // to be changed then returned {white, black}
		

		
		
		
		int[] moveValues = { 0, 3, 1, 2, 1, 0, 0 }; // value of a move of each piece
		
		
		
		
		
		
		
		for( int y = 0; y<8; y++ )
		{
			for( int x = 0; x<8; x++ )
			{
				
				
				if( board[y][x] != 0 )
				{
				
					boolean mate = false;
					boolean check = false;
					
					boolean falseWhiteTrueBlack = board[y][x] < 0; // this boolean is false if the piece is white or true if the piece is black
					
					
					if( falseWhiteTrueBlack ) // if the piece is black
					{
						ratings[1] = ratings[1] + ( pieceValues[ Math.abs( board[y][x] ) ] * pieceValueCoeficient ); 
					}
					else
					{
						ratings[0] = ratings[0] + ( pieceValues[ Math.abs( board[y][x] ) ] * pieceValueCoeficient ); 
					}
					
					
					
					if( Math.abs( board[y][x] ) == 1 ) // if its a pawn
					{
						if( falseWhiteTrueBlack ) // if we're black
						{
							ratings[1] += y - 1;
						}
						else // if we're white
						{
							ratings[0] += 6 - y;
						}
						
					}
					
					
					
					// check
					if( Math.abs( board[y][x] ) == 6 ) // if it is a king
					{
						if( spaceIsSafe( board, x, y, board[y][x]/Math.abs( board[y][x] ) ) ) // if the king is not in check
						{
						
						}
						else // if the king is in check
						{
							check = true;
						
							if( falseWhiteTrueBlack ) // if black
							{
								ratings[1] -= 2; 
							}
							else
							{
								ratings[0] -= 2; 
							}
							
							if( getMovesForPieceAt( board, x, y, true,castlingHasMoved, enPassantLastMovePawnDoubleStep ).length == 0 ) // if the king has no moves
							{
								mate = true;
								for( int iy = 0; iy<8 && (mate==true); iy++ )
								{
									for( int ix = 0; ix<8 && (mate==true); ix++ )
									{
									
										if( falseWhiteTrueBlack )
										{
											if( board[iy][ix] < 0 )
											{
												if( getMovesForPieceAt( board, ix, iy, true, castlingHasMoved, enPassantLastMovePawnDoubleStep ).length > 0 )
												{
													mate = false;
												}
											}
										} // end if( falseWhiteTrueBlack )
										else 
										{
											if( board[iy][ix] > 0 )
											{
												if( getMovesForPieceAt( board, ix, iy, true, castlingHasMoved, enPassantLastMovePawnDoubleStep ).length > 0 )
												{
													mate = false;
												}
											}
										} // end else of if( falseWhiteTrueBlack )
										
									}
								}
								
								if( mate == true ) // if there is a checkmate
								{
								
									if( falseWhiteTrueBlack )
									{
										ratings[0] += 100;
									}
									else
									{
										ratings[1] += 100; 
									}
									
								}
								
							} // end if this king has no moves
							
						} // end if this king is in check
						
					} // end if this is a king
						
					
					
					/*
					int[][] thisPiecesMoves = getMovesForPieceAt( board, x, y, true, castlingHasMoved, enPassantLastMovePawnDoubleStep ); // lag warning
					
					
					if( falseWhiteTrueBlack ) // if the piece is black
					{
						ratings[1] = ratings[1] + ( moveValues[ Math.abs( board[y][x] ) ] * thisPiecesMoves.length ); 
					}
					else
					{
						ratings[0] = ratings[0] + ( moveValues[ Math.abs( board[y][x] ) ] * thisPiecesMoves.length ); 
					}
					*/
					
					
				}
				
			}
		}
		
		
		
		
		
		
		
		
		
		
		// Add some randomness
		Random Randolf = new Random();
		if( Randolf.nextInt(5) == 1 )
		{
			ratings[0] += Randolf.nextInt( 4 ) - 2;
			ratings[1] += Randolf.nextInt( 4 ) - 2;
		}
		
		
		
		
		
		
		return ratings;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void computerPlayerMakeMove( int[] theMove )
	{
		
		selectedSpace[0] = theMove[1]; // x of starting space
		selectedSpace[1] = theMove[0]; // y of starting space
		
		int x = theMove[2]; // initialize the x of the landing space
		int y = theMove[3]; // initialize the y of the landing space
		
		
		
		// append the game log
		String[] pieceTypeAbbreviations = { "_", "P", "R", "N", "B", "Q", "K" };
		
		gameLog = gameLog
		 + pieceTypeAbbreviations[ Math.abs( theSpaces[ selectedSpace[0] ][ selectedSpace[1] ] ) ] // moved piece type
		 + " " // space character
		 + abcdefgh[ selectedSpace[1] ] // horizontal coordinate of starting space
		 + String.valueOf( 8 - selectedSpace[ 0 ] ) // vertical coordinate of starting space
		 + "-" // hyphen character
		 + abcdefgh[ x ] // horizontal coordinate of landing space
		 + String.valueOf( 8 - y ) // vertical coordinate of landing space
		 + "\n"; // line break
		 
		 
		
		
		// say when En Passant oocurs
		if( (theMove[3] != theMove[5]) && (theMove[4] > -1) && (theMove[5] > -1) )
		{
			gameLog = gameLog + " En Passant\n";
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// make the move!!!!!!!!!!!!!!!!
		try
		{
			// append gameLog
			gameLog = gameLog + " Capt: " + pieceTypeAbbreviations[ Math.abs( theSpaces[ theMove[5] ][ theMove[4] ] ) ] + "\n";
			
			theSpaces[ theMove[5] ][ theMove[4] ] = 0; // do any capturing
			// if no capture, theMove[4] and theMove[5] should be -1, throwing an Exception
		}
		catch( Exception e )
		{
			
		}
		theSpaces[ y ][ x ] = theSpaces[ selectedSpace[0] ][ selectedSpace[1] ]; // make landing space have the moved piece
		theSpaces[ selectedSpace[0] ][ selectedSpace[1] ] = 0; // clear starting space
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// adjust hasMoved if necessary
		if( Math.abs( theSpaces[7][0] ) != 2 )
		{
			hasMoved[0][0] = true;
		}
		if( Math.abs( theSpaces[7][7] ) != 2 )
		{
			hasMoved[0][1] = true;
		}
		if( Math.abs( theSpaces[7][4] ) != 6 )
		{
			hasMoved[0][2] = true;
		}
		if( Math.abs( theSpaces[0][0] ) != 2 )
		{
			hasMoved[1][0] = true;
		}
		if( Math.abs( theSpaces[0][7] ) != 2 )
		{
			hasMoved[1][1] = true;
		}
		if( Math.abs( theSpaces[0][4] ) != 6 )
		{
			hasMoved[1][2] = true;
		}
	
		// if pawn double step
		if( (Math.abs(theSpaces[y][x]) == 1) && (Math.abs( y - selectedSpace[0] ) == 2) )
		{
			
			// adjust lastMovePawnDoubleStep
			lastMovePawnDoubleStep[0] = y;
			lastMovePawnDoubleStep[1] = x;
			
		}
		else // if not a pawn double step
		{
			// clear lastMovePawnDoubleStep of coordinates
			lastMovePawnDoubleStep[0] = -1;
			lastMovePawnDoubleStep[1] = -1;
		}
		
		
		// if left castle
		if( theMove[6] == 1 ) 
		{
			theSpaces[ y ][ x + 1 ] = theSpaces[ y ][ 0 ]; // make rook at castle place
			theSpaces[ y ][ 0 ] = 0; // clear rook from its starting space
			
			// adjust hasMoved
			hasMoved[1 - (y / 7)][0] = true;
			hasMoved[1 - (y / 7)][2] = true;
			
			// append gameLog
			gameLog = gameLog + " Castle Left\n";
		}
		// if right castle
		if( theMove[6] == 2 ) 
		{
			theSpaces[ y ][ x - 1 ] = theSpaces[ y ][ 7 ]; // make rook at castle place
			theSpaces[ y ][ 7 ] = 0; // clear rook from its starting space
			
			// adjust hasMoved
			hasMoved[1 - (y / 7)][1] = true;
			hasMoved[1 - (y / 7)][2] = true;
			
			// append gameLog
			gameLog = gameLog + " Castle Right\n";
		}
		
		
		
		
		
		// if a pawn reaches the furthest row
		if( ( Math.abs( theSpaces[y][x] ) == 1 ) && (( y == 0 ) || ( y == 7 )) )
		{
			
			int side = 0;
			int side01 = 0;
			
			if( theSpaces[y][x] > 0 )
			{
				side = 1;
				side01 = 0;
			}
			else if( theSpaces[y][x] < 0 )
			{
				side = -1;
				side01 = 1;
			}
			
			
			
			String newPiece = "q";
			
			
			
			// a little AI to choose either queen or rook based on how good the board looks for the computer player
			int[] boardRating = rateBoard( theSpaces, hasMoved, lastMovePawnDoubleStep );
			
			int howGoodTheBoardLooksForTheComputerPlayer = boardRating[ side01 ] - boardRating[ 1 - side01 ];
			
			if( howGoodTheBoardLooksForTheComputerPlayer > -10 )
			{
				newPiece = "q";
			}
			else
			{
				newPiece = "r";
			}
			
			
			if( newPiece.charAt(0) == 'q' )
			{
				theSpaces[y][x] = 5 * side;
			}
			if( newPiece.charAt(0) == 'r' )
			{
				theSpaces[y][x] = 2 * side;
			}
			/*the computer player doesn't do bishops or knights for pawn replacements
			if( newPiece.charAt(0) == 'b' )
			{
				theSpaces[y][x] = 4 * side;
			}
			if( (newPiece.charAt(0) == 'k') || (newPiece.charAt(0) == 'n') )
			{
				theSpaces[y][x] = 3 * side;
			}
			*/
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		turn++; // increment the turn counter
		// this is a critical point
		
		
		
		
		
		
		
		
		
		
		createUserInterface(); // this is a big statement
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		int[][] kingLocations = {{7, 4}, {0, 4}}; // These values are to be changed.  They are to follow the format  [0][0]-white Y  [0][1]-white X  [1][0]-black Y  [1][1]-black X
		
		// scan for the kings
		for( int currentY = 0; currentY<8; currentY++ )
		{
			for( int currentX = 0; currentX<8; currentX++ )
			{
				if( theSpaces[currentY][currentX] == 6 )
				{
					kingLocations[0][0] = currentY;
					kingLocations[0][1] = currentX;
				}
				else if( theSpaces[currentY][currentX] == -6 )
				{
					kingLocations[1][0] = currentY;
					kingLocations[1][1] = currentX;
				}
			}
		}
		
		
		
		if( 
			spaceIsSafe( 
				theSpaces, 
				kingLocations[ turn % 2 ][1], 
				kingLocations[ turn % 2 ][0], 
				1 - (2 * ( turn % 2 ) ) 
			) == false
		)
		{
			
			
			if( numberOfPossibleMoves == 0 )
			{
				System.out.println( "Checkmate" );
				
				String[] whiteBlack = { "White", "Black" };
				gameLog = gameLog + "Checkmate\n" + whiteBlack[ 1 - (turn % 2) ] + " Wins\n";
				
				help = "The " + whiteBlack[ 1 - (turn % 2) ].toLowerCase() + "\nside wins.\nThere are\nno moves the\n" + whiteBlack[ turn % 2 ].toLowerCase() + " side\ncan make that\ngets the king\nout of check.\nClick the\nend game button\nto return to\nthe menu.";
				
				gameActivity = 2 + (1 - (turn % 2));
				
				
				
				quitJButton.setText( "End Game" );
				getContentPane().repaint();
				
				
				
				
			}
			else // if check
			{
				gameLog = gameLog + " Check\n";
				
				// say "White:" or "Black:" for the next move (this also occurs in another condition in this method)
				if( theSpaces[ y ][ x ] > 0 )
				{
					
					gameLog = gameLog + "Black: ";
					if( sides[1] == 0 )
					{
						help = "Black player,\nclick a piece.";
					}
				}
				else if( theSpaces[ y ][ x ] < 0 )
				{
					
					gameLog = gameLog + "White: ";
					if( sides[0] == 0 )
					{
						help = "White player,\nclick a piece.";
					}
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			}
			
			
		}
		else if( numberOfPossibleMoves == 0 )
		{
			gameLog = gameLog + "Stalemate\nIts a Draw";
			
			help = "Its a draw.\nThere are\nno moves\nthat can\nbe made\nbut the\nking is not\nin check.\nClick the\nend game button\nto return to\nthe menu.";
			
			if( gameActivity == 4 )
			{
				help = "Its a draw.\nOnly kings\nare left.\nCheckmate is\n impossible.";
			}
			else
			{
				gameActivity = 1;
			}
			
			quitJButton.setText( "End Game" );
			getContentPane().repaint();
		}
		else // if no check or stalemate
		{
			// say "White:" or "Black:" for the next move (this also occurs in another condition in this method)
			if( theSpaces[ y ][ x ] > 0 )
			{
				
				gameLog = gameLog + "Black: ";
				if( sides[1] == 0 )
				{
					help = "Black player,\nclick a piece.";
				}
				else
				{
					help = "The black\ncomputer\nplayer is going.";
				}
			}
			else if( theSpaces[ y ][ x ] < 0 )
			{
				
				gameLog = gameLog + "White: ";
				if( sides[0] == 0 )
				{
					help = "White player,\nclick a piece.";
				}
				else
				{
					help = "The white\ncomputer\nplayer is going.";
				}
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		}
		
		
		
		gameLogJTextArea.setText( gameLog );
		
		helpJTextArea.setText( help );
		helpJTextArea.setCaretPosition( 0 );

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	
	
	
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public chessComX()
	{
		createUserInterface();
	}
	
	
	public static void main( String[] args )
	{
		chessComX application = new chessComX();
		application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

}