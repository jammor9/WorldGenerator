package simulationInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;


//Create interface window
public class GridPanel extends JPanel {

    public interface GridListener {
        void gridReady();
        void onClick(int gridx, int gridy, int button);
    }

    public void setGridListener(GridListener gridListener) {
        this.gridListener = gridListener;
    }
 
    private GridListener gridListener;

    private static final int CELL_SIZE = 32;
    private static final Font FONT = new Font("Courier", Font.BOLD, 25);

    private int gridWidth;
    private int gridHeight;
    private int leftMargin;
    private int topMargin;
    private Map<Integer, BufferedImage> statesMap = new HashMap(); 
    private int[][] states;

    public GridPanel() {
        setBackground(Color.DARK_GRAY);

        addState(0, Color.ORANGE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                
                int gridx = (e.getX() - leftMargin) / CELL_SIZE;
                int gridy = (e.getY() - topMargin) / CELL_SIZE;

                if (gridListener != null) gridListener.onClick(gridx, gridy, e.getButton());
                super.mouseClicked(e);
            }
        });
    }

    public void update() {
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D)g;

        //Calculate grid width & height
        int width = getWidth();
        int height = getHeight();
        gridWidth = (width / CELL_SIZE) - 1;
        gridHeight = (height / CELL_SIZE) - 1;

        initCells(gridWidth, gridHeight);

        //Calculate extra space
        int xSpare = width - (gridWidth * CELL_SIZE);
        int ySpare = height - (gridHeight * CELL_SIZE);

        //Calculate margins
        leftMargin = xSpare / 2;
        topMargin = ySpare / 2;
        
        //Draw squares & background
        g2.setColor(Color.blue);
        g2.fillRect(leftMargin, topMargin, width + 1 - xSpare, height + 1 - ySpare);

        g2.setColor(Color.black);

        for (int gridy = 0; gridy < gridHeight; gridy++) {
            for (int gridx = 0; gridx < gridWidth; gridx++) {
                int x = gridx * CELL_SIZE + leftMargin;
                int y = gridy * CELL_SIZE + topMargin;

                Integer state = states[gridy][gridx];

                BufferedImage bi = statesMap.get(state);
                g2.drawImage(bi, x+1, y+1, null);
            }
        }
    }

    private void initCells(int gridWidth, int gridHeight) {
        if (states != null) return;

        states = new int[gridHeight][gridWidth];
        Arrays.stream(states).forEach(a -> Arrays.fill(a, 0));

        if (gridListener != null) {
            gridListener.gridReady();
        }
    }

    public void addState(Integer state, Color bg) {
        addState(state, Color.WHITE, bg, "");
    }

    public void addState(Integer state, Color fg, Color bg, String character) {
        BufferedImage bi = new BufferedImage(CELL_SIZE-1, CELL_SIZE-1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(bg);
        g.fillRect(0, 0, CELL_SIZE-1, CELL_SIZE-1);
        
        if (character.length() != 0) {
            g.setColor(fg);
            g.setFont(FONT);

            FontRenderContext frc = g.getFontRenderContext();
            TextLayout tl = new TextLayout(character, FONT,  frc);
            Rectangle2D bounds = tl.getBounds();

            float x = CELL_SIZE/2 - (float)bounds.getCenterX();
            float y = CELL_SIZE/2 - (float)bounds.getCenterY();

            tl.draw(g, x, y);
        }

        g.dispose();

        statesMap.put(state, bi);
    }

    public void setCell(int state, int x, int y) {
        if (x < 0 || y < 0 || x >= gridWidth || y >= gridHeight ) return;

        states[y][x] = state;
    }

    public int getCell(int x, int y) {
        if (x < 0 || y < 0 || x >= gridWidth || y >= gridHeight ) return -1;

        return states[y][x];
    }
}
