import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class CoolComponents {
    //some of this code was taken from the internet because as a wise man once said "Why do something when someone has already done it". I will still explain what the code does
    static class RoundedPanel extends JPanel
    {

        protected int strokeSize = 1;

        protected Color shadowColor = Color.black;

        protected boolean shady = true;
        protected boolean highQuality = true;
        //this instance variable is important. it sets how rounded it will be
        protected Dimension arcs = new Dimension(20, 20);
        protected int shadowGap = 5;
        protected int shadowOffset = 4;
        protected int shadowAlpha = 150;
        //i made this constructor which takes in the only useful instance variable in my opinion
        RoundedPanel(int roundAmount){
            //setting opaque to false because things get messed up if i dont
            setOpaque(false);
            arcs.height = roundAmount;
            arcs.width = roundAmount;
        }

        //this method overrides the method from the JPanel class which paints the component,
        @Override
        protected void paintComponent(Graphics g) {
            //call JPanels paintComponet method
            super.paintComponent(g);
            int width = getWidth();
            int height = getHeight();
            int shadowGap = this.shadowGap;
            Color shadowColorA = new Color(shadowColor.getRed(),
                    shadowColor.getGreen(), shadowColor.getBlue(), shadowAlpha);
            Graphics2D graphics = (Graphics2D) g;


            if (highQuality) {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
            }

            //this draws shadows
            if (shady) {
                graphics.setColor(shadowColorA);
                graphics.fillRoundRect(
                        shadowOffset,// X position
                        shadowOffset,// Y position
                        width - strokeSize - shadowOffset, // width
                        height - strokeSize - shadowOffset, // height
                        arcs.width, arcs.height);// arc Dimension
            } else {
                shadowGap = 1;
            }

            //draw an arc in parts of the JLabel to give it that rounded look
            graphics.setColor(getBackground());
            graphics.fillRoundRect(0, 0, width - shadowGap,
                    height - shadowGap, arcs.width, arcs.height);
            graphics.setColor(getForeground());
            graphics.setStroke(new BasicStroke(strokeSize));
            graphics.drawRoundRect(0, 0, width - shadowGap,
                    height - shadowGap, arcs.width, arcs.height);

            graphics.setStroke(new BasicStroke());
        }

    }




    public static class RoundJTextField extends JTextField {
        private Shape shape;
        private Color backgroundColor = new Color(0);
        private int cornerRadius;
        public RoundJTextField(int cornerRadius) {
            this.cornerRadius = cornerRadius;
            setOpaque(false);
            backgroundColor = Color.WHITE;

        }
        @Override
        protected void paintComponent(Graphics g) {
            //same idea as the previous one
            Graphics2D graphics = (Graphics2D) g;

            graphics.setColor(backgroundColor);

            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //draws rounded rectangle with the appropriate arc size
            graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height); //paint background
            graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height); //paint border
            super.paintComponent(g);

        }
        protected void paintBorder(Graphics g) {
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            g.setColor(getForeground());
            g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, arcs.width, arcs.height);
        }
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
            return shape.contains(x, y);
        }
    }
    public static class RoundPasswordField extends JPasswordField {
        private Shape shape;
        private int cornerRadius;
        public RoundPasswordField(int cornerRadius) {
            setOpaque(false);
            this.cornerRadius = cornerRadius;
        }
        protected void paintComponent(Graphics g) {
            //same idea
            Graphics2D graphics = (Graphics2D) g;

            graphics.setColor(getBackground());

            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


            graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height); //paint background
            super.paintComponent(g);

        }
        protected void paintBorder(Graphics g) {
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            g.setColor(getForeground());
            g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, arcs.width, arcs.height);
        }
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
            return shape.contains(x, y);
        }
    }


}