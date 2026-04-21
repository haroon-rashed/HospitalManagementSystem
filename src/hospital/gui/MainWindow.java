package hospital.gui;

import hospital.util.UITheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {

    private final JPanel content;
    private DashboardPanel   dashboard;
    private PatientPanel     patients;
    private DoctorPanel      doctors;
    private AppointmentPanel appointments;
    private JPanel activeNav = null;

    public MainWindow() {
        setTitle("MediCare HMS — Hospital Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 760);
        setMinimumSize(new Dimension(1050, 620));
        setLocationRelativeTo(null);

        JPanel bg = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(UITheme.BG_DARK); g2.fillRect(0,0,getWidth(),getHeight());
                g2.setColor(new Color(255,255,255,5));
                for(int x=0;x<getWidth();x+=40) g2.drawLine(x,0,x,getHeight());
                for(int y=0;y<getHeight();y+=40) g2.drawLine(0,y,getWidth(),y);
                g2.dispose();
            }
        };
        setContentPane(bg);

        content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        bg.add(buildSidebar(), BorderLayout.WEST);
        bg.add(content, BorderLayout.CENTER);

        showPanel("dashboard");
        setVisible(true);
    }

    private JPanel buildSidebar() {
        JPanel sb = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setColor(UITheme.BG_SIDEBAR); g2.fillRect(0,0,getWidth(),getHeight());
                g2.setColor(UITheme.BORDER); g2.drawLine(getWidth()-1,0,getWidth()-1,getHeight());
                GradientPaint gp=new GradientPaint(0,0,new Color(212,175,55,55),0,110,new Color(0,0,0,0));
                g2.setPaint(gp); g2.fillRect(0,0,getWidth(),110); g2.dispose();
            }
        };
        sb.setPreferredSize(new Dimension(225,0));
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));

        sb.add(buildLogoPanel());
        sb.add(makeDivider());
        sb.add(sectionLabel("MAIN MENU"));

        NavButton nav1 = createNavBtn("Dashboard",    "dashboard");
        NavButton nav2 = createNavBtn("Patients",     "patients");
        NavButton nav3 = createNavBtn("Doctors",      "doctors");
        NavButton nav4 = createNavBtn("Appointments", "appointments");

        sb.add(nav1); sb.add(nav2); sb.add(nav3); sb.add(nav4);
        sb.add(makeDivider());
        sb.add(sectionLabel("SYSTEM"));

        NavButton nav5 = createNavBtn("About", "about");
        sb.add(nav5);
        sb.add(Box.createVerticalGlue());

        JLabel ver = UITheme.label("  MediCare HMS  v1.0", UITheme.F_SMALL, UITheme.TEXT_MUTED);
        ver.setBorder(BorderFactory.createEmptyBorder(8,14,10,8));
        sb.add(ver);

        // Activate first nav by default
        setNavActive(nav1);

        return sb;
    }

    private JPanel buildLogoPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(225,95));
        p.setMaximumSize(new Dimension(225,95));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner,BoxLayout.Y_AXIS));

        JPanel cross = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(212,175,55,50)); g2.fillOval(2,2,38,38);
                g2.setColor(UITheme.GOLD);
                g2.setStroke(new BasicStroke(3.5f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                g2.drawLine(21,9,21,33); g2.drawLine(9,21,33,21); g2.dispose();
            }
        };
        cross.setOpaque(false);
        cross.setPreferredSize(new Dimension(42,42));
        cross.setMaximumSize(new Dimension(42,42));
        cross.setAlignmentX(CENTER_ALIGNMENT);

        JLabel name = UITheme.label("MediCare", new Font("SansSerif",Font.BOLD,19), UITheme.GOLD);
        name.setAlignmentX(CENTER_ALIGNMENT);
        JLabel sub  = UITheme.label("Hospital Management", UITheme.F_SMALL, UITheme.TEXT_MUTED);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        inner.add(cross); inner.add(Box.createVerticalStrut(5));
        inner.add(name);  inner.add(sub);
        p.add(inner);
        return p;
    }

    private NavButton createNavBtn(String label, String key) {
        NavButton btn = new NavButton(label);
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                setNavActive(btn);
                showPanel(key);
            }
        });
        return btn;
    }

    private void setNavActive(NavButton btn) {
        if (activeNav instanceof NavButton) {
            ((NavButton) activeNav).setActive(false);
        }
        activeNav = btn;
        btn.setActive(true);
    }

    // ── Clean inner NavButton class ───────────────────────────────────────────
    private static class NavButton extends JPanel {
        private final String label;
        private boolean active  = false;
        private boolean hovered = false;

        NavButton(String label) {
            this.label = label;
            setOpaque(false);
            setPreferredSize(new Dimension(225,43));
            setMaximumSize(new Dimension(225,43));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered=true;  repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hovered=false; repaint(); }
            });
        }

        void setActive(boolean a) { this.active = a; repaint(); }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            if(active) {
                g2.setColor(new Color(212,175,55,22));
                g2.fillRoundRect(8,2,getWidth()-16,getHeight()-4,10,10);
                g2.setColor(UITheme.GOLD);
                g2.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                g2.drawLine(3,7,3,getHeight()-7);
            } else if(hovered) {
                g2.setColor(new Color(255,255,255,8));
                g2.fillRoundRect(8,2,getWidth()-16,getHeight()-4,10,10);
            }
            g2.setFont(active ? new Font("SansSerif",Font.BOLD,13) : UITheme.F_NAV);
            g2.setColor(active ? UITheme.GOLD : UITheme.TEXT_SECONDARY);
            g2.drawString(label, 22, getHeight()/2+5);
            g2.dispose();
        }
    }

    private JPanel sectionLabel(String txt) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT,18,0));
        p.setOpaque(false); p.setMaximumSize(new Dimension(225,26));
        p.add(UITheme.label(txt, new Font("SansSerif",Font.BOLD,10), UITheme.TEXT_MUTED));
        return p;
    }

    private JSeparator makeDivider() {
        JSeparator s = new JSeparator();
        s.setForeground(UITheme.BORDER); s.setBackground(UITheme.BG_SIDEBAR);
        s.setMaximumSize(new Dimension(225,1)); return s;
    }

    private void showPanel(String key) {
        content.removeAll();
        switch(key) {
            case "dashboard":
                if(dashboard==null) dashboard=new DashboardPanel(); else dashboard.refreshStats();
                content.add(dashboard, BorderLayout.CENTER); break;
            case "patients":
                if(patients==null) patients=new PatientPanel();
                content.add(patients, BorderLayout.CENTER); break;
            case "doctors":
                if(doctors==null) doctors=new DoctorPanel();
                content.add(doctors, BorderLayout.CENTER); break;
            case "appointments":
                if(appointments==null) appointments=new AppointmentPanel();
                content.add(appointments, BorderLayout.CENTER); break;
            case "about":
                content.add(buildAboutPanel(), BorderLayout.CENTER); break;
            default: break;
        }
        content.revalidate();
        content.repaint();
    }

    private JPanel buildAboutPanel() {
        JPanel bg = new JPanel(new GridBagLayout()); bg.setOpaque(false);
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.BG_CARD); g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                GradientPaint gp=new GradientPaint(0,0,new Color(212,175,55,12),0,getHeight(),new Color(0,0,0,0));
                g2.setPaint(gp); g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                g2.setColor(UITheme.BORDER); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,20,20);
                g2.dispose();
            }
        };
        card.setOpaque(false); card.setPreferredSize(new Dimension(480,390));
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(32,38,32,38));

        JPanel cross = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(212,175,55,40)); g2.fillOval(0,0,58,58);
                g2.setColor(UITheme.GOLD);
                g2.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                g2.drawLine(29,8,29,50); g2.drawLine(8,29,50,29); g2.dispose();
            }
        };
        cross.setOpaque(false); cross.setPreferredSize(new Dimension(58,58));
        cross.setMaximumSize(new Dimension(58,58)); cross.setAlignmentX(CENTER_ALIGNMENT);

        JLabel t1=UITheme.label("MediCare HMS",new Font("SansSerif",Font.BOLD,26),UITheme.GOLD);
        t1.setAlignmentX(CENTER_ALIGNMENT);
        JLabel t2=UITheme.label("Hospital Management System  v1.0",UITheme.F_SUB,UITheme.TEXT_SECONDARY);
        t2.setAlignmentX(CENTER_ALIGNMENT);

        JSeparator sep=new JSeparator(); sep.setForeground(UITheme.BORDER);
        sep.setMaximumSize(new Dimension(400,1));

        JPanel grid=new JPanel(new GridLayout(6,2,10,8));
        grid.setOpaque(false); grid.setMaximumSize(new Dimension(420,210));
        String[][] info={
            {"OOP Concepts","Encapsulation, Inheritance, Polymorphism"},
            {"Overriding","getRole(), getDisplayInfo(), toString()"},
            {"Overloading","Constructors & search methods"},
            {"Abstraction","Abstract class: Person"},
            {"GUI Framework","Java Swing (AWT)"},
            {"Language","Java SE 11+"}
        };
        for(String[] r:info){
            grid.add(UITheme.label(r[0]+":",UITheme.F_LABEL,UITheme.TEXT_SECONDARY));
            grid.add(UITheme.label(r[1],UITheme.F_BODY,UITheme.TEXT_PRIMARY));
        }

        JLabel copy=UITheme.label("© 2025 MediCare — OOP Project",UITheme.F_SMALL,UITheme.TEXT_MUTED);
        copy.setAlignmentX(CENTER_ALIGNMENT);

        card.add(cross); card.add(Box.createVerticalStrut(10));
        card.add(t1); card.add(Box.createVerticalStrut(4));
        card.add(t2); card.add(Box.createVerticalStrut(14));
        card.add(sep); card.add(Box.createVerticalStrut(14));
        card.add(grid); card.add(Box.createVerticalGlue());
        card.add(copy);

        bg.add(card); return bg;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("OptionPane.background",        UITheme.BG_CARD);
            UIManager.put("Panel.background",             UITheme.BG_CARD);
            UIManager.put("OptionPane.messageForeground", UITheme.TEXT_PRIMARY);
            UIManager.put("Button.background",            UITheme.BLUE);
            UIManager.put("Button.foreground",            Color.WHITE);
            UIManager.put("ComboBox.background",          UITheme.BG_CARD);
            UIManager.put("ComboBox.foreground",          UITheme.TEXT_PRIMARY);
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
