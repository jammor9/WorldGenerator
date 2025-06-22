package simulationInterface;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.JFrame;


//Create interface window
public class MainFrame extends JFrame{
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    private TileState start = null;
    private TileState end = null;

    private GridPanel gridPanel;
    private Pathfinding pathfinding;

    public MainFrame() {
        super("A Star Test");

        this.gridPanel = new GridPanel();
        this.pathfinding = new Pathfinding();

        gridPanel.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                return;
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (start == null || end == null) System.out.println("Assign start and end point first!");
                    pathfinding.findPath(gridPanel, start, end);
                }
                else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    System.out.println("Generating maze...");
                    MazeGenerator mazeGenerator = new MazeGenerator(gridPanel, gridPanel.getGridWidth(), gridPanel.getGridHeight());
                    mazeGenerator.generateMaze(gridPanel);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                return;
            }
            
        });

        gridPanel.addState(TileType.CLOSED, Color.BLACK);
        gridPanel.addState(TileType.START, Color.BLUE);
        gridPanel.addState(TileType.END, Color.ORANGE);
        gridPanel.addState(TileType.PATH, Color.CYAN);

        gridPanel.setGridListener(new GridPanel.GridListener() {
            @Override
            public void onClick(int gridx, int gridy, int button) {

                TileState tileState = gridPanel.getCell(gridx, gridy);
                if (tileState == null) return;


                TileType tileType = tileState.getTileType();

                if (button == 1) {
                    if (tileType == tileType.OPEN)  gridPanel.setCell(TileType.CLOSED, gridx, gridy);
                    else if (tileType == tileType.CLOSED) gridPanel.setCell(TileType.OPEN, gridx, gridy);
                }
                else if (button == 2) {
                    if (start != null) {
                        int[] coords = start.getCoord();
                        gridPanel.setCell(TileType.OPEN, coords[1], coords[0]);
                    }
                    gridPanel.setCell(TileType.START, gridx, gridy);
                    start = gridPanel.getCell(gridx, gridy);
                }
                else if (button == 3){
                    if (end != null) {
                        int[] coords = end.getCoord();
                        gridPanel.setCell(TileType.OPEN, coords[1], coords[0]);
                    }
                    gridPanel.setCell(TileType.END, gridx, gridy);
                    end = gridPanel.getCell(gridx, gridy);
                }

                gridPanel.update();
                    System.out.println(tileState);

                return;
            }

            @Override
            public void gridReady() {
                // TODO Auto-generated method stub
                return;
            }
            
        });

        setContentPane(gridPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
    }

    public GridPanel getGridPanel() {
        return this.gridPanel;
    }
}
