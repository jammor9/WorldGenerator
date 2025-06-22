import javax.swing.SwingUtilities;

import simulationInterface.MainFrame;
import simulationInterface.MazeGenerator;

//Following Cave of Programming tutorial on his youtube channel

public class App {
    public static void main(String[] args) throws Exception {
        createInterface();
    }

    public static void createInterface() {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
