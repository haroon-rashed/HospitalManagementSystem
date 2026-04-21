package hospital.util;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * UITheme - Centralized dark luxury medical theme for all Swing components
 */
public class UITheme {

    // ── Colors ──────────────────────────────────────────────────────────────
    public static final Color BG_DARK       = new Color(10,  14,  26);
    public static final Color BG_PANEL      = new Color(16,  22,  40);
    public static final Color BG_CARD       = new Color(22,  32,  56);
    public static final Color BG_SIDEBAR    = new Color(13,  18,  33);

    public static final Color GOLD          = new Color(212, 175, 55);
    public static final Color BLUE          = new Color(41,  182, 246);
    public static final Color GREEN         = new Color(72,  199, 142);
    public static final Color RED           = new Color(252, 100, 100);
    public static final Color PURPLE        = new Color(155, 89,  182);
    public static final Color ORANGE        = new Color(255, 165, 50);

    public static final Color TEXT_PRIMARY  = new Color(240, 244, 255);
    public static final Color TEXT_SECONDARY= new Color(160, 175, 200);
    public static final Color TEXT_MUTED    = new Color(100, 115, 145);
    public static final Color BORDER        = new Color(40,  55,  90);
    public static final Color ROW_ALT       = new Color(19,  27,  48);

    // ── Fonts ────────────────────────────────────────────────────────────────
    public static final Font F_TITLE   = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font F_SUB     = new Font("Segoe UI", Font.BOLD,  15);
    public static final Font F_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font F_SMALL   = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font F_LABEL   = new Font("Segoe UI", Font.BOLD,  12);
    public static final Font F_BUTTON  = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font F_NAV     = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font F_BIGNUM  = new Font("Segoe UI", Font.BOLD,  34);

    // ── Factory methods ──────────────────────────────────────────────────────

    public static JLabel label(String text, Font f, Color c) {
        JLabel l = new JLabel(text);
        l.setFont(f); l.setForeground(c);
        return l;
    }

    public static JTextField textField() {
        JTextField f = new JTextField();
        f.setFont(F_BODY);
        f.setBackground(BG_CARD);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(GOLD);
        f.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(BORDER, 8),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return f;
    }

    public static JComboBox<String> comboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(F_BODY);
        cb.setBackground(BG_CARD);
        cb.setForeground(TEXT_PRIMARY);
        cb.setBorder(new RoundBorder(BORDER, 8));
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> list, Object val, int idx, boolean sel, boolean focus) {
                super.getListCellRendererComponent(list, val, idx, sel, focus);
                setBackground(sel ? BLUE : BG_CARD);
                setForeground(TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
                return this;
            }
        });
        return cb;
    }

    public static JButton button(String text, Color bg) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isPressed() ? bg.darker().darker()
                        : getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        b.setFont(F_BUTTON); b.setForeground(Color.WHITE); b.setBackground(bg);
        b.setBorderPainted(false); b.setFocusPainted(false); b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(130, 36));
        return b;
    }

    public static void styleTable(JTable t) {
        t.setBackground(BG_PANEL);
        t.setForeground(TEXT_PRIMARY);
        t.setFont(F_BODY);
        t.setRowHeight(30);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(new Color(41, 100, 160));
        t.setSelectionForeground(Color.WHITE);

        t.getTableHeader().setBackground(BG_SIDEBAR);
        t.getTableHeader().setForeground(GOLD);
        t.getTableHeader().setFont(F_LABEL);
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,1,0, BORDER));
        t.getTableHeader().setReorderingAllowed(false);

        t.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable tbl, Object val, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(tbl, val, sel, focus, row, col);
                if (sel) { setBackground(new Color(41,100,160)); setForeground(Color.WHITE); }
                else     { setBackground(row%2==0 ? BG_PANEL : ROW_ALT); setForeground(TEXT_PRIMARY); }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setFont(F_BODY);
                return this;
            }
        });
    }

    public static void styleScrollPane(JScrollPane sp) {
        sp.setBorder(BorderFactory.createLineBorder(BORDER));
        sp.getViewport().setBackground(BG_PANEL);
        sp.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = new Color(60,80,120); trackColor = BG_SIDEBAR;
            }
            @Override protected JButton createDecreaseButton(int o) { return zeroBtn(); }
            @Override protected JButton createIncreaseButton(int o) { return zeroBtn(); }
            private JButton zeroBtn() {
                JButton b=new JButton(); b.setPreferredSize(new Dimension(0,0)); return b;
            }
        });
    }

    // ── Inner border ─────────────────────────────────────────────────────────
    public static class RoundBorder extends AbstractBorder {
        private final Color color; private final int r;
        public RoundBorder(Color color, int r) { this.color=color; this.r=r; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color); g2.drawRoundRect(x,y,w-1,h-1,r,r); g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(2,2,2,2); }
    }
}
