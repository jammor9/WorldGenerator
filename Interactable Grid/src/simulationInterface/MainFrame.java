package simulationInterface;

import java.awt.Color;

import javax.swing.JFrame;


//Create interface window
public class MainFrame extends JFrame{
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    public MainFrame() {
        super("Test");

        GridPanel gridPanel = new GridPanel();

        gridPanel.addState(1, Color.BLUE);
        gridPanel.addState(2, Color.WHITE, Color.BLACK, "?");
        gridPanel.addState(3, Color.WHITE, Color.RED, "*");

        gridPanel.setGridListener(new GridPanel.GridListener() {

            @Override
            public void gridReady() {
                gridPanel.setCell(1, 1, 1);
                gridPanel.setCell(2, 3, 4);
            }

            @Override
            public void onClick(int gridx, int gridy, int button) {
                gridPanel.setCell(3, gridx, gridy);
                gridPanel.update();
            }
            
        });

        setContentPane(gridPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
    }
}
