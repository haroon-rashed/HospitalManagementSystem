package hospital.gui;

import hospital.service.HospitalService;
import hospital.util.UITheme;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardPanel extends JPanel {

    private final HospitalService svc;
    private JLabel lPatients, lDoctors, lAppts, lAdmitted, clockLbl;

    public DashboardPanel() {
        svc = HospitalService.getInstance();
        setOpaque(false);
        setLayout(new BorderLayout(0,18));
        setBorder(BorderFactory.createEmptyBorder(22,22,22,22));
        build();
        startClock();
    }

    private void build() {
        // Header
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        JPanel titleBlk = new JPanel(new GridLayout(2,1,0,3));
        titleBlk.setOpaque(false);
        titleBlk.add(UITheme.label("Dashboard Overview", UITheme.F_TITLE, UITheme.TEXT_PRIMARY));
        titleBlk.add(UITheme.label("MediCare Hospital Management System", UITheme.F_BODY, UITheme.TEXT_SECONDARY));
        clockLbl = UITheme.label("", UITheme.F_LABEL, UITheme.GOLD);
        clockLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        hdr.add(titleBlk, BorderLayout.WEST);
        hdr.add(clockLbl,  BorderLayout.EAST);
        add(hdr, BorderLayout.NORTH);

        // Centre
        JPanel centre = new JPanel();
        centre.setOpaque(false);
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));

        // Stat cards
        JPanel cards = new JPanel(new GridLayout(1,4,14,0));
        cards.setOpaque(false);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE,120));
        lPatients = new JLabel("0"); lDoctors = new JLabel("0");
        lAppts    = new JLabel("0"); lAdmitted = new JLabel("0");
        cards.add(statCard("Total Patients",  lPatients, UITheme.BLUE));
        cards.add(statCard("Doctors on Staff",lDoctors,  UITheme.GREEN));
        cards.add(statCard("Appointments",    lAppts,    UITheme.GOLD));
        cards.add(statCard("Admitted",        lAdmitted, UITheme.RED));
        centre.add(cards);
        centre.add(Box.createVerticalStrut(16));

        // Lower row
        JPanel lower = new JPanel(new GridLayout(1,2,14,0));
        lower.setOpaque(false);
        lower.setMaximumSize(new Dimension(Integer.MAX_VALUE,290));
        lower.add(recentPanel());
        lower.add(deptPanel());
        centre.add(lower);
        centre.add(Box.createVerticalStrut(14));
        centre.add(infoBanner());

        add(centre, BorderLayout.CENTER);
        refreshStats();
    }

    private JPanel statCard(String label, JLabel valLbl, Color accent) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.BG_CARD); g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                g2.setColor(accent); g2.fillRoundRect(0,0,5,getHeight(),5,5);
                GradientPaint gp=new GradientPaint(0,0,new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),18),getWidth(),0,new Color(0,0,0,0));
                g2.setPaint(gp); g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                g2.dispose();
            }
        };
        p.setOpaque(false); p.setLayout(new BorderLayout(8,4));
        p.setBorder(BorderFactory.createEmptyBorder(16,18,16,18));
        valLbl.setFont(UITheme.F_BIGNUM); valLbl.setForeground(accent);
        JLabel lbl=UITheme.label(label, UITheme.F_SMALL, UITheme.TEXT_SECONDARY);
        JPanel right=new JPanel(new GridLayout(2,1,0,2)); right.setOpaque(false);
        right.add(valLbl); right.add(lbl);
        p.add(right, BorderLayout.CENTER);
        return p;
    }

    private JPanel recentPanel() {
        JPanel p = cardPanel();
        p.setLayout(new BorderLayout(0,10));
        p.add(UITheme.label("Recent Patients", UITheme.F_SUB, UITheme.TEXT_PRIMARY), BorderLayout.NORTH);
        JPanel list=new JPanel(); list.setOpaque(false); list.setLayout(new BoxLayout(list,BoxLayout.Y_AXIS));
        String[][] rows={{"Muhammad Zain","Cardiology","Admitted"},{"Aisha Noor","Neurology","Admitted"},
                         {"Zara Ali","Pediatrics","OPD"},{"Tariq Mehmood","Surgery","Admitted"}};
        Color[] clrs={UITheme.RED,UITheme.RED,UITheme.BLUE,UITheme.RED};
        for(int i=0;i<rows.length;i++){
            list.add(recentRow(rows[i][0],rows[i][1],rows[i][2],clrs[i]));
            list.add(Box.createVerticalStrut(4));
        }
        p.add(list, BorderLayout.CENTER);
        return p;
    }

    private JPanel recentRow(String name, String dept, String status, Color sc) {
        JPanel row=new JPanel(new BorderLayout(8,0));
        row.setOpaque(false); row.setMaximumSize(new Dimension(Integer.MAX_VALUE,36));
        // avatar
        JPanel av=new JPanel(){ @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(UITheme.BLUE); g2.fillOval(0,0,getWidth(),getHeight());
            g2.setColor(Color.WHITE); g2.setFont(UITheme.F_LABEL);
            String in=name.substring(0,1); FontMetrics fm=g2.getFontMetrics();
            g2.drawString(in,(getWidth()-fm.stringWidth(in))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);
            g2.dispose();
        }};
        av.setOpaque(false); av.setPreferredSize(new Dimension(28,28));
        JPanel txt=new JPanel(new GridLayout(1,2)); txt.setOpaque(false);
        txt.add(UITheme.label(name, UITheme.F_LABEL, UITheme.TEXT_PRIMARY));
        txt.add(UITheme.label(dept, UITheme.F_SMALL, UITheme.TEXT_SECONDARY));
        JLabel badge=badge(status,sc);
        row.add(av,BorderLayout.WEST); row.add(txt,BorderLayout.CENTER); row.add(badge,BorderLayout.EAST);
        return row;
    }

    private JLabel badge(String text, Color c) {
        JLabel l=new JLabel(text){ @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),35)); g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
            g2.setColor(c); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10); g2.dispose(); super.paintComponent(g);
        }};
        l.setFont(UITheme.F_SMALL); l.setForeground(c); l.setOpaque(false);
        l.setBorder(BorderFactory.createEmptyBorder(2,8,2,8));
        l.setHorizontalAlignment(SwingConstants.CENTER);
        return l;
    }

    private JPanel deptPanel() {
        JPanel p=cardPanel(); p.setLayout(new BorderLayout(0,10));
        p.add(UITheme.label("Departments", UITheme.F_SUB, UITheme.TEXT_PRIMARY), BorderLayout.NORTH);
        JPanel list=new JPanel(); list.setOpaque(false); list.setLayout(new BoxLayout(list,BoxLayout.Y_AXIS));
        Object[][] depts={{"Cardiology","3 Doctors","12 Patients",UITheme.RED},
                          {"Neurology","2 Doctors","8 Patients",UITheme.PURPLE},
                          {"Orthopedics","2 Doctors","6 Patients",UITheme.ORANGE},
                          {"Pediatrics","3 Doctors","10 Patients",UITheme.BLUE},
                          {"Surgery","4 Doctors","15 Patients",UITheme.GREEN}};
        for(Object[] d:depts){
            list.add(deptRow((String)d[0],(String)d[1],(String)d[2],(Color)d[3]));
            list.add(Box.createVerticalStrut(5));
        }
        p.add(list, BorderLayout.CENTER);
        return p;
    }

    private JPanel deptRow(String name, String docs, String pts, Color c) {
        JPanel row=new JPanel(new BorderLayout(8,0));
        row.setOpaque(false); row.setMaximumSize(new Dimension(Integer.MAX_VALUE,36));
        JPanel dot=new JPanel(){ @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.fillOval(3,6,13,13); g2.dispose();
        }};
        dot.setOpaque(false); dot.setPreferredSize(new Dimension(22,26));
        JPanel info=new JPanel(new GridLayout(2,1)); info.setOpaque(false);
        info.add(UITheme.label(name, UITheme.F_LABEL, UITheme.TEXT_PRIMARY));
        info.add(UITheme.label(docs+" • "+pts, UITheme.F_SMALL, UITheme.TEXT_SECONDARY));
        row.add(dot, BorderLayout.WEST); row.add(info, BorderLayout.CENTER);
        return row;
    }

    private JPanel infoBanner() {
        JPanel p=new JPanel(new GridLayout(1,3,10,0)){ @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp=new GradientPaint(0,0,new Color(41,182,246,25),getWidth(),0,new Color(212,175,55,25));
            g2.setPaint(gp); g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
            g2.setColor(UITheme.BORDER); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
            g2.dispose();
        }};
        p.setOpaque(false); p.setMaximumSize(new Dimension(Integer.MAX_VALUE,55));
        p.setBorder(BorderFactory.createEmptyBorder(8,18,8,18));
        p.add(infoItem("MediCare General Hospital"));
        p.add(infoItem("Islamabad, Pakistan"));
        p.add(infoItem("Emergency: 1122  |  Reception: 0300-9505112"));
        return p;
    }

    private JPanel infoItem(String txt) {
        JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER,5,0)); p.setOpaque(false);
        p.add(UITheme.label(txt, UITheme.F_SMALL, UITheme.TEXT_SECONDARY));
        return p;
    }

    private JPanel cardPanel() {
        JPanel p=new JPanel(){ @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(UITheme.BG_CARD); g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
            g2.dispose();
        }};
        p.setOpaque(false); p.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        return p;
    }

    public void refreshStats() {
        lPatients.setText(String.valueOf(svc.getTotalPatients()));
        lDoctors .setText(String.valueOf(svc.getTotalDoctors()));
        lAppts   .setText(String.valueOf(svc.getTotalAppointments()));
        lAdmitted.setText(String.valueOf(svc.getAdmittedPatients()));
    }

    private void startClock() {
        new Timer(1000, e -> clockLbl.setText(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE dd MMM yyyy  |  hh:mm:ss a")))
        ).start();
    }
}
